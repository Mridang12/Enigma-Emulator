package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Mridang Sheth
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine m = readConfig();
        boolean isMachineConfigured = false;
        while (_input.hasNextLine()) {
            String line = _input.nextLine().trim();

            if (line.isEmpty()) {
                printMessageLine("");
                continue;
            }
            if (line.charAt(0) == '*') {
                String setting = line.substring(1);
                setUp(m, setting.trim());
                isMachineConfigured = true;
            } else {
                if (isMachineConfigured) {
                    String convertedLine = m.convert(line);
                    printMessageLine(convertedLine);
                } else {
                    throw error("Machine not configured yet,"
                            + "possibly because no settings line provided.");
                }
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String alph = _config.next();
            _alphabet = new Alphabet(alph);
            int numRotors = 0;
            int numPawls = 0;
            if (!_config.hasNextInt()) {
                throw error("Bad config file,"
                        + " does not have numRotors properly");
            } else {
                numRotors = _config.nextInt();
            }
            if (!_config.hasNextInt()) {
                throw error("Bad config file, does not have numPawls properly");
            } else {
                numPawls = _config.nextInt();
            }

            _config.useDelimiter("\\Z");
            if (_config.hasNext()) {
                String input = _config.next().trim();
                checkRotorParse(input);
                return new Machine(_alphabet, numRotors,
                        numPawls, readRotors(input));
            } else {
                throw error("Bad config file");
            }


        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }


    /**
     * Checks the configuration file for parsing errors in rotor descriptions.
     * @param input : The input string for rotors
     */
    private void checkRotorParse(String input) {
        String ptr = "([^\\s\\(\\)]+)([\\s]*)"
                + "(M[^\\s\\*\\(\\)]+|N|R)([\\s]*)"
                + "(([\\s]*[\\(][^\\s]*[\\)][\\s]*)*)";
        Pattern pattern = Pattern.compile(ptr);
        Matcher matcher = pattern.matcher(input);

        String checkError = "";
        while (matcher.find()) {
            checkError += matcher.group(1);
            checkError += matcher.group(2);
            checkError += matcher.group(3);
            checkError += matcher.group(4);
            checkError += matcher.group(5);
        }

        if (!checkError.equals(input)) {
            throw error("Bad config file formatting,"
                    + " description of rotors has a problem");
        }
    }

    /** Return the list of rotors, reading its description from _config.
     * @param input : Input string read from config */
    private ArrayList<Rotor> readRotors(String input) {
        try {
            String ptr = "([^\\s\\(\\)]+)"
                    + "([\\s]*)"
                    + "(M[^\\s\\*\\(\\)]+|N|R)"
                    + "([\\s]*)"
                    + "(([\\s]*[\\(][^\\s]*[\\)][\\s]*)*)";
            Pattern pattern = Pattern.compile(ptr);
            Matcher matcher = pattern.matcher(input);

            ArrayList<Rotor> rotorList = new ArrayList<Rotor>();
            matcher = pattern.matcher(input);
            while (matcher.find()) {
                Rotor r;
                String name = matcher.group(1);
                Permutation perm = new Permutation(matcher.group(5), _alphabet);
                switch (matcher.group(3).charAt(0)) {
                case 'M' :
                    r = new MovingRotor(name, perm,
                            matcher.group(3).substring(1));
                    break;
                case 'N' :
                    r = new FixedRotor(name, perm);
                    break;
                case 'R' :
                    r = new Reflector(name, perm);
                    break;
                default :
                    throw error("Invalid Rotor type, "
                            + matcher.group(3).charAt(0));
                }
                rotorList.add(r);
            }
            return rotorList;
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        checkSettingParse(settings);
        Pattern p = Pattern.compile("(([^\\s\\(\\)]+[\\s]*)+)"
                + "(([\\s]*[\\(][^\\s]*[\\)][\\s]*)*)");
        Matcher m = p.matcher(settings);

        while (m.find()) {
            String[] names = m.group(1).trim().split("\\s+");
            if (names.length == M.numRotors() + 1) {
                M.insertRotors(Arrays.copyOfRange(names, 0, names.length - 1));
                M.setRotors(names[names.length - 1]);
            } else if (names.length == M.numRotors() + 2) {
                M.insertRotors(Arrays.copyOfRange(names, 0, names.length - 2));
                M.setRotors(names[names.length - 2], names[names.length - 1]);
            } else {
                throw error("Invalid settings line");
            }
            if (!m.group(3).equals("")) {
                M.setPlugboard(new Permutation(m.group(3).trim(), _alphabet));
            }
        }
    }

    /**
     * Checks for parse errors in settings line.
     * @param settings : The settings line string
     */
    private void checkSettingParse(String settings) {
        Pattern p = Pattern.compile("(([^\\s\\(\\)]+[\\s]*)+)"
                + "(([\\s]*[\\(][^\\s]*[\\)][\\s]*)*)");
        Matcher m = p.matcher(settings);
        String checkError = "";

        while (m.find()) {
            checkError += m.group(1);
            checkError += m.group(3);
        }

        if (!checkError.equals(settings)) {
            throw error("Settings parse error! \n"
                    + settings + "~\n" + checkError);
        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        msg = convertedMsg(msg);
        int i = 0;
        for (; i < msg.length() - msg.length() % 5; i++) {
            _output.print(msg.charAt(i));
            if (i != 0 && (i + 1) % 5 == 0 && i != msg.length() - 1) {
                _output.print(' ');
            }
        }
        for (; i < msg.length(); i++) {
            _output.print(msg.charAt(i));
        }
        _output.println();
    }

    /**
     * Removes whitespaces from message.
     * @param msg : The message
     * @return : String with white spaces removed from msg
     */
    private String convertedMsg(String msg) {
        int countSpaces = 0;
        for (char c : msg.toCharArray()) {
            if (" \t\n".indexOf(c) != -1) {
                countSpaces++;
            }
        }
        char[] convertedMsg = new char[msg.length() - countSpaces];
        int index = 0;
        for (char c : msg.toCharArray()) {
            if (" \t\n".indexOf(c) == -1) {
                convertedMsg[index] = c;
                index++;
            }
        }
        return String.valueOf(convertedMsg);
    }


    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of machine configuration. */
    private Scanner _config;

    /** Source of input messages. */
    private Scanner _input;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
