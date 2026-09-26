package io.github.orlisan.caputmundi.entities;

import io.github.orlisan.caputmundi.items.CaputMundiItems;
import io.github.orlisan.caputmundi.PilaCountAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jspecify.annotations.NonNull;

public class PilumLanciato extends AbstractArrow {

    public PilumLanciato(final EntityType<? extends PilumLanciato> type, final Level level) {
        super(type, level);
    }

    public PilumLanciato(final Level level, final LivingEntity owner, final ItemStack tridentItem) {
        super(CaputMundiEntities.PILUM, owner, level, tridentItem, null);
    }

    public PilumLanciato(final Level level, final double x, final double y, final double z, final ItemStack tridentItem) {
        super(CaputMundiEntities.PILUM, x, y, z, level, tridentItem, tridentItem);
    }

    @Override
    protected @NonNull ItemStack getDefaultPickupItem() {
        return new ItemStack(CaputMundiItems.PILUM);
    }

    @Override
    protected void onHitEntity(final EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        float dmg = 8.0F;
        Entity currentOwner = this.getOwner();
        DamageSource damageSource = this.damageSources().trident(this, currentOwner == null ? this : currentOwner);
        Level var7;

        if (entity.hurtOrSimulate(damageSource, dmg)) {
            if (entity.is(EntityTypes.ENDERMAN)) {
                return;
            }

            var7 = this.level();
            if (var7 instanceof ServerLevel serverLevel) {
                EnchantmentHelper.doPostAttackEffectsWithItemSourceOnBreak(serverLevel, entity, damageSource, this.getWeaponItem(), (weapon) -> this.kill(serverLevel));
            }

            if (entity instanceof LivingEntity mob) {
                mob.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1));
                mob.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 100, 1));
                this.doKnockback(mob, damageSource);
                this.doPostHurtEffects(mob);
            }
            if (entity instanceof PilaCountAccessor pila) {
                pila.setPila$Count(pila.getPila$Count() + 1);
            }
            this.discard();
        } else {
            this.deflect(ProjectileDeflection.REVERSE, entity, this.owner, false);
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.02, 0.2, 0.02));
        }
        this.playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);
    }

}
