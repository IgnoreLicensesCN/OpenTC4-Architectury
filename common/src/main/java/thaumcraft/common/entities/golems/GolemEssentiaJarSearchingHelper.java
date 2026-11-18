package thaumcraft.common.entities.golems;

import com.linearity.opentc4.utils.BlockSearchDeque;
import com.linearity.opentc4.utils.collectionlike.ListenerManager;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IAspectFilterAccessibleBlockEntity;
import thaumcraft.api.aspects.essentiabe.IEssentiaOverflowDestroyerBlockEntity;
import thaumcraft.api.aspects.essentiabe.IEssentiaTransportInBlockEntity;
import thaumcraft.api.aspects.essentiabe.IEssentiaTransportOutBlockEntity;

public class GolemEssentiaJarSearchingHelper {
    //lower priority first
    //we wont make sure blockEntity could be used to store aspect here you need to test in other ways
    public static class GolemSearchingEssentiaJarPriority implements Comparable<GolemSearchingEssentiaJarPriority> {
        @AllArgsConstructor
        public static abstract class PriorityCalculator implements Comparable<PriorityCalculator> {
            @RequiredArgsConstructor
            public static class PriorityCalculatorContext {
                public @NotNull IEssentiaTransportInBlockEntity inBE;//== maybeEssentiaJar
                public @NotNull Aspect expectedAspect = Aspect.EMPTY;
                public int expectAmount = 0;
                public @NotNull Direction interactionDirection = Direction.UP;
                public @Nullable BaseGolemEntity golem;
                public @NotNull BlockEntity maybeEssentiaJarToFillIn = (BlockEntity) inBE;//we want to insert aspect here

                public void setInBE(@NotNull IEssentiaTransportInBlockEntity inBE) {
                    this.inBE = inBE;
                    this.maybeEssentiaJarToFillIn = (BlockEntity) inBE;
                }
            }

            public static final ListenerManager<PriorityCalculator> PRIORITY_CALCULATORS = new ListenerManager<>();
            public final int weight;//calc order

            public int compareTo(PriorityCalculator o) {
                return Integer.compare(weight, o.weight);
            }

            public abstract int getPriorityFrom(PriorityCalculatorContext calculatorContext);

            @AllArgsConstructor
            public enum DefaultImpl {
                FILTERED_FIRST(new PriorityCalculator(100) {
                    @Override
                    public int getPriorityFrom(PriorityCalculatorContext calculatorContext) {
                        return getPriorityMakesFilteredFirst(calculatorContext);
                    }
                }),
                EMPTY_JAR_AFTER_NOT_EMPTY(new PriorityCalculator(200) {
                    @Override
                    public int getPriorityFrom(PriorityCalculatorContext calculatorContext) {
                        return getPriorityMakesEmptyJarAfterNotEmpty(calculatorContext);
                    }
                }),
                WONT_OVERFLOW_INTO_VOID(new PriorityCalculator(300) {
                    @Override
                    public int getPriorityFrom(PriorityCalculatorContext calculatorContext) {

                        return getPriorityMakesOverflowDestroyedAfter(calculatorContext);
                    }
                }),
                ;

                public final PriorityCalculator calculator;

                public static int getPriorityMakesOverflowDestroyedAfter(PriorityCalculatorContext calculatorContext) {
                    if (calculatorContext.maybeEssentiaJarToFillIn instanceof IEssentiaTransportOutBlockEntity outBE && outBE instanceof IEssentiaOverflowDestroyerBlockEntity destroyerBlockEntity) {
                        var expectedAspect = calculatorContext.expectedAspect;
                        var dir = calculatorContext.interactionDirection;
                        if (destroyerBlockEntity.willDestroy(expectedAspect)) {
                            return Integer.MAX_VALUE;
                        }
                        return 0;
//                        int remainingSpace =
//                                calculatorContext.inBE.getEssentiaAmount(dir)
//                                        - outBE.getEssentiaAmount(dir);
//                        return remainingSpace - expectAmount;
                    }
                    return Integer.MIN_VALUE;
                }

                public static int getPriorityMakesEmptyJarAfterNotEmpty(PriorityCalculatorContext calculatorContext) {
                    if (calculatorContext.maybeEssentiaJarToFillIn instanceof IEssentiaTransportOutBlockEntity outBE) {
                        var dir = calculatorContext.interactionDirection;
                        var type = outBE.getEssentiaType(dir);
                        if (type.isEmpty() || outBE.getEssentiaAmount(dir) == 0) {
                            if (outBE instanceof IEssentiaOverflowDestroyerBlockEntity) {//nonVoid first
                                return 100;
                            }
                            return 0;
                        }
                    }
                    return -100;
                }

