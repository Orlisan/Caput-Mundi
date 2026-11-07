package io.github.orlisan.caputmundi.entities;

import io.github.orlisan.caputmundi.CaputMundi;
import io.github.orlisan.caputmundi.CaputMundiConstants;
import io.github.orlisan.caputmundi.DignitasRomana;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class CivisRomanus extends Villager {
    public CivisRomanus(EntityType<? extends Villager> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public int getPlayerReputation(Player player) {
        DignitasRomana attached = player.getAttached(CaputMundiConstants.DIGNITAS_ROMANA);
        return super.getPlayerReputation(player) + (attached == null? 0:(getVillagerData().profession().is(CAMPI_DOCTOR)? attached.fides():0));
    }
    public static final ResourceKey<VillagerProfession> CAMPI_DOCTOR = ResourceKey.create(Registries.VILLAGER_PROFESSION, Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, "campi_doctor"));
}
