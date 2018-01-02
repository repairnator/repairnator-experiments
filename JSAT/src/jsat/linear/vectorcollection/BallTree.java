/*
 * Copyright (C) 2018 Edward Raff
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package jsat.linear.vectorcollection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jsat.clustering.PAM;
import jsat.clustering.TRIKMEDS;
import jsat.linear.DenseVector;
import jsat.linear.IndexValue;
import jsat.linear.Vec;
import jsat.linear.distancemetrics.DistanceMetric;
import jsat.linear.distancemetrics.EuclideanDistance;
import jsat.utils.BoundedSortedList;
import jsat.utils.DoubleList;
import jsat.utils.IndexTable;
import jsat.utils.IntList;
import jsat.utils.concurrent.AtomicDoubleArray;
import jsat.utils.concurrent.ParallelUtils;

/**
 *
 * @author Edward Raff
 * @param <V>
 */
public class BallTree<V extends Vec> implements IncrementalCollection<V>
{
    public static final int DEFAULT_LEAF_SIZE = 40;
    private int leaf_size = DEFAULT_LEAF_SIZE;
    private DistanceMetric dm;
    private List<V> allVecs;
    private List<Double> cache;
    private ConstructionMethod construction_method;
    private PivotSelection pivot_method;
    private Node root;
    
    public enum ConstructionMethod
    {
        /**
         * This represents a top-down construction approach, that can be used
         * for any distance metric. At each branch the children are given
         * initial prototypes. The left child has a prototype selected as the
         * point farthest from the pivot, and the right child the point farthest
         * from the left's. The points are split based on which prototype thye
         * are closest too. The process continues recursively. <br>
         *
         * See: Moore, A. W. (2000). The Anchors Hierarchy: Using the Triangle
         * Inequality to Survive High Dimensional Data. In Proceedings of the
         * Sixteenth Conference on Uncertainty in Artificial Intelligence (pp.
         * 397–405). San Francisco, CA, USA: Morgan Kaufmann Publishers Inc.
         * Retrieved from http://dl.acm.org/citation.cfm?id=2073946.2073993 for
         * details.
         */
        TOP_DOWN_FARTHEST,
        /**
         * This represents a top-down construction approach similar to a
         * KD-tree's construction. It requires a metric where it has access to
         * meaningful feature values. At each node, the dimension with the
         * largest spread in values is selected. Then the split is made based on
         * sorting the found feature into two even halves.<br>
         * See: Omohundro, S. M. (1989). Five Balltree Construction Algorithms
         * (No. TR-89-063).
         */
        KD_STYLE;
    }
    
    public enum PivotSelection
    {
        /**
         * This method selects the pivot by taking the centroid (average) of all
         * the data within a node. This method may not be applicable for all
         * metrics, and can't be used for once for which there is no computable
         * average.
         */
        CENTROID 
        {
            @Override
            public Vec getPivot(boolean parallel, List<Integer> points, List<? extends Vec> data,  DistanceMetric dm, List<Double> cache)
            {
                if (points.size() == 1)
                    return data.get(points.get(0)).clone();
                
                Vec pivot = new DenseVector(data.get(points.get(0)).length());
                for (int i : points)
                    pivot.mutableAdd(data.get(i));
                pivot.mutableDivide(points.size());
                return pivot;
                
            }
        },
        /**
         * This method selects the pivot by searching for the medoid of the
         * data. This can be used in all circumstances, but may be slower.
         */
        MEDOID 
        {
            @Override
            public Vec getPivot(boolean parallel, List<Integer> points, List<? extends Vec> data,  DistanceMetric dm, List<Double> cache)
            {
                if (points.size() == 1)
                    return data.get(points.get(0)).clone();
                int indx;
                if(dm.isValidMetric())
                    indx = TRIKMEDS.medoid(parallel, points, data, dm, cache);
                else
                    indx = PAM.medoid(parallel, points, data, dm, cache);
                return data.get(indx);
            }
        },;
        
        public abstract Vec getPivot(boolean parallel, List<Integer> points, List<? extends Vec> data, DistanceMetric dm, List<Double> cache);
    }

    public BallTree()
    {
        this(new EuclideanDistance(), ConstructionMethod.TOP_DOWN_FARTHEST, PivotSelection.CENTROID);
    }

