package io.github.orlisan.caputmundi.items;

import io.github.orlisan.caputmundi.accessors.SwingAnimationAccessor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwingAnimationType;
import net.minecraft.world.item.component.SwingAnimation;

public class GladiusItem extends Item {
    public GladiusItem(Properties props) {
        SwingAnimationType type = SwingAnimationType.NONE;
        if((Object) type instanceof SwingAnimationAccessor ac) {
            ac.caput_mundi$setGladius(true);
        }
        super(props.component(DataComponents.ATTACK_ANIMATION,  new SwingAnimation(type, 6)));
    }
}
