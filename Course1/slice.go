package main

import (
	"fmt"
	"sort"
	"strconv"
)

func main() {
	sl := make([]int, 0, 5)
	for true {
		fmt.Println("Enter number or 'X' to exit")
		var temp string
		fmt.Scan(&temp)
		if temp == "X" {
			fmt.Println("Exiting")
			break
		}
		n, _ := strconv.Atoi(temp)
		sl = append(sl, n)
		sort.Ints(sl)
		fmt.Println("Sorted Slice:", sl)
	}
}
