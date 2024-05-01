package com.stats.lolgg.bot;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stats.lolgg.command.LeagueManager;
import com.stats.lolgg.command.UserManager;

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
                sendMessage(channel, userManager.sendUserList());
            }
    
            /* 
             * 취소
             */
            if (message[0].equals("!ㅊㅅ") || message[0].equals("!취소")) {
                String resultMessage = userManager.cancelUser(event, originMessage);
                if("error".equals(resultMessage)){
                    sendErrorMessage(channel);
                }else {
                    sendMessage(channel, resultMessage);
                }
            }

            /* 
             * 팀 취소(추가작업필요)
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
                String resultMessage = userManager.userMention(originMessage);
                if("error".equals(resultMessage)){
                    sendErrorMessage(channel);
                }else {
                    sendMessage(channel, resultMessage);
                }
            }
    
            /* 
             * get server Id
             */
            if (event.getMessage().getContentRaw().equals("!서버")) {
                // JDA jda = event.getJDA();
                Guild guild = event.getGuild();
                String servierId = guild.getId();
                log.info("serverId " + servierId);
                
                channel.sendMessage("서버 ID: " + servierId).queue();
            }

            /*
             * !전적
             */
            if (message[0].equalsIgnoreCase("!전적")) {
                
                String riotName = "";
                String templateMessage = "";
                // 남의꺼 호출
                if(message.length > 1) {
                    int commandIndex = originMessage.indexOf(message[1]);
                    riotName = originMessage.substring(commandIndex);
                    // 공백제거
                    riotName = riotName.replaceAll("\\s+", "");

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

            
            /* 
             * !장인
             */
            if (message[0].equalsIgnoreCase("!장인")) {
                String templateMessage = "";
                if(message.length > 1) {
                    int commandIndex = originMessage.indexOf(message[1]);
                    String champName = originMessage.substring(commandIndex);
                    champName = champName.replaceAll("\\s+", "");
                    templateMessage = leagueManager.getChampMaster(champName);

                    if("".equals(templateMessage)){
                        sendMessage(channel, "not found data");
                    } else {
                        sendMessage(channel, templateMessage);
                    }
                }
            }

            /* 
             * !통계
             */
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
            /* 
             * !탈퇴 
             * riot_name
             */
            if (message[0].equalsIgnoreCase("!탈퇴")){
                int result = userManager.withdrawUser(event, originMessage);
                if(result > 0){
                    sendMessage(channel, "탈퇴 처리 완료");
                }else {
                    sendMessage(channel, "권한 없음");
                }
            }
            /* 
             * !복귀
             * riot_name
             */
            if (message[0].equalsIgnoreCase("!복귀")){
                int result = userManager.recoverUser(event, originMessage);
                if(result > 0){
                    sendMessage(channel, "복귀 처리 완료");
                }else {
                    sendMessage(channel, "권한 없음");
                }
            }
            /* 
             * !부캐저장
             * sub_name,main_name
             */
            if(message[0].equalsIgnoreCase("!부캐저장")){
                String result = userManager.saveSubInfo(event, originMessage);
                if("error".equals(result)){
                    sendErrorMessage(channel);
                }else {
                    sendMessage(channel, result);
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
}