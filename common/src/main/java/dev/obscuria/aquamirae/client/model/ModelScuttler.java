package dev.obscuria.aquamirae.client.model;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.AquamiraeClient;
import dev.obscuria.aquamirae.common.entity.Scuttler;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.loading.math.MathParser;
import software.bernie.geckolib.model.DefaultedGeoModel;

public class ModelScuttler extends DefaultedGeoModel<Scuttler> {

    public ModelScuttler() {
        super(Aquamirae.id("scuttler"));
    }

    @Override
    public void applyMolangQueries(AnimationState<Scuttler> state, double animTime) {
        super.applyMolangQueries(state, animTime);
        MathParser.setVariable(
                AquamiraeClient.MOLANG_SCUTTLER_WALK,
                () -> molangWalkSpeed(state.getAnimatable()));
    }

    @Override
    protected String subtype() {
        return "entity";
    }

    private static double molangWalkSpeed(Scuttler scuttler) {
        return scuttler.walkAnimation.position(AquamiraeClient.partialTicks())
                * 0.25;// * Math.pow(scuttler.getSizeVariant(), -0.5);
    }
}
