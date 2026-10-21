package pl.edu.pw.ee.aisd2025zex5.huffman;

import pl.edu.pw.ee.aisd2025zex5.cmdargs.ProgramConfig;
import pl.edu.pw.ee.aisd2025zex5.cmdargs.CustomArgumentException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import pl.edu.pw.ee.aisd2025zex5.utils.ByteArrayWrapper;

public class FrequencyCollector {

    public DoubleHashingHashMap<ByteArrayWrapper, Long> collectFrequencies(ProgramConfig config) throws IOException, CustomArgumentException {
        int l = config.getMaxLength();
        if (l < 1) {
            throw new CustomArgumentException("Argument -l musi być wiekszy lub równy 1.");
        }

        byte[] fileBytes = Files.readAllBytes(Paths.get(config.getSourcePath()));
        // mapa
        DoubleHashingHashMap<ByteArrayWrapper, Long> frequencies = new DoubleHashingHashMap<>();

        int length = fileBytes.length;

        // Iteracja co L bajtów
        for (int i = 0; i < length; i += l) { 

            int blockSize = l;
            
            // Sprawdzenie czy  ostatni blok niepełny 
            if (i + l > length) {
                blockSize = length - i;
            }
            
            byte[] subArray = new byte[blockSize];
            
            System.arraycopy(fileBytes, i, subArray, 0, blockSize);
            
            ByteArrayWrapper blockKey = new ByteArrayWrapper(subArray);

            Long currentFreq = frequencies.get(blockKey);
            if (currentFreq == null) {
                frequencies.put(blockKey, 1L);
            } else {
                frequencies.put(blockKey, currentFreq + 1);
            }
        }
        
        if (frequencies.getEntries().isEmpty()) {
            throw new CustomArgumentException("Plik zródlowy jest pusty lub nie zawiera danych.");
        }
        
        return frequencies;
    }
}
