package org.example.task3;

public class UserFriend {
    int userId;
    int friendId;

    public UserFriend(int userId, int friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

    @Override
    public String toString() {
        return "UserFriend{userId=" + userId + ", friendId=" + friendId + "}";
    }
}
