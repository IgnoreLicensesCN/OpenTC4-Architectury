from itertools import permutations

dirs = ["Direction.DOWN", "Direction.UP", "Direction.NORTH", "Direction.SOUTH", "Direction.EAST", "Direction.WEST"]

all_perms = list(permutations(dirs))

print(str(all_perms).replace("[", "{").replace("]", "}").replace("'", "").replace("(", "{").replace(")","}"))