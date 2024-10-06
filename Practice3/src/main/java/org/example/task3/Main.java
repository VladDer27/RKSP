package org.example.task3;

import io.reactivex.rxjava3.core.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static final Random random = new Random();
    private static final List<UserFriend> userFriendsList = new ArrayList<>();

    public static void main(String[] args) {
        // 1. Заполняем массив объектов UserFriend случайными данными
        populateUserFriends(10); // Например, 10 объектов

        // 2. Массив случайных userId
        Integer[] randomUserIds = generateRandomUserIds(5); // Массив из 5 случайных userId

        // 3. Создаем поток из массива случайных userId
        Observable<Integer> userIdStream = Observable.fromArray(randomUserIds);

        // 4. Преобразуем поток userId в поток объектов UserFriend через getFriends
        userIdStream
                .flatMap(Main::getFriends) // Получаем друзей для каждого userId
                .subscribe(userFriend -> System.out.println("Найден друг: " + userFriend));
    }

    // Функция для генерации случайного массива userId
    private static Integer[] generateRandomUserIds(int count) {
        Integer[] userIds = new Integer[count];
        for (int i = 0; i < count; i++) {
            userIds[i] = random.nextInt(10); // userId от 0 до 9 (например)
        }
        return userIds;
    }

    // Функция для заполнения массива UserFriend случайными данными
    private static void populateUserFriends(int count) {
        for (int i = 0; i < count; i++) {
            int userId = random.nextInt(10); // userId от 0 до 9
            int friendId = random.nextInt(10); // friendId от 0 до 9
            userFriendsList.add(new UserFriend(userId, friendId));
        }
    }

    // Функция, возвращающая поток UserFriend по заданному userId
    public static Observable<UserFriend> getFriends(int userId) {
        // Отфильтровываем друзей по userId и возвращаем поток
        return Observable.fromIterable(userFriendsList)
                .filter(userFriend -> userFriend.userId == userId);
    }
}
