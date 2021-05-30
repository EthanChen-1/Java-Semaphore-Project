import java.util.ArrayList;
import java.util.concurrent.*;
public class SchoolDay {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long start_time = System.currentTimeMillis();
		ArrayList<Student> student = new ArrayList<Student>();
		Semaphore wait_yard = new Semaphore(1, true);
		Semaphore lock2 = new Semaphore(0, true);
		Semaphore lock_classes = new Semaphore(0, true);
		Semaphore fill_classes = new Semaphore(0, true);
		Semaphore in_Class = new Semaphore (0,true);		
		int num_of_students = 10;
		Thread stu = new Thread();
		for(int i = 0; i < num_of_students; i++) {
			student.add(new Student(Integer.toString(i), wait_yard, start_time, in_Class));
			stu = new Thread(student.get(i), Integer.toString(i));
			stu.start();
		}
		Principal principal = new Principal("Malfurion", student,  lock2, start_time, in_Class, lock_classes);
		Thread prin = new Thread(principal);
		prin.start();
		Nurse nurse = new Nurse("Tyrande", student, lock2, lock_classes, start_time);
		Thread nur = new Thread(nurse);
		nur.start();
		Teacher teacher1 = new Teacher("Illidan", student, lock_classes, start_time, in_Class);
		Thread teach1 = new Thread(teacher1);
		Teacher teacher2 = new Teacher("Khadgar", student, lock_classes, start_time, in_Class);
		Thread teach2 = new Thread(teacher2);
		teach1.start();
		teach2.start();
	}
}
