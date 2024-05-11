package com.stats.lolgg.bot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.stats.lolgg.command.LeagueManager;
import com.stats.lolgg.command.ReplayManager;
import com.stats.lolgg.command.UserManager;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Discord Bot 명령어
 * @author codingpoppy94
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class ReadyListener extends ListenerAdapter {
    private static Log log = LogFactory.getLog(ReadyListener.class); 

    private final UserManager userManager;
    private final LeagueManager leagueManager;
    private final ReplayManager replayManager;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        String originMessage = event.getMessage().getContentRaw();

        //파일관련
        Message fileMessage = event.getMessage();
        for(Attachment attachment : fileMessage.getAttachments()){
            String fileName = attachment.getFileName();
            // int Size = attachment.getSize();
            String fileRegExp = ".rofl";
            if(fileName.contains(fileRegExp)){
                String fileUrl = attachment.getUrl();
                try {
                    String resultMessage = replayManager.saveFile(fileUrl,fileName,event);
                    sendMessage(channel, resultMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                    // e.getMessage();
                    sendMessage(channel, e.getMessage());
                }
            }   
        }

        if(originMessage.length() > 0) {
            if(originMessage.charAt(0) == '!'){
                String[] message = originMessage.split("\\s");
                // !doc
                if(message[0].equals("!doc")){
                    sendMessage(channel, leagueManager.getHelp());
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
                    if(message.length > 1){
                        String resultMessage = userManager.teamCancel(event, originMessage);
                        if("error".equals(resultMessage)){
                            sendMessage(channel,"존재하지않는 채널");
                        }else {
                            sendMessage(channel, resultMessage);
                        }
                    }else {
                        sendErrorMessage(channel);
                    }
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
                    EmbedBuilder templateMessage;
                    // 남의꺼 호출
                    if(message.length > 1) {
                        int commandIndex = originMessage.indexOf(message[1]);
                        riotName = originMessage.substring(commandIndex);
                        // 공백제거, й -> n으로 replace
                        riotName = riotName.replaceAll("\\s+", "").replaceAll("й", "n");
                        
                        templateMessage = leagueManager.getRecord(riotName);
                    } else {
                        // 자기꺼 호출
                        String nickName = event.getMember().getNickname();
                        int index = nickName.lastIndexOf("/");
                        riotName = nickName.substring(0, index);
                        riotName = riotName.replaceAll("\\s+", "").replaceAll("й", "n");

                        templateMessage = leagueManager.getRecord(riotName);
                    }
                    if(templateMessage == null) {
                        sendMessage(channel, "not found data");
                    } else {
                        sendMessage(channel, templateMessage);
                    }
                }

                
                /* 
                * !장인
                */
                if (message[0].equalsIgnoreCase("!장인")) {
                    EmbedBuilder templateMessage;
                    if(message.length > 1) {
                        int commandIndex = originMessage.indexOf(message[1]);
                        String champName = originMessage.substring(commandIndex);
                        champName = champName.replaceAll("\\s+", "");
                        templateMessage = leagueManager.getChampMaster(champName);

                        if(templateMessage == null){
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
                    EmbedBuilder templateMessage;
                    if(message.length < 2) {
                        templateMessage = leagueManager.getChampStats();
                        
                        if(templateMessage == null){
                            sendMessage(channel, "not found data");
                        } else {
                            sendMessage(channel, templateMessage);
                        }
                    }
                }
                /* 
                 * !라인 {position}
                 */
                if (message[0].equalsIgnoreCase("!라인")) {
                    EmbedBuilder templateMessage;
                    if(message.length > 1) {
                        int commandIndex = originMessage.indexOf(message[1]);
                        String position = originMessage.substring(commandIndex);
                        templateMessage = leagueManager.getRecordLine(position);

                        if(templateMessage == null){
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
    }

    // 채널 메시지 보내기
    private void sendMessage(MessageChannel channel,String message){
        channel.sendMessage(message).queue();
    }

    private void sendMessage(MessageChannel channel,EmbedBuilder message){
        channel.sendMessageEmbeds(message.build()).queue();
    }

    // 공통 에러 메시지
    private void sendErrorMessage(MessageChannel channel) {
        channel.sendMessage("잘못된 명령어 사용").queue();
    }
}