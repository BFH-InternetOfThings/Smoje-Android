package ch.bfh.mobicomp.smuoy.utils;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by chris on 11.12.14.
 */
public class MagicMap<K, V> implements Map<K, V> {
    private Map<K, V> map = new HashMap<>();

    private int depth = 0;
    private Class<?> valueClass;

    public MagicMap(Class<V> valueClass) {
        this.valueClass = valueClass;
    }

    public MagicMap(int depth, Class<?> valueClass) {
        this.depth = depth;
        this.valueClass = valueClass;
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized V get(Object key) {
        V value = map.get(key);
        if (value == null) {
            try {
                if (depth > 1) {
                    value = (V) new MagicMap<>(depth - 1, valueClass);
                } else {
                    value = (V) valueClass.newInstance();
                }
                map.put((K) key, value);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return value;
    }

    @Override
    public V put(K key, V value) {
        return map.put(key, value);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public boolean containsEncapsulatedKeys(Object... keys) {
        Map<?, ?> m = map;
        for (Object key : keys) {
            if (map.containsKey(key)) {
                Object o = map.get(key);
                if (o instanceof MagicMap) {
                    m = (Map<?, ?>) o;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @NonNull
    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @NonNull
    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        for (Map.Entry<? extends K, ? extends V> e : map.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public V remove(Object key) {
        return map.remove(key);
    }

    @Override
    public int size() {
        return map.size();
    }

    @NonNull
    @Override
    public Collection<V> values() {
        return map.values();
    }

    public Map<K, V> getMap() {
        return map;
    }
}
