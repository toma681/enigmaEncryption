package enigma;

import java.util.Hashtable;
import java.util.HashSet;


/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Andrew Tom
 */

class Permutation {
    /** permcycleforward.
     */
    private Hashtable<Object, Object> _permuteCycleForward =
            new Hashtable<Object, Object>();

    /** permcyclebackward.
     */
    private Hashtable<Object, Object> _permuteCycleBackward =
            new Hashtable<Object, Object>();

    /** Set this Permutation to that specified by CYCLES, a string in the
         *  form "(cccc) (cc) ..." where the c's are charactersin ALPHABET,which
         *  is interpreted as a permutation in cycle notation.Characters in the
         *  alphabet that are not included in any cycle map to themselves.
         *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        if (!cycles.equals("")) {
            addCycle(cycles);
        } else {
            addEmptyCycle();
        }
    }

    /** Hashy forward.
     *
     * @return hashcycleforward
     */
    public Hashtable<Object, Object> permForward() {
        return _permuteCycleForward;
    }

    /** Hashy backward.
     *
     * @return hashcycleback
     */
    public Hashtable<Object, Object> permBackward() {
        return _permuteCycleBackward;
    }

    /** Add empty cycle.
     * Basically maps each letter to themselves because theres
     * no permute cycle to use lol
     */
    private void addEmptyCycle() {
        int len = alphabet().salphabet().length();
        String alphStr = alphabet().salphabet();
        for (int x = 0; x < len; x += 1) {
            char cur = alphStr.charAt(x);
            _permuteCycleBackward.put(cur, cur);
            _permuteCycleForward.put(cur, cur);
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm.
     *
     *
     *  THIS ACTUALLY ADDS ALL CYCLES.
     *  */
    private void addCycle(String cycles) {
        cycles = cycles.replaceAll("\\(", " ");
        cycles = cycles.replaceAll("\\)", " ").trim();
        String [] cycleArr = cycles.split("\\s+");

        String concatenatedStr = cycles.replaceAll("\\s+", "").trim();


        HashSet<Object> duplicateCheck = new HashSet<Object>();
        for (int x = 0; x < concatenatedStr.length(); x += 1) {
            duplicateCheck.add(concatenatedStr.charAt(x));

            char curChar = concatenatedStr.charAt(x);
            if (!alphabet().contains(curChar)) {
                throw EnigmaException.error("Cycle char not in alphabet!!");
            }
        }
        if (duplicateCheck.size() < concatenatedStr.length()) {
            throw EnigmaException.error("Duplicate characters in Cycle");
        }

        for (int x = 0; x < size(); x += 1) {
            char curAlpha = alphabet().salphabet().charAt(x);
            String charToStr = Character.toString(curAlpha);
            boolean notSelfMap = concatenatedStr.contains(charToStr);
            if (!notSelfMap) {
                _permuteCycleForward.put(curAlpha, curAlpha);
                _permuteCycleBackward.put(curAlpha, curAlpha);
            }
        }

        for (String cycle : cycleArr) {
            for (int pos = 0; pos < cycle.length(); pos += 1) {
                int len = cycle.length();
                char cur = cycle.charAt(pos);
                char next = cycle.charAt((pos + 1) % len);
                _permuteCycleForward.put(cur, next);
                int negMod = ((pos - 1) % len + len) % len;
                char prev = cycle.charAt(negMod);
                _permuteCycleBackward.put(cur, prev);
            }
        }
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return alphabet().size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size.
     *
     *  Changes int p to a char, then uses the permute
     *  method that takes in a char to get the
     *  permuted value.
     *  */
    int permute(int p) {
        if (p >= size() || p < 0) {
            p = wrap(p);
        }
        char feed = alphabet().toChar(p);
        return alphabet().toInt(permute(feed));
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size.
     *
     *  Changes int c to a char, then uses the invert
     *  method that takes in a char to get the
     *  permuted value.
     *  */
    int invert(int c) {
        if (c >= size() || c < 0) {
            c = wrap(c);
        }
        char feed = alphabet().toChar(c);
        return alphabet().toInt(invert(feed));
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET.
     *
     *  Grabs the next val in the forward hash table.
     *  */
    char permute(char p) {
        String charToStr = Character.toString(p);
        if (!alphabet().salphabet().contains(charToStr)) {
            throw EnigmaException.error("Char not in Alphabet");
        }
        return (char) _permuteCycleForward.get(p);
    }

    /** Return the result of applying the inverse of this permutation to C.
     *
     * Grabs the next val in the backward hashtable.
     * */
    char invert(char c) {
        String charToStr = Character.toString(c);
        if (!alphabet().salphabet().contains(charToStr)) {
            throw EnigmaException.error("Char not in Alphabet");
        }
        return (char) _permuteCycleBackward.get(c);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself).
     *
     *  Just checks the hash table if its mapped val
     *  is the same as itself
     *  */
    boolean derangement() {
        for (int x = 0; x < alphabet().size(); x += 1) {
            char letter = alphabet().salphabet().charAt(x);
            if ((char) _permuteCycleForward.get(letter) == letter) {
                return false;
            }
        }
        return true;

    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

}
