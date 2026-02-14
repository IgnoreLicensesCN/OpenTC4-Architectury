package thaumcraft.api.nodes;

import com.linearity.opentc4.utils.vanilla1710.MathHelper;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.resourcelocations.NodeModifierResourceLocation;
import thaumcraft.common.tiles.abstracts.AbstractNodeBlockEntity;
import thaumcraft.common.tiles.crafted.EnergizedAuraNodeBlockEntity;

import java.util.*;


public class NodeModifier {

    private static final Map<NodeModifierResourceLocation, NodeModifier> BY_NAME = new LinkedHashMap<>();
    private static final List<NodeModifier> VALUES = new ArrayList<>();

    // 预定义常量
    public static final NodeModifier BRIGHT = new NodeModifier(NodeModifierResourceLocation.of(Thaumcraft.MOD_ID,"bright"),400,1.5f){
        @Override
        public int getAttackAnotherNodePeriod(AbstractNodeBlockEntity thisNode) {
            return 1;
        }

        @Override
        public void onPeriodicReduceSize(AbstractNodeBlockEntity thisNode) {
            var level = thisNode.getLevel();
            if (level != null){
                if (level.getRandom().nextInt(5) == 0){
                    thisNode.setNodeModifier(EMPTY);
                }
            }
        }

        @Override
        public int onSetupEnergizedNodeAspectAmount(EnergizedAuraNodeBlockEntity node, Aspect aspect, int centiVisAmount) {
            return super.onSetupEnergizedNodeAspectAmount(node, aspect, (int)(centiVisAmount*1.2));
        }
    };
    public static final NodeModifier PALE   = new NodeModifier(NodeModifierResourceLocation.of(Thaumcraft.MOD_ID,"pale"),900,1.f){
        @Override
        public void onAttackAnotherNode(AbstractNodeBlockEntity thisNode, AbstractNodeBlockEntity beingAttacked, Aspect stoleAspect) {
            var level = thisNode.getLevel();
            if (level == null){
                level = beingAttacked.getLevel();
            }
            if (level != null && Platform.getEnvironment() == Env.SERVER) {
                if (level.random.nextInt(100) == 0){
                    thisNode.setNodeModifier(null);
                    thisNode.regenerationTickPeriod = -1;//feature:attack another node to remove "pale"(chance:1/100)
                }
            }
            super.onAttackAnotherNode(thisNode, beingAttacked, stoleAspect);
        }
        @Override
        public int getAttackAnotherNodePeriod(AbstractNodeBlockEntity thisNode) {
            return 3;
        }

        @Override
        public boolean allowToAttackAnotherNode(AbstractNodeBlockEntity thisNode) {
            var level = thisNode.getLevel();
            if (level != null){
                return !level.random.nextBoolean();
            }
            return true;
        }

        @Override
        public void onPeriodicReduceSize(AbstractNodeBlockEntity thisNode) {
            var level = thisNode.getLevel();
            if (level != null){
                if (level.getRandom().nextInt(25) == 0){
                    thisNode.setNodeModifier(FADING);
                }
            }
        }

        @Override
        public int onSetupEnergizedNodeAspectAmount(EnergizedAuraNodeBlockEntity node, Aspect aspect, int centiVisAmount) {
            return super.onSetupEnergizedNodeAspectAmount(node, aspect, (int)(centiVisAmount*0.8));
        }
    };
    public static final NodeModifier FADING = new NodeModifier(NodeModifierResourceLocation.of(Thaumcraft.MOD_ID,"fading"),0,1.f){
        @Override
        public boolean allowToAttackAnotherNode(AbstractNodeBlockEntity thisNode) {
            return false;
        }

        @Override
        public int onSetupEnergizedNodeAspectAmount(EnergizedAuraNodeBlockEntity node, Aspect aspect, int centiVisAmount) {
            return super.onSetupEnergizedNodeAspectAmount(node, aspect, (int)(centiVisAmount*0.5));
        }
    };
    public static final NodeModifier EMPTY = new NodeModifier(NodeModifierResourceLocation.of(Thaumcraft.MOD_ID,"empty"),600,1.f){
        @Override
        public void onPeriodicReduceSize(AbstractNodeBlockEntity thisNode) {
            var level = thisNode.getLevel();
            if (level != null){
                if (level.getRandom().nextInt(5) == 0){
                    thisNode.setNodeModifier(PALE);
                }
            }
        }
    };

