package pl.edu.pw.ee.aisd2025zex5.huffman;


 //liśc ( symbol i częstość) lub węzeł wewnętrzny (częstość i dzieci).

public class HuffmanNode implements Comparable<HuffmanNode> {

    private final byte[] symbol; 
    
    //  wystąpienia symbolu lub suma częstości dzieci dla węzłów wewnętrznych
    private final long frequency; 
    
    private HuffmanNode left;
    private HuffmanNode right;

    public HuffmanNode(byte[] symbol, long frequency) {
        this.symbol = symbol;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }

    public HuffmanNode(long frequency, HuffmanNode left, HuffmanNode right) {
        this.symbol = null; 
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }
    
    public boolean isLeaf() {
        return symbol != null;
    }

    // Porównuje węzły na podstawie częstości. Węzły o mniejszej częstości mają wyższy priorytet.
    
    @Override
    public int compareTo(HuffmanNode other) {
        // Używamy Long.compare, aby uniknąć przepełnienia i uzyskać porządek rosnący
        return Long.compare(this.frequency, other.frequency);
    }
    
    public long getFrequency() {
        return frequency;
    }
    
    public byte[] getSymbol() {
        return symbol;
    }

    public HuffmanNode getLeft() {
        return left;
    }

    public HuffmanNode getRight() {
        return right;
    }
}
