package dev.obscuria.aquamirae.common.legacy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

@Deprecated
public final class EntityUtils {

    public static List<LivingEntity> getRelativeEntities(LivingEntity entity, Class<? extends LivingEntity> targets, float forward, float left, float up, float area) {
        return getRelativeEntities(entity, targets, forward, left, up, area, false, false);
    }

    public static List<LivingEntity> getRelativeEntities(LivingEntity entity, Class<? extends LivingEntity> targets, Vec3 pos, float area) {
        return getRelativeEntities(entity, targets, pos, area, false, false);
    }

    public static List<LivingEntity> getRelativeEntities(LivingEntity entity, Class<? extends LivingEntity> targets, float forward, float left, float up, float area, boolean visibleOnly) {
        return getRelativeEntities(entity, targets, forward, left, up, area, visibleOnly, false);
    }

    public static List<LivingEntity> getRelativeEntities(LivingEntity entity, Class<? extends LivingEntity> targets, Vec3 pos, float area, boolean visibleOnly) {
        return getRelativeEntities(entity, targets, pos, area, visibleOnly, false);
    }

    public static List<LivingEntity> getRelativeEntities(LivingEntity entity, Class<? extends LivingEntity> targets, float forward, float left, float up, float area, boolean visibleOnly, boolean circle) {
        return getRelativeEntities(entity, targets, getRelativePos(entity, forward, left, up), area, visibleOnly, circle);
    }

    public static List<LivingEntity> getRelativeEntities(LivingEntity entity, Class<? extends LivingEntity> targets, Vec3 pos, float area, boolean visibleOnly, boolean circle) {
        List<? extends LivingEntity> list = entity.level().getEntitiesOfClass(targets, (new AABB(pos, pos)).inflate((double)area));
        List<LivingEntity> entities = new ArrayList();
        list.forEach((entry) -> {
            if ((!visibleOnly || entity.hasLineOfSight(entry)) && (!circle || pos.distanceTo(entry.position()) <= (double)area)) {
                entities.add(entry);
            }

        });
        return entities;
    }

