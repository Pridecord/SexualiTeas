package net.lucypoulton.identities.discord;

import net.lucypoulton.identities.IdentitiesPlatform;
import net.lucypoulton.identities.api.set.IdentitySet;
import net.lucypoulton.identities.config.ConfigHandler;
import net.lucypoulton.identities.storage.Storage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.lucypoulton.squirtgun.discord.standalone.StandaloneDiscordPlatform;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class IdentitiesDiscordPlatform extends StandaloneDiscordPlatform implements IdentitiesPlatform {

    private final ConfigHandler handler;
    private final IdentitiesDiscordStandalone host;

    public IdentitiesDiscordPlatform(JDA jda, TestConfigHandler handler, IdentitiesDiscordStandalone host) {
        super(jda, handler.getCommandPrefix());
        this.handler = handler;
        this.host = host;
    }

    @Override
    public ConfigHandler getConfigHandler() {
        return handler;
    }

    @Override
    public Storage getStorage() {
        return host.getStorage();
    }

    @Override
    public void reloadConfig() {

    }

    public void setRoles(UUID uuid, Set<IdentitySet> sets) {
        User user = Objects.requireNonNull(getPlayer(uuid)).discordUser();

        for (Guild guild : user.getMutualGuilds()) {

            Member member = Objects.requireNonNull(guild.getMember(user));

            for (Role role : member.getRoles()) {
                if (role.getName().startsWith("Identities ")) {
                    guild.removeRoleFromMember(member, role).queue();
                    break;
                }
            }

            String setName = IdentitySet.format(sets);

            Role matchingRole = guild.getRolesByName("Identities " + setName, false).stream()
                .filter(role -> role.getPermissionsRaw() == 0)
                .findFirst().orElseGet(() ->
                    guild.createRole().setName("Identities " + setName).setPermissions(0L).complete()
                );

            guild.addRoleToMember(member, matchingRole).queue();
        }
    }
}
