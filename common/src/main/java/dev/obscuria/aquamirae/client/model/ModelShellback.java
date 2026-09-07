package dev.obscuria.aquamirae.client.model;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.AquamiraeClient;
import dev.obscuria.aquamirae.common.entity.Shellback;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.loading.math.MathParser;
import software.bernie.geckolib.model.DefaultedGeoModel;

public class ModelShellback extends DefaultedGeoModel<Shellback> {

    public ModelShellback() {
        super(Aquamirae.id("shellback"));
    }

    @Override
    public void applyMolangQueries(AnimationState<Shellback> state, double animTime) {
        super.applyMolangQueries(state, animTime);
        MathParser.setVariable(
                AquamiraeClient.MOLANG_SCUTTLER_WALK,
                () -> molangWalkSpeed(state.getAnimatable()));
    }

    @Override
    protected String subtype() {
        return "entity";
    }

    private static double molangWalkSpeed(Shellback shellback) {
        return shellback.walkAnimation.position(AquamiraeClient.partialTicks()) * 0.1;
    }
}
