/*package io.github.orlisan.caputmundi.mixin;

import io.github.orlisan.caputmundi.CaputMundi;
import io.github.orlisan.caputmundi.items.CaputMundiItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Player.class)
public class PlayerHurtMixin {
    @Unique
    LivingEntity self = (Player) (Object) this;

    @ModifyVariable(method = "actuallyHurt", at = @At("HEAD"),/* argsOnly = true,*/ /*ordinal = 0, argsOnly = true)
    private float detraeDannoCorona(float dmg) {
        CaputMundi.LOGGER.info("ActuallyHurt chiamato da player, self: " + self.toString());
        ItemStack itemBySlot = self.getItemBySlot(EquipmentSlot.HEAD);
        Item item = itemBySlot.getItem();
        if (item == CaputMundiItems.LAURUS_CROWN) {
            itemBySlot.hurtAndBreak((int) dmg, self, EquipmentSlot.HEAD);
            return dmg * 0.75F;
        } else if (item == CaputMundiItems.GOLDEN_LAURUS_CROWN) {
            itemBySlot.hurtAndBreak((int) (dmg * 0.75F), self, EquipmentSlot.HEAD);
            return dmg * 0.5F;
        }
        return dmg;
    }
}
*/