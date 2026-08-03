package io.github.orlisan.caputmundi.mixin;

import io.github.orlisan.caputmundi.items.CaputMundiItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("rawtypes")
@Mixin(LivingEntity.class)
public class EquipmentSlotMixin {
    @SuppressWarnings("unchecked")
    @Inject(at = @At("HEAD"), method = "isEquippableInSlot"/*, cancellable = true*/)
    private void mettiMieiItemEquippabili(final ItemStack itemStack, final EquipmentSlot slot, CallbackInfoReturnable ci) {
        /*if (slot == EquipmentSlot.HEAD) {
            if (itemStack.getItem() == CaputMundiItems.LAURUS_CROWN || itemStack.getItem() == CaputMundiItems.GOLDEN_LAURUS_CROWN) {
                ci.setReturnValue(true);
            }
        }*/
    }
}