package thaumcraft.api.nodes;


import thaumcraft.common.tiles.AbstractNodeBlockEntity;

import java.util.*;

public class NodeType {
    private static final Map<String, NodeType> BY_NAME = new LinkedHashMap<>();
    private static final List<NodeType> VALUES = new ArrayList<>();

    public static final NodeType NORMAL = new NodeType("NORMAL",1.f);
    public static final NodeType UNSTABLE = new NodeType("UNSTABLE",1.f);
    public static final NodeType DARK = new NodeType("DARK",1.f);
    public static final NodeType TAINTED = new NodeType("TAINTED",1.f);
    public static final NodeType HUNGRY = new NodeType("HUNGRY",1.5f){
        @Override
        public int getAttackAnotherNodePeriod(AbstractNodeBlockEntity thisNode) {
            return 1;
        }
    };
    public static final NodeType PURE = new NodeType("PURE",1.f);
    public static final NodeType EMPTY = new NodeType("EMPTY",1.f);

    private final String name;
    //when doing a node attack:
    // if another node has aspectAmount more than this node's aspect capacity(for same aspect)
    // we will have a chance of 1/(1 + (int) ((double) aspectCapacityAmountOfThis / attackBiggerNodeChangeModifier))
    // to attack that 'bigger' node
    // this value will pick Math.max(NodeModifier's[default:1],NodeType's[default:1]) for a node
    private final float attackBiggerNodeChangeModifier;

    public NodeType(String name,float attackBiggerNodeChangeModifier) {
        this.name = name;
        this.attackBiggerNodeChangeModifier = attackBiggerNodeChangeModifier;
        register(this);
    }

//    /** 注册新的 NodeType，可用于动态扩展 */
//    public static NodeType register(String name,float attackBiggerNodeChangeModifier) {
//        if (BY_NAME.containsKey(name)) {
//            throw new IllegalArgumentException("NodeType already exists: " + name);
//        }
//        return new NodeType(name,attackBiggerNodeChangeModifier);
//    }

    /** 内部注册方法 */
    private static void register(NodeType type) {
        BY_NAME.put(type.name, type);
        VALUES.add(type);
    }

    /** 返回所有 NodeType（顺序和定义顺序一致） */
    public static List<NodeType> values() {
        return Collections.unmodifiableList(VALUES);
    }

    /** 类似 enum 的 valueOf 方法 */
    public static NodeType valueOf(String name) {
        NodeType type = BY_NAME.get(name);
        if (type == null) {
            throw new IllegalArgumentException("No enum constant NodeType." + name);
        }
        return type;
    }

    /** 返回名称 */
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NodeType type)) return false;
        return Objects.equals(name, type.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    public float getAttackBiggerNodeChangeModifier() {
        return attackBiggerNodeChangeModifier;
    }

    public boolean allowToAttackAnotherNode(AbstractNodeBlockEntity thisNode) {
        return true;
    }
    public int getAttackAnotherNodePeriod(AbstractNodeBlockEntity thisNode){
        return 2;
    }
}