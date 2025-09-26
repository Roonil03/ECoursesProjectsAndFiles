package main

import (
	"encoding/json"
	"fmt"
)

func main() {
	var name, addr string
	fmt.Println("Enter your name:")
	fmt.Scan(&name)
	fmt.Println("Enter your address:")
	fmt.Scan(&addr)
	m := make(map[string]string)
	m["name"] = name
	m["address"] = addr
	data, err := json.Marshal(m)
	if err != nil {
		fmt.Println("Error in creating json")
		return
	}
	fmt.Println(string(data))
}
