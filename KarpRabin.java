/* KarpRabin.java
   A Java implementation of the Karp-Rabin seminumerical string
   matching algorithm.
   See CLRS Section 32.2.
*/
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class KarpRabin {
    private static final int BASE = 103;  // Arbitrary base for hash.

    /**
       Finds first match, returns true, false if no match found.
    */
    public static boolean match(String pattern, String text) {
        if (pattern.length() > text.length()) {
            return false;
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
                    return true;
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
                return true;
        }
        return false;
    }

    /**
       Finds all matches of pattern in text, returns a list of
       matching positions.
    */
    public static List<Integer> allMatches(String pattern, String text) {
        if (pattern.length() > text.length()) {
            return Collections.EMPTY_LIST;
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

        List<Integer> matches = new ArrayList<>();
        for (int s = 0; s < text.length() - pattern.length(); ++s) {
            if (phash == thash) {
                if (pattern.equals(text.substring(s, s+pattern.length()))) {
                    matches.add(s);
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
                matches.add(text.length() - pattern.length());
        }
        if (matches.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        return matches;
    }

    /* Not ready for primetime yet.
    public static Set<PatternPosition> matchSet(Set<String> patterns, String text) {
        if (pattern.length() > text.length()) {
            return Collections.EMPTY_SET;
        }
        int out, thash;
        int[] phashes = new int[pattern.size()];
        out = 1;

        for (int expt = pattern.length(); expt > 1; --expt) {
            out *= BASE;
        }
        
        thash = 0;
        for (int i = 0; i < pattern.length(); ++i) {
            phash = phash*BASE + pattern.charAt(i);
            thash = thash*BASE + text.charAt(i);
        }

        Set<PatterPosition> matches = new TreeSet<>(); 
        for (int s = 0; s < text.length() - pattern.length(); ++s) {
            if (phash == thash) {
                if (pattern.equals(text.substring(s, s+pattern.length()))) {
                    matches.add(s);
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
                matches.add(text.length() - pattern.length());
        }
        if (matches.size() == 0) {
            return Collections.EMPTY_SET;
        }
        return matches;
        }
    */
}
