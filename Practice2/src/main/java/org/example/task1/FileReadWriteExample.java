package org.example.task1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileReadWriteExample {
    public static void main(String[] args) {


        Path filePath = Paths.get("example.txt");

        List<String> linesToWrite = List.of(
                "Строка 1: Пример текста.",
                "Строка 2: Вторая строка.",
                "Строка 3: Заключительная строка."
        );

        try {

            Files.write(filePath, linesToWrite);
            System.out.println("Текст успешно записан в файл.");

            List<String> linesRead = Files.readAllLines(filePath);
            System.out.println("Чтение из файла:");

            for (String line : linesRead) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Произошла ошибка при работе с файлом: " + e.getMessage());
        }
    }
}

