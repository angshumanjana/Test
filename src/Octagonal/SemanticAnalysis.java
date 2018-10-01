package Octagonal;
import java.io.*;
import java.util.*;

class lineStructure implements Serializable
{
	int lineNumber;
	//double usedTrueArray[][];
	//double usedTrueConstant[];
	double usedArray[][];
	double usedConstant[];
	//double usedFalseArray[][];
	//double usedFalseConstant[];
	double definedArray[][];
	double definedConstant[];
	double definedArray1[][];
	double definedConstant1[];
	ArrayList<String> usedVariables;
	ArrayList<String> definedVaribles;
	public lineStructure()
	{}
}

class dependencyStructure implements Serializable
{
	int firstStmt;
	int secondStmt;
	int[][] defineUsed;
	int[][] def1;
	int[][] def2;
	int[][] defineUsed2;
	ArrayList<String> useVar2;
	ArrayList<String> defVar1;
}


public class SemanticAnalysis
{
	public static int[][] main(File inputMain)
	{
		int[][] depArray = null;
		long timeStamp1 = System.currentTimeMillis();
		Scanner S = new Scanner(System.in);
		BufferedReader br = null;
		try
		{
		br = new BufferedReader(new InputStreamReader(new FileInputStream(inputMain)));
		String str = null;
		int count=0;
		ArrayList<lineStructure> output = new ArrayList<lineStructure>();
		ArrayList<dependencyStructure> dependencyOutput = new ArrayList<dependencyStructure>();
		/*
		String[] variables = {"eventid","no_of_days","year","no_of_presenters","no_of_participants"};
		double[][] array = { {1,0,0,0,0}, {-1,0,0,0,0}, {0,1,0,0,0}, {0,-1,0,0,0}, {0,0,1,0,0}, {0,0,-1,0,0}, {0,0,0,1,0}, {0,0,0,-1,0}, {0,0,0,0,1}, {0,0,0,0,-1}};
		double[] constants = {0,-500,1,-10,2000,-2010,1,-30,1,-100};
		*/
		
		double[][] array;
		String[] variables;
		double[] constants;
		try
		{

		FileOutputStream out = new FileOutputStream("output.txt");
		ObjectOutputStream oout = new ObjectOutputStream(out);
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("input.txt"));

         // read and print an object and cast it as string
        variables = (String[]) ois.readObject();
		array = (double[][]) ois.readObject();
		constants = (double[]) ois.readObject();


		while((str = br.readLine())!=null)
		{
			count++;
			//System.out.println(count);
		if(str.contains("executeUpdate")||str.contains("insert into")||str.contains("openrs"))
		{	
			lineStructure temp = new lineStructure();
		//System.out.println(count);
      //  for(int i=0;i<)
		//System.out.println(count);
		String[] orLines;
		String original_str = str;
		if(str.contains("openrs"))
			orLines = str.split("[\"]",2);
		else
			orLines = (str.split("[(][\"]",2));
			//System.out.println(str+"******");
		if(orLines.length==2)
		{
		str = orLines[1];
		Object obj[]  = Method(str,array,variables,constants);
		/*if(str.contains("insertinto"))
		{
			if(obj[1]!=null)
				System.out.println("****************Insert Defined");
			else
				System.out.println("***************Insert not defined");
		}*/
		ArrayList<String> usedVariables = null;
		double definedArray[][] = null;
		double definedConstant[] = null;
		double usedArray[][] = null;
		double usedConstant[] = null;
		double trueArray[][] = null;
		double trueConstant[] = null;
		double falseArray[][] = null;
		double falseConstant[] = null;
		double modifiedTrueArray[][] = null;
		double modifiedTrueConstant[] = null;
		ArrayList<String> definedVaribles = null;
		if(obj==null)
		{
			System.out.println("***************************Not Computable*************");
			return null;
		}
		if(obj!=null)
		{
		if(obj[0]!=null)
			usedVariables = (ArrayList<String>)obj[0];
		if(obj[1]!=null)
			definedArray = (double[][])obj[1];
		if(obj[2]!=null)
			trueArray = (double[][])obj[2];
		if(obj[3]!=null)
			trueConstant = (double[])obj[3];
		if(obj[4]!=null)
			falseArray = (double[][])obj[4];
		if(obj[5]!=null)
			falseConstant = (double[])obj[5];
		if(obj[6]!=null)
			definedConstant = (double[])obj[6];
		if(obj[7]!=null)
			usedArray = (double[][])obj[7];
		if(obj[8]!=null)
			usedConstant = (double[])obj[8];
		if(obj[9]!=null)
			modifiedTrueArray = (double[][])obj[9];
		if(obj[10]!=null)
			modifiedTrueConstant = (double[])obj[10];
		if(obj[11]!=null)
			definedVaribles = (ArrayList<String>)obj[11];

		//////////////////////////////

		Object obj2[];
		if(falseArray!=null&&falseConstant!=null){
			obj2= removeRedundant(falseArray, falseConstant);
			falseArray = (double[][])obj2[0];
			falseConstant = (double[])obj2[1];
			
		}
		if(trueArray!=null && trueConstant!=null){
			obj2= removeRedundant(trueArray, trueConstant);
			trueArray = (double[][])obj2[0];
			trueConstant = (double[])obj2[1];
		}
			if(definedArray!=null&&!original_str.contains("insert"))
			{
				definedArray = trueArray;
				definedConstant = trueConstant;
			}
        if(trueConstant!=null)
        {
			double[] trueConstants2;
            double[] coeff = new double[trueArray[0].length];
    		double result=0;
    		int c2=0;
    		double[][] trueArray2;
    		/*System.out.println("dskjs " + trueArray.length);
    		for(int i=0; i<trueArray.length;i++)
    			for(int k=0; k<trueArray[0].length;k++)
    				System.out.println(trueArray[i][k]);*/
    		for(int i=0;i<trueArray.length;i++)
    		{
    			int check=-1;
    			for(int j=0;j<trueArray[0].length;j++)
    			{
    				coeff[j] = trueArray[i][j]*-1;
    				for(int k=0;k<trueArray.length;k++)
    				{
    					if(k!=i && Check(trueArray[k],trueArray[i]))
    					{
    						check = k;
    					}
    				}
    			}
    			//System.out.println(".............."+check);
    			if(check!=-1)
    			{
    				trueArray2 = new double[trueArray.length-2][trueArray[0].length];
    				trueConstants2 = new double[trueConstant.length-2];
    			}
    			else
    			{
    				trueArray2 = new double[trueArray.length][trueArray[0].length];
    				trueConstants2 = new double[trueConstant.length];
    			}
    			for(int j=0;j<trueArray[0].length;j++)
    			{
    				coeff[j] = trueArray[i][j]*-1;
    				for(int k=0;k<trueArray.length;k++)
    				{
    					//if(k!=i && !Check(trueArray[k],trueArray[i]))
    					if(k!=i && k!=check)
    					{
    						//System.out.println("/*///*/*/*//"+c2);
    						trueArray2[c2][j] = trueArray[k][j];
    						trueConstants2[c2] = trueConstant[k];
    						c2++;
    					}
    				}
    				c2=0;
    			}
    			result = SimplexMethod(trueArray2,trueConstants2,coeff)*-1;
    		//	System.out.println(result+"asdf"+trueConstant[i]);
    				if(result>trueConstant[i])
    					trueConstant[i] = result;
    		}
    	}
    	if(falseConstant!=null)
        {
			double[] falseConstants2;
            double[] coeff = new double[falseArray[0].length];
    		double result=0;
    		int c2=0;
    		double[][] falseArray2;
    		/*System.out.println("dskjs " + trueArray.length);
    		for(int i=0; i<trueArray.length;i++)
    			for(int k=0; k<trueArray[0].length;k++)
    				System.out.println(trueArray[i][k]);*/
    		for(int i=0;i<falseArray.length;i++)
    		{
    			int check=-1;
    			for(int j=0;j<falseArray[0].length;j++)
    			{
    				coeff[j] = falseArray[i][j]*-1;
    				for(int k=0;k<falseArray.length;k++)
    				{
    					if(k!=i && Check(falseArray[k],falseArray[i]))
    					{
    						check = k;
    					}
    				}
    			}
    			//System.out.println(".............."+check);
    			if(check!=-1)
    			{
    				falseArray2 = new double[falseArray.length-2][falseArray[0].length];
    				falseConstants2 = new double[falseConstant.length-2];
    			}
    			else
    			{
    				falseArray2 = new double[falseArray.length][falseArray[0].length];
    				falseConstants2 = new double[falseConstant.length];
    			}
    			for(int j=0;j<falseArray[0].length;j++)
    			{
    				coeff[j] = falseArray[i][j]*-1;
    				for(int k=0;k<falseArray.length;k++)
    				{
    					//if(k!=i && !Check(trueArray[k],trueArray[i]))
    					if(k!=i && k!=check)
    					{
    						//System.out.println("/*///*/*/*//"+c2);
    						falseArray2[c2][j] = falseArray[k][j];
    						falseConstants2[c2] = falseConstant[k];
    						c2++;
    					}
    				}
    				c2=0;
    			}
    			result = SimplexMethod(falseArray2,falseConstants2,coeff)*-1;
    		//	System.out.println(result+"asdf"+trueConstant[i]);
    				if(result>falseConstant[i])
    					falseConstant[i] = result;
    			}
    		}
    		////////////////////////////////////////////////////////
    		if(usedArray==null)
    		{
	           	Object obj4[] = usedArray(str,variables,array,constants);
	           	if(obj4!=null)
	           	{
		           	usedArray = (double[][])obj4[0];
		           	usedConstant = (double[])obj4[1];
	           	}
	        }

           		/*if((original_str.contains("executeUpdate") && !original_str.contains("delete")))
           		{
           			//System.out.println("yayyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
           			temp.usedArray1 = trueArray;
           			temp.usedConstant1 = trueConstant;
           			temp.definedArray1 = array;
           			temp.definedConstant1 = constants;
           		}
           		else if(original_str.contains("openrs"))
           		{
           			temp.usedArray1 = trueArray;
           			temp.usedConstant1 = trueConstant;
           		}*/


				temp.lineNumber = count;
				//System.out.println(count);
				//temp.usedTrueArray = trueArray;
				//temp.usedFalseArray = falseArray;
				//temp.usedTrueConstant = trueConstant;
				//temp.usedFalseConstant = falseConstant;
				temp.definedArray = definedArray;
				temp.definedConstant = definedConstant;
				temp.usedArray = usedArray;
				temp.usedConstant = usedConstant;
				temp.definedArray1 = null;
				temp.definedConstant1 = null;
				temp.definedVaribles = definedVaribles;
				temp.usedVariables = usedVariables;

				if(modifiedTrueArray!=null && original_str.contains("executeUpdate") && !original_str.contains("delete"))
				{
					temp.definedArray1 = modifiedTrueArray;
					temp.definedConstant1 = modifiedTrueConstant;
				}

				output.add(temp);
				//oout.writeObject(output);

			System.out.println("LINE NUMBER : " + count);
			System.out.println("ARRAY : ");
			for(int i=0;i<array.length;i++)
			{
				for(int j=0;j<array[0].length;j++)
					if(array[i][j]!=0)
						System.out.print(array[i][j]+"  ");
					else
						System.out.print("0.0" + "  ");
				System.out.println();
			}
			System.out.println("CONSTANTS : ");
			for(int i=0;i<constants.length;i++)
				System.out.println(constants[i]);
			if(usedArray!=null)
			{
				System.out.println("USED ARRAY : ");
				for(int i=0;i<usedArray.length;i++)
				{
					for(int j=0;j<usedArray[0].length;j++)
						if(usedArray[i][j]!=0)
							System.out.print(usedArray[i][j]+"   ");
						else
						System.out.print("0.0" + "  ");
					System.out.println();
				}
			}
			if(usedConstant!=null)
			{
				System.out.println("USED CONSTANT : ");
				for(int i=0;i<usedConstant.length;i++)
					System.out.println(usedConstant[i]);
			}

			/*if(trueArray!=null)
			{
			System.out.println("TRUE ARRAY");
			for(int i=0;i<trueArray.length;i++)
			{
				for(int j=0;j<trueArray[0].length;j++)
					System.out.print(trueArray[i][j]+"   ");
				System.out.println();
			}
			}
			if(trueConstant!=null)
			{
			System.out.println("TRUE CONSTANT");
			for(int i=0;i<trueConstant.length;i++)
				System.out.println(trueConstant[i]);
			}
			if(falseArray!=null)
			{
			System.out.println("FALSE ARRAY");
			for(int i=0;i<falseArray.length;i++)
			{
				for(int j=0;j<falseArray[0].length;j++)
					System.out.print(falseArray[i][j]+"   ");
				System.out.println();
			}
			}
			if(falseConstant!=null)
			{
			System.out.println("FALSE CONSTANT");
			for(int i=0;i<falseConstant.length;i++)
				System.out.println(falseConstant[i]);
			}*/
			if(definedArray!=null)
			{
			System.out.println("DEFINED ARRAY");
			for(int i=0;i<definedArray.length;i++)
			{
				for(int j=0;j<definedArray[0].length;j++)
					if(definedArray[i][j]!=0)
						System.out.print(definedArray[i][j]+"   ");
					else
						System.out.print("0.0" + "  ");
				System.out.println();
			}
			System.out.println("DEFINED CONSTANT");
			for(int i=0;i<definedConstant.length;i++)
				System.out.println(definedConstant[i]);
			}
			if(modifiedTrueArray!=null)
			{
				System.out.println("MODIFIED TRUE ARRAY");
				for(int i=0;i<modifiedTrueArray.length;i++)
				{
					for(int j=0;j<modifiedTrueArray[0].length;j++)
						if(modifiedTrueArray[i][j]!=0)
							System.out.print(modifiedTrueArray[i][j]+"   ");
						else
						System.out.print("0.0" + "  ");
					System.out.println();
				}
				System.out.println("MODIFIED TRUE CONSTANT");
				for(int i=0;i<modifiedTrueArray.length;i++)
					System.out.println(modifiedTrueConstant[i]);
			}

			
		}
		}
		}
		}
			
			//ObjectInputStream outin = new ObjectInputStream(new FileInputStream("output.txt"));
			//output = (ArrayList)outin.readObject();
			for(int i=0; i<output.size();i++)
			{
				/*double[][] ada = output.get(i).definedArray;
				double[][] aua = output.get(i).usedArray;
				double[][] ada1 = output.get(i).definedArray1;

				if(ada!=null)
				{
					System.out.println("Defined Array");
					for(int p=0;p<ada.length;p++)
					{
						for(int q=0;q<ada[0].length;q++)
						{
							System.out.print(ada[p][q] + "   ");
						}
						System.out.println();
					}
				}

				if(ada1!=null)
				{
					System.out.println("Modified Defined Array");
					for(int p=0;p<ada1.length;p++)
					{
						for(int q=0;q<ada1[0].length;q++)
						{
							System.out.print(ada1[p][q] + "   ");
						}
						System.out.println();
					}
				}

				if(aua!=null)
				{
					System.out.println("Used Array");
					for(int p=0;p<aua.length;p++)
					{
						for(int q=0;q<aua[0].length;q++)
						{
							System.out.print(aua[p][q] + "   ");
						}
						System.out.println();
					}
				}*/

				for(int j=i+1; j<output.size();j++)
				{
					int flag = 1;
					dependencyStructure struc = new dependencyStructure();
					struc.firstStmt = output.get(i).lineNumber;
					struc.secondStmt = output.get(j).lineNumber;
					System.out.println(struc.firstStmt+"  "+struc.secondStmt);
					double[][] used2 = output.get(j).usedArray;
					double[] usedCons2 = output.get(j).usedConstant;
					double[][] defArray1 = output.get(i).definedArray;
					double[] defCons1 = output.get(i).definedConstant;
					double[][] defArray2 = output.get(j).definedArray;
					double[] defCons2 = output.get(j).definedConstant;

					//double[][] def1 = Modify(defArray1,defCons1);
					//double[] defCons1 = output.get(i).definedConstant;
					//double[][] def2 = Modify(defArray2,defCons2);
					//double[] defCons2 = output.get(j).definedConstant;
					double[][] def11 = null;
					//double[] defCons11 = null;
					double[][] def21 = null;
					//double[] defCons21 = null;
					double[][] defArray11 = null;
					double[] defCons11 = null;
					double[][] defArray21 = null;
					double[] defCons21 = null;

					if(output.get(i).definedArray1!=null)
					{
						defArray11 = output.get(i).definedArray1;
						defCons11 = output.get(i).definedConstant1;
					}

					double[][] _defUse;
					double[] _defCons;
					if(defArray1!=null && used2!=null)
					{
						Object[] objdu = Intersection(defArray1,defCons1,used2,usedCons2);
						_defUse = (double[][])objdu[0];
						_defCons = (double[])objdu[1];
						struc.defineUsed = Modify(_defUse,_defCons);
						/*System.out.println("define + use");
						for(int p=0;p<struc.defineUsed.length;p++)
						{
							for(int q=0;q<struc.defineUsed[0].length;q++)
								System.out.print(struc.defineUsed[p][q] + "  ");
							System.out.println();
						}*/
					}
					if(defArray11!=null&&used2!=null)
					{
						Object[] objdu = Intersection(defArray11,defCons11,used2,usedCons2);
						_defUse = (double[][])objdu[0];
						_defCons = (double[])objdu[1];
						struc.defineUsed2 = Modify(_defUse,_defCons);
						/*System.out.println("define + use 2");
						for(int p=0;p<struc.defineUsed2.length;p++)
						{
							for(int q=0;q<struc.defineUsed2[0].length;q++)
								System.out.print(struc.defineUsed2[p][q] + "  ");
							System.out.println();
						}*/
					}
					
					/*if(output.get(j).definedArray1!=null)
					{
						defArray21 = output.get(j).definedArray1;
						defCons21 = output.get(j).definedConstant1;
						def21 = Modify(defArray21,defCons21);
					}*/

					/*int[][] defUse;
					if(def1!=null && used2!=null)
					{*/
						/*Object obj10[] = checkM(used2,usedCons2);
						used2 = (double[][]) obj10[0];
						usedCons2 = (double[]) obj10[1];*/
						/*defUse = new int[def1.length+used2.length][def1[0].length];
						for(int k=0; k<def1.length; k++)
						{
							for(int l=0; l<def1[0].length;l++)
								defUse[k][l]=(int)def1[k][l];
							//defUse[k][def1[0].length]=(int)defCons1[k];
							flag = k+1;
						}
						for(int k=0; k<used2.length; k++)
						{
							for(int l=0; l<used2[0].length;l++)
								defUse[k+flag][l]=(int)used2[k][l];
							//defUse[k+flag][used2[0].length]=(int)usedCons2[k];
						}
						struc.defineUsed = defUse;
					}*/
					/*else if(def1!=null)
					{
						defUse = new int[def1.length][def1[0].length + 1];
						for(int k=0; k<def1.length; k++)
						{
							for(int l=0; l<def1[0].length;l++)
								defUse[k][l]=(int)def1[k][l];
							defUse[k][def1[0].length]=(int)defCons1[k];
						}
						struc.defineUsed = defUse;
					}
					else if(used2!=null)
					{
						defUse = new int[used2.length][used2[0].length + 1];
						for(int k=0; k<used2.length; k++)
						{
							for(int l=0; l<used2[0].length;l++)
								defUse[k][l]=(int)used2[k][l];
							defUse[k][used2[0].length]=(int)usedCons2[k];
						}
						struc.defineUsed = defUse;
					}*/
					/*int[][] defUse2;
					if(def11!=null)
					{
						
						if(used2!=null)
						{*/
							/*Object[] obj10 = checkM(used2,usedCons2);
							used2 = (double[][]) obj10[0];
							usedCons2 = (double[]) obj10[1];*/
							/*defUse2 = new int[def11.length+used2.length][def11[0].length];
							for(int k=0; k<def11.length; k++)
							{
								for(int l=0; l<def11[0].length;l++)
									defUse2[k][l]=(int)def11[k][l];
								//defUse2[k][def11[0].length]=(int)defCons11[k];
								flag = k+1;
							}
							for(int k=0; k<used2.length; k++)
							{
								for(int l=0; l<used2[0].length;l++)
									defUse2[k+flag][l]=(int)used2[k][l];
								//defUse2[k+flag][used2[0].length]=(int)usedCons2[k];
							}
							struc.defineUsed2 = defUse2;

						}*/
						/*else
						{
							defUse2 = new int[def11.length][def11[0].length + 1];
							for(int k=0; k<def11.length; k++)
							{
								for(int l=0; l<def11[0].length;l++)
									defUse2[k][l]=(int)def11[k][l];
								defUse2[k][def11[0].length]=(int)defCons11[k];
							}
							struc.defineUsed2 = defUse2;

						}*/
					//}
					/*else
					{
						if(used2!=null)
						{
							defUse2 = new int[used2.length][used2[0].length+1];
							for(int k=0; k<used2.length; k++)
							{
								for(int l=0; l<used2[0].length;l++)
									defUse2[k][l]=(int)used2[k][l];
								defUse2[k][used2[0].length]=(int)usedCons2[k];
							}
							struc.defineUsed2 = defUse2;
						}
					}*/


					/*double[][] defdef;
					double[] defdefcons;

					if(def1!=null&&def2!=null)
					{
						defdef = new double[def1.length+def2.length][def1[0].length];
						defdefcons = new double[defCons1.length+defCons2.length];
						for(int k=0; k<def1.length; k++)
						{
							for(int l=0; l<def1[0].length;l++)
								defdef[k][l]=def1[k][l];
							defdefcons[k]=defCons1[k];
							flag = k+1;
						}
						for(int k=0; k<def2.length; k++)
						{
							for(int l=0; l<def2[0].length;l++)
								defdef[k+flag][l]=def2[k][l];
							defdefcons[k+flag]=defCons2[k];
						}
					}
					else if(def1!=null)
					{
						defdef = new double[def1.length][def1[0].length];
						defdefcons = new double[defCons1.length];
						for(int k=0; k<def1.length; k++)
						{
							for(int l=0; l<def1[0].length;l++)
								defdef[k][l]=def1[k][l];
							defdefcons[k]=defCons1[k];
						}
					}
					else if(def2!=null)
					{
						defdef = new double[def2.length][def2[0].length];
						defdefcons = new double[defCons2.length];
						for(int k=0; k<def2.length; k++)
						{
							for(int l=0; l<def2[0].length;l++)
								defdef[k][l]=def2[k][l];
							defdefcons[k]=defCons2[k];
						}
					}
					else
					{
						defdef = null;
						defdefcons = null;
					}
					int [][]_defdef;
					if(defdef!=null){
						Object red[] = removeRedundant(defdef,defdefcons);
						double[][] _defdef1 = (double[][])red[0];
						double[] _defdefcons1 = (double[])red[1];

						_defdef = new  int[_defdef1.length][_defdef1[0].length+1];
						for(int k=0; k<_defdef.length; k++)
						{
							for(int l=0; l<defdef[0].length;l++)
								_defdef[k][l]=(int)_defdef1[k][l];
							_defdef[k][defdef[0].length]=(int)_defdefcons1[k];
						}	
					}
					else
						_defdef = null;

					struc.defineDefined = _defdef;	*/

					//struct.def1
					//struct.def21
					int[][] _def1;
					int[][] _def2;
					if(defArray1!=null&&defArray2!=null){
						/*_def1 = new int[defArray1.length][defArray1[0].length+1];
						_def2 = new int[defArray2.length][defArray2[0].length+1];
						for(int k=0;k<_def1.length;k++)
						{
							for(int l=0;l<defArray1[0].length;l++)
							{
								_def1[k][l] = (int)defArray1[k][l];
							}
							_def1[k][defArray1[0].length] = (int)defCons1[k];
						}
						for(int k=0;k<_def2.length;k++)
						{
							for(int l=0;l<defArray2[0].length;l++)
							{
								_def2[k][l] = (int)defArray2[k][l];
							}
							_def2[k][defArray2[0].length] = (int)defCons2[k];
						}*/
						struc.def1 = Modify(defArray1,defCons1);
						struc.def2 = Modify(defArray2,defCons2);
					}
					/*if(output.get(i).definedVaribles!=null&&output.get(j).definedVaribles!=null){
						struc.defVar1 = output.get(i).definedVaribles;
						struc.useVar2 = output.get(j).usedVariables;
					}
					else
					{
						struc.defVar1 = null;
						struc.useVar2 = null;
					}*/
					struc.defVar1 = output.get(i).definedVaribles;
					struc.useVar2 = output.get(j).usedVariables;
					dependencyOutput.add(struc);
				}
			}
			oout.writeObject(count);
			oout.writeObject(dependencyOutput);
			oout.close();
			//outin.close();

			//System.out.println("break 1");
//			SemanticDependency sd = new SemanticDependency();
			depArray =  SemanticDependency.main();
			for(int i=0;i<depArray.length;i++)
			{
				for(int j=0;j<depArray[i].length;j++)
				{
					if(depArray[i][j]!=0)
						System.out.println( i + " --> " + j + "   " + depArray[i][j]);
				}
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Stack Trace 1");
		}

		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Stack Trace 2");
		}

		long timeStamp2 = System.currentTimeMillis();

		System.out.println("Time taken::::: " + (timeStamp2-timeStamp1));
		return depArray;

	}

