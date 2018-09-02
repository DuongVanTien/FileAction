package main.java;

import java.io.*;

public class ReadFromLineToLineInLargeFile {
    public static void main(String[] args) {

        // sizeOfRecordInBytes depend of size of one record
        // to get size of one record, we need to getBytes of them. Ex: "Data - 10000000".getBytes().length
        int sizeOfRecordInBytes = 20;

        int fromLine = 5;
        int toLine = 25;

        File myfile = new File("/home/duongvantien/Desktop/data_test.txt");

        // where to seek to
        long seekToByte =  (fromLine == 1 ? 0 : ((fromLine-1) * sizeOfRecordInBytes));

        // default is 0
        long startAtByte = 0;

        // seek to that position after know that where to seek to (using a RandomAccessFile)
        try {
            RandomAccessFile rand = new RandomAccessFile(myfile,"r");
            rand.seek(seekToByte);
            startAtByte = rand.getFilePointer();
            rand.close();

        } catch(IOException e) {
            // do something
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(myfile));
            reader.skip(startAtByte);

            char[] buffer = new char[sizeOfRecordInBytes];
            while(fromLine < toLine && (-1 != reader.read(buffer, 0, sizeOfRecordInBytes))) {
                System.out.println(new String(buffer));
                fromLine++;
            }
        } catch(Exception e) {
            // do something
        } finally {
            if (reader != null) {
                try {reader.close();} catch(Exception ignore) {}
            }
        }
    }
}
