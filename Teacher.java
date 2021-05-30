import java.util.ArrayList;
import java.util.concurrent.Semaphore;

class ela{
	static int sent_home;
	static int counter = 0;
	static Student[] ela_class = new Student[6];
}

class math{
	static int sent_home;
	static int counter = 0;
	static Student[] math_class = new Student[6];
}

public class Teacher implements Runnable{
	String name;
	long start_time;
	ArrayList<Student> roster;
	Semaphore lock_classes;
	Semaphore lock = new Semaphore(1, true);
	Semaphore in_Class;
	Teacher(String name, ArrayList<Student> students, Semaphore lock_classes, long start_time, Semaphore in_Class){
		this.name = "Teacher_" + name;
		this.roster = students;
		this.lock_classes = lock_classes;
		this.start_time = start_time;
		this.in_Class = in_Class;
		System.out.println(this.name + " has been created");
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println(this.name + " is starting");
		System.out.println(this.name + " start waiting for students");
		try {
			lock_classes.acquire(1);
			lock.acquire(1);
			for(int i = 0; i < roster.size(); i++) {
				if(roster.get(i).sent_home != true){
					put_in_classes(roster.get(i));
				} else {
					ela.sent_home++;
					math.sent_home++;
				}
			}
			for(int i = 0; i < ela.ela_class.length; i++) {
				ela.ela_class[i].wait_class.release(1);
			}
			for(int i = 0; i < roster.size() - math.math_class.length - math.sent_home; i++) {
				math.math_class[i].wait_class.release(1);
			}
			lock.release(1);
			System.out.println(this.name + " starts teaching");
			in_Class.acquire(1);
			System.out.println(this.name + " waits for next class");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(this.name + " went home");
	}
	
	public void put_in_classes(Student student) {
		if(ela.counter < ela.ela_class.length) {
			ela.ela_class[ela.counter] = student;
			student.in_ELA = true;
			ela.counter++;
		} else if (ela.counter == ela.ela_class.length && math.counter < (roster.size() - math.math_class.length)) {
			math.math_class[math.counter] = student;
			student.in_MATH = true;
			math.counter++;
		}
	}
}


