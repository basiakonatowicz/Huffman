package pl.edu.pw.ee.aisd2025zex5.huffman;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import pl.edu.pw.ee.aisd2025zex5.cmdargs.ProgramConfig;
import pl.edu.pw.ee.aisd2025zex5.cmdargs.CustomArgumentException;
import pl.edu.pw.ee.aisd2025zex5.utils.ByteArrayWrapper;

import java.io.IOException;

public class FrequencyCollectorTest {

    private MyHashMap<ByteArrayWrapper, Long> collectFrequencies(int L, byte[] data)
        throws CustomArgumentException, IOException {

    // plik tymczasowy
    java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("freq_test_", ".bin");
    java.nio.file.Files.write(tempFile, data);

    ProgramConfig config = new ProgramConfig();
    config.setMaxLength(L);
    config.setSourcePath(tempFile.toString());

    FrequencyCollector collector = new FrequencyCollector();
    return collector.collectFrequencies(config);
}

    
    private long getFrequency(MyHashMap<ByteArrayWrapper, Long> map, byte[] key) {
        Long freq = map.get(new ByteArrayWrapper(key));
        return freq != null ? freq : 0;
    }

    @Test
    void testL1StandardHuffman() throws CustomArgumentException, IOException {
        byte[] testData = {10, 20, 10, 30, 10}; 
        MyHashMap<ByteArrayWrapper, Long> frequencies = collectFrequencies(1, testData); 

        assertEquals(3, getFrequency(frequencies, new byte[]{10}), "Częstość [10] powinna być 3.");
        assertEquals(1, getFrequency(frequencies, new byte[]{20}), "Częstość [20] powinna być 1.");
        assertEquals(1, getFrequency(frequencies, new byte[]{30}), "Częstość [30] powinna być 1.");
        assertEquals(3, frequencies.getEntries().size(), "Powinny być 3 unikalne symbole.");
    }
    

}

