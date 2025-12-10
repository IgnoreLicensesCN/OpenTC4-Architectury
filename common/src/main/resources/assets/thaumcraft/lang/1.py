import json
import os

# 配置输入输出文件夹
input_folder = "./"   # 放 .lang 文件的文件夹
output_folder = "json_files"  # 输出 .json 文件的文件夹

os.makedirs(output_folder, exist_ok=True)

for filename in os.listdir(input_folder):
    if not filename.endswith(".lang"):
        continue
    
    lang_path = os.path.join(input_folder, filename)
    json_filename = filename.rsplit(".", 1)[0] + ".json"
    json_path = os.path.join(output_folder, json_filename)

    data = {}
    with open(lang_path, "r", encoding="utf-8") as f:
        for line in f:
            line = line.strip()
            if not line or line.startswith("#"):
                continue
            if "=" in line:
                key, value = line.split("=", 1)
                data[key.strip()] = value.strip()

    with open(json_path, "w", encoding="utf-8") as f:
        json.dump(data, f, ensure_ascii=False, indent=4)
    
    print(f"Converted {lang_path} -> {json_path}")

print("All files converted.")