	public static int[][] Modify(double[][] array, double[] constant)
	{	
		if(array!=null){
			int size = array[0].length;
			int[][] finalArray = new int[size*2][size*2];

			for(int i=0;i<array.length;i++)
			{
				ArrayList<Integer> idxPos = new ArrayList<Integer>();
				ArrayList<Integer> idxNeg = new ArrayList<Integer>();
				ArrayList<Integer> idx = new ArrayList<Integer>();

				for(int j=0; j<array[i].length;j++)
				{
					if(array[i][j]==1)
						idxPos.add(j);
					else if(array[i][j]==-1)
						idxNeg.add(j);
					idx.add(j);
				}
				if(idxPos.size()==1&&idxNeg.size()==0)
				{
					finalArray[idxPos.get(0)*2][idxPos.get(0)*2+1] = (int)constant[i]*-1;
				}
				else if(idxPos.size()==0&&idxNeg.size()==1)
				{
					finalArray[idxNeg.get(0)*2+1][idxNeg.get(0)*2] = (int)constant[i]*-1;
				}
				else if(idxPos.size()==2)
				{
					finalArray[idxPos.get(1)*2+1][idxPos.get(0)*2] = (int)constant[i]*-1;
				}
				else if(idxNeg.size()==2)
				{
					finalArray[idxNeg.get(0)*2][idxNeg.get(1)*2+1] = (int)constant[i]*-1;
				}
				else if(idxPos.size()==1 && idxNeg.size()==1)
				{
					finalArray[idxPos.get(0)*2][idxNeg.get(0)*2] = (int)constant[i]*-1;
					//finalArray[idxNeg.get(0)*2][idxPos.get(0)*2] = (int)constant[i]*-1;
				}
			}
			return finalArray;
		}
		else
			return null;

	}

