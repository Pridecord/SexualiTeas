package net.lucypoulton.identities;

import com.google.common.io.CharStreams;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextDecoration;
import net.lucypoulton.identities.api.IdentityHandler;
import net.lucypoulton.identities.command.ClearOtherNode;
import net.lucypoulton.identities.command.ClearIdentitiesNode;
import net.lucypoulton.identities.command.ListIdentitiesNode;
import net.lucypoulton.identities.command.PreviewNode;
import net.lucypoulton.identities.command.SetOtherNode;
import net.lucypoulton.identities.command.SetIdentitiesNode;
import net.lucypoulton.identities.command.ShowIdentitiesNode;
import net.lucypoulton.identities.config.ConfigHandler;
import net.lucypoulton.identities.listener.FilteredSetAttemptListener;
import net.lucypoulton.identities.listener.JoinLeaveListener;
import net.lucypoulton.identities.provider.BuiltinIdentityProvider;
import net.lucypoulton.squirtgun.command.condition.Condition;
import net.lucypoulton.squirtgun.command.node.CommandNode;
import net.lucypoulton.squirtgun.command.node.NodeBuilder;
import net.lucypoulton.squirtgun.command.node.PluginInfoNode;
import net.lucypoulton.squirtgun.command.node.subcommand.SubcommandNode;
import net.lucypoulton.squirtgun.platform.audience.PermissionHolder;
import net.lucypoulton.squirtgun.platform.event.EventHandler;
import net.lucypoulton.squirtgun.platform.event.PluginReloadEvent;
import net.lucypoulton.squirtgun.plugin.SquirtgunPlugin;
import net.lucypoulton.squirtgun.update.PolymartUpdateChecker;
import net.lucypoulton.squirtgun.util.SemanticVersion;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class Identities extends SquirtgunPlugin<IdentitiesPlatform> {

    private IdentityHandlerImpl identityHandler;

    private final SemanticVersion version;

    public Identities(@NotNull IdentitiesPlatform platform) {
        super(platform);
        String version;
        try (InputStream stream = Identities.class.getResourceAsStream("/identities-version.txt")) {
            version = CharStreams.toString(new InputStreamReader(Objects.requireNonNull(stream)));
        } catch (IOException e) {
            e.printStackTrace();
            version = "ERROR - see console";
        }
        this.version = SemanticVersion.parse(version);
    }

    @Override
    public @NotNull String getPluginName() {
        return "Identities";
    }

    @Override
    public @NotNull SemanticVersion getPluginVersion() {
        return version;
    }

    @Override
    public @NotNull String[] getAuthors() {
        return new String[]{"__lucyy"};
    }


    @Override
    public void onEnable() {
        identityHandler = new IdentityHandlerImpl(this, getPlatform().getStorage());


        identityHandler.registerProvider(new BuiltinIdentityProvider());
        getPlatform().getEventManager().register(EventHandler.executes(PluginReloadEvent.class, e -> getPlatform().reloadConfig()));

        final CommandNode<PermissionHolder> rootNode = SubcommandNode.withHelp("identities",
            "Identities root command",
            Condition.alwaysTrue(),
            new SetIdentitiesNode(this),
            new ShowIdentitiesNode(getPlatform(), getIdentityHandler()),
            new PreviewNode(this),
            new ClearIdentitiesNode(identityHandler),
            new ListIdentitiesNode(identityHandler),
            new PluginInfoNode("version", this),
            new SetOtherNode(this),
            new ClearOtherNode(this),
            new NodeBuilder<>()
                .name("reload")
                .description("Reloads the plugin.")
                .condition(Condition.hasPermission("identities.admin"))
                .executes(x -> {
                    reload();
                    return x.getFormat().getPrefix().append(x.getFormat().formatMain("Reloaded"));
                }).build(),
            SubcommandNode.withHelp("cloud",
                "Cloud admin commands",
                Condition.hasPermission("identities.cloud")
            )
        );
        getPlatform().registerCommand(rootNode, getConfigHandler());

        if (getConfigHandler().checkForUpdates()) {
            new PolymartUpdateChecker(this,
                921,
                getConfigHandler().getPrefix()
                    .append(getConfigHandler().formatMain("A new version of Identities is available!\nUnfortunately, the original creator's website is down. Here's the link anyways: "))
                    .append(getConfigHandler().formatAccent("https://lucyy.me/identities",
                            new TextDecoration[]{TextDecoration.UNDERLINED})
                        .clickEvent(ClickEvent.openUrl("https://lucyy.me/identities"))),
                "identities.admin");
        } else {
            getPlatform().getLogger().warning("Update checking is disabled. You might be running an old version!");
        }

        getPlatform().getEventManager().register(new JoinLeaveListener(this));
        getPlatform().getEventManager().register(new FilteredSetAttemptListener(this.getPlatform()));
    }

    public IdentityHandler getIdentityHandler() {
        return identityHandler;
    }

    public ConfigHandler getConfigHandler() {
        return getPlatform().getConfigHandler();
    }
}
