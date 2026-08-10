package thaumcraft.common.entities.abstracts;

public interface ITaintRecoverableMob {
    boolean canBeRecoveredFromTaintedMob();
    void recoverFromTaintedMob();
}