	public static boolean Check(double[] array1,double[] array2)
	{
		for(int i=0;i<array1.length;i++)
			if(array2[i]!=array1[i]*-1)
				return false;;
		return true;
	}
	public  static Object[] usedArray(String str,String[] variables,double[][] array,double[] constants)
	{
		String exp = getExpression(str);
		if(exp!=null)
		{
			ArrayList<Double> temp = new ArrayList<Double>();
			ArrayList<Integer> indexes = new ArrayList<Integer>();
			String[] orLines = splitOR(exp);
			double[][][] array2 = new double[orLines.length][][];
			//double[][][] _result = new double[orLines.length][][];
			for(int i=0;i<orLines.length;i++)
			{
			 	array2[i] = getArray(orLines[i],variables,temp);
			}

			for(int k=0;k<array2.length;k++)
			{
				for(int i=0;i<array2[k].length;i++)
					for(int j=0;j<array2[k][i].length;j++)
						if(array2[k][i][j]!=0)
							indexes.add(j);
			}
			Set<Integer> hs = new HashSet<>();
			hs.addAll(indexes);
			indexes.clear();
			indexes.addAll(hs);
			double[][] usedArray = new double[indexes.size()*2][array[0].length];
			double[] usedConstant = new double[indexes.size()*2];
			int count=0;
			/*System.out.println("INEXES");
			for(int i=0;i<indexes.size();i++)
				System.out.println(indexes.get(i));*/
			for(int i=0;i<array.length;i++)
			{
				for(int j=0;j<indexes.size();j++)
				{
					if(array[i][indexes.get(j)]!=0)
					{
						usedArray[count] = array[i];
						usedConstant[count] = constants[i];
						count++;
					}
				}
			}
			return new Object[]{usedArray,usedConstant};
		}
		else
			return null;
	}
	public static Object[] removeRedundant(double[][] array,double[] constant)
	{
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for(int i=0;i<array.length-1;i++)
		{
			for(int j=i+1;j<array.length;j++)
			{
				for(int k=0;k<array[0].length;k++)
				{
					if(array[i][k]!=array[j][k])
						break;
					else
					{
						if(k==array[0].length-1)
						{
							if(constant[i]>constant[j])
								indexes.add(j);
							else
								indexes.add(i);
						}
					}
				}
			}
		}
		Set<Integer> hs = new HashSet<>();
		hs.addAll(indexes);
		indexes.clear();
		indexes.addAll(hs);
		/*System.out.println("..........."+indexes.size()+"..."+array.length);
		for(int i=0; i<indexes.size();i++)
			System.out.println(".."+indexes.get(i));
		for(int i=0;i<array.length;i++)
			for(int j=0; j<array[0].length;j++)
				System.out.print(array[i][j]);
		for(int i=0; i<indexes.size();i++)
			System.out.println(".."+indexes.get(i));*/
		double[][] trueArray = new double[array.length-indexes.size()][array[0].length];
		double[] trueConstants = new double[array.length-indexes.size()];
		int count=0;
		for(int i=0;i<array.length;i++)
		{
			if(!indexes.contains(i))
			{
				for(int j=0;j<array[0].length;j++)
					trueArray[count][j] = array[i][j];
				trueConstants[count] = constant[i];
				count++;
			}
		}
		return new Object[]{trueArray,trueConstants};
	}
	public static Object[] Method(String str,double[][] array,String[] variables,double[] constants)
	{
		//str=str.replaceAll("\\s+","");
		String exp = getExpression(str);
		ArrayList<String> usedVaribles = null;
		ArrayList<String> definedVaribles = new ArrayList<String>();
		double trueArray[][] = null;
		double trueConstant[]  = null;
		double falseArray[][] = null;
		double falseConstant[] = null;
		double definedArray[][] = null;
		double definedConstant[] = null;
		double modifiedTrueArray[][] = null;
		double modifiedTrueConstant[] = null;
		double usedArray[][] = null;
		double usedConstant[] = null;
		if(exp!=null||str.contains("insert"))
		{
			str = str.toLowerCase();
			if(str.contains("select"))  
			{
				String[] lines1 = str.split("[S|s][E|e][L|l][E|e][C|c][T|t]",2);
		        String[] lines2 = lines1[1].split("[F|f][R|r][O|o][M|m]",2);
		        String[] lines3 = lines2[1].split("[w][h][e][r][e]",2);

				Object obj[] = SELECT(exp,str,array,variables,constants); 
				if(obj==null)
					return null;
				trueArray = (double[][])obj[0];
				trueConstant = (double[])obj[1];
				ArrayList<String> demo = (ArrayList<String>)obj[2];
				falseArray = (double[][])obj[3];
				falseConstant = (double[])obj[4];
				usedVaribles = findVariables(lines2[0],variables);
				usedVaribles.addAll(demo);
				//System.out.println(lines2[0] + " 1111");
				if(lines2[0].replaceAll("\\s","").equals("*"))
					for(int k=0;k<variables.length;k++)
						usedVaribles.add(variables[k]);
				else
				{
					demo = findVariables(lines1[0],variables);
					usedVaribles.addAll(demo);
				}
				Set<String> hs = new HashSet<>();
				hs.addAll(usedVaribles);
				usedVaribles.clear();
				usedVaribles.addAll(hs);
				usedArray = trueArray;
				usedConstant = trueConstant;
				definedArray = null;
				definedConstant = null;
				definedVaribles = null;
				for(int k=0;k<usedVaribles.size();k++)
					System.out.print(usedVaribles.get(k) + "   ");
			}
			else if(str.contains("insert"))
			{
				String[] lines1 = str.split("[i][n][s][e][r][t][i][n][t][o]",2);
				String[] lines2 = lines1[1].split("[v][a][l][u][e][s]",2);
				String[] lines3 = lines2[1].split("[\"]");

				for(int i=0;i<variables.length;i++)
					definedVaribles.add(variables[i]);
				/*if(lines2[0].contains("(")&&lines2[0].contains(")"))
				{
					String[] lines3 = lines2[0].split("[(]",2);
					String[] lines4 = lines3[1].split("[)]",2);
					definedVaribles = findVariables(lines4[0], variables);
					usedVaribles = null;
					trueArray = null;
					trueConstant = null;
				}
				else
				{
					definedVaribles = new ArrayList<String>();
					for(int i=0;i<variables.length;i++)
						definedVaribles.add(variables[i]);
					usedVaribles = null;
					trueArray = null;
					trueConstant = null;
				}*/
				//System.out.println("Insert  "+lines3[0]);
				usedVaribles = null;
				trueArray = null;
				trueConstant = null;
				//
				definedArray = new double[array.length][array[0].length];
				definedConstant = new double[constants.length];

				//changing limits
				ArrayList<String> var = new ArrayList<String>();
				if(lines2[0].contains("(")&&lines2[0].contains(")"))
				{
					String[] lines31 = lines2[0].split("[(]",2);
					String[] lines41 = lines31[1].split("[)]",2);
					var = findVariables(lines41[0], variables);
				}
				else
				{
					for(int i=0;i<variables.length;i++)
						var.add(variables[i]);
				}
				if(lines3[0].contains("(")&&lines3[0].contains(")"))
				{
					String[] lines4 = lines3[0].split("[(]",2);
					String[] lines5 = lines4[1].split("[)]",2);
					ArrayList<String> con = findConstants(lines5[0]);
					for(int i=0;i<var.size();i++)
					{
						int index = findIndex(var.get(i),variables);
						double temp;
						try{
							temp = Double.parseDouble(con.get(i));
						}
						catch(Exception e){
							return null;
						}
						for(int j=0;j<array.length;j++)
						{
							if(array[j][index]==1)
							{
								definedArray[j]=array[j];
								definedConstant[j]=temp;
								if(constants[j]>temp)
									constants[j] = temp;
							}
							if(array[j][index]==-1)
							{
								definedArray[j]=array[j];
								definedConstant[j]=temp*-1;
								double demo2 = constants[j]*-1;
								if(demo2<temp)
									constants[j] = temp*-1;
							}
						}
					}
				}
				/*System.out.println("Insert Defined!!!!!!!");
				for(int i=0;i<definedArray.length;i++)
				{
					for(int j=0;j<definedArray[i].length;j++)
						System.out.print(definedArray[i][j] + "   ");
					System.out.print(definedConstant[i]);
					System.out.println();
				}*/
			}
			else if(str.contains("delete"))
			{
				Object obj[] = SELECT(exp,str,array,variables,constants);
				if(obj==null)
					return null;
				trueArray = (double[][])obj[0];
				trueConstant = (double[])obj[1];
				ArrayList<String> demo = (ArrayList<String>)obj[2];
				falseArray = (double[][])obj[3];
				falseConstant = (double[])obj[4];
				usedVaribles = new ArrayList<String>();
				usedVaribles.addAll(demo);
				Set<String> hs = new HashSet<>();
				hs.addAll(usedVaribles);
				usedVaribles.clear();
				usedVaribles.addAll(hs);
				definedArray = trueArray;
				definedConstant = trueConstant;
				usedArray = trueArray;
				usedConstant = trueConstant;

				for(int i=0;i<variables.length;i++)
					definedVaribles.add(variables[i]);

				double[] coeff = new double[array.length];
				double[][] falseArray1= new double[falseArray.length][array[0].length];
				double[] falseConstant1 = new double[falseConstant.length];

				for(int i=0;i<falseArray1.length;i++)
				{
					for(int j=0;j<array[0].length;j++)
						falseArray1[i][j] = falseArray[i][j];
					falseConstant1[i] = falseConstant[i];
				}
				/*for(int i=0;i<trueConstant.length;i++)
					System.out.print(trueConstant[i]+"  ");*/
				/*Object[] obj2 = removeRedundant(falseArray1, falseConstant1);
				falseArray1 = (double[][])obj2[0];
				falseConstant1 = (double[])obj2[1];*/
				double[][] falseArray2;
				double[] falseConstants2;
				int c2=0;
				double result = 0;
				////////
				for(int i=0;i<falseArray1.length;i++)
    			{
	    			int check=-1;
	    			for(int j=0;j<falseArray1[0].length;j++)
	    			{
	    				coeff[j] = falseArray1[i][j]*-1;
	    				for(int k=0;k<falseArray1.length;k++)
	    				{
	    					if(k!=i && Check(falseArray1[k],falseArray1[i]))
	    					{
	    						check = k;
	    					}
	    				}
	    			}
	    			//System.out.println(".............."+check);
	    			if(check!=-1)
	    			{
	    				falseArray2 = new double[falseArray1.length-2][falseArray1[0].length];
	    				falseConstants2 = new double[falseConstant1.length-2];
	    			}
	    			else
	    			{
	    				falseArray2 = new double[falseArray1.length][falseArray1[0].length];
	    				falseConstants2 = new double[falseConstant1.length];
	    			}
	    			for(int j=0;j<falseArray1[0].length;j++)
	    			{
	    				coeff[j] = falseArray1[i][j]*-1;
	    				for(int k=0;k<falseArray1.length;k++)
	    				{
	    					//if(k!=i && !Check(trueArray[k],trueArray[i]))
	    					if(k!=i && k!=check)
	    					{
	    						//System.out.println("/*///*/*/*//"+c2);
	    						falseArray2[c2][j] = falseArray1[k][j];
	    						falseConstants2[c2] = falseConstant1[k];
	    						c2++;
	    					}
	    				}
	    				c2=0;
	    			}
	    			result = SimplexMethod(falseArray2,falseConstants2,coeff)*-1;
	    				if(result>falseConstant[i])
	    					falseConstant1[i] = result;
    		}
				/*
				falseArray2 = new double[falseArray1.length-1][trueArray[0].length];
				falseConstants2 = new double[falseArray1.length-1];
				for(int i=0;i<falseArray1.length;i++)
	    		{
	    			for(int j=0;j<falseArray1[0].length;j++)
	    			{
	    				//System.out.print(falseArray1[i][j]);
	    				coeff[j] = falseArray1[i][j]*-1;
	    				for(int k=0;k<falseArray1.length;k++)
	    				{
	    					if(k!=i && !Check(falseArray1[k],falseArray1[i]))
	    					{
	    						falseArray2[c2][j] = falseArray1[k][j];
	    						falseConstants2[c2] = falseConstant1[k];
	    						c2++;
	    					}
	    				}
	    				if(c2!=falseArray2.length-1)
	    				{
	    					falseArray2[falseArray2.length-1] = falseArray2[0];
	    					falseConstants2[falseArray2.length-1] = falseConstants2[0];
	    				}
	    				c2=0;
	    			}
	    			double result = SimplexMethod(falseArray2,falseConstants2,coeff)*-1;
	    			//System.out.print("    "+result+"as"+falseConstant1[i]+"\n");
	    			if(result>falseConstant1[i])
	    					falseConstant1[i] = result;
	    		}*/
	    		/*for(int i=0;i<falseArray1.length;i++)
	    		{
	    			for(int j=0;j<falseArray1[0].length;j++)
	    				System.out.print(falseArray1[i][j]+"    ");
	    			System.out.print("    "+falseConstant1[i]);
	    			System.out.println();
	    		}*/
				int c3=0,count=0;
				for(int i=0;i<falseArray1.length;i++)
				{
					for(int j=0;j<array.length;j++)
					{
						count=0;
						for(int k=0;k<array[0].length;k++)
						{
							if(array[j][k]==falseArray1[i][k])
								count++;
						}
						if(count==array[0].length)
						{
							constants[j] = falseConstant1[i];
						}
					}
				}
			}
			else if(str.contains("update"))
			{
				//System.out.println(str+"......");
				String[] lines1 = str.split("[s][e][t]",2);
				String[] lines2 = lines1[1].split("[w][h][e][r][e]",2);
				String[] lines3 = lines2[1].split("[\"]");

				Object obj2[] = Update(lines2[0],variables);
				usedVaribles = (ArrayList<String>)obj2[0];
				definedVaribles = (ArrayList<String>)obj2[1];
				Object obj[] = SELECT(exp,str,array,variables,constants);
				if(obj==null)
					return null;
				trueArray = (double[][])obj[0];
				trueConstant = (double[])obj[1];
				ArrayList<String> demo = (ArrayList<String>)obj[2];
				falseArray = (double[][])obj[3];
				falseConstant = (double[])obj[4];
				usedVaribles.addAll(demo);
				Set<String> hs = new HashSet<>();
				hs.addAll(usedVaribles);
				usedVaribles.clear();
				usedVaribles.addAll(hs);
				definedArray = trueArray;
				definedConstant = trueConstant;
				usedArray = trueArray;
				usedConstant = trueConstant;
				int c3=0,count=0;
				for(int i=0;i<trueArray.length;i++)
				{
					for(int j=0;j<array.length;j++)
						if(array[j]==trueArray[i])
							constants[j] = trueConstant[i];
				}

				
				//eq[0] eq[1]
				
				//System.out.println(ind);
				//System.out.println("**"+ lines2[0]);
								/*for(int p=0;p<_array.length;p++)
				{
					for(int q=0; q<_array[0].length;q++)				
						System.out.println(_array[p][q]);
					System.out.println();
				}*/

				//System.out.println(_array[0]);


					double[][] trueArray1 = new double[array.length][array[0].length];
					double[] trueConstant1 = new double[constants.length];

					modifiedTrueArray = new double[trueArray.length][trueArray[0].length];
					modifiedTrueConstant = new double[trueConstant.length];

					//System.out.println(min_val+"**"+max_val);////////////???????????????????????????
					//*************************************************
					for(int i=0; i<trueArray.length;i++)
						for(int j=0; j<trueArray[0].length;j++)
							modifiedTrueArray[i][j] = trueArray[i][j];
					for(int i=0; i<trueConstant.length; i++)
						modifiedTrueConstant[i] = trueConstant[i];

					///for loop
					String[] lines33 = lines2[0].split(","); 
					for(int t=0; t<lines33.length; t++)
					{
						String[] eq = lines33[t].split("=",2);
						//System.out.println(lines33[t]);
						Object[] _obj = getArray2(lines33[t],variables);
						if(_obj==null)
							return null;
						double[][] _array = (double[][])_obj[0];
						double _constant = (double)_obj[1];
						//System.out.println("*******"+_constant);
						/*for(int x=0;x<_array.length;x++)
							for(int y=0;y<_array[0].length;y++)
								System.out.print(_array[x][y]);*/
						

						int ind = findIndex(eq[0],variables);
						//System.out.println(eq[0]);
						for(int i=0;i<trueArray1.length;i++)
						{
							for(int j=0;j<trueArray1[0].length;j++)
								trueArray1[i][j] = trueArray[i][j];
							trueConstant1[i] = trueConstant[i];
						}
						/*for(int x=0;x<trueArray1.length;x++)
						{
							for(int y=0;y<trueArray1[0].length;y++)
								System.out.print(trueArray1[x][y] + "  ");
							System.out.print(trueConstant1[x]);
							System.out.println();
						}*/
						/*double max_val=-99999,min_val=99999;
						for(int i=0;i<trueArray.length;i++)
						{
							//System.out.println(i);
							if(Arrays.equals(trueArray[i],_array[0]))
							{
								if(modifiedTrueConstant[i]+_constant>=0)
								{
									modifiedTrueConstant[i] += _constant;
									min_val = modifiedTrueConstant[i];
								}
								//System.out.println("||||||  "+modifiedTrueConstant[i]);
							}
						}*/
						for(int p =0;p<_array[0].length;p++)
							if(_array[0][p]!=0)
								_array[0][p]=_array[0][p]*-1;
						/*for(int x=0;x<_array.length;x++)
							for(int y=0;y<_array[0].length;y++)
								System.out.print(_array[x][y]);*/
						/*for(int i=0;i<trueArray.length;i++)
						{
							if(Arrays.equals(trueArray[i],_array[0]))
							{
								modifiedTrueConstant[i] -= _constant;
								max_val = modifiedTrueConstant[i];
								//System.out.println("||||||  "+modifiedTrueConstant[i]);
							}
						}
						System.out.println(min_val+"**"+max_val);*/
						
						double max_val = ( SimplexMethod(trueArray1,trueConstant1,_array[0]) )*-1;
						for(int p =0;p<_array[0].length;p++)
							_array[0][p]=_array[0][p]*-1;
					
						for(int i=0;i<trueArray1.length;i++)
						{
							for(int j=0;j<trueArray1[0].length;j++)
								trueArray1[i][j] = trueArray[i][j];
							trueConstant1[i] = trueConstant[i];
						}
						double min_val = ( SimplexMethod(trueArray1,trueConstant1,_array[0]) )*-1;

						//System.out.println(min_val+"**"+max_val);////////////???????????????????????????
						

						for(int i=0; i<trueArray1.length;i++)
								if(modifiedTrueArray[i][ind]==1&&min_val!=999999&&min_val!=-999999)
									modifiedTrueConstant[i]=max_val + _constant;
								else if(modifiedTrueArray[i][ind]==-1&&max_val!=-999999&&max_val!=-999999)
									modifiedTrueConstant[i]=min_val - _constant;

						//false array && modified array


						for(int i=0; i<array.length;i++)
								if(array[i][ind]==1&&max_val+_constant<constants[i]&&min_val!=999999)
									constants[i]=max_val + _constant;
								else if(array[i][ind]==-1&&min_val-_constant<constants[i]&&max_val!=-999999)
									constants[i]=min_val - _constant;
					}

				//for loop end

/*				// updating maximum & minimun in array
				lines2[0] = lines2[0].replaceAll("\\s","");
				//System.out.println("////"+lines2[0]);
				Object[] _obj = getArray2(lines2[0],variables);
				double[][] _array = (double[][])_obj[0];
				double _constant = (double)_obj[1];

				//if(_array.length==1)
				//{
					String[] lines21 = lines2[0].split("=",2);
					int ind = findIndex(lines21[0],variables);
					if(ind != -1)
					{
						//simplex method finding min
						double val_min = SimplexMethod(trueArray,trueConstant,_array[0])+_constant;
						//simplex metod findind max
						for(int i=0; i<_array[0].length;i++)
							_array[0][i]*=-1;
						double val_max = SimplexMethod(trueArray,trueConstant,_array[0])-_constant; //negative of maximum
						for(int i=0; i<array.length;i++)
						{
							int cnt = 0;
							for(int j=0; j<array[0].length;j++)
							{
								if(j!=ind)
									if(array[i][j]==0)
										cnt++;
							}
							if(cnt==array.length-1)
								if(array[i][ind]==1 && val_min > constants[i])
									constants[i] = val_min;
								else if(array[i][ind]==-1 && val_max > constants[i])
									constants[i] = val_max;
						}
					}*/
				/*}
				else
				{
					System.out.println("Can't compute!!!");
				}*/
				
				/*if(usedNum.length==1)
				{
					String[] lines10 = lines2[0].split("=",2);
					String[] terms = lines10[1].split("[+|-]");
					for(int i=0;i<terms.length;i++)
					{
						int cnt = 1;
						if(terms[i].contains("*"))
						{
							String[] terms1 = terms[i].split("[*]");
							for(int j=0;j<terms1.length;j++)
									if(isNumber(terms1[j]))
										cnt = cnt * int(terms1[j]);
									else if(terms1[j].contains("/"))
									{
										String[] terms12[j] = terms1[j].split("/",2);
										if(terms12[0].isNumber)
											cnt = cnt * int(terms12[0]);
										if(terms12[1].isNumber)
											cnt = cnt/int(terms12[1]);
									}
						}
						else if(terms[i].contains("/"))
						{
							String[] terms1 = terms[i].split("[/]");
							for(int j=0; j<term1.length; j++)
							{
								if(isNumber(terms1[j]))
									cnt = cnt / int(terms1[j]);
							}
						}
						else
						{
							;
						}
						ArrayList<String> var = findvariables(terms[i],variables);
						if(var.size()==1)
						{
							int ind = findIndex(var.get(0),variables);
							
						}
						else

						{
							System.out.println("Non-Linear Equation");
						}
					}
					double val = SimplexMethod(trueArray, trueConstants , coeff);
				}*/

			}
			/*if(str.contains("insert"))
			{
			System.out.println("Insert Defined!!!!!!!");
				for(int i=0;i<definedArray.length;i++)
				{
					for(int j=0;j<definedArray[i].length;j++)
						System.out.print(definedArray[i][j] + "   ");
					System.out.print(definedConstant[i]);
					System.out.println();
				}
			}*/
			
			return new Object[]{usedVaribles,definedArray,trueArray,trueConstant,falseArray,falseConstant,definedConstant,usedArray,usedConstant,modifiedTrueArray,modifiedTrueConstant,definedVaribles};
		}
		else if(exp==null)
		{
			trueArray = array;
			trueConstant = constants;
			falseArray = null;
			falseConstant = null;
			ArrayList<String> usedVaribles22 = new ArrayList<String>();
			for(int i=0;i<variables.length;i++)
				usedVaribles22.add(variables[i]);
			usedVaribles = usedVaribles22;
			usedArray = trueArray;
			usedConstant = trueConstant;
			definedArray = null;
			definedConstant = null;
			return new Object[]{usedVaribles,definedArray,trueArray,trueConstant,falseArray,falseConstant,definedConstant,usedArray,usedConstant,modifiedTrueArray,modifiedTrueConstant,definedVaribles};

		}
		return null;
	}
	public static Object[] Update(String str,String[] variables)
	{
		ArrayList<String> definedVaribles = new ArrayList<String>();
		ArrayList<String> usedVariables = new ArrayList<String>();
		//ArrayList<Integer> definedNum = new ArrayList<Integer>();
		//ArrayList<Integer> usedNum = new ArrayList<Integer>();
		String[] lines = str.split("[,]");
		for(int i=0;i<lines.length;i++)
		{
			String[] lines2 = lines[i].split("[=]",2);
			for(int j=0;j<variables.length;j++)
				if(lines2[0].contains(variables[j]))
				{
					//System.out.println(variables[j]);
					definedVaribles.add(variables[j]);
					//definedNum.add(j);
				}
			for(int j=0;j<variables.length;j++)
				if(lines2[1].contains(variables[j]))
				{
					usedVariables.add(variables[j]);
					//usedNum.add(j);
				}
		}
		Set<String> hs = new HashSet<>();
		hs.addAll(usedVariables);
		usedVariables.clear();
		usedVariables.addAll(hs);
		hs.clear();
		hs.addAll(definedVaribles);
		definedVaribles.clear();
		definedVaribles.addAll(hs);
		hs.clear();
		//System.out.println(definedVaribles.toString());
		/*hs.addAll(usedNum);
		usedNum.clear();
		usedNum.addAll(hs);
		hs.clear();
		hs.addAll(definedNum);
		definedNum.clear();
		definedNum.addAll(hs);*/
		// return new Object[]{usedVariables,definedVariables};
		return new Object[]{usedVariables,definedVaribles};
	}
	public static ArrayList<String> findVariables(String str,String[] variables)
	{
		ArrayList<String> output = new ArrayList<String>();
		String[] lines = str.split("[,|*|/]");
		for(int i=0;i<lines.length;i++)
		{
			for(int j=0;j<variables.length;j++)
			{
				if(lines[i].equals(variables[j]))
				{
					output.add(lines[i]);
					j = variables.length;
				}
			}
		}
		return output;
	}
	public static ArrayList<String> findConstants(String str)
	{
		ArrayList<String> output = new ArrayList<String>();
		String[] lines = str.split("[,]");
		for(int i=0;i<lines.length;i++)
		{
			output.add(lines[i]+"");
		}
		return output;
	}
	public static Object[] SELECT(String exp,String str,double[][] array,String[] variables,double[] constants)
	{
		ArrayList<String> usedVar = new ArrayList<String>();
		double[] constants4 = new double[constants.length];
		double[] constants5 = new double[constants.length];
		double falseArray[][];
		double falseConstant[];
		double[][] array4 = new double[array.length][array[0].length];
		double[][] array5 = new double[array.length][array[0].length];

		String[] orLines = splitOR(exp);
		double[][][] resFalseArray = new double[orLines.length][][];
		double[][] resFalseCons = new double[orLines.length][];
		double[][][] resTrueArray = new double[orLines.length][][];
		double[][] resTrueCons = new double[orLines.length][];
		
		for(int pq=0;pq<orLines.length;pq++){
			String _exp = orLines[pq];
			ArrayList<Double> constant = new ArrayList<Double>();
			double[][] array2 = getArray(_exp,variables,constant);
			if(array2==null||array2.length==0)
			{
				System.out.println(_exp);
				return null;
			}
			
			//System.out.println(array2.length);
			falseArray = new double[array2.length+array.length][array2[0].length];
			for(int i=0;i<array.length;i++)
				for(int j=0;j<array[0].length;j++)
					falseArray[i][j] = array[i][j];
			for(int i=0;i<array2.length;i++)
				for(int j=0;j<array2[0].length;j++)
					falseArray[array.length+i][j] = array2[i][j]*-1;
			falseConstant = new double[constants.length+constant.size()];
			for(int i=0;i<constants.length;i++)
				falseConstant[i] = constants[i];
			for(int i=0;i<constant.size();i++)
				falseConstant[constants.length+i] = constant.get(i)*-1 + 1;
			//
			//////
			double[][] array3 = new double[array.length+array2.length][array[0].length];
			double[] constants3 = new double[constants.length+array2.length];
			for(int j=0;j<array.length;j++)
				for(int k=0;k<array[0].length;k++)
					array3[j][k] = array[j][k];
			for(int j=0;j<constants.length;j++)
				constants3[j] = constants[j];

			for(int i=0;i<array2.length;i++)
			{
				for(int p=0;p<constants.length;p++)
				{
					constants4[p] = constants[p];
					constants5[p] = constants[p];
				}
				for(int p=0;p<array.length;p++)
				{
					for(int j=0;j<array[0].length;j++)
					{
						array4[p][j] = array[p][j];
						array5[p][j] = array[p][j];
					}
				}
				double[] temp = new double[array2[0].length];
				for(int j=0;j<array2[0].length;j++)
					temp[j] = array2[i][j];
				double maxValue = SimplexMethod(array4, constants4, temp);
				for(int j=0;j<temp.length;j++)
					temp[j]*=-1;
				for(int j=0;j<temp.length;j++)
					if(temp[j]!=0)
						usedVar.add(variables[j]);
				Set<String> hs = new HashSet<>();
				hs.addAll(usedVar);
				usedVar.clear();
				usedVar.addAll(hs);
				/*for(int j=0;j<usedVar.size();j++)
					System.out.print(i+ "  "+usedVar.get(j)+"   ");*/
				double minValue = SimplexMethod(array5, constants5, temp);
				minValue*=-1;
				for(int j=0;j<temp.length;j++)
					temp[j]*=-1;
				for(int j=array.length+i;j<array.length+i+1;j++)
				{
					for(int k=0;k<array3[0].length;k++)
					{
						array3[j][k] = temp[k];
					}
					constants3[j] = constant.get(i);
					//System.out.println("min" + minValue + " " +maxValue);
					//constants3[j] = minValue;
				}
			}

			Object[] objT = removeRedundant(array3,constants3);
			Object[] objF = removeRedundant(falseArray,falseConstant);
			resTrueArray[pq]=(double[][])objT[0];
			resTrueCons[pq]=(double[])objT[1];
			resFalseArray[pq]=(double[][])objF[0];
			resFalseCons[pq]=(double[])objF[1];
		}

		/*for(int i=0;i<resTrueArray.length;i++)
		{
			for(int j=0;j<resTrueArray[i].length;j++)
			{
				for(int k=0;k<resTrueArray[i][j].length;k++)
					System.out.print(resTrueArray[i][j][k]);
				System.out.println();
			}
			System.out.println("********");
		}*/

		int Tcount =0;
		int Tcnt =0;
		for(int i=0; i<orLines.length; i++)
		{
			Tcnt+=resTrueArray[i].length;
			for(int j=i+1; j<orLines.length; j++)
			{
				Tcount+=getRepetitions(resTrueArray[i],resTrueArray[j]);
			}
		}

		int Fcount =0;
		int Fcnt =0;
		for(int i=0; i<orLines.length; i++)
		{
			Fcnt+=resFalseArray[i].length;
			for(int j=i+1; j<orLines.length; j++)
			{
				Fcount+=getRepetitions(resFalseArray[i],resFalseArray[j]);
			}
		}

		double[][] resultTrueArray = new double[Tcnt-Tcount][array[0].length];
		double[] resultTrueCons = new double[Tcnt-Tcount];
		//System.out.println(Tcnt+"-"+Tcount);
		Tcount = 0;
		Tcnt = 0;
		for(int i=0; i<resTrueArray[0].length;i++)
		{
			//System.out.println(resTrueArray.length+"  "+resTrueArray[0].length);
			for(int j=0; j<array[0].length;j++)
			{
				//System.out.println(Tcnt+"....."+i+"......"+j);
				resultTrueArray[Tcnt][j] = resTrueArray[0][i][j];
			}
			resultTrueCons[Tcnt]=resTrueCons[0][i];
			Tcnt++;
		}

		for(int i=1; i<resTrueArray.length; i++)
		{
			Tcnt+=MergeOR(resultTrueArray,resultTrueCons,resTrueArray[i],resTrueCons[i],Tcnt);
		}

		double[][] resultFalseArray = new double[Fcnt-Fcount][array[0].length];
		double[] resultFalseCons = new double[Fcnt-Fcount];
		Fcount = 0;
		Fcnt = 0;
		for(int i=0; i<resFalseArray[0].length;i++)
		{
			for(int j=0; j<array[0].length;j++)
				resultFalseArray[Fcnt][j] = resFalseArray[0][i][j];
			resultFalseCons[Fcnt]=resFalseCons[0][i];
			Fcnt++;
		}

		for(int i=1; i<resFalseArray.length; i++)
		{
			Fcnt+=MergeAND(resultFalseArray,resultFalseCons,resFalseArray[i],resFalseCons[i],Fcnt);
		}
		/////////////////////////////////////////////////////////	
		Object obj2[];
		if(resultFalseArray!=null&&resultFalseCons!=null){
			obj2= removeRedundant(resultFalseArray, resultFalseCons);
			resultFalseArray = (double[][])obj2[0];
			resultFalseCons = (double[])obj2[1];
			
		}
		if(resultTrueArray!=null && resultTrueCons!=null){
			obj2= removeRedundant(resultTrueArray, resultTrueCons);
			resultTrueArray = (double[][])obj2[0];
			resultTrueCons = (double[])obj2[1];
		}
        if(resultTrueCons!=null)
        {
			double[] resultTrueConss2;
            double[] coeff = new double[resultTrueArray[0].length];
    		double result=0;
    		int c2=0;
    		double[][] resultTrueArray2;
    		/*System.out.println("dskjs " + resultTrueArray.length);
    		for(int i=0; i<resultTrueArray.length;i++)
    			for(int k=0; k<resultTrueArray[0].length;k++)
    				System.out.println(resultTrueArray[i][k]);*/
    		for(int i=0;i<resultTrueArray.length;i++)
    		{
    			int check=-1;
    			for(int j=0;j<resultTrueArray[0].length;j++)
    			{
    				coeff[j] = resultTrueArray[i][j]*-1;
    				for(int k=0;k<resultTrueArray.length;k++)
    				{
    					if(k!=i && Check(resultTrueArray[k],resultTrueArray[i]))
    					{
    						check = k;
    					}
    				}
    			}
    			//System.out.println(".............."+check);
    			if(check!=-1)
    			{
    				resultTrueArray2 = new double[resultTrueArray.length-2][resultTrueArray[0].length];
    				resultTrueConss2 = new double[resultTrueCons.length-2];
    			}
    			else
    			{
    				resultTrueArray2 = new double[resultTrueArray.length][resultTrueArray[0].length];
    				resultTrueConss2 = new double[resultTrueCons.length];
    			}
    			for(int j=0;j<resultTrueArray[0].length;j++)
    			{
    				coeff[j] = resultTrueArray[i][j]*-1;
    				for(int k=0;k<resultTrueArray.length;k++)
    				{
    					//if(k!=i && !Check(resultTrueArray[k],resultTrueArray[i]))
    					if(k!=i && k!=check)
    					{
    						//System.out.println("/*///*/*/*//"+c2);
    						resultTrueArray2[c2][j] = resultTrueArray[k][j];
    						resultTrueConss2[c2] = resultTrueCons[k];
    						c2++;
    					}
    				}
    				c2=0;
    			}
    			result = SimplexMethod(resultTrueArray2,resultTrueConss2,coeff)*-1;
    		//	System.out.println(result+"asdf"+resultTrueCons[i]);
    				if(result>resultTrueCons[i])
    					resultTrueCons[i] = result;
    		}
    	}
    	if(resultFalseCons!=null)
        {
			double[] resultFalseConss2;
            double[] coeff = new double[resultFalseArray[0].length];
    		double result=0;
    		int c2=0;
    		double[][] resultFalseArray2;
    		/*System.out.println("dskjs " + resultTrueArray.length);
    		for(int i=0; i<resultTrueArray.length;i++)
    			for(int k=0; k<resultTrueArray[0].length;k++)
    				System.out.println(resultTrueArray[i][k]);*/
    		for(int i=0;i<resultFalseArray.length;i++)
    		{
    			int check=-1;
    			for(int j=0;j<resultFalseArray[0].length;j++)
    			{
    				coeff[j] = resultFalseArray[i][j]*-1;
    				for(int k=0;k<resultFalseArray.length;k++)
    				{
    					if(k!=i && Check(resultFalseArray[k],resultFalseArray[i]))
    					{
    						check = k;
    					}
    				}
    			}
    			//System.out.println(".............."+check);
    			if(check!=-1)
    			{
    				resultFalseArray2 = new double[resultFalseArray.length-2][resultFalseArray[0].length];
    				resultFalseConss2 = new double[resultFalseCons.length-2];
    			}
    			else
    			{
    				resultFalseArray2 = new double[resultFalseArray.length][resultFalseArray[0].length];
    				resultFalseConss2 = new double[resultFalseCons.length];
    			}
    			for(int j=0;j<resultFalseArray[0].length;j++)
    			{
    				coeff[j] = resultFalseArray[i][j]*-1;
    				for(int k=0;k<resultFalseArray.length;k++)
    				{
    					//if(k!=i && !Check(resultTrueArray[k],resultTrueArray[i]))
    					if(k!=i && k!=check)
    					{
    						//System.out.println("/*///*/*/*//"+c2);
    						resultFalseArray2[c2][j] = resultFalseArray[k][j];
    						resultFalseConss2[c2] = resultFalseCons[k];
    						c2++;
    					}
    				}
    				c2=0;
    			}
    			result = SimplexMethod(resultFalseArray2,resultFalseConss2,coeff)*-1;
    		//	System.out.println(result+"asdf"+resultTrueCons[i]);
    				if(result>resultFalseCons[i])
    					resultFalseCons[i] = result;
    			}
    		}


		return new Object[]{resultTrueArray,resultTrueCons,usedVar,resultFalseArray,resultFalseCons};
	}
///*
	public static double[][] getArray(String str, String[] array, ArrayList<Double> constant)
	{
		//String[] strings = str.split("[o][r]");
		//for(int p=0;p<strings.length;p++)
		//{
			//String str_ = strings[p];
			String[] str1 = splitAND(str);
			//breaking using or
			double[][][] _result = new double[str1.length][][];
			double[][] _resultCons = new double[str1.length][];
			//ArrayList<Double> cons = new ArrayList<Double>();
			for(int ij=0; ij<str1.length; ij++)
			{
				//System.out.println("ij " + ij);
				ArrayList<Double> cons = new ArrayList<Double>();
				_result[ij] = _getArray(str1[ij],array,cons);
				/*for(int xx=0;xx<_result[ij].length;xx++)
				{
					for(int yy=0;yy<_result[ij][0].length;yy++)
						System.out.print(_result[ij][xx][yy] + "   ");
					System.out.println();
				}*/
				if(_result[ij]==null)
				{
					//System.out.println("return 1402");
					return null;
				}
				_resultCons[ij] = new double[cons.size()];
				for(int p = 0; p<cons.size(); p++)
				{
					_resultCons[ij][p] = cons.get(p);
				}

			}
			int count =0;
			int cnt =0;
			for(int i=0; i<str1.length; i++)
			{
				cnt+=_result[i].length;
				for(int j=i+1; j<str1.length; j++)
				{
					count+=getRepetitions(_result[i],_result[j]);
				}
			}
			double[][] result = new double[cnt-count][array.length];
			double[] resultCons = new double[cnt-count];
			count = 0;
			cnt = 0;
			for(int i=0; i<_result[0].length;i++)
			{
				for(int j=0; j<array.length;j++)
					result[cnt][j] = _result[0][i][j];
				resultCons[cnt]=_resultCons[0][i];
				cnt++;
			}

			for(int i=1; i<_result.length; i++)
			{
				//System.out.println("cnt  " + cnt + "_res " + _result.length + " _resCons " + _resultCons[i].length);
				cnt+=MergeAND(result,resultCons,_result[i],_resultCons[i],cnt);
			}

			for(int i=0; i<resultCons.length; i++)
			{
				constant.add(resultCons[i]);
			}
		

		/*for(int i=0;i<result.length;i++)
		{
			for(int j=0;j<result[0].length;j++)
				System.out.print(result[i][j]+"    ");
			System.out.print(resultCons[i]+"\n");
		}*/
		

		return result;
	}

