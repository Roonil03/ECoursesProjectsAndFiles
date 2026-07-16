package com.lesson.lab;

public class TextEditorUndo {
    private String[] stack;
    private int top;

    // Constructor to initialize the stack and top index
    public TextEditorUndo(int size) {
        stack = new String[size];
        top = -1;
    }

    // Method to add a new action to the stack
    public void push(String action) {
        if (top == stack.length - 1) {
            System.out.println("Undo stack is full. Cannot add more actions.");
        } else {
            stack[++top] = action;
            System.out.println("Action \"" + action + "\" added to undo stack.");
        }
    }

    // Method to remove and return the most recent action from the stack
    public String pop() {
        if (top == -1) {
            System.out.println("Undo stack is empty. No action to undo.");
            return null;
        } else {
            String action = stack[top--];
            System.out.println("Undone action: \"" + action + "\"");
            return action; // Return the undone action
        }
    }

    // Method to view the most recent action in the stack without removing it
    public String peek() {
        if (top == -1) {
            System.out.println("Undo stack is empty. No action to show.");
            return null;
        } else {
            return stack[top];
        }
    }

    // Method to display all actions in the stack
    public void display() {
        if (top == -1) {
            System.out.println("Undo stack is empty.");
        } else {
            System.out.print("Undo stack: ");
            for (int i = 0; i <= top; i++) {
                System.out.print("\"" + stack[i] + "\" ");
            }
            System.out.println();
        }
    }
}