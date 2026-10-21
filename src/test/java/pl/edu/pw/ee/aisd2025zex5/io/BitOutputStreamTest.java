package pl.edu.pw.ee.aisd2025zex5.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import pl.edu.pw.ee.aisd2025zex5.cmdargs.CustomArgumentException;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BitOutputStreamTest {

    @TempDir
    Path tempDir;

    @Test
    void testWriteBit() throws IOException, CustomArgumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try (BitOutputStream bos = new BitOutputStream(baos)) {
            // 11001000 (0xC8)
            bos.writeBit(1);
            bos.writeBit(1);
            bos.writeBit(0);
            bos.writeBit(0);
            bos.writeBit(1);
            bos.writeBit(0);
            bos.writeBit(0);
            bos.writeBit(0);
            
            int padding = bos.flushAndGetPadding();
            assertEquals(0, padding); 
        }
        
        byte[] result = baos.toByteArray();
        assertEquals(1, result.length);
        assertEquals((byte) 0xC8, result[0]);
    }

    @Test
    void testWriteBitWithPadding() throws IOException, CustomArgumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try (BitOutputStream bos = new BitOutputStream(baos)) {
            // 5 bitów
            bos.writeBit(1);
            bos.writeBit(1);
            bos.writeBit(0);
            bos.writeBit(0);
            bos.writeBit(1);
            
            int padding = bos.flushAndGetPadding();
            assertEquals(3, padding);
        }
        
        byte[] result = baos.toByteArray();
        assertEquals(1, result.length);
        assertEquals((byte) 0xC8, result[0]); // 11001000
    }

    @Test
    void testWriteByte() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try (BitOutputStream bos = new BitOutputStream(baos)) {
            bos.writeByte(65); // A
            bos.writeByte(66); // B
            bos.writeByte(67); // C
        }
        
        byte[] result = baos.toByteArray();
        assertArrayEquals(new byte[]{65, 66, 67}, result);
    }

    @Test
    void testWriteCode() throws IOException, CustomArgumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try (BitOutputStream bos = new BitOutputStream(baos)) {
            bos.writeCode("11001000");
            int padding = bos.flushAndGetPadding();
            assertEquals(0, padding);
        }
        
        byte[] result = baos.toByteArray();
        assertEquals(1, result.length);
        assertEquals((byte) 0xC8, result[0]);
    }

    @Test
    void testWriteCodeWithPadding() throws IOException, CustomArgumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try (BitOutputStream bos = new BitOutputStream(baos)) {
            bos.writeCode("11001");
            int padding = bos.flushAndGetPadding();
            assertEquals(3, padding);
        }
        
        byte[] result = baos.toByteArray();
        assertEquals(1, result.length);
        assertEquals((byte) 0xC8, result[0]); // 11001000
    }

    @Test
    void testMixedBitAndByteOperations() throws IOException, CustomArgumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try (BitOutputStream bos = new BitOutputStream(baos)) {
            // zapisuje bity
            bos.writeBit(1);
            bos.writeBit(1);
            bos.writeBit(0);
            
            // Bajt
            bos.writeByte(65); // 'A'
            
            // znowu bity
            bos.writeBit(1);
            bos.writeBit(0);
            
            int padding = bos.flushAndGetPadding();
            assertEquals(6, padding); 
        }
        
        byte[] result = baos.toByteArray();
        assertEquals(3, result.length);
        assertEquals((byte) 0xC0, result[0]);
        assertEquals(65, result[1]);
        assertEquals((byte) 0x80, result[2]);
    }

    @Test
    void testInvalidBitValue() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try (BitOutputStream bos = new BitOutputStream(baos)) {
            assertThrows(CustomArgumentException.class, () -> bos.writeBit(2));
            assertThrows(CustomArgumentException.class, () -> bos.writeBit(-1));
        } catch (IOException e) {
            fail("IOException should not be thrown in this test");
        }
    }

    @Test
    void testInvalidCodeCharacter() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try (BitOutputStream bos = new BitOutputStream(baos)) {
            assertThrows(CustomArgumentException.class, () -> bos.writeCode("1102"));
            assertThrows(CustomArgumentException.class, () -> bos.writeCode("abc"));
        } catch (IOException e) {
            fail("IOException should not be thrown in this test");
        }
    }

    @Test
    void testFileOutput() throws IOException, CustomArgumentException {
        Path testFile = tempDir.resolve("output.bin");
        
        try (BitOutputStream bos = new BitOutputStream(new FileOutputStream(testFile.toFile()))) {
            bos.writeCode("11001000");
            bos.flushAndGetPadding();
        }
        
        byte[] fileContent = Files.readAllBytes(testFile);
        assertEquals(1, fileContent.length);
        assertEquals((byte) 0xC8, fileContent[0]);
    }
}