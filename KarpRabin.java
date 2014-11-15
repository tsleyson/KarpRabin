/* KarpRabin.java
   A Java implementation of the Karp-Rabin seminumerical string
   matching algorithm.
   See CLRS Section 32.2.
*/

public class KarpRabin {
    private static final int BASE = 103;
    public static final int NONE = -1;

    public static int match(String pattern, String text) {
        // Returns location of first match of pattern in text.
        if (pattern.length() > text.length()) {
            return NONE;
        }
        int out, phash, thash;
        out = 1;

        for (int expt = pattern.length(); expt > 1; --expt) {
            out *= BASE;
        }
        
        // Calculate fingerprint of pattern and of first
        // pattern.length-length group in text.
        phash = thash = 0;
        for (int i = 0; i < pattern.length(); ++i) {
            phash = phash*BASE + pattern.charAt(i);
            thash = thash*BASE + text.charAt(i);
        }
        
        for (int s = 0; s < text.length() - pattern.length(); ++s) {
            if (phash == thash) {
                if (pattern.equals(text.substring(s, s+pattern.length()))) {
                    return s;
                }
            }
            assert s < text.length() &&
                s + pattern.length() < text.length() :
            "s is " + s + " and s+pattern.length() is " +
                (s + pattern.length());
            thash = BASE*(thash - out*text.charAt(s)) +
                text.charAt(s+pattern.length());
        }

        // See note [4].
        if (phash == thash) {
            if (pattern.equals(text.substring(text.length() - pattern.length(),
                                              text.length())))
                return text.length() - pattern.length();
        }
        return NONE;
    }

    private static int hash(String s) {
        int hash = 0;
        for (int i = 0; i < s.length(); i++) {
            hash *= 31;
            hash += s.charAt(i);
        }
        return hash;
    }

    public static void main(String[] args) {
        String s1 = "abcdefghij";
        String s2 = s1.substring(1) + "k";
        int pow = 1;
        for (int i = 0; i < s1.length(); i++) {
            pow *= 31;
        }
        System.out.printf("hash(%s) = %d%n", s1, hash(s1));
        System.out.printf("hash(%s) = %d%n31 * hash(%s) - (31^%d * %s) + %s = %s%n",
        s2,
        hash(s2),
        s1,
        s1.length(),
        s1.charAt(0),
        s2.charAt(s2.length() - 1),
        31 * hash(s1) - (pow * s1.charAt(0)) + s2.charAt(s2.length() - 1));

        System.out.println("" + (int)'a');
    }
}
