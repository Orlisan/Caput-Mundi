package io.github.orlisan.caputmundi.entities.goals;

import io.github.orlisan.caputmundi.entities.AquilaEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
    boolean hasPadrone;

    @Override
    public void start() {
        super.start();
        entity.isFlying = true;
        if (entity.padrone != null) {
            hasPadrone = true;
        }
        if (entity.centroCerchio != AquilaEntity.NO_CENTER) {
            centroCerchio = entity.centroCerchio;

        } else {
            int radius = Math.random() > 0.5 ? 4 : 6;
            centroCerchio = new BlockPos(Math.random() > 0.5 ? entity.blockPosition().getX() + radius : entity.blockPosition().getX() - radius, entity.blockPosition().getY(),
                    Math.random() > 0.5 ? entity.blockPosition().getZ() + radius : entity.blockPosition().getZ() - radius);
        }
        raggioDalCerchio = distanceTo2d(entity.position(), Vec3.atCenterOf(hasPadrone ? BlockPos.containing(entity.padrone.position()) : centroCerchio));
        LOGGER.info("raggio: {}, entità:{}", raggioDalCerchio, entity.getId());
        aquilaInc = findStartInc(entity.blockPosition());
    }

    double findStartInc(BlockPos punto) {
        BlockPos realCentroCerchio = hasPadrone ? BlockPos.containing(entity.padrone.position()) : centroCerchio;
        final double X = punto.getX() - realCentroCerchio.getX();
        final double Z = punto.getZ() - realCentroCerchio.getZ();
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

    boolean isNavigationToPadrone = false;

    @Override
    public void tick() {
        super.tick();
        if (!hasPadrone && entity.padrone != null) {
            if (isNavigationToPadrone && entity.getNavigation().isDone()) {
                isNavigationToPadrone = false;
                entity.changedCenter = false;
                start();
            } else {
                int radius = Math.random() > 0.5 ? 10 : 15;
                entity.getNavigation().moveTo(entity.padrone.getX() - radius, entity.padrone.position().y + 30, entity.padrone.getZ(), 1.5);
                isNavigationToPadrone = true;
            }
        }

        if (entity.getNavigation().isDone()) {
            aquilaInc += 10;
            BlockPos realCentroCerchio = hasPadrone ? BlockPos.containing(entity.padrone.position().with(Direction.Axis.Y, entity.padrone.position().y + 30)) : centroCerchio;
            double cos = raggioDalCerchio * Math.cos(aquilaInc * (Math.PI / 180));
            double sin = raggioDalCerchio * Math.sin(aquilaInc * (Math.PI / 180)) + realCentroCerchio.getX();
            // LOGGER.info("cos:{}, sin:{}, entità:{}", cos, sin, entity.getId());
            entity.getNavigation().moveTo(sin, realCentroCerchio.getY(), -cos + realCentroCerchio.getZ(), 1.0);
            // LOGGER.info("entity:{}, pos:{}", entity.getId(), entity.position());
            entity.getLookControl().setLookAt(sin, realCentroCerchio.getY(), -cos + realCentroCerchio.getZ());
            //      LOGGER.info("centro:{}, inclinazione:{}, target:{}, pos:{}, entity:{}, lookControl:{}", realCentroCerchio, aquilaInc, new Vec3(sin, realCentroCerchio.getY(), -cos + realCentroCerchio.getZ()), entity.position(), entity.getId(), new Vec3(entity.getLookControl().getWantedX(), entity.getLookControl().getWantedY(), entity.getLookControl().getWantedZ()));
        }
    }

    public double distanceTo2d(Vec3 a, Vec3 b) {
        return Math.sqrt(Math.pow(a.x() - b.x(), 2) + Math.pow(a.z() - b.z(), 2));
    }
}
