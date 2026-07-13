#maybe war to impl blocks is about to over(one last block) but i still want this.
from enum import Enum

import numpy as np



class Direction(Enum):
    NORTH = 0
    WEST = 1
    SOUTH = 2
    EAST = 3
    UP = 4
    DOWN = 5

DIRECTION_TO_BLOCK_POS = {
    Direction.NORTH : np.array([0,0,-1]).T,
    Direction.WEST : np.array([-1,0,0]).T,
    Direction.SOUTH : np.array([0,0,1]).T,
    Direction.EAST : np.array([1,0,0]).T,
    Direction.UP : np.array([0,1,0]).T,
    Direction.DOWN : np.array([0,-1,0]).T,
}

ROT_FROM_NORTH = {
    Direction.NORTH: np.array([
        [ 1, 0, 0],
        [ 0, 1, 0],
        [ 0, 0, 1],
    ]),

    Direction.SOUTH: np.array([
        [-1, 0, 0],
        [ 0, 1, 0],
        [ 0, 0,-1],
    ]),

    Direction.EAST: np.array([
        [ 0, 0, 1],
        [ 0, 1, 0],
        [-1, 0, 0],
    ]),

    Direction.WEST: np.array([
        [ 0, 0,-1],
        [ 0, 1, 0],
        [ 1, 0, 0],
    ]),

    Direction.UP: np.array([
        [ 1, 0, 0],
        [ 0, 0, 1],
        [ 0,-1, 0],
    ]),

    Direction.DOWN: np.array([
        [ 1, 0, 0],
        [ 0, 0,-1],
        [ 0, 1, 0],
    ]),
}
ROTATION_MAPPING:dict[tuple[Direction,Direction],np.ndarray] = {}
for direction in Direction:
    for direction2 in Direction:
        ROTATION_MAPPING[(direction,direction2)] = ROT_FROM_NORTH[direction].T @ ROT_FROM_NORTH[direction2]


#verifying
for directionToTransform in Direction:
        for directionToTransformTo in Direction:
            transformed = DIRECTION_TO_BLOCK_POS[directionToTransform].T @ ROTATION_MAPPING[(directionToTransform,directionToTransformTo)]
            target = DIRECTION_TO_BLOCK_POS[directionToTransformTo].T
            if (transformed[0] != target[0] or (transformed[1] != target[1]) or (transformed[2] != target[2])):
                print(directionToTransform,directionToTransformTo,target,transformed)


def getDirectionsForBox(toTransformOuter:np.ndarray, basePoint=None, fromDir:Direction=Direction.UP):
    result = {}
    if basePoint is None:
        basePoint = [8, 8, 8]
    for directionTo in Direction:
        toTransform = toTransformOuter.copy()
        toTransform -= np.array([basePoint,basePoint])

        transformed = toTransform @ ROTATION_MAPPING[(fromDir,directionTo)]

        transformed += np.array([basePoint,basePoint])

        transformedString = f"({min(transformed[0][0],transformed[1][0])},{min(transformed[0][1],transformed[1][1])},{min(transformed[0][2],transformed[1][2])},{max(transformed[0][0],transformed[1][0])},{max(transformed[0][1],transformed[1][1])},{max(transformed[0][2],transformed[1][2])})"
        result[directionTo] = transformedString
    return result

boxes = [getDirectionsForBox(toTransformOuter=np.array([[0,0,0],[16,8,16]])),getDirectionsForBox(toTransformOuter=np.array([[0,8,0],[8,16,8]]))]
for dir in Direction:
    boxesForDir = [boxes[0][dir],boxes[1][dir]]
    print(f"put(Direction.{dir.name},Shapes.or(Block.box{boxes[0][dir]},Block.box{boxes[1][dir]}));")