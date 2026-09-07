package dev.obscuria.aquamirae.api.common.entity;

import lombok.Getter;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;

public abstract class SubPartEntity<T extends Entity> extends Entity {

    @Getter private final T parent;

    public SubPartEntity(T parent) {
        super(parent.getType(), parent.level());
        this.parent = parent;
    }

    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        throw new UnsupportedOperationException();
    }
}