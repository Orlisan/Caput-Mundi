package io.github.orlisan.caputmundi.entities;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.PlayState;
import com.geckolib.util.GeckoLibUtil;
import io.github.orlisan.caputmundi.CaputMundiConstants;
import io.github.orlisan.caputmundi.entities.goals.*;
import io.github.orlisan.caputmundi.items.CaputMundiItems;
import io.github.orlisan.caputmundi.packets.AquilaVistaMobsPacket;
import io.github.orlisan.caputmundi.packets.AquilaVistaPacket;
import io.github.orlisan.caputmundi.packets.LituusPacket;
import io.github.orlisan.caputmundi.packets.PlayerDatasPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.monster.spider.CaveSpider;
import net.minecraft.world.entity.monster.spider.Spider;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.*;

import static io.github.orlisan.caputmundi.CaputMundi.LOGGER;

@SuppressWarnings("NullableProblems")
public class AquilaEntity extends PathfinderMob implements GeoEntity {
    private final AnimatableInstanceCache cache =
            GeckoLibUtil.createInstanceCache(this);
    public boolean isFlying = true;
    public boolean startDecollo = false;
    public static final BlockPos NO_CENTER = new BlockPos(Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE);
    public boolean ruotaInCerchio = false;
    public boolean changedCenter = false;
    public BlockPos centroCerchio = NO_CENTER;
    public boolean richiama = false;
    public boolean distanceAttack = false;
    public boolean vicinoAttack = false;
    public ServerPlayer playerToSpy = null;
    public ServerPlayer playerToSpyDopo = null;
    public boolean startSendVista = false;
    private static final EntityDataAccessor<Boolean> HAS_COLLAR =
            SynchedEntityData.defineId(AquilaEntity.class, EntityDataSerializers.BOOLEAN);

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData) {
        var ret = super.finalizeSpawn(level, difficulty, spawnReason, groupData);
        startDecollo();
        return ret;
    }

    private static final EntityDataAccessor<Boolean> STARTATTERRAGGIO =
            SynchedEntityData.defineId(AquilaEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> STARTDECOLLO =
            SynchedEntityData.defineId(AquilaEntity.class, EntityDataSerializers.BOOLEAN);
    public ThingsToDoAfterDecollo cosaDaFare = ThingsToDoAfterDecollo.RUOTAINCERCHIO;

    public enum ThingsToDoAfterDecollo {
        RUOTAINCERCHIO,
        MOVETOPLAYER,
        ATTACKDISTANZA
    }

    public void startGoToPlayer(ServerPlayer playerToSpy) {
        if (isFlying) this.playerToSpy = playerToSpy;
        else {
            this.playerToSpyDopo = playerToSpy;
            startDecollo();
            cosaDaFare = ThingsToDoAfterDecollo.MOVETOPLAYER;
        }
    }

    public void setStartAtterraggioAnim(boolean flag) {
        this.entityData.set(STARTATTERRAGGIO, flag);
    }

    public boolean getStartAtterraggioAnim() {
        return this.entityData.get(STARTATTERRAGGIO);
    }

    public void setStartDecolloAnim(boolean flag) {
        this.entityData.set(STARTDECOLLO, flag);
    }

    public boolean getStartDecolloAnim() {
        return this.entityData.get(STARTDECOLLO);
    }

    private static final EntityDataAccessor<Boolean> HAS_ARMOR =
            SynchedEntityData.defineId(AquilaEntity.class, EntityDataSerializers.BOOLEAN);

    public void startRichiama() {
        if (isFlying) this.richiama = true;
    }

    @Override
    protected void actuallyHurt(ServerLevel level, DamageSource source, float dmg) {
        if (source.is(DamageTypes.FALL)) return;
        Entity entity = source.getEntity();
        if (/*source.is(DamageTypes.MOB_ATTACK) &&*/ entity instanceof Mob livingEntity) {
            if (this.distanceTo(entity) < 3.0f) {
                vicinoAttack = true;
            } else {
                startAttaccoADistanza();
            }
            setTarget(livingEntity);
        } else if (/*(source.is(DamageTypes.PLAYER_ATTACK) && source.is(DamageTypes.ARROW)) && */entity instanceof ServerPlayer player) {
            if (player == padrone && RandomSource.create().nextDouble() > 0.9) {
                ServerPlayNetworking.send(padrone, new AquilaVistaPacket(new ArrayList<>()));
                ServerPlayNetworking.send(padrone, new AquilaVistaMobsPacket(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
                setHasCollar(false);
                padrone = null;
            }
            if (this.distanceTo(entity) < 3.0f) {
                vicinoAttack = true;
            } else {
                startAttaccoADistanza();
            }
            setTarget(player);
        }
        super.actuallyHurt(level, source, dmg);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(HAS_COLLAR, false);
        entityData.define(HAS_ARMOR, false);
        entityData.define(STARTATTERRAGGIO, false);
        entityData.define(STARTDECOLLO, false);
    }

    public ItemStack lituus;

    public boolean hasCollar() {
        return this.entityData.get(HAS_COLLAR);
    }

    public boolean hasArmor() {
        return this.entityData.get(HAS_ARMOR);
    }

    public void setHasCollar(boolean newCollar) {
        this.entityData.set(HAS_COLLAR, newCollar);
    }

    public void setHasArmor(boolean newArmor) {
        this.entityData.set(HAS_ARMOR, newArmor);
        AttributeInstance abs = this.getAttribute(Attributes.MAX_ABSORPTION);
        if (abs != null) abs.setBaseValue(10);
        AttributeInstance healt = this.getAttribute(Attributes.MAX_HEALTH);
        if (healt != null) healt.setBaseValue(30);
        AttributeInstance dmg = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (dmg != null) dmg.setBaseValue(5);
    }

    public ServerPlayer padrone = null;
    public static final double DURATA_VOLO = 2.5;

    public void spyPlayer(String playerName) {
        if (this.padrone != null) {
            for (ServerPlayer p : this.padrone.level().players()) {
                if (p.getName().getString().equals(playerName)) startGoToPlayer(p);
            }
        }
    }

    protected AquilaEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.moveControl = new AquilaMoveControl<>(this/*, 10, true*/);
        this.lookControl = new AquilaLookControl(this);
        this.setHasCollar(false);
        setXRot(90);
    }

    public static final Item ADDOMESTICATION_ITEM = Items.RABBIT_FOOT;

    @Override
    public @NotNull InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        super.interact(player, hand, location);
        if (!player.level().isClientSide()) {
            ItemStack stack = player.getItemInHand(hand);
            LOGGER.info(String.valueOf(stack.getItem() == ADDOMESTICATION_ITEM));
            LOGGER.info("Item:{}", stack.getItem());
            if (stack.getItem() == ADDOMESTICATION_ITEM) {
                if (this.padrone == null) {
                    if (!player.isCreative())
                        player.setItemInHand(hand, new ItemStack(stack.getItem(), stack.getCount() - 1));
                    if (Math.random() < 0.3d) {
                        //       LOGGER.info("Passato random");
                        this.padrone = (ServerPlayer) player;
                        this.ruotaInCerchio = false;
                        this.startDecollo = false;
                        this.startRichiama();
                        this.centroCerchio = BlockPos.containing(location);
                        changedCenter = true;
                        this.setHasCollar(true);
                        player.swing(hand);
                        this.setPersistenceRequired();
                        return InteractionResult.SUCCESS;
                    } else {
                        RandomSource random = RandomSource.create();
                        for (int i = 0; i < 6; i++) {
                            level().addParticle(ParticleTypes.ASH, this.position().x + random.nextInt(-1, 1), this.position().y + random.nextInt(-1, 1), this.position().z + random.nextInt(-1, 1), 0, 0, 0);
                        }
                    }
                    //     LOGGER.info("Non passato random");
                } else {
                    this.heal(Math.random() > 0.5 ? 3 : 4);
                    if (!player.isCreative())
                        player.setItemInHand(hand, new ItemStack(stack.getItem(), stack.getCount() - 1));
                }
            } else if (stack.getItem() == CaputMundiItems.AQUILA_ARMOR_ITEM) {
                if (padrone != null && padrone == player) {
                    LOGGER.info("Ci sono dentro all'armors");
                    setHasArmor(true);
                    return InteractionResult.SUCCESS;
                }
            } else if (stack.getItem() == CaputMundiItems.LITUUS_ITEM) {
                if (padrone != null && padrone == player) {
                    lituus = stack;
                    CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
                    CompoundTag nbt = data.copyTag();
                    nbt.putString("AquilaUUID", this.getStringUUID());
                    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.FLYING_SPEED, 5.0f).add(Attributes.MAX_HEALTH, 30.0F).add(Attributes.ATTACK_DAMAGE, 3.0).add(Attributes.MOVEMENT_SPEED, 1.0);
    }

    @Override
    public void tick() {
        super.tick();
        tickCounter++;
     /*   if (!startDecollo && !distanceAttack && !vicinoAttack) {
            startRuotaInCerchio();
        }*/

        if (padrone != null) {
            if (!level().isClientSide() && level() instanceof ServerLevel serverlvl) {
                if (startSendVista && tickCounter % serverlvl.getGameRules().get(CaputMundiConstants.AQUILA_VIEW_UPDATE_TICKS) == 0) {
                    ArrayList<ArrayList<String>> vista = getAquilaVista();
                    Map<String, coordsMob> vistaMobs = getMobs();
                    List<String> mobs = vistaMobs.keySet().stream().toList();
                    Collection<coordsMob> coords = vistaMobs.values();
                    List<Double> x = new ArrayList<>();
                    List<Double> y = new ArrayList<>();
                    List<Vec3> poss = new ArrayList<>();
                    for (coordsMob c : coords) {
                        x.add(c.x);
                        y.add(c.y);
                        poss.add(c.effectivePos);
                    }
                    //         LOGGER.info("Vista dell'aquila {}: {}", getId(), vista);

                    ServerPlayNetworking.send(padrone, new AquilaVistaPacket(vista));
                    ServerPlayNetworking.send(padrone, new AquilaVistaMobsPacket(mobs, x, y, poss));
                }
            }
        }
    }

    int tickCounter = 0;


    public record coordsMob(double x, double y, Vec3 effectivePos) {
    }

    public Map<String, coordsMob> getMobs() {
        int posX = (int) Math.floor(position().x());
        int posY = (int) Math.floor(position().y());
        int posZ = (int) Math.floor(position().z());
        int minY = Integer.MAX_VALUE;
        for (int x = posX - 8; x < posX + 8; x++) {
            for (int z = posZ - 8; z < posZ + 8; z++) {
                for (int i = posY; i > -64; i--) {
                    BlockPos pos = new BlockPos(x, i, z);
                    if (!level().isEmptyBlock(pos)) {
                        if (i < minY) minY = i;
                        break;
                    }
                }
            }
        }
        int mobsCount = 0;
        int x = (int) Math.floor(position().x);
        int y = (int) Math.floor(position().y);
        int z = (int) Math.floor(position().z);
        AABB aabb = new AABB(x - 8, minY, z - 8, x + 8, y, z + 8);
        Map<String, coordsMob> result = new HashMap<>();
        ServerPlayNetworking.send(this.padrone, new LituusPacket("clearplayers", ""));
        for (Entity entity : level().getEntitiesOfClass(Player.class, aabb)) {
            Player player = (Player) entity;
            coordsMob coords = findCoords(aabb, player.position());
            PlayerDatasPacket packet = new PlayerDatasPacket(player.getStringUUID(), player.getPlainTextName(), player.getHealth(), player.position(), new Vec2((float) coords.x, (float) coords.y));
            IO.println("Packet: " + packet + " mapPos:" + coords.x + "," + coords.y);
            ServerPlayNetworking.send(this.padrone, packet);
            //      LOGGER.info("Mob:{}, Coords:{}", entity, findCoords(aabb, entity.position()));
        }

        for (Entity entity : level().getEntitiesOfClass(Zombie.class, aabb)) {
            result.put("minecraft:zombie" + mobsCount, findCoords(aabb, entity.position()));
            mobsCount++;
            //      LOGGER.info("Mob:{}, Coords:{}", entity, findCoords(aabb, entity.position()));
        }
        for (Entity entity : level().getEntitiesOfClass(Creeper.class, aabb)) {
            result.put("minecraft:creeper" + mobsCount, findCoords(aabb, entity.position()));
            mobsCount++;
        }
        for (Entity entity : level().getEntitiesOfClass(EnderMan.class, aabb)) {
            result.put("minecraft:enderman" + mobsCount, findCoords(aabb, entity.position()));
            mobsCount++;
        }
        for (Entity entity : level().getEntitiesOfClass(Skeleton.class, aabb)) {
            result.put("minecraft:skeleton" + mobsCount, findCoords(aabb, entity.position()));
            mobsCount++;
        }
        for (Entity entity : level().getEntitiesOfClass(Spider.class, aabb)) {
            result.put("minecraft:spider" + mobsCount, findCoords(aabb, entity.position()));
            mobsCount++;
        }
        for (Entity entity : level().getEntitiesOfClass(CaveSpider.class, aabb)) {
            result.put("minecraft:cave_spider" + mobsCount, findCoords(aabb, entity.position()));
            mobsCount++;
        }
        return result;
    }

    coordsMob findCoords(AABB aabb, Vec3 position) {

        return new coordsMob(position.z - aabb.minZ, position.x - aabb.minX, position);
    }

    @Override
    public void remove(RemovalReason reason) {
        if (padrone != null) {
            lituus.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().remove("AquilaUUID");
        }
        super.remove(reason);
    }

    ArrayList<ArrayList<String>> getAquilaVista() {
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        ArrayList<String> tempYList = new ArrayList<>();
        int posX = (int) Math.floor(position().x());
        int posY = (int) Math.floor(position().y());
        int posZ = (int) Math.floor(position().z());
        for (int x = posX - 8; x < posX + 8; x++) {
            for (int z = posZ - 8; z < posZ + 8; z++) {
                for (int i = posY; i > -64; i--) {
                    BlockPos pos = new BlockPos(x, i, z);
                    if (!level().isEmptyBlock(pos)) {
                        BlockState blockState = level().getBlockState(pos);
                        tempYList.add(BuiltInRegistries.BLOCK.getKey(blockState.getBlock()).toString());
                        if (blockState.isCollisionShapeFullBlock(level(), pos)) {
                            result.add(new ArrayList<>(tempYList));
                            tempYList.clear();
                            break;
                        }
                    }
                }
            }
        }
        // LOGGER.info("Size:{}, Aquila:{}, Result:{}", result.size(), this.getId(), result);

        return result;
    }

    public void startRuotaInCerchio() {
        IO.println("RuotaInCerchio Chiamato");
        if (isFlying) {
            this.ruotaInCerchio = true;
        } else {
            startDecollo();
            cosaDaFare = ThingsToDoAfterDecollo.RUOTAINCERCHIO;
        }
    }

    public void startAttaccoADistanza() {
        if (isFlying) {
            this.distanceAttack = true;
            this.setStartAtterraggioAnim(true);
        } else {
            startDecollo();
            cosaDaFare = ThingsToDoAfterDecollo.ATTACKDISTANZA;
        }
    }


    public void startDecollo() {
   //     IO.println("StartDecollo chiamato");
        this.startDecollo = true;
        this.setStartDecolloAnim(true);
    //    IO.println("getStartDecolloAnim: " + getStartDecolloAnim());
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation(this, level);
        flyingPathNavigation.setCanFloat(true);
        flyingPathNavigation.setCanOpenDoors(false);
        return flyingPathNavigation;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>("movement", 0, state -> {


            if (getStartDecolloAnim()) {
                //     (logPoint:"Voglio Decollare: atterraggioAnim"+getStartAtterraggioAnim() )
                //  IO.println("Voglio Decollare: atterraggioAnim" + getStartAtterraggioAnim());
                state.setAnimation(
                        RawAnimation.begin()
                                .thenPlay("animazione_decollo").thenLoop("animazione_volo"));
                setStartDecolloAnim(false);
                ClientPlayNetworking.send(new LituusPacket("setfalse:decollo", this.stringUUID));
                //  IO.println("Decollato: decolloAnim: " + getStartDecolloAnim() + "atterraggio:" + getStartAtterraggioAnim());
                //(logPoint: "Decollato: decolloAnim: "+getStartDecolloAnim()+"atterraggio:"+getStartAtterraggioAnim())
            } else if (getStartAtterraggioAnim()) {
                state.setAnimation(
                        RawAnimation.begin()
                                .thenPlayAndHold("animazione_atterraggio")
                );
                setStartAtterraggioAnim(false);
                ClientPlayNetworking.send(new LituusPacket("setfalse:atterraggio", this.stringUUID));
            }
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public boolean isNoGravity() {
        return isFlying;
    }

    @Override
    protected void registerGoals() {
        //this.goalSelector.addGoal();
        this.goalSelector.removeAllGoals(_ -> true);
        this.goalSelector.addGoal(0, new AttaccoRavvicinatoGoal(this, 3.5, false));
        this.goalSelector.addGoal(1, new AttaccoDistanzaGoal(this));
        this.goalSelector.addGoal(2, new GoToPlayerGoal(this));
        this.goalSelector.addGoal(3, new RichiamaGoal(this));
        this.goalSelector.addGoal(4, new DecolloGoal(this));
        this.goalSelector.addGoal(5, new RuotaInCerchioGoal(this));
        super.registerGoals();
    }

    @Override
    public @NotNull AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


    //Come il phantom
    private static class AquilaLookControl extends LookControl {
        public AquilaLookControl(Mob mob) {
            super(mob);
        }

        @Override
        public void tick() {
        }
    }

    private class AquilaMoveControl<T extends Mob> extends /*Flying*/MoveControl<T> {
        private float speed;

        public AquilaMoveControl(final T mob/*, int qualcosa, boolean hoversInPlace*/) {
            super(mob/*, qualcosa, hoversInPlace*/);
            this.speed = (float) (0.1F * getSpeedModifier());
        }

        @Override
        public void tick() {
            if (AquilaEntity.this.horizontalCollision) {
                //   AquilaEntity.this.setYRot(AquilaEntity.this.getYRot() + 180.0F);
                this.speed = (float) (0.1F * getSpeedModifier());
            }
            if (this.operation != Operation.MOVE_TO) return;
            //this.operation = Operation.WAIT;
            double tdx = this.getWantedX() - AquilaEntity.this.getX();
            double tdy = this.getWantedY() - AquilaEntity.this.getY();
            double tdz = this.getWantedZ() - AquilaEntity.this.getZ();
            double sd = Math.sqrt(tdx * tdx + tdz * tdz);
            if (Math.abs(sd) > (double) 1.0E-5F) {
                double yRelativeScale = (double) 1.0F - Math.abs(tdy * (double) 0.7F) / sd;
                tdx *= yRelativeScale;
                tdz *= yRelativeScale;
                sd = Math.sqrt(tdx * tdx + tdz * tdz);
                double sd2 = Math.sqrt(tdx * tdx + tdz * tdz + tdy * tdy);
                float prev = AquilaEntity.this.getYRot();
                float angle = (float) Mth.atan2(tdz, tdx);
                float a = Mth.wrapDegrees(AquilaEntity.this.getYRot() + 90.0F);
                float b = Mth.wrapDegrees(angle * (180F / (float) Math.PI));
                AquilaEntity.this.setYRot(Mth.approachDegrees(a, b, 4.0F) - 90.0F);
                AquilaEntity.this.yBodyRot = AquilaEntity.this.getYRot();
                if (Mth.degreesDifferenceAbs(prev, AquilaEntity.this.getYRot()) < 3.0F) {
                    this.speed = Mth.approach(this.speed, 1.8F, 0.005F * (1.8F / this.speed));
                } else {
                    this.speed = Mth.approach(this.speed, 0.2F, 0.025F);
                }

                float xRotD = (float) (-(Mth.atan2(-tdy, sd) * (double) (180F / (float) Math.PI)));
                AquilaEntity.this.setXRot(xRotD);
                float moveAngle = AquilaEntity.this.getYRot() + 90.0F;
                double txd = (double) (this.speed * Mth.cos(moveAngle * ((float) Math.PI / 180F))) * Math.abs(tdx / sd2);
                double tzd = (double) (this.speed * Mth.sin(moveAngle * ((float) Math.PI / 180F))) * Math.abs(tdz / sd2);
                double tyd = (double) (this.speed * Mth.sin(xRotD * ((float) Math.PI / 180F))) * Math.abs(tdy / sd2);
                Vec3 movement = AquilaEntity.this.getDeltaMovement();
                AquilaEntity.this.setDeltaMovement(movement.add((new Vec3(txd, tyd, tzd)).subtract(movement).scale(0.2)));
            }

        }
    }
}