package pl.edu.pw.ee.aisd2025zex5.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import pl.edu.pw.ee.aisd2025zex5.cmdargs.CustomArgumentException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public class BitInputStreamTest {

    @TempDir
    Path tempDir;

    @Test
    void testReadBit() throws IOException {
        Path testFile = tempDir.resolve("test.bin");
        
        // 0xC8 = 11001000 
        try (FileOutputStream fos = new FileOutputStream(testFile.toFile())) {
            fos.write(0xC8);
        }
        
        try (BitInputStream bis = new BitInputStream(testFile.toString())) {
            assertEquals(1, bis.readBit());
            assertEquals(1, bis.readBit());
            assertEquals(0, bis.readBit());
            assertEquals(0, bis.readBit());
            assertEquals(1, bis.readBit());
            assertEquals(0, bis.readBit());
            assertEquals(0, bis.readBit());
            assertEquals(0, bis.readBit());
            assertEquals(-1, bis.readBit());
        }
    }

    @Test
    void testReadByte() throws IOException, CustomArgumentException {
        Path testFile = tempDir.resolve("test.bin");
        
        try (FileOutputStream fos = new FileOutputStream(testFile.toFile())) {
            fos.write(new byte[]{65, 66, 67}); // ABC
        }
        
        try (BitInputStream bis = new BitInputStream(testFile.toString())) {
            assertEquals(65, bis.readByte());
            assertEquals(66, bis.readByte());
            assertEquals(67, bis.readByte());
            assertEquals(-1, bis.readByte()); // EOF
        }
    }

    @Test
    void testReadBytes() throws IOException, CustomArgumentException {
        Path testFile = tempDir.resolve("test.bin");
        
        byte[] testData = {72, 101, 108, 108, 111}; 
        try (FileOutputStream fos = new FileOutputStream(testFile.toFile())) {
            fos.write(testData);
        }
        
        try (BitInputStream bis = new BitInputStream(testFile.toString())) {
            byte[] buffer = new byte[5];
            int bytesRead = bis.readBytes(buffer, 5);
            
            assertEquals(5, bytesRead);
            assertArrayEquals(testData, buffer);
        }
    }

    @Test
    void testMixedBitAndByteOperations() throws IOException, CustomArgumentException {
        Path testFile = tempDir.resolve("test.bin");
        
        // 0xC8, 0x41 (11001000, 01000001)
        try (FileOutputStream fos = new FileOutputStream(testFile.toFile())) {
            fos.write(new byte[]{(byte) 0xC8, 0x41});
        }
        
        try (BitInputStream bis = new BitInputStream(testFile.toString())) {
            assertEquals(1, bis.readBit());
            assertEquals(1, bis.readBit());
            assertEquals(0, bis.readBit());
            
            int nextByte = bis.readByte();
            assertEquals(0x41, nextByte);
        }
    }

    @Test
    void testEmptyFile() throws IOException {
        Path testFile = tempDir.resolve("empty.bin");
        
        try (FileOutputStream fos = new FileOutputStream(testFile.toFile())) {
        }
        
        try (BitInputStream bis = new BitInputStream(testFile.toString())) {
            assertEquals(-1, bis.readBit());
        }
    }

    @Test
    void testNonExistentFile() {
        Path testFile = tempDir.resolve("nonexistent.bin");
        
        assertThrows(IOException.class, () -> {
            new BitInputStream(testFile.toString());
        });
    }
}