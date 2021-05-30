import java.util.ArrayList;

import java.util.concurrent.*;

class school_entry{
	static int counter = -1;
}

class class_entry{
	static int counter = -1;
}

public class Student implements Runnable, Comparable<Student>{
	String name;
	long start_time;
	Semaphore wait_entry;
	Boolean sent_home = false;
	Boolean get_tested = false;
	Boolean has_covid = false;
	Boolean in_ELA = false;
	Boolean in_MATH = false;
	Boolean in_Yard = false;
	Semaphore wait_covid_test = new Semaphore(0, true);
	Semaphore wait_nurse = new Semaphore(0, true);
	Semaphore wait_class = new Semaphore(0, true);
	Semaphore in_Class;
	int entry_position = 0;
	Student(String num, Semaphore wait_for_entry, long start_time, Semaphore in_Class){
		this.name = "Student_" + num;
		this.wait_entry = wait_for_entry;
		this.start_time = start_time;
		this.in_Class = in_Class;
		System.out.println(this.name + " has been created");
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println(this.name + " is starting");
		try {
			System.out.println(this.name + " waits in the yard");
			wait_entry.acquire(1); // One student enters the building
				school_entry.counter++; // Get position of entry
				this.entry_position = school_entry.counter; // Update students position
				System.out.println("Position:" + this.entry_position + ", " + this.name + " enters into building");	
			wait_entry.release(1); // Allow the next student to enter the building
			System.out.println(this.name + " waits to see if they are going to be tested for covid");
			wait_covid_test.acquire(1); // waiting to see whether they need to go to nurse 
			if(get_tested) {
				System.out.println(this.name + " goes to nurses office to get tested");
				System.out.println(this.name + " waits for nurse");
				wait_nurse.acquire(1);
				wait_class.acquire(1);
			} else {
				System.out.println(this.name + " waits for peers to be tested");
				wait_class.acquire(1);
			}
			System.out.println(this.name + " starts going to class");
			System.out.println(this.name + " starts sleeping");
			in_Class.acquire(1);
			System.out.println(this.name + " goes to next class");
			System.out.println(this.name + " goes home");
			
		} 
		catch(InterruptedException e) {
			System.out.println(e);
		}
		
	}

	public int compareTo(Student o) {
		 if(this.entry_position < o.entry_position) {
			 return -1;
		 } else if (this.entry_position > o.entry_position) {
			 return 1;
		 } else {
			 return 0;
		 }
	}
}
