# import re
#
# code = """//PRIMAL
#     public static final PrimalAspect AIR = new PrimalAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "aer"),0xffff7e,"e",1);
#     public static final PrimalAspect EARTH = new PrimalAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "terra"),0x56c000,"2",1);
#     public static final PrimalAspect FIRE = new PrimalAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "ignis"),0xff5a01,"c",1);
#     public static final PrimalAspect WATER = new PrimalAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "aqua"),0x3cd4fc,"3",1);
#     public static final PrimalAspect ORDER = new PrimalAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "ordo"),0xd5d4ec,"7",1);
#     public static final PrimalAspect ENTROPY = new PrimalAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "perditio"),0x404040,"8",771);
#
#     //SECONDARY
#     public static final CompoundAspect VOID = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "vacuos"),0x888888, CompoundAspectComponent.of(AIR, ENTROPY),771);
#     public static final CompoundAspect LIGHT = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "lux"),0xfff663, CompoundAspectComponent.of(AIR, FIRE));
#     public static final CompoundAspect WEATHER = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "tempestas"),0xFFFFFF, CompoundAspectComponent.of(AIR, WATER));
#     public static final CompoundAspect MOTION = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "motus"),0xcdccf4, CompoundAspectComponent.of(AIR, ORDER));
#     public static final CompoundAspect COLD = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "gelum"),0xe1ffff, CompoundAspectComponent.of(FIRE, ENTROPY));
#     public static final CompoundAspect CRYSTAL = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "vitreus"),0x80ffff, CompoundAspectComponent.of(EARTH, ORDER));
#     public static final CompoundAspect LIFE = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "victus"),0xde0005, CompoundAspectComponent.of(WATER, EARTH));
#     public static final CompoundAspect POISON = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "venenum"),0x89f000,  CompoundAspectComponent.of(WATER, ENTROPY));
#     public static final CompoundAspect ENERGY = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "potentia"),0xc0ffff, CompoundAspectComponent.of(ORDER, FIRE));
#     public static final CompoundAspect EXCHANGE = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "permutatio"),0x578357, CompoundAspectComponent.of(ENTROPY, ORDER));
# //		public static final CompoundAspect ?? = new CompoundAspect("??",0xcdccf4, CompoundAspectComponent.of(AIR, EARTH));
# //		public static final CompoundAspect ?? = new CompoundAspect("??",0xcdccf4, CompoundAspectComponent.of(FIRE, EARTH));
# //		public static final CompoundAspect ?? = new CompoundAspect("??",0xcdccf4, CompoundAspectComponent.of(FIRE, WATER));
# //		public static final CompoundAspect ?? = new CompoundAspect("??",0xcdccf4, CompoundAspectComponent.of(ORDER, WATER));
# //		public static final CompoundAspect ?? = new CompoundAspect("??",0xcdccf4, CompoundAspectComponent.of(EARTH, ENTROPY));
#
#     //TERTIARY
#     public static final CompoundAspect METAL = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "metallum"),0xb5b5cd, CompoundAspectComponent.of(EARTH, CRYSTAL));
#     public static final CompoundAspect DEATH = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "mortuus"),0x887788, CompoundAspectComponent.of(LIFE, ENTROPY));
#     public static final CompoundAspect FLIGHT = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "volatus"),0xe7e7d7, CompoundAspectComponent.of(AIR, MOTION));
#     public static final CompoundAspect DARKNESS = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "tenebrae"),0x222222, CompoundAspectComponent.of(VOID, LIGHT));
#     public static final CompoundAspect SOUL = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "spiritus"),0xebebfb, CompoundAspectComponent.of(LIFE, DEATH));
#     public static final CompoundAspect HEAL = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "sano"),0xff2f34, CompoundAspectComponent.of(LIFE, ORDER));
#     public static final CompoundAspect TRAVEL = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "iter"),0xe0585b, CompoundAspectComponent.of(MOTION, EARTH));
#     public static final CompoundAspect ELDRITCH = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "alienis"),0x805080, CompoundAspectComponent.of(VOID, DARKNESS));
#     public static final CompoundAspect MAGIC = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "praecantatio"),0x9700c0, CompoundAspectComponent.of(VOID, ENERGY));
#     public static final CompoundAspect AURA = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "auram"),0xffc0ff, CompoundAspectComponent.of(MAGIC, AIR));
#     public static final CompoundAspect TAINT = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "vitium"),0x800080, CompoundAspectComponent.of(MAGIC, ENTROPY));
#     public static final CompoundAspect SLIME = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "limus"),0x01f800, CompoundAspectComponent.of(LIFE, WATER));
#     public static final CompoundAspect PLANT = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "herba"),0x01ac00, CompoundAspectComponent.of(LIFE, EARTH));
#     public static final CompoundAspect TREE = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "arbor"),0x876531, CompoundAspectComponent.of(AIR, PLANT));
#     public static final CompoundAspect BEAST = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "bestia"),0x9f6409, CompoundAspectComponent.of(MOTION, LIFE));
#     public static final CompoundAspect FLESH = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "corpus"),0xee478d, CompoundAspectComponent.of(DEATH, BEAST));
#     public static final CompoundAspect UNDEAD = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "exanimis"),0x3a4000, CompoundAspectComponent.of(MOTION, DEATH));
#     public static final CompoundAspect MIND = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "cognitio"),0xffc2b3, CompoundAspectComponent.of(FIRE, SOUL));
#     public static final CompoundAspect SENSES = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "sensus"),0x0fd9ff, CompoundAspectComponent.of(AIR, SOUL));
#     public static final CompoundAspect MAN = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "humanus"),0xffd7c0, CompoundAspectComponent.of(BEAST, MIND));
#     public static final CompoundAspect CROP = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "messis"),0xe1b371, CompoundAspectComponent.of(PLANT, MAN));
#     public static final CompoundAspect MINE = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "perfodio"),0xdcd2d8, CompoundAspectComponent.of(MAN, EARTH));
#     public static final CompoundAspect TOOL = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "instrumentum"),0x4040ee, CompoundAspectComponent.of(MAN, ORDER));
#     public static final CompoundAspect HARVEST = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "meto"),0xeead82, CompoundAspectComponent.of(CROP, TOOL));
#     public static final CompoundAspect WEAPON = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "telum"),0xc05050, CompoundAspectComponent.of(TOOL, FIRE));
#     public static final CompoundAspect ARMOR = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "tutamen"),0x00c0c0, CompoundAspectComponent.of(TOOL, EARTH));
#     public static final CompoundAspect HUNGER = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "fames"),0x9a0305, CompoundAspectComponent.of(LIFE, VOID));
#     public static final CompoundAspect GREED = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "lucrum"),0xe6be44, CompoundAspectComponent.of(MAN, HUNGER));
#     public static final CompoundAspect CRAFT = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "fabrico"),0x809d80, CompoundAspectComponent.of(MAN, TOOL));
#     public static final CompoundAspect CLOTH = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "pannus"),0xeaeac2, CompoundAspectComponent.of(TOOL, BEAST));
#     public static final CompoundAspect MECHANISM = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "machina"),0x8080a0, CompoundAspectComponent.of(MOTION, TOOL));
#     public static final CompoundAspect TRAP = new CompoundAspect(AspectResourceLocation.of(Thaumcraft.MOD_ID, "vinculum"),0x9a8080, CompoundAspectComponent.of(MOTION, ENTROPY));"""
# regexStr = r'AspectResourceLocation\.of\([^,]+,\s*"([^"]+)"\),\s*(0x[a-fA-F0-9]+)'
# aspectColorPattern = re.compile(r'AspectResourceLocation\.of\([^,]+,\s*"([^"]+)"\),\s*(0x[a-fA-F0-9]+)')
# matches = aspectColorPattern.findall(code)
#
# # 转换为一个干净的字典结构
# color_dict = {}
# for aspect_name, hex_str in matches:
#     # 将 "0xd5d4ec" 统一规范化为小写，方便以后读取
#     colorValue = int(hex_str, 16)
#     b = colorValue % 256
#     colorValue //= 256
#     g = colorValue % 256
#     colorValue //= 256
#     r = colorValue % 256
#     color_dict[aspect_name] = (r,g,b)
#
# print(color_dict)
#
ASPECTS_TO_COLORS = {'aer': (255, 255, 126), 'terra': (86, 192, 0), 'ignis': (255, 90, 1), 'aqua': (60, 212, 252),
                     'ordo': (213, 212, 236), 'perditio': (64, 64, 64), 'vacuos': (136, 136, 136),
                     'lux': (255, 246, 99), 'tempestas': (255, 255, 255), 'motus': (205, 204, 244),
                     'gelum': (225, 255, 255), 'vitreus': (128, 255, 255), 'victus': (222, 0, 5),
                     'venenum': (137, 240, 0), 'potentia': (192, 255, 255), 'permutatio': (87, 131, 87),
                     'metallum': (181, 181, 205), 'mortuus': (136, 119, 136), 'volatus': (231, 231, 215),
                     'tenebrae': (34, 34, 34), 'spiritus': (235, 235, 251), 'sano': (255, 47, 52),
                     'iter': (224, 88, 91), 'alienis': (128, 80, 128), 'praecantatio': (151, 0, 192),
                     'auram': (255, 192, 255), 'vitium': (128, 0, 128), 'limus': (1, 248, 0), 'herba': (1, 172, 0),
                     'arbor': (135, 101, 49), 'bestia': (159, 100, 9), 'corpus': (238, 71, 141),
                     'exanimis': (58, 64, 0), 'cognitio': (255, 194, 179), 'sensus': (15, 217, 255),
                     'humanus': (255, 215, 192), 'messis': (225, 179, 113), 'perfodio': (220, 210, 216),
                     'instrumentum': (64, 64, 238), 'meto': (238, 173, 130), 'telum': (192, 80, 80),
                     'tutamen': (0, 192, 192), 'fames': (154, 3, 5), 'lucrum': (230, 190, 68),
                     'fabrico': (128, 157, 128), 'pannus': (234, 234, 194), 'machina': (128, 128, 160),
                     'vinculum': (154, 128, 128)}
