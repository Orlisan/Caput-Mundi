package io.github.orlisan.caputmundi.items;

import io.github.orlisan.caputmundi.accessors.SwingAnimationAccessor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwingAnimationType;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.SwingAnimation;
import org.jspecify.annotations.NonNull;

public class PugioItem extends Item {
    public PugioItem(Properties properties) {
        SwingAnimation swing = new SwingAnimation(SwingAnimationType.NONE, 4);
        if ((Object) swing instanceof SwingAnimationAccessor ac) {
            ac.caput_mundi$setPugio(true);
        }
        super(properties.component(DataComponents.ATTACK_ANIMATION, swing)
                .stacksTo(1)
                .attributes(ItemAttributeModifiers.builder().add(Attributes.ATTACK_DAMAGE,
                new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 4.0, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND).build()));
    }

    @Override
    public float getAttackDamageBonus(@NonNull Entity victim, float damage, @NonNull DamageSource damageSource) {
        var entit = damageSource.getEntity();
        if (entit instanceof LivingEntity attacker) {
            double distance = attacker.distanceTo(victim);
            double h = attacker.getHealth();
            double mult = 0;
            if (distance < 2) mult += (7 - distance) / 10;
            if (h < 5) mult += (7 - h) / 10;
            return (float) (damage * mult);
        }
        return 0f;
    }
}
