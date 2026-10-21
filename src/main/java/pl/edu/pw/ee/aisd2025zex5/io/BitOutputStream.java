package pl.edu.pw.ee.aisd2025zex5.io;

import pl.edu.pw.ee.aisd2025zex5.cmdargs.CustomArgumentException;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class BitOutputStream implements AutoCloseable {
    private final BufferedOutputStream os;
    private int buffer;
    private int bitCount;

    public BitOutputStream(OutputStream os) {
        this.os = new BufferedOutputStream(os);
        this.buffer = 0;
        this.bitCount = 0;
    }
    // Zapis pojedynczego bajta
    public void writeByte(int b) throws IOException {
        // czy sa jakies resztki bitow
        if (bitCount > 0) {
            os.write(buffer << (8 - bitCount));
        }
        os.write(b);
        buffer = 0;
        bitCount = 0;
    }
    // zapis tabloicy bajtow
    public void writeBytes(byte[] bytes) throws IOException {
        // czy sa jakies resztki bitow
        if (bitCount > 0) {
            os.write(buffer << (8 - bitCount));
        }
        os.write(bytes);
        buffer = 0;
        bitCount = 0;
    }
    // 
    public void writeBit(int bit) throws IOException, CustomArgumentException {
        if (bit != 0 && bit != 1) {
            throw new CustomArgumentException("Można zapisywać tylko bity 0 lub 1.");
        }
        // zapis bitu od lewej strony buffer
        buffer = (buffer << 1) | bit;
        bitCount++;
        // czyszczenie buffer i bitCount
        if (bitCount == 8) {
            os.write(buffer);
            buffer = 0;
            bitCount = 0;
        }
    }

    public void writeCode(String code) throws IOException, CustomArgumentException {
        for (char c : code.toCharArray()) {
            if (c == '0') {
                writeBit(0);
            } else if (c == '1') {
                writeBit(1);
            } else {
                throw new CustomArgumentException("Kod binarny zawiera nieprawidłowy znak.");
            }
        }
    }
    // dopelnia zerami i zwraca ile dopisal zer
    public int flushAndGetPadding() throws IOException {
        if (bitCount == 0) {
            return 0;
        }
        
        int paddingBits = 8 - bitCount;
        
        buffer <<= paddingBits; 
        os.write(buffer);
        
        os.flush();
        
        buffer = 0;
        bitCount = 0;
        return paddingBits;
    }
    public BufferedOutputStream getInnerStream() {
        return os;
    }
    @Override
    public void close() throws IOException {
        os.close();
    }
}