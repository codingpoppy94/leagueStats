package com.stats.lolgg.template;

import java.util.List;
import java.util.Map;

import com.stats.lolgg.model.LeagueStatsVO;
import com.stats.lolgg.model.LeagueVO;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;

public class LolTemplate {

    /* !전적 템플릿 */
    public EmbedBuilder makeRecordTemplate(Map<String,Object> map){

        EmbedBuilder embed = new EmbedBuilder();

        String riotName = (String) map.get("riotName");
        LeagueStatsVO monthRecord = (LeagueStatsVO) map.get("monthRecord");
        List<LeagueStatsVO> allRecords = (List<LeagueStatsVO>) map.get("allRecord");
        List<LeagueVO> recentMatchs = (List<LeagueVO>) map.get("recentMatch");
        List<LeagueStatsVO> mostPicks = (List<LeagueStatsVO>) map.get("mostPick");
        List<LeagueStatsVO> goodTeams = (List<LeagueStatsVO>) map.get("goodTeam");
        List<LeagueStatsVO> badTeams = (List<LeagueStatsVO>) map.get("badTeam");
        List<LeagueStatsVO> easyRivals = (List<LeagueStatsVO>) map.get("easyRivals");
        List<LeagueStatsVO> hardRivals = (List<LeagueStatsVO>) map.get("hardRivals");
        
        int allTotal = 0;
        int allWin = 0;
        int allLose = 0;
        float allWinLate = 0;

        int maxTotalCount = 0;
        for(LeagueStatsVO record: allRecords){
            if (record.getTotal_count() > maxTotalCount) {
                maxTotalCount = record.getTotal_count();
            }
        }
        //이번달전적
        String monthStatStr = makeStats("이번달 전적", monthRecord.getTotal_count(), monthRecord.getWin(), monthRecord.getLose(), monthRecord.getWin_rate(), monthRecord.getKda());
        
        //통합전적
        String allStatStr = "";
        for(LeagueStatsVO record: allRecords){
            allWin += record.getWin();
            allLose += record.getLose();

            if(record.getTotal_count() ==  maxTotalCount ) {
                allStatStr += ":thumbsup: ";
            }
            allStatStr += makeStats(record.getPosition(), record.getTotal_count(), record.getWin(), record.getLose(), record.getWin_rate(),9999);
        }
        allTotal = allWin + allLose;
        allWinLate = Math.round((float) allWin * 100 / allTotal * 100) / 100.0f;
        String allStatStrHeader = makeStats("통합 전적", allTotal, allWin, allLose, allWinLate,9999);

        //최근전적
        String matchStr = "";
        int recentTotal = 0;
        int recentWin = 0;
        int recentlose = 0;
        for(LeagueVO recentMatch : recentMatchs) {
            recentTotal ++;
            if("승".equals(recentMatch.getGame_result())){
                recentWin ++;
                matchStr += ":blue_circle: ";
            }else {
                recentlose ++;
                matchStr += ":red_circle: ";
            }
            String kda = recentMatch.getKill()+"/"+recentMatch.getDeath()+"/"+recentMatch.getAssist();
            matchStr +=recentMatch.getChamp_name() + " " + kda + "\n";
        }
        String matchHeader = "최근 "+recentTotal+"전 "+recentWin+"승 "+recentlose+"패";

        //teamGood
        String goodTeamStr = "";
        for(LeagueStatsVO goodTeam: goodTeams){
            goodTeamStr += makeTeamStats(goodTeam.getRiot_name(), goodTeam.getWin(), goodTeam.getLose(), goodTeam.getWin_rate());
        }

        String goodTeamHeader = "팀워크:blue_heart:";

        //teamBad
        String badTeamStr = "";
        for(LeagueStatsVO badTeam: badTeams){
            badTeamStr += makeTeamStats(badTeam.getRiot_name(), badTeam.getWin(), badTeam.getLose(), badTeam.getWin_rate());
        }

        String badTeamHeader = "팀워크:broken_heart:";

        //모스트픽
        String mostStr = "";
        for(LeagueStatsVO most : mostPicks){
            mostStr += most.getChamp_name() + ": " + most.getTotal_count() + "회 " + most.getWin_rate() + "%" + "\n";
        }
        String mostHeader = "MostPick";

        //easyRivals
        String easyStr = "";
        for(LeagueStatsVO easy : easyRivals){
            easyStr += makeTeamStats(easy.getRiot_name(), easy.getWin(), easy.getLose(), easy.getWin_rate());
        }
        String esayHeader = "맞라인:thumbsup:";


        //hardRival
        String hardStr = "";
        for(LeagueStatsVO hard : hardRivals){
            hardStr += makeTeamStats(hard.getRiot_name(), hard.getWin(), hard.getLose(), hard.getWin_rate());
        }
        String hardHeader = "맞라인:thumbsdown:";

        String desc = "<:__:1197186572433490090>"+ monthStatStr +"\n"+ allStatStrHeader + allStatStr;
        // CustomEmoji emoji = "<:__:1197186572433490090>";
        // emoji.getAsMention();

        if("크넹".equals(riotName)){
            riotName = "<:__:1197186572433490090>";
            riotName += "<:__:1197186590968139836>";
        }

        //템플릿생성
        embed.setTitle(riotName);   
        // embed.setColor(Color.ORANGE);
        embed.setDescription(desc);
        embed.addField(matchHeader,matchStr,true);
        embed.addField(goodTeamHeader,goodTeamStr,true);
        embed.addField(badTeamHeader,badTeamStr,true);
        embed.addField(mostHeader,mostStr,true);
        embed.addField(esayHeader,easyStr,true);
        embed.addField(hardHeader,hardStr,true);
        return embed;
    }

