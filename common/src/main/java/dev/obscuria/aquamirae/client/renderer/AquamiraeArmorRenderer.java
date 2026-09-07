package dev.obscuria.aquamirae.client.renderer;

import dev.obscuria.aquamirae.client.renderer.entity.layer.ArmorAutoGlowingLayer;
import dev.obscuria.aquamirae.common.item.AquamiraeArmorItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class AquamiraeArmorRenderer extends GeoArmorRenderer<AquamiraeArmorItem> {

    public AquamiraeArmorRenderer(AquamiraeArmorItem armorItem) {
        super(armorItem);
        this.addRenderLayer(new ArmorAutoGlowingLayer(this));
    }

    @Override
    protected void applyBoneVisibilityBySlot(EquipmentSlot slot) {
        super.applyBoneVisibilityBySlot(slot);
        if (slot != EquipmentSlot.LEGS) return;
        HumanoidModel<?> vanillaModel = this;
        setBoneVisible(body, vanillaModel.body.visible);
    }
}
