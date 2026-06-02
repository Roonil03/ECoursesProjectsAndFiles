package main

import (
	"fmt"
	"sort"
	"sync"
)

func worker(nums []int, wg *sync.WaitGroup, ch chan []int) {
	defer wg.Done()
	fmt.Println("Sorting subarray:", nums)
	sort.Ints(nums)
	ch <- nums
}

func mergeTwo(left, right []int) []int {
	result := make([]int, 0, len(left)+len(right))
	i, j := 0, 0
	for i < len(left) && j < len(right) {
		if left[i] < right[j] {
			result = append(result, left[i])
			i++
		} else {
			result = append(result, right[j])
			j++
		}
	}
	result = append(result, left[i:]...)
	result = append(result, right[j:]...)
	return result
}

func main() {
	fmt.Println("Enter integers separated by spaces (press Enter then Ctrl+D / Ctrl+Z to finish):")
	var input []int
	var val int
	for {
		_, err := fmt.Scan(&val)
		if err != nil {
			break
		}
		input = append(input, val)
	}
	n := len(input)
	if n == 0 {
		fmt.Println("Sorted result:", []int{})
		return
	}
	var wg sync.WaitGroup
	ch := make(chan []int, 4)
	baseSize := n / 4
	remainder := n % 4
	start := 0
	for i := 0; i < 4; i++ {
		end := start + baseSize
		if i < remainder {
			end++
		}
		if start == end {
			continue
		}
		wg.Add(1)
		go worker(input[start:end], &wg, ch)
		start = end
	}
	wg.Wait()
	close(ch)
	var sortedSubarrays [][]int
	for sub := range ch {
		sortedSubarrays = append(sortedSubarrays, sub)
	}
	for len(sortedSubarrays) > 1 {
		var nextRound [][]int
		for i := 0; i < len(sortedSubarrays); i += 2 {
			if i+1 < len(sortedSubarrays) {
				nextRound = append(nextRound, mergeTwo(sortedSubarrays[i], sortedSubarrays[i+1]))
			} else {
				nextRound = append(nextRound, sortedSubarrays[i])
			}
		}
		sortedSubarrays = nextRound
	}
	fmt.Println("Sorted result:", sortedSubarrays[0])
}
