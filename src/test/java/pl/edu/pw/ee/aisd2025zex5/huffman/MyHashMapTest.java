package pl.edu.pw.ee.aisd2025zex5.huffman;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MyHashMapTest {

    @Test
    void testBasicPutAndGet() {
        DoubleHashingHashMap<String, Integer> map = new DoubleHashingHashMap<>();
        map.put("jeden", 1);
        map.put("dwa", 2);

        assertEquals(1, map.get("jeden"), "Powinien zwrócić 1 dla klucza 'jeden'.");
        assertEquals(2, map.get("dwa"), "Powinien zwrócić 2 dla klucza 'dwa'.");
        assertNull(map.get("trzy"), "Powinien zwrócić null dla nieistniejącego klucza.");
    }
    
    @Test
    void testUpdateExistingKey() {
        DoubleHashingHashMap<String, Integer> map = new DoubleHashingHashMap<>();
        map.put("freq", 10);
        
        map.put("freq", 50);
        
        assertEquals(50, map.get("freq"), "Wartość powinna zostać zaktualizowana do 50.");
        assertEquals(1, map.getEntries().size(), "Liczba elementów powinna wynosić 1 (tylko aktualizacja, brak wstawienia).");
    }

    @Test
    void testResizeAndRehashing() {
        DoubleHashingHashMap<Integer, Integer> map = new DoubleHashingHashMap<>(5);
        
        for (int i = 0; i < 5; i++) {
            map.put(i, i);
        }
        
        for (int i = 0; i < 5; i++) {
            assertEquals(i, map.get(i), "Element " + i + " powinien być dostępny po resize.");
        }
        
        assertTrue(map.getSize() > 5, "Rozmiar tablicy powinien zostać powiększony.");
    }
}
