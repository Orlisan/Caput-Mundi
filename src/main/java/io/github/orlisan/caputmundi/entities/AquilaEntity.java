package io.github.orlisan.caputmundi.entities;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.PlayState;
import com.geckolib.util.GeckoLibUtil;
import io.github.orlisan.caputmundi.CaputMundiConstants;
import io.github.orlisan.caputmundi.entities.goals.DecolloGoal;
import io.github.orlisan.caputmundi.entities.goals.RuotaInCerchioGoal;
import io.github.orlisan.caputmundi.packets.AquilaVistaMobsPacket;
import io.github.orlisan.caputmundi.packets.AquilaVistaPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static io.github.orlisan.caputmundi.CaputMundi.LOGGER;

@SuppressWarnings("NullableProblems")
public class AquilaEntity extends PathfinderMob implements GeoEntity {
    private final AnimatableInstanceCache cache =
            GeckoLibUtil.createInstanceCache(this);
    public boolean isFlying = true;
    public boolean startDecollo = true;
    private boolean startAnimDecollo = true;
    public static final BlockPos NO_CENTER = new BlockPos(Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE);
    public boolean ruotaInCerchio = false;
    public boolean startAnimCerchio = false;
    public boolean changedCenter = false;
    public BlockPos centroCerchio = NO_CENTER;
    private static final EntityDataAccessor<Boolean> HAS_COLLAR =
            SynchedEntityData.defineId(AquilaEntity.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(HAS_COLLAR, false);
    }

    public boolean hasCollar() {
        return this.entityData.get(HAS_COLLAR);
    }

    public void setHasCollar(boolean newCollar) {
        this.entityData.set(HAS_COLLAR, newCollar);
    }

    //TODO:Mettere addomesticamento e coso client mixin a Hud.class
    public ServerPlayer padrone = null;
    public static final double DURATA_VOLO = 2.5;

