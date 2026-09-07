package dev.obscuria.aquamirae.client;

import dev.obscuria.aquamirae.Aquamirae;
import dev.obscuria.aquamirae.client.model.*;
import dev.obscuria.aquamirae.client.particle.GhostParticle;
import dev.obscuria.aquamirae.client.particle.GhostShineParticle;
import dev.obscuria.aquamirae.client.particle.ShineParticle;
import dev.obscuria.aquamirae.client.renderer.entity.*;
import dev.obscuria.aquamirae.client.renderer.entity.projectile.AbyssalShardRenderer;
import dev.obscuria.aquamirae.client.screen.LogBookViewScreen;
import dev.obscuria.aquamirae.client.screen.tooltip.ClientAbilitySetTooltip;
import dev.obscuria.aquamirae.api.common.ability.AbilitySetTooltip;
import dev.obscuria.aquamirae.client.screen.tooltip.ClientKeepsakeTooltip;
import dev.obscuria.aquamirae.client.screen.tooltip.ClientRuneTooltip;
import dev.obscuria.aquamirae.client.sound.RelativeSoundInstance;
import dev.obscuria.aquamirae.common.component.Keepsake;
import dev.obscuria.aquamirae.common.component.RuneDamage;
import dev.obscuria.aquamirae.common.component.RuneProtection;
import dev.obscuria.aquamirae.config.ClientConfig;
import dev.obscuria.aquamirae.registry.AquamiraeDataComponents;
import dev.obscuria.aquamirae.registry.AquamiraeEntityTypes;
import dev.obscuria.aquamirae.registry.AquamiraeParticleTypes;
import dev.obscuria.fragmentum.client.FragmentumClientRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.loading.math.MathParser;
import software.bernie.geckolib.loading.math.value.Variable;

public final class AquamiraeClient {

    public static final String MOLANG_SCUTTLER_WALK;
    public static final PanoramaRenderer PANORAMA;
    public static final ResourceLocation PIRATE_PILLAGER_TEXTURE;
    public static final ResourceLocation PIRATE_VINDICATOR_TEXTURE;

    public static float partialTicks() {
        return Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
    }

    public static void animateCritEffect(Entity entity) {
        Minecraft.getInstance().particleEngine.createTrackingEmitter(entity, ParticleTypes.CRIT);
    }

    public static void playRelativeSound(SoundEvent soundEvent, Vec3 pos, float volume, float pitch) {
        Minecraft.getInstance().getSoundManager().play(new RelativeSoundInstance(soundEvent, pos, volume, pitch));
    }

