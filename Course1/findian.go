package main

import "fmt"

func main() {
	fmt.Println("Enter string:")
	var s string
	fmt.Scan(&s)
	a := false
	for i := 1; i <= len(s)-2; i++ {
		if s[i] == 'a' {
			a = true
			break
		}
	}
	if a && s[0] == 'i' && s[len(s)-1] == 'n' {
		fmt.Println("Found!")
	} else {
		fmt.Println("Not Found!")
	}
}
