package thaumcraft.common.entities.abstracts;

public interface ITaintConvertableEntity {
    boolean canConvertToTaintedMob();
    void convertToTaintedMob();
}
