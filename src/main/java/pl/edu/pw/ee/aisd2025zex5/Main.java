package pl.edu.pw.ee.aisd2025zex5;

import pl.edu.pw.ee.aisd2025zex5.cmdargs.ArgumentsParser;
import pl.edu.pw.ee.aisd2025zex5.cmdargs.CustomArgumentException;
import pl.edu.pw.ee.aisd2025zex5.cmdargs.ProgramConfig;
import pl.edu.pw.ee.aisd2025zex5.huffman.HuffmanCompressor;
import pl.edu.pw.ee.aisd2025zex5.huffman.HuffmanDecompressor;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            ArgumentsParser parser = new ArgumentsParser();
            ProgramConfig config = parser.parse(args);

            if (config.getMode() == ProgramConfig.Mode.COMP) {
                HuffmanCompressor compressor = new HuffmanCompressor();
                compressor.compress(config);
                System.out.println("Kompresja zakonczona pomyslnie.");
                
            } else if (config.getMode() == ProgramConfig.Mode.DECOMP) {
                HuffmanDecompressor decompressor = new HuffmanDecompressor();
                decompressor.decompress(config);
                System.out.println("Dekompresja zakonczona pomyslnie.");
                
            } 

        } catch (CustomArgumentException e) {
            System.err.println("Blad: Nieprawidlowe uzycie programu lub danych: " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Blad IO (odczytu/zapisu pliku): " + e.getMessage());
            //e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Wystapil nieznany blad: " + e.getMessage());
            System.exit(1);
        }
    }
}
