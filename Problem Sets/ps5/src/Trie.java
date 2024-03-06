import java.util.ArrayList;

public class Trie {


    public TrieNode root;
    // Wildcards
    final char WILDCARD = '.';

    private class TrieNode {
        // TODO: Create your TrieNode class here.
        TrieNode[] presentChars = new TrieNode[62];
        // 0-9 10 values for 0-9 (0 - 48)
        // 10-35 26 values for A-Z (A-55)
        // 36-61 26 values for a-z (a-61)

        boolean end = false;
        int asciiToInt(char c){
            if (c < 123 && c > 96){ //a-z
                return c-61;
            }
            else if (c < 91 && c > 64){ //A-Z
                return c-55;
            }
            else if (c < 58 && c > 47){ //0-9
                return c-48;
            }
            return -1;
        }

        char intToAscii(int i){
            if (i < 10 && i > -1) return (char) (i + 48); //0-9
            else if (i < 36 && i > 9) return (char) (i + 55); //A-Z
            else if (i < 62 && i > 35) return (char) (i + 61); //a-z
            else return (char) -1; //this one doesnt make sense haha
        }



        TrieNode exists(char c){
            //System.out.println(asciiToInt(c));
            if (presentChars[asciiToInt(c)] != null){
                return presentChars[asciiToInt(c)];
            }
            return null;
        }

        TrieNode existsOrCreate(char c){
            if (presentChars[asciiToInt(c)] == null){
                presentChars[asciiToInt(c)] = new TrieNode();
            }
            return presentChars[asciiToInt(c)];
        }

        void endString(){
            end = true;
        }

        TrieNode getNode(char c){
            return presentChars[asciiToInt(c)];
        }

        ArrayList<Character> getNonEmptyChars(){
            ArrayList<Character> result = new ArrayList<>();
            for (int i = 0; i < 62; i++){
                if (presentChars[i] != null){
                    result.add(intToAscii(i));
                }

            }
            return result;
        }

    }

    public Trie() {
        // TODO: Initialise a trie class here.
        root = null;
    }

    /**
     * Inserts string s into the Trie.
     *
     * @param s string to insert into the Trie
     */
    void insert(String s) {
        // TODO
        if (root == null){
            root = new TrieNode();
        }
        TrieNode currentNode = root;
        for (char c: s.toCharArray()){
            //System.out.println(c);
            currentNode = currentNode.existsOrCreate(c);
        }
        currentNode.endString();

    }

    /**
     * Checks whether string s exists inside the Trie or not.
     *
     * @param s string to check for
     * @return whether string s is inside the Trie
     */
    boolean contains(String s) {
        // TODO
        TrieNode currentNode = root;
        if (currentNode == null) return false;
        for (char c: s.toCharArray()){
            TrieNode node = currentNode.exists(c);
            if (node == null) return false;
            else currentNode = node;
        }
        if (currentNode.end == true){
            return true;
        }
        return false;
    }



    void recurse(TrieNode currentNode, String current, String remaining, ArrayList<String> results, int limit){
        while (remaining.length() > 0){
            if (currentNode == null) return;
            if (remaining.charAt(0) != '.'){
                //if (currentNode == null) return;
                currentNode = currentNode.getNode(remaining.charAt(0));
                current += remaining.charAt(0);
                if (remaining.length() == 1) remaining = "";
                else remaining = remaining.substring(1);
            }
            else{
                //System.out.println(remaining);
                if (remaining.length() == 1) remaining = "";
                else remaining = remaining.substring(1);
                ArrayList<Character> chars = currentNode.getNonEmptyChars();
                for (Character c: chars){
                    //System.out.println(c);
                    recurse(currentNode.getNode(c), current + c , remaining, results, limit);
                }
                return;
            }
        }

        //System.out.println(current);
        //start recursing
        while (results.size() < limit){
            if (currentNode == null) return;
            if (currentNode.end == true) results.add(current);
            ArrayList<Character> chars = currentNode.getNonEmptyChars();
            for (Character c: chars){
                recurse(currentNode.getNode(c), current + c , "", results, limit);
            }
            return;
        }


    }
    /**
     * Searches for strings with prefix matching the specified pattern sorted by lexicographical order. This inserts the
     * results into the specified ArrayList. Only returns at most the first limit results.
     *
     * @param s       pattern to match prefixes with
     * @param results array to add the results into
     * @param limit   max number of strings to add into results
     */
    void prefixSearch(String s, ArrayList<String> results, int limit) {
        if (root == null) return;
        TrieNode currentNode = root;
        recurse(currentNode, "", s, results, limit);

    }


    // Simplifies function call by initializing an empty array to store the results.
    // PLEASE DO NOT CHANGE the implementation for this function as it will be used
    // to run the test cases.
    String[] prefixSearch(String s, int limit) {
        ArrayList<String> results = new ArrayList<String>();
        prefixSearch(s, results, limit);
        return results.toArray(new String[0]);
    }


    public static void main(String[] args) {
        Trie t = new Trie();
//        t.insert("peter");
//        //System.out.println(t.contains("peter"));
//        t.insert("piper");
//        t.insert("picked");
//        t.insert("a");
//        t.insert("peck");
//        t.insert("of");
//        t.insert("pickled");
//        t.insert("peppers");
//        t.insert("pepppito");
//        t.insert("pepi");
//        t.insert("pik");
        t.insert("pass");
        t.insert("abbde");
        t.insert("abcd");
        t.insert("abcdef");
        t.insert("abed");
        t.insert("abd");

        //String[] result1 = t.prefixSearch("pe", 10);
        //String[] result2 = t.prefixSearch("pe.", 10);
        String[] result3 = t.prefixSearch("..s.", 5);
        //for (String res: result1) System.out.println(res);
        for (String res: result3) System.out.println(res);

        // result1 should be:
        // ["peck", "pepi", "peppers", "pepppito", "peter"]
        // result2 should contain the same elements with result1 but may be ordered arbitrarily
    }
}
