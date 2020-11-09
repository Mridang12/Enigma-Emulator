package enigma;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Mridang Sheth
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        chars = chars.trim();
        Pattern pattern = Pattern.compile("[\\s\\*\\(\\)]");
        Matcher matcher = pattern.matcher(chars);
        boolean found = matcher.find();
        if (areCharactersUnique(chars, "") && !found) {
            this._chars = chars;
        } else {
            throw new EnigmaException("Bad input for Alphabet, repeated"
                    + "characters or invalid characters found");
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _chars.length();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        return _chars.indexOf(ch) != -1;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        if (!(index >= 0 && index < size())) {
            throw new EnigmaException("Invalid index : " + index);
        }
        return _chars.charAt(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        if (!contains(ch)) {
            throw EnigmaException.error("Character "
                    + ch + " is not part of Alphabet.");
        }
        return _chars.indexOf(ch);
    }

    /**
     * Checks if the characters in chars are unique,
     *  disregarding the character repeats in exception.
     * @param chars The string for which testing is done
     * @param exception All characters in exception can be repeated in chars
     * @return True if condition is met
     */
    public static boolean areCharactersUnique(String chars, String exception) {
        boolean uniqueChars = true;
        for (int i = 0; i < chars.length(); i++) {
            for (int j = 1 + i; j < chars.length(); j++) {
                if (chars.charAt(i) == chars.charAt(j)
                        && exception.indexOf(chars.charAt(i)) == -1) {
                    uniqueChars = false;
                }
            }
        }
        return uniqueChars;
    }

    /** Represents the characters of the Alphabet. */
    private String _chars;

}
