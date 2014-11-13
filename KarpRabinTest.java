import junit.framework.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

public class KarpRabinTest extends TestCase{
    KarpRabin fullAlphabet  = new KarpRabin("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    KarpRabin restricted = new KarpRabin("AGCT");
    KarpRabin upperLower = new KarpRabin("ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                                         " abcdefghijklmnopqrstuvwxyz");

    String msg = "Got %d, expected %d on pattern %s\n";

    public void testStringMatch() {
        // Successful and then unsuccessful matches with each alphabet.
        String text = "ACTGCATGCAGTTTAG";
        String pattern = "TGCA";
        int result = restricted.match(pattern, text);
        assertEquals(String.format(msg, result, 2, pattern), 2, result);

        pattern = "AAA";
        result = restricted.match(pattern, text);
        assertEquals(String.format(msg, result, KarpRabin.NONE, pattern),
                     KarpRabin.NONE, result);

        // test for match on the end of a string.
        pattern = "TAG";
        result = restricted.match(pattern, text);
        assertEquals(String.format(msg, result, 13, pattern), 13, result);

        // Test for one-character match.
        text = "T";
        pattern = "T";
        result = restricted.match(pattern, text);
        assertEquals(String.format(msg, result, 0, pattern), 0, result);

        // The empty string always matches starting at zero.
        pattern = "";
        result = restricted.match(pattern, text);
        assertEquals(String.format(msg, result, 0, pattern),
                     0, result);

        text = "TERPSICHOREAN";
        pattern = "SICHO";
        result = fullAlphabet.match(pattern, text);
        assertEquals(String.format(msg, result, 4, pattern), 4, result);

        // Test if the text and pattern are identical.
        result = fullAlphabet.match(text, text);
        assertEquals(String.format(msg, result, 0, pattern), 0, result);
        
        pattern = "ORE";
        result = fullAlphabet.match(pattern, text);
        assertEquals(String.format(msg, result, 8, pattern), 8, result);

        pattern = "ADSF";
        result = fullAlphabet.match(pattern, text);
        assertEquals(String.format(msg, result, KarpRabin.NONE, pattern),
                     KarpRabin.NONE, result);
    }

    public void testUpperLowerNoOverflow() {
        // These use upperLower but shouldn't cause any overflow.
        // These all pass with an int as the primary numeric type.
        String text, pattern;
        int result;
        
        text = "BAAAAA";
        pattern = "AAA";
        result = upperLower.match(pattern, text);
        assertEquals(String.format(msg, result, 1, pattern), 1, result);

        text = "B ZZZZZZ";
        pattern = "ZZZZ";
        result = upperLower.match(pattern, text);
        assertEquals(String.format(msg, result, 2, pattern), 2, result);

        text = "bxxxxxxxxxxxxxxxxzzz";
        pattern = "zz";
        result = upperLower.match(pattern, text);
        assertEquals(String.format(msg, result, 17, pattern), 17, result);

        text = "SORE NADEKO DA YO";
        pattern = "NADE";
        result = upperLower.match(pattern, text);
        assertEquals(String.format(msg, result, 5, pattern), 5, result);
    }

    public void testStringMatchWithOverflow() {
        // These tests should cause overflow and make the modulus
        // matter.
        // All but the first fail with an int.
        // Those with fewer than 11 characters succeed with everything
        // as a long.
        // They all seem to work with a BigInteger or with my hybrid
        // long/BigInteger scheme.
        String text, pattern;
        int result;

        text = "Sore Nadeko da YO";

        pattern = "Nade";  // Four--the magic length where ints don't overflow.
        result = upperLower.match(pattern, text);
        assertEquals(String.format(msg, result, 5, pattern),
                     5, result);        

        pattern = "Nadek";
        result = upperLower.match(pattern, text);
        assertEquals(String.format(msg, result, 5, pattern),
                     5, result);

        // Look for match at end of text.
        pattern = "da YO";
        result = upperLower.match(pattern, text);
        assertEquals(String.format(msg, result, 12, pattern), 12, result);

        pattern = "nadeko";
        result = upperLower.match(pattern, text);
        assertEquals(String.format(msg, result, KarpRabin.NONE, pattern),
                     KarpRabin.NONE, result);

        text = "zAAAAAAAAAAAAAAAAAAA";
        pattern = "AAAAAAAAAAA";
        result = upperLower.match(pattern, text);
        assertEquals(String.format(msg, result, 1, pattern), 1, result);

        // Following two only succeed with long because it's at the start,
        // where we calculate the fingerprint with the straight method.
        text = "Azzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
        pattern = "zzzz";
        result = upperLower.match(pattern, text);
        assertEquals(String.format(msg, result, 1, pattern), 1, result);
        
        text = "I heard zach say you were ALL broken Up when Your poodle died";

        pattern = "I heard zach say you were ALL broken Up";
        result = upperLower.match(pattern, text);
        assertEquals(String.format(msg, result, 0, pattern), 0, result);

        // 10--the magic pattern length where overflow doesn't happen with a long. 
        pattern = " heard zac";
        result = upperLower.match(pattern, text);
        assertEquals(String.format(msg, result, 1, pattern), 1, result);

        pattern = "you were A";
        result = upperLower.match(pattern, text);
        assertEquals(String.format(msg, result, 17, pattern), 17, result);

        pattern = " heard zach";
        result = upperLower.match(pattern, text);
        assertEquals(String.format(msg, result, 1, pattern), 1, result);
        
        pattern = "zach say you were ALL broken Up";
        result = upperLower.match(pattern, text);
        assertEquals(String.format(msg, result, 8, pattern), 8, result);

        pattern = " heard zach say you were ALL broken Up when Your poodle died";
        result = upperLower.match(pattern, text);
        assertEquals(String.format(msg, result, 1, pattern), 1, result);

        // This succeeds with an int or long, but probably for the wrong reason: it
        // overflows, but since there is no match anyway, it returns
        // NONE as it should.
        pattern = "Zach said YOU were all broken up over HIS poodle";
        result = upperLower.match(pattern, text);
        assertEquals(String.format(msg, result, KarpRabin.NONE, pattern),
                     KarpRabin.NONE, result);
    }
    
    public void testConstruction() {
        int expectedQ = 26900927;
        int expectedB = 4;
        Map<Character, Integer> expectedDigitMap = new HashMap<>();
        expectedDigitMap.put('A', 0);
        expectedDigitMap.put('G', 1);
        expectedDigitMap.put('C', 2);
        expectedDigitMap.put('T', 3);

        assertEquals(String.format("Modulus incorrect; was %d, should have been %d",
                                  restricted.q, expectedQ),
                    expectedQ,
                    restricted.q);
        assertEquals(String.format("Base incorrect; was %d, expected %d",
                                   restricted.b, expectedB),
                     expectedB,
                     restricted.b);
        assertEquals("Digit map is incorrect",
                    expectedDigitMap,
                    restricted.digitMap); 

        assertEquals(String.format("Modulus incorrect; was %d, should have been %d",
                                   fullAlphabet.q, expectedQ),
                     expectedQ,
                     fullAlphabet.q);
        
        expectedDigitMap = new HashMap<>();
        expectedDigitMap.put('A', 0);
        expectedDigitMap.put('C', 2);
        expectedDigitMap.put('B', 1);
        expectedDigitMap.put('E', 4);
        expectedDigitMap.put('D', 3);
        expectedDigitMap.put('G', 6);
        expectedDigitMap.put('F', 5);
        expectedDigitMap.put('I', 8);
        expectedDigitMap.put('H', 7);
        expectedDigitMap.put('K', 10);
        expectedDigitMap.put('J', 9);
        expectedDigitMap.put('M', 12);
        expectedDigitMap.put('L', 11);
        expectedDigitMap.put('O', 14);
        expectedDigitMap.put('N', 13);
        expectedDigitMap.put('Q', 16);
        expectedDigitMap.put('P', 15);
        expectedDigitMap.put('S', 18);
        expectedDigitMap.put('R', 17);
        expectedDigitMap.put('U', 20);
        expectedDigitMap.put('T', 19);
        expectedDigitMap.put('W', 22);
        expectedDigitMap.put('V', 21);
        expectedDigitMap.put('Y', 24);
        expectedDigitMap.put('X', 23);
        expectedDigitMap.put('Z', 25);

        assertEquals("Digit map is incorrect",
                     expectedDigitMap,
                     fullAlphabet.digitMap); 
    }

    public void testDigitValue() {
        int[] value = restricted.digitValue("ACTGCTA");
        int[] expected = new int[] {0, 2, 3, 1, 2, 3, 0};
        
        assertTrue("Digit value of string incorrectly computed.",
                   Arrays.equals(expected, value));

        value = fullAlphabet.digitValue("ZAKU");
        expected = new int[] {25, 0, 10, 20};
        assertTrue("Digit value of string was incorrectly computed.",
                   Arrays.equals(expected, value));

        value = upperLower.digitValue("no Zaku");
        expected = new int[] {40, 41, 26, 25, 27, 37, 47};
        assertTrue("Digit value of string was incorrectly computed.",
                   Arrays.equals(expected, value));
    }
    
    public KarpRabinTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(KarpRabinTest.class);
    }

    public static void main(String[] args) {
        TestSuite suite = new TestSuite();
        if (args.length > 0) {
            // Run tests passed in as command line args.
            for (int i = 0; i < args.length; ++i) {
                suite.addTest(new KarpRabinTest(args[i]));
            }
        } else {
            // Dynamically discover all tests, or use
            // user-defined suite.
            suite.addTest(KarpRabinTest.suite());
        }
        junit.textui.TestRunner.run(suite);
    }
}
