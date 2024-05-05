package com.stats.lolgg.command;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.stats.lolgg.service.ReplayService;

/**
 * Replay 저장 Manager
 * @author codingpoppy94
 * @version 1.0
 */
@Component
public class ReplayManager {
    @Autowired
    private ReplayService replayService;

    // 리플 저장 및 데이터 저장
    public String saveFile(String fileUrl,String fileName) throws Exception{
        String fileRegExp = "^[a-zA-Z0-9]*_\\d{4}_\\d{4}.rofl$";
        if(!fileName.matches(fileRegExp)){
            return ":red_circle:등록실패: 잘못된 파일 이름 형식";
        }
        Path path = replayService.downloadFile(fileUrl, fileName);
        if (path != null){
            replayService.saveOne(path);
            return ":green_circle:등록완료: 리플레이 전적 반영 완료";
        }
        return "error";
    }

}
