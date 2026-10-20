package io.github.orlisan.caputmundi.items;

import io.github.orlisan.caputmundi.accessors.SwingAnimationAccessor;
import io.github.orlisan.caputmundi.entities.CaputMundiDamages;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwingAnimationType;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.SwingAnimation;

public class GladiusItem extends Item {
    public GladiusItem(Properties props) {
        SwingAnimationType type = SwingAnimationType.NONE;
        SwingAnimation swing = new SwingAnimation(type, 4);
        if((Object) swing instanceof SwingAnimationAccessor ac) {
            ac.caput_mundi$setGladius(true);
        }
        super(props.component(DataComponents.ATTACK_ANIMATION, swing)
                .stacksTo(1)
                .attributes(ItemAttributeModifiers.builder().add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 7.0, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND).build()));
    }

    @Override
    public void hurtEnemy(ItemStack itemStack, LivingEntity mob, LivingEntity attacker) {
        attacker.setDeltaMovement(attacker.getLookAngle().normalize());
        attacker.syncVelocity = true;

        super.hurtEnemy(itemStack, mob, attacker);
    }
}
