import os

from PIL import Image
import math
from collections import defaultdict

MAP_COLORS = dict()
mapColorDefinitions = """
	public static final MapColor NONE = new MapColor(0, 0);
	public static final MapColor GRASS = new MapColor(1, 8368696);
	public static final MapColor SAND = new MapColor(2, 16247203);
	public static final MapColor WOOL = new MapColor(3, 13092807);
	public static final MapColor FIRE = new MapColor(4, 16711680);
	public static final MapColor ICE = new MapColor(5, 10526975);
	public static final MapColor METAL = new MapColor(6, 10987431);
	public static final MapColor PLANT = new MapColor(7, 31744);
	public static final MapColor SNOW = new MapColor(8, 16777215);
	public static final MapColor CLAY = new MapColor(9, 10791096);
	public static final MapColor DIRT = new MapColor(10, 9923917);
	public static final MapColor STONE = new MapColor(11, 7368816);
	public static final MapColor WATER = new MapColor(12, 4210943);
	public static final MapColor WOOD = new MapColor(13, 9402184);
	public static final MapColor QUARTZ = new MapColor(14, 16776437);
	public static final MapColor COLOR_ORANGE = new MapColor(15, 14188339);
	public static final MapColor COLOR_MAGENTA = new MapColor(16, 11685080);
	public static final MapColor COLOR_LIGHT_BLUE = new MapColor(17, 6724056);
	public static final MapColor COLOR_YELLOW = new MapColor(18, 15066419);
	public static final MapColor COLOR_LIGHT_GREEN = new MapColor(19, 8375321);
	public static final MapColor COLOR_PINK = new MapColor(20, 15892389);
	public static final MapColor COLOR_GRAY = new MapColor(21, 5000268);
	public static final MapColor COLOR_LIGHT_GRAY = new MapColor(22, 10066329);
	public static final MapColor COLOR_CYAN = new MapColor(23, 5013401);
	public static final MapColor COLOR_PURPLE = new MapColor(24, 8339378);
	public static final MapColor COLOR_BLUE = new MapColor(25, 3361970);
	public static final MapColor COLOR_BROWN = new MapColor(26, 6704179);
	public static final MapColor COLOR_GREEN = new MapColor(27, 6717235);
	public static final MapColor COLOR_RED = new MapColor(28, 10040115);
	public static final MapColor COLOR_BLACK = new MapColor(29, 1644825);
	public static final MapColor GOLD = new MapColor(30, 16445005);
	public static final MapColor DIAMOND = new MapColor(31, 6085589);
	public static final MapColor LAPIS = new MapColor(32, 4882687);
	public static final MapColor EMERALD = new MapColor(33, 55610);
	public static final MapColor PODZOL = new MapColor(34, 8476209);
	public static final MapColor NETHER = new MapColor(35, 7340544);
	public static final MapColor TERRACOTTA_WHITE = new MapColor(36, 13742497);
	public static final MapColor TERRACOTTA_ORANGE = new MapColor(37, 10441252);
	public static final MapColor TERRACOTTA_MAGENTA = new MapColor(38, 9787244);
	public static final MapColor TERRACOTTA_LIGHT_BLUE = new MapColor(39, 7367818);
	public static final MapColor TERRACOTTA_YELLOW = new MapColor(40, 12223780);
	public static final MapColor TERRACOTTA_LIGHT_GREEN = new MapColor(41, 6780213);
	public static final MapColor TERRACOTTA_PINK = new MapColor(42, 10505550);
	public static final MapColor TERRACOTTA_GRAY = new MapColor(43, 3746083);
	public static final MapColor TERRACOTTA_LIGHT_GRAY = new MapColor(44, 8874850);
	public static final MapColor TERRACOTTA_CYAN = new MapColor(45, 5725276);
	public static final MapColor TERRACOTTA_PURPLE = new MapColor(46, 8014168);
	public static final MapColor TERRACOTTA_BLUE = new MapColor(47, 4996700);
	public static final MapColor TERRACOTTA_BROWN = new MapColor(48, 4993571);
	public static final MapColor TERRACOTTA_GREEN = new MapColor(49, 5001770);
	public static final MapColor TERRACOTTA_RED = new MapColor(50, 9321518);
	public static final MapColor TERRACOTTA_BLACK = new MapColor(51, 2430480);
	public static final MapColor CRIMSON_NYLIUM = new MapColor(52, 12398641);
	public static final MapColor CRIMSON_STEM = new MapColor(53, 9715553);
	public static final MapColor CRIMSON_HYPHAE = new MapColor(54, 6035741);
	public static final MapColor WARPED_NYLIUM = new MapColor(55, 1474182);
	public static final MapColor WARPED_STEM = new MapColor(56, 3837580);
	public static final MapColor WARPED_HYPHAE = new MapColor(57, 5647422);
	public static final MapColor WARPED_WART_BLOCK = new MapColor(58, 1356933);
	public static final MapColor DEEPSLATE = new MapColor(59, 6579300);
	public static final MapColor RAW_IRON = new MapColor(60, 14200723);
	public static final MapColor GLOW_LICHEN = new MapColor(61, 8365974);""".split("\n")
