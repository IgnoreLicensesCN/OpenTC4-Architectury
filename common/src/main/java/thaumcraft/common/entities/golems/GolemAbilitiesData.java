package thaumcraft.common.entities.golems;

import thaumcraft.common.entities.golems.properties.ExtraAbilityDataMap;
import thaumcraft.common.entities.golems.properties.GolemBasicAbilityProperties;

public class GolemAbilitiesData {
    public float carryPower;//item carry capacity:*1,fluid carry capacity(mb):*1000
    public float inventorySize = 0;
    public float inventorySizeMultiplier = 0;
    public float attackDelay;
    public float regenDelay;
    public float regenAmount;
    public float strength;
    public float attackFireTicks;
    public float speedMultiplier = 1;
    public final GolemGoalSelector goalSelector;
    public final GolemGoalSelector targetSelector;
    public final GolemBasicAbilityProperties properties;
    public ExtraAbilityDataMap extraAbilitiesData;
    public float homeRange;

    public GolemAbilitiesData(GolemGoalSelector goalSelector, GolemGoalSelector targetSelector, GolemBasicAbilityProperties properties) {
        this.carryPower = properties.carryPower;
        this.attackDelay = properties.attackDelay;
        this.regenDelay = properties.regenDelay;
        this.regenAmount = properties.regenAmount;
        this.strength = properties.strength;
        this.attackFireTicks = properties.attackFireTicks;
        this.goalSelector = goalSelector;
        this.targetSelector = targetSelector;
        this.inventorySizeMultiplier = properties.inventorySizeMultiplier;
        this.homeRange = properties.basicHomeRange;
        this.properties = properties;
        this.extraAbilitiesData = new ExtraAbilityDataMap();
        this.extraAbilitiesData.setTo(properties.extraProperties);
    }

    public void reset() {
        this.carryPower = properties.carryPower;
        this.attackDelay = properties.attackDelay;
        this.regenDelay = properties.regenDelay;
        this.regenAmount = properties.regenAmount;
        this.strength = properties.strength;
        this.attackFireTicks = properties.attackFireTicks;
        this.speedMultiplier = properties.basicSpeedMultiplier;
        this.inventorySize = properties.basicInventorySize;
        this.inventorySizeMultiplier = properties.inventorySizeMultiplier;
        this.homeRange = properties.basicHomeRange;
        this.extraAbilitiesData.restoreTo(properties.extraProperties);
    }

    public void safetyCheckAndFix() {
        this.carryPower = Math.max(this.carryPower, 0);
        this.attackDelay = Math.max(this.attackDelay, 1);
        this.regenDelay = Math.max(this.regenDelay, 1);
        this.speedMultiplier = Math.max(this.speedMultiplier, 10E-4F);
        this.inventorySize = Math.max(this.inventorySize, 0);
        this.regenAmount = Math.max(this.regenAmount, 0);
        this.strength = Math.max(this.strength, 0);
        this.attackFireTicks = Math.max(this.attackFireTicks, 0);
        this.inventorySizeMultiplier = Math.max(this.inventorySizeMultiplier, 0);
        this.homeRange = Math.max(this.homeRange, 0);
        this.extraAbilitiesData.safetyCheckAndFix();
    }
}
