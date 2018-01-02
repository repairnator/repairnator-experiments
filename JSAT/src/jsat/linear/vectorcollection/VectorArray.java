
package jsat.linear.vectorcollection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import jsat.linear.Vec;
import jsat.linear.distancemetrics.DistanceMetric;
import jsat.utils.BoundedSortedList;
import jsat.utils.DoubleList;
import jsat.utils.IndexTable;

/**
 * This is the naive implementation of a Vector collection. Construction time is 
 * O(n) only to clone the n elements, and all queries are O(n)
 * <br><br>
 * Removing elements from the vector array will result in the destruction of any
 * {@link DistanceMetric#getAccelerationCache(java.util.List) acceleration cache}
 * 
 * @author Edward Raff
 */
public class VectorArray<V extends Vec> extends ArrayList<V> implements IncrementalCollection<V>
{
    private static final long serialVersionUID = 5365949686370986234L;
    private DistanceMetric distanceMetric;
    private List<Double> distCache;

    public VectorArray(DistanceMetric distanceMetric, int initialCapacity)
    {
        super(initialCapacity);
        this.distanceMetric = distanceMetric;
        if(distanceMetric.supportsAcceleration())
            distCache = new DoubleList(initialCapacity);
    }

    public VectorArray(DistanceMetric distanceMetric, Collection<? extends V> c)
    {
        super(c);
        this.distanceMetric = distanceMetric;
        if(distanceMetric.supportsAcceleration())
            distCache = distanceMetric.getAccelerationCache(this);
    }

    public VectorArray(DistanceMetric distanceMetric)
    {
        super();
        this.distanceMetric = distanceMetric;
        if(distanceMetric.supportsAcceleration())
            distCache = new DoubleList();
    }

    public DistanceMetric getDistanceMetric()
    {
        return distanceMetric;
    }

    public void setDistanceMetric(DistanceMetric distanceMetric)
    {
        this.distanceMetric = distanceMetric;
        if(distanceMetric.supportsAcceleration())
            this.distCache = distanceMetric.getAccelerationCache(this);
        else
            this.distCache = null;
    }
    
    @Override
    public void insert(V x)
    {
        add(x);
    }

    @Override
    public boolean add(V e)
    {
        boolean toRet = super.add(e);
        if(distCache != null)
            this.distCache.addAll(distanceMetric.getQueryInfo(e));
        return toRet;
    }

    @Override
    public boolean addAll(Collection<? extends V> c)
    {
        boolean toRet = super.addAll(c);
        if(this.distCache != null)
            for(V v : c)
                this.distCache.addAll(this.distanceMetric.getQueryInfo(v));
        return toRet;
    }

    @Override
    public V remove(int index)
    {
        distCache = null;
        return super.remove(index); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void search(Vec query, double range, List<Integer> neighbors, List<Double> distances)
    {
        List<Double> qi = distanceMetric.getQueryInfo(query);
        
        for(int i = 0; i < size(); i++)
        {
            double dist = distanceMetric.dist(i, query, qi, this, distCache);
            if(dist <= range)
            {
                neighbors.add(i);
                distances.add(dist);
            }
        }
        
        IndexTable it = new IndexTable(distances);
        it.apply(neighbors);
        it.apply(distances);
    }

    @Override
    public void search(Vec query, int numNeighbors, List<Integer> neighbors, List<Double> distances)
    {
        BoundedSortedList<IndexDistPair> knns = new BoundedSortedList<>(numNeighbors);
        
        List<Double> qi = distanceMetric.getQueryInfo(query);
        
        for(int i = 0; i < size(); i++)
        {
            double distance = distanceMetric.dist(i, query, qi, this, distCache);
            knns.add(new IndexDistPair(i, distance));
        }
        
        for(int i = 0; i < knns.size(); i++)
        {
            neighbors.add(knns.get(i).getIndex());
            distances.add(knns.get(i).getDist());
        }
    }

    @Override
    public VectorArray<V> clone()
    {
        VectorArray<V> clone = new VectorArray<>(distanceMetric, this);
        
        return clone;
    }

    public static class VectorArrayFactory<V extends Vec> implements VectorCollectionFactory<V>
    {
        private static final long serialVersionUID = -7470849503958877157L;

        @Override
        public VectorCollection<V> getVectorCollection(List<V> source, DistanceMetric distanceMetric)
        {
            return new VectorArray<>(distanceMetric, source);
        }

        @Override
        public VectorCollection<V> getVectorCollection(List<V> source, DistanceMetric distanceMetric, ExecutorService threadpool)
        {
            return getVectorCollection(source, distanceMetric);
        }

        @Override
        public VectorArrayFactory<V> clone()
        {
            return new VectorArrayFactory<>();
        }
    }
    
}
