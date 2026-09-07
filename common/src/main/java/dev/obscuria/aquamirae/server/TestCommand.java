package dev.obscuria.aquamirae.server;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.network.ClientboundCameraShakePayload;
import dev.obscuria.aquamirae.network.ClientboundTestPayload;
import dev.obscuria.aquamirae.server.event.ShipbreakerEvent;
import dev.obscuria.fragmentum.content.network.FragmentumNetworking;
import lombok.experimental.UtilityClass;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

@UtilityClass
public final class TestCommand {

    public static void register(
            CommandDispatcher<CommandSourceStack> dispatcher,
            CommandBuildContext context,
            Commands.CommandSelection selection
    ) {
        dispatcher.register(Commands.literal(Aquamirae.MOD_ID)
                .requires(source -> source.hasPermission(3))
                .then(Commands.literal("test")
                        .then(Commands.literal("earthquake")
                                .then(Commands.argument("target", EntityArgument.player())
                                        .executes(TestCommand::testEarthquake)))
                        .then(Commands.literal("maze-mother-encounter")
                                .then(Commands.argument("target", EntityArgument.player())
                                        .executes(TestCommand::testMazeMotherEncounter)))
                        .then(Commands.literal("camera-shake")
                                .then(Commands.argument("target", EntityArgument.player())
                                        .then(Commands.argument("strength", FloatArgumentType.floatArg(0f))
                                                .then(Commands.argument("duration", FloatArgumentType.floatArg(0f))
                                                        .executes(TestCommand::testCameraShake))))))
                .then(Commands.literal("shipbreaker")
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(TestCommand::triggerShipbreaker))));
    }

    private static int testEarthquake(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
        var target = EntityArgument.getPlayer(command, "target");
        FragmentumNetworking.sendTo(target, ClientboundTestPayload.EARTHQUAKE);
        return 1;
    }

    private static int testMazeMotherEncounter(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
        var target = EntityArgument.getPlayer(command, "target");
        FragmentumNetworking.sendTo(target, ClientboundTestPayload.MAZE_MOTHER_ENCOUNTER);
        return 1;
    }

    private static int testCameraShake(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
        var target = EntityArgument.getPlayer(command, "target");
        var strength = FloatArgumentType.getFloat(command, "strength");
        var duration = FloatArgumentType.getFloat(command, "duration");
        FragmentumNetworking.sendTo(target, new ClientboundCameraShakePayload(strength, duration));
        return 1;
    }

    private static int triggerShipbreaker(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
        var target = EntityArgument.getPlayer(command, "target");
        if (!(target.level() instanceof ServerLevel level)) return 0;
        var event = ShipbreakerEvent.trigger(level, target.position());
        command.getSource().sendSuccess(() -> Component.literal("Shipbreaker event " + event.id + " started at " + target.getName().getString()), true);
        return 1;
    }
}