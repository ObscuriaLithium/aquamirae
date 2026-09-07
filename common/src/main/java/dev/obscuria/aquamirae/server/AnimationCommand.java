package dev.obscuria.aquamirae.server;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.common.entity.animation.IAnimatable;
import lombok.experimental.UtilityClass;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;

@UtilityClass
public final class AnimationCommand {

    public static void register(
            CommandDispatcher<CommandSourceStack> dispatcher,
            CommandBuildContext context,
            Commands.CommandSelection selection
    ) {
        dispatcher.register(Commands.literal(Aquamirae.MOD_ID).requires(source -> source.hasPermission(3))
                .then(Commands.literal("animation")
                        .then(Commands.argument("target", EntityArgument.entity())
                                .then(Commands.literal("play")
                                        .then(Commands.argument("name", StringArgumentType.word())
                                                .executes(AnimationCommand::playAnimation)))
                                .then(Commands.literal("stop")
                                        .then(Commands.argument("name", StringArgumentType.word())
                                                .executes(AnimationCommand::stopAnimation))))));
    }

    private static int playAnimation(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
        var target = EntityArgument.getEntity(command, "target");
        var name = StringArgumentType.getString(command, "name");
        if (!(target instanceof IAnimatable animatable)) return 0;
        var state = animatable.getAnimationController().find(name);
        if (state.isEmpty()) return 0;
        state.get().play(target);
        return 1;
    }

    private static int stopAnimation(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
        var target = EntityArgument.getEntity(command, "target");
        var name = StringArgumentType.getString(command, "name");
        if (!(target instanceof IAnimatable animatable)) return 0;
        var state = animatable.getAnimationController().find(name);
        if (state.isEmpty()) return 0;
        state.get().stop(target);
        return 1;
    }
}
