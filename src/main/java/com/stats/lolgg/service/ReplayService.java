package com.stats.lolgg.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stats.lolgg.mapper.LeagueMapper;
import com.stats.lolgg.model.ChampEnum;
import com.stats.lolgg.model.LeagueVO;

import lombok.RequiredArgsConstructor;

/* 
 * Replay Service app을 통해 파싱 데이터를 가져오고 데이터 저장
 */
@Service
@RequiredArgsConstructor
public class ReplayService {

    private static final Logger logger = LoggerFactory.getLogger(ReplayService.class);

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
     * Replay 데이터 파싱 후 저장
     * @param file
     * @throws Exception
     */
    public void save(String fileUrl, String fileNameWithExt, String createUser) throws Exception{
        String fileName = validateFile(fileNameWithExt);

        // 파싱 데이터
        JsonNode statsArray = parseReplay(fileUrl);
        
        // 데이터 저장
        leagueMapper.insertLeague(setData(statsArray, fileName, createUser));
        saveReplayFileLog(fileName);
    }

    /**
     * api 호출로 데이터 저장
     * @param file
     * @throws Exception
     */
    public void saveFromApi(JsonNode statsArray, String fileNameWithExt, String createUser) throws Exception {
        // 파일이름
        String fileName = validateFile(fileNameWithExt);
        
        leagueMapper.insertLeague(setData(statsArray, fileName, createUser));
        saveReplayFileLog(fileName);
    }


    // replay 파일 이름 로그 저장
    private void saveReplayFileLog(String fileName) throws IOException{
        String[] monthDate = fileName.split("_");
        // String savePath = "src/main/resources/replays/"+monthDate[1]+"/"+fileName;
        String savePath = "./replays/"+monthDate[1]+"/"+fileName;
        
        String textFileName = "./replays/"+monthDate[1]+"/"+monthDate[1]+".text";

        try {
            Path directory = Paths.get(savePath).getParent();
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(textFileName, true));
            writer.write(fileName);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.err.println("디렉토리 생성 중 오류가 발생했습니다: " + e.getMessage());
            return;
        }
    }

    // 본캐이름 불러오기
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

    private List<LeagueVO> setData(JsonNode statsArray,String fileName, String createUser){
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

        List<LeagueVO> leagueVOList = new ArrayList<>();

        // 부캐본캐 정리
        List<Map<String,Object>> mappingMaps = findMappingName();
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
        return leagueVOList;
    }

    // 파일명, 중복파일 검증
    private String validateFile(String fileNameWithExt){
        String fileRegExp = "^[a-zA-Z0-9]*_\\d{4}_\\d{4}.rofl$";

        if(!fileNameWithExt.matches(fileRegExp)){
            throw new IllegalArgumentException(":red_circle:등록실패: 잘못된 리플 파일 형식");
        }

        int index = fileNameWithExt.lastIndexOf('.');
        String fileName = fileNameWithExt.substring(0, index).toLowerCase();

        if(this.findReplayName(fileName) > 1) {
            throw new IllegalArgumentException(":red_circle:등록실패: 중복된 리플 파일 등록");
        }
        return fileName;
    }

    private JsonNode parseReplay(String fileUrl) throws Exception{
        byte[] bytes = getInputStreamDiscordFile(fileUrl);
        try {
            return parseReplayData(bytes);
        } finally {
            
        }
    }

    // 리플레이 파일 데이터 파싱
    public JsonNode parseReplayData(byte[] byteArrays) throws Exception{
        String startIndex = "{\"gameLength\":";
        String endIndex = "\\\"}]\"}";
        int bytesToRead = 65536; 

        if(byteArrays == null || byteArrays.length == 0){
            throw new Exception("파싱 데이터가 없습니다");
        }

        try {
            // 파일 열기
            int fileSize = byteArrays.length;

            long startPosition = Math.max(fileSize - bytesToRead, 0);
            int actualBytesToRead = (int) Math.min(bytesToRead, fileSize);

            byte[] bytes = new byte[actualBytesToRead];

            System.arraycopy(byteArrays, (int) startPosition, bytes, 0, actualBytesToRead);

            String data = new String(bytes, "UTF-8");

            StringBuilder hexData = new StringBuilder();

            for (int i = 0; i < data.length(); i++) {
                hexData.append(data.charAt(i));

                if (hexData.toString().endsWith(startIndex)) {
                    hexData.setLength(0);
                    hexData.append(startIndex);
                }
                if (hexData.toString().endsWith(endIndex)) {
                    break;
                }
            }
            
            String StringData = hexData.toString().replace("\\"+"\"", "\"");
            StringData = StringData.replace("\"[", "[").replace("]\"", "]");

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(StringData);
            JsonNode statsArray = rootNode.get("statsJson");

            return statsArray;
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("파싱에러");
        }
    }

    // 디스코드에 올린 파일 데이터 가져오기
    private byte[] getInputStreamDiscordFile(String fileUrl) throws IOException, InterruptedException {
        // HttpClient 생성
        HttpClient httpClient = HttpClient.newHttpClient();

        // HTTP 요청 생성
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(fileUrl))
            .build();

        HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());

        return changeByteArray(response.body());
    }

    public byte[] changeByteArray(InputStream inputStream) {
        int nRead;
        byte[] data = new byte[1024]; 
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                // 읽은 데이터의 각 바이트를 로그로 출력
                // for (int i = 0; i < nRead; i++) {
                    // System.out.printf("%02X ", data[i]);
                // }
                buffer.write(data, 0, nRead);
            }
                // 버퍼 플러시
            buffer.flush();
            return buffer.toByteArray();
        } catch (Exception e) {
            logger.info(e.getMessage());
            return null;
        }
    }
}