    public BallTree(DistanceMetric dm, ConstructionMethod method, PivotSelection pivot_method)
    {
        setConstruction_method(method);
        setPivot_method(pivot_method);
        setDistanceMetric(dm);
    }

    /**
     * Copy constructor
     * @param toCopy the object to copy
     */
    public BallTree(BallTree toCopy)
    {
        this(toCopy.dm, toCopy.construction_method, toCopy.pivot_method);
        if(toCopy.allVecs != null)
            this.allVecs = new ArrayList<>(toCopy.allVecs);
        if(toCopy.cache != null)
            this.cache = new DoubleList(toCopy.cache);
        if(toCopy.root != null)
            this.root = cloneChangeContext(toCopy.root);
        this.leaf_size = toCopy.leaf_size;
    }
    
    public void setDistanceMetric(DistanceMetric dm)
    {
        this.dm = dm;
    }

    public DistanceMetric getDistanceMetric()
    {
        return dm;
    }

    public void setLeafSize(int leaf_size)
    {
        if(leaf_size < 2)
            throw new IllegalArgumentException("The leaf size must be >= 2 to support all splitting methods");
        this.leaf_size = leaf_size;
    }

    public int getLeafSize()
    {
        return leaf_size;
    }
    
    public void setPivot_method(PivotSelection pivot_method)
    {
        this.pivot_method = pivot_method;
    }

    public PivotSelection getPivot_method()
    {
        return pivot_method;
    }

    public void setConstruction_method(ConstructionMethod construction_method)
    {
        this.construction_method = construction_method;
    }

    public ConstructionMethod getConstruction_method()
    {
        return construction_method;
    }
    
    private Node build_far_top_down(List<Integer> points, boolean parallel)
    {
        Branch branch = new Branch();
        branch.setPivot(points);
        branch.setRadius(points);

        //Use point farthest from parent pivot for left child
        int f1 = ParallelUtils.streamP(points.stream(), parallel)
                .map(i->new IndexDistPair(i, dm.dist(i, branch.pivot, branch.pivot_qi, allVecs, cache)))
                .max(IndexDistPair::compareTo).orElse(new IndexDistPair(0, 0.0)).indx;
        //use point farhter from f1 for right child
        int f2 = ParallelUtils.streamP(points.stream(), parallel)
                .map(i->new IndexDistPair(i, dm.dist(i, f1, allVecs, cache)))
                .max(IndexDistPair::compareTo).orElse(new IndexDistPair(1, 0.0)).indx;

        //Now split children based on who is closes to f1 and f2
        IntList left_children = new IntList();
        IntList right_children = new IntList();
        for(int p : points)
        {
            double d_f1 = dm.dist(p, f1, allVecs, cache);
            double d_f2 = dm.dist(p, f2, allVecs, cache);
            if(d_f1 < d_f2)
                left_children.add(p);
            else
                right_children.add(p);
        }

        //everyone has been assigned, now creat children objects
        branch.left_child = build(left_children, parallel);
        branch.right_child = build(right_children, parallel);

        return branch;
    }
    
    private Node build_kd(List<Integer> points, boolean parallel)
    {
        //Lets find the dimension with the maximum spread
        int D = allVecs.get(0).length();
        AtomicDoubleArray mins = new AtomicDoubleArray(D);
        mins.fill(Double.POSITIVE_INFINITY);
        AtomicDoubleArray maxs = new AtomicDoubleArray(D);
        maxs.fill(Double.NEGATIVE_INFINITY);
        ParallelUtils.streamP(points.stream(), parallel).forEach(i->{
            for(IndexValue iv : get(i))
            {
                int d = iv.getIndex();
                mins.updateAndGet(d, (m_d)->Math.min(m_d, iv.getValue()));
                maxs.updateAndGet(d, (m_d)->Math.max(m_d, iv.getValue()));
            }
        });
        
        IndexDistPair maxSpread = ParallelUtils.range(D, parallel)
                .mapToObj(d->new IndexDistPair(d, maxs.get(d)-mins.get(d)))
                .max(IndexDistPair::compareTo).get();

        
        if(maxSpread.dist == 0)//all the data is the same? Return a leaf
        {
            Leaf leaf = new Leaf(new IntList(points));
            leaf.setPivot(points);
            leaf.setRadius(points);
            return leaf;
        }
        
        //We found it! Lets sort points by this new value
        final int d = maxSpread.indx;
        points.sort((Integer o1, Integer o2) -> Double.compare(get(o1).get(d), get(o2).get(d)));
        int midPoint = points.size()/2;
        //Lets check that we don't have identical values, and adjust as needed
        while(midPoint > 1 && get(midPoint-1).get(d) == get(midPoint).get(d))
            midPoint--;
        List<Integer> left_children = points.subList(0, midPoint);
        List<Integer> right_children = points.subList(midPoint, points.size());
        
        Branch branch = new Branch();
        branch.setPivot(points);
        branch.setRadius(points);
        //everyone has been assigned, now creat children objects
        branch.left_child = build(left_children, parallel);
        branch.right_child = build(right_children, parallel);
        
        return branch;
    }
    
