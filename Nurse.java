import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Nurse implements Runnable{
	String name; 
	long start_time;
	ArrayList<Student> students;
	Semaphore lock2;
	Semaphore lock_classes;
	Semaphore give_shot = new Semaphore(1, true);
	Nurse(String name, ArrayList<Student> students, Semaphore lock2, Semaphore lock_classes, long start_time){
		this.name = "Nurse_" + name;
		this.students = students;
		this.lock2 = lock2;
		this.lock_classes = lock_classes;
		this.start_time = start_time;
		System.out.println(this.name + " has been created");
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println(this.name + " is starting");
		try {
			lock2.acquire(1);
			for(int i = 0; i < students.size(); i++) {
				if(students.get(i).get_tested == true) {
					System.out.println(this.name + " tests " + students.get(i).name + " for covid");
					give_shot.acquire(1);
						has_covid(students.get(i));
						System.out.println("Covid test for " + students.get(i).name +" returned " + students.get(i).has_covid);
						if(!students.get(i).has_covid) {
							System.out.println(this.name +  " released " + students.get(i).name + " from nurse's office");
							students.get(i).wait_nurse.release(1);
						} else {
							System.out.println(this.name + " sent " + students.get(i).name + " home");
							students.get(i).sent_home = true;
						}
					give_shot.release(1);
				} 
			}
			lock2.release(1);
			lock_classes.release(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(this.name + " went home");
	}
	
	public Boolean has_covid(Student student) {
		if(Math.random() <= .03) {
			student.has_covid = true;
		}
		return false;
	}
}
