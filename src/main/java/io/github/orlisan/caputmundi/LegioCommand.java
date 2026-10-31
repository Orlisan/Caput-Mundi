package io.github.orlisan.caputmundi;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.orlisan.caputmundi.entities.Legionarius;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;

public class LegioCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("legio")
                // .requires(...)  <- copialo da un comando vanilla (vedi sotto)
                .then(Commands.literal("create")
                        .then(Commands.argument("id", IntegerArgumentType.integer(0))
                                .then(Commands.argument("legionari", EntityArgument.entities())
                                        .executes(LegioCommand::create))))
                .then(Commands.literal("moveto")
                        .then(Commands.argument("id", IntegerArgumentType.integer(0))
                                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                        .executes(LegioCommand::moveTo))))
                .then(Commands.literal("setinactive")
                        .then(Commands.argument("id", IntegerArgumentType.integer(0))
                                .executes(LegioCommand::setInactive)))
                .then(Commands.literal("info")
                        .then(Commands.argument("id", IntegerArgumentType.integer(0))
                                .executes(LegioCommand::info))));
    }

    /** Cerca la Legio senza il log d'errore di getLegio; null (con messaggio) se non esiste. */
    private static Legio find(CommandContext<CommandSourceStack> ctx) {
        int id = IntegerArgumentType.getInteger(ctx, "id");
        for (Legio l : Legio.legios) if (l.legioId == id) return l;
        ctx.getSource().sendFailure(Component.literal("Nessuna Legio con id " + id));
        return null;
    }

    /** Livello della dimensione in cui sta la Legio (non quello di chi esegue il comando). */
    private static ServerLevel levelOf(CommandContext<CommandSourceStack> ctx, Legio legio) {
        ServerLevel level = ctx.getSource().getServer().getLevel(legio.base.dimension());
        if (level == null) ctx.getSource().sendFailure(Component.literal("Dimensione della Legio non caricata"));
        return level;
    }

    private static int create(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSourceStack src = ctx.getSource();
        int id = IntegerArgumentType.getInteger(ctx, "id");
        if (Legio.legios.stream().anyMatch(l -> l.legioId == id)) {
            src.sendFailure(Component.literal("Esiste già una Legio con id " + id));
            return 0;
        }
        ArrayList<String> uuids = new ArrayList<>();
        for (Entity e : EntityArgument.getEntities(ctx, "legionari")) {
            if (e instanceof Legionarius leg) uuids.add(leg.getStringUUID());
        }
        if (uuids.isEmpty()) {
            src.sendFailure(Component.literal("Nel selettore non c'è nessun Legionarius"));
            return 0;
        }
        // il primo è il capo; base = posizione di chi esegue il comando, nella sua dimensione
        GlobalPos base = new GlobalPos(src.getLevel().dimension(), BlockPos.containing(src.getPosition()));
        Legio.create(id, uuids.getFirst(), base, src.getLevel(), uuids.toArray(new String[0]));
        src.sendSuccess(() -> Component.literal("Legio " + id + " creata con " + uuids.size() + " soldati"), false);
        return 1;
    }

    private static int moveTo(CommandContext<CommandSourceStack> ctx) {
        Legio legio = find(ctx);
        if (legio == null) return 0;
        ServerLevel level = levelOf(ctx, legio);
        if (level == null) return 0;
        BlockPos pos = BlockPosArgument.getBlockPos(ctx, "pos");
        legio.moveTo(level, pos, pos.getY());
        ctx.getSource().sendSuccess(() -> Component.literal("Ordine moveto inviato a " + pos.toShortString()), false);
        return 1;
    }

    private static int setInactive(CommandContext<CommandSourceStack> ctx) {
        Legio legio = find(ctx);
        if (legio == null) return 0;
        ServerLevel level = levelOf(ctx, legio);
        if (level == null) return 0;
        legio.setInactive(level);
        ctx.getSource().sendSuccess(() -> Component.literal("Legio " + legio.legioId + " messa inattiva"), false);
        return 1;
    }

    private static int info(CommandContext<CommandSourceStack> ctx) {
        Legio legio = find(ctx);
        if (legio == null) return 0;
        ServerLevel level = levelOf(ctx, legio);
        if (level == null) return 0;
        legio.refreshLegio(level); // così il conteggio è quello reale, non la cache vecchia
        String stato = legio.tickCounter >= 0 ? "in risveglio (" + legio.tickCounter + "/100)"
                : legio.inactivePos != null ? "inattiva"
                : "attiva";
        String msg = "Legio " + legio.legioId
                     + " | stato: " + stato
                     + " | soldati caricati: " + legio.legionari.size() + "/" + legio.legionariUUID.size()
                     + " | base: " + legio.base.dimension().identifier() + " " + legio.base.pos().toShortString()
                     + " | capo: " + legio.capoUUID;
        ctx.getSource().sendSuccess(() -> Component.literal(msg), false);
        return 1;
    }
}