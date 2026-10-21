package pl.edu.pw.ee.aisd2025zex5.huffman;

import org.junit.jupiter.api.Test;
import pl.edu.pw.ee.aisd2025zex5.cmdargs.CustomArgumentException;
import static org.junit.jupiter.api.Assertions.*;
import pl.edu.pw.ee.aisd2025zex5.utils.ByteArrayWrapper;

public class HuffmanTreeBuilderTest {

    private HuffmanTreeBuilder builder = new HuffmanTreeBuilder();

    private DoubleHashingHashMap<ByteArrayWrapper, Long> createFreqMap(String[] symbols, long[] freqs) {
        DoubleHashingHashMap<ByteArrayWrapper, Long> map = new DoubleHashingHashMap<>();
        for (int i = 0; i < symbols.length; i++) {
            map.put(new ByteArrayWrapper(symbols[i].getBytes()), freqs[i]);
        }
        return map;
    }

    @Test
    void testSimpleTreeStructure() throws CustomArgumentException {
        // Oczekiwany root(6) 
        String[] symbols = {"A", "B", "C"};
        long[] freqs = {3L, 2L, 1L};
        DoubleHashingHashMap<ByteArrayWrapper, Long> freqMap = createFreqMap(symbols, freqs);
        
        HuffmanNode root = builder.buildTree(freqMap);

        assertNotNull(root, "Korzeń nie powinien być null.");
        assertEquals(6, root.getFrequency(), "Korzeń powinien mieć sumę częstości 6.");
        
        
        HuffmanNode node1 = root.getLeft();
        HuffmanNode node2 = root.getRight();
        
        assertEquals(3, node1.getFrequency(), "Poddrzewo 1 musi mieć częstość 3.");
        assertEquals(3, node2.getFrequency(), "Poddrzewo 2 musi mieć częstość 3.");
        
        boolean node1IsLeaf = node1.isLeaf();
        boolean node2IsLeaf = node2.isLeaf();

        assertTrue(node1IsLeaf ^ node2IsLeaf, "Dokładnie jeden z węzłów potomnych korzenia musi być liściem (A).");

        HuffmanNode internalNode = node1IsLeaf ? node2 : node1;
        
        assertFalse(internalNode.isLeaf(), "Węzeł pośredni musi być wewnętrzny.");
        
        assertTrue(internalNode.getLeft().isLeaf(), "Lewe dziecko węzła wewnętrznego musi być liściem.");
        assertTrue(internalNode.getRight().isLeaf(), "Prawe dziecko węzła wewnętrznego musi być liściem.");
    }
    @Test
    void testSingleSymbolHandling() throws CustomArgumentException {
        String[] symbols = {"A"};
        long[] freqs = {5L};
        DoubleHashingHashMap<ByteArrayWrapper, Long> freqMap = createFreqMap(symbols, freqs);
        
        HuffmanNode root = builder.buildTree(freqMap);
        
        assertFalse(root.isLeaf(), "Korzeń nie może być liściem.");
        assertTrue(root.getLeft().isLeaf(), "Korzeń powinien mieć lewe dziecko jako liść.");
        assertNull(root.getRight(), "Korzeń powinien mieć puste prawe dziecko.");
        assertEquals(5, root.getFrequency());
    }
}