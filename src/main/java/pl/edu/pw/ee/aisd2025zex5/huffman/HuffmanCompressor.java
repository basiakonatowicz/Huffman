package pl.edu.pw.ee.aisd2025zex5.huffman;

import pl.edu.pw.ee.aisd2025zex5.cmdargs.CustomArgumentException;
import pl.edu.pw.ee.aisd2025zex5.cmdargs.ProgramConfig;
import pl.edu.pw.ee.aisd2025zex5.io.BitOutputStream;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.Arrays;
import pl.edu.pw.ee.aisd2025zex5.utils.ByteArrayWrapper;


public class HuffmanCompressor {

    // zapis nagłówka preorder
    //ormat: liść: 1, długość symboli (1 bajt, max 255), bajty lsymbolu, węzeł wewnętrzny: 0.

    private void writeHeader(HuffmanNode node, BitOutputStream bos) throws IOException, CustomArgumentException {
        if (node.isLeaf()) {
            bos.writeBit(1);

            byte[] symbol = node.getSymbol();

            if (symbol.length > 255) {
                throw new CustomArgumentException("Dlugosc l przekracza 255 (limit 1 bajta na dlugosc).");
            }
            // zapis długości 
            bos.writeByte(symbol.length);

            // zapis faktycznych bajtów 
            bos.writeBytes(symbol);
        } else {
            bos.writeBit(0);

            if (node.getLeft() != null) {
                writeHeader(node.getLeft(), bos);
            }
            if (node.getRight() != null) {
                writeHeader(node.getRight(), bos);
            }
        }
    }

    public void compress(ProgramConfig config) throws IOException, CustomArgumentException {

        FrequencyCollector collector = new FrequencyCollector();
        DoubleHashingHashMap<ByteArrayWrapper, Long> frequencies = collector.collectFrequencies(config);

        HuffmanTreeBuilder treeBuilder = new HuffmanTreeBuilder();
        HuffmanNode root = treeBuilder.buildTree(frequencies);

        if (root == null) {
            throw new CustomArgumentException("Nie można skompresować pustego pliku.");
        }

        CodeGenerator codeGenerator = new CodeGenerator();
        DoubleHashingHashMap<ByteArrayWrapper, String> codeMap = codeGenerator.generateCodes(root);

        // Odczyt pliku źródłowego
        byte[] fileBytes;
        try (FileInputStream fis = new FileInputStream(config.getSourcePath())) {
            fileBytes = fis.readAllBytes();
        }

        int l = config.getMaxLength();
        int currentPos = 0;

        // zapis naglowka i danych do 
        ByteArrayOutputStream bufferOutput = new ByteArrayOutputStream();
        int paddingBits = 0; 

        try (BitOutputStream bos = new BitOutputStream(bufferOutput)) {

            // zapis naglowka
            writeHeader(root, bos);

            // zapis zakodowanych danych
            while (currentPos < fileBytes.length) {

                int blockSize = l;

                // Określenie rozmiaru bloku (L lub reszta dla ostatniego bloku)
                if (currentPos + l > fileBytes.length) {
                    blockSize = fileBytes.length - currentPos;
                }

                // Wytnij blok bajtów i użyj go jako klucza
                byte[] subArray = Arrays.copyOfRange(fileBytes, currentPos, currentPos + blockSize);
                ByteArrayWrapper key = new ByteArrayWrapper(subArray);

                String code = codeMap.get(key);

                if (code == null) {
                    throw new CustomArgumentException("Błąd: Nie znaleziono kodu dla bloku w trakcie kompresji.");
                }

                bos.writeCode(code);
                currentPos += blockSize; 
            }

            // Zapis ostatniego niepełnego bajta i pobranie liczby zer dopełniających
            paddingBits = bos.flushAndGetPadding();
        }

        //Zapis do pliku docelowego
        try (FileOutputStream fos = new FileOutputStream(config.getDestPath())) {

            fos.write(paddingBits);

            bufferOutput.writeTo(fos);
        }
    }
}
