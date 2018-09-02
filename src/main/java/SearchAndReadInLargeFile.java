package main.java;

import java.io.*;

public class SearchAndReadInLargeFile {

    private static String bashType = "/bin/bash";
    private static String pathFileDataTest = "/home/duongvantien/Desktop/data_test_2.txt";

    // sizeOfRecordInBytes depend of size of one record
    // to get size of one record, we need to getBytes of them. Ex: "Data - 10000000".getBytes().length
    private static int sizeOfRecordInBytes = 38;

    private static File file;

    private SearchAndReadInLargeFile() throws IOException {
        file = new File(pathFileDataTest);
    }


    public static void main(String[] args) throws IOException {
        long start = System.nanoTime();
        SearchAndReadInLargeFile rlblnb = new SearchAndReadInLargeFile();

        // example a record : Data - 30000997 : 2009-09-22 16:47:08
        rlblnb.printResult("Data - 31999996 : 2009", "Data - 31999999 : 2009", pathFileDataTest);

        long stop = System.nanoTime();
        System.out.println("Total time : " + (stop - start) / 1000000 + "ms");
    }

    public void printResult(String fromPattern, String toPattern, String path) throws IOException {
        SearchAndReadInLargeFile rlblnb = new SearchAndReadInLargeFile();

        long fromLine = rlblnb.getLineByPattern(fromPattern, path);
        System.out.println("fromLine : " + fromLine);

        long toLine = rlblnb.getLineByPattern(toPattern, path);
        System.out.println("toLine : " + toLine);

        System.out.println("===========================");
        rlblnb.readData(fromLine, toLine);
    }

    private long getLineByPattern(String pattern, String path) throws IOException {
        Process process = Runtime.getRuntime().exec(new String[]{"grep", "-n", pattern, path});

        String result = null;
        Long lineNumber = null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while ((result = bufferedReader.readLine()) != null) {
            System.out.println("getLineByPattern : " + result);
            lineNumber = Long.parseLong(result.split(":")[0]);
        }
        return lineNumber;
    }

    private long setStartAtByte(long fromLine) {
        RandomAccessFile rand = null;

        // where to seek to
        long seekToByte = (fromLine == 1 ? 0 : ((fromLine - 1) * sizeOfRecordInBytes));

        // default byte start at is 0
        long startAtByte = 0;

        // seek to that position after know that where to seek to (using a RandomAccessFile)
        try {
            rand = new RandomAccessFile(file, "r");
            rand.seek(seekToByte);
            startAtByte = rand.getFilePointer();
        } catch (IOException e) {
            // do something
        } finally {
            try {
                rand.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return startAtByte;
    }

    private void readData(long fromLine, long toLine) {
        // new position start at
        long startAtByte = setStartAtByte(fromLine);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.skip(startAtByte);
            char[] record = new char[sizeOfRecordInBytes];
            while (fromLine <= toLine && (-1 != reader.read(record, 0, sizeOfRecordInBytes))) {
                System.out.println(new String(record));
                fromLine++;
            }
        } catch (Exception e) {
            // do something
        }
    }
}
