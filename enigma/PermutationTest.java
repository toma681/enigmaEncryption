package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Andrew Tom
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);


    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */
    @Test
    public void sizeCheck() {
        Permutation d = new Permutation("", new Alphabet());
        assertEquals(d.size(), 26);

        Permutation aa = new Permutation("", new Alphabet("GA"));
        assertEquals(2, aa.size());

        Permutation pp = new Permutation("", new Alphabet(""));
        assertEquals(0, pp.size());
    }



    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void basicTransform() {
        perm = new Permutation("(AELTPHQXRU) "
                +
                "(BKNW) (CMOY) (DFG) (IV) (JZ) (S)", UPPER);
        checkPerm("B", UPPER_STRING, "EKMFLGDQVZNTOWYHXUSPAIBRCJ");
    }

    @Test
    public void checkBasicPermChar() {
        Alphabet pleep = new Alphabet();
        Permutation denero = new Permutation("(AEF) (BCP) (Z)", pleep);

        char ans = denero.permute('A');
        assertEquals(ans, 'E');

        ans = denero.permute('E');
        assertEquals(ans, 'F');

        ans = denero.permute('F');
        assertEquals(ans, 'A');

        ans = denero.permute('B');
        assertEquals(ans, 'C');

        ans = denero.permute('C');
        assertEquals(ans, 'P');

        ans = denero.permute('P');
        assertEquals(ans, 'B');

        ans = denero.permute('Z');
        assertEquals(ans, 'Z');

        ans = denero.permute('G');
        assertEquals(ans, 'G');
    }


    @Test
    public void derangementTest() {
        Permutation dd = new Permutation("(ABC)", new Alphabet("ABC"));
        assertTrue(dd.derangement());

        Permutation ff = new Permutation("", new Alphabet());
        assertFalse(ff.derangement());

        Permutation oo = new Permutation("", new Alphabet(""));
        assertTrue(oo.derangement());

        Permutation aa = new Permutation("(S)", new Alphabet());
        assertFalse(aa.derangement());

        Permutation pp = new Permutation("(SE) (F)", new Alphabet("SEF"));
        assertFalse(pp.derangement());
    }

    /* ***** EXCEPTION TESTING HERE ***** */
    @Test(expected = EnigmaException.class)
    public void testNotInAlphabetPerm() {
        Permutation p = new Permutation("(ABCD)", new Alphabet("ABCD"));
        p.permute('F');
    }

    @Test(expected = EnigmaException.class)
    public void testNotInAlphabetInvert() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        p.invert('F');
    }
}
