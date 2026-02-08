import java.io.*;
import java.util.*;

// ================= INTERFACE =================
interface Enroll {
    void enrollStudent(String studentName, int creditHours) throws Exception;
}

// ================= BASE CLASS =================
class Course {
    private String courseCode;
    private double feePerCredit;

    public Course(String courseCode, double feePerCredit) {
        this.courseCode = courseCode;
        this.feePerCredit = feePerCredit;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public double calculateFee(int creditHours) {
        return feePerCredit * creditHours;
    }
}

// ================= CHILD CLASS (INHERITANCE + POLYMORPHISM) =================
class LabCourse extends Course {
    public LabCourse(String courseCode) {
        super(courseCode, 1500);
    }

    @Override
    public double calculateFee(int creditHours) {
        return (1500 * creditHours) + 1000; // lab charge
    }
}

// ================= CUSTOM EXCEPTION =================
class InvalidCreditException extends Exception {
    public InvalidCreditException(String msg) {
        super(msg);
    }
}

// ================= FILE HANDLING =================
class EnrollmentFile {
    public static void save(String name, String course, int credits, double total) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("enrollments.txt", true))) {
            bw.write("Student: " + name +
                    ", Course: " + course +
                    ", Credits: " + credits +
                    ", Total Fee: Rs " + total);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("File error!");
        }
    }
}

// ================= MULTITHREADING =================
class EnrollmentThread extends Thread {
    public void run() {
        try {
            System.out.println("\nProcessing enrollment...");
            Thread.sleep(2000);
            System.out.println("Enrollment Successful ✅");
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted");
        }
    }
}

// ================= COURSE MANAGEMENT =================
class University implements Enroll {
    private Course course;

    public University(Course course) {
        this.course = course;
    }

    @Override
    public void enrollStudent(String studentName, int creditHours) throws Exception {
        if (creditHours <= 0) {
            throw new InvalidCreditException("Credit hours must be positive!");
        }

        double totalFee = course.calculateFee(creditHours);

        EnrollmentThread et = new EnrollmentThread();
        et.start();

        EnrollmentFile.save(studentName, course.getCourseCode(), creditHours, totalFee);

        System.out.println("\n===== ENROLLMENT DETAILS =====");
        System.out.println("Student Name: " + studentName);
        System.out.println("Course Code: " + course.getCourseCode());
        System.out.println("Credits: " + creditHours);
        System.out.println("Total Fee: Rs " + totalFee);
    }
}

// ================= MAIN =================
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.println("===== UNIVERSITY COURSE MANAGEMENT SYSTEM =====");
            System.out.print("Enter Student Name: ");
            String name = sc.nextLine();

            System.out.println("\nChoose Course Type:");
            System.out.println("1. Theory Course (Rs 1200 per credit)");
            System.out.println("2. Lab Course (Rs 1500 per credit + lab fee)");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            System.out.print("Enter Credit Hours: ");
            int credits = sc.nextInt();

            Course course;
            if (choice == 1) {
                course = new Course("CS101", 1200);
            } else if (choice == 2) {
                course = new LabCourse("CS101L");
            } else {
                System.out.println("Invalid course choice!");
                return;
            }

            University uni = new University(course);
            uni.enrollStudent(name, credits);

            System.out.println("\nEnrollment saved in enrollments.txt");

        } catch (InvalidCreditException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error!");
        } finally {
            sc.close();
        }
    }
}
