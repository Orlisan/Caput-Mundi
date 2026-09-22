package io.github.orlisan.caputmundi.items;

import io.github.orlisan.caputmundi.CaputMundiConstants;
import io.github.orlisan.caputmundi.entities.AquilaEntity;
import io.github.orlisan.caputmundi.gui.LituusMenu;
import io.github.orlisan.caputmundi.packets.LituusPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public class LituusItem extends Item {
    public LituusItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        super.use(level, player, hand);
        if(!level.isClientSide()) {
            ItemStack stack = player.getItemInHand(hand);
            player.openMenu(new MenuProvider() {

                @Override
                public @Nullable AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
                    return new LituusMenu(id, (ServerPlayer) player);
                }

                @Override
                public Component getDisplayName() {
                    return Component.literal("Lituus");
                }
            });
            String aquilaUUID = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getString("AquilaUUID").orElse(CaputMundiConstants.NULLID);
            AquilaEntity aquila = (AquilaEntity) level.getEntity(UUID.fromString(aquilaUUID));
            boolean b = aquila != null;
            ServerPlayNetworking.send((ServerPlayer) player, new LituusPacket(b, b?aquila.getName().getString():"No Aquila",b?aquila.getHealth():Double.NaN, b?aquilaUUID:"No Aquila"));
        }
        return InteractionResult.SUCCESS;
    }
}
