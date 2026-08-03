package io.github.orlisan.caputmundi.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class LaurusCrownItem extends Item {
    public LaurusCrownItem(Properties properties) {
        super(properties);
    }

   /* @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        super.use(level, player, hand);
        ItemStack vecchioHead = player.getItemBySlot(EquipmentSlot.HEAD);
        player.setItemSlot(EquipmentSlot.HEAD, player.getItemInHand(hand));
        player.setItemInHand(hand, vecchioHead);
        return InteractionResult.SUCCESS;
    }*/

    @Override
    public int getDefaultMaxStackSize() {
        return 1;
    }
}
