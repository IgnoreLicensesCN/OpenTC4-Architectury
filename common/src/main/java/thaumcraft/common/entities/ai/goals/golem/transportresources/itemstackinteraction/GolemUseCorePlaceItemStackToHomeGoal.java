package thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction;

import it.unimi.dsi.fastutil.bytes.ByteArraySet;
import it.unimi.dsi.fastutil.bytes.ByteSet;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.golems.BaseGolemEntity;
import thaumcraft.common.entities.golems.GolemHelper;
import thaumcraft.common.entities.golems.Marker;

import static thaumcraft.common.entities.ai.goals.golem.transportresources.itemstackinteraction.GolemPlaceItemStackToHomeGoalWithItemStackTickMark.ItemStackSortingTickMarkFakeField.getOrCreateAndSetTickMarkFrom;
import static thaumcraft.common.entities.golems.configuration.GolemConfigurationKey.Default.USE_CORE_BLOCK_OR_EMPTY;

public class GolemUseCorePlaceItemStackToHomeGoal extends GolemPlaceItemStackToHomeGoalWithItemStackTickMark {

    public GolemUseCorePlaceItemStackToHomeGoal(BaseGolemEntity golem) {
        super(golem);
    }

    protected boolean canUseAdditional() {
        return !findSomethingUseCore(this.golem, this.golem.getMainHandItem(),colorsCache);
    }

    protected final ByteSet colorsCache = new ByteArraySet(17);

    public static Marker findSomethingUseCoreReturnMarker(BaseGolemEntity golem, ItemStack itemToMatch,ByteSet colorsCache) {
        var useBlockFlag = golem.configurationProvidersView.contains(USE_CORE_BLOCK_OR_EMPTY)
                ? USE_CORE_BLOCK_OR_EMPTY.provider.getBooleanFrom(golem)
                : USE_CORE_BLOCK_OR_EMPTY.provider.defaultValue;
        GolemHelper.getColorsMatching(colorsCache,golem, itemToMatch);
        var level = golem.level();
        for (var marker : golem.markers) {
            if (marker.colorEqualsFuzzy(colorsCache)) {
                var isBlockFlag = !level.getBlockState(marker.getPos()).isAir();
                if (isBlockFlag == useBlockFlag) {
                    if (level.getBlockState(marker.getPos().relative(marker.getSide())).isAir()) {
                        return marker;
                    }
                }
            }
        }
        return null;
    }
    public static boolean findSomethingUseCore(BaseGolemEntity golem, ItemStack itemToMatch,ByteSet colorsCache) {
        if (findSomethingUseCoreReturnMarker(golem, itemToMatch, colorsCache) != null) {
            return true;
        }

        getOrCreateAndSetTickMarkFrom(
                itemToMatch,
                golem.getId(),
                golem.tickCount + Config.CONFIG_INSTANCE.getGolemIgnoreDelayTicks()
        );
        return false;
    }
}
