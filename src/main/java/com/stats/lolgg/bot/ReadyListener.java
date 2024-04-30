package com.stats.lolgg.bot;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stats.lolgg.command.LeagueManager;
import com.stats.lolgg.command.UserManager;
import com.stats.lolgg.model.LeagueStatsVO;
import com.stats.lolgg.model.LeagueVO;
import com.stats.lolgg.service.LeagueService;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Component
public class ReadyListener extends ListenerAdapter {
    private static Log log = LogFactory.getLog(ReadyListener.class); 

    @Autowired
    private UserManager userManager;

    @Autowired
    private LeagueManager leagueManager;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        MessageChannel channel = event.getChannel();
        
        // String message = event.getMessage().getContentRaw();
        String originMessage = event.getMessage().getContentRaw();
        String[] message = originMessage.split("\\s");

        String command = message[0];
        // String nickName = "";

        if(message[0].charAt(0) == '!'){
            
            // 테스트1
            if(message[0].equals("!ping")){
                // log.info("message get");
                event.getChannel().sendMessage("pong!").queue();
                return;
            }
    
            /* 
             * 체크
             */
            if(message[0].equals("!ㅊㅋ") || message[0].equals("!.")){
                // log.info("목록 추가");
                userManager.addUser(event.getMember());
                sendMessage(channel, userManager.sendUserList());
            }
    
            /* 
             * 취소
             */
            if (message[0].equals("!ㅊㅅ") || message[0].equals("!취소")) {
                // log.info("목록 삭제");

                if(message.length > 1){
                    // 범위취소
                    String memberIndex = message[1];
                    int[] selectMember = memberIndexparsing(memberIndex, channel);

                    if(memberIndex.length() > 1 && selectMember[0] != 0) {
                        // 다중
                        int start = selectMember[0];
                        int end = selectMember[1];
                        int count = end - start;

                        List<Member> userList = userManager.getUserList();

                        if(end +1 > userList.size()) {
                            sendErrorMessage(channel);
                            return;
                        }

                        for(int i=0; i<= count; i++){  
                            Member deleteMember = userList.get(start);
                            userManager.removeUser(deleteMember);
                        }
                        sendMessage(channel, userManager.sendUserList());
                        
                    } else {
                        if(selectMember[0] != 0){
                            int min = selectMember[0];
                            List<Member> userList = userManager.getUserList();
                            Member deleteMember = userList.get(min);
                            userManager.removeUser(deleteMember);
                            sendMessage(channel, userManager.sendUserList());
                        }
                    }
                    
                }  else {
                    Member user = event.getMember();
                    boolean removed = userManager.removeUser(user);
                    
                    if (removed) {
                        sendMessage(channel, userManager.sendUserList());
                        // channel.sendMessage("User " + user.getNickname() + " removed.\nUsers: \n" + userManager.sendUserList() ).queue();
                    } else {
                        sendMessage(channel, "not found in the list");
                        // channel.sendMessage("User " + user.getNickname() + " not found in the list.").queue();
                    }
                }
            }

            /* 
             * 팀 취소
             */

            if (message[0].equals("!ㅌㅊㅅ")) {
                VoiceChannel targetChannel = event.getGuild().getVoiceChannelById("347303677289037836");

                List<Member> targetMembers = targetChannel.getMembers();
                userManager.compareMembers(targetMembers);

                sendMessage(channel, userManager.sendUserList());
            }
    
            /* 
             * 목록 
             */
            if (message[0].equals("!ㄷㄱ") || message[0].equals("!대기")) {
                channel.sendMessage(userManager.sendUserList()).queue();
            }
    
    
            /* 
             * mention
             */
        
            if (message[0].equalsIgnoreCase("!ㅁㅅ")) {

                String memberIndex = message[1];

                int[] selectMember = memberIndexparsing(memberIndex,channel);

                // mentionMessage
                String mentionMessage = "";
                if(message.length > 2) {
                    mentionMessage = originMessage.substring(originMessage.indexOf(message[2]));
                } 

                if(memberIndex.length() > 1){
                    // 다중 선택 
                    int min = selectMember[0];
                    int max = selectMember[1];

                    List<Member> userList = userManager.getUserList();

                    if (userList.isEmpty()) {
                        sendMessage(channel, "대기자 확인");
                        return;
                    }
        
                    if (max+1 > userList.size()) {
                        sendMessage(channel, "대기 인원을 확인하세요.");
                        return;
                    }

                    StringBuilder messageBuilder = new StringBuilder();

                    for(int i=min; i<= max; i++){  
                        Member mentionMember = userList.get(i);
                        messageBuilder.append(mentionMember.getAsMention()).append(" ");

                    }
                    messageBuilder.append(mentionMessage);
                    sendMessage(channel, messageBuilder.toString());
              
                } else {
                    // 한명 선택
                    List<Member> userList = userManager.getUserList();
                    Member mentionMember = userList.get(selectMember[0]);
                    sendMessage(channel, mentionMember.getAsMention() + mentionMessage);
                }
            }
    
            /* 
             * get server Id
             * 
             */
            if (event.getMessage().getContentRaw().equals("!서버")) {
                // JDA jda = event.getJDA();
                Guild guild = event.getGuild();
                String servierId = guild.getId();
                log.info("serverId " + servierId);
                
                channel.sendMessage("서버 ID: " + servierId).queue();
            }

            /**
             * !전적
             */
            if (message[0].equalsIgnoreCase("!전적")) {
                
                String riotName = "";
                String templateMessage = "";
                // 남의꺼 호출
                if(message.length > 1) {
                    int commandIndex = originMessage.indexOf(message[1]);
                    riotName = originMessage.substring(commandIndex);

                    templateMessage = leagueManager.getRecord(riotName);
                } else {
                    // 자기꺼 호출
                    String nickName = event.getMember().getNickname();
                    int index = nickName.lastIndexOf("/");
                    riotName = nickName.substring(0, index);

                    templateMessage = leagueManager.getRecord(riotName);
                }
                if("".equals(templateMessage)) {
                    sendMessage(channel, "not found data");
                } else {
                    sendMessage(channel, templateMessage);
                }
            }

            /* !장인 */
            if (message[0].equalsIgnoreCase("!장인")) {
                String templateMessage = "";
                if(message.length > 1) {
                    String champName = message[1];
                    templateMessage = leagueManager.getChampMaster(champName);

                    if("".equals(templateMessage)){
                        sendMessage(channel, "not found data");
                    } else {
                        sendMessage(channel, templateMessage);
                    }
                }
            }

            /* !통계 */
            if (message[0].equalsIgnoreCase("!통계")) {
                String templateMessage = "";
                if(message.length < 2) {
                    templateMessage = leagueManager.getChampHighRate();
                    
                    if("".equals(templateMessage)){
                        sendMessage(channel, "not found data");
                    } else {
                        sendMessage(channel, templateMessage);
                    }
                }
            }

            /* !탈퇴 {riot_name} */
            if (message[0].equalsIgnoreCase("!탈퇴")){
                int result = userManager.withdrawUser(event, originMessage);
                if(result > 0){
                    sendMessage(channel, "탈퇴 처리 완료");
                }else {
                    sendMessage(channel, "권한 없음");
                }
            }

            /* !복귀 {riot_name} */
            if (message[0].equalsIgnoreCase("!복귀")){
                int result = userManager.recoverUser(event, originMessage);
                if(result > 0){
                    sendMessage(channel, "복귀 처리 완료");
                }else {
                    sendMessage(channel, "권한 없음");
                }
            }
        }
    }

    // 채널 메시지 보내기
    private void sendMessage(MessageChannel channel,String message){
        channel.sendMessage(message).queue();
    }

    // 공통 에러 메시지
    private void sendErrorMessage(MessageChannel channel) {
        channel.sendMessage("잘못된 명령어 사용").queue();
    }

    // 다중 memberIndex 처리
    private int[] memberIndexparsing(String memberIndex, MessageChannel channel){
        int[] result = new int[2];

        try{
            Pattern numberPattern = Pattern.compile("\\d");
            Matcher numberMatcher = numberPattern.matcher(memberIndex);
            if(numberMatcher.find()){
                if(memberIndex.length() > 1) {
                    int min = Integer.parseInt(numberMatcher.group(0)) - 1;
                    int max = Integer.parseInt(memberIndex.substring(2)) -1;
    
                    result[0] = min;
                    result[1] = max;
                
                } else {
                    result[0] = Integer.parseInt(memberIndex) - 1;
                }
            } else {
                sendErrorMessage(channel);
            }
        } catch (Exception e) {
            sendErrorMessage(channel);
        }

        return result;
    }

}