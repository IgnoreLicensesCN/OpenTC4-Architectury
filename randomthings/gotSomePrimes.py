
alreadyPrimes = [2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47]
for i in range(47,65536,2):
    isPrime = True
    for j in alreadyPrimes:
        if i%j==0:
            isPrime = False
            break
    if isPrime:
        alreadyPrimes.append(i)
alreadyPrimes = list(alreadyPrimes)
print(sorted(alreadyPrimes))