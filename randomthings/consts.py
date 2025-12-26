from pathlib import Path

colorNames = [
    'white','orange','magenta','light_blue','yellow','lime','pink','gray','light_gray','cyan','purple','blue','brown','green','red','black'
]
colors = [15790320, 15435844, 12801229, 6719955, 14602026, 4312372, 14188952, 4408131, 10526880, 2651799, 8073150, 2437522, 5320730, 3887386, 11743532, 1973019]
colorsHex = [hex(i) for i in colors]
translationFilesPath = Path('../common/src/main/resources/assets/thaumcraft/lang')

if __name__ == '__main__':
    def show_color(name, color):
        r = (color >> 16) & 0xFF
        g = (color >> 8) & 0xFF
        b = color & 0xFF
        block = f"\033[48;2;{r};{g};{b}m  \033[0m"
        print(f"{block} {name:12} #{color:06X}  RGB({r},{g},{b})")


    for name, c in zip(colorNames, colors):
        show_color(name, c)
