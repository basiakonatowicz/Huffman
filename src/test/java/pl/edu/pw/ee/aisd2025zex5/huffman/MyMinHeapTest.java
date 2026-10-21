package pl.edu.pw.ee.aisd2025zex5.huffman;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import pl.edu.pw.ee.aisd2025zex5.cmdargs.CustomArgumentException;

public class MyMinHeapTest {

    @Test
    void testMinExtractionOrder() throws CustomArgumentException {
        MyMinHeap<HuffmanNode> heap = new MyMinHeap<>();
        
        heap.insert(new HuffmanNode(new byte[]{99}, 10)); // C=10
        heap.insert(new HuffmanNode(new byte[]{97}, 5));  // A=5
        heap.insert(new HuffmanNode(new byte[]{100}, 12)); // D=12
        heap.insert(new HuffmanNode(new byte[]{98}, 2));  // B=2

        assertEquals(2, heap.extractMin().getFrequency(), "Najpierw powinno wyjść 2.");
        assertEquals(5, heap.extractMin().getFrequency(), "Następnie powinno wyjść 5.");
        assertEquals(10, heap.extractMin().getFrequency(), "Następnie powinno wyjść 10.");
        assertEquals(12, heap.extractMin().getFrequency(), "Ostatnie to 12.");
        
        assertTrue(heap.isEmpty(), "Kopiec powinien być pusty.");
    }
    
    @Test
    void testMinExtractionWithInternalNodes() throws CustomArgumentException {
        MyMinHeap<HuffmanNode> heap = new MyMinHeap<>();
        
        HuffmanNode nodeA = new HuffmanNode(new byte[]{65}, 1);
        HuffmanNode nodeB = new HuffmanNode(new byte[]{66}, 2);
        
        HuffmanNode nodeAB = new HuffmanNode(3, nodeA, nodeB);
        
        heap.insert(nodeAB);
        heap.insert(new HuffmanNode(new byte[]{67}, 5)); // C=5
        
        assertEquals(3, heap.extractMin().getFrequency(), "Powinno wyjść 3 (węzeł wewnętrzny) jako minimum.");
    }

    @Test
    void testExtractMinOnEmptyHeap() {
        MyMinHeap<HuffmanNode> heap = new MyMinHeap<>();
        
        assertThrows(CustomArgumentException.class, heap::extractMin, "Pobranie z pustego kopca powinno rzucić CustomArgumentException.");
    }
}