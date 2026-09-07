package dev.obscuria.aquamirae.fabric.client;

import dev.obscuria.aquamirae.client.AquamiraeClient;
import dev.obscuria.aquamirae.registry.AquamiraeBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;

public final class FabricAquamiraeClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AquamiraeClient.init();
        BlockRenderLayerMap.INSTANCE.putBlock(AquamiraeBlocks.WISTERIA_NIVEIS.get(), RenderType.cutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(AquamiraeBlocks.OXYHELIUM.get(), RenderType.cutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(AquamiraeBlocks.GOLDEN_MOTH_JAR.get(), RenderType.cutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(AquamiraeBlocks.FROZEN_CHEST.get(), RenderType.cutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(AquamiraeBlocks.LUMINESCENT_LAMP.get(), RenderType.cutoutMipped());
        BlockRenderLayerMap.INSTANCE.putBlock(AquamiraeBlocks.LUMINESCENT_BUBBLE.get(), RenderType.cutoutMipped());
    }
}
