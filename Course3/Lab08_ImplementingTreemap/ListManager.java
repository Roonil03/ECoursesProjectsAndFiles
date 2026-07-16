// TODO 1: Change the HashMap import into a tree map import
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ListManager {

    // TODO 2: Change studentsMap from HashMap to TreeMap
    TreeMap studentsMap = new TreeMap();

    public ListManager(Student [] students){

        for (Student student : students) {
            studentsMap.put(student.lastName, student);
        }
    }
    
    public void listStudents( ) {
        // Implemented previously in the lab "Using HashMap"
        if(studentsMap.isEmpty()){
            System.out.println("The system has no students in the waiting list");
        } else {
            System.out.println("Listing all Students\n");
            Set entries = studentsMap.entrySet();
            for (Object entry : entries) {
                Map.Entry mapEntry = (Map.Entry) entry;
                Student student = (Student)mapEntry.getValue();
                System.out.println(student);
            }
        }

    }

    public void findStudent(String lastName) {
        // Implemented previously in the lab "Using HashMap"
        if(studentsMap.containsKey(lastName)){
            System.out.println("Student Found\n");
            Student student = (Student)studentsMap.get(lastName);
            System.out.println(student);
        }else{
            System.out.println("\nStudent with name '" + lastName + "' Does not exist.");
        }

    }

    public void updateStudentStatus(String lastName, String newStatus){
        // Implemented previously in the lab "Using HashMap"
        Student student = (Student)studentsMap.get(lastName);
        if(student != null){
            String oldStatus = student.status;
            student.status = newStatus;
            studentsMap.put(lastName,student);
            System.out.println();
            System.out.println("Student ("+ lastName +") status was changed from '" + oldStatus + "'' to '" + newStatus + "'");
            System.out.println(student);
        }else {
            System.out.println();
            System.out.println("Student does not exist. Status was not updated.");
        }
    }

    public void removeStudent(String lastName) {
        // Implemented previously in the lab "Using HashMap"
        Student studentRemoved = (Student)studentsMap.remove(lastName);
        if(studentRemoved != null){
            System.out.println();
            System.out.println("Student removed.");
            System.out.println(studentRemoved);
            System.out.println();
        }else{
            System.out.println("Student was not removed.");
        }
    }

public void findFirstStudent(){
     System.out.println();
    // TODO 3: Get the first Entry in studentsMap
    Map.Entry firstEntry = studentsMap.firstEntry();

    if(firstEntry != null) {
        /* TODO 4: If the first entry exists, get the Student object
            from the entry */
        Student firstStudent = (Student) firstEntry.getValue();
        if (firstStudent != null) {
            // TODO 5: If the Student is not null, print the Student object
            System.out.println(firstStudent);
            // TODO 6: If the Student is not null, return from the method.
            return;
        }
    }
    /* TODO 7: If the first Entry doesn't exist or the Student is
        null, print an error message. */
    System.out.println("There are no students registered.");

}


    public void findLastStudent(){
        System.out.println();
        // TODO 9: Get the last Entry in studentsMap
        Map.Entry lastEntry = studentsMap.lastEntry();

        if(lastEntry != null) {
        /* TODO 10: If the last entry exists, get Student object
            from the entry */
            Student firstStudent = (Student) lastEntry.getValue();
            if (firstStudent != null) {
                // TODO 11: If the Student is not null, print the Student object
                System.out.println(firstStudent);
                // TODO 12: If the Student is not null, return from the method.
                return;
            }
        }
    /* TODO 13: If the last Entry doesn't exist or the Student is
        null, print an error message. */
        System.out.println("There are no students registered.");    }

    public void findNextStudent(String searchString) {

        System.out.println();
        /* TODO 15: Find the higher entry after the searchString */
        Map.Entry entry = studentsMap.higherEntry(searchString);

        if (entry != null) {
            /* TODO 16: If an entry is found, get the Student object
                from the entry */
            Student student = (Student) entry.getValue();
            if (student != null) {
                /* TODO 17: If a student is not null, print the student
                    object */
                System.out.println(student);
                /* TODO 18: If the Student is not null, return from the
                    method.*/
                return;
            }
        }
        /* TODO 19: If the higher entry is not found or the student is
            null, print "Student not found." */
        System.out.println("There are is no 'next' student.");
    }

    public void findPreviousStudent(String searchString) {

        System.out.println();
        /* TODO 22: Find the lower entry before the searchString */
        Map.Entry entry = studentsMap.lowerEntry(searchString);

        if (entry != null) {
            /* TODO 23: If an entry is found, get the Student object
                from the entry */
            Student student = (Student) entry.getValue();
            if (student != null) {
                /* TODO 24: If a student is not null, print the student
                    object */
                System.out.println(student);
                /* TODO 25: If the Student is not null, return from the
                    method.*/
                return;
            }
        }
        /* TODO 26: If the lower entry is not found or the student is
            null, print "Student not found." */
        System.out.println("There are is no 'next' student.");
    }


}