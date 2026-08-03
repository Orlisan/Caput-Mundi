package io.github.orlisan.caputmundi.blocks;

import io.github.orlisan.caputmundi.items.CaputMundiItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class LaurusBlock extends CropBlock {
    public LaurusBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull InteractionResult useItemOn(@NotNull ItemStack itemStack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (itemStack.is(Items.SHEARS) && state.getValue(CropBlock.AGE) > 5) {
            level.addFreshEntity(new ItemEntity(level, Math.random() > 0.5 ? pos.getX() + 0.5 : pos.getX() - 0.5, pos.getY() + 0.5, Math.random() > 0.5 ? pos.getZ() + 0.5 : pos.getZ() - 0.5, new ItemStack(CaputMundiItems.LAURUS_LEAF)));
            level.setBlock(pos, state.setValue(AGE, state.getValue(AGE) - 1), 2);
            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        return CaputMundiItems.LAURUS_SEED;
    }
}
