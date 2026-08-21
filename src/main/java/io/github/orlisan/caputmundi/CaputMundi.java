package io.github.orlisan.caputmundi;

import com.geckolib.GeckoLib;
import io.github.orlisan.caputmundi.blocks.CaputMundiBlocks;
import io.github.orlisan.caputmundi.entities.AquilaEntity;
import io.github.orlisan.caputmundi.entities.CaputMundiEntities;
import io.github.orlisan.caputmundi.items.CaputMundiCreativeTab;
import io.github.orlisan.caputmundi.items.CaputMundiItems;
import io.github.orlisan.caputmundi.packets.AquilaVistaMobsPacket;
import io.github.orlisan.caputmundi.packets.AquilaVistaPacket;
import io.github.orlisan.caputmundi.packets.LituusInitialPacket;
import io.github.orlisan.caputmundi.potions.CaputMundiPotions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.resources.Identifier;

import net.minecraft.world.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class CaputMundi implements ModInitializer {
    public static final String MOD_ID = "caput_mundi";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        CommandRegistrationCallback.EVENT.register(((commandDispatcher, commandBuildContext, commandSelection) ->
        {
            //DEBUG
            commandDispatcher.register(Commands.literal("aquilagoals")
                    .then(Commands.argument("aquile", EntityArgument.entities())
                            .then(Commands.literal("attaccoDistanza")
                                    .executes(commandContext -> {
                                        Collection<? extends Entity> entita = EntityArgument.getEntities(commandContext, "aquile");
                                        for (Entity entity : entita) {
                                            if (entity instanceof AquilaEntity aquilaEntity) {
                                                aquilaEntity.startAttaccoADistanza();
                                            }
                                        }
                                        return 1;
                                    }))
                            .then(Commands.literal("decollo")
                                    .executes(commandContext -> {
                                        Collection<? extends Entity> entita = EntityArgument.getEntities(commandContext, "aquile");
                                        for (Entity entity : entita) {
                                            if (entity instanceof AquilaEntity aquilaEntity) {
                                                aquilaEntity.startDecollo();
                                            }
                                        }
                                        return 1;
                                    }))
                            .then(Commands.literal("ruota")
                                    .executes(commandContext -> {
                                        Collection<? extends Entity> entita = EntityArgument.getEntities(commandContext, "aquile");
                                        for (Entity entity : entita) {
                                            if (entity instanceof AquilaEntity aquilaEntity) {
                                                aquilaEntity.startRuotaInCerchio();
                                            }
                                        }
                                        return 1;
                                    }))
                    ));
        }));
        PayloadTypeRegistry.clientboundPlay().register(AquilaVistaPacket.TYPE, AquilaVistaPacket.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(AquilaVistaMobsPacket.TYPE, AquilaVistaMobsPacket.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(LituusInitialPacket.TYPE, LituusInitialPacket.CODEC);
        CaputMundiConstants.register();
        CaputMundiBlocks.register();
        CaputMundiItems.register();
        CaputMundiPotions.register();
        CaputMundiEntities.register();
        CaputMundiCreativeTab.register();
        LOGGER.info("Hello Fabric world!");
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