	public static String[] splitAND(String str)
	{
		String[] _str = str.split("[a][n][d]");
		for(int i=0;i<_str.length-1;i++)
			if(!(_str[i].contains(">")||_str[i].contains("<")||_str[i].contains("=")))
			{
				_str[i]=_str[i]+"and"+_str[i+1];
				String[] __str = new String[_str.length-1];
				for(int j=0;j<_str.length;j++)
				{
					if(j<i+1)
						__str[j]=_str[j];
					else
						__str[j]=_str[j+1];
				}
				//_str.ArrayList.remove(i+1);
				_str = __str;
				i--;
			}
			else
			{
				String[] eq;
				if(_str[i].contains(">=")||_str[i].contains("<="))
					eq = _str[i].split("[>][=]|[<][=]",2);
				else
					eq = _str[i].split("[<|>|=]",2);
				try{
					Double.parseDouble(eq[1]);
				}
				catch(Exception e){
					_str[i]=_str[i]+"and"+_str[i+1];
					String[] __str = new String[_str.length-1];
					for(int j=0;j<_str.length-1;j++)
					{
						if(j<i+1)
							__str[j]=_str[j];
						else if(j>i+1)
							__str[j]=_str[j+1];
					}
					//_str.ArrayList.remove(i+1);
					_str = __str;
					i--;
				}
			}
			//System.out.println("splitAND  " + _str.length);
		return _str;
	}

