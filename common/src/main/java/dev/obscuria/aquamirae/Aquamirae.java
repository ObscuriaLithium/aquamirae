package dev.obscuria.aquamirae;

import dev.obscuria.aquamirae.registry.AquamiraeRegistries;
import dev.obscuria.aquamirae.config.CommonConfig;
import dev.obscuria.aquamirae.network.*;
import dev.obscuria.aquamirae.server.AnimationCommand;
import dev.obscuria.aquamirae.server.TestCommand;
import dev.obscuria.fragmentum.content.network.FragmentumNetworking;
import dev.obscuria.fragmentum.server.FragmentumServerRegistry;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;

public final class Aquamirae {

    public static final String MOD_ID = "aquamirae";
    public static final String DISPLAY_NAME = "Aquamirae";
    public static final Logger LOG = LoggerFactory.getLogger(DISPLAY_NAME);
    public static final AquamiraeService SERVICE = ServiceLoader.load(AquamiraeService.class).findFirst().orElseThrow();

    public static ResourceLocation id(String key) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, key);
    }

    public static void init() {
        CommonConfig.init();
        AquamiraeRegistries.init();
        FragmentumServerRegistry.registerCommand(TestCommand::register);
        FragmentumServerRegistry.registerCommand(AnimationCommand::register);
        registerPayloads();
    }

    private static void registerPayloads() {
        var registrar = FragmentumNetworking.registrar(MOD_ID);
        registrar.registerClientbound(
                ClientboundTestPayload.class,
                ClientboundTestPayload.TYPE,
                ClientboundTestPayload.STREAM_CODEC,
                ClientboundTestPayload::handle);
        registrar.registerClientbound(
                ClientboundCameraShakePayload.class,
                ClientboundCameraShakePayload.TYPE,
                ClientboundCameraShakePayload.STREAM_CODEC,
                ClientboundCameraShakePayload::handle);
        registrar.registerClientbound(
                ClientboundShipbreakerAddPayload.class,
                ClientboundShipbreakerAddPayload.TYPE,
                ClientboundShipbreakerAddPayload.STREAM_CODEC,
                ClientboundShipbreakerAddPayload::handle);
        registrar.registerClientbound(
                ClientboundShipbreakerRemovePayload.class,
                ClientboundShipbreakerRemovePayload.TYPE,
                ClientboundShipbreakerRemovePayload.STREAM_CODEC,
                ClientboundShipbreakerRemovePayload::handle);
        registrar.registerClientbound(
                ClientboundAnimationPlayPayload.class,
                ClientboundAnimationPlayPayload.TYPE,
                ClientboundAnimationPlayPayload.STREAM_CODEC,
                ClientboundAnimationPlayPayload::handle);
        registrar.registerClientbound(
                ClientboundAnimationStopPayload.class,
                ClientboundAnimationStopPayload.TYPE,
                ClientboundAnimationStopPayload.STREAM_CODEC,
                ClientboundAnimationStopPayload::handle);
        registrar.registerClientbound(
                ClientboundLogBookOpenPayload.class,
                ClientboundLogBookOpenPayload.TYPE,
                ClientboundLogBookOpenPayload.STREAM_CODEC,
                ClientboundLogBookOpenPayload::handle);
        registrar.registerClientbound(
                ClientboundTravelPayload.class,
                ClientboundTravelPayload.TYPE,
                ClientboundTravelPayload.STREAM_CODEC,
                ClientboundTravelPayload::handle);
    }
}
