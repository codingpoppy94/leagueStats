package com.stats.lolgg.replays;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stats.lolgg.model.ReplaysModel;

public class ReadReplayver2 {
    public static void main(String[] args) {
        // 파일 경로 설정
        String filePath = "src/main/resources/replays/KR-7037267518.rofl";

        String textData = "";

        try {
            // 파일 열기
            FileInputStream inputStream = new FileInputStream(filePath);

            // 데이터 읽기
            StringBuilder hexData = new StringBuilder();
            boolean patternFound = false;
            int data;
            while ((data = inputStream.read()) != -1) {
                // 각 바이트를 16진수로 변환하여 문자열에 추가
                hexData.append(String.format("%02X", data));

                // 000099C800
                // "4C 65" 패턴 찾기
                if (!patternFound && hexData.toString().endsWith("4C65")) {
                    patternFound = true;
                    hexData.setLength(0); // 이전 데이터를 지우고 다시 시작
                }

                // "45 01 00 00 00" 패턴 찾기
                if (patternFound && hexData.toString().endsWith("5D227D")) {
                    break;
                }
            }

            // 읽은 16진수 데이터 출력
            // System.out.println("16진수 데이터:");
            // System.out.println(hexData);

            textData = hexToText(hexData.toString());
            // System.out.println(textData);


            // 파일 닫기
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            // ObjectMapper 생성
            ObjectMapper objectMapper = new ObjectMapper();

            textData = "{" + "\"" + textData;

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
