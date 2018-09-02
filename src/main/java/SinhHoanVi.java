package main.java;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class SinhHoanVi {
    private Integer i, n, a[];
    String pathToFile = "";
    BufferedWriter writer;

    public SinhHoanVi() throws IOException {
        pathToFile = "/home/duongvantien/Desktop/hoanvi.txt";
        Path path = Paths.get(pathToFile);
        writer = Files.newBufferedWriter(Paths.get(String.valueOf(path)));
    }

    public void Init() {
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.print("Nhập vào số phần tử cần hoán vị:");
            n = scanner.nextInt();
        } while (n <= 0);

        a = new Integer[n + 1];
        for (int j = 1; j <= n; j++) {
            a[j] = j;
        }
    }

    public void Result() throws IOException {
        for (int j = 1; j <= n; j++) {
            System.out.print(a[j]);
            Files.write(Paths.get(pathToFile), a[j].toString().getBytes(), StandardOpenOption.APPEND);
        }
        Files.write(Paths.get(pathToFile), "\n".getBytes(), StandardOpenOption.APPEND);
        System.out.println();
    }

    public void sinhHoanVi() throws IOException {
        do {
            Result();
            i = n - 1;
            while (i > 0 && a[i] > a[i + 1]) --i;
            if (i > 0) {
                int k = n;
                while (a[k] < a[i]) --k; //lùi dần từ cuối dãy để tìm phân tử đầu tiên lớn hơn x[i]
                //đổi chỗ sau khi tìm thấy
                int temp = a[k];
                a[k] = a[i];
                a[i] = temp;
                //Lật ngược đoạn cuối cùng
                k = n;
                for (int j = i + 1; j < k; j++, k--) {
                    temp = a[j];
                    a[j] = a[k];
                    a[k] = temp;
                }
            }
        } while (i != 0);
    }

    public static void main(String[] args) throws IOException {
        SinhHoanVi hoanvi = new SinhHoanVi();
        hoanvi.Init();
        hoanvi.sinhHoanVi();
        System.gc();
    }
}
