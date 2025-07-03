package net.lucypoulton.identities.listener;

import net.lucypoulton.identities.Identities;
import net.lucypoulton.identities.config.ConnectionType;
import net.lucypoulton.identities.storage.MysqlFileStorage;
import net.lucypoulton.squirtgun.platform.Platform;
import net.lucypoulton.squirtgun.platform.event.EventHandler;
import net.lucypoulton.squirtgun.platform.event.EventListener;
import net.lucypoulton.squirtgun.platform.event.player.PlayerJoinEvent;
import net.lucypoulton.squirtgun.platform.event.player.PlayerLeaveEvent;
import net.lucypoulton.squirtgun.platform.scheduler.Task;

import java.util.List;

public class JoinLeaveListener implements EventListener {

    private final Identities plugin;

    public JoinLeaveListener(Identities plugin) {
        this.plugin = plugin;
    }

    private void onJoin(PlayerJoinEvent event) {
        if (plugin.getConfigHandler().getConnectionType() == ConnectionType.MYSQL) {
            Task.builder()
                .async()
                .action((Platform ignored) -> {
                    MysqlFileStorage storage = (MysqlFileStorage) plugin.getPlatform().getStorage();
                    storage.getIdentities(event.player().getUuid(), false);
                })
                .build().execute(plugin.getPlatform());
        }
    }

    private void onLeave(PlayerLeaveEvent event) {
        if (plugin.getConfigHandler().getConnectionType() == ConnectionType.MYSQL) {
            MysqlFileStorage storage = (MysqlFileStorage) plugin.getPlatform().getStorage();
            storage.onPlayerDisconnect(event.player().getUuid());
        }
    }

    private final List<EventHandler<?>> handlers = List.of(
        EventHandler.executes(PlayerJoinEvent.class, this::onJoin),
        EventHandler.executes(PlayerLeaveEvent.class, this::onLeave)
    );

    @Override
    public List<EventHandler<?>> handlers() {
        return handlers;
    }
}
