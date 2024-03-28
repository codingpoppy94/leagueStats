package com.stats.lolgg.bot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stats.lolgg.command.UserManager;

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
        // 테스트1
        if(event.getMessage().getContentRaw().equals("ping")){
            log.info("message get");
            event.getChannel().sendMessage("pong!").queue();
            return;
        }

        // 목록 추가
        if(event.getMessage().getContentRaw().equals("!ㅊㅋ")){
            log.info("목록 추가");
            userManager.addUser(event.getAuthor().getName());
            MessageChannel channel = event.getChannel();

            channel.sendMessage("Users: \n" + userManager.sendUserList()).queue();            
        }

        // 목록 삭제
        if (event.getMessage().getContentRaw().equals("!ㅊㅅ")) {
            log.info("목록 삭제");
            String username = event.getAuthor().getName();
            boolean removed = userManager.removeUser(username);
            MessageChannel channel = event.getChannel();

            if (removed) {
                channel.sendMessage("User " + username + " removed.\nUsers: \n" + userManager.sendUserList()).queue();
            } else {
                channel.sendMessage("User " + username + " not found in the list.").queue();
            }
        }

        // 목록 보기
        if (event.getMessage().getContentRaw().equals("!ㄷㄱ")) {
            log.info("목록 보기");
            MessageChannel channel = event.getChannel();
            channel.sendMessage("Users: \n" + userManager.sendUserList()).queue();
        }
    }

    // @Override
    // public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
    //     String[] message = event.getMessage().getContentRaw().split("\\s+"); // 메시지를 공백으로 분할

    //     // "!ㅁㅅ1" 명령어를 받았을 때
    //     if (message[0].equalsIgnoreCase("!ㅁㅅ1")) {
    //         if (message.length < 2) {
    //             event.getChannel().sendMessage("메시지를 입력하세요.").queue();
    //             return;
    //         }

    //         String welcomeMessage = String.join(" ", message).substring(5); // 메시지에서 !ㅁㅅ1을 제외한 부분을 가져옴
    //         List<String> memberList = userManager.getUserList();

    //         if (memberList.isEmpty()) {
    //             event.getChannel().sendMessage("멤버 리스트가 비어있습니다.").queue();
    //             return;
    //         }

    //         // 리스트에서 첫 번째 멤버를 얻어옵니다.
    //         Member firstMember = memberList.get(0);

    //         // 해당 멤버에게 멘션을 보냅니다.
    //         event.getChannel().sendMessage(firstMember.getAsMention() + " " + welcomeMessage).queue();
    //     }
    // }

}