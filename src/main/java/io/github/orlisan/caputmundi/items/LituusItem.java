package io.github.orlisan.caputmundi.items;

import io.github.orlisan.caputmundi.CaputMundiConstants;
import io.github.orlisan.caputmundi.entities.AquilaEntity;
import io.github.orlisan.caputmundi.gui.LituusMenu;
import io.github.orlisan.caputmundi.packets.LituusInitialPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.impl.networking.server.ServerPlayNetworkAddon;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class LituusItem extends Item {
    public LituusItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        super.use(level, player, hand);
        if(!level.isClientSide()) {
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
            AquilaEntity aquila = player.getAttached(CaputMundiConstants.AQUILA_VISUALIZZATA);
            boolean b = aquila != null;
            ServerPlayNetworking.send((ServerPlayer) player, new LituusInitialPacket(b, b?aquila.getName().getString():"No Aquila",b?aquila.getHealth():Double.NaN));
        }
        return InteractionResult.SUCCESS;
    }
}
