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
        
        String message = event.getMessage().getContentRaw();

        if(message.chatAt(0) == '!'){
            
            // 테스트1
            if(message.equals("!ping")){
                log.info("message get");
                event.getChannel().sendMessage("pong!").queue();
                return;
            }
    
            // 목록 추가
            if(message.equals("!ㅊㅋ")){
                log.info("목록 추가");
                userManager.addUser(event.getMember());
                MessageChannel channel = event.getChannel();
    
                channel.sendMessage("Users: \n" + userManager.sendUserList() ).queue();            
            }
    
            // 목록 삭제
            if (message.equals("!ㅊㅅ")) {
                log.info("목록 삭제");
                Member user = event.getMember();
                boolean removed = userManager.removeUser(user);
                MessageChannel channel = event.getChannel();
    
                if (removed) {
                    channel.sendMessage("User " + user.getNickname() + " removed.\nUsers: \n" + userManager.sendUserList() ).queue();
                } else {
                    channel.sendMessage("User " + user.getNickname() + " not found in the list.").queue();
                }
            }
    
            // 목록 보기
            if (message.equals("!ㄷㄱ")) {
                log.info("목록 보기");
                MessageChannel channel = event.getChannel();
                channel.sendMessage("Users: \n" + userManager.sendUserList()).queue();
            }
    
    
            String[] message = event.getMessage().getContentRaw().split("\\s+");
            // 메시지 예시 !ㅁㅅ {1~4} {메시지}
            // "!ㅁㅅ1" 명령어를 받았을 때
            if (message[0].equalsIgnoreCase("!ㅁㅅ")) {
                if (message.length < 2) {
                    event.getChannel().sendMessage("메시지를 입력하세요.").queue();
                    return;
                }
    
                Pattern pattern = Pattern.compile("!ㅁㅅ\\s+(\\d{1,2})~(\\d{1,2})\\s+(.+)");
                Matcher matcher = pattern.matcher(event.getMessage().getContentRaw());
    
                int minMentionCount = Integer.parseInt(matcher.group(1)); // 최소 멘션할 사용자 수
                int maxMentionCount = Integer.parseInt(matcher.group(2)); // 최대 멘션할 사용자 수
                String welcomeMessage = matcher.group(3); // 환영 메시지
    
                List<Member> userList = userManager.getUserList();
    
                if (userList.isEmpty()) {
                    event.getChannel().sendMessage("멤버 리스트가 비어있습니다.").queue();
                    return;
                }
    
                if (maxMentionCount > userList.size()) {
                    event.getChannel().sendMessage("대기 인원을 확인하세요.").queue();
                    return;
                }
    
                // // 리스트에서 첫 번째 멤버를 얻어옵니다.
                // Member firstMember = event.getMember();
                // log.info(firstMember.getAvatarId());
    
                for(int i=minMentionCount; i<maxMentionCount; i++){  
                    Member mentionMember = userList.get(i);
                    event.getChannel().sendMessage(mentionMember.getAsMention());
                }
    
                // 해당 멤버에게 멘션을 보냅니다.
                // {메시지 부분 삽입}
                event.getChannel().sendMessage(welcomeMessage).queue();
            }
    
            if (event.getMessage().getContentRaw().equals("!서버")) {
                // JDA jda = event.getJDA();
                Guild guild = event.getGuild();
                String servierId = guild.getId();
                log.info("serverId " + servierId);
    
                MessageChannel channel = event.getChannel();
                channel.sendMessage("서버 ID: " + servierId).queue();
            }


















        }


    }
}