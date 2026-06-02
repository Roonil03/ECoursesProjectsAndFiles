package main

import (
	"bufio"
	"encoding/json"
	"fmt"
	"os"
	"strconv"
	"strings"
)

type Task struct {
	ID          int    `json:"id"`
	Description string `json:"description"`
	Completed   bool   `json:"completed"`
}

const dataFile = "tasks.json"

var tasks []Task
var nextID int = 1

func main() {
	loadTasks()

	reader := bufio.NewReader(os.Stdin)

	for {
		fmt.Println("\n--- To-Do List Manager ---")
		fmt.Println("1. Add a task")
		fmt.Println("2. View all tasks")
		fmt.Println("3. Mark task as completed")
		fmt.Println("4. Remove a task")
		fmt.Println("5. Save and Exit")
		fmt.Print("Choose an option: ")

		input, _ := reader.ReadString('\n')
		input = strings.TrimSpace(input)

		switch input {
		case "1":
			addTask(reader)
		case "2":
			displayTasks()
		case "3":
			completeTask(reader)
		case "4":
			removeTask(reader)
		case "5":
			saveTasks()
			fmt.Println("Goodbye!")
			return
		default:
			fmt.Println("Invalid option, please try again.")
		}
	}
}

func loadTasks() {
	file, err := os.ReadFile(dataFile)
	if err != nil {
		if os.IsNotExist(err) {
			return
		}
		fmt.Println("Error reading file:", err)
		return
	}

	err = json.Unmarshal(file, &tasks)
	if err != nil {
		fmt.Println("Error parsing data:", err)
		return
	}

	for _, task := range tasks {
		if task.ID >= nextID {
			nextID = task.ID + 1
		}
	}
}

func saveTasks() {
	data, err := json.MarshalIndent(tasks, "", "  ")
	if err != nil {
		fmt.Println("Error encoding tasks:", err)
		return
	}

	err = os.WriteFile(dataFile, data, 0644)
	if err != nil {
		fmt.Println("Error saving to file:", err)
	} else {
		fmt.Println("Tasks saved successfully.")
	}
}

func addTask(reader *bufio.Reader) {
	fmt.Print("Enter task description: ")
	desc, _ := reader.ReadString('\n')
	desc = strings.TrimSpace(desc)

	if desc == "" {
		fmt.Println("Task description cannot be empty.")
		return
	}

	task := Task{
		ID:          nextID,
		Description: desc,
		Completed:   false,
	}
	tasks = append(tasks, task)
	nextID++
	fmt.Println("Task added successfully.")
}

func displayTasks() {
	if len(tasks) == 0 {
		fmt.Println("No tasks in the list.")
		return
	}

	fmt.Println("\n--- Task List ---")
	for _, task := range tasks {
		status := " "
		if task.Completed {
			status = "X"
		}
		fmt.Printf("[%s] %d: %s\n", status, task.ID, task.Description)
	}
}

func completeTask(reader *bufio.Reader) {
	displayTasks()
	if len(tasks) == 0 {
		return
	}

	fmt.Print("Enter the ID of the task to mark as completed: ")
	idStr, _ := reader.ReadString('\n')
	idStr = strings.TrimSpace(idStr)

	id, err := strconv.Atoi(idStr)
	if err != nil {
		fmt.Println("Invalid ID format.")
		return
	}

	for i, task := range tasks {
		if task.ID == id {
			tasks[i].Completed = true
			fmt.Println("Task marked as completed.")
			return
		}
	}

	fmt.Println("Task not found.")
}

func removeTask(reader *bufio.Reader) {
	displayTasks()
	if len(tasks) == 0 {
		return
	}

	fmt.Print("Enter the ID of the task to remove: ")
	idStr, _ := reader.ReadString('\n')
	idStr = strings.TrimSpace(idStr)

	id, err := strconv.Atoi(idStr)
	if err != nil {
		fmt.Println("Invalid ID format.")
		return
	}

	for i, task := range tasks {
		if task.ID == id {
			tasks = append(tasks[:i], tasks[i+1:]...)
			fmt.Println("Task removed successfully.")
			return
		}
	}

	fmt.Println("Task not found.")
}
