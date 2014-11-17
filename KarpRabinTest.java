import junit.framework.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

public class KarpRabinTest extends TestCase{
    String msg = "Unexpected result with pattern %s";

    public void testStringMatch() {
        // Successful and then unsuccessful matches.
        String text = "ACTGCATGCAGTTTAG";
        String pattern = "TGCA";
        assertTrue(KarpRabin.match(pattern, text));

        pattern = "AAA";
        assertFalse(String.format(msg, pattern), KarpRabin.match(pattern, text));

        // test for match on the end of a string.
        pattern = "TAG";
        assertTrue(String.format(msg, pattern), KarpRabin.match(pattern, text));

        // Test for one-character match.
        text = "T";
        pattern = "T";
        assertTrue(String.format(msg, pattern), KarpRabin.match(pattern, text));

        // The empty string always matches starting at zero.
        pattern = "";
        assertTrue(String.format(msg, pattern), KarpRabin.match(pattern, text));

        text = "TERPSICHOREAN";
        pattern = "SICHO";
        assertTrue(String.format(msg, pattern), KarpRabin.match(pattern, text));

        // Test if the text and pattern are identical.
        assertTrue(String.format(msg, pattern), KarpRabin.match(text, text));
        
        pattern = "ORE";
        assertTrue(String.format(msg, pattern), KarpRabin.match(pattern, text));

        pattern = "ADSF";
        assertFalse(String.format(msg, pattern), KarpRabin.match(pattern, text));
        
        text = "BAAAAA";
        pattern = "AAA";
        assertTrue(String.format(msg, pattern), KarpRabin.match(pattern, text));

        text = "B ZZZZZZ";
        pattern = "ZZZZ";
        assertTrue(String.format(msg, pattern), KarpRabin.match(pattern, text));

        text = "bxxxxxxxxxxxxxxxxzzz";
        pattern = "zz";
        assertTrue(String.format(msg, pattern), KarpRabin.match(pattern, text));

        text = "SORE NADEKO DA YO";

        pattern = "NADE";
        assertTrue(String.format(msg, pattern), KarpRabin.match(pattern, text));

        text = "Sore Nadeko da YO";

        pattern = "Nade";
        assertTrue(String.format(msg, pattern), KarpRabin.match(pattern, text));

        pattern = "Nadek";
        assertTrue(String.format(msg, pattern), KarpRabin.match(pattern, text));

        // Look for match at end of text.
        pattern = "da YO";
        assertTrue(String.format(msg, pattern), KarpRabin.match(pattern, text));

        pattern = "nadeko";
        assertFalse(String.format(msg, pattern), KarpRabin.match(pattern, text));

        // If pattern longer than text, no match.
        pattern = "Sore Nadeko da YO, yo. Maji, Nadeko rappa ja nai!";
        assertFalse(String.format(msg, pattern), KarpRabin.match(pattern, text));
    }

    public void testStringMatchWithOverflow() {
        // These tests should cause overflow and ensure that the
        // arithmetic modulo 2^32 works.
        String text, pattern;
        int result;
        text = "zAAAAAAAAAAAAAAAAAAA";
        pattern = "AAAAAAAAAAA";
        assertTrue(String.format(msg, pattern), KarpRabin.match(pattern, text));
        

        text = "Azzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
        pattern = "zzzz";
        assertTrue(String.format(msg, pattern), KarpRabin.match(pattern, text));
        
        
        text = "I heard zach say you were ALL broken Up when Your poodle died";

        pattern = "I heard zach say you were ALL broken Up";
        assertTrue(String.format(msg, pattern), KarpRabin.match(pattern, text));
        

        pattern = " heard zac";
        assertTrue(String.format(msg, pattern), KarpRabin.match(pattern, text));
        

        pattern = "you were A";
        assertTrue(String.format(msg, pattern), KarpRabin.match(pattern, text));
        

        pattern = " heard zach";
        assertTrue(String.format(msg, pattern), KarpRabin.match(pattern, text));
        
        
        pattern = "zach say you were ALL broken Up";
        assertTrue(String.format(msg, pattern), KarpRabin.match(pattern, text));
        

        pattern = " heard zach say you were ALL broken Up when Your poodle died";
        assertTrue(String.format(msg, pattern), KarpRabin.match(pattern, text));
        

        pattern = "Zach said YOU were all broken up over HIS poodle";
        assertFalse(String.format(msg, pattern), KarpRabin.match(pattern, text));
    }

    public void testUnicodeStringMatch() {
        // Test matches of Unicode strings.
        String text, pattern;
        int result;
     
        text = "IPA for 'carrot' is \u006b\u00e6\u0279\u01dd\u0241 in American English.";
        pattern = "\u006b\u00e6\u0279\u01dd\u0241";
        assertTrue(String.format(msg, pattern), KarpRabin.match(pattern, text));
        
    }

    public void testAllMatches() {
        String pattern, text;
        text = "What are you doing? asks Misaka as Misaka asks what you're doing.";
        List<Integer> matches;
        
        pattern = "Misaka";
        matches = KarpRabin.allMatches(pattern, text);
        assertEquals(String.format(msg, pattern), Arrays.asList(25, 35), matches);

        pattern = "as";
        matches = KarpRabin.allMatches(pattern, text);
        assertEquals(String.format(msg, pattern), Arrays.asList(20, 32, 42), matches);

        pattern = "Mikoto";
        matches = KarpRabin.allMatches(pattern, text);
        assertSame(String.format(msg, pattern), Collections.EMPTY_LIST, matches);
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
