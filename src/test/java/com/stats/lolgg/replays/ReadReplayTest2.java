// package com.stats.lolgg.replays;

// import java.io.FileInputStream;
// import java.io.IOException;
// import java.nio.ByteBuffer;
// import java.nio.channels.FileChannel;

// import com.fasterxml.jackson.databind.JsonNode;
// import com.fasterxml.jackson.databind.ObjectMapper;

// public class ReadReplayTest2 {
//     public static void main(String[] args) {

//         // List<String> fileList = Arrays.asList(args);

//         // if (fileList.isEmpty()) {
//         //     System.out.println("파일 리스트가 비어 있습니다.");
//         //     return; // 종료
//         // }
        

//         // for(String filePath : fileList) {

            
//         // }

//         // 파일 경로 설정
//         String filePath = "./replays/1T_0531_2329.rofl";
//         int bytesToRead = 65536; 
//         if(!filePath.toLowerCase().endsWith(".rofl")){
//             System.out.println("잘못된 파일 선택");
//             return;
//         }

//         String startIndex = "{\"gameLength\":";
//         String endIndex = "\\\"}]\"}";

//         try {
//             // 파일 열기
//             FileInputStream inputStream = new FileInputStream(filePath);
//             FileChannel fileChannel = inputStream.getChannel();

//             long fileSize = fileChannel.size();
//             System.out.println(fileSize);

//             long startPosition = Math.max(fileSize - bytesToRead, 0);
//             int actualBytesToRead = (int) Math.min(bytesToRead, fileSize);

            
//             ByteBuffer buffer = ByteBuffer.allocate(actualBytesToRead);
//             fileChannel.position(startPosition);
//             fileChannel.read(buffer);
//             buffer.flip();

//             // 데이터 읽기
//             byte[] bytes = new byte[buffer.remaining()];
//             buffer.get(bytes);

//             String data = new String(bytes, "UTF-8");

//             // 데이터 읽기
//             StringBuilder hexData = new StringBuilder();

//             for (int i = 0; i < data.length(); i++) {
//                 hexData.append(data.charAt(i));

//                 if(hexData.toString().endsWith(startIndex)){
//                     hexData.setLength(0);
//                     hexData.append(startIndex);
//                 }
//                 if(hexData.toString().endsWith(endIndex)){
//                     break;
//                 }
//             }

//             // while (buffer.hasRemaining()) {
//             //     // System.out.print((char) buffer.get());
//             //     hexData.append((char)buffer.get());
//             //         // System.out.print((char) buffer.get());

//             //         if(hexData.toString().endsWith(startIndex)){
//             //             hexData.setLength(0);
//             //             hexData.append(startIndex);
//             //         }
//             //         if(hexData.toString().endsWith(endIndex)){
//             //             break;
//             //         } 
//             // }

//             // int data;
            
//             // while ((data = isr.read()) != -1) {
//             //     hexData.append((char) data);

//             //     if(hexData.toString().endsWith(startIndex)){
//             //         hexData.setLength(0);
//             //         hexData.append(startIndex);
//             //     }
//             //     if(hexData.toString().endsWith(endIndex)){
//             //         break;
//             //     } 
//             // }
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


// }
