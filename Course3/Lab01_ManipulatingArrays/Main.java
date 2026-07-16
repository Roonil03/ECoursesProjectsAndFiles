package com.lesson.lab;

public class Main {
    public static void main(String[] args){
        // created array object of Student class
        Student[] students = new Student[5];
        // set the values through the constructor
        students[0] = new Student(1,"John", 19);
        // create other 4 object of student class and assign to the array Students
        students[1] = new Student(2,"Mary", 23);
        students[2] = new Student(3,"Krish", 18);
        students[3] = new Student(4,"Sara", 20);
        students[4] = new Student(5,"Harry", 25);

        // uncomment to call methods
        // remove 5th student from record
        int IndexToDelete = 4;
        System.out.println("Remove element at "+ IndexToDelete);
        removeElement(IndexToDelete, students);
        // Add new student
        System.out.println("\n Add new student rollNumber :6, Name : shally, Age: 3");
        Student newStudent = new Student(6,"shally", 30);
        AddElement(newStudent, students);
        //Update 3rd student information
        int IndexToUpdate = 2;
        System.out.println("\n Update an element at index "+IndexToUpdate);
        updateElement(2, students);
    }
    public static void removeElement(int IndexToDelete, Student[] originalArray){
        // write code to remove element
        Student[] newArray = new Student[originalArray.length -1];
        for(int i =0 ,j = 0;i< originalArray.length; i++){
            if(i!= IndexToDelete){
                newArray[j++] = originalArray[i];
            }
        }
        printArray("Remove",newArray);
    }
    public static void AddElement(Student newStudent,Student[] originalArray){
        // write code to add new element at last index
        Student[] newArray = new Student[originalArray.length +1];
        for(int i =0; i< newArray.length; i++){
            if (i == (newArray.length-1)){
                newArray[i] = newStudent;
            }else{
                newArray[i] = originalArray[i];
            }
        }
        printArray("Add",newArray);
    }
    public static void updateElement(int indexToUpdate, Student[] originalArray){
        // write code to update information of existing element based on index
        //updating name here
        originalArray[indexToUpdate].setName("Jenny");
        printArray("Update",originalArray);

    }
    public static void printArray(String message, Student[] students){
        //print here message using print statement
        System.out.println("\nOperation:"+message);
        //add for loop till array length and iterate over the array
        for(int i= 0; i< students.length; i++){
            System.out.println("Student Name : "+students[i].getName());
        }
        // fetch student details using getter method
    }
}
