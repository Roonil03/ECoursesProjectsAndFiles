package com.lesson.lab;

// StackLinkedList class to implement stack operations
public class StackLinkedList {
    // Node class to represent each element in the stack
    class Node {
        String data; // Data stored in the node (e.g., an action or text)
        Node next;   // Reference to the next node

        public Node(String data) {
            this.data = data;
            this.next = null; // Initialize the next node as null
        }
    }

    private Node top; // Top of the stack
    // Constructor to initialize the stack
    public StackLinkedList() {
        this.top = null;
    }

    // Push Operation - Add elements to the stack
    public void push(String action) {
        Node newNode = new Node(action);
        newNode.next = top; // Set the next of new node to current top
        top = newNode; // Update the top to new node
        System.out.println("Added: " + action);
    }

    // Pop Operation - Remove and return the top element of the stack
    public String pop() {
        if (top == null) {
            System.out.println("Stack is empty.");
            return null;
        }
        String poppedData = top.data;
        top = top.next;
        System.out.println("Removed: " + poppedData);
        return poppedData;
    }

    // Peek Operation - Display the top element without removing it
    public String peek() {
        if (top == null) {
            System.out.println("Stack is empty.");
            return null;
        }
        System.out.println("Top element: " + top.data);
        return top.data;
    }

    // Display All Elements - List all elements in the stack
    public void display() {
        if (top == null) {
            System.out.println("Stack is empty.");
            return;
        }
        System.out.println("Stack elements:");
        Node current = top;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }
}
