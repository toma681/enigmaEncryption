package enigma;


/** Superclass that represents a rotor in the enigma machine.
 *  @author Andrew Tom
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _ringSetting = 0;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting.*/
    int setting() {
        return _setting;
    }

    /** Ring setting setter for EC.
     * @param p int setting.
     * */
    public void ringSetter(int p) {
        _ringSetting = p;
    }

    /** Ringsetting getter method.
     *
     * @return The Int of the ringsetting.
     */
    public int ringSetting() {
        return _ringSetting;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        _setting = posn;
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        _setting = alphabet().toInt(cposn);
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        int firstContact = permutation().wrap(p + setting() - ringSetting());
        int permuted = permutation().permute(firstContact);
        int hoof = permuted - setting() + ringSetting();
        int secondContact = permutation().wrap(hoof);
        return secondContact;
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int firstContact = permutation().wrap(e + setting() - ringSetting());
        int permuted = permutation().invert(firstContact);
        int hoof = permuted - setting() + ringSetting();
        int secondContact = permutation().wrap(hoof);
        return secondContact;
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    /** setting. */
    private int _setting;

    /** rsetting.*/
    private int _ringSetting;


}
