package net.lucypoulton.identities.command.arguments;

import com.google.common.base.Splitter;
import net.lucypoulton.identities.api.IdentityHandler;
import net.lucypoulton.identities.api.set.IdentitySet;
import net.lucypoulton.squirtgun.command.argument.CommandArgument;
import net.lucypoulton.squirtgun.command.context.CommandContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class IdentitySetArgument implements CommandArgument<Set<IdentityHandler.ParseResult>> {

    private final IdentityHandler handler;

    public IdentitySetArgument(IdentityHandler handler) {
        this.handler = handler;
    }

    @Override
    public @NotNull String getName() {
        return "set";
    }

    @Override
    public String getDescription() {
        return "A list of identity sets.";
    }

    @Override
    public Set<IdentityHandler.ParseResult> getValue(Queue<String> args, CommandContext context) {
        Set<IdentityHandler.ParseResult> results = new LinkedHashSet<>();
        for (String arg : args) {
            results.add(handler.parse(arg, !context.getTarget().hasPermission("identities.bypass")));
        }
        return results.size() == 0 ? null : results;
    }

    @Override
    public @Nullable List<String> tabComplete(Queue<String> args, CommandContext context) {
        // Always suggest all identity set names for tab completion
        return handler.getAllIdentities().stream()
                .map(IdentitySet::nameForConcatenation)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    public String toString() {
        return "<identities> [identities]...";
    }
}
