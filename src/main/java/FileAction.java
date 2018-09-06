package main.java;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.stream.Stream;

public class FileAction {
    private static String bashType = "/bin/bash";
    private static String pathFileDataTest = "/home/duong.van.tien/Desktop/BTC_JPY.csv";

    private static int sizeOfRecordInBytes = 58;

    private static File file;

    private FileAction() throws IOException {
        file = new File(pathFileDataTest);
    }


    public static void main(String[] args) throws IOException {
        long start = System.nanoTime();
        FileAction fileAction = new FileAction();

        long fileSize = fileAction.getFileSizeByBash(pathFileDataTest);
        int numberLine = fileAction.getNumberLineByFileSize(sizeOfRecordInBytes, fileSize);

        System.out.println("Number lines : " + numberLine);
        long s = System.nanoTime();
        int fromLine = fileAction.binarySearch(0, numberLine, "2018-08-21 11:18:54");
        System.out.println("time search : " + (System.nanoTime()-s)/1000000);

        System.out.println("From line : " + fromLine);

        DateHelper dateHelper = new DateHelper();
        Date fromDate = dateHelper.parseStringToDate("2021-08-18 11:18:54", dateHelper.dateFormat);
        Date toDate = dateHelper.parseStringToDate("2021-08-18 11:19:05", dateHelper.dateFormat);
        int toLine = fromLine + dateHelper.getSecondBetweenTwoDate(fromDate, toDate);

        System.out.println("To line : " + fromLine);

        fileAction.readData(fromLine, toLine);


        long stop = System.nanoTime();
        System.out.println("Total time : " + (stop - start) / 1000000 + "ms");
    }

    public void printResult(String fromPatter, String toPattern) throws IOException {
        FileAction fileAction = new FileAction();

        long fromLine = fileAction.getLineByPattern(fromPatter);
        System.out.println("fromLine : " + fromLine);

        long toLine = fileAction.getLineByPattern(toPattern);
        System.out.println("toLine : " + toLine);

        System.out.println("===========================");
        fileAction.readData(fromLine, toLine);
    }

    public String readRecordByLineNumberByJava(Integer lineNumber, String path) throws IOException {
        long timeStart, timeStop;
        timeStart = System.nanoTime();

        String result = null;
        try (Stream<String> lines = Files.lines(Paths.get(path))) {
            result = lines.skip(lineNumber).findFirst().get();
        }

        timeStop = System.nanoTime();
        System.out.println("Time for read record by line number : " + (timeStop - timeStart) / 1000000 + "ms");
        return result;
    }

    public int getNumberLineOfFileByJava(String path) throws IOException {
        long timeStart, timeStop;
        timeStart = System.nanoTime();

        try (Stream<String> lines = (Stream<String>) Files.lines(Paths.get(path))) {
            timeStop = System.nanoTime();
            System.out.println("Time to get number line of file by Java code : " + (timeStop - timeStart) / 1000000 + "ms");
            return (int) lines.count();
        }
    }

    public void execBash(String bashType, String path, String fromDate, String toDate) throws IOException {
        String[] cmdScript = new String[]{bashType, path, fromDate, toDate};
        ProcessBuilder pb = new ProcessBuilder(cmdScript);
        pb.start();
    }

    private long getLineByPattern(String pattern) throws IOException {
        Process process = Runtime.getRuntime().exec(new String[]{"grep", "-n", pattern, pathFileDataTest});

        String result = null;
        Long lineNumber = null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while ((result = bufferedReader.readLine()) != null) {
            System.out.println(result);
            lineNumber = Long.parseLong(result.split(":")[0]);
        }
        return lineNumber;
    }

    private String getContentByPattern(String pattern, String path) throws IOException {
        Process process = Runtime.getRuntime().exec(new String[]{"grep", "-n", pattern, path});

        String result = null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while ((result = bufferedReader.readLine()) != null) {
            System.out.println(result);
            result = result.split(":")[1];
        }
        return result;
    }

    private String getContentByLineNumber(int lineNumber, String path) throws IOException {
        Process process = Runtime.getRuntime().exec(new String[]{"sed", lineNumber + "!d", path});

        String result = null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while ((result = bufferedReader.readLine()) != null) {
            return result;
        }
        return result;
    }

    private int getNumberLineOfFileByBash(String path) throws IOException {
        Process process = Runtime.getRuntime().exec(new String[]{"wc", "-l", path});

        String result = null;
        int numberLine = 0;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while ((result = bufferedReader.readLine()) != null) {
            System.out.println(result);
            numberLine = (int) Long.parseLong(result.split(" ")[0]);
        }
        return numberLine;
    }

    private int getNumberLineByFileSize(int sizeOfOneRecord, long fileSize) {
        return (int) (fileSize/sizeOfOneRecord);
    }

    private long getFileSizeByBash(String path) throws IOException {
        Process process = Runtime.getRuntime().exec("ls -l " + path);

        String result = null;
        long fileSize = 0;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while ((result = bufferedReader.readLine()) != null) {
            fileSize = Long.parseLong(result.split(" ")[4]);
        }
        return fileSize;
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

    private String getDataByLineNumber(long lineNumber) {
        long startAtByte = setStartAtByte(lineNumber);
        String result = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.skip(startAtByte);
            char[] record = new char[sizeOfRecordInBytes];
            reader.read(record, 0, sizeOfRecordInBytes);
            result = new String(record);
        } catch (Exception e) {
            // do something
        }
        return result;
    }

    int binarySearch(int fromLine, int toLine, String key) throws IOException {
        if (fromLine <= toLine) {
            int mid = fromLine + (toLine - fromLine) / 2;

            // String lineOfMidle = getContentByLineNumber(mid, pathFileDataTest);
            String lineOfMidle = getDataByLineNumber((int)mid);

            if (lineOfMidle.split(",")[0].compareTo(key) == 0)
                return mid;

            if (lineOfMidle.compareToIgnoreCase(key) > 0)
                return binarySearch(fromLine, mid - 1, key);

            return binarySearch(mid + 1, toLine, key);
        }

        return -1;
    }
}
