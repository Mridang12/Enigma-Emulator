package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Mridang Sheth
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notchPositions = new int[notches.length()];
        for (int i = 0; i < notches.length(); i++) {
            set(notches.charAt(i));
            _notchPositions[i] = setting();
        }
        set(0);
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    boolean atNotch() {
        for (int position : _notchPositions) {
            if (position == setting()) {
                return true;
            }
        }
        return false;
    }

    @Override
    void advance() {
        set((setting() + 1) % alphabet().size());
    }

    /** Array of notch positions in the rotor. */
    private int[] _notchPositions;

}
