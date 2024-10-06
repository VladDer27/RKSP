package org.example.task4;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FileProcessingSystem {
    private static final Random random = new Random();
    private static final String[] fileTypes = {"XML", "JSON", "XLS"};

    public static void main(String[] args) throws InterruptedException {
        // Генератор файлов
        Flowable<File> fileGenerator = Flowable.<File>create(emitter -> {
                    while (!emitter.isCancelled()) {
                        String fileType = fileTypes[random.nextInt(fileTypes.length)];
                        int fileSize = 10 + random.nextInt(91); // размер от 10 до 100
                        File file = new File(fileType, fileSize);

                        // Эмитируем файл с задержкой от 100 до 1000 мс
                        int delay = 100 + random.nextInt(901);
                        TimeUnit.MILLISECONDS.sleep(delay);
                        emitter.onNext(file);
                    }
                }, io.reactivex.rxjava3.core.BackpressureStrategy.BUFFER) // Устанавливаем стратегию обработки нагрузки
                .subscribeOn(Schedulers.io());

        // Ограничиваем очередь до 5 файлов
        Flowable<File> fileQueue = fileGenerator
                .onBackpressureBuffer(5) // ограничиваем до 5 элементов в буфере
                .observeOn(Schedulers.io());

        // Обработчики файлов
        createFileProcessor("XML", fileQueue).subscribe();
        createFileProcessor("JSON", fileQueue).subscribe();
        createFileProcessor("XLS", fileQueue).subscribe();

        // Удерживаем основную нить, чтобы приложение продолжало работать
        Thread.sleep(10000); // Время выполнения программы
    }

    // Функция создания обработчика файлов для определенного типа
    private static Flowable<File> createFileProcessor(String fileType, Flowable<File> fileQueue) {
        return fileQueue
                .filter(file -> file.type.equals(fileType)) // фильтруем по типу
                .doOnNext(file -> {
                    System.out.println("Обрабатывается файл: " + file + " обработчиком типа " + fileType);
                    try {
                        TimeUnit.MILLISECONDS.sleep(file.size * 7); // время обработки файла
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Файл обработан: " + file + " обработчиком типа " + fileType);
                });
    }
}