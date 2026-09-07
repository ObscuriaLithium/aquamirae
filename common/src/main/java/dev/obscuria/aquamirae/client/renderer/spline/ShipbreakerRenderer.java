package dev.obscuria.aquamirae.client.renderer.spline;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.model.ModelShipbreakerHead;
import dev.obscuria.aquamirae.common.event.ShipbreakerBody;
import dev.obscuria.aquamirae.common.SplineBody;
import dev.obscuria.fragmentum.content.util.easing.Easing;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ShipbreakerRenderer extends SplineBodyRenderer<ShipbreakerBody, BodyDecoration> {

    public ShipbreakerRenderer() {
        unshaded(true);
        defineWidth(1f, Easing.FLOOR.mergeOut(Easing.EASE_IN_CUBIC, 0.03f));
        defineBodyTexture(Aquamirae.id("textures/entity/shipbreaker.png"), 32);
        defineCrestVisual(CrestVisual.symmetric(
                Aquamirae.id("textures/entity/shipbreaker_crest.png"), 10.0f,
                Easing.EASE_OUT_ELASTIC.scale(2f).mergeOut(Easing.EASE_IN_CUBIC, 0.5f)));
        addRenderLayer(SplineBodyRenderer.renderLayer(
                Aquamirae.id("textures/entity/shipbreaker_overlay.png"),
                RenderType::eyes,
                32f,
                0xFFFFFFFF));
        addDecoration(5, new ModelShipbreakerHead(ModelShipbreakerHead.createBodyLayer().bakeRoot()));
    }

    public void render(
            PoseStack pose,
            ShipbreakerBody body,
            ClientLevel level,
            Frustum frustum,
            Camera camera,
            MultiBufferSource.BufferSource source,
            float partialTicks
    ) {
        Vec3 cam = camera.getPosition();

        pose.pushPose();
        pose.translate(-cam.x, -cam.y, -cam.z);

        List<SplineBody.BakedRing> rings = body.getBakedRings(partialTicks);

        for (int i = 0; i < rings.size() - 1; i++) {

            SplineBody.BakedRing first = rings.get(i);
            SplineBody.BakedRing second = rings.get(i + 1);

            if (!isSegmentVisible(frustum, first, second)) continue;
            renderSegment(level, pose, body, first, second, source, OverlayTexture.NO_OVERLAY);

            for (var entry : decorations.entrySet()) {
                BodyDecoration decoration = entry.getValue();
                if (i != entry.getKey()) continue;

                pushToRing(pose, first, () -> {
                    float widthFactor = widthAt(first.getTween());
                    BodyDecoration.Context context = new BodyDecoration.Context(
                            level, Vec3.ZERO.toVector3f(), body, first, widthFactor);
                    int packedLight = packedLightAt(level, first.getPosition());
                    decoration.render(context, pose, source,
                            OverlayTexture.NO_OVERLAY, packedLight, partialTicks);
                });
            }
        }

        pose.popPose();
    }

    private boolean isSegmentVisible(Frustum frustum, SplineBody.BakedRing first, SplineBody.BakedRing second) {
        return frustum.isVisible(aabbOf(first, 10f)) || frustum.isVisible(aabbOf(second, 10f));
    }

    private AABB aabbOf(SplineBody.BakedRing ring, float radius) {
        return new AABB(
                ring.getPosition().x - radius,
                ring.getPosition().y - radius,
                ring.getPosition().z - radius,
                ring.getPosition().x + radius,
                ring.getPosition().y + radius,
                ring.getPosition().z + radius);
    }
}