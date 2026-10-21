package pl.edu.pw.ee.aisd2025zex5.huffman;

public class DoubleHashingHashMap<K, V> extends MyHashMap<K, V> {

    public DoubleHashingHashMap() {
        super();
    }

    public DoubleHashingHashMap(int size) {
        super(size);
    }

    @Override
    int hashFunc(int keyHash, int i) {
        int size = getSize();
        keyHash = keyHash & Integer.MAX_VALUE;
        int h1 = keyHash % size;
        int h2 = 1 + (keyHash % (size - 1));
        return (h1 + i * h2) % size;
    }
}
