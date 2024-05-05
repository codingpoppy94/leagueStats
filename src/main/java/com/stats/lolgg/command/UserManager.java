package com.stats.lolgg.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.stats.lolgg.service.LeagueService;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * 내전 등록 취소 Manager
 * @author codingpoppy94
 * @version 1.0
 */

@Component
public class UserManager {

    @Value("${team.channelId.one}")
    private String teamIdOne;

    @Value("${team.channelId.two}")
    private String teamIdTwo;

    @Value("${team.channelId.three}")
    private String teamIdThree;

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

    public void compareMembers(List<Member> targetMembers){
        List<Member> memberToRemove = new ArrayList<>();

        for(Member member : targetMembers) {
            if(userList.contains(member)){
                memberToRemove.add(member);
            }
        }
        userList.removeAll(memberToRemove);
    }

    /* 취소 */
    public String cancelUser(MessageReceivedEvent event, String originMessage){
        String[] message = originMessage.split("\\s");

        if(message.length > 1){
            // 범위취소
            String memberIndex = message[1];
            int[] selectMember = memberIndexparsing(memberIndex);
            if(selectMember[0] == 9999) {
                return "error";
            }

            if(memberIndex.length() > 1) {
                // 다중
                int start = selectMember[0];
                int end = selectMember[1];
                int count = end - start;

                if(end +1 > this.userList.size()) {
                    return "error";
                }

                for(int i=0; i<= count; i++){  
                    Member deleteMember = this.userList.get(start);
                    this.removeUser(deleteMember);
                }
                return this.sendUserList();
                
            } else {
                if(selectMember[0] >= 0){
                    int min = selectMember[0];
                    if(min + 1 <= this.userList.size()){
                        Member deleteMember = this.userList.get(min);
                        this.removeUser(deleteMember);
                        return this.sendUserList();
                    }
                }
            }
            
        }  else {
            Member user = event.getMember();
            boolean removed = this.removeUser(user);
            
            if (removed) {
                return this.sendUserList();
            } else {
                return "not found in the list";
            }
        }
        return "error";
    }

    /* 팀취소 */
    public String teamCancel(MessageReceivedEvent event, String originMessage){
        String[] message = originMessage.split("\\s");
        int messageIndex =  Integer.parseInt(message[1]);
        String channelId = "";
        if(messageIndex == 1){
            channelId = teamIdOne;
        }else if(messageIndex == 2){
            channelId = teamIdTwo;
        }else if(messageIndex == 3){
            channelId = teamIdThree;
        }
        System.out.println(channelId);
        VoiceChannel targetChannel = event.getGuild().getVoiceChannelById(channelId);
        if(targetChannel == null){
            return "error";
        }
        List<Member> targetMembers = targetChannel.getMembers();
        this.compareMembers(targetMembers);
        return this.sendUserList();
    }

    /* mention */
    public String userMention(String originMessage){
        String[] message = originMessage.split("\\s");
        String memberIndex = message[1];
        int[] selectMember = memberIndexparsing(memberIndex);
        if(selectMember[0] == 9999) {
            return "error";
        }

        // mentionMessage
        String mentionMessage = "";
        if(message.length > 2) {
            mentionMessage = originMessage.substring(originMessage.indexOf(message[2]));
        } 

        if(memberIndex.length() > 1){
            // 다중 선택 
            int min = selectMember[0];
            int max = selectMember[1];

            if (this.userList.isEmpty()) {
                return "대기자 확인";
            }

            if (max+1 > this.userList.size()) {
                return "대기 인원을 확인하세요.";
            }

            StringBuilder messageBuilder = new StringBuilder();

            for(int i=min; i<= max; i++){  
                Member mentionMember = userList.get(i);
                messageBuilder.append(mentionMember.getAsMention()).append(" ");

            }
            messageBuilder.append(mentionMessage);
            return messageBuilder.toString();
        } else {
            int mentionCount = selectMember[0];
            if (this.userList.size() < mentionCount+1){
                return "error";
            }
            // 한명 선택
            Member mentionMember = this.userList.get(mentionCount);
            return mentionMember.getAsMention() + mentionMessage;
        }

    }

    public String sendUserList() {
        StringBuilder userListString = new StringBuilder();
        userListString.append("```대기자: ");
        for (int i = 0; i < userList.size(); i++) {
            String riotName = userList.get(i).getNickname();
            int index = riotName.indexOf("/");
            riotName = riotName.substring(0, index);
            userListString.append((i + 1) + "." + riotName + " ");
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

        //권한체크
        List<Role> roles = Objects.requireNonNull(event.getMember()).getRoles();
        if(checkAuth(roles)){
            return leagueService.changeDeleteYN(paramMap);
        }
        return 0;
    }

    // 회원복귀 !복귀 {riot_name}
    public int recoverUser(MessageReceivedEvent event, String originMessage){
        String[] message = originMessage.split("\\s");
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("delete_yn", "N");
        paramMap.put("riot_name", message[1]);

        //권한체크
        List<Role> roles = Objects.requireNonNull(event.getMember()).getRoles();
        if(checkAuth(roles)){
            return leagueService.changeDeleteYN(paramMap);
        }
        return 0;
    }

    // !부캐저장
    public String saveSubInfo(MessageReceivedEvent event,String originMessage){
        String[] message = originMessage.split("\\s");
        if(message.length > 1) {
            String[] names = message[1].split("/");
            Map<String,Object> paramMap = new HashMap<>();
            String subName = names[0];
            String mainName = names[1];
            paramMap.put("sub_name", subName);
            paramMap.put("main_name", mainName);

            //권한체크
            List<Role> roles = Objects.requireNonNull(event.getMember()).getRoles();
            if(checkAuth(roles)){
                leagueService.saveMappingName(paramMap);
                leagueService.changeRiotName(paramMap);
                return "등록 및 변경 완료";
            } else {
                return "권한 없음";
            }
        }
        return "error";
    }
    

    // 다중 memberIndex 처리
    private int[] memberIndexparsing(String memberIndex){
        int[] result = new int[2];

        try{
            String mulitReg = "\\d{1,2}~\\d{1,2}";
            String numberReg = "\\d{1,2}";
            if(memberIndex.matches(numberReg)){
                result[0] = Integer.parseInt(memberIndex) - 1;
                return result;
            }
            if(memberIndex.matches(mulitReg)){
                String[] indexes = memberIndex.split("~");
                int min = Integer.parseInt(indexes[0]) - 1;
                int max = Integer.parseInt(indexes[1]) -1;

                result[0] = min;
                result[1] = max;
                return result;
            }
            result[0] = 9999;
            return result;
        } catch (Exception e) {
            result[0] = 9999;
            return result;
        }
    }

    private boolean checkAuth(List<Role> roles){
        for (Role role : roles) {
            if("난민디코관리자".equals(role.getName())){
                if(role.getPermissions().contains(Permission.ADMINISTRATOR)){
                    return true;
                } 
            }
        }
        return false;
    }
}