package com.practice.collection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * All rights Reserved
 *
 * @Package com.practice.collection
 * @author: 长风
 * @date: 2018/9/20 下午6:45


 Map                  key(是否可以为null)    value(是否可以为null)   初始容量(数组大小)   shrehold    扩容(数组大小)
 HashTable                  NO                    NO                 11              11*0.75      2倍+1
 HashMap                    YES                   YES                16              16*0.75=12   2倍
 LinkedHashMap              YES                   YES                16              16*0.75=12   2倍
 TreeMap                    NO                    YES
 ConcurrentHashMap          NO                    NO                 16



 扩容：

 */
public class MapTest {
    public static void main(String[] args){
        putHashMap();
        putHashTable();
        putLinkedHashMap();
        putTreeMap();
        putConcurrentHashMap();
    }

    public static void putHashMap(){
        HashMap<String,String> map = new HashMap<>();

        map.put(null,"null");
        map.put("null",null);

        traverse(map);
    }

    /**
     public synchronized V put(K key, V value) {
         // Make sure the value is not null
         if (value == null) {
         throw new NullPointerException();
         }

         // Makes sure the key is not already in the hashtable.
         Entry<?,?> tab[] = table;
         int hash = key.hashCode();
         int index = (hash & 0x7FFFFFFF) % tab.length;
         @SuppressWarnings("unchecked")
         Entry<K,V> entry = (Entry<K,V>)tab[index];
         for(; entry != null ; entry = entry.next) {
         if ((entry.hash == hash) && entry.key.equals(key)) {
         V old = entry.value;
         entry.value = value;
         return old;
     }
     }

     addEntry(hash, key, value, index);
     return null;
     }
     */
    public static void putHashTable(){
        Hashtable<String,String> table = new Hashtable<>();

        //table.put(null,"null");//key 不能为null
        //table.put("null",null);//value 不能为null

        traverse(table);
    }

    /**
     * extends HashMap<K,V>
     static class Entry<K,V> extends HashMap.Node<K,V> {
         Entry<K,V> before, after;
         Entry(int hash, K key, V value, Node<K,V> next) {
         super(hash, key, value, next);
     }
     }
     */
    public static void putLinkedHashMap(){
        LinkedHashMap<String,String> linked = new LinkedHashMap<>();

        linked.put(null,"null");
        linked.put("null",null);

        traverse(linked);
    }

    /**
     public V put(K key, V value) {
         Entry<K,V> t = root;
         if (t == null) {
         compare(key, key); // type (and possibly null) check

         root = new Entry<>(key, value, null);
         size = 1;
         modCount++;
         return null;

         ....
         }


         final int compare(Object k1, Object k2) {
             return comparator==null ? ((Comparable<? super K>)k1).compareTo((K)k2)
             : comparator.compare((K)k1, (K)k2);
         }

     }
     */
    public static void putTreeMap(){
        TreeMap<String,String> treeMap = new TreeMap<>();

        //treeMap.put(null,"null");//key 不能为null
        treeMap.put("null",null);

        traverse(treeMap);
    }

    /**
     public V put(K key, V value) {
        return putVal(key, value, false);
     }

     final V putVal(K key, V value, boolean onlyIfAbsent) {
         if (key == null || value == null) throw new NullPointerException();
         int hash = spread(key.hashCode());
         int binCount = 0;

         ...
     }
     */
    public static void putConcurrentHashMap(){
        ConcurrentHashMap<String,String> concurrent = new ConcurrentHashMap<>();

        //concurrent.put(null,"null");//key不能为null
        //concurrent.put("null",null);//value不能为null

        traverse(concurrent);
    }

    /**
     * 遍历map的方法
     * @param map
     */
    public static void traverse(Map<String,String> map){
        System.out.println(map.getClass().getSimpleName()+":");
        //one
        for(java.util.Map.Entry<String,String> item:map.entrySet()){
            System.out.println("entrySet,"+"key:"+item.getKey()+",value:"+item.getValue());
        }


        Iterator<Map.Entry<String,String>> iterator = map.entrySet().iterator();

        //two
        while (iterator.hasNext()){
            Map.Entry<String,String> entry = iterator.next();
            System.out.println("iterator,"+"key:"+entry.getKey()+",value:"+entry.getValue());
        }

        //three
        for(String key:map.keySet()){
            System.out.println("keySet,"+"key:"+key+",value:"+map.get(key));
        }

        System.out.println("---------------------");
    }

}
