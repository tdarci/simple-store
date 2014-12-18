package com.darcimaher.simplestore;

import java.util.*;

public class SearchableStringMap implements Map<String, String> {

    protected final HashMap<String, String> map;
    protected final HashMap<String, Set<String>> reverseMap;

    public SearchableStringMap() {
        map = new HashMap<String, String>();
        reverseMap = new HashMap<String, Set<String>>();
    }

    public Set<String> keysWithValue(String value) {
        return this.reverseMap.get(value);
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    @Override
    public String get(Object key) {
        return this.map.get(key);
    }

    @Override
    public String put(String key, String value) {

        // update our reverseMap: remove old entry
        removeReverseMapEntry(key);

        // update our reverseMap: add new entry
        Set<String> keyList = reverseMap.get(value);
        if (keyList == null) {
            keyList = new HashSet<String>();
            this.reverseMap.put(value, keyList);
        }
        keyList.add(key);

        return this.map.put(key, value);

    }

    private void removeReverseMapEntry(String key) {
        String oldValue = this.map.get(key);
        if (oldValue != null) {
            // remove from reverseMap
            Set<String> keyList = reverseMap.get(oldValue);
            if (keyList != null) {
                keyList.remove(key);
            }
        }
    }

    @Override
    public String remove(Object key) {
        removeReverseMapEntry(key);
        return this.map.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
        this.map.putAll(m);
        rebuildReverseMap();
    }

    private void rebuildReverseMap() {
        for (Entry<String, String> e in this.map.entrySet()) {

        }
    }

    @Override
    public void clear() {
        this.map.clear();
        this.reverseMap.clear();
    }

    @Override
    public Set<String> keySet() {
        return this.map.keySet();
    }

    @Override
    public Collection<String> values() {
        return this.map.values();
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        return this.map.entrySet();
    }
}
