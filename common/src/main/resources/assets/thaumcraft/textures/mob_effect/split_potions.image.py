from PIL import Image
import os

INPUT_IMAGE = "potions.png"
OUTPUT_DIR = "output"
BLOCK_SIZE = 18
NEW_SIZE = 18

os.makedirs(OUTPUT_DIR, exist_ok=True)

img = Image.open(INPUT_IMAGE).convert("RGBA")
width, height = img.size

if width != 256 or height != 256:
    raise ValueError("输入图片必须是 256x256")

rows = height // BLOCK_SIZE
cols = width // BLOCK_SIZE

for y in range(rows):
    for x in range(cols):
        block = img.crop((x*BLOCK_SIZE, y*BLOCK_SIZE, (x+1)*BLOCK_SIZE, (y+1)*BLOCK_SIZE))

        # 检查整块是否全透明
        if all(p[3] == 0 for p in block.getdata()):
            continue  # 全透明块跳过

        # 新建18x18全透明图
        new_img = Image.new("RGBA", (NEW_SIZE, NEW_SIZE), (0,0,0,0))

        # 左上角粘贴，不做 offset
        new_img.paste(block, (0, 0), block)

        # 保存
        filename = f"block_{y}_{x}.png"
        new_img.save(os.path.join(OUTPUT_DIR, filename))

print("完成！")
