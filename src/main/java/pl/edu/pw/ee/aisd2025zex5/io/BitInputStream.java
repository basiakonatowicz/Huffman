package pl.edu.pw.ee.aisd2025zex5.io;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import pl.edu.pw.ee.aisd2025zex5.cmdargs.CustomArgumentException;

public class BitInputStream implements AutoCloseable {

    private final BufferedInputStream is;
    private int buffer;
    private int bitCount;

    public BitInputStream(String filePath) throws IOException {
        this.is = new BufferedInputStream(new FileInputStream(filePath));
        this.buffer = 0;
        this.bitCount = 0;
    }

    public int readByte() throws IOException, CustomArgumentException {
        // Jeśli mamy pozostałe bity w buforze, oznacza to że jesteśmy w środku bajta
        // Musimy dokończyć czytanie tego bajta (konsumując pozostałe bity jako padding)
        // a następnie przeczytać kolejny pełny bajt
        if (bitCount != 0) {
            while (bitCount > 0) {
                readBit(); 
            }
        }
        return is.read();
    }

    public int readBit() throws IOException {
        if (bitCount == 0) {
            buffer = is.read();
            if (buffer == -1) {
                return -1; // EOF
            }
            bitCount = 8; 
        }

        // odczytanie najbardziej znaczącego bitu 
        bitCount--;
        int bit = (buffer >> bitCount) & 1;

        return bit;
    }

    public int readBytes(byte[] buffer, int length) throws IOException, CustomArgumentException {
        // czy nie zostaly bity zanim przeczytamy bajt
        if (bitCount != 0) {
            while (bitCount > 0) {
                readBit();
            }
        }
        return is.read(buffer, 0, length);
    }

    public BufferedInputStream getInnerStream() {
        return is;
    }

    @Override
    public void close() throws IOException {
        is.close();
    }
}