package io.github.orlisan.caputmundi;

import io.github.orlisan.caputmundi.blocks.CaputMundiBlocks;
import io.github.orlisan.caputmundi.entities.AquilaEntity;
import io.github.orlisan.caputmundi.entities.CaputMundiEntities;
import io.github.orlisan.caputmundi.items.CaputMundiCreativeTab;
import io.github.orlisan.caputmundi.items.CaputMundiItems;
import io.github.orlisan.caputmundi.packets.AquilaVistaMobsPacket;
import io.github.orlisan.caputmundi.packets.AquilaVistaPacket;
import io.github.orlisan.caputmundi.packets.LituusPacket;
import io.github.orlisan.caputmundi.packets.PlayerDatasPacket;
import io.github.orlisan.caputmundi.potions.CaputMundiPotions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

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
                            .then(Commands.literal("dimmigoal")
                            .executes(context -> {
                                try {
                                    Collection<? extends Entity> entita = EntityArgument.getEntities(context, "aquile");
                                    for (Entity entity : entita) {
                                        if (entity instanceof AquilaEntity aquilaEntity) {
                                            context.getSource().getPlayer().sendSystemMessage(Component.literal("Aquila: " + aquilaEntity.getId() + " goal attivo: " + aquilaEntity.getGoalSelector().getAvailableGoals().stream().filter(goal -> goal.isRunning()).toList().getFirst().getGoal().getClass().getSimpleName()).withColor(TextColor.YELLOW));
                                        }
                                    }
                                }catch(Exception e) {
                                    IO.println("Eccezzione: "+e);
                                }finally{
                                    return 1;
                                }
                            }))
                            .then(Commands.literal("sullattenti")
                                    .executes(context -> {
                                        Collection<? extends Entity> entita = EntityArgument.getEntities(context, "aquile");
                                        for (Entity entity : entita) {
                                            if (entity instanceof AquilaEntity aquilaEntity) {
                                                aquilaEntity.stopAllGoals();
                                                aquilaEntity.modeSullAttenti = true;
                                           }
                                        }
                                        return 1;
                                    }))
                    ));
        }));
        PayloadTypeRegistry.clientboundPlay().register(AquilaVistaPacket.TYPE, AquilaVistaPacket.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(AquilaVistaMobsPacket.TYPE, AquilaVistaMobsPacket.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(PlayerDatasPacket.TYPE, PlayerDatasPacket.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(LituusPacket.TYPE, LituusPacket.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(LituusPacket.TYPE, LituusPacket.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(LituusPacket.TYPE, (packet, context) -> {
            context.server().execute(() -> {
                AquilaEntity entity = (AquilaEntity) context.player().level().getEntity(UUID.fromString(packet.aquilaUUID()));
                if (entity != null) {
                    String[] split = packet.aquilaName().split(":");
                    if (split[0].equals("spy")) {
                        entity.stopAllGoals();
                        if (Objects.equals(split[1], "stop")) {
                            entity.playerToSpy = null;
                            entity.startRuotaInCerchio();
                        } else
                            entity.spyPlayer(split[1]);

                    } else if (split[0].equals("recall")) {
                        entity.stopAllGoals();
                        entity.startRichiama();
                    }else if(split[0].equals("startsend")) {
                        entity.startSendVista = true;
                    }else if(split[0].equals("stopsend")) {
                        entity.startSendVista = false;
                    }else if(split[0].equals("setfalse")) {
                        if(split[1].equals("decollo")) {
                            entity.setStartDecolloAnim(false);
                        }else if(split[1].equals("atterraggio")) {
                            entity.setStartAtterraggioAnim(false);
                        }
                    }else if(split[0].equals("moveto")) {
                        entity.stopAllGoals();
                        entity.coordsToMove = new Vec3(Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
                    }
                }
            });
        });
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