	public static String[] splitOR(String str)
	{
		//System.out.println("OR str  " + str);
		String[] _str = str.split("[o][r]");
		//System.out.println(_str[0]);
		for(int i=0;i<_str.length-1;i++)
		{

			//System.out.println(_str[i] + "  " + _str.length + "  " + i);
			if(!(_str[i].contains(">")||_str[i].contains("<")||_str[i].contains("=")))
			{
				//System.out.println("1.."+_str[i] + "  " + _str[i+1] );
				_str[i]=_str[i]+"or"+_str[i+1];
				//System.out.println(_str[i]);
				String[] __str = new String[_str.length-1];
				for(int j=0;j<_str.length-1;j++)
				{
					if(j<i+1)
					{
						__str[j]=_str[j];
						//System.out.println("__str  "+j+"  " + __str[j]);
					}
					else
					{
						__str[j]=_str[j+1];
						//System.out.println("__str " + __str[j]);
					}
				}
				//System.out.println("__str " + __str[i+1]);
				_str = __str;
				i--;
			}
			else
			{
				//System.out.println("2.."+_str[i]);
				String[] eq;
				if(_str[i].contains(">=")||_str[i].contains("<="))
					eq = _str[i].split("[>][=]|[<][=]",2);
				else
					eq = _str[i].split("[<|>|=]",2);
				try{
					Double.parseDouble(eq[1]);
				}
				catch(Exception e){
					_str[i]=_str[i]+"or"+_str[i+1];
					String[] __str = new String[_str.length-1];
					for(int j=0;j<_str.length-1;j++)
					{
						if(j<i+1)
							__str[j]=_str[j];
						else if(j>i+1)
							__str[j]=_str[j+1];
					}
					_str = __str;
					i--;
				}
			}
		}
		//System.out.println(_str.toString());
		return _str;
	}

