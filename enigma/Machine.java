package enigma;

import java.util.ArrayList;


/** Class that represents a complete enigma machine.
 *  @author Andrew Tom
 */
class Machine {

    /** the num rotors. */
    private int _numRotors;
    /** the num pawls. */
    private int _numPawls;
    /** the rotorlist var. */
    private ArrayList<Rotor> _rotorList = new ArrayList<Rotor>();
    /** the atnotch array var. */
    private boolean[] _atNotchArr = new boolean[1];

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls) {

        _alphabet = alpha;
        _numPawls = pawls;
        _numRotors = numRotors;
    }

    /** Updates the _notchArr. */
    public void notchArrUpdater() {
        Rotor cur;
        _atNotchArr = new boolean[numRotors()];
        for (int x = 0; x < numRotors(); x += 1) {
            cur = _rotorList.get(x);
            if (!cur.reflecting() && cur.rotates()) {
                MovingRotor M = (MovingRotor) cur;
                _atNotchArr[x] = M.atNotch();

                ArrayList<Object> dimb = new ArrayList<>();
                dimb.add(_alphabet.toChar(M.setting()));
                dimb.add(M.atNotch());
            }
        }
    }


    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _numPawls;
    }

    /** rotorlist getter method.
     * @return returns the rotorlist var
     * */
    public ArrayList<Rotor> rotorList() {
        return _rotorList;
    }

    /** sets the rotorlist var to in.
     *
     * @param in the incoming rotorlist
     */
    public void rotorListSetter(ArrayList<Rotor> in) {
        _rotorList = in;
    }

    /** boolean arr of rotors at their notches.
     *
     * @return the notch array
     */
    public boolean[] atNotchArr() {
        return _atNotchArr;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(ArrayList<Rotor> rotors) {
        for (int x = 0; x < rotors.size(); x += 1) {
            _rotorList.add(rotors.get(x));
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).
     *
     *  Basically initializing the machine's rotors.
     *  */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw EnigmaException.error("Setting is not correct Len");
        }

        Rotor cur;
        int settingPos = 0;
        for (int x = 0; x < numRotors(); x += 1) {
            cur = _rotorList.get(x);
            if (!cur.reflecting()) {
                cur.set(setting.charAt(settingPos));
                settingPos += 1;
            }
        }
    }

    /** The extra credit ring setter method.
     * @param setting the setting for the ring
     * */
    public void setRingSetting(String setting) {
        Rotor cur;
        int settingPos = 0;
        for (int x = 0; x < numRotors(); x += 1) {
            if (settingPos == setting.length()) {
                break;
            }
            cur = _rotorList.get(x);
            if (!cur.reflecting()) {
                int asInt = cur.alphabet().toInt(setting.charAt(settingPos));
                cur.ringSetter(asInt);
                settingPos += 1;
            }
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugBoard = plugboard;
    }

    /** Advances the machine once. */
    public void advanceMachine() {
        Rotor cur;
        if (atNotchArr().length == 1) {
            notchArrUpdater();
        }

        for (int x = numRotors() - 1; x >= 0; x -= 1) {
            cur = _rotorList.get(x);
            if (x == numRotors() - 1) {
                MovingRotor fast = (MovingRotor) cur;
                fast.advance();
            } else if (cur.rotates() && !cur.reflecting()) {
                Rotor left = _rotorList.get(x - 1);
                Rotor right = _rotorList.get(x + 1);
                if (left.rotates() && right.rotates() && cur.atNotch()) {
                    cur.advance();
                } else if (left.rotates() && right.rotates()
                        &&
                        _atNotchArr[x + 1]) {
                    cur.advance();
                } else if (!left.rotates() && right.rotates()
                        &&
                        _atNotchArr[x + 1]) {
                    cur.advance();
                }
            }
        }
        notchArrUpdater();
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine.
     *
     *  Seems like this one converts one letter
     *  I am changing c throughout the method
     *  */
    int convert(int c) {
        advanceMachine();

        Rotor r;
        char cChar = _alphabet.toChar(c);
        if (_plugBoard.permForward().containsKey(cChar)) {
            cChar = (char) _plugBoard.permForward().get(cChar);
            c = _alphabet.toInt(cChar);
        }

        for (int x = numRotors() - 1; x >= 0; x -= 1) {
            r = _rotorList.get(x);
            c = r.convertForward(c);
        }
        for (int x = 0; x < numRotors(); x += 1) {
            if (!_rotorList.get(x).reflecting()) {
                r = _rotorList.get(x);
                c = r.convertBackward(c);
            }
        }

        cChar = _alphabet.toChar(c);
        if (_plugBoard.permForward().containsKey(cChar)) {
            cChar = (char) _plugBoard.permForward().get(cChar);
            c = _alphabet.toInt(cChar);
        }

        return c;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly.
     *
     *  This one calls the other convert for the whole
     *  msg
     *  */
    String convert(String msg) {

        String convertedString = "";
        char cur;
        int charInt;
        char append;

        for (int x = 0; x < msg.length(); x += 1) {
            cur = msg.charAt(x);
            if (!_alphabet.contains(cur)) {
                throw EnigmaException.error("winkwonk");
            }
            charInt = _alphabet.toInt(cur);
            append = _alphabet.toChar(convert(charInt));
            convertedString += append;
        }

        return convertedString;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** plugbord perm. */
    private Permutation _plugBoard;

    /** plugboard getter.
     * @return plugboard is returned
     * */
    public Permutation permget() {
        return _plugBoard;
    }
}
