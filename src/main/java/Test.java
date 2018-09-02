package main.java;

public class Test {
    public static void main(String[] args) {
        if ("2009-09-22 16:47:08".compareToIgnoreCase("2009-09-22 16:47:09") < 0) {
            System.out.println("A" + "2009-09-22 16:47:08".compareToIgnoreCase("2009-09-22 16:47:09"));
        } else if ("2009-09-22 16:47:08".compareToIgnoreCase("2009-09-22 16:47:08") == 0) {
            System.out.println("B" + "2009-09-22 16:47:08".compareToIgnoreCase("2009-09-22 16:47:09"));
        } else {
            System.out.println("C" + "2009-09-22 16:47:08".compareToIgnoreCase("2009-09-22 16:47:09"));
        }
    }
}
