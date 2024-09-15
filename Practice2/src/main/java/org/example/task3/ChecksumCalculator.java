package org.example.task3;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ChecksumCalculator {

    public static int calculateChecksum(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[2];  // Читаем по 2 байта за раз
            int checksum = 0;

            while (fis.read(buffer) != -1) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);

                // Преобразуем 2 байта в одно 16-битное число (short)
                int word = byteBuffer.getShort() & 0xFFFF; // & 0xFFFF для работы с беззнаковыми числами

                // Суммируем с учётом переполнения
                checksum += word;

                // Если возникло переполнение (больше чем 16 бит), добавляем перенос
                if ((checksum & 0xFFFF0000) != 0) {
                    checksum = (checksum & 0xFFFF) + 1;
                }
            }

            // Возвращаем инвертированное значение как контрольную сумму
            return ~checksum & 0xFFFF; // Инвертируем, чтобы получить контрольную сумму
        }
    }

    public static void main(String[] args) {
        try {
            File file = new File("test_file_100mb.bin");
            int checksum = calculateChecksum(file);
            System.out.printf("16-битная контрольная сумма: 0x%04X\n", checksum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

