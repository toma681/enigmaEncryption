package enigma;

import org.junit.Test;
import static org.junit.Assert.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Andrew Tom
 */
public class AllRotorTests {


    @Test
    public void nameTest() {
        Permutation pleep = new Permutation("(ABC) (DPOK)", new Alphabet());
        Rotor r = new Rotor("W", pleep);
        assertEquals("W", r.name());
    }

    @Test
    public void settingTest() {
        Permutation pleep = new Permutation("(ABC) (DPOK)", new Alphabet());
        Rotor r = new Rotor("W", pleep);
        assertEquals("W", r.name());

        r.set(0);
        assertEquals(r.setting(), 0);

        r.set(22);
        assertEquals(r.setting(), 22);

        r.set('C');
        assertEquals(r.setting(), 2);

        r.set('Z');
        assertEquals(r.setting(), 25);
    }

    @Test
    public void rotorConvertTest() {
        String cycles = "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)";
        Permutation pleep = new Permutation(cycles, new Alphabet());
        Rotor r = new Rotor("W", pleep);

        r.set(0);
        assertEquals(r.convertForward(0), 4);

        r.set(1);
        assertEquals(r.convertForward(0), 9);
    }


    @Test
    public void basic() {
        String cycles = "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)";
        Permutation pleep = new Permutation(cycles, new Alphabet());
        MovingRotor r = new MovingRotor("W", pleep, "BZ");

        assertFalse(r.reflecting());
        assertTrue(r.rotates());
    }

    @Test
    public void notchTest() {
        String cycles = "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)";
        Permutation pleep = new Permutation(cycles, new Alphabet());
        MovingRotor r = new MovingRotor("W", pleep, "BZ");

        assertEquals(r.notches(), "BZ");

        r.set(1);
        assertTrue(r.atNotch());

        r.set(25);
        assertTrue(r.atNotch());


        r.set('B');
        assertTrue(r.atNotch());

        r.set('Z');
        assertTrue(r.atNotch());
    }



    @Test
    public void rotorAdvanceTest() {
        String cycles = "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)";
        Permutation pleep = new Permutation(cycles, new Alphabet());
        MovingRotor r = new MovingRotor("W", pleep, "BZ");

        r.set(0);
        assertEquals(r.convertForward(0), 4);


        r.advance();
        assertEquals(r.setting(), 1);
        assertEquals(r.convertForward(0), 9);


        r.set(25);
        r.advance();

        assertEquals(r.setting(), 0);
    }
}
