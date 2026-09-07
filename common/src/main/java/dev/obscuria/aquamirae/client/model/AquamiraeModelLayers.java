package dev.obscuria.aquamirae.client.model;

import dev.obscuria.aquamirae.Aquamirae;
import net.minecraft.client.model.geom.ModelLayerLocation;

public interface AquamiraeModelLayers {

    ModelLayerLocation ABYSSAL_SCYPHOID = create("abyssal_scyphoid");
    ModelLayerLocation SPINEFISH = create("spinefish");
    ModelLayerLocation HANGING_CHEST = create("hanging_chest");
    ModelLayerLocation SHELLBACK_SADDLE = create("shellback_saddle");
    ModelLayerLocation MAW = create("maw");
    ModelLayerLocation ANGLERFISH = create("anglerfish");
    ModelLayerLocation MAZE_MOTHER = create("maze_mother");
    ModelLayerLocation CAPTAIN_CORNELIA = create("captain_cornelia");
    ModelLayerLocation EEL = create("eel");
    ModelLayerLocation TORTURED_SOUL = create("tortured_soul");
    ModelLayerLocation TERRIBLE_CHAKRAM = create("terrible_chakram");
    ModelLayerLocation MAZE_ROSE = create("maze_rose");
    ModelLayerLocation ABYSSAL_SHARD = create("abyssal_shard");

    private static ModelLayerLocation create(String name) {
        return new ModelLayerLocation(Aquamirae.id(name), "main");
    }
}
