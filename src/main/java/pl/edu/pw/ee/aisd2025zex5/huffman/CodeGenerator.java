package pl.edu.pw.ee.aisd2025zex5.huffman;

import pl.edu.pw.ee.aisd2025zex5.utils.ByteArrayWrapper;

// mapa kodów binarnych dla symboli na podstwie drzewa
public class CodeGenerator {

    public DoubleHashingHashMap<ByteArrayWrapper, String> generateCodes(HuffmanNode root) {
        DoubleHashingHashMap<ByteArrayWrapper, String> codeMap = new DoubleHashingHashMap<>(); 
        if (root != null) {
            traverseTree(root, "", codeMap);
        }
        return codeMap;
    }

    private void traverseTree(HuffmanNode node, String code, MyHashMap<ByteArrayWrapper, String> codeMap) {
        
        if (node.isLeaf()) {
            codeMap.put(new ByteArrayWrapper(node.getSymbol()), code); 
            return;
        }

        if (node.getLeft() != null) {
            traverseTree(node.getLeft(), code + "0", codeMap);
        }

        if (node.getRight() != null) {
            traverseTree(node.getRight(), code + "1", codeMap);
        }
    }
}