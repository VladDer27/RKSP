package org.example.task4;

import org.example.task3.ChecksumCalculator;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.*;

public class DirectoryWatcher {

    private static final Path DIRECTORY_TO_WATCH = Paths.get("D:\\whatislove\\RKSP\\Practice2\\testFolder");

    public static void main(String[] args) throws IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        DIRECTORY_TO_WATCH.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);

        System.out.println("Watching directory: " + DIRECTORY_TO_WATCH);

        while (true) {
            WatchKey key;
            try {
                key = watchService.take();  // Ожидание события
            } catch (InterruptedException ex) {
                return;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                Path fileName = (Path) event.context();
                Path filePath = DIRECTORY_TO_WATCH.resolve(fileName);

                boolean b = !(filePath.toString().endsWith("~") || filePath.toString().contains(".bak"));
                if (kind == ENTRY_CREATE) {
                    if (b) {
                        System.out.println("File created: " + fileName);
                    }
                } else if (kind == ENTRY_MODIFY) {
                    if (b) {
                        System.out.println("File modified: " + fileName);
                    }
                    handleFileModification(filePath);
                } else if (kind == ENTRY_DELETE) {
                    if (b) {
                        System.out.println("File deleted: " + fileName);
                    }
                    handleFileDeletion(filePath);
                }
            }

            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
    }

    // 2. Обработка изменения файла: вывод добавленных и удаленных строк
    private static void handleFileModification(Path filePath) throws IOException {
        // Игнорируем временные и резервные файлы
        if (filePath.toString().endsWith("~") || filePath.toString().contains(".bak")) {
            return;
        }

        Path tempBackupFile = Paths.get(filePath.toString() + ".bak");

        if (Files.exists(tempBackupFile)) {
            List<String> oldLines = Files.readAllLines(tempBackupFile);
            List<String> newLines = Files.readAllLines(filePath);

            List<String> addedLines = new ArrayList<>(newLines);
            addedLines.removeAll(oldLines);

            List<String> removedLines = new ArrayList<>(oldLines);
            removedLines.removeAll(newLines);

            System.out.println("Added lines: " + addedLines);
            System.out.println("Removed lines: " + removedLines);
        }

        // Обновляем или создаем бэкап файла, заменяя старый
        Files.copy(filePath, tempBackupFile, StandardCopyOption.REPLACE_EXISTING);
    }




    // 3. Обработка удаления файла: вывод размера и контрольной суммы
    private static void handleFileDeletion(Path filePath) throws IOException {
        Path backupFile = Paths.get(filePath.toString() + ".bak");

        if (Files.exists(backupFile)) {
            BasicFileAttributes attrs = Files.readAttributes(backupFile, BasicFileAttributes.class);
            long fileSize = attrs.size();

            // Вычисление контрольной суммы
            int checksum = ChecksumCalculator.calculateChecksum(backupFile.toFile());

            System.out.println("File size: " + fileSize + " bytes");
            System.out.printf("File checksum: 0x%04X\n", checksum);

            // Удаляем бэкап, если файл был удалён
            Files.delete(backupFile);
        }
    }
}

