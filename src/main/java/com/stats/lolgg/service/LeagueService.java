package com.stats.lolgg.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.stats.lolgg.mapper.LeagueMapper;
import com.stats.lolgg.model.LeagueStatsVO;
import com.stats.lolgg.model.LeagueVO;

import lombok.RequiredArgsConstructor;

/** 
 * 통계 명령어(!전적 !라인 !장인 !통계) service
 * @author: codingpoppy94
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class LeagueService {

    private final LeagueMapper leagueMapper;

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

    /* insert */

    /* 
     * 부캐닉 저장
     */
    public void saveMappingName(Map<String,Object> paramMap){
        leagueMapper.insertMappingName(paramMap);
    }
    
    /* update */

    // 탈퇴
    public int changeDeleteYN(Map<String,Object> paramMap){
        return leagueMapper.changeDeleteYN(paramMap);
    }

    // 닉네임 변경
    public int changeRiotName(Map<String,Object> paramMap){
        return leagueMapper.changeRiotName(paramMap);
    }
}
