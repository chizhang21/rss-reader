package com.cashzhang.ashley;

import android.widget.BaseAdapter;

import org.apache.commons.collections.map.LinkedMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangchi on 2018/2/11.
 */

public abstract class LinkedMapAdapter<K, V> extends BaseAdapter {
    private final LinkedMap m_map;

    LinkedMapAdapter(Map<K, V> map) {
        m_map = null == map ? new LinkedMap(1) : new LinkedMap(map);
        notifyDataSetChanged();
    }

    @Override
    public boolean isEmpty() {
        return m_map.isEmpty();
    }

    public void put(K key, V value) {
        m_map.put(key, value);
        notifyDataSetChanged();
    }

    public void replaceAll(Map<K, V> map) {
        m_map.clear();
        m_map.putAll(map);
        notifyDataSetChanged();
    }

    public boolean containsValue(V value) {
        return m_map.containsValue(value);
    }

    public void remove(K key) {
        m_map.remove(key);
        notifyDataSetChanged();
    }

    public int indexOf(K key) {
        return m_map.indexOf(key);
    }

    public K getKey(int index) {
        return (K) m_map.get(index);
    }

    /**
     * Not a cheap method to call.
     *
     * @return a list of the keys, in order.
     */
    public List<K> getKeyList() {
        return (List<K>) Arrays.asList(m_map.keySet().toArray());
    }

    public LinkedMap getMap() {
        return (LinkedMap) m_map.clone();
    }

    @Override
    public int getCount() {
        return m_map.size();
    }

    @Override
    public V getItem(int position) {
        return (V) m_map.getValue(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}