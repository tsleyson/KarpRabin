/*
 [1] It overflows there on the input "Sore Nadeko da" when it tries to
 calculate the fingerprint of "ore N". The debugger
 doesn't detect overflow; it promotes stuff to longs to make sure the
 answer is correct. But I took the value into Python and took it mod
 2**31, the number of non-negative values a Java int can hold,
 and got the same wrong answer as the program was getting. So we can't
 hold the values in an int. So we have two choices:
 1. Use a long and figure we'll never need enough characters to
 overflow a long.
 2. Use BigIntegers.

 [2] It didn't work before when I used longs in the main match routine
 because I forgot that Java's modulus operator will return negative
 answers. Which is mathematically correct, but means that the result
 of calculating a fingerprint is different depending on whether
 t - T[s]*h was negative. If t > T[s]*h for all s, this value is never
 negative, so you always get a positive answer and the straight method
 (used to calculate the fingerprint of the pattern and of
 text[0..m-1]) gives the same answer as the continuation method. But
 if there are values of t and s where t < T[s]*h, which there can be
 since t is taken modulo q (this wouldn't be possible if we didn't
 take t modulo q), then that term is negative. In this case, the
 straight method and the continuation method give different answers in
 Java, because the straight method always yields a positive number
 which is taken modulo q, but the continuation method can give a
 negative number, which is equivalent modulo q to the fingerprint from
 the straight method, but since we compare the fingerprints using
 regular ==, we don't take account of that and just end up seeing a
 non-matching fingerprint, leading us to reject matches calculated
 from the string using the continuation method.

 This is also why if the match was at the beginning of the text, it
 would always work: because the first fingerprint was always
 calculated with the straight method, so we would get the same
 fingerprint as we did from the pattern.

 [3] Also keep in mind that Java's modulus has weird precedence, so if
 you don't completely enclose the expression you're taking the modulus
 of in parentheses, you sometimes get the wrong answer for that reason.

[4] The last group of characters in the text is always a special case because in general you have
no idea how long it is. If it’s the same length as the pattern, you’re good, but otherwise you can’t
align it with the pattern.

In the textbook, they solve this problem with a test inside the loop:

    if s < n - m:
      t = // etc.

And let s run all the way up to equal n - m. This works since it’s the update to t that makes the array
index go out of bounds when you’re at the  last group and try to read T[s+m] when s+m is beyond the
end of the array. So by only doing that when you know there actually is an end, you avoid going
out of bounds.

In my version, the update to t always happens, but for the last group of characters, we don’t actually
end up checking if p == t because we update t to the fingerprint for the last group, then see that
s has gone out of bounds and end the loop. So my version just has a special case comparision of
p and t after the loop. I like this because you don’t have an extra condition being checked every
go-around of the loop.

[5] a + b mod m = a mod m + b mod m, and ab mod m = (a mod m)(b mod m).
It might be possible to use this to prevent overflows and use ints or
longs instead of going to big integers.

[6] Ideas for a parallel version in Clojure:
  - Make a closure around the text and an empty map of matched text to a
    list of match positions, inside an atom.
  - Have the function take a vector of patterns to match
  - Partition the text into size-m groups
  - Use reduce or loop/recur to run the fingerprint along the vector of groups
  - Call the matching function in a future with each pattern. (Alternately, we could
    stick the matching map in an agent and send the calls with each pattern to the agent.)
  - When done with a pattern, return the agent/atom map. The receiving thread will have
    a snapshot of the matching map which it can update by receiving from the other calls.

[7] I was calculating h (the high-order digit’s power) using Math.pow. This was all right
for small patterns, but with larger patterns we actually started to get floating point
error (it would kick in around 11 characters; we got an error of about +10 compared to
the exact BigInteger version). So I wrote the expt routine.

Then expt started to overflow, so I changed it to return a big int and took the modulus
of it as a big int, then converted that to a long, since q was set so that its entire
modulo group would fit within an int (thus easily fitting in a long). Now all the
tests pass....FOR NOW!!!

[8] Recall that q has some effect on how many spurious matches we find, so larger
values of q result in fewer spurious matches and can potentially speed up the algorithm.
 */
