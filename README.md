# UFO2
Fast tally assignment

# How did I get here
From the profiler I got the tallyChars was apx. 50% cpu time, while reading the file and creating the map was the remaining 50% (this was without timing the print_tally method). The tallychars has 3 main cpu-time sinks, reading one char ata time from the file stream (reader) updating an existing entry in the map, by first loading the entry then adding 1 and then storing the entry back into the map, and finally creating a new entry in the map (incase of first occurance of char read) - so basicly all of it.

# Map
No need to read whats stored in the before updating the value - infact no need for a map at all since chars are already numbered (aschii) just use that value as index on array (int[128] will work for this assignment)

# Reader
No need to maintain a filestream in order to read a single byte in a while loop per interation. Load the whole file (FoundationSeries.txt) into byte-array and traverse it in a for-loop, use the byte value, of the byte read in each loop, as index in the array created in place of the map


# New code
So as oppose to this
```
    int b;
    while ((b = reader.read()) != -1) {
        try {
            freq.put(b, freq.get(b) + 1);
        } catch (NullPointerException np) {
            freq.put(b, 1L);
        };
    }

``` 
You end up with this
```
    for (int i = 0; i < byteArr.length; i++) {
        freq[byteArr[i]] ++; 
    }

```


# Timing
I've made 2 versions of the mark-5 timing methods, just so I could call both the original tally and the fast tally methods from the main.

I changed the mark5-timing method to print in ms. instead of ns. Also added the storage as parameter so the result can be printed after the tally is completed - THE PRINTING OF THE TALLY IS NOT INCLUDED IN THE TIME MEASUREMENTS.

Included in the timing is all the setup the tally-method requires to run this means; reading the file,creating the storage/allocating room for the storage (reading from the filestream was apx. 6% of cpu time spend according to the profiler).


# Console output
This is the most important snippet from the console output - the entire dump is available in the included [output.txt file](https://github.com/cph-js284/UFO2/blob/master/output.txt)
```
*******************************************************
USING ORIGINAL TALLY METHOD - MARK5 TIMING
*******************************************************
 108.5 ms +/-    32.28  2
  94.7 ms +/-     6.14  4

*******************************************************
USING FAST TALLY METHOD - MARK5 TIMING
*******************************************************
   3.4 ms +/-     1.93  2
   2.3 ms +/-     0.32  4
   2.1 ms +/-     0.18  8
   2.1 ms +/-     0.26 16
   2.0 ms +/-     0.19 32
   2.0 ms +/-     0.14 64
   2.0 ms +/-     0.06 128
   2.0 ms +/-     0.04 256
```
<b>Improvement apx. 4700%</b>


# Test it yourself
Requirements: must have java and maven installed.
1) clone the repo
2) Navigate to root and execute
for linux
```
mvn exec:java -Dexec.mainClass="dk.cph.js284.App" -q
```
for windows
```
mvn exec:java -D"exec.mainClass"="dk.cph.js284.App" -q
```

# Documentation
I consider the the file output.txt adaquate documentation in that; it contains output from both Kaspers tally and my own tally methods, and they match.

# Note
My fast tally method will fail if the aschii read from the file is above 127, in which case the storage should be expanded to integer-array size 256.

Compared to Kasper, I've dumbed down the print_tally method so that it now just prints out whatever chars it has read. So please note, if you compare the results to Kaspers tally - that Kasper only counts and prints lowercase letters (int dist = 'a' - 'A'), effectively ignoring anything else. If you were to actually use the same dumbed down printTally method I've made, in Kaspers code, you'd see matching results.
