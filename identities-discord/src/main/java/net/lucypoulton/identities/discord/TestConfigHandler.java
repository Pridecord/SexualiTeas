package net.lucypoulton.identities.discord;

import net.lucypoulton.identities.config.ConfigHandler;
import net.lucypoulton.identities.config.ConnectionType;
import net.lucypoulton.identities.config.SqlInfoContainer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.lucypoulton.squirtgun.discord.DiscordFormatProvider;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class TestConfigHandler implements ConfigHandler {

    private final Properties properties;

    public TestConfigHandler() throws IOException {

        if (!Files.exists(Path.of("identities.properties"))) {
            File config = new File("identities.properties");

            Objects.requireNonNull(getClass().getResourceAsStream("/identities.properties"))
                .transferTo(new FileOutputStream(config));
        }

        properties = new Properties();
        properties.load(new FileInputStream("identities.properties"));
    }

    public String getDiscordToken() {
        return properties.getProperty("discordToken");
    }

    public String getCommandPrefix() {
        return properties.getProperty("prefix", "-");
    }

    @Override
    public List<String> getPredefinedSets() {
        return List.of();
    }

    @Override
    public List<String> getFilterPatterns() {
        return List.of();
    }

    @Override
    public boolean filterEnabled() {
        return false;
    }

    @Override
    public SqlInfoContainer getSqlConnectionData() {
        return new SqlInfoContainer(properties.getProperty("sql.host", "localhost"),
            Integer.parseInt(properties.getProperty("sql.port", "3306")),
            properties.getProperty("sql.database", "identities"),
            properties.getProperty("sql.user", "identities"),
            properties.getProperty("sql.password"));
    }

    @Override
    public boolean checkForUpdates() {
        return true;
    }

    @Override
    public ConnectionType getConnectionType() {
        return ConnectionType.MYSQL;
    }

    @Override
    public Component formatMain(@NotNull String input, @NotNull TextDecoration[] formatters) {
        return DiscordFormatProvider.INSTANCE.formatMain(input, formatters);
    }

    @Override
    public Component formatAccent(@NotNull String input, @NotNull TextDecoration[] formatters) {
        return DiscordFormatProvider.INSTANCE.formatAccent(input, formatters);
    }

    @Override
    public Component getPrefix() {
        return DiscordFormatProvider.INSTANCE.getPrefix();
    }

    @Override
    public Component formatTitle(String input) {
        return DiscordFormatProvider.INSTANCE.formatTitle(input);
    }

    @Override
    public Component formatFooter(String input) {
        return DiscordFormatProvider.INSTANCE.formatFooter(input);
    }
}
