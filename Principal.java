import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.*;

class test_ForCovid{
	static double random_num = Math.random();
}

public class Principal implements Runnable{
	String name;
	long start_time;
	ArrayList<Student> student_list;
	Semaphore lock1 = new Semaphore(1, true);
	Semaphore lock2;
	Semaphore in_Class;
	Semaphore lock_classes;
	Principal(String name, ArrayList<Student> student_list, Semaphore lock2, long start_time, Semaphore in_Class, Semaphore lock_classes){
		this.name = "Principal_" + name;
		this.student_list = student_list;
		this.lock2 = lock2;
		this.start_time = start_time;
		this.in_Class = in_Class;
		this.lock_classes = lock_classes;
		System.out.println(this.name + " has been created");
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println(this.name + " is starting");
		ordered_by_entry(this.student_list); 
		// Greeting students by ordered entered
		for(int i = 0; i < student_list.size(); i++) {
			System.out.println("Welcome! " + student_list.get(i).name); 
			// Determining who gets checked for covid
			if(test_ForCovid.random_num <= .333333333333) {
				System.out.println(this.name + " tells " + student_list.get(i).name +" to get tested for covid" );
				try {
					lock1.acquire(1);
					student_list.get(i).get_tested = true;
					student_list.get(i).wait_covid_test.release(1);
					lock1.release(1);
					test_ForCovid.random_num = Math.random();
					if(i == student_list.size() - 1 ) {
						lock2.release(1);
					}
					
//					System.out.println(this.name + " this class period is over continue to your next classes");
//					in_Class.release(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (test_ForCovid.random_num > .333333333333){
				student_list.get(i).wait_covid_test.release(1);
				test_ForCovid.random_num = Math.random();
				if(i == student_list.size() - 1 ) {
					lock2.release(1);
				}
			}
			System.out.println(this.name + " went home");
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Student> ordered_by_entry(ArrayList<Student> student_list){
		Collections.sort(student_list);
		return student_list;
	}

}
