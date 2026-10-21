package pl.edu.pw.ee.aisd2025zex5.huffman;

/*
para klucz wartość do mapy
*/
public class MapEntry<K, V> {
    private final K key;
    private V value;

    public MapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
    
    // equals i hashCode działają na podstaiwe klucza
    @Override
    public int hashCode() {
        return key.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        MapEntry<?, ?> other = (MapEntry<?, ?>) obj;
        if (key == null) {
            return other.key == null;
        }
        return key.equals(other.key); 
    }
    
    // do iterowania HuffmanTreeBuilder
    public static <K, V> java.util.Map.Entry<K, V> createAbstractEntry(K key, V value) {
        return new java.util.AbstractMap.SimpleEntry<>(key, value);
    }
}
