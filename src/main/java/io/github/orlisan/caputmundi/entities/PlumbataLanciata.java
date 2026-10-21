package io.github.orlisan.caputmundi.entities;

import io.github.orlisan.caputmundi.items.CaputMundiItems;
import io.github.orlisan.caputmundi.items.PlumbataItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public class PlumbataLanciata extends ThrowableItemProjectile {
    double fallHeight = Double.NEGATIVE_INFINITY;

    public PlumbataLanciata(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    public PlumbataLanciata(EntityType<? extends ThrowableItemProjectile> type, LivingEntity owner, Level level, ItemStack itemStack) {
        super(type, owner, level, itemStack);
    }

    public PlumbataLanciata(EntityType<? extends ThrowableItemProjectile> type, double x, double y, double z, Level level, ItemStack itemStack) {
        super(type, x, y, z, level, itemStack);
    }

    public PlumbataLanciata(ServerLevel serverLevel, LivingEntity livingEntity, ItemStack itemStack) {
        this(CaputMundiEntities.PLUMBATA, livingEntity, serverLevel, itemStack);
    }

    @Override
    public void tick() {
        if(hittedBlock) return;
        if (this.getY() > fallHeight) fallHeight = this.getY();
        super.tick();
    }

    @Override
    protected double getDefaultGravity() {
        return 0.08;
    }

    @Override
    protected @NonNull Item getDefaultItem() {
        return CaputMundiItems.PLUMBATA;
    }

    @Override
    protected void onHitEntity(final EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        if (!level().isClientSide() && level() instanceof ServerLevel level) {
            Entity entity = hitResult.getEntity();
            double damage;
            double fallDistance = fallHeight - this.getY();//getOwner() == null ? 12:((Math.pow(PlumbataItem.VELOCITA_DI_PARTENZA, 2) * Math.pow(Math.sin(Math.toRadians(-getOwner().getXRot())), 2)) / (2 * getDefaultGravity())) - entity.getY();
            damage = fallDistance / 3;
            //    if(getOwner() != null) {
/*           if (entity.onGround()) {
                for (int i = 1; i <= (int) Math.min(Math.floor(fallDistance / 6), 2); i++) {
                    BlockPos pos = entity.blockPosition().above(-i);
                    BlockState blockState = level.getBlockState(pos);
                    Block block = blockState.getBlock();
                    if (block.getExplosionResistance() < Math.floor(fallDistance / 2)) {
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    } else break;
                    //  }
                }
           }*/
            DamageSource source1 = this.damageSources().source(CaputMundiDamages.PLUMBATA_DAMAGE, this, this.getOwner());
            entity.hurtServer(level, source1, (float) damage);
            RandomSource source = RandomSource.create();
            if (source.nextDouble() < 0.5 && entity instanceof LivingEntity lEntity)
                lEntity.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 140, 1), getOwner());
        }
    }
    boolean hittedBlock = false;
    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        hittedBlock = true;
        setDeltaMovement(Vec3.ZERO);
    }
}