	public static Object[] Intersection(double[][]array1, double[] cons1, double[][] array2, double[] cons2){
		int rep = getRepetitions(array1,array2);
		int len = array1.length + array2.length - rep;
		double[][] array = new double[len][array1[0].length];
		double[] cons = new double[len];
		for(int i=0;i<array1.length;i++)
		{
			for(int j=0;j<array1[0].length;j++)
				array[i][j] = (int)array1[i][j];
			cons[i] = (int)cons1[i];
		}

		int cnt  = _Intersection(array,cons,array2,cons2,array1.length);
		return new Object[] {array,cons};
	}

	public static int _Intersection(double[][] array1, double[] cons1, double[][] array2, double[] cons2, int c){
		int count = 0, check =0;
		for(int i=0; i<array2.length; i++)
		{
			check = 0;	
			for(int j=0; j<array1.length; j++)
			{
				int cnt = 0;
				for(int k=0;k<array1[0].length;k++)
				{
					if((int)array2[i][k]==array1[j][k])
						cnt++;
				}
				if(cnt==array1[0].length)
				{
					if(cons1[j]<cons2[i])
						cons1[j]=cons2[i];
					break;
				}
				else
				{
					check++;
				}
			}
			if(check==array1.length)
			{
				//System.out.println("sjjsj"+c+" "+count);
				for(int k=0;k<array1[0].length;k++)
					array1[c+count][k]=array2[i][k];
				cons1[c+count]=cons2[i];
				count++;			
			}
			
		}
		return count;
	}

