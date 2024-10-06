package org.example.task2;

import io.reactivex.rxjava3.core.Observable;

import java.util.Random;

public class Task2_3_3 {
    public static void main(String[] args) {
        Random random = new Random();
        // Создаем поток случайного количества чисел
        Observable<Integer> randomNumbersStream = Observable.create(emitter -> {
            int count = random.nextInt(1001); // случайное количество чисел от 0 до 1000
            for (int i = 0; i < count; i++) {
                emitter.onNext(random.nextInt(100)); // генерируем случайные числа
            }
            emitter.onComplete();
        });

        // Извлекаем только последнее значение из потока
        randomNumbersStream
                .lastElement()
                .subscribe(last -> System.out.println("Последнее число в потоке: " + last),
                        Throwable::printStackTrace);
    }
}