for definitionLine in mapColorDefinitions:
    if "public static final MapColor" in definitionLine:
        definitionParts = definitionLine.split(" ")
        while '' in definitionParts:
            definitionParts.remove('')
        MAP_COLORS[definitionParts[4]] = int(definitionParts[8][:-2])


# ===== 工具函数 =====

def rgb_from_int(c):
    return ((c >> 16) & 255, (c >> 8) & 255, c & 255)

def quantize(rgb, bits=4):
    shift = 8 - bits
    return tuple((c >> shift) << shift for c in rgb)

def color_distance(c1, c2):
    # 加权 RGB 距离（感知更合理）
    return (
        0.30 * (c1[0] - c2[0]) ** 2 +
        0.59 * (c1[1] - c2[1]) ** 2 +
        0.11 * (c1[2] - c2[2]) ** 2
    )

# ===== 主逻辑 =====

def analyze_texture(path, threshold=0.6):
    img = Image.open(path).convert("RGBA")
    w, h = img.size
    pixels = img.load()

    freq = defaultdict(int)

    for y in range(h):
        for x in range(w):
            r, g, b, a = pixels[x, y]
            if a < 16:
                continue
            qr, qg, qb = quantize((r, g, b))
            freq[(qr, qg, qb)] += 1

    total = sum(freq.values())
    if total == 0:
        raise RuntimeError("图片中没有有效像素")

    colors = sorted(freq.items(), key=lambda e: e[1], reverse=True)

    acc = 0
    selected = []

    for rgb, count in colors:
        selected.append((rgb, count))
        acc += count
        if acc >= total * threshold:
            break

    # 加权平均
    r = g = b = weight = 0
    for (cr, cg, cb), cnt in selected:
        r += cr * cnt
        g += cg * cnt
        b += cb * cnt
        weight += cnt

    avg = (r / weight, g / weight, b / weight)

    # 找最近 MapColor
    best_name = None
    best_dist = float("inf")

    for name, col in MAP_COLORS.items():
        mc_rgb = rgb_from_int(col)
        dist = color_distance(avg, mc_rgb)
        if dist < best_dist:
            best_dist = dist
            best_name = name

    return avg, best_name, best_dist

# ===== 运行示例 =====

if __name__ == "__main__":
    print(len(MAP_COLORS))
    for texture in os.listdir("pick_map_color_images"):
        print(f"========={texture}==============")
        texture = "./pick_map_color_images/"+texture
        avg_color, map_color, dist = analyze_texture(texture)
        r, g, b = [int(c) for c in avg_color]
        mapColorInt = MAP_COLORS[map_color]
        mapr = (mapColorInt >> 16) & 0xFF
        mapg = (mapColorInt >> 8) & 0xFF
        mapb = mapColorInt & 0xFF
        print(f"\033[38;2;{r};{g};{b}m加权主色: {avg_color}\033[0m")
        print(f"最接近 MapColor:\033[38;2;{mapr};{mapg};{mapb}m{map_color}\033[0m")
        print("距离:", int(dist))
