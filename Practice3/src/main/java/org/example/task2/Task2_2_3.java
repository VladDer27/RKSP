package org.example.task2;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.Random;


public class Task2_2_3 {
    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();

        // Создаем первый поток из 1000 случайных чисел
        Observable<Integer> firstStream = Observable.range(0, 1000)
                .map(i -> random.nextInt(10))
                .subscribeOn(Schedulers.io());

        // Создаем второй поток из 1000 случайных чисел
        Observable<Integer> secondStream = Observable.range(0, 1000)
                .map(i -> random.nextInt(10))
                .subscribeOn(Schedulers.io());

        // Смешиваем два потока
        Observable.zip(firstStream, secondStream, (first, second) -> new int[]{first, second})
                .flatMap(arr -> Observable.just(arr[0], arr[1]))
                .subscribe(System.out::println);

        Thread.sleep(3000); // Ждем завершения потоков
    }
}

