/* KarpRabin.java
   A Java implementation of the Karp-Rabin seminumerical string
   matching algorithm.
   See CLRS Section 32.2.
*/
import java.util.Map;
import java.util.HashMap;
import java.math.BigInteger;

public class KarpRabin {
    final long q, b;
    public static final int NONE = -1;
    Map<Character, Integer> digitMap;

    public KarpRabin(String alphabet) {
        digitMap = new HashMap<>();
        for (int i = 0; i < alphabet.length(); ++i) {
            digitMap.put(alphabet.charAt(i), i);
        }
        q = 26900927;  // A prime such that b*q can reasonably fit in
                       // a word for most values of b.
        b = alphabet.length();
        if (b >= q) {
            throw new IllegalArgumentException("b cannot be larger than " + q);
        }
    }

    public int match(String pattern, String text) {
        // Returns location of first match of pattern in text.
        if (pattern.length() > text.length()) {
            throw new IllegalArgumentException("Pattern longer than text.");
        }
        int[] T = digitValue(text);
        int[] P = digitValue(pattern);
        long h, p, t;
        p = t = 0;
        h = expt(b, pattern.length()-1).mod(BigInteger.valueOf(q))
            .longValue(); // See note [7].

        // Calculate fingerprint of pattern and of first
        // pattern.length-length group in text.
        for (int i = 0; i < pattern.length(); ++i) {
            p = (b*p + P[i]) % q;
            t = (b*t + T[i]) % q;
        }

        for (int s = 0; s < text.length() - pattern.length(); ++s) {
            if (p == t) {
                if (pattern.equals(text.substring(s, s+pattern.length()))) {
                    return s;
                }
            }
            assert s < T.length && s + pattern.length() < T.length :
            "s is " + s + " and s+pattern.length() is " +
                (s + pattern.length());
            t = (b * (t - T[s]*h) + T[s + pattern.length()]) % q;
            // Handle Java's modulus behavior; see note[2].
            t += q;
            t %= q;
            assert t >= 0 : "t is below 0, has value " + t;
        }

        // See note [4].
        if (p == t) {
            if (pattern.equals(text.substring(text.length() - pattern.length(), text.length())))
                return text.length() - pattern.length();
        }
        return NONE;
    }

    int[] digitValue(String text) {
        // Converts input string into individual digits in base-alphabet.length.
        int[] digits = new int[text.length()];
        for (int i = 0; i < text.length(); ++i) {
            Integer retrieved = digitMap.get(text.charAt(i));
            if (retrieved == null) {
                throw new IllegalArgumentException("Invalid character " +
                                                   text.charAt(i) + " in text.");
            }
            digits[i] = retrieved;
        }
        assert text.length() == digits.length :
        "Length of digit value != length of original text.";
        return digits;
    }

    BigInteger expt(long base, long power) {
        BigInteger val = BigInteger.ONE;
        BigInteger b = BigInteger.valueOf(base);
        for(; power > 0; --power) {
            val = val.multiply(b);
        }
        return val;
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
  }
}
