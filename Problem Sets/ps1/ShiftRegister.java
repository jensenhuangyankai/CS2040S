///////////////////////////////////
// This is the main shift register class.
// Notice that it implements the ILFShiftRegister interface.
// You will need to fill in the functionality.
///////////////////////////////////

import java.util.Arrays;

/**
 * class ShiftRegister
 * @author
 * Description: implements the ILFShiftRegister interface.
 */
public class ShiftRegister implements ILFShiftRegister {
    ///////////////////////////////////
    // Create your class variables here
    ///////////////////////////////////
    // TODO:

    int[] register;
    int tapBit;

    ///////////////////////////////////
    // Create your constructor here:
    ///////////////////////////////////
    ShiftRegister(int size, int tap) {
        // TODO:
        register = new int[size];
        tapBit = tap;
    }

    ///////////////////////////////////
    // Create your class methods here:
    ///////////////////////////////////
    /**
     * setSeed
     * @param seed
     * Description:
     */
    @Override
    public void setSeed(int[] seed) {
        // TODO:
        register = seed;
    }

    /**
     * shift
     * @return
     * Description:
     */
    @Override
    public int shift() {
        // TODO:
        int xorResult = register[register.length-1] ^ register[tapBit];
        for (int i=register.length-1; i>0; i--){
            register[i] = register[i-1];
        }
        
        register[0] = xorResult;
        //System.out.println(Arrays.toString(register));
        return register[0];
    }

    /**
     * generate
     * @param k
     * @return
     * Description:
     */
    @Override
    public int generate(int k) {
        // TODO:
        int returnValue = 0;

        while (k > 0){
            returnValue = (int) (returnValue + Math.pow(2,k-1) * shift());
            k--;
        }

        return returnValue;
    }

    /**
     * Returns the integer representation for a binary int array.
     * @param array
     * @return
     */
    private int toDecimal(int[] array) {
        // TODO:
        return 0;
    }
}
