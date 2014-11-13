q = 26900927
def fingerprints(s, m, d):
    global q
    t = 0
    h = (len(d)**(m-1)) % q
    for i in range(m):
        t = (len(d)*t + d[s[i]]) % q
    for i in range(len(s) - m):
        yield (s[i:i+m], t)
        t = (len(d)*(t - d[s[i]]*h) + d[s[i+m]]) % q

def fingerprint(s, d):
    global q
    p = 0
    print("len(d): {}, q: {}".format(len(d), q))
    for i in range(len(s)):
        print(p)
        p = (len(d)*p + d[s[i]]) % q
        if isinstance(p, long):
            print("Went long at {}".format(p))
    return p

def direct(s, d):
    "Directly calculate fingerprint from definition."
    global q
    b = len(d)
    return sum([n*b**m for (n, m) in zip([d[l] for l in s], reversed(range(b)))]) % q

class KarpRabin(object):
    "Uses Karp-Rabin to match a string."
    def __init__(self, alphabet):
        self.digit_values = {k:v for (k, v) in zip(alphabet, range(len(alphabet)))}
        self.q = 26900927
        self.b = len(alphabet)

    def match(self, pattern, text):
        assert len(pattern) <= len(text)
        T = [self.digit_values[k] for k in text]
        P = [self.digit_values[k] for k in pattern]
        h = self.b**(len(pattern) - 1) % self.q
        p = t = 0

        for i in range(len(pattern)):
            p = (self.b*p + P[i]) % q
            t = (self.b*t + T[i]) % q

        if (p == t): return 0

        for s in range(len(text) - len(pattern)):
            if (p == t):
                if pattern == text[s:s+len(pattern)]:
                    return s
            assert s < len(T) and s + len(pattern) < len(T)
            t = (self.b*(t - T[s]*h) + T[s+len(pattern)]) % self.q

        return None

    def __str__(self):
        return str(self.digit_values, " ", self.q, " ", self.b)

