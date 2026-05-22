# offsetVar = 'this.chunkSize'
# l = [f' - {offsetVar}','',f' + {offsetVar}']
#
# for xOff in range(3):
#     for yOff in range(3):
#         for zOff in range(3):
#             print(f'items = get(x{l[xOff]},y{l[yOff]},z{l[zOff]});\nif (items != null){{\nfor (var item:items){{\nif(action.apply(item)){{\nreturn true;\n}}\n}}\n}}')


for i in range(9):
    print(f'{(i//3) - 1},{(i%3)-1}')