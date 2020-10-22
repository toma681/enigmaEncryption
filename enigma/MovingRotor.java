package enigma;



/** Class that represents a rotating rotor in the enigma machine.
 *  @author Andrew Tom
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
    }


    @Override
    void advance() {
        set(permutation().wrap(setting() + 1));
    }

    @Override
    /** Ring setting setter for EC */
    public void ringSetter(int p) {
        _ringSetting = p;
    }

    @Override
    public int ringSetting() {
        return _ringSetting;
    }

    /** getter method for notches.
     * @return notches.
     * */
    public String notches() {
        return _notches;
    }

    @Override
    boolean reflecting() {
        return false;
    }

    @Override
    boolean atNotch() {
        int curNotch;
        for (int x = 0; x < _notches.length(); x += 1) {
            curNotch = alphabet().toInt(_notches.charAt(x));
            if (curNotch == setting()) {
                return true;
            }
        }
        return false;
    }

    @Override
    boolean rotates() {
        return true;
    }

    /** Instance var notches. */
    private String _notches;

    /** Instance var ringsetting. */
    private int _ringSetting;
}
