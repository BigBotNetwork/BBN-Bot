/*
 * Copyright 2018-2020 GregTCLTK and Schlauer-Hax
 *
 * Licensed under the MIT License;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bbn.bot.listeners;

import com.bbn.bot.core.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.voice.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoiceLogListener extends ListenerAdapter {

    Config config;
    HashMap<Long, Member> events;

    public VoiceLogListener(Config config) {
        this.config = config;
        this.events = new HashMap<>();
    }

    public void sendMessage(GenericGuildVoiceEvent event) {
        TextChannel c = event.getJDA().getTextChannelById(config.getVoiceChannelID());
        event.getGuild().retrieveMember(event.getMember().getUser()).queue();

        events.put(System.currentTimeMillis(), event.getMember());

        events.forEach((timestamp, member) -> {
                    if (timestamp + 120000 < System.currentTimeMillis()) {
                        events.remove(timestamp, member);
                    }
                }
        );

        int count = 0;
        for (Map.Entry<Long, Member> entry : events.entrySet()) {
            if (entry.getValue().getIdLong() == event.getMember().getIdLong()) count++;
        }

        EmbedBuilder eb = new EmbedBuilder();
        if (event instanceof GuildVoiceMuteEvent)
            eb.setTitle(event.getMember().getUser().getAsTag() + " " + ((!event.getVoiceState().isMuted()) ? "un" : "") + "muted")
                    .setColor(((!event.getVoiceState().isMuted()) ? Color.GREEN : Color.RED));
        else if (event instanceof GuildVoiceDeafenEvent)
            eb.setTitle(event.getMember().getUser().getAsTag() + " " + ((!event.getVoiceState().isDeafened()) ? "un" : "") + "deafed")
                    .setColor(((!event.getVoiceState().isDeafened()) ? Color.GREEN : Color.RED));
        else if (event instanceof GuildVoiceJoinEvent)
            eb.setTitle(event.getMember().getUser().getAsTag() + " joined").setColor(Color.GREEN);
        else if (event instanceof GuildVoiceLeaveEvent)
            eb.setTitle(event.getMember().getUser().getAsTag() + " left")
                    .addField("Channel", ((GuildVoiceLeaveEvent) event).getChannelLeft().getName(), true)
                    .addField("Members in Channel", String.valueOf(((GuildVoiceLeaveEvent) event).getChannelLeft().getMembers().size()), true)
                    .setColor(Color.RED);
        else if (event instanceof GuildVoiceMoveEvent) {
            eb.setAuthor(event.getMember().getUser().getAsTag(), event.getMember().getUser().getAvatarUrl(), event.getMember().getUser().getAvatarUrl())
                    .setTitle(event.getMember().getUser().getAsTag() + " switched channel")
                    .addField("Old Channel", ((GuildVoiceMoveEvent) event).getChannelLeft().getName(), true)
                    .addBlankField(true)
                    .addField("Members in old channel", String.valueOf(((GuildVoiceMoveEvent) event).getChannelLeft().getMembers().size()), true)
                    .setColor(Color.ORANGE);
        }

        eb.addField("Events in last 2 Minutes", String.valueOf(count), true).setAuthor(event.getMember().getUser().getAsTag(), event.getMember().getUser().getAvatarUrl(), event.getMember().getUser().getAvatarUrl())
                .setFooter("Provided by BBN", "https://bigbotnetwork.com/images/avatar.png")
                .setTimestamp(Instant.now());
        if (event.getVoiceState().getChannel() != null)
            eb.addField("Channel", event.getVoiceState().getChannel().getName(), true)
                    .addField("Members in Channel", String.valueOf(event.getVoiceState().getChannel().getMembers().size()), true);
        c.sendMessage(eb.build()).queue();
        if (count > 5) {
            c.sendMessage("Over 5 Events, kick").queue();
            event.getMember().kick("Voicelog").queue();
        }
    }

    @Override
    public void onGuildVoiceDeafen(@Nonnull GuildVoiceDeafenEvent event) {
        this.sendMessage(event);
    }

    @Override
    public void onGuildVoiceMute(@Nonnull GuildVoiceMuteEvent event) {
        this.sendMessage(event);
    }

    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {
        this.sendMessage(event);
    }

    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        this.sendMessage(event);
    }

    @Override
    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent event) {
        this.sendMessage(event);
    }
}
