package com.bbn.BBNBot.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.Instant;

public class MemberLeaveListener extends ListenerAdapter {
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        if (!event.getMember().getUser().isBot()) {
            if (event.getMember().getUser().getAvatarId() == null) {
                event.getGuild().getTextChannelById("452789888945750046").sendMessage(new EmbedBuilder()
                        .setTitle("User left")
                        .setAuthor(event.getMember().getUser().getAsTag(), event.getMember().getUser().getDefaultAvatarUrl(), event.getMember().getUser().getDefaultAvatarUrl())
                        .setTimestamp(Instant.now())
                        .setColor(Color.RED)
                        .build()).queue();
            } else {
                event.getGuild().getTextChannelById("452789888945750046").sendMessage(new EmbedBuilder()
                        .setTitle("User left")
                        .setAuthor(event.getMember().getUser().getAsTag(), event.getMember().getUser().getAvatarUrl(), event.getMember().getUser().getAvatarUrl())
                        .setTimestamp(Instant.now())
                        .setColor(Color.RED)
                        .build()).queue();
            }

        } else {
           if (event.getMember().getUser().getAvatarId() == null) {
                event.getGuild().getTextChannelById("452789888945750046").sendMessage(new EmbedBuilder()
                        .setTitle("Bot left")
                        .setAuthor(event.getMember().getUser().getAsTag(), event.getMember().getUser().getDefaultAvatarUrl(), event.getMember().getUser().getDefaultAvatarUrl())
                        .setTimestamp(Instant.now())
                        .setColor(Color.RED)
                        .build()).queue();
            } else {
                event.getGuild().getTextChannelById("452789888945750046").sendMessage(new EmbedBuilder()
                        .setTitle("Bot left")
                        .setAuthor(event.getMember().getUser().getAsTag(), event.getMember().getUser().getAvatarUrl(), event.getMember().getUser().getAvatarUrl())
                        .setTimestamp(Instant.now())
                        .setColor(Color.RED)
                        .build()).queue();
            }
        }
    }
}
