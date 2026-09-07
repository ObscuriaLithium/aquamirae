package dev.obscuria.aquamirae.integration.mixin;

import dev.obscuria.aquamirae.network.ClientboundLogBookOpenPayload;
import dev.obscuria.aquamirae.registry.AquamiraeItems;
import dev.obscuria.fragmentum.content.network.FragmentumNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LecternBlock.class)
public abstract class MixinLecternBlock {

    @Inject(method = "useWithoutItem", at = @At("HEAD"), cancellable = true)
    private void useWithoutItem$onHead(
            BlockState state, Level level, BlockPos pos, Player player,
            BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir
    ) {
        if (!(level.getBlockEntity(pos) instanceof LecternBlockEntity lectern)) return;
        var book = lectern.getBook();
        if (!book.is(AquamiraeItems.LOGBOOK.asItem())) return;
        if (player instanceof ServerPlayer serverPlayer)
            FragmentumNetworking.sendTo(serverPlayer, new ClientboundLogBookOpenPayload(book));
        cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
    }
}