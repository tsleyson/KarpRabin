import unittest
from KarpRabin import KarpRabin

class KarpRabinTest(unittest.TestCase):
    def setUp(self):
        self.upperLower = KarpRabin("ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz")
        
    def testStringMatch(self):
        text = "TERPSICHOREAN"
        pattern = "SICHO"
        self.assertEqual(4, self.upperLower.match(pattern, text))

    def testProblematicStringMatch(self):
        text = "Sore Nadeko da"
        pattern = "Nade"
        self.assertEqual(5, self.upperLower.match(pattern, text))

        pattern = "Nadek"
        self.assertEqual(5, self.upperLower.match(pattern, text))

        text = "I heard zach say you were ALL broken Up when Your poodle died"
        pattern = "zach say you were ALL broken Up"
        self.assertEqual(8, self.upperLower.match(pattern, text))

if __name__ == "__main__":
    unittest.main()
        
