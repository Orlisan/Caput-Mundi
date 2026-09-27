package io.github.orlisan.caputmundi.mixin;

import io.github.orlisan.caputmundi.entities.CaputMundiDamages;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class PlayerBlockingMixin {
    @Inject(method="applyItemBlocking", at=@At("TAIL"))
    public void rompiti(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Float> cir) {
        if(source.is(CaputMundiDamages.PILUM_DAMAGE)) {
            LivingEntity player = (LivingEntity) (Object) this;
            ItemStack itemWith = player.getItemBlockingWith();
            if(itemWith != null && player instanceof ServerPlayer serverPlayer) {
                itemWith.hurtAndBreak(9999, level, serverPlayer, (item) -> {
                    EquipmentSlot slot = player.getUsedItemHand() == InteractionHand.MAIN_HAND
                                ? EquipmentSlot.MAINHAND
                            : EquipmentSlot.OFFHAND;
                    player.onEquippedItemBroken(item, slot);
                });
            }
        }
    }
}
