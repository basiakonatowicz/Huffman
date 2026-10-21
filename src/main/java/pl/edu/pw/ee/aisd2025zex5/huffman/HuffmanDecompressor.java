package pl.edu.pw.ee.aisd2025zex5.huffman;

import pl.edu.pw.ee.aisd2025zex5.cmdargs.ProgramConfig;
import pl.edu.pw.ee.aisd2025zex5.cmdargs.CustomArgumentException;
import pl.edu.pw.ee.aisd2025zex5.io.BitInputStream;
import java.io.IOException;
import java.io.FileOutputStream;

public class HuffmanDecompressor {

    private HuffmanNode readHeader(BitInputStream bis) throws IOException, CustomArgumentException {
        int bit = bis.readBit();

        if (bit == -1) {
            throw new CustomArgumentException("Niekompletny nagłówek pliku skompresowanego.");
        }

        if (bit == 1) {
            int length = bis.readByte();

            if (length == -1 || length == 0) {
                throw new CustomArgumentException("Niekompletny nagłówek (brak długości lub l=0).");
            }

            byte[] symbol = new byte[length];
 
            int bytesRead = bis.getInnerStream().read(symbol);

            if (bytesRead != length) {
                throw new CustomArgumentException("Niekompletny nagłówek (brak bajtów l-gramu).");
            }

            return new HuffmanNode(symbol, 0);

        } else if (bit == 0) {
 
            HuffmanNode left = readHeader(bis);

            HuffmanNode right = readHeader(bis);

            return new HuffmanNode(0, left, right); // dla wezlow wewnetrznych czestosc = 0
        }

        throw new CustomArgumentException("Nieznany bit w nagłówku: " + bit);
    }

    public void decompress(ProgramConfig config) throws IOException, CustomArgumentException {

        try (BitInputStream bis = new BitInputStream(config.getSourcePath()); FileOutputStream fos = new FileOutputStream(config.getDestPath())) {

            int paddingBits = bis.readByte();
            if (paddingBits == -1) {
                throw new CustomArgumentException("Plik skompresowany jest pusty.");
            }
            HuffmanNode root = readHeader(bis);

            if (root == null) {
                return;
            }

            HuffmanNode currentNode = root;
            int bit;

            while ((bit = bis.readBit()) != -1) {

                currentNode = (bit == 0) ? currentNode.getLeft() : currentNode.getRight();

                // Jeśli dotarliśmy do liścia, mamy zdekodowany symbol
                if (currentNode.isLeaf()) {

                    // Zapisz l-gram do pliku wyjściowego
                    fos.write(currentNode.getSymbol());

                    // Wróć do korzenia, aby rozpocząć dekodowanie następnego symbolu
                    currentNode = root;
                }
            }

            if (currentNode != root && !currentNode.isLeaf()) {
            }

        } 
    }
}
