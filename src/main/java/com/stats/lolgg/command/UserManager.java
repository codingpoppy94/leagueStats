package com.stats.lolgg.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stats.lolgg.service.LeagueService;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@Component
public class UserManager {

    @Autowired
    private LeagueService leagueService;

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

    // 회원탈퇴 !탈퇴 {riot_name}
    public int withdrawUser(MessageReceivedEvent event, String originMessage){
        String[] message = originMessage.split("\\s");
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("delete_yn", "Y");
        paramMap.put("riot_name", message[1]);

        List<Role> roles = event.getMember().getRoles();
        for (Role role : roles) {
            // System.out.println("Role: " + role.getName());
            if(role.getPermissions().contains(Permission.ADMINISTRATOR)){
                return leagueService.changeDeleteYN(paramMap);
            }
        }
        return 0;
    }

    // 회원복귀 !복귀 {riot_name}
    public int recoverUser(MessageReceivedEvent event, String originMessage){
        String[] message = originMessage.split("\\s");
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("delete_yn", "N");
        paramMap.put("riot_name", message[1]);

        List<Role> roles = event.getMember().getRoles();
        for (Role role : roles) {
            // System.out.println(role.getPermissions());
            if(role.getPermissions().contains(Permission.ADMINISTRATOR)){
                return leagueService.changeDeleteYN(paramMap);
            }
        }
        return 0;
    }
}