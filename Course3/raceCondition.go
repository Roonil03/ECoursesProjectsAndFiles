package main

import (
	"fmt"
	"time"
)

func main() {
	counter := 0
	go func() {
		for i := 0; i < 1000; i++ {
			counter++
		}
	}()
	go func() {
		for i := 0; i < 1000; i++ {
			counter++
		}
	}()
	time.Sleep(500 * time.Millisecond)
	fmt.Println("Final Counter:", counter)
}

/*
Explanation of the Race Condition:

A race condition occurs when two or more goroutines concurrently access shared
data (the `counter` variable in this case) and at least one of the accesses is
a write, without any synchronization to coordinate them.

Although the line `counter++` looks like a single operation, the CPU actually
breaks it down into three distinct operations at the machine level:
  1. Read: Fetch the current value of `counter` from memory into a CPU register.
  2. Modify: Increment the value inside the register by 1.
  3. Write: Store the updated value from the register back into memory.

Because the two goroutines run concurrently and are interleaved unpredictably
by the scheduler, their read-modify-write cycles can overlap. For example:
  - Goroutine 1 reads the value of `counter` (let's say it is currently 10).
  - Before Goroutine 1 can write the updated value back, the scheduler pauses
    it and switches to Goroutine 2.
  - Goroutine 2 reads the same value of `counter` (which is still 10).
  - Goroutine 2 increments it to 11 and writes 11 to memory.
  - The scheduler switches back to Goroutine 1. Goroutine 1 already has the
    value 10 in its register, increments it to 11, and writes 11 to memory.

Two increments occurred, but the value only increased by one. Goroutine 1
effectively overwrote and erased the update made by Goroutine 2. As a result,
the final value of `counter` will be unpredictable and almost always less
than the expected 2000.
*/