    public static Vec3 getRelativePos(Entity entity, float forward, float left, float up) {
        Vec2 vec2 = new Vec2(entity.getViewXRot(1.0F), entity.getViewYRot(1.0F));
        Vec3 vec3 = entity.getEyePosition(1.0F);
        float f = Mth.cos((vec2.y + 90.0F) * ((float)Math.PI / 180F));
        float f1 = Mth.sin((vec2.y + 90.0F) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(-vec2.x * ((float)Math.PI / 180F));
        float f3 = Mth.sin(-vec2.x * ((float)Math.PI / 180F));
        float f4 = Mth.cos((-vec2.x + 90.0F) * ((float)Math.PI / 180F));
        float f5 = Mth.sin((-vec2.x + 90.0F) * ((float)Math.PI / 180F));
        Vec3 vec31 = new Vec3((double)(f * f2), (double)f3, (double)(f1 * f2));
        Vec3 vec32 = new Vec3((double)(f * f4), (double)f5, (double)(f1 * f4));
        Vec3 vec33 = vec31.cross(vec32).scale((double)-1.0F);
        double d0 = vec31.x * (double)forward + vec32.x * (double)up + vec33.x * (double)left;
        double d1 = vec31.y * (double)forward + vec32.y * (double)up + vec33.y * (double)left;
        double d2 = vec31.z * (double)forward + vec32.z * (double)up + vec33.z * (double)left;
        return new Vec3(vec3.x + d0, vec3.y + d1, vec3.z + d2);
    }

    public static float getYAngleBetween(Entity first, Entity second) {
        return getYAngleBetween(first.getX(), first.getZ(), second.getX(), second.getZ());
    }

    public static float getYAngleBetween(double x1, double z1, double x2, double z2) {
        double result;
        if (x2 > x1) {
            result = Math.atan2(x2 - x1, z1 - z2) * (double)180.0F / Math.PI;
        } else if (x2 < x1) {
            result = (double)360.0F - Math.atan2(x1 - x2, z1 - z2) * (double)180.0F / Math.PI;
        } else {
            result = Math.atan2((double)0.0F, (double)0.0F);
        }

        return (float)(result + (double)180.0F > (double)360.0F ? result - (double)180.0F : result + (double)180.0F);
    }

    public static void setMotion(Entity entity, Vec3 velocity) {
        entity.setDeltaMovement(velocity);
        entity.hurtMarked = true;
    }

    public static void addMotion(Entity entity, Vec3 velocity) {
        setMotion(entity, entity.getDeltaMovement().add(velocity));
    }

    public static void setMotion(Entity entity, float x, float y, float z) {
        setMotion(entity, new Vec3((double)x, (double)y, (double)z));
    }

    public static void setMotionX(Entity entity, float x) {
        entity.setDeltaMovement((double)x, entity.getDeltaMovement().y, entity.getDeltaMovement().z);
    }

    public static void setMotionY(Entity entity, float y) {
        entity.setDeltaMovement(entity.getDeltaMovement().x, (double)y, entity.getDeltaMovement().z);
    }

    public static void setMotionZ(Entity entity, float z) {
        entity.setDeltaMovement(entity.getDeltaMovement().x, entity.getDeltaMovement().y, (double)z);
    }

    public static void addMotion(Entity entity, float x, float y, float z) {
        addMotion(entity, new Vec3((double)x, (double)y, (double)z));
    }

    public static Projectile shootProjectile(Projectile projectile, Entity entity, float pitch, float yaw, float newPitch, float speed, float spread) {
        Projectile projectileEntity = shootProjectileIgnoreMotion(projectile, entity, pitch, yaw, newPitch, speed, spread);
        Vec3 vec = entity.getDeltaMovement();
        projectileEntity.setDeltaMovement(projectileEntity.getDeltaMovement().add(vec.x, entity.onGround() ? (double)0.0F : vec.y, vec.z));
        return projectileEntity;
    }

    @Contract("_, _, _, _, _, _, _ -> param1")
    public static Projectile shootProjectileIgnoreMotion(Projectile projectile, Entity entity, float pitch, float yaw, float newPitch, float speed, float spread) {
        float f = (float)(-Math.sin((double)yaw * (Math.PI / 180D)) * Math.cos((double)pitch * (Math.PI / 180D)));
        float f1 = (float)(-Math.sin((double)(pitch + newPitch) * (Math.PI / 180D)));
        float f2 = (float)(Math.cos((double)yaw * (Math.PI / 180D)) * Math.cos((double)pitch * (Math.PI / 180D)));
        projectile.shoot((double)f, (double)f1, (double)f2, speed, spread);
        projectile.setOwner(entity);
        projectile.setDeltaMovement(projectile.getDeltaMovement());
        return projectile;
    }

    public static BlockHitResult rayTrace(Level level, Entity entity, Predicate<BlockState> predicate, double maxDist) {
        Vec3 eye = entity.getEyePosition(1.0F);
        float f2 = (float)Math.cos((double)(-entity.getYHeadRot()) * (Math.PI / 180D) - Math.PI);
        float f3 = (float)Math.sin((double)(-entity.getYHeadRot()) * (Math.PI / 180D) - Math.PI);
        float f4 = (float)(-Math.cos((double)(-entity.getXRot()) * (Math.PI / 180D)));
        float f5 = (float)Math.sin((double)(-entity.getXRot()) * (Math.PI / 180D));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        Vec3 vector = eye.add((double)f6 * maxDist, (double)f5 * maxDist, (double)f7 * maxDist);
        return level.isBlockInLine(new ClipBlockStateContext(eye, vector, predicate));
    }

    public static BlockHitResult rayTrace(Level level, Entity entity, double maxDist) {
        return rayTrace(level, entity, BlockBehaviour.BlockStateBase::isSolid, maxDist);
    }

    public static BlockHitResult rayTraceFluid(Level level, Entity entity, double maxDist) {
        return rayTrace(level, entity, BlockBehaviour.BlockStateBase::liquid, maxDist);
    }

    public static @Nullable Entity rayTraceEntity(Player entity, Class<? extends Entity> target, int distance, Predicate<Entity> predicate) {
        Vec3 direction = direction(entity);

        for(double i = (double)0.0F; i < (double)distance + (double)0.375F; i += (double)0.5F) {
            Vec3 pos = new Vec3(entity.getX() + direction.x * i, entity.getEyeY() - 0.2 + direction.y * i, entity.getZ() + direction.z * i);
            if (entity.level().getBlockState(new BlockPos((int)pos.x, (int)pos.y, (int)pos.z)).isSolid()) {
                break;
            }

            for(Entity targetEntity : entity.level().getEntitiesOfClass(target, new AABB(pos.x - 0.1, pos.y - 0.1, pos.z - 0.1, pos.x + 0.1, pos.y + 0.1, pos.z + 0.1), (e) -> e.getId() != entity.getId())) {
                if (predicate.test(targetEntity)) {
                    return targetEntity;
                }
            }
        }

        return null;
    }

    public static Vec3 direction(Entity entity) {
        float rotationYaw = entity.getYHeadRot();
        float rotationPitch = entity.getXRot();
        float x = (float)(-Math.sin(Math.toRadians((double)rotationYaw)) * Math.cos(Math.toRadians((double)rotationPitch)));
        float y = (float)(-Math.sin(Math.toRadians((double)rotationPitch)));
        float z = (float)(Math.cos(Math.toRadians((double)rotationYaw)) * Math.cos(Math.toRadians((double)rotationPitch)));
        return new Vec3((double)x, (double)y, (double)z);
    }

    public static ItemStack helmet(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.HEAD);
    }

    public static ItemStack chestplate(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.CHEST);
    }

    public static ItemStack leggings(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.LEGS);
    }

    public static ItemStack boots(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.FEET);
    }

    public static ItemStack mainHand(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.MAINHAND);
    }

    public static ItemStack offhand(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.OFFHAND);
    }

    public static boolean canSpawnAtPosition(LevelReader level, BlockPos spawnPos, EntityType<?> type) {
        AABB size = (new AABB(spawnPos)).inflate((double)type.getWidth(), (double)type.getHeight(), (double)type.getWidth());
        return BlockPos.betweenClosedStream(size).noneMatch((pos) -> level.getBlockState(pos).isSolid() || level.getFluidState(pos).is(FluidTags.LAVA));
    }
}