package main

import "fmt"

func main() {
	fmt.Println("Enter floating point:")
	var x float32
	fmt.Scan(&x)
	fmt.Printf("Truncated number: %d\n", int(x))
}
