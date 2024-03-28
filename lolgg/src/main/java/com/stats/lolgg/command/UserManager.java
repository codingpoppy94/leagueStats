package com.stats.lolgg.command;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class UserManager {

    private final List<String> userList;

    public UserManager(){
        userList = new ArrayList<>();
    }

    public void addUser(String user) {
        userList.add(user);
    }

    public boolean removeUser(String user) {
        return userList.remove(user);
    }

    public List<String> getUserList() {
        return userList;
    }

    public void userMention(){
        
    }

    public String sendUserList() {
        StringBuilder userListString = new StringBuilder();
        for (int i = 0; i < userList.size(); i++) {
            userListString.append((i + 1) + ". " + userList.get(i) + "\t");
        }
        return userListString.toString();
    }
}