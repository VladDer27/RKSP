package org.example.task2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class CreateTestFile {
    public static void main(String[] args) throws IOException {
        try (FileOutputStream fos = new FileOutputStream("test_file_100mb.bin")) {
            byte[] buffer = new byte[1024]; // 1 KB буфер
            Random random = new Random();
            for (int i = 0; i < 1024 * 100; i++) { // 100 MB файла
                random.nextBytes(buffer);
                fos.write(buffer);
            }
        }
        System.out.println("Test file created.");
    }
}

