package com.stats.lolgg.login;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.stats.lolgg.bot.ReadyListener;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

/**
 * login 첫 기동시 JDA Build
 * @author codingpoppy94
 * @version 1.0
 */
@Configuration
public class Login {

    private final ReadyListener readyListener;

    public Login(ReadyListener readyListener) {
        this.readyListener = readyListener;
    }

    @Bean
    public JDA jda() throws Exception {
        final String token = "test-token";

        return JDABuilder.createDefault(token)
                            .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                            .addEventListeners(readyListener)
                            .build();
    }
}