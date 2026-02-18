package thaumcraft.common.tiles.abstracts;

public interface IThaumcraftFurnace {
    int getCookedTime();
    int getRequiredCookTime();
    int getFuelBurnRemainingTime();
    int getFuelBurnTotalTime();
    boolean getSpeedBoost();
    void setCookedTime(int cookTime);
    void setRequiredCookTime(int requiredCookTime);
    void setFuelBurnTimeRemaining(int fuelBurnTime);
    void setFuelBurnTotalTime(int fuelBurnTotalTime);
    void setSpeedBoost(boolean speedBoost);
}
