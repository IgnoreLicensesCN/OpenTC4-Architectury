package thaumcraft.api.effects;

/**
 *if your #getVisCostAddPercentage returns > 0 in same category the max value in category will be used.
 * if returns <=0,the min value will be used.
 * getVisCostAddPercentage should always return >0 or <=0 in the same category.
 * I will not throw error for different sign.
 */
public interface VisCostAddEffectWithCategory {
    String getVisCostAddCategory();
    int getVisCostAddPercentage(int amplifier);
}
