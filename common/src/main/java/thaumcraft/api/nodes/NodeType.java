package thaumcraft.api.nodes;


import java.util.*;

public class NodeType {
    private static final Map<String, NodeType> BY_NAME = new LinkedHashMap<>();
    private static final List<NodeType> VALUES = new ArrayList<>();

    public static final NodeType NORMAL = new NodeType("NORMAL");
    public static final NodeType UNSTABLE = new NodeType("UNSTABLE");
    public static final NodeType DARK = new NodeType("DARK");
    public static final NodeType TAINTED = new NodeType("TAINTED");
    public static final NodeType HUNGRY = new NodeType("HUNGRY");
    public static final NodeType PURE = new NodeType("PURE");

    private final String name;

    // 构造函数私有，防止外部直接 new
    private NodeType(String name) {
        this.name = name;
        register(this);
    }

    /** 注册新的 NodeType，可用于动态扩展 */
    public static NodeType register(String name) {
        if (BY_NAME.containsKey(name)) {
            throw new IllegalArgumentException("NodeType already exists: " + name);
        }
        return new NodeType(name);
    }

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
}