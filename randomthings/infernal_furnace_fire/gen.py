from PIL import Image

animImage = 'fire_0.png'
addWith = 'furnace15.png'
output = 'output.png'

FRAME_SIZE = 16

# 读取图片
anim = Image.open(animImage).convert("RGBA")
overlay = Image.open(addWith).convert("RGBA")

width, height = anim.size
assert width == FRAME_SIZE, "动画图片宽度必须是 16"
assert overlay.size == (FRAME_SIZE, FRAME_SIZE), "叠加图片必须是 16x16"

frame_count = height // FRAME_SIZE

# 创建输出图
result = Image.new("RGBA", anim.size)

for i in range(frame_count):
    y = i * FRAME_SIZE

    # 裁剪当前帧
    frame = anim.crop((0, y, FRAME_SIZE, y + FRAME_SIZE))

    # 叠加（保留透明）
    combined = Image.alpha_composite(frame, overlay)

    # 粘回结果图
    result.paste(combined, (0, y))

# 保存
result.save(output)
print(f"完成，共处理 {frame_count} 帧 -> {output}")
