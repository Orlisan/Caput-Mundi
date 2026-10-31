package io.github.orlisan.caputmundi;

import io.github.orlisan.caputmundi.entities.Legionarius;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;

import java.util.*;

import static net.minecraft.world.level.Level.OVERWORLD;

public class Legio {
    private Legio(int legioId, ArrayList<String> legionariUUID, String capoUUID, GlobalPos base) {
        this.legionariUUID = legionariUUID;
        this.capoUUID = capoUUID;
        this.legioId = legioId;
        this.base = base;
    }

    int getDurabilita() {
        int maxDurabilita = 0;
        int durabilita = 0;
        for (Legionarius legionario : legionari) {
            maxDurabilita += legionario.getItemBySlot(EquipmentSlot.HEAD).getMaxDamage();
            durabilita += legionario.getItemBySlot(EquipmentSlot.HEAD).getMaxDamage() - legionario.getItemBySlot(EquipmentSlot.HEAD).getDamageValue();
            maxDurabilita += legionario.getItemBySlot(EquipmentSlot.CHEST).getMaxDamage();
            durabilita += legionario.getItemBySlot(EquipmentSlot.CHEST).getMaxDamage() - legionario.getItemBySlot(EquipmentSlot.CHEST).getDamageValue();
            maxDurabilita += legionario.getItemBySlot(EquipmentSlot.LEGS).getMaxDamage();
            durabilita += legionario.getItemBySlot(EquipmentSlot.LEGS).getMaxDamage() - legionario.getItemBySlot(EquipmentSlot.LEGS).getDamageValue();
            maxDurabilita += legionario.getItemBySlot(EquipmentSlot.FEET).getMaxDamage();
            durabilita += legionario.getItemBySlot(EquipmentSlot.FEET).getMaxDamage() - legionario.getItemBySlot(EquipmentSlot.FEET).getDamageValue();
            maxDurabilita += legionario.getItemBySlot(EquipmentSlot.MAINHAND).getMaxDamage();
            durabilita += legionario.getItemBySlot(EquipmentSlot.MAINHAND).getMaxDamage() - legionario.getItemBySlot(EquipmentSlot.MAINHAND).getDamageValue();
            maxDurabilita += legionario.getItemBySlot(EquipmentSlot.OFFHAND).getMaxDamage();
            durabilita += legionario.getItemBySlot(EquipmentSlot.OFFHAND).getMaxDamage() - legionario.getItemBySlot(EquipmentSlot.OFFHAND).getDamageValue();
        }
        if(maxDurabilita == 0) return 0;
        return durabilita*100 /maxDurabilita;
    }

    void refreshLegio(ServerLevel level) {
        refreshCapo(level);
        legionari.clear();
        for (String uuid : legionariUUID) {
            Legionarius legionario = (Legionarius) level.getEntity(UUID.fromString(uuid));
            if (legionario != null) legionari.add(legionario);
        }
    }
    private boolean refreshCapo(ServerLevel level) {
        Entity capo = level.getEntity(UUID.fromString(capoUUID));
        if(capo != null) {this.capo = capo;return true;}
        else return false;
    }

    GlobalPos base;
    ArrayList<String> legionariUUID;
    String capoUUID;
    Entity capo;
    int legioId;
    ArrayList<Legionarius> legionari = new ArrayList<>();

    public static Legio create(int legioId, String capoUUID, GlobalPos base, ServerLevel level, String... legionariUUIDs) {
        ArrayList<String> legionariUUID1 = new ArrayList<>(Arrays.stream(legionariUUIDs).toList());
        Legio legio = new Legio(legioId, legionariUUID1, capoUUID, base);
        legio.refreshLegio(level);
        legios.add(legio);
        return legio;
    }

    public static Legio create(int legioId, String capoUUID, BlockPos base,ServerLevel level,  String... legionariUUIDs) {
        return create(legioId, capoUUID, new GlobalPos(OVERWORLD, base), level, legionariUUIDs);
    }

    public static final List<Legio> legios = new ArrayList<>();

    public static Legio getLegio(int legioId) {
        try {
            return legios.stream().filter(legio -> legio.legioId == legioId).toList().getFirst();
        } catch (NoSuchElementException _) {
            CaputMundi.LOGGER.error("No Legio with id: {}", legioId);
            return null;
        }
    } 

    private static TicketType register(final String name, final long timeout, final @TicketType.Flags int flags) {
        return Registry.register(BuiltInRegistries.TICKET_TYPE, Identifier.fromNamespaceAndPath(CaputMundi.MOD_ID, name), new TicketType(timeout, flags));
    }
    ChunkPos inactivePos;
    private interface Activity {
    }
    private record MovActivity(ServerLevel lvl, BlockPos pos, int y) implements Activity {
    }
    //Noon mi piace, era per vedere come andava e cmq è buggato
    void moveTo(ServerLevel level, BlockPos pos, int y) {
        if (inactivePos != null) {
            setActive(level, new MovActivity(level, pos, y));
            return;
        }
        for(int i = 0; i < legionari.size();i++) {
            var leg = legionari.get(i);
            int x = (int) (pos.getX() - 8 + (double) i / 16);
            int z = pos.getZ() - 8 + i % 16;
            leg.coord = new Vec3(x, findY(level, x, z, y), z);
        }
    }
    int findY(ServerLevel level, int x, int z, int maxY) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, maxY, z);
        for(int i = maxY; i > level.getMinY(); i-- ) {
            pos.setY(i);
            if(!level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()) return pos.getY()+1;
        }
        return maxY;
    }
    void setActive(ServerLevel level, Activity reason) {
        level.getChunkSource().addTicketWithRadius(LEGIO, inactivePos, 3);
        this.activityPostLoad = reason;
    }
    private Activity activityPostLoad;
    long tickCounter = -1L;
    void tick(ServerLevel level) {
        if(activityPostLoad != null && tickCounter == -1) tickCounter = 0;
        if(tickCounter % 20 == 0 )
            level.getChunkSource().addTicketWithRadius(LEGIO, inactivePos, 3);
        if(tickCounter == 100) {
            switch(activityPostLoad) {
                case MovActivity act:
                    this.inactivePos = null;
                    refreshLegio(level);
                    legionari.forEach(leg -> leg.shouldLoadChunks = true);
                    moveTo(act.lvl, act.pos, act.y);
                    break;
                case null, default:
                    break;
            }
            activityPostLoad = null;
            tickCounter = -1L;
        }
        if(tickCounter >= 0 && tickCounter < 100) tickCounter++;
    }
    public void setInactive(ServerLevel level) {
            this.inactivePos = refreshCapo(level) ? capo.chunkPosition()
                    : new ChunkPos(base.pos().getX()/16, base.pos().getZ()/16);
            legionari.forEach(leg -> leg.shouldLoadChunks = false);

    }
    static void registerInOnInizialize() {}
    public static final TicketType LEGIO = register("legio", 40L, 14);
}