/**
 * 
 */
package com.rains.proxy.core.algorithm.impl;


import com.rains.proxy.core.algorithm.Hashing;
import com.rains.proxy.core.log.impl.LoggerUtils;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 * @author liubing
 *
 */
public class ConsistentHash<T> {
	
	private final Hashing hash;
	private final int numberOfReplicas;
	private final SortedMap<Long, T> circle = new TreeMap<Long, T>();

	public ConsistentHash(Hashing hash, int numberOfReplicas,
						  Collection<T> nodes) {
		super();
		this.hash = hash;
		this.numberOfReplicas = numberOfReplicas;
		for (T node : nodes) {
			add(node);
		}
	}

	/**
	 * 增加真实机器节点
	 * 
	 * @param node
	 */
	public void add(T node) {
		for (int i = 0; i < this.numberOfReplicas; i++) {
			long nodeKey =this.hash.hash(node.toString() + i);
			circle.put(nodeKey, node);
			if(LoggerUtils.isDebugEnabled()){
				LoggerUtils.debug("机器key:{}",nodeKey);
			}
		}
	}

	/**
	 * 删除真实机器节点
	 * 
	 * @param node
	 */
	public void remove(T node) {
		for (int i = 0; i < this.numberOfReplicas; i++) {
			long nodeKey = this.hash.hash(node.toString() + i);
			circle.remove(nodeKey);
			if(LoggerUtils.isDebugEnabled()){
				LoggerUtils.debug("机器key:{}",nodeKey);
			}
		}
	}

	/**
	 * 取得真实机器节点
	 * 
	 * @param key
	 * @return
	 */
	public T get(String key) {
		if (circle.isEmpty()) {
			return null;
		}
		long hash = this.hash.hash(key);
		if(LoggerUtils.isDebugEnabled()){
			LoggerUtils.debug("get key:{}",hash);
		}
		if (!circle.containsKey(hash)) {
			SortedMap<Long, T> tailMap = circle.tailMap(hash);// 沿环的顺时针找到一个虚拟节点
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
		}
		return circle.get(hash); // 返回该虚拟节点对应的真实机器节点的信息
	}
	
	public T getBytes(byte[] key) {
		if (circle.isEmpty()) {
			return null;
		}
		long hash = this.hash.hash(key);
		if(LoggerUtils.isDebugEnabled()){
			LoggerUtils.debug("get key:{}",hash);
		}
		if (!circle.containsKey(hash)) {
			SortedMap<Long, T> tailMap = circle.tailMap(hash);// 沿环的顺时针找到一个虚拟节点
			hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
		}
		return circle.get(hash); // 返回该虚拟节点对应的真实机器节点的信息
	}
}
