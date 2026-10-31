package io.github.orlisan.caputmundi.entities;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.util.GeckoLibUtil;
import io.github.orlisan.caputmundi.Legio;
import io.github.orlisan.caputmundi.entities.goals.MoveToCoordGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Legionarius extends PathfinderMob implements GeoEntity, MoveToCoordGoal.MovableToCoord {

    protected Legionarius(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new MoveToCoordGoal(this, 1f));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0F).add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    @Override
    public boolean removeWhenFarAway(double distSqr) {
        return false;
    }

    public boolean shouldLoadChunks = true;
    public Vec3 coord = AquilaEntity.NO_MOVES;
    long tickCounter = 0L;
    @Override
    public void tick() {
        tickCounter++;
        if(tickCounter == 20) tickCounter = 0;
        if(shouldLoadChunks && level() instanceof ServerLevel serverLevel && tickCounter == 0) {
            serverLevel.getChunkSource().addTicketWithRadius(Legio.LEGIO, this.chunkPosition(), 2);
        }
        super.tick();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }
    private final AnimatableInstanceCache cache =
            GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public Vec3 coordsToMove() {
        return coord;
    }

    @Override
    public void setCoordsToMove(Vec3 move) {
        this.coord = move;
    }
}
