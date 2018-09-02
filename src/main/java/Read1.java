package main.java;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Date;

public class Read1 {
    public static void main(String[] args) throws IOException {
//        if (args.length != 2) {
//            throw new IllegalArgumentException();
//        }
        String grepfor = "2009-09-22 16:47:08.128";
        Path path = Paths.get("/home/duongvantien/Desktop/test.txt");

        String report = searchFor(grepfor, path);

        System.out.println(report);

    }


    private static final int MAPSIZE = 4 * 1024 ; // 4K - make this * 1024 to 4MB in a real system.

    private static String searchFor(String grepfor, Path path) throws IOException {
        System.out.println("start : " + new Date());
        long start = System.nanoTime();

        final byte[] stringSearch = grepfor.getBytes(StandardCharsets.UTF_8);
        System.out.println("pattern search : " + Arrays.toString(stringSearch));
        System.out.println("pattern search length : " + stringSearch.length);

        StringBuilder report = new StringBuilder();
        int padding = 1; // need to scan 1 character ahead in case it is a word boundary.
        int linecount = 0;
        int matches = 0;
        boolean inword = false;
        boolean scantolineend = false;

        try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
            final long lengthFile = channel.size();
            System.out.println("length file : " + lengthFile);

            int pos = 0;
            while (pos < lengthFile) {
                long remaining = lengthFile - pos;
                // int conversion is safe because of a safe MAPSIZE.. Assume a reaosnably sized stringSearch.
                int trymap = MAPSIZE + stringSearch.length + padding;
                int tomap = (int)Math.min(trymap, remaining);
                // different limits depending on whether we are the last mapped segment.
                int limit = trymap == tomap ? MAPSIZE : (tomap - stringSearch.length);
                MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, pos, tomap);
//                System.out.println("Mapped from " + pos + " for " + tomap);
                pos += (trymap == tomap) ? MAPSIZE : tomap;
                for (int i = 0; i < limit; i++) {
                    final byte b = buffer.get(i);
                    if (scantolineend) {
                        if (b == '\n') {
                            scantolineend = false;
                            inword = false;
                            linecount ++;
                        }
                    } else if (b == '\n') {
                        linecount++;
                        inword = false;
                    } else if (b == '\r' || b == ' ') {
                        inword = false;
                    } else if (!inword) {
                        if (wordMatch(buffer, i, tomap, stringSearch)) {
                            matches++;
                            i += stringSearch.length - 1;
                            if (report.length() > 0) {
                                report.append(", ");
                            }
                            report.append(linecount);
                            scantolineend = true;
                        } else {
                            inword = true;
                        }
                    }
                }
            }
        }

        System.out.println("end : " + new Date());
        long stop = System.nanoTime();
        System.out.println("time : "+(stop-start)/1000000);
        return "Matches : " + matches + "\nWord found at : " + report;
    }

    private static boolean wordMatch(MappedByteBuffer buffer, int pos, int tomap, byte[] tosearch) {
        //assume at valid word start.
        for (int i = 0; i < tosearch.length; i++) {
            if (tosearch[i] != buffer.get(pos + i)) {
                return false;
            }
        }
        byte nxt = (pos + tosearch.length) == tomap ? (byte)' ' : buffer.get(pos + tosearch.length);
        return nxt == ' ' || nxt == '\n' || nxt == '\r';
    }
}
