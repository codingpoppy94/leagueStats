package com.stats.lolgg.command;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.stats.lolgg.service.ParseService;
import com.stats.lolgg.service.ReplayService;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * Replay 파싱 - 저장 Manager
 * @author codingpoppy94
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class ReplayManager {

    private final ReplayService replayService;

    private final ParseService parseService;

    // 리플 저장 및 데이터 저장
    public String saveFile(String fileUrl, String fileNameWithExt, String createUser) throws Exception{

        String fileName = replayService.validateFile(fileNameWithExt);
        
        if(createUser != null){
            int index = createUser.indexOf("/");
            createUser = createUser.substring(0, index);
        } else {
            throw new Exception("별명 설정 해주세요");
        }

        JsonNode statsArray = parseService.parseReplay(fileUrl);
        
        replayService.save(statsArray, fileName, createUser);
        return ":green_circle:등록완료: "+fileNameWithExt+" 반영 완료";
    }

    // 리플레이 파일 데이터 삭제
    public String deleteReplayData(MessageReceivedEvent event, String originMessage){
        String[] message = originMessage.split("\\s");
        String gameId = message[1].toLowerCase();
        //권한체크
        List<Role> roles = Objects.requireNonNull(event.getMember()).getRoles();
        if(checkAuth(roles)){
                int result = replayService.deleteLeagueByGameId(gameId);
            if(result > 0) {
                return ":green_circle:데이터 삭제완료: " + gameId;
            } else {
                return "no data or fail";
            }
        }else {
            return "권한 없음";
        }
    }

    private boolean checkAuth(List<Role> roles){
        for (Role role : roles) {
            if("난민디코관리자".equals(role.getName()) || "난민운영진".equals(role.getName()) ){
                return true;
                // if(role.getPermissions().contains(Permission.ADMINISTRATOR)){
                //     return true;
                // } 
            }
        }
        return false;
    }
}