	/*public static void mergeUpdate(double[][] array, double[] cons ,double[][] array1, double[] cons1, double[][] array2, double[] cons2)
	{
		for(int i=0; i<array1.length; i++)
		{
			for(int j=0; j<array2.length; j++)
			{
				int cnt=0;
				for(int k=0; k<array2[0].length; k++)
				{
					if(array1[i][k]==array2[j][k])
						cnt++;
				}
				if(cnt==array2[0].length)
				{
					
					break;
				}
			}
		}
	}*/

	public static int MergeAND(double[][] array1, double[] cons1, double[][] array2, double[] cons2, int c)
	{
		int count = 0;
		int check = 0;
		for(int i=0; i<array2.length; i++)
		{
			check = 0;
			
			for(int j=0; j<array1.length; j++)
			{
				int cnt = 0;
				for(int k=0;k<array1[0].length;k++)
				{
					if(array2[i][k]==array1[j][k])
						cnt++;
				}
				if(cnt==array1[0].length)
				{
					//System.out.println("cons1 "+cons1.length+"  cons2  "+cons2.length);
					if(cons1[j]<cons2[i])
						cons1[j]=cons2[i];
					break;
				}
				else
				{
					check++;
			
				}
			}
			if(check==array1.length)
			{
				//System.out.println("sjjsj"+c+count);
				for(int k=0;k<array1[0].length;k++)
					array1[c+count][k]=array2[i][k];
				cons1[c+count]=cons2[i];
				count++;
			}
		}
		return count;
	}

	public static int MergeOR(double[][] array1, double[] cons1, double[][] array2, double[] cons2, int c)
	{
		int count = 0;
		int check = 0;
		for(int i=0; i<array2.length; i++)
		{
			check = 0;
			
			for(int j=0; j<array1.length; j++)
			{
				int cnt = 0;
				for(int k=0;k<array1[0].length;k++)
				{
					if(array2[i][k]==array1[j][k])
						cnt++;
				}
				if(cnt==array1[0].length)
				{
					if(cons1[j]>cons2[i])
						cons1[j]=cons2[i];
				}
				else
				{
					//System.out.println("sjjsj"+c+count);
					check++;
				}
			}
			if(check==array1.length)
			{
				for(int k=0;k<array1[0].length;k++)
						array1[c+count][k]=array2[i][k];
				cons1[c+count]=cons2[i];
				count++;
			}
		}
		return count;
	}

	public static int getRepetitions(double[][] array1, double[][] array2)
	{
		int count =0 ;
		for(int i=0; i<array1.length; i++)
		{
			
			for(int j=0; j<array2.length; j++)
			{
				int cnt = 0;
				for(int k=0;k<array1[0].length;k++)
				{
					if(array1[i][k]==array2[j][k])
						cnt++;
				}
				if(cnt==array1[0].length)
					count++;
			}
		}
		return count;
	}

	//*/

	public static double[][] _getArray(String str_,String[] array,ArrayList<Double> constant)
	{

			ArrayList<String> demo = new ArrayList<String>();
			String str=str_;
			if(str.contains("=")&&!str.contains(">=")&&!str.contains("<="))
			{
				String[] demo2 = replace2(str);
				for(int i=0;i<demo2.length;i++)
				{
					demo.add(demo2[i]);
				}
			}
			else
			{
			    str = replace1(str);
			    if(!str.equals(""))
			    	demo.add(str);
			}
			
			double[][] array2 = new double[demo.size()][array.length];
			
			for(int i=0;i<demo.size();i++)
			{
				str = demo.get(i);
				String[] lines;

				if(str.contains(">="))
					 lines = str.split("[>][=]",2);
		        else if(str.contains("<="))
		            lines = str.split("[<][=]",2);
				else
					lines = str.split("[<|>|=]",2);

				try{  
    				constant.add(Double.parseDouble(lines[1]));
    			}
    			catch(Exception e){
    				System.out.println("return 1648");
    				return null;
    			}  
				constant.add(Double.parseDouble(lines[1]));
				String[] lines2 = lines[0].split("[+]");
				int index = -1;

				if(lines2.length>2)
					return null;

				for(int k=0;k<lines2.length;k++)
				{
					int ind_val = 1;
					if(lines2[k].contains("-"))
					{
						ind_val = -1;
						try{
							lines2[k] = lines2[k].split("[-]")[1];
						}
						catch(Exception e)
						{
							return null;
						}
					}
					index = findIndex(lines2[k],array);
					array2[i][index] = ind_val;
				}
			}
		return array2;
	}

	public static Object[] getArray2(String str_,String[] array)
	{

		String[] str1 = str_.split(",");
		double cnt = 0;
		ArrayList<String> demo = new ArrayList<String>();
		
		int pqr=0;

		for(int ij=0; ij<str1.length; ij++)
		{
			String str=str1[ij];
			
				String[] demo2 = replace2(str);
				for(int i=0;i<demo2.length;i++)
					if(demo2[i].contains(">="))
						demo.add(demo2[i]);
		}
		double[][] array2 = new double[demo.size()][array.length];
		for(int ij=0; ij<str1.length; ij++)
		{
			String str=str1[ij];
			int xyz=0;
				String[] demo2 = replace2(str);
				for(int i=0;i<demo2.length;i++)
					if(demo2[i].contains(">="))
						xyz++;

			for(int i=pqr;i<xyz+pqr;i++)
			{
				String[] lines  = new String[2];
				if(str.contains(">="))
					 lines = str.split("[>][=]",2);
		        else if(str.contains("<="))
		            lines = str.split("[<][=]",2);
				else
					lines = str.split("[<|>|=]",2);

				//constant.add(Double.parseDouble(lines[1]));
				lines[1]=lines[1].replaceAll("\\s+","");
				lines[1]=lines[1].replaceAll("-","+-");
				String[] lines2 = lines[1].split("[+]");   // ******** changed from lines[0] to lines[1]
				int index = -1;
				for(int k=0;k<lines2.length;k++)
				{
				   int count=0;
					String[] lines31 = lines2[k].split("[*]"); ////////////////////b*c/d : b , "**c/d**"
					double ind_val = 1;
					for(int lk=0;lk<lines31.length;lk++)
					{
						String[] lines3 = lines31[lk].split("[/]");
						double ind_val_tmp = 1;

						if(isNumber(lines3[0]) && !(lines3.length==1 && lines31.length==1))
						{
							ind_val*=Double.parseDouble(lines3[0]);
							//System.out.println("***** "+ind_val+" ****");
						}
						else
						{
							count=0;
							//System.out.println("lines3"+lines3[0]);
							for(int lm=0;lm<lines3[0].length();lm++)
							{
								//System.out.println(lines3[l]);
								if(isNumber(lines3[0].charAt(lm)+"")||lines3[0].charAt(lm)=='-'||lines3[0].charAt(lm)=='.')
								{
			                        count++;
			                        //System.out.println("count"+count);
								}
							}
							if(count==0)
							{
								//System.out.println("0000000000000"+lines3[0]);
							    index = findIndex(lines3[0], array);
								ind_val *= 1;
							}
							else if (count == lines3[0].length()) 
							{
									//System.out.println(Double.parseDouble(lines3[l]+"") + "    " +count);
									cnt += Double.parseDouble(lines3[0]+"");
									//System.out.println("**************************************" + cnt);
							}
							else if(count==1 && lines3[0].charAt(0)=='-')
							{
									index = findIndex(lines3[0].substring(1, lines3[0].length()), array);
									ind_val *= -1;
							}
							else
							{
								//System.out.println("count"+count+"  **"+lines3[l]);
								index = findIndex(lines3[0].substring(count,lines3[0].length()), array);
								ind_val *= Double.parseDouble(lines3[0].substring(0,count));
							}
						}

						for(int l=1;l<lines3.length;l++)
						{
							if(isNumber(lines3[l]) && !(lines3.length==1 && lines31.length==1))
							{
								ind_val_tmp/=Double.parseDouble(lines3[l]);
								//System.out.println("***** "+ind_val+" ****");
							}
							else
							{
								count=0;
							    //System.out.println("lines3"+lines3[0]);
								for(int lm=0;lm<lines3[l].length();lm++)
								{
									//System.out.println(lines3[l]);
									if(isNumber(lines3[l].charAt(lm)+"")||lines3[l].charAt(lm)=='-'||lines3[l].charAt(lm)=='.')
									{
				                        count++;
				                        //System.out.println("count"+count);
									}
								}
								if(count==0)
								{
									//System.out.println(lines3[l]);
								    index = findIndex(lines3[l], array);
									ind_val_tmp /= 1;
								}
								else if (count == lines3[l].length()) 
								{
										//System.out.println(Double.parseDouble(lines3[l]+"") + "    " +count);
										cnt += Double.parseDouble(lines3[l]+"");
										//System.out.println("**************************************" + cnt);
								}
								else if(count==1 && lines3[l].charAt(0)=='-')
								{
									index = findIndex(lines3[l].substring(1, lines3[l].length()), array);
									ind_val_tmp /= -1;
								}
								else
								{
									//System.out.println("count"+count+"  **"+lines3[l]);
									index = findIndex(lines3[l].substring(count,lines3[l].length()), array);
									ind_val_tmp /= Double.parseDouble(lines3[l].substring(0,count));
								}
							}

							ind_val*=ind_val_tmp;
						}
					}
					//System.out.println("index::::"+ind_val);
					array2[i][index] = ind_val;
					////////////////////////////////
					/*
					if(lines3.length>1&&lines3[1]!=null&&isNumber(lines3[0]))
					{
						double coeff = Double.parseDouble(lines3[0]);
						index = findIndex(lines3[1], array);
						array2[i][index] = coeff;
					}
					else
					{
					    
					    count=0;
					    //System.out.println("lines3"+lines3[0]);
						for(int l=0;l<lines3[0].length();l++)
						{
							System.out.println(lines3[0]);
							if(isNumber(lines3[0].charAt(l)+"")||lines3[0].charAt(l)=='-'||lines3[0].charAt(l)=='.')
							{
		                        count++;
		                        //System.out.println("count"+count);
							}
						}
						if(count==0)
						{
							//System.out.println(lines3[0]);
						    index = findIndex(lines3[0], array);
							array2[i][index] = 1;
						}
						else if (count == lines3[0].length()) 
						{
								//System.out.println(Double.parseDouble(lines3[0]+"") + "    " +count);
								cnt += Double.parseDouble(lines3[0]+"");
								//System.out.println("**************************************" + cnt);
						}
						else if(count==1 && lines3[0].charAt(0)=='-')
						{
							index = findIndex(lines3[0].substring(1, lines3[0].length()), array);
							array2[i][index] = -1;
						}
						else
						{
							//System.out.println("count"+count+"  **"+lines3[0]);
							index = findIndex(lines3[0].substring(count,lines3[0].length()), array);
							array2[i][index] = Double.parseDouble(lines3[0].substring(0,count));
						}
					}
					*/
					//////////////////////////
				}
			}
			pqr+=xyz;
			//add array2 to something else
		}

		//return array2;
			return new Object[]{array2,cnt};
	}

