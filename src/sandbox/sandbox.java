package sandbox;

import java.util.*;

import hillbillies.model.*;
import hillbillies.model.expression.*;
import hillbillies.model.statement.*;

public class sandbox {
	public static void main(String[] args){
		Scheduler s = new Scheduler();
		s.scheduleTask(new Task("a", 5, new Break(), null));
		s.scheduleTask(new Task("b", 50, new Break(), null));
		s.scheduleTask(new Task("c", -6, new Break(), null));
		s.scheduleTask(new Task("d", 50, new Break(), null));
		Iterator<Task> i = s.iterator();
		while(i.hasNext()){
			Task next = i.next();
			System.out.println(next.getPriority() + " : " + next.getName());
		}
	}

}
