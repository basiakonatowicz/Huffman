package pl.edu.pw.ee.aisd2025zex5.huffman;

import pl.edu.pw.ee.aisd2025zex5.cmdargs.CustomArgumentException;
import pl.edu.pw.ee.aisd2025zex5.utils.ByteArrayWrapper;

// Budowa drzewa za pomocą MyMinHeap
public class HuffmanTreeBuilder {
    
    public HuffmanNode buildTree(MyHashMap<ByteArrayWrapper, Long> frequencies) throws CustomArgumentException {
        
        MyMinHeap<HuffmanNode> pq = new MyMinHeap<>(); 

        for (MapEntry<ByteArrayWrapper, Long> entry : frequencies.getEntries()) {
            HuffmanNode leaf = new HuffmanNode(entry.getKey().getData(), entry.getValue());
            pq.insert(leaf);
        }

        if (pq.isEmpty()) {
            return null;
        }
        if (pq.size() == 1) {
            HuffmanNode single = pq.extractMin();
            return new HuffmanNode(single.getFrequency(), single, null);
        }
        
        // HUFFMAN iteracyjny
        while (pq.size() > 1) {
            // Pobranie dwóch węzłów o najmniejszej częstości (O(log N))
            HuffmanNode left = pq.extractMin(); 
            HuffmanNode right = pq.extractMin();
            
            long sumFrequency = left.getFrequency() + right.getFrequency();
            HuffmanNode parent = new HuffmanNode(sumFrequency, left, right);
            
            pq.insert(parent); 
        }
// ostatni wezel co pozostal to korzen
        return pq.extractMin();
    }
}