package net.lucypoulton.identities.discord;

import net.lucypoulton.identities.Identities;
import net.lucypoulton.identities.IdentitiesPlatform;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.kyori.adventure.text.Component;
import net.lucypoulton.squirtgun.command.condition.Condition;
import net.lucypoulton.squirtgun.command.node.NodeBuilder;
import net.lucypoulton.squirtgun.command.node.subcommand.SubcommandNode;
import net.lucypoulton.squirtgun.discord.DiscordFormatProvider;
import net.lucypoulton.squirtgun.discord.command.DiscordConditions;
import net.lucypoulton.squirtgun.platform.scheduler.Task;
import org.jetbrains.annotations.NotNull;

public class IdentitiesDiscord extends Identities {

    private final JDA jda;

    private void cleanRoles() {
        getPlatform().getLogger().info("Cleaning roles");
        for (Guild guild : jda.getGuilds()) {
            getPlatform().getLogger().info("Cleaning roles for guild " + guild.getName());
            guild.getRoleCache().parallelStreamUnordered()
                .filter(role -> role.getName().startsWith("Identities ")
                    && guild.getMembers().stream().noneMatch(member -> member.getRoles().contains(role)))
                .forEach(role -> {
                    role.delete().queue();
                    getPlatform().getLogger().info("Deleting unused role " + role.getName());
                });
        }
    }


    private void updateRoles() {
        getPlatform().getLogger().info("Updating roles");
        for (Guild guild : jda.getGuilds()) {
            getPlatform().getLogger().info("Deleting roles for guild " + guild.getName());
            for (Role role : guild.getRoleCache()) {
                if (role.getName().startsWith("Identities ")) {
                    role.delete().complete();
                }
            }
        }

        // TODO actually update roles
        getPlatform().getLogger().info("Updated roles successfully");
    }

    public IdentitiesDiscord(@NotNull IdentitiesPlatform platform, JDA jda) {
        super(platform);
        this.jda = jda;

        SubcommandNode node = SubcommandNode.withHelp("ideda",
            "Identities Discord admin commands",
            DiscordConditions.DISCORD_USER.and(DiscordConditions.discordPermission(Permission.ADMINISTRATOR)),
            new NodeBuilder<>()
                .name("clean")
                .description("Cleans all roles")
                .condition(Condition.alwaysTrue())
                .executes(ctx -> {
                    cleanRoles();
                    return Component.text("Cleaning roles");
                })
                .build(),
            new NodeBuilder<>()
                .name("update")
                .description("Deletes and resets all identity roles")
                .condition(Condition.alwaysTrue())
                .executes(ctx -> {
                    updateRoles();
                    return Component.text("Updating roles");
                }).build()
        );

        platform.registerCommand(node, DiscordFormatProvider.INSTANCE);

        platform.getTaskScheduler().start(Task.builder()
            .action(this::cleanRoles)
            .interval(36000)
            .build());
    }
}
