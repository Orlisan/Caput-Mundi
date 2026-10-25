package io.github.orlisan.caputmundi.items;

import io.github.orlisan.caputmundi.entities.PlumbataLanciata;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import static io.github.orlisan.caputmundi.items.PlumbataItem.VELOCITA_DI_PARTENZA;

public class ScutumItem extends ShieldItem {
    public ScutumItem(Properties props) {
        super(props);
    }

    /*
        @Override
        public InteractionResult use(Level level, Player player, InteractionHand hand) {
            InteractionResult result = super.use(level, player, hand);
            ItemStack self = player.getItemInHand(hand);
            Integer i = self.get(CaputMundiItems.PLUMBATA_COUNT);
            boolean mainHand = hand == InteractionHand.MAIN_HAND;
            if (i != null && i > 0 && player.getItemInHand(mainHand ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND).isEmpty()) {
                return InteractionResult.SUCCESS;
            }

            return result;
        }
    */
    @Override
    public boolean releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime) {

        //         chargingTime = 0;
        Integer count = itemStack.get(CaputMundiItems.PLUMBATA_COUNT);
        if (entity instanceof Player player) {
            if (count != null && count > 0 && player.getItemInHand(player.getUsedItemHand() == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND :
                    InteractionHand.MAIN_HAND).isEmpty()) {
                if (level instanceof ServerLevel serverLevel) {
                    Projectile.spawnProjectileFromRotation(PlumbataLanciata::new, serverLevel, new ItemStack(CaputMundiItems.PLUMBATA, 1), player, 0.0F, VELOCITA_DI_PARTENZA, 1.0F);
                }
                itemStack.set(CaputMundiItems.PLUMBATA_COUNT, count - 1);
                return true;
            }
        }
        return super.releaseUsing(itemStack, level, entity, remainingTime);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack self, ItemStack other, Slot slot, ClickAction clickAction, Player player, SlotAccess carriedItem) {
        if (other.is(CaputMundiItems.PLUMBATA)) {
            Integer i = self.get(CaputMundiItems.PLUMBATA_COUNT);
            if (i == null) i = 0;
            if (i < 5) {
                self.set(CaputMundiItems.PLUMBATA_COUNT, i + 1);
                other.shrink(1);
                return true;
            }
        }
        return super.overrideOtherStackedOnMe(self, other, slot, clickAction, player, carriedItem);
    }


    /*@Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        super.inventoryTick(itemStack, level, owner, slot);
        if(chargingTime != -1) {
            chargingTime++;
            if(chargingTime == 60) {
                CustomData customData = itemStack.get(DataComponents.CUSTOM_DATA);
                if (customData != null) {
                    CompoundTag compoundTag = customData.copyTag();
                    compoundTag.putInt("after", 1);
                    itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(compoundTag));
                    chargingTime = -1;
                }
            }
        }
    }*/
}
