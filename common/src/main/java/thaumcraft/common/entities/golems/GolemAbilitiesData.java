package thaumcraft.common.entities.golems;

import thaumcraft.common.entities.golems.properties.ExtraAbilityDataMap;
import thaumcraft.common.entities.golems.properties.GolemBasicAbilityProperties;

public class GolemAbilitiesData {
    public float carryPower;//item carry capacity:*1,fluid carry capacity(mb):*1000
    public float inventorySize;
    public float inventorySizeMultiplier;
    public float attackDelay;
    public float regenDelay;
    public float regenAmount;
    public float strength;
    public float attackFireTicks;
    public float speedMultiplier;
    public float homeRange;
    public final GolemGoalSelector goalSelector;
    public final GolemGoalSelector targetSelector;
    public final GolemBasicAbilityProperties properties;
    public ExtraAbilityDataMap extraAbilitiesData = new ExtraAbilityDataMap();

    public GolemAbilitiesData(GolemGoalSelector goalSelector, GolemGoalSelector targetSelector, GolemBasicAbilityProperties properties) {
        this.goalSelector = goalSelector;
        this.targetSelector = targetSelector;
        this.properties = properties;
        resetToProperties();
        this.extraAbilitiesData.setTo(this.properties.extraProperties);
    }

    protected void resetToProperties() {
        this.attackDelay = this.properties.attackDelay;
        this.attackFireTicks = this.properties.attackFireTicks;
        this.carryPower = this.properties.carryPower;
        this.homeRange = this.properties.basicHomeRange;
        this.inventorySize = this.properties.basicInventorySize;
        this.inventorySizeMultiplier = this.properties.inventorySizeMultiplier;
        this.regenAmount = this.properties.regenAmount;
        this.regenDelay = this.properties.regenDelay;
        this.strength = this.properties.strength;
        this.speedMultiplier = this.properties.basicSpeedMultiplier;
    }

    public void reset() {
        resetToProperties();
        this.extraAbilitiesData.restoreTo(properties.extraProperties);
    }

    public void safetyCheckAndFix() {
        this.attackDelay = Math.max(this.attackDelay, 1);
        this.attackFireTicks = Math.max(this.attackFireTicks, 0);
        this.carryPower = Math.max(this.carryPower, 0);
        this.regenAmount = Math.max(this.regenAmount, 0);
        this.regenDelay = Math.max(this.regenDelay, 1);
        this.strength = Math.max(this.strength, 0);
        this.speedMultiplier = Math.max(this.speedMultiplier, 10E-4F);
        this.inventorySize = Math.max(this.inventorySize, 0);
        this.inventorySizeMultiplier = Math.max(this.inventorySizeMultiplier, 0);
        this.homeRange = Math.max(this.homeRange, 0);
        this.extraAbilitiesData.safetyCheckAndFix();
    }
}
