package enigma;

import java.util.HashMap;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Mridang Sheth
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {

        if (numRotors <= 0) {
            throw new EnigmaException("Number of rotors less than 1");
        }

        if (!(pawls < numRotors && pawls >= 0)) {
            throw new EnigmaException("0 <= PAWLS < NUMROTORS not satisfied");
        }

        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = new HashMap<String, Rotor>();
        _plugboard = null;
        for (Rotor rotor : allRotors) {
            _allRotors.put(rotor.name(), rotor);
        }
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {

        if (rotors.length != numRotors()) {
            throw error("Invalid number of rotors, tried to assign "
                    + rotors.length + " but configured : " + numRotors());
        }

        int moving = 0;
        _myRotors = new Rotor[rotors.length];

        for (int i = 0; i < rotors.length; i++) {
            if (!_allRotors.containsKey(rotors[i])) {
                throw error("Rotor with the name "
                        + rotors[i] + " does not exist.");
            }
            if (i == 0 && !_allRotors.get(rotors[i]).reflecting()) {
                throw error("First rotor not reflector.");
            }
            if (i < _numRotors - _pawls && i != 0) {
                if (_allRotors.get(rotors[i]).rotates()) {
                    throw error("Rotor at position "
                            + (i + 1) + " should be fixed");
                }
            }
            if (containsRotor(_allRotors.get(rotors[i]))) {
                throw error("Rotor with the name "
                        + rotors[i] + " repeated");
            }
            _myRotors[i] = _allRotors.get(rotors[i]);
            if (_myRotors[i].rotates()) {
                moving++;
            }
        }
        if (moving != _pawls) {
            throw error("Invalid number of moving rotors, found : "
                    + moving + " but configured : " + numPawls());
        }
    }

    /**
     * Returns true iff Rotor r is contained in _myrotors.
     * @param r : The rotor to be tested
     * @return True/False depending on condition
     */
    private boolean containsRotor(Rotor r) {
        for (Rotor rotor : _myRotors) {
            if (rotor != null && r.name().equals(rotor.name())) {
                return true;
            }
        }
        return false;
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */

    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw new EnigmaException("Invalid "
                    + "setting passed in Machine.setRotors");
        }

        for (int i = 1; i < numRotors(); i++) {
            _myRotors[i].set(setting.charAt(i - 1));
        }
    }

    /** Override of setrotors to account for the existence of Ringstellungs.
     * @param setting : Setting for the rotors.
     * @param ringSetting : Setting for the ringstellung. */
    void setRotors(String setting, String ringSetting) {
        setRotors(setting);
        if (ringSetting.length() != numRotors() - 1) {
            throw new EnigmaException("Invalid "
                    + "setting passed in Machine.setRotors");
        }
        for (int i = 1; i < numRotors(); i++) {
            _myRotors[i].configureRing(true,
                    _alphabet.toInt(ringSetting.charAt(i - 1)));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {

        boolean[] movesThisCycle = new boolean[_myRotors.length];
        movesThisCycle[_myRotors.length - 1] = true;
        for (int i = _myRotors.length - 1; i > 0; i--) {
            Rotor currentRotor = _myRotors[i];
            Rotor leftRotor = _myRotors[i - 1];

            if (currentRotor.atNotch() && leftRotor.rotates()) {
                movesThisCycle[i] = true;
                movesThisCycle[i - 1] = true;
            }
        }

        for (int i = 0; i < _myRotors.length; i++) {
            if (movesThisCycle[i]) {
                _myRotors[i].advance();
            }
        }

        if (_plugboard != null) {
            c = _plugboard.permute(c);
        }

        for (int i = _myRotors.length - 1; i >= 0; i--) {
            c = _myRotors[i].convertForward(c);
        }
        for (int i = 1; i < _myRotors.length; i++) {
            c = _myRotors[i].convertBackward(c);
        }

        if (_plugboard != null) {
            return _plugboard.invert(c);
        }
        return c;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        char[] convertedMsg = new char[msg.length()];
        for (int i = 0; i < msg.length(); i++) {
            if ("\t\n ".indexOf(msg.charAt(i)) == -1) {
                convertedMsg[i] = _alphabet.toChar(
                        convert(_alphabet.toInt(msg.charAt(i))));
            } else {
                convertedMsg[i] = msg.charAt(i);
            }
        }
        return new String(convertedMsg);
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of rotors in this machine. */
    private int _numRotors;

    /** Number of moving rotors in this machine. */
    private int _pawls;

    /** Hashmap of all possible rotors in this machine. */
    private HashMap<String, Rotor> _allRotors;

    /** Array of rotors present in this machine. */
    private Rotor[] _myRotors;

    /** plugboard of this machine. */
    private Permutation _plugboard;
}
