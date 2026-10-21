package pl.edu.pw.ee.aisd2025zex5.huffman;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import pl.edu.pw.ee.aisd2025zex5.cmdargs.ProgramConfig;
import pl.edu.pw.ee.aisd2025zex5.cmdargs.CustomArgumentException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HuffmanCompressorTest {

    @TempDir
    Path tempDir;

    @Test
    void testCompressSimpleFile() throws IOException, CustomArgumentException {
        Path sourceFile = tempDir.resolve("test.txt");
        Files.write(sourceFile, "AAABBC".getBytes());
        
        Path compressedFile = tempDir.resolve("compressed.comp");
        
        ProgramConfig config = new ProgramConfig();
        config.setSourcePath(sourceFile.toString());
        config.setDestPath(compressedFile.toString());
        config.setMaxLength(1);
        
        HuffmanCompressor compressor = new HuffmanCompressor();
        assertDoesNotThrow(() -> compressor.compress(config));
        
        assertTrue(Files.exists(compressedFile));
        assertTrue(Files.size(compressedFile) > 0);
    }

    @Test
    void testCompressEmptyFile() throws IOException {
        Path sourceFile = tempDir.resolve("empty.txt");
        Files.write(sourceFile, new byte[0]);
        
        Path compressedFile = tempDir.resolve("compressed.comp");
        
        ProgramConfig config = new ProgramConfig();
        config.setSourcePath(sourceFile.toString());
        config.setDestPath(compressedFile.toString());
        config.setMaxLength(1);
        
        HuffmanCompressor compressor = new HuffmanCompressor();
        
        assertThrows(CustomArgumentException.class, () -> compressor.compress(config),
                "Should throw exception for empty file");
    }

    @Test
    void testCompressWithDifferentLValues() throws IOException, CustomArgumentException {
        Path sourceFile = tempDir.resolve("test.txt");
        Files.write(sourceFile, "ABCABCABC".getBytes());
        
        for (int l = 1; l <= 3; l++) {
            Path compressedFile = tempDir.resolve("compressed_L" + l + ".comp");
            
            ProgramConfig config = new ProgramConfig();
            config.setSourcePath(sourceFile.toString());
            config.setDestPath(compressedFile.toString());
            config.setMaxLength(l);
            
            HuffmanCompressor compressor = new HuffmanCompressor();
            assertDoesNotThrow(() -> compressor.compress(config),
                    "Compression should work with L=" + l);
            
            assertTrue(Files.exists(compressedFile));
        }
    }

    @Test
    void testCompressNonExistentFile() {
        Path sourceFile = tempDir.resolve("nonexistent.txt");
        Path compressedFile = tempDir.resolve("compressed.comp");
        
        ProgramConfig config = new ProgramConfig();
        config.setSourcePath(sourceFile.toString());
        config.setDestPath(compressedFile.toString());
        config.setMaxLength(1);
        
        HuffmanCompressor compressor = new HuffmanCompressor();
        
        assertThrows(IOException.class, () -> compressor.compress(config),
                "Should throw IOException for non-existent source file");
    }

    @Test
    void testCompressInvalidMaxLength() throws IOException {
        Path sourceFile = tempDir.resolve("test.txt");
        Files.write(sourceFile, "ABC".getBytes());
        
        Path compressedFile = tempDir.resolve("compressed.comp");
        
        ProgramConfig config = new ProgramConfig();
        config.setSourcePath(sourceFile.toString());
        config.setDestPath(compressedFile.toString());
        config.setMaxLength(0);
        
        HuffmanCompressor compressor = new HuffmanCompressor();
        
        assertThrows(CustomArgumentException.class, () -> compressor.compress(config),
                "Should throw exception for invalid L value");
    }

    @Test
    void testCompressSingleCharacterFile() throws IOException, CustomArgumentException {
        Path sourceFile = tempDir.resolve("single.txt");
        Files.write(sourceFile, "A".getBytes());
        
        Path compressedFile = tempDir.resolve("compressed.comp");
        
        ProgramConfig config = new ProgramConfig();
        config.setSourcePath(sourceFile.toString());
        config.setDestPath(compressedFile.toString());
        config.setMaxLength(1);
        
        HuffmanCompressor compressor = new HuffmanCompressor();
        assertDoesNotThrow(() -> compressor.compress(config));
        
        assertTrue(Files.exists(compressedFile));
    }
}