    protected AquilaEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl<>(this, 10, true);
        this.setHasCollar(false);
    }

    public static final Item ADDOMESTICATION_ITEM = Items.RABBIT_FOOT;

    @Override
    public @NotNull InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        LOGGER.info("Interact Chiamato");
        super.interact(player, hand, location);
        LOGGER.info("Dopo super, ci sono ancora");
        if (!player.level().isClientSide()) {
            ItemStack stack = player.getItemInHand(hand);
            LOGGER.info("Item:{}, hand:{}", stack, hand);
            LOGGER.info(String.valueOf(stack.getItem() == ADDOMESTICATION_ITEM));
            if (stack.getItem() == ADDOMESTICATION_ITEM) {
                if (this.padrone == null) {
                    if (!player.isCreative())
                        player.setItemInHand(hand, new ItemStack(stack.getItem(), stack.getCount() - 1));
                    if (Math.random() < 0.3d) {
                        //       LOGGER.info("Passato random");
                        this.padrone = (ServerPlayer) player;
                        this.centroCerchio = BlockPos.containing(location);
                        changedCenter = true;
                        this.setHasCollar(true);
                        player.swing(hand);
                        this.setPersistenceRequired();
                        return InteractionResult.SUCCESS;
                    }
                    //     LOGGER.info("Non passato random");
                } else {
                    this.heal(Math.random() > 0.5 ? 3 : 4);
                    if (!player.isCreative())
                        player.setItemInHand(hand, new ItemStack(stack.getItem(), stack.getCount() - 1));
                }
            }
        }
        return InteractionResult.PASS;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.FLYING_SPEED, 5.0f).add(Attributes.MAX_HEALTH, 30.0F);
    }

    @Override
    public void tick() {
        super.tick();
        //LOGGER.info("Aquila:{}, vola:{}, posizione:{}, superIsNOGravity:{}, isNOGravity:{}, haPadrone:{}", getId(), isFlying, position(), super.isNoGravity(), isNoGravity(), padrone != null);
        tickCounter++;
        if (!startDecollo) {
            startRuotaInCerchio();
        }
        if (padrone != null) {
            AquilaEntity attached = padrone.getAttached(CaputMundiConstants.AQUILA_VISUALIZZATA);
            if (!level().isClientSide() && level() instanceof ServerLevel serverlvl) {
                if (attached == null || (attached == this && tickCounter % serverlvl.getGameRules().get(CaputMundiConstants.AQUILA_VIEW_UPDATE_TICKS) == 0)) {
                    ArrayList<ArrayList<String>> vista = getAquilaVista();
                    Map<String, coordsMob> vistaMobs = getMobs();
                    List<String> mobs = vistaMobs.keySet().stream().toList();
                    Collection<coordsMob> coords = vistaMobs.values();
                    List<Double> x = new ArrayList<>();
                    List<Double> y = new ArrayList<>();
                    for (coordsMob c : coords) {
                        x.add(c.x);
                        y.add(c.y);
                    }
                    //         LOGGER.info("Vista dell'aquila {}: {}", getId(), vista);
                    ServerPlayNetworking.send(padrone, new AquilaVistaPacket(vista));
                    ServerPlayNetworking.send(padrone, new AquilaVistaMobsPacket(mobs, x, y));
                    padrone.setAttached(CaputMundiConstants.AQUILA_VISUALIZZATA, this);
                }
            }
        }
    }

    int tickCounter = 0;

    /* @Override
     protected void tickDeath() {
         super.tickDeath();
         if (padrone != null) {
             AquilaEntity attached = padrone.getAttached(CaputMundiConstants.AQUILA_VISUALIZZATA);
             if (attached != null) {
                 if (attached == this) {
                     padrone.setAttached(CaputMundiConstants.AQUILA_VISUALIZZATA, null);
                     ServerPlayNetworking.send(padrone, new AquilaVistaPacket(new ArrayList<>()));
                 }
             }
         }
     }*/
    record coordsMob(double x, double y) {
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
     /*   for (Entity entity : level().getEntitiesOfClass(Skeleton.class, aabb)) {
            result.put("minecraft:skeleton", findCoords(aabb, entity.position()));
        }
        for (Entity entity : level().getEntitiesOfClass(Spider.class, aabb)) {
            result.put("minecraft:spider", findCoords(aabb, entity.position()));
        }
        for (Entity entity : level().getEntitiesOfClass(CaveSpider.class, aabb)) {
            result.put("minecraft:cave_spider", findCoords(aabb, entity.position()));
        }*/
        return result;
    }

    coordsMob findCoords(AABB aabb, Vec3 position) {

        return new coordsMob( /*aabb.maxZ - position.z()*/position.z - aabb.minZ, position.x - aabb.minX);
    }

    @Override
    public void onRemoval(RemovalReason reason) {
        super.onRemoval(reason);
        if (padrone != null) {
            AquilaEntity attached = padrone.getAttached(CaputMundiConstants.AQUILA_VISUALIZZATA);
            if (attached != null) {
                if (attached == this) {
                    padrone.setAttached(CaputMundiConstants.AQUILA_VISUALIZZATA, null);
                    ServerPlayNetworking.send(padrone, new AquilaVistaPacket(new ArrayList<>()));
                }
            }
        }
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

    void startRuotaInCerchio() {
        this.ruotaInCerchio = true;
        this.startAnimCerchio = true;
    }

    void startDecollo() {
        this.startDecollo = true;
        this.startAnimDecollo = true;
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
            if (startAnimDecollo) {
                state.setAnimation(
                        RawAnimation.begin()
                                .thenPlay("animazione_decollo").thenLoop("animazione_volo"));
                startAnimDecollo = false;
            } else if (startAnimCerchio) {
                if (state.controller().getCurrentAnimationTime() % DURATA_VOLO < 0.1) {
                    state.setAnimation(
                            RawAnimation.begin()
                                    .thenPlay("animazione_trans_inclinazione")
                                    .thenLoop("animazione_volo_inclinato")
                    );
                    startAnimCerchio = false;
                }
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
        this.goalSelector.addGoal(0, new DecolloGoal(this));
        this.goalSelector.addGoal(1, new RuotaInCerchioGoal(this));
        super.registerGoals();
    }

    @Override
    public @NotNull AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}