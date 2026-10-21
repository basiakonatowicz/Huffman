package pl.edu.pw.ee.aisd2025zex5.cmdargs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArgumentsParserTest {

    private ArgumentsParser parser = new ArgumentsParser();

    @Test
    void testValidCompressionArguments() throws CustomArgumentException {
        String[] args = {"-m", "comp", "-s", "input.txt", "-d", "output.comp"};
        
        ProgramConfig config = parser.parse(args);
        
        assertEquals(ProgramConfig.Mode.COMP, config.getMode());
        assertEquals("input.txt", config.getSourcePath());
        assertEquals("output.comp", config.getDestPath());
    }

    @Test
    void testValidDecompressionArguments() throws CustomArgumentException {
        String[] args = {"-m", "decomp", "-s", "input.comp", "-d", "output.txt"};
        
        ProgramConfig config = parser.parse(args);
        
        assertEquals(ProgramConfig.Mode.DECOMP, config.getMode());
        assertEquals("input.comp", config.getSourcePath());
        assertEquals("output.txt", config.getDestPath());
    }

    @Test
    void testArgumentsWithMaxLength() throws CustomArgumentException {
        String[] args = {"-m", "comp", "-s", "input.txt", "-d", "output.comp", "-l", "5"};
        
        ProgramConfig config = parser.parse(args);
        
        assertEquals(ProgramConfig.Mode.COMP, config.getMode());
        assertEquals("input.txt", config.getSourcePath());
        assertEquals("output.comp", config.getDestPath());
        assertEquals(5, config.getMaxLength());
    }

    @Test
    void testArgumentsInDifferentOrder() throws CustomArgumentException {
        String[] args = {"-s", "input.txt", "-l", "3", "-m", "comp", "-d", "output.comp"};
        
        ProgramConfig config = parser.parse(args);
        
        assertEquals(ProgramConfig.Mode.COMP, config.getMode());
        assertEquals("input.txt", config.getSourcePath());
        assertEquals("output.comp", config.getDestPath());
        assertEquals(3, config.getMaxLength());
    }

    @Test
    void testMissingMode() {
        String[] args = {"-s", "input.txt", "-d", "output.comp"};
        
        assertThrows(CustomArgumentException.class, () -> parser.parse(args),
                "Powinien zostać rzucony wyjątek, gdy brakuje trybu (-m)");
    }

    @Test
    void testMissingSourcePath() {
        String[] args = {"-m", "comp", "-d", "output.comp"};
        
        assertThrows(CustomArgumentException.class, () -> parser.parse(args),
                "Powinien zostać rzucony wyjątek, gdy brakuje ścieżki źródłowej (-s)");
    }

    @Test
    void testMissingDestinationPath() {
        String[] args = {"-m", "comp", "-s", "input.txt"};
        
        assertThrows(CustomArgumentException.class, () -> parser.parse(args),
                "Powinien zostać rzucony wyjątek, gdy brakuje ścieżki docelowej (-d)");
    }

    @Test
    void testInvalidMode() {
        String[] args = {"-m", "invalid", "-s", "input.txt", "-d", "output.comp"};
        
        assertThrows(CustomArgumentException.class, () -> parser.parse(args),
                "Powinien zostać rzucony wyjątek dla nieprawidłowego trybu");
    }

    @Test
    void testCaseInsensitiveMode() throws CustomArgumentException {
        String[] args1 = {"-m", "COMP", "-s", "input.txt", "-d", "output.comp"};
        String[] args2 = {"-m", "Decomp", "-s", "input.comp", "-d", "output.txt"};
        
        ProgramConfig config1 = parser.parse(args1);
        ProgramConfig config2 = parser.parse(args2);
        
        assertEquals(ProgramConfig.Mode.COMP, config1.getMode());
        assertEquals(ProgramConfig.Mode.DECOMP, config2.getMode());
    }

    @Test
    void testMissingValueAfterFlag() {
        String[] args1 = {"-m"};
        String[] args2 = {"-m", "comp", "-s"};
        String[] args3 = {"-m", "comp", "-s", "input.txt", "-d"};
        String[] args4 = {"-m", "comp", "-s", "input.txt", "-d", "output.comp", "-l"};
        
        assertThrows(CustomArgumentException.class, () -> parser.parse(args1),
                "Powinien zostać rzucony wyjątek, gdy brakuje wartości po -m");
        assertThrows(CustomArgumentException.class, () -> parser.parse(args2),
                "Powinien zostać rzucony wyjątek, gdy brakuje wartości po -s");
        assertThrows(CustomArgumentException.class, () -> parser.parse(args3),
                "Powinien zostać rzucony wyjątek, gdy brakuje wartości po -d");
        assertThrows(CustomArgumentException.class, () -> parser.parse(args4),
                "Powinien zostać rzucony wyjątek, gdy brakuje wartości po -l");
    }

    @Test
    void testInvalidMaxLength() {
        String[] args = {"-m", "comp", "-s", "input.txt", "-d", "output.comp", "-l", "abc"};
        
        assertThrows(CustomArgumentException.class, () -> parser.parse(args),
                "Powinien zostać rzucony wyjątek dla nieprawidłowej wartości długości");
    }

    @Test
    void testUnknownOption() {
        String[] args = {"-m", "comp", "-s", "input.txt", "-d", "output.comp", "-x", "unknown"};
        
        assertThrows(CustomArgumentException.class, () -> parser.parse(args),
                "Powinien zostać rzucony wyjątek dla nieznanej opcji");
    }

    @Test
    void testEmptyArguments() {
        String[] args = {};
        
        assertThrows(CustomArgumentException.class, () -> parser.parse(args),
                "Powinien zostać rzucony wyjątek dla pustej listy argumentów");
    }

    @Test
    void testNegativeMaxLength() {
        String[] args = {"-m", "comp", "-s", "input.txt", "-d", "output.comp", "-l", "-5"};
        
        assertDoesNotThrow(() -> parser.parse(args),
                "Parser powinien akceptować liczby ujemne (walidacja następuje później)");
    }

    @Test
    void testZeroMaxLength() throws CustomArgumentException {
        String[] args = {"-m", "comp", "-s", "input.txt", "-d", "output.comp", "-l", "0"};
        
        ProgramConfig config = parser.parse(args);
        assertEquals(0, config.getMaxLength());
    }
}
