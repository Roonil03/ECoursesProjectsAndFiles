function fib(n) {
    // Step 1: Define the base case(s) here.
    // Hint: What is the value of Fibonacci(0) and Fibonacci(1)?
    if (n === 0) return 0;
    if (n === 1) return 1;
    return fib(n - 1) + fib(n - 2)
    // Step 2: Define the recursive case here.
    // Hint: Return the sum of Fibonacci(n-1) and Fibonacci(n-2).
}

console.log(fib(5));
console.log(fib(10));