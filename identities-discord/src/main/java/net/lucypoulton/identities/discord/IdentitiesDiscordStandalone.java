package net.lucypoulton.identities.discord;

import net.lucypoulton.identities.Identities;
import net.lucypoulton.identities.IdentitiesPlatform;
import net.lucypoulton.identities.storage.MysqlConnectionException;
import net.lucypoulton.identities.storage.MysqlFileStorage;
import net.lucypoulton.identities.storage.Storage;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class IdentitiesDiscordStandalone {

    private final Storage storage;

    public Storage getStorage() {
        return storage;
    }

    public IdentitiesDiscordStandalone() throws LoginException, MysqlConnectionException, IOException {
        TestConfigHandler configHandler = new TestConfigHandler();
        JDA jda = JDABuilder.createDefault(configHandler.getDiscordToken())
            .setChunkingFilter(ChunkingFilter.ALL)
            .setActivity(Activity.listening(configHandler.getCommandPrefix() + "identities - Identities for Discord Beta"))
            .setMemberCachePolicy(MemberCachePolicy.ALL)
            .enableIntents(GatewayIntent.GUILD_MEMBERS)
            .build();
        IdentitiesPlatform platform = new IdentitiesDiscordPlatform(jda, configHandler, this);

        Identities plugin = new IdentitiesDiscord(platform, jda);
        storage = new MysqlFileStorage(plugin);
        plugin.onEnable();
    }

    public static void main(String[] args) throws LoginException, MysqlConnectionException, IOException {
        new IdentitiesDiscordStandalone();
    }
}
