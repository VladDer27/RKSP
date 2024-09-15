package org.example.task2;

import java.io.File;
import java.io.IOException;

import static org.example.task2.CopyUsingApacheCommonsIO.copyUsingApacheCommonsIO;
import static org.example.task2.CopyUsingFileChannel.copyUsingFileChannel;
import static org.example.task2.CopyUsingFilesClass.copyUsingFilesClass;
import static org.example.task2.CopyUsingStreams.copyUsingStreams;

public class Main {
    public static void measureCopyMethod(Runnable copyMethod, String methodName) {
        System.gc(); // Вызов сборщика мусора для очистки памяти перед тестом
        long startTime = System.nanoTime();
        long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        copyMethod.run(); // Выполняем копирование

        long endTime = System.nanoTime();
        long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        long timeElapsed = endTime - startTime;
        long memoryUsed = endMemory - startMemory;

        System.out.println(methodName + " - Время: " + timeElapsed / 1_000_000 + " мс, Память: " + memoryUsed / 1024 + " КБ");
    }

    public static void main(String[] args) {
        File source = new File("test_file_100mb.bin");
        File dest = new File("output_file.bin");

        measureCopyMethod(() -> {
            try {
                copyUsingStreams(source, dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "FileInputStream/FileOutputStream");

        measureCopyMethod(() -> {
            try {
                copyUsingFileChannel(source, dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "FileChannel");

        measureCopyMethod(() -> {
            try {
                copyUsingApacheCommonsIO(source, dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "Apache Commons IO");

        measureCopyMethod(() -> {
            try {
                copyUsingFilesClass(source, dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "Files Class");

    }
}
