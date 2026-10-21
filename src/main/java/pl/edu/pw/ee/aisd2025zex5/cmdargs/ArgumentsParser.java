package pl.edu.pw.ee.aisd2025zex5.cmdargs;

public class ArgumentsParser {



    public ProgramConfig parse(String[] args) throws CustomArgumentException {
        ProgramConfig config = new ProgramConfig();

        for (int i = 0; i < args.length; i++) {

            boolean isLastArg = (i + 1 >= args.length);

            switch (args[i]) {
                case "-m": {
                    if (isLastArg) {
                        throw new CustomArgumentException("Brak wartosci po argumencie -m.");
                    }
                    String m = args[++i];
                    if (m.equalsIgnoreCase("comp")) {
                        config.setMode(ProgramConfig.Mode.COMP);
                    } else if (m.equalsIgnoreCase("decomp")) { 
                        config.setMode(ProgramConfig.Mode.DECOMP);
                    } else {
                        throw new CustomArgumentException("Nieznany tryb: " + m + ". Oczekiwano 'comp' lub 'decomp'.");
                    }
                    break;
                }

                case "-s": {
                    if (isLastArg) {
                        throw new CustomArgumentException("Brak sciezki pliku zródlowego po opcji -s.");
                    }
                    config.setSourcePath(args[++i]);
                    break;
                }

                case "-d": {
                    if (isLastArg) {
                        throw new CustomArgumentException("Brak sciezki pliku docelowego po opcji -d.");
                    }
                    config.setDestPath(args[++i]);
                    break;
                }

                case "-l": {
                    try {
                        if (isLastArg) {
                            throw new CustomArgumentException("Brak wartosci dla argumentu -l.");
                        }
                        config.setMaxLength(Integer.parseInt(args[++i])); 
                    } catch (NumberFormatException e) {
                        throw new CustomArgumentException("Dlugosc (-l) musi byc liczba calkowita.");
                    }
                    break;
                }

                default: {
                    throw new CustomArgumentException("Nieznana opcja: " + args[i]);
                }
            }
        }

        if (config.getMode() == null || config.getSourcePath() == null || config.getDestPath() == null) {
            throw new CustomArgumentException("Brak wymaganych argumentów (-m, -s, lub -d).");
        }
        return config;
    }
}