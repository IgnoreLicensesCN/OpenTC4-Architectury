from PIL import Image

animImage = 'runed_tablet.png'                 # 16xN
addWith = 'ancient_lock_empty_face.png'        # 32x32
output = 'ancient_lock_inserted_face.png'      # 32xN

FRAME_SIZE = 16

anim = Image.open(animImage).convert("RGBA")
overlay = Image.open(addWith).convert("RGBA")

ow, oh = overlay.size   # 32, 32
width, height = anim.size

frame_count = height // FRAME_SIZE

# 输出是 32xN
result = Image.new("RGBA", (ow, frame_count * oh))

for i in range(frame_count):
    y = i * FRAME_SIZE

    # 裁剪 16x16 帧
    frame = anim.crop((0, y, FRAME_SIZE, y + FRAME_SIZE))

    # 创建 32x32 透明画布
    canvas = Image.new("RGBA", (ow, oh), (0, 0, 0, 0))

    # 居中位置
    offset = ((ow - FRAME_SIZE) // 2, (oh - FRAME_SIZE) // 2)

    # 把 16x16 贴到中间
    canvas.paste(frame, offset, frame)

    # 叠加到底图
    combined = Image.alpha_composite(overlay, canvas)

    # 粘回结果（按 32x32 一帧）
    result.paste(combined, (0, i * oh))

result.save(output)
print(f"完成，共处理 {frame_count} 帧 -> {output}")
