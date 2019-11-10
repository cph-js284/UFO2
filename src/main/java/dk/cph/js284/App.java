package dk.cph.js284;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * Frequency analysis Inspired by
 * https://en.wikipedia.org/wiki/Frequency_analysis
 *
 * @author Jacob
 */
public class App {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        //used for profiling - uncomment these lines for profiling
        //tallyCharsOriginal(setupReaderOriginal("./src/main/ress/FoundationSeries.txt"), setupMapOriginal());
        //tallyCharsFast(setupReaderFast("./src/main/ress/FoundationSeries.txt"), setupMapFast());


        //used for timing - comment these lines for profiling
        System.out.println("*******************************************************");
        System.out.println("USING ORIGINAL TALLY METHOD - MARK5 TIMING");
        System.out.println("*******************************************************");
        Mark5TallyOriginal(setupMapOriginal());

        System.out.println("*******************************************************");
        System.out.println("USING FAST TALLY METHOD - MARK5 TIMING");
        System.out.println("*******************************************************");
        Mark5TallyFast(setupMapFast());
    }

    private static void tallyCharsFast(byte[] byteArr, int[] freq) throws IOException {
        for (int i = 0; i < byteArr.length; i++) {
           freq[byteArr[i]] ++; 
        }
    }

    private static byte[] setupReaderFast(String path) throws IOException{
        return Files.readAllBytes(new File(path).toPath() );
    }

    private static int[] setupMapFast(){
        return new int[128];
    }

    private static Reader setupReaderOriginal(String path) throws IOException{
        return new FileReader(path);
    }

    private static Map<Integer,Long> setupMapOriginal(){
        return new HashMap<>();
    } 

    private static void tallyCharsOriginal(Reader reader, Map<Integer, Long> freq) throws IOException {
        int b;
        while ((b = reader.read()) != -1) {
            try {
                freq.put(b, freq.get(b) + 1);
            } catch (NullPointerException np) {
                freq.put(b, 1L);
            };
        }
    }


    private static void printTallyV1(Map<Integer, Long> freq){
        for (int name: freq.keySet()){
            char key = (char)name;
            String value = freq.get(name).toString();  
            System.out.println(key + " " + value);  
        } 
    }

    private static void printTallyV2(int[] freq){
        for (int i = 0; i < freq.length; i++) {
            if(freq[i]!=0){
                System.out.println((char)i+ " " + freq[i]);
            }
        }
    }

    public static double Mark5TallyOriginal(Map<Integer, Long> freq) throws IOException {

        int n = 10, count = 1, totalCount = 0;
        double dummy = 0.0, runningTime = 0.0, st = 0.0, sst = 0.0;
        do {
            count *= 2;
            st = sst = 0.0;
            for (int j = 0; j < n; j++) {
                Timer t = new Timer();
                for (int i = 0; i < count; i++) {
                    freq = setupMapOriginal();
                    tallyCharsOriginal(setupReaderOriginal("./src/main/ress/FoundationSeries.txt"), freq);
                }
                runningTime = t.check();
                double time = runningTime * 1e9 / count;
                st += time;
                sst += time * time;
                totalCount += count;
            }
            double mean = st / n, sdev = Math.sqrt((sst - mean * mean * n) / (n - 1));
            System.out.printf("%6.1f ms +/- %8.2f %2d%n", (mean/1_000_000.0), (sdev/1_000_000.0), count);
        } while (runningTime < 0.25 && count < Integer.MAX_VALUE / 2);
        printTallyV1(freq);
        return dummy / totalCount;
    }

    public static double Mark5TallyFast(int[] freq) throws IOException {

        int n = 10, count = 1, totalCount = 0;
        double dummy = 0.0, runningTime = 0.0, st = 0.0, sst = 0.0;
        do {
            count *= 2;
            st = sst = 0.0;
            for (int j = 0; j < n; j++) {
                Timer t = new Timer();
                for (int i = 0; i < count; i++) {
                    freq = setupMapFast();
                    tallyCharsFast(setupReaderFast("./src/main/ress/FoundationSeries.txt"), freq);
                }
                runningTime = t.check();
                double time = runningTime * 1e9 / count;
                st += time;
                sst += time * time;
                totalCount += count;
            }
            double mean = st / n, sdev = Math.sqrt((sst - mean * mean * n) / (n - 1));
            System.out.printf("%6.1f ms +/- %8.2f %2d%n", (mean/1_000_000.0), (sdev/1_000_000.0), count);
        } while (runningTime < 0.25 && count < Integer.MAX_VALUE / 2);
        printTallyV2(freq);
        return dummy / totalCount;
    }



}
