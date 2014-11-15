import junit.framework.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

public class KarpRabinTest extends TestCase{
    String msg = "Got %d, expected %d on pattern %s\n";

    public void testStringMatch() {
        // Successful and then unsuccessful matches.
        String text = "ACTGCATGCAGTTTAG";
        String pattern = "TGCA";
        int result = KarpRabin.match(pattern, text);
        assertEquals(String.format(msg, result, 2, pattern), 2, result);

        pattern = "AAA";
        result = KarpRabin.match(pattern, text);
        assertEquals(String.format(msg, result, KarpRabin.NONE, pattern),
                     KarpRabin.NONE, result);

        // test for match on the end of a string.
        pattern = "TAG";
        result = KarpRabin.match(pattern, text);
        assertEquals(String.format(msg, result, 13, pattern), 13, result);

        // Test for one-character match.
        text = "T";
        pattern = "T";
        result = KarpRabin.match(pattern, text);
        assertEquals(String.format(msg, result, 0, pattern), 0, result);

        // The empty string always matches starting at zero.
        pattern = "";
        result = KarpRabin.match(pattern, text);
        assertEquals(String.format(msg, result, 0, pattern),
                     0, result);

        text = "TERPSICHOREAN";
        pattern = "SICHO";
        result = KarpRabin.match(pattern, text);
        assertEquals(String.format(msg, result, 4, pattern), 4, result);

        // Test if the text and pattern are identical.
        result = KarpRabin.match(text, text);
        assertEquals(String.format(msg, result, 0, pattern), 0, result);
        
        pattern = "ORE";
        result = KarpRabin.match(pattern, text);
        assertEquals(String.format(msg, result, 8, pattern), 8, result);

        pattern = "ADSF";
        result = KarpRabin.match(pattern, text);
        assertEquals(String.format(msg, result, KarpRabin.NONE, pattern),
                     KarpRabin.NONE, result);
        
        text = "BAAAAA";
        pattern = "AAA";
        result = KarpRabin.match(pattern, text);
        assertEquals(String.format(msg, result, 1, pattern), 1, result);

        text = "B ZZZZZZ";
        pattern = "ZZZZ";
        result = KarpRabin.match(pattern, text);
        assertEquals(String.format(msg, result, 2, pattern), 2, result);

        text = "bxxxxxxxxxxxxxxxxzzz";
        pattern = "zz";
        result = KarpRabin.match(pattern, text);
        assertEquals(String.format(msg, result, 17, pattern), 17, result);

        text = "SORE NADEKO DA YO";
        pattern = "NADE";
        result = KarpRabin.match(pattern, text);
        assertEquals(String.format(msg, result, 5, pattern), 5, result);

                text = "Sore Nadeko da YO";

        pattern = "Nade";
        result = KarpRabin.match(pattern, text);
        assertEquals(String.format(msg, result, 5, pattern),
                     5, result);        

        pattern = "Nadek";
        result = KarpRabin.match(pattern, text);
        assertEquals(String.format(msg, result, 5, pattern),
                     5, result);

        // Look for match at end of text.
        pattern = "da YO";
        result = KarpRabin.match(pattern, text);
        assertEquals(String.format(msg, result, 12, pattern), 12, result);

        pattern = "nadeko";
        result = KarpRabin.match(pattern, text);
        assertEquals(String.format(msg, result, KarpRabin.NONE, pattern),
                     KarpRabin.NONE, result);
    }

    public void testStringMatchWithOverflow() {
        // These tests should cause overflow and ensure that the
        // arithmetic modulo 2^32 works.
        String text, pattern;
        int result;
        text = "zAAAAAAAAAAAAAAAAAAA";
        pattern = "AAAAAAAAAAA";
        result = KarpRabin.match(pattern, text);
        assertEquals(String.format(msg, result, 1, pattern), 1, result);

        text = "Azzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
        pattern = "zzzz";
        result = KarpRabin.match(pattern, text);
        assertEquals(String.format(msg, result, 1, pattern), 1, result);
        
        text = "I heard zach say you were ALL broken Up when Your poodle died";

        pattern = "I heard zach say you were ALL broken Up";
        result = KarpRabin.match(pattern, text);
        assertEquals(String.format(msg, result, 0, pattern), 0, result);

        pattern = " heard zac";
        result = KarpRabin.match(pattern, text);
        assertEquals(String.format(msg, result, 1, pattern), 1, result);

        pattern = "you were A";
        result = KarpRabin.match(pattern, text);
        assertEquals(String.format(msg, result, 17, pattern), 17, result);

        pattern = " heard zach";
        result = KarpRabin.match(pattern, text);
        assertEquals(String.format(msg, result, 1, pattern), 1, result);
        
        pattern = "zach say you were ALL broken Up";
        result = KarpRabin.match(pattern, text);
        assertEquals(String.format(msg, result, 8, pattern), 8, result);

        pattern = " heard zach say you were ALL broken Up when Your poodle died";
        result = KarpRabin.match(pattern, text);
        assertEquals(String.format(msg, result, 1, pattern), 1, result);

        pattern = "Zach said YOU were all broken up over HIS poodle";
        result = KarpRabin.match(pattern, text);
        assertEquals(String.format(msg, result, KarpRabin.NONE, pattern),
                     KarpRabin.NONE, result);
    }

    public void testUnicodeStringMatch() {
        // Test matches of Unicode strings.
        String text, pattern;
        int result;
     
        text = "IPA for 'carrot' is \u006b\u00e6\u0279\u01dd\u0241 in American English.";
        pattern = "\u006b\u00e6\u0279\u01dd\u0241";
        result = KarpRabin.match(pattern, text);
        assertEquals(String.format(msg, result, 20, pattern), 20, result);
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
