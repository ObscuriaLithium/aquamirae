package dev.obscuria.aquamirae.api.common.ability;

import com.mojang.serialization.Codec;
import dev.obscuria.aquamirae.api.common.TooltipImageProvider;
import dev.obscuria.aquamirae.registry.AquamiraeRegistries;
import dev.obscuria.fragmentum.content.registry.Deferred;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record AbilitySetComponent(Holder<AbilitySet> abilitySet) implements TooltipImageProvider {

    public static final Codec<AbilitySetComponent> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, AbilitySetComponent> STREAM_CODEC;

    public static AbilitySetComponent of(Deferred<AbilitySet, AbilitySet> deferredAbilitySet) {
        return new AbilitySetComponent(deferredAbilitySet.holder());
    }

    @Override
    public TooltipComponent getTooltipImage(ItemStack stack) {
        return new AbilitySetTooltip(stack, abilitySet);
    }

    @Override
    public int priority() {
        return 10;
    }

    static {
        CODEC = AquamiraeRegistries
                .ABILITY_SET.holderByNameCodec()
                .xmap(AbilitySetComponent::new, AbilitySetComponent::abilitySet);
        STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);
    }
}
