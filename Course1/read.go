package main

import (
	"fmt"
	"os"
	"strings"
)

type name struct {
	fname string
	lname string
}

func main() {
	fmt.Println("Enter the filename:")
	var f string
	fmt.Scan(&f)
	var n []name
	data, err := os.ReadFile(f)
	if err != nil {
		fmt.Println("There is some error")
		return
	}
	lines := strings.Split(string(data), "\n")
	for _, l := range lines {
		l = strings.TrimSpace(l)
		p := strings.SplitN(l, " ", 2)
		a, b := p[0], p[1]
		if len(a) > 20 {
			a = a[:20]
		}
		if len(b) > 20 {
			b = b[:20]
		}
		n = append(n, name{a, b})
	}
	for _, i := range n {
		fmt.Printf("First Name: %s, Last Name: %s\n", i.fname, i.lname)
	}
}
