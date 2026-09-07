package dev.obscuria.aquamirae.client.renderer;

import dev.obscuria.aquamirae.common.item.AquamiraeArmorItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;

public class AquamiraeArmorRendererProvider implements GeoRenderProvider {

    private final AquamiraeArmorRenderer renderer;

    public AquamiraeArmorRendererProvider(AquamiraeArmorItem armorItem) {
        this.renderer = new AquamiraeArmorRenderer(armorItem);
    }

    @Override
    public @Nullable <T extends LivingEntity> HumanoidModel<?> getGeoArmorRenderer(
            @Nullable T livingEntity, ItemStack itemStack,
            @Nullable EquipmentSlot equipmentSlot,
            @Nullable HumanoidModel<T> original
    ) {
        return renderer;
    }
}
