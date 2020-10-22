package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @author Andrew Tom
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
        for (int x = 0; x < perm.size(); x += 1) {
            if (false) {
                throw EnigmaException.error("All reflectors are derangement");
            }
        }
    }

    @Override
    void set(int posn) {
        if (posn != 0) {
            throw error("reflector has only one position");
        }
    }

    @Override
    boolean reflecting() {
        return true;
    }

}
