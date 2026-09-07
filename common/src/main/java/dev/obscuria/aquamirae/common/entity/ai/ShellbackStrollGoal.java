package dev.obscuria.aquamirae.common.entity.ai;

import dev.obscuria.aquamirae.common.entity.Shellback;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Player;

public class ShellbackStrollGoal extends RandomStrollGoal {

    protected final Shellback shellback;

    public ShellbackStrollGoal(Shellback shellback, double speedModifier) {
        super(shellback, speedModifier, 600);
        this.shellback = shellback;
    }

    @Override
    public boolean canUse() {
        return !shellback.isInventoryOpen()
                && super.canUse()
                && !hasPlayerAbove();
    }

    @Override
    public boolean canContinueToUse() {
        return !shellback.isInventoryOpen()
                && super.canContinueToUse()
                && !hasPlayerAbove();
    }

    protected boolean hasPlayerAbove() {
        var areaToCheck = shellback.getBoundingBox().move(0, shellback.getBbHeight(), 0);
        return !shellback.level().getEntitiesOfClass(Player.class, areaToCheck).isEmpty();
    }
}
