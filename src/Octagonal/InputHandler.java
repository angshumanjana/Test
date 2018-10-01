package Octagonal;
import java.util.*;
import java.io.*;

public class InputHandler{

	static String tableName = "";
	static String[] variables = null;
	static double[] constants = null;
	static double[][] array = null;

	public static int[][] main(File dependencyMain,File inputMain){
		int[][] depArray = null;
		try{
			FileOutputStream out = new FileOutputStream("input.txt");
	         ObjectOutputStream oout = new ObjectOutputStream(out);
	         


	         try{
	        	 BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(dependencyMain)));
			 	tableName = in.readLine();
			 	variables = (in.readLine()).split(",");
			 	String[] constantString = (in.readLine()).split(",");
			 	constants = new double[variables.length*2];
			 	array = new double[2*variables.length][variables.length];
			 	for(int i=0;i<variables.length*2;i=i+2){
			 		constants[i] = Integer.parseInt(constantString[i]);
			 		constants[i+1] = -1*Integer.parseInt(constantString[i+1]);
			 	}

				for(int i=0;i<variables.length;i++)
				{
					array[2*i][i] = 1;
					array[2*i+1][i] =-1;
				}
				//double[] constants = {1,-15,1,-10,1,-8,10000,-20000};

	        	oout.writeObject(variables);
	    	    oout.writeObject(array);
		        oout.writeObject(constants);
		        oout.flush();

	         	depArray = SemanticAnalysis.main(inputMain);
	         }
			 catch(Exception e){
			 	e.printStackTrace();
			 	System.out.println("Stack Trace 1111");
			 }

	         

	         //String[] variables = {"officer_id","officer_depid", "officer_position", "officer_phno"};
	         //String[] variables = {"project_id", "duration", "no_of_worker", "cost"};
	         //String[] variables = {"depid", "no_of_emp", "max_capacity"};
	         //String[]  variables = {"itemid", "category_id", "year", "price", "no_of_item"};
	         //String[] variables = {"eventid", "no_of_days", "year", "no_of_presenters", "no_of_perticipants"};
	        // String[] variables = {"empid", "age", "com", "da", "hra", "basic", "depid"};
			// double[][] array = { {1,0,0,0,0,0,0}, {-1,0,0,0,0,0,0}, {0,1,0,0,0,0,0}, {0,-1,0,0,0}, {0,0,1,0,0}, {0,0,-1,0,0}, {0,0,0,1,0}, {0,0,0,-1,0}, {0,0,0,0,1}, {0,0,0,0,-1} };
			 //double[][] array = new double[2*variables.length][variables.length];
			 //for(int i=0;i<variables.length;i++)
			 //{
			 //	array[2*i][i] = 1;
			 //	array[2*i+1][i] =-1;
			 //}
			 //double[] constants = {1,-15,1,-10,1,-8,10000,-20000};
			 //double[] constants = {1,-10,1,-6,1,-800,2000,-5000};
			 //double[] constants = {1,-15,1,-10,1,-8};	         
     	}
     	catch(Exception e){
				e.printStackTrace();
     	}
		return depArray;
     }

}