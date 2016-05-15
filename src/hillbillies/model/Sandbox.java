package hillbillies.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import hillbillies.model.expression.*;
import hillbillies.model.statement.*;
import hillbillies.part3.programs.ITaskFactory;
import hillbillies.part3.programs.TaskParser;

public class Sandbox {
	public static void main(String[] args){
		
		String text = "name : \"test\" priority : 1000 activities : while true do if true then print v; fi done break; ";

		
		ITaskFactory<Expression<?>, Statement, Task> factory = new TaskFactory();
		
		int[] sublist = new int[] {1, 1, 1};
		
		List<int[]> list = new ArrayList<int[]>();
		list.add(sublist);
		
		TaskParser<Expression<?>, Statement, Task> parser = new TaskParser<Expression<?>, Statement, Task>(factory);
		
		Optional<List<Task>> T = parser.parseString(text, list);
		
		System.out.println(T.get().get(0).isWellFormed());
	


	}
}
	
