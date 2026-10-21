package pl.edu.pw.ee.aisd2025zex5.huffman;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HuffmanNodeTest {

    @Test
    void testLeafNodeCreation() {
        byte[] symbol = {65};
        long frequency = 5;
        
        HuffmanNode leafNode = new HuffmanNode(symbol, frequency);
        
        assertTrue(leafNode.isLeaf());
        assertEquals(frequency, leafNode.getFrequency());
        assertArrayEquals(symbol, leafNode.getSymbol());
        assertNull(leafNode.getLeft());
        assertNull(leafNode.getRight());
    }

    @Test
    void testInternalNodeCreation() {
        byte[] symbolA = {65}; 
        byte[] symbolB = {66}; 
        
        HuffmanNode leftChild = new HuffmanNode(symbolA, 3);
        HuffmanNode rightChild = new HuffmanNode(symbolB, 2);
        HuffmanNode internalNode = new HuffmanNode(5, leftChild, rightChild);
        
        assertFalse(internalNode.isLeaf());
        assertEquals(5, internalNode.getFrequency());
        assertNull(internalNode.getSymbol());
        assertEquals(leftChild, internalNode.getLeft());
        assertEquals(rightChild, internalNode.getRight());
    }

    @Test
    void testCompareTo() {
        HuffmanNode node1 = new HuffmanNode(new byte[]{65}, 3);
        HuffmanNode node2 = new HuffmanNode(new byte[]{66}, 5);
        HuffmanNode node3 = new HuffmanNode(new byte[]{67}, 3);
        
        assertTrue(node1.compareTo(node2) < 0);
        assertTrue(node2.compareTo(node1) > 0);
        assertEquals(0, node1.compareTo(node3));
    }

    @Test
    void testMultiByteSymbol() {
        byte[] multiByteSymbol = {65, 66, 67}; 
        long frequency = 10;
        
        HuffmanNode node = new HuffmanNode(multiByteSymbol, frequency);
        
        assertTrue(node.isLeaf());
        assertArrayEquals(multiByteSymbol, node.getSymbol());
        assertEquals(frequency, node.getFrequency());
    }

    @Test
    void testZeroFrequency() {
        byte[] symbol = {88};
        HuffmanNode node = new HuffmanNode(symbol, 0);
        
        assertEquals(0, node.getFrequency());
        assertTrue(node.isLeaf());
    }
}