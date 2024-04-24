package com.stats.lolgg.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stats.lolgg.mapper.LeagueMapper;
import com.stats.lolgg.model.LeagueVO;


@Service
public class LeagueService {

    @Autowired
    private LeagueMapper leagueMapper;
    
    /**
     * 전체 조회
     * @return
     */
    public List<LeagueVO> findAll(){
        return leagueMapper.findAll();
    }


    /**
     * Replay 데이터 파싱>저장
     * @param file
     * @throws Exception
     */
    public void saveAll(MultipartFile file) throws Exception{
        // 파싱 데이터
        JsonNode statsArray = parseReplay(file);

        // 파일이름
        String fileNameWithExt = file.getOriginalFilename();

        int index = fileNameWithExt.lastIndexOf('.');
        String fileName = fileNameWithExt.substring(0, index);
        // System.out.println("fileName: "+fileName);

        List<LeagueVO> leagueVOList = new ArrayList<>();
    
        // 날짜
        int currentYear = LocalDateTime.now().getYear();
        String[] monthDay = fileName.split("_");
        
        int month = Integer.parseInt(monthDay[1].substring(0, 2));
        int day = Integer.parseInt(monthDay[1].substring(2));

        // 현재 년도와 추출한 월, 일을 사용하여 LocalDateTime 생성
        LocalDateTime gameDate = LocalDateTime.of(currentYear, month, day, 0, 0);

        for (JsonNode statsNode : statsArray) {
            LeagueVO leagueVO = new LeagueVO();
            // 원하는 프로퍼티 추출
            String assists = statsNode.get("ASSISTS").asText();
            String numDeaths = statsNode.get("NUM_DEATHS").asText();
            String championsKilled = statsNode.get("CHAMPIONS_KILLED").asText();
            String teamPostion = statsNode.get("TEAM_POSITION").asText().replace("JUNGLE", "JUG").replace("BOTTOM", "ADC").replace("UTILITY", "SUP").replace("MIDDLE", "MID");
            String name = statsNode.get("NAME").asText();
            String win = statsNode.get("WIN").asText().replace("Win", "승").replace("Fail","패");
            String skin = statsNode.get("SKIN").asText();
            String Camp = statsNode.get("TEAM").asText().replace("100", "Blue").replace("200", "RED");

            String kda = championsKilled + "/" + numDeaths + "/" + assists;

            leagueVO.setGame_id(fileName);
            leagueVO.setGame_team(Camp);
            leagueVO.setPosition(teamPostion);
            leagueVO.setRiot_name(name);
            leagueVO.setChamp_name(skin);
            leagueVO.setKda(kda);
            leagueVO.setGame_result(win);
            leagueVO.setGame_date(gameDate);
            leagueVO.setDelete_yn('N');

            leagueVOList.add(leagueVO);
        }
        leagueMapper.insertLeague(leagueVOList);
    }

    private JsonNode parseReplay(MultipartFile file) throws Exception {

        String startIndex = "{\"gameLength\":";
        String endIndex = "\\\"}]\"}";

        try {
            // 파일 열기
            InputStream inputStream = file.getInputStream();
            InputStreamReader isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            
            // 데이터 읽기
            StringBuilder hexData = new StringBuilder();
            int data;
             
            while ((data = isr.read()) != -1) {
                hexData.append((char) data);

                if(hexData.toString().endsWith(startIndex)){
                    hexData.setLength(0);
                    hexData.append(startIndex);
                }
                if(hexData.toString().endsWith(endIndex)){
                    break;
                }
             }
            String StringData = hexData.toString().replace("\\"+"\"", "\"");
            StringData = StringData.replace("\"[", "[").replace("]\"", "]");

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(StringData);
            JsonNode statsArray = rootNode.get("statsJson");

            // for (JsonNode statsNode : statsArray) {
            //     // 원하는 프로퍼티 추출
            //     String assists = statsNode.get("ASSISTS").asText();
            //     String numDeaths = statsNode.get("NUM_DEATHS").asText();
            //     String championsKilled = statsNode.get("CHAMPIONS_KILLED").asText();
            //     String teamPostion = statsNode.get("TEAM_POSITION").asText().replace("JUNGLE", "JUG").replace("BOTTOM", "ADC").replace("UTILITY", "SUP").replace("MIDDLE", "MID");
            //     String name = statsNode.get("NAME").asText();
            //     String win = statsNode.get("WIN").asText().replace("WIN", "승").replace("FAIL","패");
            //     String skin = statsNode.get("SKIN").asText();
            //     String Camp = statsNode.get("TEAM").asText().replace("100", "Blue").replace("200", "RED");
                
            //     // 추출한 프로퍼티 출력
            //     System.out.println("ASSISTS: " + assists);
            //     System.out.println("NUM_DEATHS: " + numDeaths);
            //     System.out.println("CHAMPIONS_KILLED: " + championsKilled);
            //     System.out.println("TEAM_POSITION: " + teamPostion);
            //     System.out.println("NAME: " + name);
            //     System.out.println("WIN: " + win);
            //     System.out.println("SKIN: " + skin);
            // }
            System.out.println("파싱완료");
            // 파일 닫기
            inputStream.close();
            return statsArray;
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("파싱에러");
        }

    }
}
