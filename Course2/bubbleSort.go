package main

import (
	"fmt"
)

func Swap(s []int, i int) {
	s[i], s[i+1] = s[i+1], s[i]
}

func BubbleSort(s []int) {
	n := len(s)
	for i := range n {
		for j := 0; j < n-1-i; j++ {
			if s[j] > s[j+1] {
				Swap(s, j)
			}
		}
	}
}

func main() {
	var nums []int
	fmt.Println("Enter up to 10 integers (press Enter after each, or type a non-integer to stop):")
	for i := 0; i < 10; i++ {
		var val int
		_, err := fmt.Scan(&val)
		if err != nil {
			break
		}
		nums = append(nums, val)
	}
	BubbleSort(nums)
	fmt.Println("Sorted slice:", nums)
}
