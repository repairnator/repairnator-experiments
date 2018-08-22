package com.zhongkexinli.micro.serv.common.util;


import java.util.ArrayList;
import java.util.List;

import com.zhongkexinli.micro.serv.common.vo.TreeNode;

/**
 */
public  class TreeUtil{
  
  private TreeUtil() {
    // 空构造
  }
  
  /**
   * 两层循环实现建树
   * 
   * @param treeNodes 传入的树节点列表
   * @return
   */
  public static <T extends TreeNode> List<T> bulid(List<T> treeNodes,Object root) {

    List<T> trees = new ArrayList<>();

    for (T treeNode : treeNodes) {

      if (root.equals(treeNode.getParentId())) {
        trees.add(treeNode);
      }

      for (T it : treeNodes) {
        if (it.getParentId() == treeNode.getId()) {
          if (treeNode.getChildren() == null) {
            treeNode.setChildren(new ArrayList<>());
          }
          treeNode.add(it);
        }
      }
    }
    return trees;
  }

  /**
   * 使用递归方法建树
   * 
   * @param treeNodes
   * @return
   */
  public static <T extends TreeNode> List<T> buildByRecursive(List<T> treeNodes,Object root) {
    List<T> trees = new ArrayList<>();
    for (T treeNode : treeNodes) {
      if (root.equals(treeNode.getParentId())) {
        trees.add(findChildren(treeNode, treeNodes));
      }
    }
    return trees;
  }

  /**
   * 递归查找子节点
   * 
   * @param treeNodes
   * @return
   */
  public static <T extends TreeNode> T findChildren(T treeNode, List<T> treeNodes) {
    for (T it : treeNodes) {
      if (treeNode.getId() == it.getParentId()) {
        if (treeNode.getChildren() == null) {
          treeNode.setChildren(new ArrayList<>());
        }
        treeNode.add(findChildren(it, treeNodes));
      }
    }
    return treeNode;
  }

}