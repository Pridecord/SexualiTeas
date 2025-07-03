package net.lucypoulton.identities.listener;

import net.kyori.adventure.text.Component;
import net.lucypoulton.identities.IdentitiesPlatform;
import net.lucypoulton.squirtgun.format.FormatProvider;
import net.lucypoulton.squirtgun.platform.event.EventHandler;
import net.lucypoulton.squirtgun.platform.event.EventListener;

import java.util.List;

public class FilteredSetAttemptListener implements EventListener {

    private final List<EventHandler<?>> handlers;

    public FilteredSetAttemptListener(IdentitiesPlatform platform) {
        handlers = List.of(EventHandler.executes(FilteredSetAttemptEvent.class, e -> {
            FormatProvider fmt = platform.getConfigHandler();
            Component message = fmt.getPrefix().append(fmt.formatMain("Player "))
                .append(fmt.formatAccent(e.player().getUsername()))
                .append(fmt.formatMain(" tried to use filtered set "))
                .append(fmt.formatAccent(e.blockedInput()));
            platform.getOnlinePlayers().stream()
                .filter(p -> p.hasPermission("identitys.admin"))
                .forEach(p -> p.sendMessage(message));
        }));
    }


    @Override
    public List<EventHandler<?>> handlers() {
        return handlers;
    }
}
