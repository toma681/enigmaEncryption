package enigma;

import java.util.Hashtable;
import java.util.HashSet;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Andrew Tom
 */
class Alphabet {

    /** the alphabetstring var. */
    private String _alphabetString;
    /** hashtable int object inttochar. */
    private Hashtable<Integer, Object> _intToCharHash =
            new Hashtable<Integer, Object>();
    /** hashtable object int chartoint. */
    private Hashtable<Object, Integer> _charToIntHash =
            new Hashtable<Object, Integer>();

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {

        HashSet<Object> alphaDupe = new HashSet<Object>();
        _alphabetString = chars;

        for (int letterPos = 0; letterPos < chars.length(); letterPos += 1) {

            char cur = chars.charAt(letterPos);
            if (cur == '*' || cur == '(' || cur == ')') {
                throw EnigmaException.error("Bad chars in alphabet boo!");
            }

            alphaDupe.add(chars.charAt(letterPos));

            _intToCharHash.put(letterPos, chars.charAt(letterPos));
            _charToIntHash.put(chars.charAt(letterPos), letterPos);
        }

        if (alphaDupe.size() < chars.length()) {
            throw EnigmaException.error("Duplicate elements in Alphabet");
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }
    /** intotocharhash table for conversions.
     * @return intochar hash table
     * */
    public Hashtable<Integer, Object> getIntToCharHash() {
        return _intToCharHash;
    }
    /** chartointhash for conversions!.
     * @return chartoint hashtable
     * */
    public Hashtable<Object, Integer> getCharToIntHash() {
        return _charToIntHash;
    }
    /** alphabet string returner method.
     * @return alphabetstring in string form
     * */
    public String salphabet() {
        return _alphabetString;
    }

    /** Returns the size of the alphabet.
     * @return int of the size of the alphabet
     */
    int size() {
        return _alphabetString.length();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        String charToString = Character.toString(ch);
        return _alphabetString.contains(charToString);
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return (char) _intToCharHash.get(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        return _charToIntHash.get(ch);
    }

}
