import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * This is the main class for your Markov Model.
 *
 * Assume that the text will contain ASCII characters in the range [1,255].
 * ASCII character 0 (the NULL character) will be treated as a non-character.
 *
 * Any such NULL characters in the original text should be ignored.
 */
public class MarkovModel {

	private int order;
	// Use this to generate random numbers as needed
	private Random generator = new Random();

	// This is a special symbol to indicate no character
	public static final char NOCHARACTER = (char) 0;

	private Map<String, Integer[]> hashmap; //hashmap of kgram to frequency of each ascii value


	/**
	 * Constructor for MarkovModel class.
	 *
	 * @param order the number of characters to identify for the Markov Model sequence
	 * @param seed the seed used by the random number generator
	 */
	public MarkovModel(int order, long seed) {
		// Initialize your class here
		this.order = order;
		// Initialize the random number generator
		generator.setSeed(seed);
	}

	/**
	 * Builds the Markov Model based on the specified text string.
	 */
	public void initializeText(String text) {
		// Build the Markov model here
		hashmap = new HashMap<>();
		for (int i = 0; i < text.length() - order ; i ++) {
			String substr = text.substring(i, i + order);

			Integer[] zeroArray = new Integer[256];
			Arrays.fill(zeroArray, 0);
			Integer[] value = hashmap.getOrDefault(substr, zeroArray);
			value[text.charAt(i + order)] += 1;
			value[0] += 1;
			hashmap.put(substr, value);
		}

	}

	/**
	 * Returns the number of times the specified kgram appeared in the text.
	 */
	public int getFrequency(String kgram) {
		if (kgram.length() != order) {
			return 0;
		}

		Integer[] values =  hashmap.get(kgram);
		if (values == null) return 0;
		int freq = values[0];
		return freq;

	}

	/**
	 * Returns the number of times the character c appears immediately after the specified kgram.
	 */
	public int getFrequency(String kgram, char c) {
		if (kgram.length() != order) {
			return 0;
		}
		if (hashmap.get(kgram) == null) {
			return 0;
		}

		else {
			return hashmap.get(kgram)[c];
		}

	}

	/**
	 * Generates the next character from the Markov Model.
	 * Return NOCHARACTER if the kgram is not in the table, or if there is no
	 * valid character following the kgram.
	 */
	public char nextCharacter(String kgram) {
		// See the problem set description for details
		// on how to make the random selection.
		int noOfChars = getFrequency(kgram);
		if (noOfChars == 0){
			return NOCHARACTER;
		}

		int random = generator.nextInt(noOfChars);
		Integer[] arr = hashmap.get(kgram);
		for (int i = 1 ; i < 256; i++){
			if (random - arr[i] < 0) return (char) i;
			else random = random - arr[i];
		}
		return NOCHARACTER;
	}
}
