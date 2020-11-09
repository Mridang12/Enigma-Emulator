package enigma;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Mridang Sheth
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;

        if (cycles.equals("")) {
            cycleList = new ArrayList<String>();
        } else {
            String exception = "() \n\t\r";
            if (!Alphabet.areCharactersUnique(cycles, exception)) {
                throw new EnigmaException("Bad input to Permutation,"
                        + " characters are repeated");
            }
            for (char c : cycles.toCharArray()) {
                if (exception.indexOf(c) == -1) {
                    if (!alphabet.contains(c)) {
                        throw new EnigmaException("Bad input to Permutation, "
                                + c + " is not present in Alphabet");
                    }
                }
            }
            cycleList = new ArrayList<String>();
            Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(cycles);
            while (m.find()) {
                cycleList.add(m.group(1));
            }
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        cycleList.add(cycle);
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
        return this.alphabet().size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        return _alphabet.toInt(permute(_alphabet.toChar(wrap(p))));
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        return _alphabet.toInt(invert(_alphabet.toChar(wrap(c))));
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (alphabet().toInt(p) == -1) {
            throw new EnigmaException("Invalid input to permute, "
                    + p + " is not present in Alphabet");
        } else {
            String cycle = findCycle(p);
            if (cycle.length() == 1) {
                return p;
            } else {
                int index = cycle.indexOf(p);
                return cycle.charAt((index == cycle.length() - 1)
                        ? 0 : index + 1);
            }
        }
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        if (alphabet().toInt(c) == -1) {
            throw new EnigmaException("Invalid input to permute, "
                    + c + " is not present in Alphabet");
        } else {
            String cycle = findCycle(c);
            if (cycle.length() == 1) {
                return c;
            } else {
                int index = cycle.indexOf(c);
                return cycle.charAt((index == 0)
                        ? cycle.length() - 1 : index - 1);
            }
        }
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < alphabet().size(); i++) {
            if (findCycle(alphabet().toChar(i)).length() == 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the cycle corresponding to char c.
     * @param c : The character whose cycle is to be found.
     * @return : The cycle containing c.
     */
    private String findCycle(char c) {
        for (String cycle : cycleList) {
            if (cycle.indexOf(c) != -1) {
                return cycle;
            }
        }
        return String.valueOf(c);
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** ArrayList of cycles in this permutation. */
    private ArrayList<String> cycleList;
}
