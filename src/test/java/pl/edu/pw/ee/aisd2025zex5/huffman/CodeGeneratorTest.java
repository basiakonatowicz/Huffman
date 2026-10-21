package pl.edu.pw.ee.aisd2025zex5.huffman;

import org.junit.jupiter.api.Test;
import pl.edu.pw.ee.aisd2025zex5.cmdargs.CustomArgumentException;
import static org.junit.jupiter.api.Assertions.*;
import pl.edu.pw.ee.aisd2025zex5.utils.ByteArrayWrapper;

public class CodeGeneratorTest {

    private HuffmanTreeBuilder builder = new HuffmanTreeBuilder();
    private CodeGenerator generator = new CodeGenerator();

    private DoubleHashingHashMap<ByteArrayWrapper, Long> createFreqMap(String[] symbols, long[] freqs) {
        DoubleHashingHashMap<ByteArrayWrapper, Long> map = new DoubleHashingHashMap<>();
        for (int i = 0; i < symbols.length; i++) {
            map.put(new ByteArrayWrapper(symbols[i].getBytes()), freqs[i]);
        }
        return map;
    }

    @Test
    void testStandardHuffmanCodes() throws CustomArgumentException {
        String[] symbols = {"A", "B", "C"};
        long[] freqs = {3L, 2L, 1L};
        DoubleHashingHashMap<ByteArrayWrapper, Long> freqMap = createFreqMap(symbols, freqs);

        HuffmanNode root = builder.buildTree(freqMap);
        DoubleHashingHashMap<ByteArrayWrapper, String> codeMap = generator.generateCodes(root);

        String codeA = codeMap.get(new ByteArrayWrapper("A".getBytes()));
        String codeB = codeMap.get(new ByteArrayWrapper("B".getBytes()));
        String codeC = codeMap.get(new ByteArrayWrapper("C".getBytes()));

        assertNotNull(codeA, "Kod dla symbolu A powinien istnieć");
        assertNotNull(codeB, "Kod dla symbolu B powinien istnieć");
        assertNotNull(codeC, "Kod dla symbolu C powinien istnieć");

        assertTrue(codeA.matches("[01]+"), "Kod A powinien być binarny");
        assertTrue(codeB.matches("[01]+"), "Kod B powinien być binarny");
        assertTrue(codeC.matches("[01]+"), "Kod C powinien być binarny");

        assertFalse(codeA.startsWith(codeB) || codeB.startsWith(codeA),
                "Kody A i B nie powinny być prefiksami");
        assertFalse(codeA.startsWith(codeC) || codeC.startsWith(codeA),
                "Kody A i C nie powinny być prefiksami");
        assertFalse(codeB.startsWith(codeC) || codeC.startsWith(codeB),
                "Kody B i C nie powinny być prefiksami");
    }

    @Test
    void testSingleSymbol() throws CustomArgumentException {
        String[] symbols = {"X"};
        long[] freqs = {5L};
        DoubleHashingHashMap<ByteArrayWrapper, Long> freqMap = createFreqMap(symbols, freqs);

        HuffmanNode root = builder.buildTree(freqMap);
        DoubleHashingHashMap<ByteArrayWrapper, String> codeMap = generator.generateCodes(root);

        String codeX = codeMap.get(new ByteArrayWrapper("X".getBytes()));
        assertNotNull(codeX, "Kod dla pojedynczego symbolu powinien istnieć");
        assertTrue(codeX.matches("[01]+"), "Kod powinien być binarny");
    }

