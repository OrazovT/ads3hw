package com.company;
import java.sql.SQLOutput;
import java.util.Arrays;
public class MyHashTable<K, V> {
    private HashNode<K,V>[] chainArray;
    private int M;
    private int size = 0;
    private static final float LOAD_FACTOR = 0.75f;
    public MyHashTable(){
        this(11);
    }

    public MyHashTable(int M){
        this.M = M;
        chainArray = new HashNode[M];
    }

    private int hash(K key){
        int hashCode = getHashCode(key);
        return hashCode%M;
    }

    public void put(K key, V value){
        int index = hash(key);
        HashNode<K,V> head = chainArray[index];
        if (head == null){
            head = new HashNode<>(key, value, null);
            chainArray[index] = head;
        }
        else{
            HashNode<K, V> prev = null;
            HashNode<K, V> curr = head;
            boolean isInserted = false;
            while(curr != null){
                K currKey = curr.key;
                if (currKey == key){
                    curr.value = value;
                    isInserted = true;
                    break;
                }
                else{
                    prev = curr;
                    curr = curr.next;
                }
            }
            if (!isInserted){
                prev.next = new HashNode<K, V>(key, value, null);
            }
        }
        size++;
        float currentLoadFactor = size/(float) M;
        if (currentLoadFactor > LOAD_FACTOR){
            HashNode<K, V>[] list = asList();
            M *= 2;
            size = 0;
            chainArray = new HashNode[M];
            for (HashNode<K, V> hash: list) {
                put(hash.key, hash.value);
            }
        }
    }
    public V get(K key){
        int index = hash(key);
        HashNode<K, V> curr = chainArray[index];
        if (curr == null)
            return null;
        while (curr != null){
            if (curr.key == key){
                return curr.value;
            }
            else{
                curr = curr.next;
            }
        }
        return null;
    }
    public V remove(K key){
        V value = null;
        int index = hash(key);
        HashNode<K, V> curr = chainArray[index];
        if (curr == null){
            return null;
        }
        if (curr.key == key){
            chainArray[index] = curr.next;
            value = curr.value;
        }
        else{
            HashNode<K, V> prev = curr;
            curr = curr.next;
            while (curr != null){
                if (curr.key == key){
                    value = curr.value;
                    prev.next = curr.next;
                    curr.next = null;
                    break;
                }
                curr = curr.next;
            }
        }
        size--;
        return value;
    }
    public boolean contains(V value){
        return (value != null);
    }

    public HashNode<K, V>[] asList(){
        HashNode<K, V>[] list = new HashNode[size];
        int index = 0;
        for (HashNode<K, V> hash: chainArray){
            while (hash != null){
                list[index] = new HashNode<K, V>(hash.key, hash.value);
                index++;
                hash = hash.next;
            }
        }
        return list;
    }
    public String toString(){
        StringBuffer sb = new StringBuffer();
        for (HashNode<K, V> hash: chainArray){
            while (hash != null){
                sb.append(hash + ", ");
                hash = hash.next;
            }
        }
        if (sb.length() != 0){
            sb.delete(sb.length()-2, sb.length());
        }
        return sb.toString();
    }
    public boolean containsKey(K key){
        V value = get(key);
        return (value != null);
    }
    private int getHashCode(K key){
        return Math.abs(key.hashCode());
    }
    static class HashNode<K, V>{
        K key;
        V value;
        HashNode<K, V> next;
        public HashNode(K key, V value, HashNode<K, V> next){
            this.key = key;
            this.value = value;
            this.next = next;
        }
        public HashNode(K key, V value){
            this(key, value, null);
        }

    }
    public static void main(String[] args) {
        MyHashTable<Integer, String> m = new MyHashTable<Integer, String>(2);
        m.put(3, "3");
        System.out.println("value for key == 999999: " + m.get(999999));
        System.out.println("value for key == 1: " + m.get(1));

        System.out.print("Current hashTable : ");
        System.out.println(m.get(3));
        System.out.print(Arrays.toString(new MyHashTable[]{m}));
    }
}