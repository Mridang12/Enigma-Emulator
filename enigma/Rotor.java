package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Mridang Sheth
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _setting = 0;
        _hasRing = false;
    }


    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _setting;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        _setting = posn % alphabet().size();
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        if (this.alphabet().toInt(cposn) != -1) {
            _setting = this.alphabet().toInt(cposn);
        } else {
            throw new EnigmaException("Character "
                    + cposn + "not present in Alphabet");
        }
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        if (_hasRing) {
            return _permutation.wrap(_permutation.permute(
                    p + setting() - _ringsetting) - setting() + _ringsetting);
        } else {
            return _permutation.wrap(_permutation.permute(
                    p + setting()) - setting());
        }

    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        if (_hasRing) {
            return _permutation.wrap(_permutation.invert(
                    e + setting() - _ringsetting) - setting() + _ringsetting);
        } else {
            return _permutation.wrap(_permutation.invert(
                    e + setting()) - setting());
        }

    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    /**
     * Configure ringstellung parameters of this rotor.
     * @param hasRing : The attribute
     * @param ringsetting : The attribute
     */
    void configureRing(boolean hasRing, int ringsetting) {
        _hasRing = hasRing;
        _ringsetting = ringsetting;
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    /** The current setting of the rotor. */
    private int _setting;

    /** True if rotor has a Ringstellung. */
    private boolean _hasRing;

    /** The current setting of the Ringstellung. */
    private int _ringsetting;

}
