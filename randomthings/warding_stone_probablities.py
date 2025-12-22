#part1
totalTicks = 0
totalTimes = 0
for i in range(100):
    for j in range(i+1):
        if j%5 == 0:
            totalTimes += 1
    totalTicks += i


print(totalTicks,totalTimes,totalTicks/totalTimes)
# 第二块：++count % 100 == 0

print(sum(range(0,100))/100)
print(49.5*2)
