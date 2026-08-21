package io.github.orlisan.caputmundi.gui;

import io.github.orlisan.caputmundi.CaputMundiConstants;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class LituusMenu extends AbstractContainerMenu {
    ServerPlayer player;
    ServerPlayer playerToSpy;

    public LituusMenu(int containerId, ServerPlayer player) {
        super(CaputMundiMenuTypes.LITUUS_MENU, containerId);
        this.player = player;
    }

    public LituusMenu(int id, Inventory ignoredInv) {
        super(CaputMundiMenuTypes.LITUUS_MENU, id);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
   /* public boolean hasAquila() {
        return player.getAttached(CaputMundiConstants.AQUILA_VISUALIZZATA) != null;
    }*/
    public void spyPlayer(String playerName) {
        if (playerToSpy != null) {
            for (ServerPlayer player : player.level().players()) {
                if (player.getName().getString().equals(playerName)) {
                    playerToSpy = player;
                }
            }
        }
    }

}
