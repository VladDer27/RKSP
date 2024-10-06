package org.example.task2;

import io.reactivex.rxjava3.core.Observable;

import java.util.Random;

public class Task2_1_3 {
    public static void main(String[] args) throws InterruptedException {
        // Создаем поток случайного количества чисел от 0 до 1000
        Observable<Integer> randomNumbersStream = Observable.create(emitter -> {
            Random random = new Random();
            int count = random.nextInt(1001); // случайное количество чисел от 0 до 1000
            for (int i = 0; i < count; i++) {
                emitter.onNext(random.nextInt(100)); // генерируем случайные числа
            }
            emitter.onComplete();
        });

        // Преобразуем поток в поток, содержащий только количество чисел
        randomNumbersStream
                .count()
                .toObservable()
                .subscribe(count -> System.out.println("Количество чисел в потоке: " + count));
    }
}
