package main.java;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WriteDataTest {

    static String pathToLogFile = "/home/duongvantien/Desktop/data_test_3.txt";

    public static void main(String[] args) throws Exception {
        WriteDataTest rdf = new WriteDataTest();
        rdf.writeData(pathToLogFile);
    }

    public void writeData(String pathToLogFile)
            throws IOException {
        long start, stop;
        start = System.nanoTime();
        Path path = Paths.get(pathToLogFile);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (int i = 10000000; i < (10000000+3600*24*30*12); ++i) {
                writer.write(i + " : 2009-09-22 16:47:08\n");
            }
        }
        stop = System.nanoTime();
        System.out.println("Time write data : " + (stop - start)/1000000 + "ms");
    }
}
