package com.stats.lolgg.login;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.stats.lolgg.bot.ReadyListener;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

@Component
public class Login implements CommandLineRunner {

    private final ReadyListener readyListener;

    public Login(ReadyListener readyListener) {
        this.readyListener = readyListener;
    }

    @Override
    public void run(String... args) throws Exception {
        final String token = "MTE4NDc0ODY4MjUzMzQwNDczMg.GNPa0H.oRvHxQo25Z8DwJdBiovvwC-DeZjFDzxPe93v48";

        JDA jda = JDABuilder.createDefault(token)
                            .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                            .build();
        
        jda.addEventListener(readyListener);
    }
}
