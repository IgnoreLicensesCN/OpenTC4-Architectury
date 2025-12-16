package thaumcraft.api.nodes;

import java.util.*;

public class NodeModifier {

    private static final Map<String, NodeModifier> BY_NAME = new LinkedHashMap<>();
    private static final List<NodeModifier> VALUES = new ArrayList<>();

    // 预定义常量
    public static final NodeModifier BRIGHT = new NodeModifier("BRIGHT",400);
    public static final NodeModifier PALE   = new NodeModifier("PALE",900);
    public static final NodeModifier FADING = new NodeModifier("FADING",0);

    private final String name;
    private final int regenValue;

    // 构造函数私有，防止外部直接 new
    private NodeModifier(String name,int regenValue) {
        this.name = name;
        this.regenValue = regenValue;
        register(this);
    }

    /** 注册新的 NodeModifier，可用于动态扩展 */
    public static NodeModifier register(String name,int regenValue) {
        if (BY_NAME.containsKey(name)) {
            throw new IllegalArgumentException("NodeModifier already exists: " + name);
        }
        return new NodeModifier(name,regenValue);
    }

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
    public static NodeModifier valueOf(String name) {
        NodeModifier modifier = BY_NAME.get(name);
        if (modifier == null) {
            throw new IllegalArgumentException("No enum constant NodeModifier." + name);
        }
        return modifier;
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
        if (!(o instanceof NodeModifier mod)) return false;
        return Objects.equals(name, mod.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    public int getRegenValue() {
        return regenValue;
    }
}