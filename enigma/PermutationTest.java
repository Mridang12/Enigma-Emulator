package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;
import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Permutation class. For the purposes of
 * this lab (in order to test) this is an abstract class, but in proj1, it will
 * be a concrete class. If you want to copy your tests for proj1, you can make
 * this class concrete by removing the 4 abstract keywords and implementing the
 * 3 abstract methods.
 *
 *  @author
 */
public class PermutationTest {

    /**
     * For this lab, you must use this to get a new Permutation,
     * the equivalent to:
     * new Permutation(cycles, alphabet)
     * @return a Permutation with cycles as its cycles and alphabet as
     * its alphabet
     * @see Permutation for description of the Permutation conctructor
     */
    Permutation getNewPermutation(String cycles, Alphabet alphabet) {
        return new Permutation(cycles, alphabet);
    }

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet(chars)
     * @return an Alphabet with chars as its characters
     * @see Alphabet for description of the Alphabet constructor
     */
    Alphabet getNewAlphabet(String chars) {
        return new Alphabet(chars);
    };

    /**
     * For this lab, you must use this to get a new Alphabet,
     * the equivalent to:
     * new Alphabet()
     * @return a default Alphabet with characters ABCD...Z
     * @see Alphabet for description of the Alphabet constructor
     */
    Alphabet getNewAlphabet() {
        return new Alphabet();
    }

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /** Check that PERM has an ALPHABET whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha,
                           Permutation perm, Alphabet alpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.toInt(c), ei = alpha.toInt(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        Alphabet alpha = getNewAlphabet();
        Permutation perm = getNewPermutation("", alpha);
        checkPerm("identity", UPPER_STRING, UPPER_STRING, perm, alpha);
    }

    @Test
    public void testpermuteint() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals(0, p.permute(1));
        assertEquals(2, p.permute(0));
        assertEquals(1, p.permute(3));
        assertEquals(2, p.permute(4));


        p = getNewPermutation("(BACD) (XFM)", getNewAlphabet("ABCDFMX"));
        assertEquals(0, p.permute(1));
        assertEquals(2, p.permute(0));
        assertEquals(1, p.permute(3));
        assertEquals(6, p.permute(5));
        assertEquals(4, p.permute(6));
        assertEquals(5, p.permute(4));
        assertEquals(2, p.permute(7));
        assertEquals(1, p.permute(10));

        p = getNewPermutation("(BACD) (XF) (M)", getNewAlphabet("ABCDFMX"));
        assertEquals(0, p.permute(1));
        assertEquals(2, p.permute(0));
        assertEquals(1, p.permute(3));
        assertEquals(5, p.permute(5));
        assertEquals(4, p.permute(6));
        assertEquals(6, p.permute(4));


        p = getNewPermutation("(BACD)", getNewAlphabet("ABCDE"));
        assertEquals(4, p.permute(4));
    }

    @Test
    public void testpermutechar() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals('A', p.permute('B'));
        assertEquals('C', p.permute('A'));
        assertEquals('B', p.permute('D'));

        p = getNewPermutation("(BACD) (XFM)", getNewAlphabet("ABCDFMX"));
        assertEquals('A', p.permute('B'));
        assertEquals('C', p.permute('A'));
        assertEquals('B', p.permute('D'));
        assertEquals('F', p.permute('X'));
        assertEquals('M', p.permute('F'));
        assertEquals('X', p.permute('M'));

        p = getNewPermutation("(BACD)", getNewAlphabet("ABCDE"));
        assertEquals('E', p.permute('E'));
    }

    @Test
    public void testinvertint() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals(3, p.invert(1));
        assertEquals(1, p.invert(0));
        assertEquals(2, p.invert(3));
        assertEquals(1, p.invert(4));
        assertEquals(3, p.invert(5));

        p = getNewPermutation("(BACD)", getNewAlphabet("ABCDE"));
        assertEquals(4, p.invert(4));
    }

    @Test
    public void testinvertchar() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        assertEquals('D', p.invert('B'));
        assertEquals('B', p.invert('A'));
        assertEquals('C', p.invert('D'));

        p = getNewPermutation("(BACD) (XFM)", getNewAlphabet("ABCDFMX"));
        assertEquals('D', p.invert('B'));
        assertEquals('B', p.invert('A'));
        assertEquals('C', p.invert('D'));
        assertEquals('M', p.invert('X'));
        assertEquals('X', p.invert('F'));
        assertEquals('F', p.invert('M'));

        p = getNewPermutation("(BACD) (XF) (M)", getNewAlphabet("ABCDFMX"));
        assertEquals('D', p.invert('B'));
        assertEquals('B', p.invert('A'));
        assertEquals('C', p.invert('D'));
        assertEquals('F', p.invert('X'));
        assertEquals('X', p.invert('F'));
        assertEquals('M', p.invert('M'));



        p = getNewPermutation("(BACD)", getNewAlphabet("ABCDE"));
        assertEquals('E', p.invert('E'));
    }

    @Test(expected = EnigmaException.class)
    public void testNotInAlphabet() {
        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
        p.permute('F');
    }

    @Test(expected = EnigmaException.class)
    public void testBadPermutationInput() {
        Permutation p1 = getNewPermutation("(BACD) (BF)", getNewAlphabet());
        Permutation p2 = getNewPermutation("(BACD) (G)",
                getNewAlphabet("ABCD"));
        Permutation p3 = getNewPermutation("(BACD(G)", getNewAlphabet("ABCD"));
        Permutation p4 = getNewPermutation("BACD", getNewAlphabet("ABCD"));
        p2.permute('G');
    }

    @Test(expected = EnigmaException.class)
    public void testBadAlphabetInput() {
        Alphabet alph = getNewAlphabet("AAAABBBBCCCCDDDD");
    }

}
