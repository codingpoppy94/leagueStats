package com.stats.lolgg.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stats.lolgg.mapper.LeagueMapper;
import com.stats.lolgg.model.ChampEnum;
import com.stats.lolgg.model.LeagueStatsVO;
import com.stats.lolgg.model.LeagueVO;


@Service
public class LeagueService {

    @Autowired
    private LeagueMapper leagueMapper;

    /* select */
    
    /**
     * 전체 조회
     * @return
     */
    public List<LeagueVO> findAll(){
        return leagueMapper.findAll();
    }

    public List<LeagueVO> findTopTen(String riot_name){
        return leagueMapper.findTopTen(riot_name);
    }

    public List<LeagueStatsVO> findRecord(String riot_name){
        return leagueMapper.findRecord(riot_name);
    }

    public LeagueStatsVO findRecordMonth(String riot_name){
        return leagueMapper.findRecordMonth(riot_name);
    }

    public List<LeagueStatsVO> findMostPick(String riot_name){
        return leagueMapper.findMostPick(riot_name);
    }

    public List<LeagueStatsVO> findChampMaster(String champ_name){
        return leagueMapper.findChampMaster(champ_name);
    }

    public List<LeagueStatsVO> findChampHighRate(){
        return leagueMapper.findChampHighRate();
    }

    public List<LeagueStatsVO> findRecordWithTeam(String riot_name){
        return leagueMapper.findRecordWithTeam(riot_name);
    }

    public List<LeagueStatsVO> findRecordOtherTeam(String riot_name){
        return leagueMapper.findRecordOtherTeam(riot_name);
    }

    public List<LeagueStatsVO> findRecordLine(String position){
        return leagueMapper.findRecordLine(position);
    }

    public List<Map<String,Object>> findMappingName(){
        return leagueMapper.findMappingName();
    }

    /* insert */

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
        String fileName = fileNameWithExt.substring(0, index).toLowerCase();
        System.out.println("fileName: "+fileName);

        List<LeagueVO> leagueVOList = new ArrayList<>();
        List<Map<String,Object>> mappingMaps = findMappingName();
        
        // 날짜
        int currentYear = LocalDateTime.now().getYear();
        String[] dateTime = fileName.split("_");
        
        int month = Integer.parseInt(dateTime[1].substring(0, 2));
        int day = Integer.parseInt(dateTime[1].substring(2));

        int hour = Integer.parseInt(dateTime[2].substring(0, 2));
        if(hour == 24) {
            hour = 0;
        }
        int minute = Integer.parseInt(dateTime[2].substring(2));
        

        // 현재 년도와 추출한 월, 일을 사용하여 LocalDateTime 생성
        LocalDateTime gameDate = LocalDateTime.of(currentYear, month, day, hour, minute);

        for (JsonNode statsNode : statsArray) {
            LeagueVO leagueVO = new LeagueVO();
            // 원하는 프로퍼티 추출
            int assists = statsNode.get("ASSISTS").asInt();
            int numDeaths = statsNode.get("NUM_DEATHS").asInt();
            int championsKilled = statsNode.get("CHAMPIONS_KILLED").asInt();
            String teamPostion = statsNode.get("TEAM_POSITION").asText().replace("JUNGLE", "JUG").replace("BOTTOM", "ADC").replace("UTILITY", "SUP").replace("MIDDLE", "MID");
            String name = statsNode.get("NAME").asText().trim();
            name = name.replaceAll("\\s+","");
            name = getMainName(mappingMaps, name);
            String win = statsNode.get("WIN").asText().replace("Win", "승").replace("Fail","패");
            String skin = statsNode.get("SKIN").asText().toLowerCase().trim();
            skin = ChampEnum.getKoreanValue(skin);
            String Camp = statsNode.get("TEAM").asText().replace("100", "blue").replace("200", "red");

            leagueVO.setGame_id(fileName);
            leagueVO.setGame_team(Camp);
            leagueVO.setPosition(teamPostion);
            leagueVO.setRiot_name(name);
            leagueVO.setChamp_name(skin);
            leagueVO.setKill(championsKilled);
            leagueVO.setDeath(numDeaths);
            leagueVO.setAssist(assists);
            leagueVO.setGame_result(win);
            leagueVO.setGame_date(gameDate);
            leagueVO.setDelete_yn('N');

            leagueVOList.add(leagueVO);
        }
        leagueMapper.insertLeague(leagueVOList);
    }
    /* 
     * 부캐닉 저장
     */
    public void saveMappingName(Map<String,Object> paramMap){
        leagueMapper.insertMappingName(paramMap);
    }
    
    /* update */

    public int changeDeleteYN(Map<String,Object> paramMap){
        return leagueMapper.changeDeleteYN(paramMap);
    }

    public int changeRiotName(Map<String,Object> paramMap){
        return leagueMapper.changeRiotName(paramMap);
    }

    /* 내부 함수 */
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

            System.out.println("파싱완료");
            // 파일 닫기
            inputStream.close();
            return statsArray;
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("파싱에러");
        }

    }

    private String getMainName(List<Map<String,Object>> mappingMaps, String originName) {
        // System.out.println("originName: " +originName);
        for (Map<String,Object> mappingMap : mappingMaps){
            // for (Entry<String, Object> entrySet : mappingMap.entrySet()) {     
            //     System.out.println(entrySet.getKey() + " : " + entrySet.getValue());        
            // }
            // System.out.println(mappingMap.entrySet());
            if(mappingMap.get("sub_name").equals(originName)){
                return (String) mappingMap.getOrDefault("main_name", originName);
            }   
        }
        return  originName;
    }
}
