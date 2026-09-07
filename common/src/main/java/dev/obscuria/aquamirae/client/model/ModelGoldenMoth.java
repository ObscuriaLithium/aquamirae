package dev.obscuria.aquamirae.client.model;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.common.entity.GoldenMoth;
import software.bernie.geckolib.model.DefaultedGeoModel;

public class ModelGoldenMoth extends DefaultedGeoModel<GoldenMoth> {

    public ModelGoldenMoth() {
        super(Aquamirae.id("golden_moth"));
    }

    @Override
    protected String subtype() {
        return "entity";
    }
}
