package com.stats.lolgg.command;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

/**
 * Schedule
 * @author codingpoppy94
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@EnableScheduling
public class ScheduleManager {

    private final UserManager userManager;
    private final JDA jda;

    // // 매 오전 6시 마다
    // @Scheduled(cron="0 0 6 * * *")
    // public void clearUser(){
    //     userManager.userClear();
    //     String channelId = "1238766857289203773";
    //     TextChannel channel = jda.getTextChannelById(channelId);
    //     channel.sendMessage("```오전 6시 초기화 완료```").queue();
    // }

    // // 매 오후 7시 체크 
    // @Scheduled(cron="0 0 19 * * *")
    // public void sendUserCheckMessage(){
    //     String channelId = "1238766857289203773";
    //     TextChannel channel = jda.getTextChannelById(channelId);
    //     channel.sendMessage("```내전 체크해주세요. 9시 시작!```").queue();
    // }
}