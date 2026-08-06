package io.github.orlisan.caputmundi.entities.goals;

import io.github.orlisan.caputmundi.entities.AquilaEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import static io.github.orlisan.caputmundi.CaputMundi.LOGGER;

import java.util.EnumSet;

public class RuotaInCerchioGoal extends AquilaGoalConCostruttore {


    public RuotaInCerchioGoal(AquilaEntity entity) {
        super(entity);
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return entity.ruotaInCerchio;
    }

    BlockPos centroCerchio;
    double raggioDalCerchio;
    double aquilaInc;

    @Override
    public void start() {
        super.start();
        entity.isFlying = true;
        if (entity.centroCerchio != AquilaEntity.NO_CENTER) {
            centroCerchio = entity.centroCerchio;
        } else {
            int radius = Math.random() > 0.5 ? 4 : 6;
            centroCerchio = new BlockPos(Math.random() > 0.5 ? entity.blockPosition().getX() + radius : entity.blockPosition().getX() - radius, entity.blockPosition().getY(),
                    Math.random() > 0.5 ? entity.blockPosition().getZ() + radius : entity.blockPosition().getZ() - radius);
        }
        raggioDalCerchio = distanceTo2d(entity.position(), Vec3.atCenterOf(centroCerchio));
        LOGGER.info("raggio: {}, entità:{}", raggioDalCerchio, entity.getId());
        aquilaInc = findStartInc(entity.blockPosition());
    }

    double findStartInc(BlockPos punto) {
        final double X = punto.getX() - centroCerchio.getX();
        final double Z = punto.getZ() - centroCerchio.getZ();
        double arcoseno = Math.asin((double) X / raggioDalCerchio) * 180 / Math.PI;
        LOGGER.info("X: {}, Z:{}, asin:{}, punto:{}, entità:{}", X, Z, arcoseno, punto, entity.getId());
        double inclinazioneFinale = 0;
        if (arcoseno < 0) {
            if (Z > 0) {
                inclinazioneFinale = -180 - arcoseno + 360;
            } else {
                inclinazioneFinale = arcoseno + 360;
            }
        } else {
            if (Z > 0) {
                inclinazioneFinale = 180 - arcoseno;
            } else {
                inclinazioneFinale = arcoseno;
            }
        }
        LOGGER.info("inc:{}", inclinazioneFinale);
        return inclinazioneFinale;
    }

    //TODO Scegliere centro cerchio e ruotare
    @Override
    public void tick() {
        super.tick();
        if (entity.getNavigation().isDone()) {
            aquilaInc += 10;
            double cos = raggioDalCerchio * Math.cos(aquilaInc * (Math.PI / 180));
            double sin = raggioDalCerchio * Math.sin(aquilaInc * (Math.PI / 180)) + centroCerchio.getX();
            LOGGER.info("cos:{}, sin:{}, entità:{}", cos, sin, entity.getId());
            entity.getNavigation().moveTo(sin, centroCerchio.getY(), -cos + centroCerchio.getZ(), 1.0);
           // LOGGER.info("entity:{}, pos:{}", entity.getId(), entity.position());
            entity.getLookControl().setLookAt(sin, centroCerchio.getY(), -cos + centroCerchio.getZ());
            LOGGER.info("centro:{}, inclinazione:{}, target:{}, pos:{}, entity:{}, lookControl:{}", centroCerchio, aquilaInc, new Vec3(sin, centroCerchio.getY(), -cos+centroCerchio.getZ()), entity.position(), entity.getId(), new Vec3(entity.getLookControl().getWantedX(), entity.getLookControl().getWantedY(), entity.getLookControl().getWantedZ()));
        }
    }

    public double distanceTo2d(Vec3 a, Vec3 b) {
        return Math.sqrt(Math.pow(a.x() - b.x(), 2) + Math.pow(a.z() - b.z(), 2));
    }
}
