package pl.edu.pw.ee.aisd2025zex5.huffman;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import pl.edu.pw.ee.aisd2025zex5.cmdargs.ProgramConfig;
import pl.edu.pw.ee.aisd2025zex5.cmdargs.CustomArgumentException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HuffmanDecompressorTest {

    @TempDir
    Path tempDir;

    @Test
    void testDecompressNonExistentFile() {
        Path sourceFile = tempDir.resolve("nonexistent.comp");
        Path decompressedFile = tempDir.resolve("decompressed.txt");
        
        ProgramConfig config = new ProgramConfig();
        config.setSourcePath(sourceFile.toString());
        config.setDestPath(decompressedFile.toString());
        
        HuffmanDecompressor decompressor = new HuffmanDecompressor();
        
        assertThrows(IOException.class, () -> decompressor.decompress(config),
                "Powinien zostać rzucony IOException dla nieistniejącego pliku źródłowego");
    }

    @Test
    void testDecompressEmptyFile() throws IOException {
        Path sourceFile = tempDir.resolve("empty.comp");
        Files.write(sourceFile, new byte[0]);
        
        Path decompressedFile = tempDir.resolve("decompressed.txt");
        
        ProgramConfig config = new ProgramConfig();
        config.setSourcePath(sourceFile.toString());
        config.setDestPath(decompressedFile.toString());
        
        HuffmanDecompressor decompressor = new HuffmanDecompressor();
        
        assertThrows(CustomArgumentException.class, () -> decompressor.decompress(config),
                "Powinien zostać rzucony wyjątek dla pustego pliku skompresowanego");
    }

    @Test
    void testDecompressInvalidHeader() throws IOException {
        Path sourceFile = tempDir.resolve("invalid.comp");
        Files.write(sourceFile, new byte[]{0, 1, 2, 3, 4});
        
        Path decompressedFile = tempDir.resolve("decompressed.txt");
        
        ProgramConfig config = new ProgramConfig();
        config.setSourcePath(sourceFile.toString());
        config.setDestPath(decompressedFile.toString());
        
        HuffmanDecompressor decompressor = new HuffmanDecompressor();
        
        assertThrows(CustomArgumentException.class, () -> decompressor.decompress(config),
                "Powinien zostać rzucony wyjątek dla niepoprawnego nagłówka pliku");
    }

    @Test
    void testDecompressCorruptedFile() throws IOException {
        Path sourceFile = tempDir.resolve("corrupted.comp");
        Files.write(sourceFile, new byte[]{0, 1}); 
        
        Path decompressedFile = tempDir.resolve("decompressed.txt");
        
        ProgramConfig config = new ProgramConfig();
        config.setSourcePath(sourceFile.toString());
        config.setDestPath(decompressedFile.toString());
        
        HuffmanDecompressor decompressor = new HuffmanDecompressor();
        
        assertThrows(CustomArgumentException.class, () -> decompressor.decompress(config),
                "Powinien zostać rzucony wyjątek dla uszkodzonego pliku");
    }

    @Test
    void testDecompressToInvalidPath() throws IOException {
        Path sourceFile = tempDir.resolve("test.comp");
        Files.write(sourceFile, new byte[]{0, 1, 1, 65});
        
        Path invalidDir = tempDir.resolve("nonexistent_dir");
        Path decompressedFile = invalidDir.resolve("decompressed.txt");
        
        ProgramConfig config = new ProgramConfig();
        config.setSourcePath(sourceFile.toString());
        config.setDestPath(decompressedFile.toString());
        
        HuffmanDecompressor decompressor = new HuffmanDecompressor();
        
        assertThrows(IOException.class, () -> decompressor.decompress(config),
                "Powinien zostać rzucony IOException, gdy katalog wyjściowy nie istnieje");
    }
}

