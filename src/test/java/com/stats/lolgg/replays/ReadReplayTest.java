// package com.stats.lolgg.replays;

// import java.io.FileInputStream;
// import java.io.IOException;
// import java.io.InputStreamReader;
// import java.nio.charset.StandardCharsets;

// import com.fasterxml.jackson.databind.JsonNode;
// import com.fasterxml.jackson.databind.ObjectMapper;

// public class ReadReplayTest {
//     public static void main(String[] args) {

//         // List<String> fileList = Arrays.asList(args);

//         // if (fileList.isEmpty()) {
//         //     System.out.println("파일 리스트가 비어 있습니다.");
//         //     return; // 종료
//         // }
        

//         // for(String filePath : fileList) {

            
//         // }

//         // 파일 경로 설정
//         String filePath = "./replays/1ttest_0530_1211.rofl";
//         if(!filePath.toLowerCase().endsWith(".rofl")){
//             System.out.println("잘못된 파일 선택");
//             return;
//         }

//         String[] gameDate = filePath.split("_");
//         String team = gameDate[0].toUpperCase();
//         String date = gameDate[1] + gameDate[2];



//         String startIndex = "{\"gameLength\":";
//         String endIndex = "\\\"}]\"}";

//         try {
//             // 파일 열기
//             FileInputStream inputStream = new FileInputStream(filePath);
//             InputStreamReader isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            
//             // 데이터 읽기
//             StringBuilder hexData = new StringBuilder();
//             // boolean patternFound = false;
//             int data;
            
//             while ((data = isr.read()) != -1) {
//                 hexData.append((char) data);

//                 // byte[] byteArray = new byte[] {(byte)data};
//                 // String dataString = new String(byteArray,"UTF-8");
//                 // System.out.println(dataString);

//                 // hexData.append(dataString);

//                 if(hexData.toString().endsWith(startIndex)){
//                     hexData.setLength(0);
//                     hexData.append(startIndex);
//                 }
//                 if(hexData.toString().endsWith(endIndex)){
//                     break;
//                 } 
//             }
//             inputStream.close();

//             String StringData = hexData.toString().replace("\\"+"\"", "\"");
//             StringData = StringData.replace("\"[", "[").replace("]\"", "]");

//             System.out.println(StringData);

//             ObjectMapper objectMapper = new ObjectMapper();
//             JsonNode rootNode = objectMapper.readTree(StringData);
//             JsonNode statsArray = rootNode.get("statsJson");

//             for (JsonNode statsNode : statsArray) {
//                 // 원하는 프로퍼티 추출
//                 String assists = statsNode.get("ASSISTS").asText();
//                 String numDeaths = statsNode.get("NUM_DEATHS").asText();
//                 String championsKilled = statsNode.get("CHAMPIONS_KILLED").asText();
//                 String teamPostion = statsNode.get("TEAM_POSITION").asText().replace("JUNGLE", "JUG").replace("BOTTOM", "ADC").replace("UTILITY", "SUP").replace("MIDDLE", "MID");
//                 String name = statsNode.get("NAME").asText();
//                 String win = statsNode.get("WIN").asText().replace("WIN", "승").replace("FAIL","패");
//                 String skin = statsNode.get("SKIN").asText();
//                 // String Camp = statsNode.get("TEAM").asText().replace("100", "Blue").replace("200", "RED");
                
//                 // 추출한 프로퍼티 출력
//                 System.out.println("ASSISTS: " + assists);
//                 System.out.println("NUM_DEATHS: " + numDeaths);
//                 System.out.println("CHAMPIONS_KILLED: " + championsKilled);
//                 System.out.println("TEAM_POSITION: " + teamPostion);
//                 System.out.println("NAME: " + name);
//                 System.out.println("WIN: " + win);
//                 System.out.println("SKIN: " + skin);
                
//             }

//             // outFile(statsArray,team+date);
        
//             // 파일 닫기
//         } catch (IOException e) {
//             e.printStackTrace();
//         }

//     }

//     // public static void outFile(JsonNode statsArray, String fileName){
//     //     // String fileName = "output.csv";
//     //     // String csvFilePath = "src/main/resources/csv/"+fileName;
//     //     String csvFilePath = "csv/"+ fileName + ".csv";


//     //     try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
//     //         // 헤더 작성
//     //         writer.write("날짜,팀,포지션,챔피언,K,D,A,승/패\n");

//     //         // 데이터 작성
//     //         for (JsonNode statsNode : statsArray) {
//     //             // 원하는 프로퍼티 추출
//     //             String assists = statsNode.get("ASSISTS").asText();
//     //             String numDeaths = statsNode.get("NUM_DEATHS").asText();
//     //             String championsKilled = statsNode.get("CHAMPIONS_KILLED").asText();
//     //             String teamPostion = statsNode.get("TEAM_POSITION").asText().replace("JUNGLE", "JUG").replace("BOTTOM", "ADC").replace("UTILITY", "SUP").replace("MIDDLE", "MID");
//     //             String name = statsNode.get("NAME").asText();
//     //             String win = statsNode.get("WIN").asText().replace("Win", "승").replace("Fail","패");
//     //             String skin = statsNode.get("SKIN").asText();
//     //             String Camp = statsNode.get("TEAM").asText().replace("100", "Blue").replace("200", "RED");
    
//     //             writer.write(String.join(",", "",Camp , teamPostion, name, skin, championsKilled, numDeaths, assists, win) + "\n");
//     //         }

//     //         System.out.println("CSV 파일이 생성되었습니다: " + csvFilePath);
//     //     } catch (IOException e) {
//     //         e.printStackTrace();
//     //     }

//     // }

    
//     // // 16진수 데이터를 UTF-8 텍스트로 변환하는 메서드
//     // public static String hexToText(String hexData) {
//     //     try {
//     //         // 16진수 문자열을 바이트 배열로 변환
//     //         byte[] bytes = hexStringToByteArray(hexData);

//     //         // UTF-8으로 디코딩하여 문자열로 변환
//     //         return new String(bytes, "UTF-8");
//     //     } catch (UnsupportedEncodingException e) {
//     //         e.printStackTrace();
//     //     }
//     //     return null;
//     // }

//     // // 16진수 문자열을 바이트 배열로 변환하는 메서드
//     // public static byte[] hexStringToByteArray(String hexString) {
//     //     int len = hexString.length();
//     //     System.out.println("len: " + len);
//     //     byte[] data = new byte[len / 2];
//     //     for (int i = 0; i < len; i += 2) {
//     //         data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
//     //                              + Character.digit(hexString.charAt(i + 1), 16));
//     //     }
//     //     return data;
//     // }

// }