    /* !장인 */
    public EmbedBuilder makeChampMasterTemplate(List<LeagueStatsVO> records, String champName){
        EmbedBuilder embed = new EmbedBuilder();
        String header =champName+ " 장인";
        String content = "";
        
        for(LeagueStatsVO record : records ){
            content += record.getRiot_name() + " ";
            content += record.getWin() + "승 " + record.getLose() + "패 " + record.getWin_rate() + "% \n";
        }
        embed.setTitle(header);
        embed.setDescription(content);
        return embed;
    }

    /* !통계 */
    public EmbedBuilder makeChampHighRateTemplate(List<LeagueStatsVO> records){
        EmbedBuilder embed = new EmbedBuilder();
        String header = "이번달 챔프 통계(5판 이상)";
        String content = "";

        for(LeagueStatsVO record : records ){
            content += record.getChamp_name() + " ";
            content += record.getWin() + "승 " + record.getLose() + "패 " + record.getWin_rate() + "% \n";
        }
        embed.setTitle(header);
        embed.setDescription(content);
        return embed;
    }

    /* !라인 */
    public EmbedBuilder makeRecordLine(List<LeagueStatsVO> records, String position){
        EmbedBuilder embed = new EmbedBuilder();
        String header = position + " 라인";
        String content = "";
        int i = 1;
        for(LeagueStatsVO record : records ){
            if(i == 1){
                i++;
                content += ":one: ";
            }else if(i == 2) {
                i++;
                content += ":two: ";
            }else if(i == 3) {
                i++;
                content += ":three: ";
            }else if(i == 4) {
                i++;
                content += ":four: ";
            }else if(i == 5) {
                i++;
                content += ":five: ";
            }else {
                content += i++ +". ";
            }
            content +=record.getRiot_name()+makeStats("",record.getTotal_count(), record.getWin(), record.getLose(), record.getWin_rate(), record.getKda());
        }
        embed.setTitle(header);
        embed.setDescription(content);
        return embed;
    }

    /* !help */
    public EmbedBuilder makeHelpTemplate(){
        EmbedBuilder builder = new EmbedBuilder();
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append("※내전관리명령어 \n");
        sb.append("`!ㅊㅋ !.`  내전 대기 추가 \n");
        sb.append("`!취소 !ㅊㅅ {x} {x~y}`  본인 내전 대기 취소, x번 취소,x~y범위 취소 \n");
        sb.append("`!ㅌㅊㅅ {x}`   x팀에 속한 사람들 내전 대기 단체 취소 \n");
        sb.append("`!대기 !ㄷㄱ`  내전대기 목록 \n");
        sb.append("`!ㅁㅅ {x} {x~y}` x번 멘션, x~y범위 멘션 \n\n");
        sb.append("※통계명령어 \n");
        sb.append("`!전적  !전적 {name}` 자신의 전적, name의 전적 검색 \n");
        sb.append("`!장인 {champ}` 승률55%이상 장인 목록 \n");
        sb.append("`!통계` 이번달 5판 이상 챔피언 목록 \n\n");
        sb.append("※관리자명령어 (디코관리자 권한 필요) \n");
        sb.append("`!탈퇴 {name}` 탈퇴한 회원 추가, 전적검색제외 \n");
        sb.append("`!복귀 {name}` 탈퇴한 회원 복구, 전잭검색포함 \n");
        sb.append("`!부캐저장 {부캐닉/본캐닉}` 부캐닉네임 등록, 데이터저장할때 부캐닉네임은 본캐닉네임으로 변경되서 저장 \n");
        sb.append("");
        builder.setTitle("help");
        builder.setDescription(sb.toString());
        return builder;
    }

    private String makeStats(String prefix, int totalCount, int win, int lose, float win_rate, float kda){
        String stats = prefix + " - " + totalCount+"전 "+win+"승/"+lose+"패  "+win_rate+"% 승률";
        if(kda != 9999){
            stats += " KDA: " + kda;
        }
        stats += "\n";
        return stats;
    }

    private String makeTeamStats(String riotName, int win, int lose, float win_rate){
        return riotName + ": " + win +"승/" + lose + "패 " + win_rate+ "%\n";
    }
}

