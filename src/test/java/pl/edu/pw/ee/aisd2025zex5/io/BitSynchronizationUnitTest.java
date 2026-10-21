package pl.edu.pw.ee.aisd2025zex5.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import pl.edu.pw.ee.aisd2025zex5.cmdargs.CustomArgumentException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

/*
synchronizacja bit bajt
 */
public class BitSynchronizationUnitTest {

    @TempDir
    Path tempDir;

    @Test
    void testReadByteAfterOneBit() throws IOException, CustomArgumentException {
        Path testFile = tempDir.resolve("one_bit.bin");
        
        // 1 bit 1 bajt
        try (BitOutputStream bos = new BitOutputStream(new FileOutputStream(testFile.toFile()))) {
            bos.writeBit(1);
            bos.writeByte(42);
            bos.flushAndGetPadding();
        }
        
        //1 bit 1 bajt
        try (BitInputStream bis = new BitInputStream(testFile.toString())) {
            int bit = bis.readBit();
            int byteValue = bis.readByte();
            
            assertEquals(1, bit, "Should read bit 1");
            assertEquals(42, byteValue, "Should read byte 42 after bit synchronization");
        }
    }

    @Test
    void testReadByteAfterTwoBits() throws IOException, CustomArgumentException {
        Path testFile = tempDir.resolve("two_bits.bin");
        
        // zapis 2bity 1 bajt
        try (BitOutputStream bos = new BitOutputStream(new FileOutputStream(testFile.toFile()))) {
            bos.writeBit(0);
            bos.writeBit(1);
            bos.writeByte(99);
            bos.flushAndGetPadding();
        }
        
        //2 bity jeden bajt
        try (BitInputStream bis = new BitInputStream(testFile.toString())) {
            int bit1 = bis.readBit();
            int bit2 = bis.readBit();
            int byteValue = bis.readByte();
            
            assertEquals(0, bit1);
            assertEquals(1, bit2);
            assertEquals(99, byteValue);
        }
    }

    @Test
    void testReadByteAfterSevenBits() throws IOException, CustomArgumentException {
        Path testFile = tempDir.resolve("seven_bits.bin");
        
        // 7bitów 1bajt
        try (BitOutputStream bos = new BitOutputStream(new FileOutputStream(testFile.toFile()))) {
            bos.writeBit(1);
            bos.writeBit(0);
            bos.writeBit(1);
            bos.writeBit(0);
            bos.writeBit(1);
            bos.writeBit(0);
            bos.writeBit(1);
            bos.writeByte(123);
            bos.flushAndGetPadding();
        }
        
        try (BitInputStream bis = new BitInputStream(testFile.toString())) {
            int[] bits = new int[7];
            for (int i = 0; i < 7; i++) {
                bits[i] = bis.readBit();
            }
            int byteValue = bis.readByte();
            
            assertArrayEquals(new int[]{1, 0, 1, 0, 1, 0, 1}, bits);
            assertEquals(123, byteValue);
        }
    }

    @Test
    void testMultipleBitByteSequences() throws IOException, CustomArgumentException {
        Path testFile = tempDir.resolve("multiple.bin");
        
        try (BitOutputStream bos = new BitOutputStream(new FileOutputStream(testFile.toFile()))) {
            bos.writeBit(1);
            bos.writeByte(65); // 'A'
            bos.writeBit(0);
            bos.writeByte(66); // 'B'
            bos.flushAndGetPadding();
        }
        
        try (BitInputStream bis = new BitInputStream(testFile.toString())) {
            int bit1 = bis.readBit();
            int byte1 = bis.readByte();
            int bit2 = bis.readBit();
            int byte2 = bis.readByte();
            
            assertEquals(1, bit1);
            assertEquals(65, byte1);
            assertEquals(0, bit2);
            assertEquals(66, byte2);
        }
    }

    @Test
    void testHuffmanTreePattern() throws IOException, CustomArgumentException {
        Path testFile = tempDir.resolve("huffman_pattern.bin");
        
        try (BitOutputStream bos = new BitOutputStream(new FileOutputStream(testFile.toFile()))) {
            bos.writeBit(0);     
            bos.writeBit(1);      
            bos.writeByte(1);     
            bos.writeByte(65);    
            bos.writeBit(1);      
            bos.writeByte(1);     
            bos.writeByte(66);    
            bos.flushAndGetPadding();
        }
        
        // Read the exact same pattern
        try (BitInputStream bis = new BitInputStream(testFile.toString())) {
            int rootBit = bis.readBit();
            assertEquals(0, rootBit);
            
            int leftBit = bis.readBit();
            assertEquals(1, leftBit);
            
            int leftLength = bis.readByte();
            assertEquals(1, leftLength);
            
            int leftSymbol = bis.readByte();
            assertEquals(65, leftSymbol);
            
            int rightBit = bis.readBit();
            assertEquals(1, rightBit);
            
            int rightLength = bis.readByte();
            assertEquals(1, rightLength);
            
            int rightSymbol = bis.readByte();
            assertEquals(66, rightSymbol);
        }
    }

    @Test
    void testReadBytesAfterBits() throws IOException, CustomArgumentException {
        Path testFile = tempDir.resolve("bytes_after_bits.bin");
        
        try (BitOutputStream bos = new BitOutputStream(new FileOutputStream(testFile.toFile()))) {
            bos.writeBit(0);
            bos.writeBit(1);
            bos.writeByte(1);     
            bos.writeByte(72);    // 'H'
            bos.writeByte(101);   // 'e'
            bos.writeByte(108);   // 'l'
            bos.writeByte(108);   // 'l'
            bos.writeByte(111);   // 'o'
            bos.flushAndGetPadding();
        }
        
        try (BitInputStream bis = new BitInputStream(testFile.toString())) {
            int bit1 = bis.readBit();
            int bit2 = bis.readBit();
            
            assertEquals(0, bit1, "First bit should be 0");
            assertEquals(1, bit2, "Second bit should be 1");
            
            int length = bis.readByte();
            assertEquals(1, length, "Length should be 1");
            
            byte[] buffer = new byte[5];
            int bytesRead = bis.readBytes(buffer, 5);
            assertEquals(5, bytesRead, "Should read 5 bytes");
            
            String result = new String(buffer);
            assertEquals("Hello", result, "Should read 'Hello'");
        }
    }

    @Test
    void testExactFailureCase() throws IOException, CustomArgumentException {
        Path testFile = tempDir.resolve("failure_case.bin");
        
        byte[] exactData = {
            (byte) 0x06, 
            (byte) 0x40,  // 01000000 
            (byte) 0x01,  // 00000001 
            (byte) 0x41,  // 01000001 
            (byte) 0x80,  // 10000000 
            (byte) 0x01,  // 00000001 
            (byte) 0x42,  // 01000010
            (byte) 0x40   // 01000000
        };
        
        java.nio.file.Files.write(testFile, exactData);
        
        try (BitInputStream bis = new BitInputStream(testFile.toString())) {
            int paddingBits = bis.readByte();
            assertEquals(6, paddingBits);
            
            int rootBit = bis.readBit();
            assertEquals(0, rootBit);
            
            int leftBit = bis.readBit();
            assertEquals(1, leftBit);
            
            int leftLength = bis.readByte();
            
            assertEquals(1, leftLength);
        }
    }
}