    private Node build(List<Integer> points, boolean parallel)
    {
        //universal base case
        if(points.size() <= leaf_size)
        {
            Leaf leaf = new Leaf(new IntList(points));
            leaf.setPivot(points);
            leaf.setRadius(points);
            return leaf;
        }
        
        switch(construction_method)
        {
            case KD_STYLE:
                return build_kd(points, parallel);
            case TOP_DOWN_FARTHEST:
                return build_far_top_down(points, parallel);
                
        }
        return new Leaf(new IntList(0));
    }
    
    public void build(boolean parallel, List<V> collection)
    {
        this.allVecs = new ArrayList<>(collection);
        this.cache = dm.getAccelerationCache(allVecs, parallel);
        this.root = build(IntList.range(collection.size()), parallel);
    }

    @Override
    public void insert(V x)
    {

        if(root == null)
        {
            allVecs = new ArrayList<>();
            allVecs.add(x);
            cache = dm.getAccelerationCache(allVecs);
            
            root = new Leaf(IntList.range(1));
            root.pivot = x.clone();
            root.pivot_qi = dm.getQueryInfo(x);
            root.radius = 0;
            
            return;
        }
        int indx = allVecs.size();
        allVecs.add(x);
        if(cache != null)
            cache.addAll(dm.getQueryInfo(x));
        
        Branch parentNode = null;
        Node curNode = root;
        double dist_to_curNode = dm.dist(indx, curNode.pivot, curNode.pivot_qi, allVecs, cache);
        while(curNode != null)
        {
            curNode.radius = Math.max(curNode.radius, dist_to_curNode);
            
            if(curNode instanceof jsat.linear.vectorcollection.BallTree.Leaf)
            {
                Leaf lroot = (Leaf) curNode;
                lroot.children.add(indx);
                
                if(lroot.children.size() > leaf_size)
                {
                    Node newNode = build(lroot.children, false);
                    if(parentNode == null)//We are the root node and a leaf
                        root = newNode;
                    else if(parentNode.left_child == curNode)//YES, intentinoally checking object equality
                        parentNode.left_child = newNode;
                    else
                        parentNode.right_child = newNode;
                }
                return;
            }
            else
            {
                Branch b = (Branch) curNode;
                double left_dist = dm.dist(indx, b.left_child.pivot, b.left_child.pivot_qi, allVecs, cache);
                double right_dist = dm.dist(indx, b.right_child.pivot, b.right_child.pivot_qi, allVecs, cache);
                
                //which branch will cause the minimum increase in the radius?
                double left_rad_inc = b.left_child.radius - left_dist;
                double right_rad_inc = b.right_child.radius - right_dist;
                
                //decend tree
                parentNode = b;
                if(left_rad_inc < right_rad_inc)
                {
                    curNode = b.left_child;
                    dist_to_curNode = left_dist;
                }
                else
                {
                    curNode = b.right_child;
                    dist_to_curNode = right_dist;
                }
            }
        }
    }

    @Override
    public BallTree<V> clone()
    {
        return new BallTree<>(this);
    }

    @Override
    public void search(Vec query, double range, List<Integer> neighbors, List<Double> distances)
    {
        root.search(query, dm.getQueryInfo(query), range, neighbors, distances);
        
        IndexTable it = new IndexTable(distances);
        it.apply(distances);
        it.apply(neighbors);
    }

    @Override
    public void search(Vec query, int numNeighbors, List<Integer> neighbors, List<Double> distances)
    {
        BoundedSortedList<IndexDistPair> knn = new BoundedSortedList<>(numNeighbors);
        root.search(query, dm.getQueryInfo(query), numNeighbors, knn, Double.POSITIVE_INFINITY);
        for(IndexDistPair p : knn)
        {
            neighbors.add(p.indx);
            distances.add(p.dist);
        }
    }

