package thaumcraft.common.lib.world.biomes;

import thaumcraft.api.aspects.Aspect;

/**
 * it's not a good habit to use {@code List<Object>} everywhere.
 * so {@code List<Object>} -> BiomeInfo.Much more readable.
 */
public class BiomeInfo {
    public int getAuraLevel() {
        return auraLevel;
    }

    public void setAuraLevel(int auraLevel) {
        this.auraLevel = auraLevel;
    }

    public Aspect getTag() {
        return tag;
    }

    public void setTag(Aspect tag) {
        this.tag = tag;
    }

    public boolean isGreatwood() {
        return greatwood;
    }

    public void setGreatwood(boolean greatwood) {
        this.greatwood = greatwood;
    }

    public float getGreatwoodchance() {
        return greatwoodchance;
    }

    public void setGreatwoodchance(float greatwoodchance) {
        this.greatwoodchance = greatwoodchance;
    }

    public BiomeInfo(int auraLevel, Aspect tag, boolean greatwood, float greatwoodchance) {
        this.auraLevel = auraLevel;
        this.tag = tag;
        this.greatwood = greatwood;
        this.greatwoodchance = greatwoodchance;
    }

    private int auraLevel;
    private Aspect tag;
    private boolean greatwood;
    private float greatwoodchance;
}
