package com.stats.lolgg.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stats.lolgg.mapper.LeagueMapper;
import com.stats.lolgg.model.ChampEnum;
import com.stats.lolgg.model.LeagueVO;

import lombok.RequiredArgsConstructor;

/* 
 * Replay 자동 저장, 자동 데이터 등록 서비스
 */
@Service
@RequiredArgsConstructor
public class ReplayService {

    private final LeagueMapper leagueMapper;

    // 중복리플검사
    public int findReplayName(String game_id){
        return leagueMapper.findReplayName(game_id);
    }

    // 부캐-본캐 맵핑
    public List<Map<String,Object>> findMappingName(){
        return leagueMapper.findMappingName();
    }

    /**
     * Replay 데이터 파싱>저장
     * @param file
     * @throws Exception
     */
    public void saveAll(MultipartFile file) throws Exception{
        // MultipartFile에서 파일 이름과 내용 추출
        String fileNameWithExt = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();
        
        // 파일이름
        save(inputStream, fileNameWithExt, "api");
    }

    // 리플저장 미사용 2024-05-07
    public void saveOne(Path filePath, String createUser) throws Exception{
        try {
            File file = filePath.toFile();
    
            if (!file.exists()) {
                throw new Exception(":red_circle:등록실패: 파일을 찾을 수 없습니다.");
            }
    
            InputStream inputStream = new FileInputStream(file);
            String fileNameWithExt = file.getName();
            save(inputStream, fileNameWithExt, createUser);        
            inputStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void save(InputStream inputStream, String fileNameWithExt, String createUser) throws Exception{
        // 파일이름
        String fileRegExp = "^[a-zA-Z0-9]*_\\d{4}_\\d{4}.rofl$";

        if(!fileNameWithExt.matches(fileRegExp)){
            throw new IllegalArgumentException(":red_circle:등록실패: 잘못된 리플 파일 형식");
        }

        int index = fileNameWithExt.lastIndexOf('.');
        String fileName = fileNameWithExt.substring(0, index).toLowerCase();

        if(this.findReplayName(fileName) > 1) {
            throw new IllegalArgumentException(":red_circle:등록실패: 중복된 리플 파일 등록");
        }

        // 파싱 데이터
        JsonNode statsArray = parseReplay(inputStream);

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
            name = name.replaceAll("й", "n");
            name = getMainName(mappingMaps, name);
            String win = statsNode.get("WIN").asText().replace("Win", "승").replace("Fail","패");
            String skin = statsNode.get("SKIN").asText().toLowerCase().trim();
            skin = ChampEnum.getKoreanValue(skin);
            String Camp = statsNode.get("TEAM").asText().replace("100", "blue").replace("200", "red");

            int goldEarend = statsNode.get("GOLD_EARNED").asInt();
            int Ccing = statsNode.get("TIME_CCING_OTHERS").asInt();
            int timePlayed = statsNode.get("TIME_PLAYED").asInt();
            int totalDamage = statsNode.get("TOTAL_DAMAGE_DEALT_TO_CHAMPIONS").asInt();
            int totalDamageTaken = statsNode.get("TOTAL_DAMAGE_TAKEN").asInt();
            int visionScore = statsNode.get("VISION_SCORE").asInt();
            int visionBought = statsNode.get("VISION_WARDS_BOUGHT_IN_GAME").asInt();

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
            leagueVO.setCreate_user(createUser);

            leagueVO.setGold(goldEarend);
            leagueVO.setCcing(Ccing);
            leagueVO.setTime_played(timePlayed);
            leagueVO.setTotal_damage_champions(totalDamage);
            leagueVO.setTotal_damage_taken(totalDamageTaken);
            leagueVO.setVision_score(visionScore);
            leagueVO.setVision_bought(visionBought);

            leagueVOList.add(leagueVO);
        }
        leagueMapper.insertLeague(leagueVOList);
    }

    // discord에서 리플파일 url을 받아서 다운로드
    public void downloadFile(String fileUrl,String fileName,String createUser) throws IOException, InterruptedException{
        // HttpClient 생성
        HttpClient httpClient = HttpClient.newHttpClient();

        // HTTP 요청 생성
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(fileUrl))
            .build();

        HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
        String[] monthDate = fileName.split("_");
        String savePath = "./replays/"+monthDate[1]+"/"+fileName;

        String textFileName = "./replays/"+monthDate[1]+"/"+monthDate[1]+".text";

        // 디렉토리 생성
        try {
            Path directory = Paths.get(savePath).getParent();
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
        } catch (IOException e) {
            System.err.println("디렉토리 생성 중 오류가 발생했습니다: " + e.getMessage());
            return;
        }

        try (InputStream inputStream = response.body();
            BufferedWriter writer = new BufferedWriter(new FileWriter(textFileName, true))    
        ){
            this.save(inputStream, fileName,createUser);
            writer.write(fileName);
            writer.newLine();
            inputStream.close();
            writer.close();
        // try 
        //     (
        //     // 리플레이 파일 다운로드 (2024-05-07 변경)
        //     InputStream inputStream = response.body();
        //     FileOutputStream outputStream = new FileOutputStream(savePath)) {
        //     byte[] buffer = new byte[4096];
        //     int bytesRead;
        //     while ((bytesRead = inputStream.read(buffer)) != -1) {
        //         outputStream.write(buffer, 0, bytesRead);
        //     }
        //     inputStream.close();
        //     outputStream.close();
        //
        } catch (Exception e){
            e.printStackTrace();
        } finally {

        }
        // return Paths.get(savePath);
    }

    /* 내부 함수 */
    private JsonNode parseReplay(InputStream inputStream) throws Exception {

        String startIndex = "{\"gameLength\":";
        String endIndex = "\\\"}]\"}";

        try {
            // 파일 열기
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
