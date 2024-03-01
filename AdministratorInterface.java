import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Student {
    private String name;
    private int id;
    private List<Course> enrolledCourses;

    public Student(String name, int id) {
        this.name = name;
        this.id = id;
        this.enrolledCourses = new ArrayList<>();
    }

    // Getter and setter methods
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    // Method to enroll students in courses
    public void enrollCourse(Course course) {
        enrolledCourses.add(course);
    }

    // Method to assign grades to students
    public void assignGrade(Course course, int grade) {
        for (Course enrolledCourse : enrolledCourses) {
            if (enrolledCourse.equals(course)) {
                enrolledCourse.setGrade(this, grade);
                return;
            }
        }
        System.out.println("Student " + name + " is not enrolled in course " + course.getName());
    }
}

class Course {
    private String courseCode;
    private String name;
    private int maxCapacity;
    private static int totalEnrolledStudents = 0;

    private List<Student> enrolledStudents;
    List<Integer> grades;

    public Course(String courseCode, String name, int maxCapacity) {
        this.courseCode = courseCode;
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.enrolledStudents = new ArrayList<>();
        this.grades = new ArrayList<>();
    }

    // Getter methods
    public String getCourseCode() {
        return courseCode;
    }

    public String getName() {
        return name;
    }

    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }
    public int getMaxCapacity() {
        return maxCapacity;
    }

    // Static method to retrieve the total number of enrolled students
    public static int getTotalEnrolledStudents() {
        return totalEnrolledStudents;
    }

    // Method to enroll students in the course
    public void enrollStudent(Student student) {
        if (enrolledStudents.size() < maxCapacity) {
            enrolledStudents.add(student);
            totalEnrolledStudents++;
            grades.add(0); // Initialize grade for the newly enrolled student
        } else {
            System.out.println("Course " + name + " has reached its maximum capacity.");
        }
    }

    // Method to assign grade to a student
    public void setGrade(Student student, int grade) {
        int index = enrolledStudents.indexOf(student);
        if (index != -1) {
            grades.set(index, grade);
        } else {
            System.out.println("Student " + student.getName() + " is not enrolled in course " + name);
        }
    }
}

class CourseManagement {
    static List<Course> courses = new ArrayList<>();
    private static List<Integer> overallGrades = new ArrayList<>();

    // Method to add new course
    public static void addCourse(String courseCode, String name, int maxCapacity) {
        Course course = new Course(courseCode, name, maxCapacity);
        courses.add(course);
    }

    // Method to enroll student in a course
    public static void enrollStudent(Student student, Course course) {
        course.enrollStudent(student);
        student.enrollCourse(course);
    }

    // Method to assign grade to a student in a course
    public static void assignGrade(Student student, Course course, int grade) {
        course.setGrade(student, grade);
    }

    // Method to calculate overall course grade for a student
    public static double calculateOverallGrade(Student student) {
        double totalGrade = 0;
        int count = 0;
        for (Course course : student.getEnrolledCourses()) {
            int index = courses.indexOf(course);
            if (index != -1) {
                totalGrade += course.grades.get(index);
                count++;
            }
        }
        return count == 0 ? 0 : totalGrade / count;
    }
}

public class AdministratorInterface {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        displayMenu();
    }

    private static void displayMenu() {
        int choice;
        do {
            System.out.println("\n--- Course Enrollment and Grade Management System ---");
            System.out.println("1. Add a new course");
            System.out.println("2. Enroll a student");
            System.out.println("3. Assign grade to a student");
            System.out.println("4. Calculate overall course grade for a student");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            switch (choice) {
                case 1:
                    addNewCourse();
                    break;
                case 2:
                    enrollStudent();
                    break;
                case 3:
                    assignGrade();
                    break;
                case 4:
                    calculateOverallGrade();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice! Please enter a number between 1 and 5.");
            }
        } while (choice != 5);
        scanner.close();
    }

    private static void addNewCourse() {
        System.out.print("Enter course code: ");
        String code = scanner.nextLine();
        System.out.print("Enter course name: ");
        String name = scanner.nextLine();
        System.out.print("Enter maximum capacity: ");
        int capacity = scanner.nextInt();
        CourseManagement.addCourse(code, name, capacity);
        System.out.println("Course added successfully!");
    }

    private static void enrollStudent() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter student ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        Student student = new Student(name, id);

        System.out.print("Enter course code to enroll in: ");
        String code = scanner.nextLine();
        Course course = findCourseByCode(code);
        if (course != null) {
            CourseManagement.enrollStudent(student, course);
            System.out.println(name + " enrolled in course " + course.getName());
        } else {
            System.out.println("Course with code " + code + " not found!");
        }
    }

    private static void assignGrade() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter course code: ");
        String code = scanner.nextLine();
        Course course = findCourseByCode(code);
        if (course != null) {
            System.out.print("Enter grade for " + name + " in course " + course.getName() + ": ");
            int grade = scanner.nextInt();
            Student student = findStudentByName(name);
            if (student != null) {
                CourseManagement.assignGrade(student, course, grade);
                System.out.println("Grade assigned successfully!");
            } else {
                System.out.println("Student " + name + " not found!");
            }
        } else {
            System.out.println("Course with code " + code + " not found!");
        }
    }

    private static void calculateOverallGrade() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        Student student = findStudentByName(name);
        if (student != null) {
            double overallGrade = CourseManagement.calculateOverallGrade(student);
            System.out.println("Overall grade for " + name + ": " + overallGrade);
        } else {
            System.out.println("Student " + name + " not found!");
        }
    }

    private static Course findCourseByCode(String code) {
        for (Course course : CourseManagement.courses) {
            if (course.getCourseCode().equals(code)) {
                return course;
            }
        }
        return null;
    }

    private static Student findStudentByName(String name) {
        // Search for student by name
        // Assuming unique names for simplicity
        for (Course course : CourseManagement.courses) {
            for (Student student : course.getEnrolledStudents()) {
                if (student.getName().equals(name)) {
                    return student;
                }
            }
        }
        return null;
    }
}