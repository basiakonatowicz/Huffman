package pl.edu.pw.ee.aisd2025zex5.huffman;

import java.util.List;
import java.util.ArrayList;

/*
mapa mieszająca z adresowaniem otwartym
*/
public abstract class MyHashMap<K, V> {

    private final MapEntry<K, V> nil = null; 
    private int size;
    private int nElems;
    private MapEntry<K, V>[] hashElems; 
    private final double correctLoadFactor = 0.75;
    
    protected MyHashMap() {
        this(2039); 
    }

    protected MyHashMap(int size) {
        validateHashInitSize(size);
        this.size = size;
        this.hashElems = createTable(this.size);
    }
    
    @SuppressWarnings("unchecked")
    private MapEntry<K, V>[] createTable(int size) {
        return (MapEntry<K, V>[]) new MapEntry[size];
    }
    
    public void put(K key, V value) {
        validateInputKey(key);
        resizeIfNeeded();

        int keyHash = key.hashCode();
        int i = 0;
        int hashId = hashFunc(keyHash, i);
        
        while (hashElems[hashId] != nil) {
            
            if (hashElems[hashId].getKey().equals(key)) {
                hashElems[hashId].setValue(value); 
                return; 
            }
            
            i++; 
            if (i >= size) { 
                 doubleResize();
                 i = 0; 
            }
            hashId = hashFunc(keyHash, i);
        }

        hashElems[hashId] = new MapEntry<>(key, value);
        nElems++;
    }
    
    public V get(K key) {
        validateInputKey(key);

        int keyHash = key.hashCode();
        int i = 0;
        int hashId = hashFunc(keyHash, i);

        while (hashElems[hashId] != nil) {
            if (hashElems[hashId].getKey().equals(key)) {
                return hashElems[hashId].getValue(); 
            }

            i++; 
            if (i >= size) { 
                return null;
            }
            hashId = hashFunc(keyHash, i);
        }

        return null; 
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }
    
    public List<MapEntry<K, V>> getEntries() {
        List<MapEntry<K, V>> entriesList = new ArrayList<>();
        for (MapEntry<K, V> entry : hashElems) {
            if (entry != nil) {
                entriesList.add(entry);
            }
        }
        return entriesList;
    }
    
    private void validateHashInitSize(int initialSize) {
         if (initialSize < 1) {
            throw new IllegalArgumentException("Initial size of hash table cannot be lower than 1!");
        }
    }
    
    private void validateInputKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Input key cannot be null!");
        }
    }
    
    private void resizeIfNeeded() {
        double loadFactor = (double) nElems / size;
        if (loadFactor >= correctLoadFactor) {
            doubleResize();
        }
    }

    private void doubleResize() {
        this.size *= 2;
        MapEntry<K, V>[] oldElems = hashElems;
        hashElems = createTable(size);
        nElems = 0;

        for (MapEntry<K, V> currentEntry : oldElems) {
            if (currentEntry != nil) {
                put(currentEntry.getKey(), currentEntry.getValue()); 
            }
        }
    }
    
    int getSize() {
        return size;
    }
    
    abstract int hashFunc(int keyHash, int i);
}