package dev.obscuria.aquamirae.common.entity.ai;

import dev.obscuria.aquamirae.common.entity.Scuttler;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;

import java.util.EnumSet;
import java.util.List;

public class ScuttlerSearchForItemsGoal extends Goal {

    protected final Scuttler scuttler;

    public ScuttlerSearchForItemsGoal(Scuttler scuttler) {
        this.scuttler = scuttler;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!scuttler.getStolenItem().isEmpty()) return false;
        if (!scuttler.canMove()) return false;
        if (scuttler.getLastHurtByMob() != null) return false;
        if (scuttler.getRandom().nextInt(reducedTickDelay(10)) != 0) return false;
        return !searchItems().isEmpty();
    }

    @Override
    public void tick() {

        if (!scuttler.canMove()) {
            scuttler.getNavigation().stop();
            this.stop();
            return;
        }

        if (!scuttler.getStolenItem().isEmpty()) return;
        start();
    }

    @Override
    public void start() {
        var items = searchItems();
        if (items.isEmpty()) return;
        scuttler.getNavigation().moveTo(items.get(0), 1.2);
    }

    protected List<ItemEntity> searchItems() {
        var area = scuttler.getBoundingBox().inflate(16.0, 16.0, 16.0);
        return scuttler.level()
                .getEntitiesOfClass(ItemEntity.class, area)
                .stream()
                .filter(item -> !item.isInWaterOrBubble())
                .toList();
    }
}