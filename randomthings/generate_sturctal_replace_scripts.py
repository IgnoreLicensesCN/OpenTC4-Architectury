mapping = {
    "prevPosX":"xo",
    "prevPosY":"yo",
    "prevPosZ":"zo",
    "posX":"x",
    "posY":"y",
    "posZ":"z",
    "motionX":"xd",
    "motionY":"yd",
    "motionZ":"zd",
    "particleAge":"age",
    "particleMaxAge":"lifetime",
    "particleGravity":"gravity",
    "particleScale":"quadSize",
    "particleAlpha":"alpha",
    "particleRed":"rCol",
    "particleGreen":"gCol",
    "particleBlue":"bCol",
    "rand":"random",
}
# print("var fieldName = FIELD.getReferenceName();")
# for key in mapping.keys():
#     print(f"if (Objects.equals(fieldName,\"{key}\"))"+"{return true;}")
# print("return false;")

print()
print("var fieldName = FIELD.getReferenceName();")
for entry in mapping.items():
    key = entry[0]
    value = entry[1]
    print(f"if (Objects.equals(fieldName,\"{key}\"))"+"{return \""+value+"\";}")
print("return fieldName;")
