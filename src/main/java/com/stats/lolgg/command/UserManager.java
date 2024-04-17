package com.stats.lolgg.command;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Member;

@Component
public class UserManager {

    private final List<Member> userList;

    public UserManager(){
        userList = new ArrayList<>();
    }

    public void addUser(Member user) {
        userList.add(user);
    }

    public boolean removeUser(Member user) {
        return userList.remove(user);
    }

    public List<Member> getUserList() {
        return userList;
    }

    public String getUserId(Member user){
        return user.getNickname();
    }

    public void compareMembers(List<Member> targetMembers){
        List<Member> memberToRemove = new ArrayList<>();

        for(Member member : targetMembers) {
            if(userList.contains(member)){
                memberToRemove.add(member);
            }
        }
        userList.removeAll(memberToRemove);
    }

    public void userMention(){
        
    }

    public String sendUserList() {
        StringBuilder userListString = new StringBuilder();
        userListString.append("```대기자: ");
        for (int i = 0; i < userList.size(); i++) {
            userListString.append((i + 1) + "." + userList.get(i).getNickname() + " ");
        }
        userListString.append("```");
        return userListString.toString();
    }
}