                public static int getPriorityMakesFilteredFirst(PriorityCalculatorContext calculatorContext) {
                    if (calculatorContext.maybeEssentiaJarToFillIn instanceof IAspectFilterAccessibleBlockEntity filterAccessible) {
                        return filterAccessible.getAspectFilter().isEmpty() ? 0 : 1;
                    }
                    return -1;
                }
            }

            public static void init() {
                for (var priorityCalcEnum : DefaultImpl.values()) {
                    PRIORITY_CALCULATORS.registerListener(priorityCalcEnum.calculator);
                }
            }
        }

        public final int[] priorities = new int[PriorityCalculator.PRIORITY_CALCULATORS.getListeners().size()];

        public void calculateFor(PriorityCalculator.PriorityCalculatorContext calculatorContext) {
            var listeners = PriorityCalculator.PRIORITY_CALCULATORS.getListeners();
            for (int i = 0; i < listeners.size(); i++) {
                priorities[i] = listeners.get(i).getPriorityFrom(calculatorContext);
            }
        }

        @Override
        public int compareTo(@NotNull GolemEssentiaJarSearchingHelper.GolemSearchingEssentiaJarPriority o) {
            if (this.priorities.length != o.priorities.length) {
                throw new IllegalArgumentException("priorities length not same!did anyone added calculator during game?");
            }
            for (int i = 0; i < this.priorities.length; i++) {
                if (this.priorities[i] != o.priorities[i]) {
                    return Integer.compare(this.priorities[i], o.priorities[i]);
                }
            }
            return 0;
        }

        public static void init() {
            PriorityCalculator.init();
        }
    }

    public static void init() {
        GolemSearchingEssentiaJarPriority.init();
    }

    public static @Nullable BlockEntity getBestEssentiaJarForConnectedJars(BaseGolemEntity golem,Level level, BlockPos pos,Aspect expectedAspect,int expectedAmount) {
        var deque = new BlockSearchDeque();
        var checkingPos = new BlockPos.MutableBlockPos();
        GolemSearchingEssentiaJarPriority checkingPriority = new GolemSearchingEssentiaJarPriority();
        GolemSearchingEssentiaJarPriority.PriorityCalculator.PriorityCalculatorContext calculatorContext = null;
        deque.addIfChecked(pos.getX(), pos.getY(), pos.getZ());

//        Direction dirForBest = Direction.UP;
        GolemSearchingEssentiaJarPriority bestPriority = null;
        double distSqrFromHomeForBest = Double.POSITIVE_INFINITY;
        BlockEntity bestBE = null;

        double rangeSqrSqr = golem.getHomeRange();
        rangeSqrSqr *= rangeSqrSqr;
        rangeSqrSqr *= rangeSqrSqr;//azanor wrote this(dist < rangeSqr) probably a bug but i want to keep it,vote in democracy to change?


        while (!deque.isEmpty()){
            int currentX = deque.popX();
            int currentY = deque.popY();
            int currentZ = deque.popZ();
            var be = level.getBlockEntity(checkingPos.set(currentX, currentY, currentZ));
            if (be instanceof IEssentiaTransportInBlockEntity inBE) {
                if (calculatorContext == null){
                    calculatorContext = new GolemSearchingEssentiaJarPriority.PriorityCalculator.PriorityCalculatorContext(inBE);
                }

                calculatorContext.setInBE(inBE);
                calculatorContext.expectedAspect = expectedAspect;
                calculatorContext.expectAmount = expectedAmount;
                calculatorContext.golem = golem;

                for (var dir:Direction.values()){
                    calculatorContext.interactionDirection = dir;
                    //TODO verify and then calc
                    if (inBE.canInputFrom(dir) && !(inBE.isFullFor(expectedAspect, dir))){
                        checkingPriority.calculateFor(calculatorContext);
                        var distSqrHome = be.getBlockPos().distSqr(golem.getRestrictCenter());
                        if (
                                bestPriority == null ||
                                (bestPriority.compareTo(checkingPriority) >= 0
                                    && distSqrHome < rangeSqrSqr
                                    && distSqrHome < distSqrFromHomeForBest)
                        ){
                            {
                                var priorityCache = bestPriority;
                                bestPriority = checkingPriority;
                                checkingPriority = priorityCache;
                            }//exchange value,we're going to override checkingPriority next run
                            distSqrFromHomeForBest = distSqrHome;
                            bestBE = be;
//                            dirForBest = dir;
                        }
                        break;
                    }
                }
                for (var dir:Direction.values()){
                    deque.addIfChecked(
                            pos.getX() + dir.getStepX(),
                            pos.getY() + dir.getStepY(),
                            pos.getZ() + dir.getStepZ()
                    );
                }
            }
        }
        return bestBE;
    }
}
