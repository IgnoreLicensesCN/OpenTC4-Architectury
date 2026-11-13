#include <iostream>
#include <vector>
#include <random>
#include <cstdint>

using u64 = uint64_t;
using u128 = __uint128_t;

// Modular exponentiation: (base^exp) % mod
u64 power(u64 base, u64 exp, u64 mod) {
    u64 res = 1;
    base %= mod;
    while (exp > 0) {
        if (exp % 2 == 1) res = (u128)res * base % mod;
        base = (u128)base * base % mod;
        exp /= 2;
    }
    return res;
}

// Miller-Rabin primality test (Deterministic for 64-bit integers)
bool isPrime(u64 n) {
    if (n < 2) return false;
    if (n == 2 || n == 3) return true;
    if (n % 2 == 0) return false;

    u64 d = n - 1;
    int s = 0;
    while (d % 2 == 0) {
        d /= 2;
        s++;
    }

    // Deterministic bases for all numbers < 2^64
    static const u64 bases[] = {2, 325, 9375, 28178, 450775, 9780504, 1795265022};

    for (u64 a : bases) {
        if (a % n == 0) continue;
        u64 x = power(a, d, n);
        if (x == 1 || x == n - 1) continue;

        bool composite = true;
        for (int r = 1; r < s; r++) {
            x = (u128)x * x % n;
            if (x == n - 1) {
                composite = false;
                break;
            }
        }
        if (composite) return false;
    }
    return true;
}

int main() {
    // Setup 64-bit Mersenne Twister RNG
    std::random_device rd;
    std::mt19937_64 gen(rd());

    // Define range [2^31, 2^63]
    u64 min_val = 1ULL << 31;
    u64 max_val = 1ULL << 63;
    std::uniform_int_distribution<u64> dist(min_val, max_val);

    int count = 128; // Number of primes to find
    std::cout << "Finding " << count << " random primes in [2^31, 2^63]:\n";

    int found = 0;
    while (found < count) {
        u64 candidate = dist(gen);
        // Ensure candidate is odd to skip half the checks
        if (candidate % 2 == 0) candidate++;

        if (candidate <= max_val && isPrime(candidate)) {
            std::cout << candidate << "L,\n";
            found++;
        }
    }

    return 0;
}