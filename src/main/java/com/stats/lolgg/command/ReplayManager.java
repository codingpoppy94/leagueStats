package com.stats.lolgg.command;

import java.nio.file.Path;

import org.springframework.stereotype.Component;

import com.stats.lolgg.service.ReplayService;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * Replay 저장 Manager
 * @author codingpoppy94
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class ReplayManager {

    private final ReplayService replayService;

    // 리플 저장 및 데이터 저장
    public String saveFile(String fileUrl,String fileName,MessageReceivedEvent event) throws Exception{
        String fileRegExp = "^[a-zA-Z0-9]*_\\d{4}_\\d{4}.rofl$";
        if(!fileName.matches(fileRegExp)){
            return ":red_circle:등록실패: "+fileName+" 잘못된 파일 이름 형식";
        }
        Member member = event.getMember();
        String createUser = member.getNickname();
        int index = createUser.indexOf("/");
        createUser = createUser.substring(0, index);
        replayService.downloadFile(fileUrl, fileName, createUser);
        // if (path != null){
            // replayService.saveOne(path,createUser);
            // }
        return ":green_circle:등록완료: "+fileName+" 반영 완료";
        // return "error";
    }

}
