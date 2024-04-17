package com.stats.lolgg.bot;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stats.lolgg.command.UserManager;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Member;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Component
public class ReadyListener extends ListenerAdapter {
    private static Log log = LogFactory.getLog(ReadyListener.class); 

    @Autowired
    private UserManager userManager;
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        MessageChannel channel = event.getChannel();
        
        // String message = event.getMessage().getContentRaw();
        String originMessage = event.getMessage().getContentRaw();
        String[] message = originMessage.split("\\s");

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
                sendMessage(channel, "대기: " + userManager.sendUserList());
            }
    
            /* 
             * 취소
             */
            if (message[0].equals("!ㅊㅅ") || message[0].equals("!취소")) {
                // log.info("목록 삭제");

                if(message.length > 1){
                    // 범위취소

                    
                } else if(message.length == 1) {
                    // 단독
                } else {

                }
                Member user = event.getMember();
                boolean removed = userManager.removeUser(user);
                
                if (removed) {
                    sendMessage(channel, "대기: "+ userManager.sendUserList());
                    // channel.sendMessage("User " + user.getNickname() + " removed.\nUsers: \n" + userManager.sendUserList() ).queue();
                } else {
                    sendMessage(channel, "not found in the list");
                    // channel.sendMessage("User " + user.getNickname() + " not found in the list.").queue();
                }
            }
    
            /* 
             * 목록 
             */
            if (message[0].equals("!ㄷㄱ")) {
                log.info("목록 보기");
                
                channel.sendMessage("Users: \n" + userManager.sendUserList()).queue();
            }
    
    
            /* 
             * mention
             */
        
            if (message[0].equalsIgnoreCase("!ㅁㅅ")) {
                String mention = message[1];
                Pattern numberPattern = Pattern.compile("\\d");
                Matcher numberMatcher = numberPattern.matcher(mention);

                // Pattern spacePattern = Pattern.compile("\s");
                // Matcher spaceMacher = spacePattern.matcher(originMessage);
                String mentionMessage = "";
                if(message.length > 2) {
                    mentionMessage = originMessage.substring(originMessage.indexOf(message[2]));
                } 

                if(mention.length() > 1){
                    // 다중 선택 
                    if(numberMatcher.find()){
                        int min = Integer.parseInt(numberMatcher.group(0)) - 1;
                        int max = Integer.parseInt(mention.substring(2)) -1;

                        log.info(mentionMessage);
                        List<Member> userList = userManager.getUserList();

                        if (userList.isEmpty()) {
                            event.getChannel().sendMessage("멤버 리스트가 비어있습니다.").queue();
                            return;
                        }
            
                        if (max+1 > userList.size()) {
                            event.getChannel().sendMessage("대기 인원을 확인하세요.").queue();
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
                        sendErrorMessage(channel);
                    }

                } else {
                    // 한명 선택
                    if(numberMatcher.find()){
                        int mentionNumber = Integer.parseInt(mention) - 1;
                        List<Member> userList = userManager.getUserList();
                        Member mentionMember = userList.get(mentionNumber);
                        sendMessage(channel, mentionMember.getAsMention() + mentionMessage);
                    }
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

    // 다중 기능 처리
    private int multiCheck(String[] messge){
        return 0;
    }

}