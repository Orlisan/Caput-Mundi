package io.github.orlisan.caputmundi.entities;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.PlayState;
import com.geckolib.util.GeckoLibUtil;
import io.github.orlisan.caputmundi.entities.goals.DecolloGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class AquilaEntity extends PathfinderMob implements GeoEntity {
    private final AnimatableInstanceCache cache =
            GeckoLibUtil.createInstanceCache(this);
    public boolean isFlying = false;
    public boolean startDecollo = true;
    private boolean startAnimDecollo = true;

    public boolean ruotaInCerchio = false;
    public BlockPos centroCerchio = DecolloGoal.failPos;
    public ServerPlayer padrone = null;
    public static final double DURATA_VOLO = 2.5;
    protected AquilaEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl<>(this, 10, true);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.FLYING_SPEED, 5.0f).add(Attributes.MAX_HEALTH, 30.0F);
    }

    @Override
    public void tick() {
        super.tick();
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
        super.registerGoals();
    }

    @Override
    public @NotNull AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}