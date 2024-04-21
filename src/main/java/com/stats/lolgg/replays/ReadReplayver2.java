package com.stats.lolgg.replays;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReadReplayver2 {
    public static void main(String[] args) {
        // 파일 경로 설정
        String filePath = "src/main/resources/replays/KR-7037267518.rofl";

        String textData = "";
        String startIndex = "{\"gameLength\":";
        String endIndex = "\\\"}]\"}";

        try {
            // 파일 열기
            FileInputStream inputStream = new FileInputStream(filePath);


            // 데이터 읽기
            StringBuilder hexData = new StringBuilder();
            boolean patternFound = false;
            int data;

            
            while ((data = inputStream.read()) != -1) {
                
                byte[] byteArray = new byte[] {(byte)data};
                String dataString = new String(byteArray);
                // System.out.println(dataString);

                hexData.append(dataString);

                if(hexData.toString().endsWith(startIndex)){
                    hexData.setLength(0);
                    hexData.append(startIndex);
                }
                if(hexData.toString().endsWith(endIndex)){
                    break;
                }
            }
            inputStream.close();

            String result = hexData.toString();
            System.out.println(result);

            String StringData = hexData.toString().replace("\\"+"\"", "\"");
            StringData = StringData.replace("\"[", "[").replace("]\"", "]");
            // System.out.println(StringData);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(StringData);
            JsonNode statsArray = rootNode.get("statsJson");

            for (JsonNode statsNode : statsArray) {
                // 원하는 프로퍼티 추출
                String assists = statsNode.get("ASSISTS").asText();
                String numDeaths = statsNode.get("NUM_DEATHS").asText();
                String championsKilled = statsNode.get("CHAMPIONS_KILLED").asText();
                String teamPostion = statsNode.get("TEAM_POSITION").asText();
                String name = statsNode.get("NAME").asText();
                String win = statsNode.get("WIN").asText();
                String skin = statsNode.get("SKIN").asText();
                
                // 추출한 프로퍼티 출력
                System.out.println("ASSISTS: " + assists);
                System.out.println("NUM_DEATHS: " + numDeaths);
                System.out.println("CHAMPIONS_KILLED: " + championsKilled);
                System.out.println("TEAM_POSITION: " + teamPostion);
                System.out.println("NAME: " + name);
                System.out.println("WIN: " + win);
                System.out.println("SKIN: " + skin);
            }
        
            // 파일 닫기
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            // ObjectMapper 생성
            ObjectMapper objectMapper = new ObjectMapper();

            // textData = "{" + "\"" + textData;

            String fixedJsonData = textData.replace("\\"+"\"", "\"");
            fixedJsonData = fixedJsonData.replace("\"[", "[");
            fixedJsonData = fixedJsonData.replace("]\"", "]");
            System.out.println(fixedJsonData);

            // JSON 데이터 파싱
            JsonNode rootNode = objectMapper.readTree(fixedJsonData);

            JsonNode statsArray = rootNode.get("statsJson");
            System.out.println(statsArray);

            for (JsonNode statsNode : statsArray) {
                // 원하는 프로퍼티 추출
                String assists = statsNode.get("ASSISTS").asText();
                String numDeaths = statsNode.get("NUM_DEATHS").asText();
                String championsKilled = statsNode.get("CHAMPIONS_KILLED").asText();
                String teamPostion = statsNode.get("TEAM_POSITION").asText();
                String name = statsNode.get("NAME").asText();
                String win = statsNode.get("WIN").asText();
                String skin = statsNode.get("SKIN").asText();
                
                // 추출한 프로퍼티 출력
                System.out.println("ASSISTS: " + assists);
                System.out.println("NUM_DEATHS: " + numDeaths);
                System.out.println("CHAMPIONS_KILLED: " + championsKilled);
                System.out.println("TEAM_POSITION: " + teamPostion);
                System.out.println("NAME: " + name);
                System.out.println("WIN: " + win);
                System.out.println("SKIN: " + skin);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    // 16진수 데이터를 UTF-8 텍스트로 변환하는 메서드
    public static String hexToText(String hexData) {
        try {
            // 16진수 문자열을 바이트 배열로 변환
            byte[] bytes = hexStringToByteArray(hexData);

            // UTF-8으로 디코딩하여 문자열로 변환
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 16진수 문자열을 바이트 배열로 변환하는 메서드
    public static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                                 + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

}
