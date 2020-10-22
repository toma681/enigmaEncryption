package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.NoSuchElementException;
import java.util.Hashtable;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Andrew Tom
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
     *  results to _output.
     *
     *  This method should be focusing on input and output
     *  */
    private void process() {
        ArrayList<ArrayList<String>> nestedArr =
                new ArrayList<ArrayList<String>>();
        ArrayList<String> inputList = new ArrayList<String>();
        String[] inputArr;
        boolean blankLine = true;
        while (_input.hasNextLine()) {
            String nextline = _input.nextLine();

            for (int x = 0; x < nextline.length(); x += 1) {
                blankLine = false;
            }
            if (blankLine) {
                _output.println();
                continue;
            }
            inputList.add(nextline);
        }

        while (!inputList.isEmpty()) {
            boolean firsttime = true;
            ArrayList<String> ne = new ArrayList<String>();

            for (int x = 0; !inputList.isEmpty(); ) {
                String adder = inputList.get(x).replaceAll("\\s+", "");
                if (firsttime) {
                    ne.add(inputList.get(x));
                    firsttime = false;
                    inputList.remove(x);
                } else if (!inputList.get(x).startsWith("*")) {
                    ne.add(adder);
                    inputList.remove(x);
                } else if (inputList.get(x).startsWith("*")) {
                    break;
                }

            }
            nestedArr.add(ne);
        }

        Machine m = readConfig();



        for (int x = 0; x < nestedArr.size(); x += 1) {
            ArrayList<String> cur = nestedArr.get(x);
            processHelper(cur, m);

        }

    }

    /** helper for the process hehe.
     * @param m da machine
     * @param inputList da input
     * */
    private void processHelper(ArrayList<String> inputList, Machine m) {
        String settingLine = "";
        String insertSpace = " ";


        settingLine = inputList.get(0);
        inputList.remove(0);
        setUp(m, settingLine);

        int numMoving = 0;
        for (int x = 0; x < m.rotorList().size(); x += 1) {
            Rotor cur = m.rotorList().get(x);
            if (x == 0 && !cur.reflecting()) {
                throw EnigmaException.error("First rotor must be reflector!");
            } else if (x > 0 && cur.reflecting()) {
                throw EnigmaException.error("Reflector can't be past 0th pos!");
            }

            if (cur.rotates()) {
                numMoving++;
            }
        }

        if (numMoving != m.numPawls()) {
            throw EnigmaException.error("num pawls boo");
        }

        for (int x = 0; x < inputList.size(); x += 1) {
            String cur = m.convert(inputList.get(x));
            int counter = 0;
            for (int y = 0; y < cur.length() + 1; y += 1) {
                counter += 1;
                if (counter == 6) {
                    cur = cur.substring(0, y) + " "
                            +
                            cur.substring(y, cur.length());
                    counter = 0;
                }

            }

            cur = cur.trim();
            _output.println(cur);
        }

    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config.
     *  This is the method that reads the whole config file.
     *  */
    private Machine readConfig() {
        ArrayList<String> rotorBox = new ArrayList<String>();
        ArrayList<Rotor> rotorList = new ArrayList<Rotor>();
        String curRotor = ""; String[] lineArr;
        String wholeFile = "";
        String alphabetStr = "";
        String numRotStr = ""; String numPawlStr = "";
        int intRotors;
        int intPawls;
        int newRotor = 0;
        try {
            while (_config.hasNextLine()) {
                String line = _config.nextLine();
                wholeFile += line + " ";
            }
            wholeFile = wholeFile.trim();
            wholeFile = wholeFile.replaceAll("\\)\\s*\\w+\\s*\\(", "\\)\\(");
            lineArr = wholeFile.split("\\s+");
            for (int x = 0; x < lineArr.length; x += 1) {
                if (x == 0) {
                    alphabetStr = lineArr[x];
                    continue;
                } else if (x == 1) {
                    numRotStr = lineArr[x];
                    continue;
                } else if (x == 2) {
                    numPawlStr = lineArr[x];
                    continue;
                }
                boolean inParen = false;
                if (!lineArr[x].startsWith("(") || x == lineArr.length - 1) {
                    if (!curRotor.equals("")) {
                        if (x == lineArr.length - 1) {
                            rotorBox.add(curRotor + lineArr[x]);
                            break;
                        }
                        rotorBox.add(curRotor);
                    }
                    curRotor = "";
                    curRotor += lineArr[x] + " ";
                    curRotor += lineArr[x + 1] + " ";
                    x += 1;
                } else {
                    curRotor += lineArr[x] + " ";
                }
            }
            confighelp(alphabetStr, rotorList, rotorBox);
            try {
                intRotors = Integer.parseInt(numRotStr);
                intPawls = Integer.parseInt(numPawlStr);
            } catch (NumberFormatException oof) {
                throw EnigmaException.error("numbers wrong");
            }
            return new Machine(_alphabet, intRotors, intPawls);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Helper for the readconfig.
     * @param alphabetStr string of alpha
     * @param rotorBox arraylist of rotor strings
     * @param rotorList arraylist of rotor objects
     * */
    public void confighelp(String alphabetStr,
                           ArrayList<Rotor> rotorList,
                           ArrayList<String> rotorBox) {
        _alphabet = new Alphabet(alphabetStr);
        for (int x = 0; x < rotorBox.size(); x += 1) {
            String cur = rotorBox.get(x);
            cur = cur.trim();
            rotorList.add(readRotor(cur));
        }
        for (int x = 0; x < rotorList.size(); x += 1) {
            Rotor cur = rotorList.get(x);
            String name = cur.name();
            _possibleRotors.put(name, cur);
        }
    }


    /** Return a rotor, reading its description from _config.
     * @param rotorStr dd
     * */
    private Rotor readRotor(String rotorStr) {
        try {
            boolean moving = false;
            boolean reflector = false;
            boolean fixed = false;

            String cycles = "";
            String notches = "";
            String[] arr = rotorStr.split("\\s+");
            String name = arr[0];
            String type = arr[1];

            if (type.charAt(0) == 'M') {
                moving = true;
            } else if (type.charAt(0) == 'N') {
                fixed = true;
            } else if (type.charAt(0) == 'R') {
                reflector = true;
            } else {
                throw EnigmaException.error("Wrong rotor type");
            }

            for (int x = 0; x < type.length(); x += 1) {
                if (x == 0) {
                    continue;
                }
                notches += type.charAt(x);
            }

            for (int x = 2; x < arr.length; x += 1) {
                cycles += arr[x] + " ";
            }
            cycles.trim();

            Permutation p = new Permutation(cycles, _alphabet);
            if (moving) {
                MovingRotor r = new MovingRotor(name, p, notches);
                return r;
            } else if (fixed) {
                FixedRotor r = new FixedRotor(name, p);
                return r;
            } else if (reflector) {
                Reflector r = new Reflector(name, p);
                return r;
            }

            throw EnigmaException.error("oops");

        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** setuphelper function.
     * @param settingList the list of settings in strings
     * @param settingArr the array of settings in strings
     * @param M machine M
     * */
    public void setuphelper(ArrayList<String> settingList, String[] settingArr,
                            Machine M) {
        for (int x = 0; x < settingArr.length; x += 1) {
            settingList.add(settingArr[x]);
        }

        if (!settingList.get(0).equals("*")) {
            throw EnigmaException.error("Missing *");
        }
        settingList.remove(0);
        ArrayList<Rotor> insertRots = new ArrayList<Rotor>();
        HashSet<String> rotorDupe = new HashSet<String>();
        for (int x = 0; x < M.numRotors(); x += 1) {
            String curRotStr = settingList.get(0);
            if (!_possibleRotors.containsKey(curRotStr)) {
                throw EnigmaException.error("Rotor name incorrect");
            }

            rotorDupe.add(curRotStr);

            insertRots.add(_possibleRotors.get(curRotStr));
            settingList.remove(0);
        }
        int firstMoving = 0;
        boolean first = true;

        if (rotorDupe.size() < insertRots.size()) {
            throw EnigmaException.error("Duplicate rotors in setting!");
        }
        for (int x = 0; x < insertRots.size(); x += 1) {
            Rotor cur = insertRots.get(x);

            if (cur.rotates() && first) {
                firstMoving = x;
                first = false;
            }

            if (!cur.rotates() && x > firstMoving && !first) {
                throw EnigmaException.error("Fixed in bad spot");
            }

            if (x == 0 && !cur.reflecting()) {
                throw EnigmaException.error("First rotor must be reflector!");
            } else if (x > 0 && cur.reflecting()) {
                throw EnigmaException.error("Reflector can't be past 0th pos!");
            }
        }
        M.insertRotors(insertRots);
        M.setRotors(settingList.get(0));
        settingList.remove(0);
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment.
     *
     *  This is assuming that Machine has been instantiated already
     *  and just needs to have the rotors and rotor settings put in
     *  and also plugboard
     *  */
    public void setUp(Machine M, String settings) {
        if (M.rotorList().size() > 0) {
            M.rotorListSetter(new ArrayList<Rotor>());
        }


        ArrayList<String> settingList = new ArrayList<String>();
        String[] settingArr = settings.split("\\s+");

        setuphelper(settingList, settingArr, M);
        String defaultRings = "";
        for (int x = 0; x < M.numRotors(); x += 1) {
            char zero = M.rotorList().get(0).alphabet().toChar(0);
            Rotor cur = M.rotorList().get(x);
            if (!cur.reflecting()) {
                defaultRings += zero;
            }
        }
        M.setRingSetting(defaultRings);
        if (!settingList.isEmpty()) {
            if (!settingList.get(0).startsWith("(")) {
                String ringSetting = settingList.get(0);
                M.setRingSetting(ringSetting);
                settingList.remove(0);
            }
        }
        String concatPlugs = "";
        for (int x = 0; x < settingList.size(); x += 1) {
            String cur = settingList.get(x);
            concatPlugs += cur.trim();
        }
        String plugbord = "";
        boolean inParen = false;
        for (int x = 0; x < concatPlugs.length(); x += 1) {
            if (concatPlugs.charAt(x) == '(') {
                inParen = true;
                plugbord += '(';
                continue;
            }
            if (concatPlugs.charAt(x) == ')') {
                inParen = false;
                plugbord += ')';
                continue;
            }
            if (inParen) {
                plugbord += concatPlugs.charAt(x);
            }
        }
        Permutation plugPerm;
        Alphabet alpha = M.rotorList().get(0).alphabet();
        plugPerm = new Permutation(plugbord.trim(), alpha);
        M.setPlugboard(plugPerm);
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** Possible rotors getter.
     * @return possible rotors.
     */
    public Hashtable<String, Rotor> possibleRots() {
        return _possibleRotors;
    }

    /** hashtable of the possible rotors. */
    private Hashtable<String, Rotor> _possibleRotors =
            new Hashtable<String, Rotor>();
}
