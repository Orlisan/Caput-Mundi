package io.github.orlisan.caputmundi.items;

import io.github.orlisan.caputmundi.entities.PilumLanciato;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class PilumItem extends Item implements ProjectileItem {
    public PilumItem(Properties props) {
        super(props);
    }

    @Override
    public @NonNull ItemUseAnimation getUseAnimation(@NonNull ItemStack itemStack) {
        return ItemUseAnimation.TRIDENT;
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder().add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, (double)8.0F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, (double)-2.9F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }
    @Override
    public InteractionResult use(final Level level, final Player player, final InteractionHand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if (itemInHand.nextDamageWillBreak()) {
            return InteractionResult.FAIL;
        } else {
            player.startUsingItem(hand);
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public boolean releaseUsing(final @NonNull ItemStack itemStack, final @NonNull Level level, final @NonNull LivingEntity entity, final int remainingTime) {
        if (entity instanceof Player player) {
            int timeHeld = this.getUseDuration(itemStack, entity) - remainingTime;
            if (timeHeld < 10) {
                return false;
            } else {
                if (itemStack.nextDamageWillBreak()) {
                    return false;
                } else {
                    Holder<SoundEvent> sound = EnchantmentHelper.pickHighestLevel(itemStack, EnchantmentEffectComponents.TRIDENT_SOUND).orElse(SoundEvents.TRIDENT_THROW);
                    player.awardStat(Stats.ITEM_USED.get(this));
                    if (level instanceof ServerLevel serverLevel) {
                        itemStack.hurtWithoutBreaking(1, player);
                        ItemStack thrownItemStack = itemStack.consumeAndReturn(1, player);
                        PilumLanciato trident = (PilumLanciato) Projectile.spawnProjectileFromRotation(PilumLanciato::new, serverLevel, thrownItemStack, player, 0.0F, 2.5F, 1.0F);
                        if (player.hasInfiniteMaterials()) {
                            trident.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        }

                        level.playSound(null, trident, sound.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public @NonNull Projectile asProjectile(@NonNull Level level, Position position, ItemStack itemStack, @NonNull Direction direction) {
        PilumLanciato trident = new PilumLanciato(level, position.x(), position.y(), position.z(), itemStack.copyWithCount(1));
        trident.pickup = AbstractArrow.Pickup.ALLOWED;
        return trident;
    }
    @Override
    public int getUseDuration(final ItemStack itemStack, final LivingEntity user) {
        return 72000;
    }
    public static Tool createToolProperties() {
        return new Tool(List.of(), 1.0F, 2, false);
    }

}
