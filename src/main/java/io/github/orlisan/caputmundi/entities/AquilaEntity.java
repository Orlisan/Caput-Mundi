package io.github.orlisan.caputmundi.entities;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.PlayState;
import com.geckolib.util.GeckoLibUtil;
import io.github.orlisan.caputmundi.CaputMundi;
import io.github.orlisan.caputmundi.CaputMundiConstants;
import io.github.orlisan.caputmundi.entities.goals.DecolloGoal;
import io.github.orlisan.caputmundi.entities.goals.RuotaInCerchioGoal;
import io.github.orlisan.caputmundi.packets.AquilaVistaPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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
        CaputMundi.LOGGER.info("Interact Chiamato");
        super.interact(player, hand, location);
        CaputMundi.LOGGER.info("Dopo super, ci sono ancora");
        if (!player.level().isClientSide()) {
            ItemStack stack = player.getItemInHand(hand);
            CaputMundi.LOGGER.info("Item:{}, hand:{}", stack, hand);
            CaputMundi.LOGGER.info(String.valueOf(stack.getItem() == ADDOMESTICATION_ITEM));
            if (stack.getItem() == ADDOMESTICATION_ITEM) {
                if (this.padrone == null) {
                    if (!player.isCreative())
                        player.setItemInHand(hand, new ItemStack(stack.getItem(), stack.getCount() - 1));
                    if (Math.random() < 0.3d) {
                        //       CaputMundi.LOGGER.info("Passato random");
                        this.padrone = (ServerPlayer) player;
                        this.centroCerchio = BlockPos.containing(location);
                        changedCenter = true;
                        this.setHasCollar(true);
                        player.swing(hand);
                        return InteractionResult.SUCCESS;
                    }
                    //     CaputMundi.LOGGER.info("Non passato random");
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
        //CaputMundi.LOGGER.info("Aquila:{}, vola:{}, posizione:{}, superIsNOGravity:{}, isNOGravity:{}, haPadrone:{}", getId(), isFlying, position(), super.isNoGravity(), isNoGravity(), padrone != null);
        if (!startDecollo) {
            startRuotaInCerchio();
        }
        if (padrone != null && padrone.getAttached(CaputMundiConstants.AQUILA_VISUALIZZATA) == null) {
            ServerPlayNetworking.send(padrone, new AquilaVistaPacket(getAquilaVista()));
            padrone.setAttached(CaputMundiConstants.AQUILA_VISUALIZZATA, this);
        }
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        if(padrone != null) {
            AquilaEntity attached = padrone.getAttached(CaputMundiConstants.AQUILA_VISUALIZZATA);
            if (attached != null) {
                if(attached == this) {
                    padrone.setAttached(CaputMundiConstants.AQUILA_VISUALIZZATA, null);
                }
            }
        }
    }

    ArrayList<String> getAquilaVista() {
        ArrayList<String> result = new ArrayList<>();
        for (int x = (int) (this.position().x - 8); x < this.position().x + 8; x++) {
            for (int z = (int) (this.position().z - 8); z < this.position().z + 8; z++) {
                for (int i = (int) position().y(); i > -64; i--) {
                    BlockPos pos = new BlockPos(x, i, z);
                    if (!level().isEmptyBlock(pos)) {
                        result.add(BuiltInRegistries.BLOCK.getKey(level().getBlockState(pos).getBlock()).toString());
                        break;
                    }
                }
            }
        }
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