	public static String getExpression(String str)
	{
		String str2 = "";
		str = str.toLowerCase();
		if(str.toLowerCase().contains("select")||str.toLowerCase().contains("delete")||str.toLowerCase().contains("update"))
		{
			for(int i=0;i<str.length();i++)
			{
				if(str.charAt(i)!=' '&&str.charAt(i)!='\t')
					str2 = str2+str.charAt(i);
			}
			String[] demo = str2.split("[w][h][e][r][e]");
			String[] demo2;
			if(demo.length>1)
				demo2 = demo[1].split("[\"]");
			else
				return null;
		//	System.out.println("DMOE"+ demo2[0]);
			return demo2[0];
		}
		return null;
	}
	public static String replace1(String str)
	{
		String line = "";
		int index=-1;
		if(str.contains(">"))
		{
		for(int i=0;i<str.length();i++)
		{
		     if(str.charAt(0)=='+'&&i==0)
                i++;
			if(str.charAt(i)=='='||(str.charAt(i)=='>'&&str.charAt(i+1)!='='))
            {
                if(str.charAt(i+1)=='-')
                {
                    line =line + "=-";
                    i++;
                }
                else if(str.charAt(i+1)=='+')
                {
                    line = line + "=";
                    i++;
                }
                else
                    line = line + "=";
            }
			else if(str.charAt(i)=='-'&&i!=0)
			{
				line = line + "+-";
			}
			else
				line = line + str.charAt(i);
		}
		}
		else if(str.contains("<"))
		{
			if(!(str.charAt(0)=='-'))
				line = line + "-";
			for(int i=0;i<str.length();i++)
			{
			     if(str.charAt(0)=='+'&&i==0)
                    i++;
				if(str.charAt(i)=='='||(str.charAt(i)=='<'&&str.charAt(i+1)!='='))
				{
					if(!(str.charAt(i+1)=='-')&&isNumber(str.charAt(i+1)+""))
						line = line + "=-";
                    else if(str.charAt(i+1)=='+')
                    {
                        line = line + "=-";
                        i++;
                    }
					else
					{
						line = line + "=";
						i++;
					}
				}
				else if(str.charAt(i)=='+')
				{
					line = line + "+-";
				}
				else if(str.charAt(i)=='-')
                {
                    if(i!=0)
                        line = line+"+";
                }
				else
					line = line + str.charAt(i);
			}
		}
		return line;
	}
	public static String[] replace2(String str)
	{
		String[] line = new String[2];
		for(int i=0;i<line.length;i++)
			line[i] = "";
		for(int i=0;i<str.length();i++)
		{
			if(str.charAt(i)=='=')
				line[0] = line[0] + ">=";
			else
				line[0] = line[0] + str.charAt(i);
		}
		for(int i=0;i<str.length();i++)
		{
			if(str.charAt(i)=='=')
				line[1] = line[1] + "<=";
			else
				line[1] = line[1] + str.charAt(i);
		}
		line[0]  = replace1(line[0]);
		line[1] = replace1(line[1]);
		return line;
	}
	public static boolean isNumber(String str)
	{
		try
		{
			double lune = Double.parseDouble(str);
		}
		catch(Exception e)
		{
			return false;
		}
			return true;
	}
	public static double SimplexMethod(double[][] array,double[] constants,double[] coeff2)       //returns maximum value
	{
		int bigM = -999999;
		ArrayList<Integer> positive = new ArrayList<Integer>();
		ArrayList<Integer> negative = new ArrayList<Integer>();
		for(int i=0;i<constants.length;i++)
		{
			if(constants[i]>=0)
				positive.add(i);
			else
				negative.add(i);
		}
		for(int i=0;i<negative.size();i++)
		{
			for(int j=0;j<array[0].length;j++)
			{
				array[negative.get(i)][j]*=-1;
			}
			constants[negative.get(i)]*=-1;
		}
	/*	System.out.println("POSITIVE : ");
		for(int i=0;i<positive.size();i++)
		{
			for(int j=0;j<array[0].length;j++)
				System.out.print(array[positive.get(i)][j]+"   ");
			System.out.println();
		}
		System.out.println("NEGATIVE : ");
		for(int i=0;i<negative.size();i++)
		{
			for(int j=0;j<array[0].length;j++)
				System.out.print(array[negative.get(i)][j]+"   ");
			System.out.println();
		}*/
		int pNumber =  positive.size();
		int nNumber =  negative.size();
		int pCount=array[0].length,nCount=array[0].length + pNumber+nNumber;
		double[][] output = new double[array.length][array[0].length+2*positive.size()+negative.size()];
		double[] coeff = new double[output[0].length];
		for(int i=0;i<coeff.length;i++)
		{
			if(i<coeff2.length)
				coeff[i] = coeff2[i];
			else if(i>=coeff2.length+positive.size()+negative.size())
				coeff[i] = bigM;
		}
		int[] basic = new int[array.length];
		int bCount=0;
		for(int i=0;i<output.length;i++)
		{
			for(int j=0;j<output[0].length;j++)
			{
				if(j<array[0].length)
				{
					output[i][j] = array[i][j];
				}
				else if(j>=array[0].length&&j<array[0].length+pNumber+nNumber)
				{
					if(j==pCount&&isPositive(i,positive))
					{
						output[i][j] = -1;
					}
					else if(j==pCount)
					{
						output[i][j] = 1;
						basic[bCount++] = j;
					}
				}
				else
				{
					if(j==nCount&&isPositive(i, positive))
					{
						output[i][j] = 1;
						basic[bCount++] = j;
						nCount++;
						break;
					}
				}
			}
			pCount++;
		}
	/*	System.out.println("OUTPUT ARRAY INITIAL : ");
		for(int i=0;i<output.length;i++)
		{
			for(int j=0;j<output[0].length;j++)
				System.out.print(output[i][j]+"  ");
			System.out.println();
		}
        System.out.println("CONSTANTS : ");
			for(int i=0;i<constants.length;i++)
                System.out.println(constants[i]);
			System.out.println("RESULT : "); */
		double[] result = new double[output[0].length];
		result = computeZ(constants,coeff,output, basic);
		int index = -1;
		double indexValue = 999999;
		for(int i=0;i<result.length;i++)
		{
			if(indexValue>result[i])
			{
				index = i;
				indexValue = result[i];
			}
		//	System.out.print(result[i]+"   ");
		}
	//	System.out.println();
		int leavingIndex = -1;
		while(indexValue<0)
		{
			double min = 999999;
			for(int i=0;i<output.length;i++)
			{
				if(output[i][index]!=0&&constants[i]/output[i][index]>0&&min>=constants[i]/output[i][index])
				{
					if(min>constants[i]/output[i][index]||(min==constants[i]/output[i][index]&&basic[i]>=array[0].length+pNumber+nNumber))
					{
						leavingIndex = i;
						min = (constants[i]*1.0)/output[i][index];
					}
				}
			}
		/*	System.out.println("MRR : " + min);
			System.out.println("Leaving Index: " + leavingIndex);
			System.out.println("ENTERING INDEX : " + index); */
			if(leavingIndex==-1)
			{
				return 999999;
			}
			// CHANGING IN BASIC ARRAY
			basic[leavingIndex]= index;

			// CHANGING IN OUTPUT ARRAY
			double pivot = output[leavingIndex][index];
			for(int i=0;i<output[0].length;i++)
				output[leavingIndex][i]/=pivot;
			constants[leavingIndex]/=pivot;
			// CHANGE IN OUTPUT ARRAY
			for(int i=0;i<output.length;i++)
			{
                double p = output[i][index];
				if(i!=leavingIndex)
				{
				    constants[i] = constants[i] - p*constants[leavingIndex];
					for(int j=0;j<output[0].length;j++)
					{
						output[i][j] = output[i][j] - p*output[leavingIndex][j];
					}
					//System.out.println("AS    "+constants[i]);
				}
			}
			result = computeZ(constants,coeff,output, basic);
			indexValue = 999999;
	/*		System.out.println("COEFF : ");
			for(int i=0;i<coeff.length;i++)
				System.out.println(coeff[i]);
			System.out.println("OUTPUT : ");
			for(int i=0;i<output.length;i++)
			{
				for(int j=0;j<output[0].length;j++)
					System.out.print(output[i][j]+"    ");
				System.out.println();
			}
		//	System.out.println("CONSTANTS : ");
		//	for(int i=0;i<constants.length;i++)
        //        System.out.println(constants[i]);
		//	System.out.println("RESULT : "); */
			for(int i=0;i<result.length;i++)
			{
				if(indexValue>result[i])
				{
					index = i;
					indexValue = result[i];
				}
	//			System.out.print(result[i]+"   ");
			}
	//		System.out.println();
			leavingIndex = -1;
		}
		double count=0;
		// Z COMPUTATION
		for(int i=0;i<basic.length;i++)
		{
			if(coeff[basic[i]]!=bigM)
				count = count + coeff[basic[i]]*constants[i];
		}
		return count;
	}
	public static boolean isPositive(int index,ArrayList<Integer> Positive)
	{
		for(int i=0;i<Positive.size();i++)
			if(index==Positive.get(i))
				return true;
		return false;
	}
	public static double[] computeZ(double[] constants,double[] coeff,double[][] output,int[] basic)
	{
		double count=0;
		double[] result = new double[output[0].length];
		for(int i=0;i<output[0].length;i++)
		{
			count=0;
			for(int j=0;j<output.length;j++)
			{
				count = count + output[j][i]*coeff[basic[j]];
			}
			result[i] = count - coeff[i];
		}
		return result;
	}
	public static int findIndex(String str,String[] array)
	{
		//System.out.print("*"+str);
		str=str.replaceAll("\\s+","");
		//System.out.print("**"+str);
		for(int i=0;i<array.length;i++)
			if(str.equals(array[i]))
				return i;
		//System.out.println("////"+str);
		return -1;
	}

public static Object[] checkM(double[][] m,double []mCons){
	int att = m[0].length;
	double[][] m1 = new double[m.length][m[0].length];
	double[] m1cons = new double[m.length];
	ArrayList<Integer> order = new ArrayList<Integer>();
	ArrayList<Integer> iniOrder = new ArrayList<Integer>();
	for(int i=0;i<att;i++)
	{
		if(m[2*i][i]==1){
			order.add(2*i);
		}
		else{
			for(int p=0;p<m.length;p++)
			{
				boolean isArray = false;
				if(m[p][i]==1)
				{
					isArray = true;
					for(int q=0;q<m[0].length-1;q++)
					{
						if(q!=i&&m[p][q]!=0)
							isArray=false;
					}
				}
				if(isArray)
				{
					order.add(p);
					break;
				}
			}
		}
		if(m[2*i+1][i]==-1){
			order.add(2*i+1);
		}
		else{
			for(int p=0;p<m.length;p++)
			{
				boolean isArray = false;
				if(m[p][i]==-1)
				{
					isArray = true;
					for(int q=0;q<m[0].length-1;q++)
					{
						if(q!=i&&m[p][q]!=0)
							isArray=false;
					}
				}
				if(isArray)
				{
					order.add(p);
					break;
				}
			}
		}
	}

	//System.out.println(order.toString());
	int size = order.size();
	int diff = m.length - size;

	for(int i=0; i<m.length;i++){
		iniOrder.add(i);
	}

	for(int i=0;i<size;i++){
		m1[i] = m[order.get(i)].clone();
		m1cons[i] = mCons[order.get(i)];
		iniOrder.remove( (Integer) (order.get(i)) );
	}
	//System.out.println(iniOrder.toString());
	for(int i=0;i<diff;i++){
		m1[i+size] = m[iniOrder.get(i)].clone();
		m1cons[i+size] = mCons[order.get(i)];
	}

	return new Object[]{m1,m1cons};

}
}