    @Override
    public V get(int indx)
    {
        return allVecs.get(indx);
    }

    @Override
    public int size()
    {
        return allVecs.size();
    }

    private abstract class Node implements Cloneable, Serializable
    {
        Vec pivot;
        List<Double> pivot_qi;
        double radius;

        public Node()
        {
        }

        public Node(Node toCopy)
        {
            if(toCopy.pivot != null)
                this.pivot = toCopy.pivot.clone();
            if(toCopy.pivot_qi != null)
                this.pivot_qi = new DoubleList(toCopy.pivot_qi);
            this.radius = toCopy.radius;
        }
        
        public void setPivot(List<Integer> points)
        {
            if(points.size() == 1)
                pivot = get(points.get(0)).clone();
            else
                pivot = pivot_method.getPivot(false, points, allVecs, dm, cache);
            pivot_qi = dm.getQueryInfo(pivot);
        }
        
        public void setRadius(List<Integer> points)
        {
            this.radius = 0;
            for(int i : points)
                radius = Math.max(radius, dm.dist(i, pivot, pivot_qi, allVecs, cache));
        }
        
        abstract public void search(Vec query, List<Double> qi, double range, List<Integer> neighbors, List<Double> distances);
        
        abstract public void search(Vec query, List<Double> qi, int numNeighbors, BoundedSortedList<IndexDistPair> knn, double pivot_to_query);
    }
    
    private class Leaf extends Node
    {
        IntList children;

        public Leaf(IntList children)
        {
            this.children = children;
        }

        public Leaf(Leaf toCopy)
        {
            super(toCopy);
            this.children = new IntList(toCopy.children);
        }

        @Override
        public void search(Vec query, List<Double> qi, double range, List<Integer> neighbors, List<Double> distances)
        {
            for(int indx : children)
            {
                double dist = dm.dist(indx, query, qi, allVecs, cache);
                if(dist <= range)
                {
                    neighbors.add(indx);
                    distances.add(dist);
                }
            }
        }

        @Override
        public void search(Vec query, List<Double> qi, int numNeighbors, BoundedSortedList<IndexDistPair> knn, double pivot_to_query)
        {
            for(int indx : children)
                knn.add(new IndexDistPair(indx, dm.dist(indx, query, qi, allVecs, cache)));
        }
        
    }
    
    private class Branch extends Node
    {
        Node left_child;
        Node right_child;

        public Branch()
        {
        }

        public Branch(Branch toCopy)
        {
            super(toCopy);
            this.left_child = cloneChangeContext(toCopy.left_child);
            this.right_child = cloneChangeContext(toCopy.right_child);
        }
        
        @Override
        public void search(Vec query, List<Double> qi, double range, List<Integer> neighbors, List<Double> distances)
        {
            if(dm.dist(query, pivot) - radius >= range)
                return;//We can prune this branch!
            left_child.search(query, qi, range, neighbors, distances);
            right_child.search(query, qi, range, neighbors, distances);
        }

        @Override
        public void search(Vec query, List<Double> qi, int numNeighbors, BoundedSortedList<IndexDistPair> knn, double pivot_to_query)
        {
            if(Double.isInfinite(pivot_to_query))//can happen for first call
                pivot_to_query = dm.dist(query, pivot);
            if(knn.size() >= numNeighbors && pivot_to_query - radius >= knn.last().dist)
                return;//We can prune this branch!
            double dist_left = dm.dist(query, left_child.pivot);
            double dist_right = dm.dist(query, right_child.pivot);
            
            double close_child_dist = dist_left;
            Node close_child = left_child;
            double far_child_dist = dist_right;
            Node far_child = right_child;
            
            if(dist_right < dist_left)
            {
                close_child_dist = dist_right;
                close_child = right_child;
                far_child_dist = dist_left;
                far_child = left_child;
            }
            
            close_child.search(query, qi, numNeighbors, knn, close_child_dist);
            far_child.search(query, qi, numNeighbors, knn, far_child_dist);
        }
        
    }
    
    private Node cloneChangeContext(Node toClone)
    {
        if (toClone != null)
            if (toClone instanceof jsat.linear.vectorcollection.BallTree.Leaf)
                return new Leaf((Leaf) toClone);
            else
                return new Branch((Branch) toClone);
        return null;
    }
}
