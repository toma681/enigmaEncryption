package enigma;

import org.junit.Test;
import static org.junit.Assert.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Andrew Tom
 */
public class AlphabetTest {


    @Test(expected = EnigmaException.class)
    public void doubleAlphabet() {
        Permutation p = new Permutation("(ABCD)", new Alphabet("ABBCD"));
    }

    @Test
    public void toInt() {
        Alphabet alpha = new Alphabet();

        assertEquals(0, alpha.toInt('A'));

        assertEquals(25, alpha.toInt('Z'));

        alpha = new Alphabet("BCO");
        assertEquals(1, alpha.toInt('C'));

    }

    @Test
    public void toChar() {
        Alphabet alpha = new Alphabet();

        assertEquals('A', alpha.toChar(0));

        assertEquals('Z', alpha.toChar(25));

        alpha = new Alphabet("BCO");

        assertEquals('C', alpha.toChar(1));

    }

}
