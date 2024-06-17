package com.stats.lolgg.command;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.stats.lolgg.service.ParseService;
import com.stats.lolgg.service.ReplayService;

import lombok.RequiredArgsConstructor;

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
}
