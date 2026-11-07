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
import net.minecraft.world.entity.LivingEntity;
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
            maxDurabilita+=legionario.getMaxDurabilita();
            durabilita+=legionario.getDurabilita();
        }
        if(maxDurabilita == 0) return 0;
        return durabilita*100/maxDurabilita;
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
    public ArrayList<String> legionariUUID;
    String capoUUID;
    Entity capo;
    int legioId;
    public ArrayList<Legionarius> legionari = new ArrayList<>();
    public ArrayList<ArrayList<Legionarius>> file = new ArrayList<>();

    public static Legio create(int legioId, String capoUUID, GlobalPos base, ServerLevel level, String... legionariUUIDs) {
        ArrayList<String> legionariUUID1 = new ArrayList<>(Arrays.stream(legionariUUIDs).toList());
        Legio legio = new Legio(legioId, legionariUUID1, capoUUID, base);
        legio.refreshLegio(level);
        legio.reorder();
        legios.add(legio);
        return legio;
    }
    public void reorder() {
        file.clear();
        List<Legionarius> legionariiOrdinati = legionari.stream().sorted(Comparator.comparingDouble(LivingEntity::getHealth)).toList();
        for(int i = 0; i < legionariiOrdinati.size(); i++) {
            Legionarius leg = legionariiOrdinati.get(i);
            int fila = i / 6;
            if(fila >= file.size()) file.add(new ArrayList<>());
            file.getLast().add(leg);
        }
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
    private record MovActivity(ServerLevel lvl, BlockPos pos, int y, Entity e) implements Activity {
    }
    public void initBattle(LivingEntity victim){
        if(!(victim instanceof Legionarius)) {

        }
    }
    public void addNemico(LivingEntity nemico) {
        NEMICI.add(nemico);
        if(NEMICI.size() == 1) {
            initBattle(nemico);
        }
    }
    public List<LivingEntity> NEMICI = new ArrayList<>();

    void moveTo(ServerLevel level, BlockPos pos, int y, Entity mobDaGuardare) {
        if (inactivePos != null) {
            setActive(level, new MovActivity(level, pos, y, mobDaGuardare));
            return;
        }
        // legionari.add(new Legionarius(null, null))
        guardaLoStesso(mobDaGuardare);
        disponi(level, pos, y, 1.0, 1.5f);
    }
    protected void guardaLoStesso(Entity nemico) {
        legionari.forEach( leg -> leg.entityToLook = nemico);
        var x = 0;
        /*Vec2 centro = calcolaCentro();
        double r = centro.y-nemico.position().z;
        double tanGrossa = centro.x - nemico.position().x;
        double tan = tanGrossa/r;
        double incB = Math.atan(tan*Math.PI/180)/Math.PI*180;
        double inc = 0;
        if(tanGrossa > 0) {
            if(incB > 0) inc+=90;
        }else{
            inc+=180;
            if(incB > 0) inc+=90;
        }
        inc+=Math.abs(incB);
        final double finalInc = inc;
        legionari.forEach(leg -> {
            leg.setYRot((float) finalInc);
            leg.setYBodyRot((float) finalInc);
            leg.setYHeadRot((float) finalInc);
        });*/
    }
  /*  private Vec2 calcolaCentro() {
        Vec2 xmzm = new Vec2(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
        Vec2 xmzM = new Vec2(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY);
        Vec2 xMzm = new Vec2(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
        Vec2 xMzM = new Vec2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
        for(Legionarius leg: legionari) {
            Vec3 p = leg.position();
            if(p.x < xmzm.x && p.z < xmzm.y) xmzm = new Vec2((float) p.x, (float) p.z);
            if(p.x > xmzM.x && p.z < xmzM.y) xmzM = new Vec2((float) p.x, (float) p.z);
            if(p.x < xMzm.x && p.z > xMzm.y) xMzm = new Vec2((float) p.x, (float) p.z);
            if(p.x > xMzM.x && p.z > xMzM.y) xMzM = new Vec2((float) p.x, (float) p.z);
        }
        return new Vec2((xmzm.x+xmzM.x+xMzm.x+xMzM.x)/4, (xmzm.y+xmzM.y+xMzm.y+xMzM.y)/4);
    }*/
    protected void disponi(ServerLevel level, BlockPos pos, int y, double distanza, double velocita) {
        for(int i = 0; i < legionari.size();i++) {
            var leg = legionari.get(i);
            double x = (int) (pos.getX() + 4*distanza - ((double) i % 8))*distanza;
            double z = pos.getZ() + 5*distanza - ((double) (i - (i % 10)) /10)*distanza;
            leg.coord = new Vec3(x, findY(level, x, z, y), z);
            leg.velocity = velocita;
        }
    }

    int findY(ServerLevel level, double x, double z, int maxY) {
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
        if(activityPostLoad != null && tickCounter == -1) {
            tickCounter = 0;
            level.getChunkSource().addTicketWithRadius(LEGIO, inactivePos, 3);
        }
   //     if(tickCounter % 20 == 0 )
          //  level.getChunkSource().addTicketWithRadius(LEGIO, inactivePos, 3);
        if(tickCounter == 20) {
            switch(activityPostLoad) {
                case MovActivity act:
                    this.inactivePos = null;
                    refreshLegio(level);
                    if(legionariUUID.size() == legionari.size()) {
                        legionari.forEach(leg -> leg.shouldLoadChunks = true);
                        moveTo(act.lvl, act.pos, act.y, act.e);
                        activityPostLoad = null;
                        tickCounter = -1L;
                    }else{
                        level.getChunkSource().addTicketWithRadius(LEGIO, inactivePos, 3);
                        tickCounter = 0;
                    }
                    break;
                case null, default:
                    break;
            }
        }
        if(tickCounter >= 0) tickCounter++;
    }
    public void setInactive(ServerLevel level) {
            this.inactivePos = refreshCapo(level) ? capo.chunkPosition()
                    : new ChunkPos(base.pos().getX()/16, base.pos().getZ()/16);
            legionari.forEach(leg -> leg.shouldLoadChunks = false);

    }
    static void register() {}
    public static final TicketType LEGIO = register("legio", 40L, 14);
}