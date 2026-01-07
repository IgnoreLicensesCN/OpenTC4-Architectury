from PIL import Image, ImageDraw, ImageFont

SIZE = 16
PALETTE = [
    (255, 255, 255),  # white
    (255, 85, 85),    # red
    (255, 170, 0),    # orange
    (255, 255, 85),   # yellow
    (85, 255, 85),    # green
    (85, 255, 255),   # cyan
    (85, 85, 255),    # blue
    (170, 85, 255),   # purple
    (255, 85, 255),   # magenta
    (170, 170, 170),  # light gray
]
for i in range(27):
    # 创建透明背景图片
    img = Image.new("RGBA", (SIZE, SIZE), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)

    text = str(i)

    # 使用默认字体（Minecraft 纹理占位足够）
    font = ImageFont.load_default()

    # 计算文本尺寸以居中
    bbox = draw.textbbox((0, 0), text, font=font)
    text_w = bbox[2] - bbox[0]
    text_h = bbox[3] - bbox[1]

    x = (SIZE - text_w) // 2
    y = (SIZE - text_h) // 2

    # 画白色数字
    pickColor = PALETTE[i % len(PALETTE)]
    draw.text((x, y), text, fill=(pickColor[0], pickColor[1], pickColor[2], 255), font=font)

    # 保存
    img.save(f"furnace{i}.png")

i = 27
img = Image.new("RGBA", (SIZE, SIZE), (0, 0, 0, 0))
draw = ImageDraw.Draw(img)

# 保存
img.save(f"furnace{i}.png")

print("Generated furnace0.png ~ furnace26.png")
