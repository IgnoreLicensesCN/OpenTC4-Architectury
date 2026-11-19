package thaumcraft.api.listeners.elementalhoe.check;

import lombok.AllArgsConstructor;
import net.minecraft.world.level.block.BonemealableBlock;
import org.jetbrains.annotations.NotNull;
import thaumcraft.api.listeners.elementalhoe.IElementalHoeAffectiveBlock;

import static thaumcraft.common.blocks.ThaumcraftBlocks.Tags.ELEMENTAL_HOE_AFFECTIVE;
import static thaumcraft.common.blocks.ThaumcraftBlocks.Tags.ELEMENTAL_HOE_NOT_AFFECTIVE;

public abstract class ElementalHoeAffectiveChecker implements Comparable<ElementalHoeAffectiveChecker> {
    public final int priority;

    public ElementalHoeAffectiveChecker(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(@NotNull ElementalHoeAffectiveChecker o) {
        return Integer.compare(this.priority, o.priority);
    }

    public abstract void test(ElementalHoeAffectiveContext context);

    @AllArgsConstructor
    public enum DefaultImpl {
        TAG_FORBIDDEN(new ElementalHoeAffectiveChecker(0) {
            @Override
            public void test(ElementalHoeAffectiveContext context) {
                tagForbidden(context);
            }
        }),
        TAG_ALLOW(
                new ElementalHoeAffectiveChecker(100) {
                    @Override
                    public void test(ElementalHoeAffectiveContext context) {
                        tagAllow(context);
                    }
                }
        ),
        BONEMEALABLE(
                new ElementalHoeAffectiveChecker(200) {
                    @Override
                    public void test(ElementalHoeAffectiveContext context) {
                        bonemealable(context);
                    }
                }
        ),
        INSTANCE_AFFECTIVE(
                new ElementalHoeAffectiveChecker(300) {
                    @Override
                    public void test(ElementalHoeAffectiveContext context) {
                        instanceAffective(context);
                    }
                }
        ),
        ;

        public static void instanceAffective(ElementalHoeAffectiveContext context) {
            if (context.state instanceof IElementalHoeAffectiveBlock){
                context.recommendAffective = true;
                context.endCheck = true;
            }
        }

        public static void bonemealable(ElementalHoeAffectiveContext context) {
            if (context.state instanceof BonemealableBlock bonemealable
                    && bonemealable.isValidBonemealTarget(context.level, context.pos, context.state, context.level.isClientSide)
            ){
                context.recommendAffective = true;
                context.endCheck = true;
            }
        }

        public static void tagAllow(ElementalHoeAffectiveContext context) {
            if (context.state.is(ELEMENTAL_HOE_AFFECTIVE)){
                context.recommendAffective = true;
                context.endCheck = true;
            }
        }

        public static void tagForbidden(ElementalHoeAffectiveContext context) {
            if (context.state.is(ELEMENTAL_HOE_NOT_AFFECTIVE)){
                context.recommendAffective = false;
                context.endCheck = true;
            }
        }
        public final ElementalHoeAffectiveChecker instance;
    }
}
