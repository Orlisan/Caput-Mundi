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

    @Override
    public int getDefaultMaxStackSize() {
        return 1;
    }
}