    @Test
    void testTwoSymbols() throws CustomArgumentException {
        String[] symbols = {"X", "Y"};
        long[] freqs = {3L, 2L};
        DoubleHashingHashMap<ByteArrayWrapper, Long> freqMap = createFreqMap(symbols, freqs);

        HuffmanNode root = builder.buildTree(freqMap);
        DoubleHashingHashMap<ByteArrayWrapper, String> codeMap = generator.generateCodes(root);

        String codeX = codeMap.get(new ByteArrayWrapper("X".getBytes()));
        String codeY = codeMap.get(new ByteArrayWrapper("Y".getBytes()));

        assertNotNull(codeX, "Kod dla X powinien istnieć");
        assertNotNull(codeY, "Kod dla Y powinien istnieć");
        assertNotEquals(codeX, codeY, "Kody powinny być różne");

        assertTrue(
                (codeX.equals("0") && codeY.equals("1")) ||
                (codeX.equals("1") && codeY.equals("0")),
                "Dla dwóch symboli kody powinny wynosić 0 i 1"
        );
    }

    @Test
    void testMultiByteSymbols() throws CustomArgumentException {
        DoubleHashingHashMap<ByteArrayWrapper, Long> freqMap = new DoubleHashingHashMap<>();
        freqMap.put(new ByteArrayWrapper(new byte[]{65, 66}), 3L); // AB
        freqMap.put(new ByteArrayWrapper(new byte[]{67, 68}), 2L); // CD
        freqMap.put(new ByteArrayWrapper(new byte[]{69}), 1L);     // E

        HuffmanNode root = builder.buildTree(freqMap);
        DoubleHashingHashMap<ByteArrayWrapper, String> codeMap = generator.generateCodes(root);

        String codeAB = codeMap.get(new ByteArrayWrapper(new byte[]{65, 66}));
        String codeCD = codeMap.get(new ByteArrayWrapper(new byte[]{67, 68}));
        String codeE = codeMap.get(new ByteArrayWrapper(new byte[]{69}));

        assertNotNull(codeAB, "Kod dla AB powinien istnieć");
        assertNotNull(codeCD, "Kod dla CD powinien istnieć");
        assertNotNull(codeE, "Kod dla E powinien istnieć");

        assertFalse(codeAB.startsWith(codeCD) || codeCD.startsWith(codeAB),
                "Kody AB i CD nie powinny być prefiksami");
        assertFalse(codeAB.startsWith(codeE) || codeE.startsWith(codeAB),
                "Kody AB i E nie powinny być prefiksami");
        assertFalse(codeCD.startsWith(codeE) || codeE.startsWith(codeCD),
                "Kody CD i E nie powinny być prefiksami");
    }

    @Test
    void testNullRoot() {
        DoubleHashingHashMap<ByteArrayWrapper, String> codeMap = generator.generateCodes(null);
        assertNotNull(codeMap, "Mapa kodów nie powinna być nullem dla pustego drzewa");
        assertEquals(0, codeMap.getEntries().size(),
                "Mapa kodów powinna być pusta dla pustego drzewa");
    }

    @Test
    void testCodeLengthOptimality() throws CustomArgumentException {
        String[] symbols = {"A", "B", "C", "D"};
        long[] freqs = {8L, 4L, 2L, 1L};
        DoubleHashingHashMap<ByteArrayWrapper, Long> freqMap = createFreqMap(symbols, freqs);

        HuffmanNode root = builder.buildTree(freqMap);
        DoubleHashingHashMap<ByteArrayWrapper, String> codeMap = generator.generateCodes(root);

        String codeA = codeMap.get(new ByteArrayWrapper("A".getBytes()));
        String codeB = codeMap.get(new ByteArrayWrapper("B".getBytes()));
        String codeC = codeMap.get(new ByteArrayWrapper("C".getBytes()));
        String codeD = codeMap.get(new ByteArrayWrapper("D".getBytes()));

        assertTrue(codeA.length() <= codeB.length(),
                "A (częstość 8) powinien mieć krótszy lub równy kod niż B (częstość 4)");
        assertTrue(codeB.length() <= codeC.length(),
                "B (częstość 4) powinien mieć krótszy lub równy kod niż C (częstość 2)");
        assertTrue(codeC.length() <= codeD.length(),
                "C (częstość 2) powinien mieć krótszy lub równy kod niż D (częstość 1)");
    }
}