    public static void openLogbookScreen(ItemStack stack) {
        Minecraft.getInstance().setScreen(new LogBookViewScreen(stack));
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1f, 2f));
    }

    public static void init() {

        ClientConfig.init();
        var registrar = FragmentumClientRegistry.registrar(Aquamirae.MOD_ID);

        FragmentumClientRegistry.registerTooltipComponent(AbilitySetTooltip.class, ClientAbilitySetTooltip::create);
        FragmentumClientRegistry.registerTooltipComponent(RuneDamage.class, ClientRuneTooltip::create);
        FragmentumClientRegistry.registerTooltipComponent(RuneProtection.class, ClientRuneTooltip::create);
        FragmentumClientRegistry.registerTooltipComponent(Keepsake.class, ClientKeepsakeTooltip::create);

        registrar.registerModelLayer(AquamiraeModelLayers.ABYSSAL_SCYPHOID, ModelAbyssalScyphoid::createBodyLayer);
        registrar.registerModelLayer(AquamiraeModelLayers.SPINEFISH, ModelSpinefish::createBodyLayer);
        registrar.registerModelLayer(AquamiraeModelLayers.HANGING_CHEST, ModelHangingChest::createBodyLayer);
        registrar.registerModelLayer(AquamiraeModelLayers.SHELLBACK_SADDLE, ModelShellbackSaddle::createBodyLayer);
        registrar.registerModelLayer(AquamiraeModelLayers.MAW, ModelMaw::createBodyLayer);
        registrar.registerModelLayer(AquamiraeModelLayers.ANGLERFISH, ModelAnglerfish::createBodyLayer);
        registrar.registerModelLayer(AquamiraeModelLayers.MAZE_MOTHER, ModelMazeMother::createBodyLayer);
        registrar.registerModelLayer(AquamiraeModelLayers.CAPTAIN_CORNELIA, ModelCaptainCornelia::createBodyLayer);
        registrar.registerModelLayer(AquamiraeModelLayers.EEL, ModelEel::createBodyLayer);
        registrar.registerModelLayer(AquamiraeModelLayers.TORTURED_SOUL, ModelTorturedSoul::createBodyLayer);
        registrar.registerModelLayer(AquamiraeModelLayers.TERRIBLE_CHAKRAM, ModelTerribleChakram::createBodyLayer);
        registrar.registerModelLayer(AquamiraeModelLayers.MAZE_ROSE, ModelMazeRose::createBodyLayer);
        registrar.registerModelLayer(AquamiraeModelLayers.ABYSSAL_SHARD, ModelShard::createBodyLayer);

        registrar.registerEntityRenderer(AquamiraeEntityTypes.SCUTTLER, ScuttlerRenderer::new);
        registrar.registerEntityRenderer(AquamiraeEntityTypes.SHELLBACK, ShellbackRenderer::new);
        registrar.registerEntityRenderer(AquamiraeEntityTypes.GOLDEN_MOTH, GoldenMothRenderer::new);
        registrar.registerEntityRenderer(AquamiraeEntityTypes.MAW, MawRenderer::new);
        registrar.registerEntityRenderer(AquamiraeEntityTypes.ABYSSAL_SCYPHOID, AbyssalScyphoidRenderer::new);
        registrar.registerEntityRenderer(AquamiraeEntityTypes.SPINEFISH, SpinefishRenderer::new);
        registrar.registerEntityRenderer(AquamiraeEntityTypes.ANGLERFISH, AnglerfishRenderer::new);
        registrar.registerEntityRenderer(AquamiraeEntityTypes.MAZE_MOTHER, MazeMotherRenderer::new);
        registrar.registerEntityRenderer(AquamiraeEntityTypes.CAPTAIN_CORNELIA, CaptainCorneliaRenderer::new);
        registrar.registerEntityRenderer(AquamiraeEntityTypes.EEL, EelRenderer::new);
        registrar.registerEntityRenderer(AquamiraeEntityTypes.TORTURED_SOUL, TorturedSoulRenderer::new);
        registrar.registerEntityRenderer(AquamiraeEntityTypes.TERRIBLE_CHAKRAM, TerribleChakramRenderer::new);
        registrar.registerEntityRenderer(AquamiraeEntityTypes.MAZE_ROSE, MazeRoseRenderer::new);
        registrar.registerEntityRenderer(AquamiraeEntityTypes.ABYSSAL_SHARD, AbyssalShardRenderer::new);

        registrar.registerTexturedParticleRenderer(AquamiraeParticleTypes.SHINE, ShineParticle.Provider::new);
        registrar.registerTexturedParticleRenderer(AquamiraeParticleTypes.GHOST_SHINE, GhostShineParticle.Provider::new);
        registrar.registerTexturedParticleRenderer(AquamiraeParticleTypes.GHOST, GhostParticle.Provider::new);

        registrar.registerItemProperty(Aquamirae.id("echo_compass/angle"), echoCompassAngle());

        MathParser.registerVariable(new Variable(MOLANG_SCUTTLER_WALK, 0.0f));
    }

    private static ClampedItemPropertyFunction echoCompassAngle() {
        return new CompassItemPropertyFunction((level, stack, entity) -> stack.get(AquamiraeDataComponents.ECHO_TRACKER));
    }

    static {
        MOLANG_SCUTTLER_WALK = "aquamirae.scuttler_walk";
        PANORAMA = new PanoramaRenderer(new CubeMap(Aquamirae.id("textures/gui/title/panorama")));
        PIRATE_PILLAGER_TEXTURE = Aquamirae.id("textures/entity/pirate_pillager.png");
        PIRATE_VINDICATOR_TEXTURE = Aquamirae.id("textures/entity/pirate_vindicator.png");
    }
}