    private final NodeModifierResourceLocation name;
    private final int regenValue;
    //when doing a node attack:
    // if another node has aspectAmount more than this node's aspect capacity(for same aspect)
    // we will have a chance ofAspectVisList 1/(1 + (int) ((double) aspectCapacityAmountOfThis / attackBiggerNodeChangeModifier))
    // to attack that 'bigger' node
    // this value will pick Math.max(NodeModifier's[default:1],NodeType's[default:1]) for a node
    private final float attackBiggerNodeChangeModifier;

    public NodeModifier(NodeModifierResourceLocation name,int regenValue,float attackBiggerNodeChangeModifier) {
        this.name = name;
        this.regenValue = regenValue;
        this.attackBiggerNodeChangeModifier = attackBiggerNodeChangeModifier;
        register(this);
    }

//    /** 注册新的 NodeModifier，可用于动态扩展 */
//    public static NodeModifier register(String name,int regenValue,float attackBiggerNodeChangeModifier) {
//        if (BY_NAME.containsKey(name)) {
//            throw new IllegalArgumentException("NodeModifier already exists: " + name);
//        }
//        return new NodeModifier(name,regenValue,attackBiggerNodeChangeModifier);
//    }

    /** 内部注册方法 */
    private static void register(NodeModifier modifier) {
        BY_NAME.put(modifier.name, modifier);
        VALUES.add(modifier);
    }

    /** 返回所有 NodeModifier（顺序和定义顺序一致） */
    public static List<NodeModifier> values() {
        return Collections.unmodifiableList(VALUES);
    }

    /** 类似 enum 的 valueOf 方法 */
    public static NodeModifier valueOf(NodeModifierResourceLocation name) {
        NodeModifier modifier = BY_NAME.get(name);
        if (modifier == null) {
            throw new IllegalArgumentException("No enum constant NodeModifier." + name);
        }
        return modifier;
    }

    /** 返回名称 */
    public NodeModifierResourceLocation name() {
        return name;
    }

    @Override
    public String toString() {
        return name.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NodeModifier mod)) return false;
        return Objects.equals(name, mod.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    public int getRegenValue(AbstractNodeBlockEntity node) {
        return regenValue;
    }

    public float getAttackBiggerNodeChangeModifier() {
        return attackBiggerNodeChangeModifier;
    }

    public void onAttackAnotherNode(AbstractNodeBlockEntity thisNode, AbstractNodeBlockEntity beingAttacked, Aspect stoleAspect) {

        var level = thisNode.getLevel();
        if (level == null){
            level = beingAttacked.getLevel();
        }
        if (level == null){
            return;
        }
        if (level.random.nextInt(3) == 0) {
            beingAttacked.setNodeVisBase(
                    stoleAspect,
                    (short) (beingAttacked.getNodeVisBase(stoleAspect) - 1)
            );
        }
    }
    public boolean allowToAttackAnotherNode(AbstractNodeBlockEntity thisNode) {
        return true;
    }
    public int getAttackAnotherNodePeriod(AbstractNodeBlockEntity thisNode){
        return 2;
    }
    public void onPeriodicReduceSize(AbstractNodeBlockEntity thisNode){

    }

    public int onSetupEnergizedNodeAspectAmount(EnergizedAuraNodeBlockEntity node, Aspect aspect, int centiVisAmount){
        return MathHelper.floor_double(MathHelper.sqrt_double(centiVisAmount));
    }
}