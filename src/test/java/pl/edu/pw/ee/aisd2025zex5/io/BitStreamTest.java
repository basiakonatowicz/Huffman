package pl.edu.pw.ee.aisd2025zex5.io;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class BitStreamTest {

    @TempDir
    Path tempDir; 
    
    @Test
    void testPaddingAndFlush() throws Exception {
        Path tempFile = tempDir.resolve("test.bin");
        String testCode = "11001"; 

        int padding;
        try (BitOutputStream bos = new BitOutputStream(new FileOutputStream(tempFile.toFile()))) {
            bos.writeCode(testCode);
            padding = bos.flushAndGetPadding();
        }
        
        assertEquals(3, padding, "Powinny zostać dopisane 3 zera (5 + 3 = 8).");

        byte[] fileBytes = Files.readAllBytes(tempFile);
        assertEquals(1, fileBytes.length, "Plik powinien mieć 1 bajt.");
        assertEquals((byte) 0xC8, fileBytes[0], "Zawartość bajta (0xC8) nie zgadza się.");
        
        try (BitInputStream bis = new BitInputStream(tempFile.toString())) {
            StringBuilder readCode = new StringBuilder();
            for (int i = 0; i < 5; i++) { 
                readCode.append(bis.readBit());
            }
            assertEquals(testCode, readCode.toString(), "Odczytane bity nie zgadzają się.");
            
            assertEquals(0, bis.readBit(), "Pierwszy bit paddingu powinien być 0.");
        }
    }
    
}