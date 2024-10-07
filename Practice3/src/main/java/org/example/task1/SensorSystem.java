package org.example.task1;


import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SensorSystem {
    private static final int TEMP_NORMAL = 25;
    private static final int CO2_NORMAL = 70;

    public static void main(String[] args) throws InterruptedException {

        Observable<Integer> temperatureSensor = Observable.interval(1, TimeUnit.SECONDS)
                .map(tick -> new Random().nextInt(16) + 15)  // Генерация случайной температуры от 15 до 30
                .subscribeOn(Schedulers.io());

        Observable<Integer> co2Sensor = Observable.interval(1, TimeUnit.SECONDS)
                .map(tick -> new Random().nextInt(71) + 30)  // Генерация случайного CO2 от 30 до 100
                .subscribeOn(Schedulers.io());


        Observable.combineLatest(temperatureSensor, co2Sensor, (temperature, co2) -> {
            // Собираем данные от датчиков
            return new SensorData(temperature, co2);
        }).subscribe(new Observer<SensorData>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("Сигнализация активирована...");
            }

            @Override
            public void onNext(SensorData data) {
                if (data.temperature > TEMP_NORMAL && data.co2 > CO2_NORMAL) {
                    System.out.println("ALARM!!! Температура: " + data.temperature + "°C, CO2: " + data.co2 + "ppm");
                } else if (data.temperature > TEMP_NORMAL) {
                    System.out.println("Предупреждение: Температура превышена! Температура: " + data.temperature + "°C");
                } else if (data.co2 > CO2_NORMAL) {
                    System.out.println("Предупреждение: CO2 превышен! CO2: " + data.co2 + "ppm");
                } else {
                    System.out.println("Показатели в норме. Температура: " + data.temperature + "°C, CO2: " + data.co2 + "ppm");
                }
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("Ошибка: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("Сигнализация отключена.");
            }
        });

        // Для работы программы в течение 30 секунд
        Thread.sleep(30000);
    }
}