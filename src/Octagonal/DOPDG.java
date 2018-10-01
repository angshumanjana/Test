package Octagonal;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.Timestamp;
import java.util.*;
class Variables
{
	String type;
	String name;
	ArrayList<Integer> UsedLines = new ArrayList<Integer>();
	ArrayList<Integer> DefinedLines = new ArrayList<Integer>();
	ArrayList<String> CurrentBlockDefined = new ArrayList<String>();
	ArrayList<String> Type = new ArrayList<String>();
	ArrayList<Integer> State = new ArrayList<Integer>();
}
class Block
{
	String name;
	Deque<Variables> variables = new ArrayDeque<Variables>();
	public Block(String name,Deque<Variables> var)
	{
		this.name = name;
		variables = var;
	}
}
class ForReturn
{
	int argNumber;
	int lineNumber;
	String CalledFunction;
}
class Function
{
	String name;
	int argCount;
	int lineNumber;
}
public class DOPDG
{
	static Deque stack = new ArrayDeque();
	static ArrayList<String> Tables = new ArrayList<String>();
	static ArrayList<ForReturn> ReturnStatement = new ArrayList<ForReturn>();
	static String[][] TableVariable;
	static String strNew = "";
	static int Array[][];
	static int nTab=0;
	static int lineno=1;
	static int finalLineNo;
	static boolean isp=false;
	static int oldLine=0;
	static ArrayList<Integer> newLine = new ArrayList<Integer>();
	static ArrayList<Integer> ArrayControl = new ArrayList<Integer>();
	static Deque<Variables> Identifiers = new ArrayDeque<Variables>();
	static Deque<Block> CurrentBlock = new ArrayDeque<Block>();
	static int OutputArray[][];
	static ArrayList<Function> functions = new ArrayList<Function>();
	static int tryCount = 0;
	static int SwitchCount = 0;
	static ArrayList<String> MainArray = new ArrayList<String>();
	static java.sql.Timestamp ts1 ;
	public String main(File inputMain,InputHandler iw)
	{
		ts1 = new java.sql.Timestamp(System.currentTimeMillis());
		Scanner S = new Scanner(System.in);
		try
		{
			TableVariable = new String[1][];
			Tables.add(iw.tableName);
			String[] var = iw.variables;
			for(int i=0;i<var.length;i++)
			{
				TableVariable[0] = new String[var.length];
				TableVariable[0][i] = new String(var[i]);
				Variables V = new Variables();
				V.name = TableVariable[0][i];
				V.type = "Attribute";
				V.Type.add("Database");
				V.DefinedLines.add(0);
				V.State.add(1);
				V.CurrentBlockDefined.add("function");
				Identifiers.add(V);
				System.out.println("@@"+TableVariable[0][i]);
			}
			for(int i=0;i<TableVariable[0].length;i++)
				TableVariable[0][i] = var[i];
			for(int i=0;i<TableVariable[0].length;i++)
				System.out.println(TableVariable[0][i]);
			BufferedReader br = null,br2 = null,br3 = null;
			BufferedWriter bw = null;
			br = new BufferedReader(new InputStreamReader(new FileInputStream(inputMain)));
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.txt")));
			String line;
			int lineCount=0;
			while((line = br.readLine())!=null)
			{
				try
				{
				System.gc();
//				System.out.println(line);
				lineCount++;
				line = line.trim();
				String temp = "";
				for(int i=0;i<line.length();i++)
					if(line.charAt(i)!=' '&&line.charAt(i)!='\t')
						temp += line.charAt(i);
				line = temp;
				strNew = "";
			    printLine(line);
			    String[] lines20 = strNew.split("\n");
			    for(int y=0;y<lines20.length;y++)
			    MainArray.add(lines20[y]);
			    bw.write(strNew);
				}
				catch(Exception e)
				{
					System.out.println(line);
					e.printStackTrace();
				}
			}
			bw.close();
			int count1 = 0;
			String line1;
			int size = MainArray.size();
			BufferedReader br11 = new BufferedReader(new InputStreamReader(new FileInputStream("output.txt")));
			int yyyy = 0;
			while((line1 = br11.readLine())!=null)
			{
			//line1 = MainArray.get(yyyy);
		if(line1.contains("int ")||line1.contains("String ")||line1.contains("void "))
				{
					if(line1.contains("(")&&line1.contains(")")&&!line1.contains(";"))
					{
						System.out.println("AAA"+line1);
						String[] lines1 = new String[2];
						if(line1.contains("void"))
							lines1 = line1.split("[v][o][i][d]",2);
						else if(line1.contains("String"))
							lines1 = line1.split("[S][t][r][i][n][g]",2);
						else if(line1.contains("int"))
							lines1 = line1.split("[i][n][t]",2);


						String[] lines2 = lines1[1].split("[(]",2);
						String[] lines3 = lines2[1].split("[)]",2);

						Function func = new Function();
						func.name = lines2[0];
						func.argCount = CountArgs(lines3[0]);
						func.lineNumber = count1;
						functions.add(func);
						System.out.println("function found"+count1);
					}
				}
		    yyyy++;
	            count1++;
			}
			br11.close();
			System.out.println("Completed Function Determination");
			//System.out.println(finalLineNo);
			int finalLineNo = count1;
		//	System.out.println("ARRAY LENGTH : " + finalLineNo);
			OutputArray = new int[finalLineNo][finalLineNo];
			//br11.close();
			//System.out.println("a3");
			br3 = new BufferedReader(new InputStreamReader(new FileInputStream("output.txt")));
			String line2 = null;
			int count2 = 0,count3=0,count4=0;
			boolean value = false;
			int lineNumber=0;
			CurrentBlock.push(new Block("function",Identifiers));
			yyyy = 0;
			System.out.println("Entering Main Block");
			while((line2 = br3.readLine())!=null)
			{
				//line2 = MainArray.get(yyyy);
				//yyyy++;
				System.out.println("Here" + lineNumber + "  "+line2);
				lineNumber++;
				/*if(!value)
					ArrayControl.add(oldLine);
				else
				{
					ArrayControl.add(newLine.get(newLine.size()-1));
				}
				if(count3!=0)
				{
					ArrayControl.set(count4,newLine.get(newLine.size()-1));
					newLine.remove(newLine.size()-1);
					count3--;
				}
				if(line2.contains("if")||line2.contains("else if")||line2.contains("while")||line2.contains("else"))
				{
					value = true;
					newLine.add(count4);
					count3++;
				}
				if(line2.contains("{"))
				{
						count2++;
						if(count2>1)
						{
							value = true;
							newLine.add(count4-1);
						}
				}
				else if(line2.contains("}"))
				{
					count2--;
					if(count2<=1)
					{
						value = false;
					}
					else
					{
						value = true;
						newLine.remove(newLine.size()-1);
					}
					//CurrentBlock.removeLast();                               // UPDATING THE CURRENT BLOCK AND SET OF IDENTIFIERS
					if(CurrentBlock.size()!=1)
					{
						CurrentBlock.removeLast();
						Identifiers = CurrentBlock.getLast().variables;
					}
				//	else
				//		System.out.println("Cannot Update Identifiers, line Number " + lineNumber);
				}
				count4++;*/
				// CONTROL COMPLETED

				// PROGRAM DEPENDENCE


				// REMOVING SPACES AND TABS

			//	System.out.println("final : " + line2);
				String line10 = "";
				int count=0;
				String number = "";
				while(isDigit(line2.charAt(count)))
				{
				    number = number + line2.charAt(count);
					count++;
				}
				for(int i=count;i<line2.length();i++)
	            {
	                if(line2.charAt(i)!=' '&&line2.charAt(i)!='\t')
	                    line10 = line10 + line2.charAt(i);
	            }
	            line2 = line10;
				// READING LINES


				// IGNORING FOR ANGLE BRACES

				if(line2.charAt(0)=='<'||line2.charAt(0)=='/')
					continue;

				// IGNORING IF PRINT STATEMENT

				if(line2.contains("out.print"))
				{
					String[] temp1 = line2.split("[(]", 2);
					String[] temp2 = temp1[1].split("[)]",2);
					getStringIdentifiers2(temp2[0], lineNumber, "Program");
					continue;
				}


				// CHECKING FOR FUNCTION CALL
				for(int i=0;i<functions.size();i++)
					if(line2.contains(functions.get(i).name))
					{
						if(lineNumber-1!=functions.get(i).lineNumber)
						{
							OutputArray[lineNumber-1][functions.get(i).lineNumber] = 5;
						}
					}


				//  CHECKING IF TRY OR CATCH OF FINALLY
				if(line2.contains("try")||line2.contains("catch")||line2.contains("finally"))
				{
				if(line2.contains("try"))
					CurrentBlock.add(new Block("Try " + tryCount,Identifiers));
				if(line2.contains("catch"))
					CurrentBlock.add(new Block("Catch " + tryCount,Identifiers));
				if(line2.contains("finally"))
					CurrentBlock.add(new Block("finally " + tryCount,Identifiers));
				tryCount++;
				continue;
				}
				// CHECKING FOR ASSIGNMENT IN DATABASE STATEMENTS

				////// DO SOMETHING


				if(line2.contains("cn()"))
				{
					Object[] V = Identifiers.toArray();
					System.out.println("!!");
					for(int i=0;i<V.length;i++)
					{
						if(((Variables)V[i]).type.equals("Attribute"))
						{
							System.out.println("@@@@@");
							((Variables)V[i]).DefinedLines.add(lineNumber);
							((Variables)V[i]).Type.add("Database");
							((Variables)V[i]).CurrentBlockDefined.add(CurrentBlock.getLast().name);
							((Variables)V[i]).State.add(1);
						}
					}
				}


				String temp3 = line2;
				if(line2.toLowerCase().contains("insert")||line2.toLowerCase().contains("update")||line2.toLowerCase().contains("select")||line2.toLowerCase().contains("delete"))
				{
					line2 = temp3;
					if(!(line2.contains("if")||line2.contains("Action")))
					{
						if(line2.contains("executeUpdate"))
						{
							String[] lines = line2.split("[e][(]",2);
							String[] lines3 = lines[1].split("[)][;]",2);
							line2 = lines3[0]+";";
							System.out.println("LINE2 : " + line2);
							checkDatabase(line2,lineNumber, "Database");
						}
						else
						{
					String line11 = "";
					for(int i=0;i<line2.length();i++)
						if(line2.charAt(i)!='"'&&line2.charAt(i)!='+')
							line11 = line11 + line2.charAt(i);
					line2 = line11;
					if(line2.contains("="))
					{
						String[] lines = line2.split("[=]",2);
						getStringDeclarations(lines[0]+";", lineNumber,"Database");
						if(lines[1].contains("openrs"))
						{
							String[] temp = lines[1].split("[,]",2);
							String[] temp2 = temp[1].split("[)][;]",2);
							lines[1] = temp2[0] + ";";
						}
						System.out.println("line0 :"+lines[1]);
						checkDatabase(lines[1], lineNumber, "Database");
					}
					else
					{
						checkDatabase(line2,lineNumber, "Database");
					}
						}
					continue;
					}
				}



				// CHECKING FOR ASSIGNMENT STATEMENT IN NON-DATABASE STATEMENTS

				if(line2.contains("=")&&!(line2.contains("==")||line2.contains(">=")||line2.contains("<=")||line2.contains("!=")))
				{
					System.out.println("Assignment : "+ line2);
					getIdentifiers(line2,lineNumber,"Program");
					int pos=-1;
					for(int i=0;i<line2.length();i++)
						if(line2.charAt(i)=='=')
						{
							pos = i;
							break;
						}
					String temp = line2.substring(0,pos);
					String temp2 = line2.substring(pos+1, line2.length());
					String type = "NoTypeFound";
					Object[] V = Identifiers.toArray();
					for(int i=V.length-1;i>=0;i--)
					{
						if(temp.equals(((Variables)V[i]).name))
						{
							type = ((Variables)V[i]).type;
							((Variables)V[i]).DefinedLines.add(lineNumber);
							((Variables)V[i]).Type.add("Program");
							((Variables)V[i]).CurrentBlockDefined.add(CurrentBlock.getLast().name);
							((Variables)V[i]).State.add(0);
							break;
						}
					}
					if(type.equals("Integer"))
						getArithmeticUsed(temp2, lineNumber, "Program");
					else if(type.equals("String"))
						getStringUsed(temp2,lineNumber,"Program");
					else if(type.equals("NoTypeFound"))
						System.out.println("Identifier Type not found in line number " + lineNumber);
					continue;
				}

				// CONDITIONAL OPERATIONS
				if(line2.contains("if")||line2.contains("else")||line2.contains("while")||line2.contains("switch"))
				{
					CurrentBlock.getLast().variables = Identifiers;
					if(line2.contains("if"))
						CurrentBlock.add(new Block("if",Identifiers));
					else if(line2.contains("else"))
						CurrentBlock.add(new Block("else",Identifiers));
					else if(line2.contains("while"))       // UPDATING THE CURRENT BLOCK
						CurrentBlock.add(new Block("while",Identifiers));
					else if(line2.contains("switch"))
					{
						System.out.println("SWITCH FOUND");
						SwitchCount++;
						CurrentBlock.add(new Block("switch"+SwitchCount,Identifiers));
					}
					if(line2.contains("if")||line2.contains("while"))
						getBooleanIdentifiers(line2, lineNumber,"Program");
					else if(line2.contains("switch"))
						getBooleanIdentifiersSwitch(line2, lineNumber,"Program");
					continue;
				}

			}
		}
		catch(Exception e)
		{
			System.out.println("STR : "+str);
			e.printStackTrace();
		}
		finally
		{
			if(OutputArray!=null)
			{
				Object[] V = Identifiers.toArray();
				for(int i=0;i<V.length;i++)
					System.out.println("Variables : " + ((Variables)V[i]).name);
				print();
			}
			else
			{
				System.out.println("STR : "+str);
				System.out.println("Some problem");
			}
			return str;
	}
	}
	public static boolean isDigit(char c)
	{
		if(c=='0'||c=='1'||c=='2'||c=='3'||c=='4'||c=='5'||c=='6'||c=='7'||c=='8'||c=='9')
			return true;
		else
			return false;
	}
	static String str = "";
	public static String printLine(String line)
	{
	//	System.out.println("LINE11 : " + line);
		if(line.length()==0)
			return "";
		else
		{
			if(line.contains("{"))
			{
				String[] lines = line.split("[{]",2);
				str = str + printLine(lines[0]);
			//	printTab();
				print(lineno+"{\n");
				lineno++;
		//		nTab++;
				str = str + printLine(lines[1]);
			}
			else
				str = str + printLine2(line);
		}
		return str;
	}
	static String str2 = "";
	public static String printLine2(String line)
	{
		if(line.length()==0)
			return "";
		else
		{
			if(line.contains("}"))
			{
				String[] lines = line.split("[}]", 2);
				str2 = str2 + printLine2(lines[0]);
			//	nTab--;
			//	str2 = str2 + printTab();
				str2 = str2 + print(lineno+"}\n");
				lineno++;
				str2 = str2 + printLine2(lines[1]);
			}
			else
				str2 = str2 + printLine3(line);
		}
		return str2;
	}
	static String str3 = "";
	public static String printLine3(String line)
	{
		//str3 = str3 + printTab();
		str3  = str3 + print(lineno+line+"\n");
		lineno++;
		if(line.contains("while") || line.contains("for") || line.contains("if"))
		{
			isp=true;
		}
		return str3;
	}
	static String str4 = "";
	private static String print(String string) {
	//	if(isp && !string.startsWith("{")){
	//		strNew = strNew + "\t";
	//	}
		strNew = strNew + string;
		isp=false;
		return str4;
	}
	static String str5="";
	public static String printTab()
	{
		str5 = str5 + printLineno();
		return str5;
	}
	public static String printLineno()
	{
		strNew = strNew  + lineno++ ;
		return null;
	}
	static void getIdentifiers(String line,int lineNumber,String Type)
	{
		if(line.contains("int")||line.contains("long"))
			getArithmeticDeclarations(line,lineNumber,Type);
		else if(line.contains("String"))
			getStringDeclarations(line,lineNumber,Type);
		else if(line.contains("ResultSet"))
			getResultSetDeclarations(line,lineNumber,Type);
	}
	static void getArithmeticDeclarations(String str,int lineNumber,String Type)
	{
		//System.out.println("HERE : " + str);
		if(str.contains("int"))
		{
			for(int i=0;i<str.length()-3;i++)
			if(str.substring(i, i+3).equals("int"))
			{
				str = str.substring(i+3,str.length());
				break;
			}
		}
		if(str.contains("long"))
		{
			for(int i=0;i<str.length()-4;i++)
				if(str.substring(i, i+4).equals("long"))
				{
					str = str.substring(i+4, str.length());
					break;
				}
		}
		if(str.contains(";"))
		{
			String[] lines = str.split("[;]");
			for(int i=0;i<lines.length;i++)
				getArithmeticDeclarations(lines[i],lineNumber,Type);
		}
		else if(str.contains(","))
		{
			String[] lines = str.split("[,]");
			for(int i=0;i<lines.length;i++)
				getArithmeticDeclarations(lines[i],lineNumber,Type);
		}
		else if(str.contains("="))
		{
			int pos = -1;
			for(int i=0;i<str.length();i++)
				if(str.charAt(i)=='=')
				{
					pos = i;
					i = str.length();
				}
			String temp = str.substring(0,pos);
			Variables V = new Variables();
			V.name = temp;
			V.type = "Integer";
			V.DefinedLines.add(lineNumber);
			V.Type.add(Type);
			V.CurrentBlockDefined.add(CurrentBlock.getLast().name);
			V.State.add(1);
			Identifiers.push(V);
		}
		else
		{
			String temp = str;
			Variables V = new Variables();
			V.name = temp;
			V.type = "Integer";
			V.DefinedLines.add(lineNumber);
			V.Type.add(Type);
			V.State.add(1);
			V.CurrentBlockDefined.add(CurrentBlock.getLast().name);
			Identifiers.push(V);
		}
	}
	static void getStringDeclarations(String str,int lineNumber,String Type)
	{
		if(str.contains("String"))
		{
			for(int i=0;i<str.length()-6;i++)
			{
				if(str.substring(i,i+6).equals("String"))
				{
					str = str.substring(i+6,str.length());
					break;
				}
			}
		}
		if(str.contains(";"))
		{
			String[] lines = str.split("[;]");
			for(int i=0;i<lines.length;i++)
				getStringDeclarations(lines[i],lineNumber,Type);
		}
		else if(str.contains(","))
		{
			String[] lines = str.split("[,]");
			for(int i=0;i<lines.length;i++)
				getStringDeclarations(lines[i],lineNumber,Type);
		}
		else if(str.contains("="))
		{
			int pos = -1;
			for(int i=0;i<str.length();i++)
				if(str.charAt(i)=='=')
				{
					pos = i;
					i = str.length();
				}
			String temp = str.substring(0,pos);
			Variables V = new Variables();
			V.name = temp;
			V.type = "String";
			V.DefinedLines.add(lineNumber);
			V.State.add(1);
			V.Type.add(Type);
			V.CurrentBlockDefined.add(CurrentBlock.getLast().name);
			Identifiers.push(V);
		}
		else
		{
			String temp = str;
			Variables V = new Variables();
			V.name = temp;
			V.type = "String";
			V.DefinedLines.add(lineNumber);
			V.Type.add(Type);
			V.State.add(1);
			V.CurrentBlockDefined.add(CurrentBlock.getLast().name);
			Identifiers.push(V);
		}
	}
	static void getResultSetDeclarations(String str,int lineNumber,String Type)
	{
		if(str.contains("ResultSet"))
		{
			for(int i=0;i<str.length()-9;i++)
			{
				if(str.substring(i,i+9).equals("ResultSet"))
				{
					str = str.substring(i+9,str.length());
					break;
				}
			}
		}
		if(str.contains(";"))
		{
			String[] lines = str.split("[;]");
			for(int i=0;i<lines.length;i++)
				getResultSetDeclarations(lines[i],lineNumber,Type);
		}
		else if(str.contains(","))
		{
			String[] lines = str.split("[,]");
			for(int i=0;i<lines.length;i++)
				getResultSetDeclarations(lines[i],lineNumber,Type);
		}
		else if(str.contains("="))
		{
			int pos = -1;
			for(int i=0;i<str.length();i++)
				if(str.charAt(i)=='=')
				{
					pos = i;
					i = str.length();
				}
			String temp = str.substring(0,pos);
			Variables V = new Variables();
			V.name = temp;
			V.type = "ResultSet";
			V.DefinedLines.add(lineNumber);
			V.Type.add(Type);
			V.State.add(1);
			V.CurrentBlockDefined.add(CurrentBlock.getLast().name);
			Identifiers.push(V);
		}
		else
		{
			String temp = str;
			Variables V = new Variables();
			V.name = temp;
			V.type = "ResultSet";
			V.DefinedLines.add(lineNumber);
			V.Type.add(Type);
			V.State.add(1);
			V.CurrentBlockDefined.add(CurrentBlock.getLast().name);
			Identifiers.push(V);
		}
	}
	static void getArithmeticUsed(String str,int lineNumber,String type)
	{
		if(str.contains("int"))
		{
			for(int i=0;i<str.length()-3;i++)
			if(str.substring(i, i+3).equals("int"))
			{
				str = str.substring(i+3,str.length());
				break;
			}
		}
		if(str.contains("long"))
		{
			for(int i=0;i<str.length()-4;i++)
				if(str.substring(i, i+4).equals("long"))
				{
					str = str.substring(i+4, str.length());
					break;
				}
		}
		if(str.contains(";"))
		{
			String[] lines = str.split("[;]");
			for(int i=0;i<lines.length;i++)
				getArithmeticUsed(lines[i],lineNumber,type);
		}
		else if(str.contains(","))
		{
			String[] lines = str.split("[,]");
			for(int i=0;i<lines.length;i++)
				getArithmeticUsed(lines[i],lineNumber,type);
		}
		else if(str.contains("="))
		{
			for(int i=0;i<str.length();i++)
				if(str.charAt(i)=='=')
				{
					str = str.substring(i+1, str.length());
					getArithmeticUsed(str,lineNumber,type);
					break;
				}
		}
		else
		{
			getArithmeticIdentifiers(str,lineNumber,type);
		}
	}
	public static void getArithmeticIdentifiers(String str,int lineNumber,String type)
	{
		System.out.println(str);
		String str2 = "";
		for(int i=0;i<str.length();i++)
			if(str.charAt(i)!=' ')
				str2 = str2 + str.charAt(i);
		if(str2.length()>=1 && str2.charAt(0)=='('&&str2.charAt(str2.length()-1)==')')
		{
			String str3 = "";
			for(int i=1;i<str2.length()-1;i++)
                str3 = str3 + str2.charAt(i);
			getArithmeticIdentifiers(str3,lineNumber,type);
		}
		else if(str.contains("+")||str.contains("-")||str.contains("*")||str.contains("/")||str.contains("%"))
		{
		    boolean temp = true;
			ArrayList<Integer> positions = new ArrayList<Integer>();
			if(str.contains("+")&&temp)
			{
				for(int i=0;i<str.length()-1;i++)
				{
					if(str.charAt(i)=='+')
						positions.add(i);
				}
				int k = 0;
				for(int i=0;i<positions.size();i++)
				{
					String str3 = str.substring(0, positions.get(i));
					String str4 = str.substring(positions.get(i)+1, str.length());
					if(CheckBalanced(str3)&&CheckBalanced(str4))
					{
						getArithmeticIdentifiers(str3,lineNumber,type);
						getArithmeticIdentifiers(str4,lineNumber,type);
						temp = false;
						i = positions.size();
					}
					if(i<positions.size())
						k = positions.get(i);
				}
			}
            if(str.contains("-")&&temp)
            {
            	ArrayList<String> lines = new ArrayList<String>();
				for(int i=0;i<str.length()-1;i++)
				{
					if(str.charAt(i)=='-')
						positions.add(i);
				}
				int k = 0;
				for(int i=0;i<positions.size();i++)
				{
					String str3 = str.substring(0, positions.get(i));
					String str4 = str.substring(positions.get(i)+1, str.length());
					if(CheckBalanced(str3)&&CheckBalanced(str4))
					{
						getArithmeticIdentifiers(str3,lineNumber,type);
						getArithmeticIdentifiers(str4,lineNumber,type);
						i = positions.size();
						temp = false;
					}
					if(i<positions.size())
						k = positions.get(i);
				}
            }
            if(str.contains("*")&&temp)
            {
            	ArrayList<String> lines = new ArrayList<String>();
				for(int i=0;i<str.length()-1;i++)
				{
					if(str.charAt(i)=='*')
						positions.add(i);
				}
				int k = 0;
				for(int i=0;i<positions.size();i++)
				{
					String str3 = str.substring(0, positions.get(i));
					String str4 = str.substring(positions.get(i)+1, str.length());
					if(CheckBalanced(str3)&&CheckBalanced(str4))
					{
						getArithmeticIdentifiers(str3,lineNumber,type);
						getArithmeticIdentifiers(str4,lineNumber,type);
						i = positions.size();
						temp = false;
					}
					if(i<positions.size())
						k = positions.get(i);
				}
            }
            if(str.contains("/")&&temp)
            {
            	ArrayList<String> lines = new ArrayList<String>();
				for(int i=0;i<str.length()-1;i++)
				{
					if(str.charAt(i)=='/')
						positions.add(i);
				}
				int k = 0;
				for(int i=0;i<positions.size();i++)
				{
					String str3 = str.substring(0, positions.get(i));
					String str4 = str.substring(positions.get(i)+1, str.length());
					if(CheckBalanced(str3)&&CheckBalanced(str4))
					{
						getArithmeticIdentifiers(str3,lineNumber,type);
						getArithmeticIdentifiers(str4,lineNumber,type);
						i = positions.size();
						temp = false;
					}
					if(i<positions.size())
						k = positions.get(i);
				}
            }
            if(str.contains("%")&&temp)
            {
            	ArrayList<String> lines = new ArrayList<String>();
				for(int i=0;i<str.length()-1;i++)
				{
					if(str.charAt(i)=='%')
						positions.add(i);
				}
				int k = 0;
				for(int i=0;i<positions.size();i++)
				{
					String str3 = str.substring(0, positions.get(i));
					String str4 = str.substring(positions.get(i)+1, str.length());
					if(CheckBalanced(str3)&&CheckBalanced(str4))
					{
						getArithmeticIdentifiers(str3,lineNumber,type);
						getArithmeticIdentifiers(str4,lineNumber,type);
						i = positions.size();
						temp = false;
					}
					if(i<positions.size())
						k = positions.get(i);
				}
            }
		}
		else
		{
		//	System.out.println("YYY : " + str);
			boolean val = false;
			if(CheckInteger(str))
				val = true;
			Object[] V = Identifiers.toArray();
			for(int i=V.length-1;i>=0;i--)
			{
				if(((Variables)V[i]).name.equals(str))
				{
				//	System.out.println("A1");
					((Variables)V[i]).UsedLines.add(lineNumber);
					ArrayList<Integer> lineNoDefined = new ArrayList<Integer>();
					ArrayList<String> totalBlock = ((Variables)V[i]).CurrentBlockDefined;
					int temp = 1;
				//	System.out.println("EEE : " + totalBlock.size());
					while(!CurrentBlock.getLast().name.equals(totalBlock.get(totalBlock.size()-1))&&totalBlock.size()>=temp&&(totalBlock.get(totalBlock.size()-temp).equals("if")||totalBlock.get(totalBlock.size()-temp).equals("else")||totalBlock.get(totalBlock.size()-temp).equals("while")||totalBlock.get(totalBlock.size()-temp).contains("switch")))
					{
					//	System.out.println("##0");
						if(totalBlock.get(totalBlock.size()-temp).equals("switch"+SwitchCount) && CurrentBlock.getLast().name.equals("switch"+SwitchCount))
						{
							temp++;
							continue;
						}
					//	if((temp==1&&CurrentBlock.getLast().name.equals("else")&&totalBlock.get(totalBlock.size()-temp).equals("if")))
					//		System.out.println("##0.5");
						if(!(temp==1&&CurrentBlock.getLast().name.equals("else")&&totalBlock.get(totalBlock.size()-temp).equals("if")))
						{
					//	System.out.println("##1");
						if(totalBlock.size()>=temp)
    					{
					//		System.out.println("##2");
						int temp2 = ((Variables)V[i]).DefinedLines.get(totalBlock.size()-temp);
						String str3 = ((Variables)V[i]).Type.get(totalBlock.size()-temp);
						if(lineNumber!=temp2)
						{
							System.out.println("##"+lineNumber+"##"+temp2);
							if(str3.equals("Program")&&type.equals("Program"))
								OutputArray[lineNumber-1][temp2-1] = 1;
							else if(str3.equals("Database")&&type.equals("Database"))
								OutputArray[lineNumber-1][temp2-1] = 3;
							else
								OutputArray[lineNumber-1][temp2-1] = 2;
							val = true;
						}
						}
						}
						temp++;
					}
					if(!(temp==1&&CurrentBlock.getLast().name.equals("else")&&totalBlock.get(totalBlock.size()-temp).equals("if")))
					{
				//	System.out.println("##4");
					if(totalBlock.size()>=temp)
					{
					System.out.println("##5");
					int temp2 = ((Variables)V[i]).DefinedLines.get(totalBlock.size()-temp);
					//System.out.println("A4 : " + lineNumber + "  " + temp2);
					String str3 = ((Variables)V[i]).Type.get(totalBlock.size()-temp);
					if(lineNumber!=temp2 && temp2!=0)
					{
						System.out.println("##6");
						if((temp==1&&CurrentBlock.getLast().name.equals("else")&&totalBlock.get(totalBlock.size()-temp).equals("if")))
						{
							temp++;
							continue;
						}
					/*	if(totalBlock.get(totalBlock.size()-temp).equals("switch"+SwitchCount) && CurrentBlock.getLast().name.equals("switch"+SwitchCount))
						{
							System.out.println("AA##");
							temp++;
							continue;
						} */
						//System.out.println("A5");
						if(str3.equals("Program")&&type.equals("Program"))
							OutputArray[lineNumber-1][temp2-1] = 1;
						else if(str3.equals("Database")&&type.equals("Database"))
						{
							System.out.println("##7");
						//	OutputArray[lineNumber-1][temp2-1] = 3;
							System.out.println(((Variables)V[i]).DefinedLines.size()+"WW"+((Variables)V[i]).State.size());
							for(int p=0;p<((Variables)V[i]).DefinedLines.size();p++)
								System.out.print(((Variables)V[i]).DefinedLines.get(p)+"EWE");
							System.out.println();
							temp = 1;
							while(totalBlock.size()>=temp)
							{
								System.out.println("SWAD : " + temp);
								if((temp==1&&CurrentBlock.getLast().name.equals("else")&&totalBlock.get(totalBlock.size()-temp).equals("if")))
								{
									System.out.println("PQ : " + temp);
									temp++;
									continue;
								}
								if(totalBlock.get(totalBlock.size()-temp).equals("switch"+SwitchCount) && CurrentBlock.getLast().name.equals("switch"+SwitchCount))
								{
									System.out.println("PQ2 : " + temp);
									temp++;
									continue;
								}
								System.out.println("SWITCH : " + totalBlock.get(totalBlock.size()-temp));
								System.out.println("SWITCH2 : " + CurrentBlock.getLast().name + " " + temp);
								str3 = ((Variables)V[i]).Type.get(totalBlock.size()-temp);
								if(((Variables)V[i]).State.get(totalBlock.size()-temp)==1)
								{
									temp2 = ((Variables)V[i]).DefinedLines.get(totalBlock.size()-temp);
									System.out.println("HEEE : " + temp2);
									OutputArray[lineNumber-1][temp2-1] = 3;
									break;
								}
								temp2 = ((Variables)V[i]).DefinedLines.get(totalBlock.size()-temp);
								System.out.println("QQ : " + lineNumber);
								System.out.println("QQ2 : " + temp2);
								if(str3.equals("Database")&&type.equals("Database"))
									OutputArray[lineNumber-1][temp2-1] = 3;
								temp++;
							}
							System.out.println("$");
						}
						else
							OutputArray[lineNumber-1][temp2-1] = 2;
					}
					val = true;
					}
					}
					break;
				}
			}
			if(!val)
				 System.out.println("Identifier Not Recognised2 " + str + " in line Number + " +lineNumber);
		}
	}
	static boolean CheckBalanced(String str)
	{
		System.out.println("!!@@"+str);
		Deque temp = new ArrayDeque();
		for(int i=0;i<str.length();i++)
		{
			if(str.charAt(i)=='(')
				temp.push(1);
			else if(str.charAt(i)==')'&&!str.isEmpty())
			{
				if(!temp.isEmpty())
					temp.pop();
				else
					return false;
			}
			else if(str.isEmpty()&&str.charAt(i)==')')
				return false;
		}
		if(temp.isEmpty())
			return true;
		return false;

	}
	public static void getStringUsed(String str,int lineNumber,String type)
	{
		if(str.contains("String"))
		{
			for(int i=0;i<str.length()-6;i++)
			{
				if(str.substring(i,i+6).equals("String"))
				{
					str = str.substring(i+6,str.length());
					break;
				}
			}
		}
		String str2 = "";
		for(int i=0;i<str.length();i++)
			if(str.charAt(i)!='\\')
				str2 = str2 + str.charAt(i);
		str = str2;
		int pos = -1;
		if(str.contains(";"))
		{
			String[] lines = str.split("[;]");
			for(int i=0;i<lines.length;i++)
			{
				getStringUsed(lines[i],lineNumber,type);
			}
			return;
		}
		// DO SOMETHING FOR , CASE //
		else if(str.contains("="))
		{
			for(int i=0;i<str.length();i++)
				if(str.charAt(i)=='=')
				{
					pos = i;
					i = str.length();
				}
		}
		getStringUsed2(str.substring(pos+1,str.length()),lineNumber,type);
		return;
	}
	static void getStringUsed2(String str,int lineNumber,String type)
	{
		String str2 = "";
		for(int i=0;i<str.length();i++)
			if(str.charAt(i)!=' ')
				str2 = str2 + str.charAt(i);
		str = str2;
		if(str2.charAt(0)=='"'&&str2.charAt(str2.length()-1)=='"')
		{
			//System.out.println("----");
			return;
		}
		else if(str.contains("+"))
		{
			String[] lines = str.split("[+]");
			for(int i=0;i<lines.length;i++)
				getStringUsed2(lines[i],lineNumber,type);
			return;
		}
		else
		{
		    boolean val = false;
			if(str.contains(".substring")||str.contains(".toLowerCase")||str.contains(".toUpperCase")||str.contains(".trim"))
			{
				String[] lines = str.split("[.]", 2);
				Object[] V = Identifiers.toArray();
				for(int i=V.length-1;i>=0;i--)
                    if(lines[0].equals(((Variables)V[i]).name))
                    {
                    	((Variables)V[i]).UsedLines.add(lineNumber);
                    	ArrayList<Integer> lineNoDefined = new ArrayList<Integer>();
    					ArrayList<String> totalBlock = ((Variables)V[i]).CurrentBlockDefined;
    					int temp = 1;
    					while(!CurrentBlock.getLast().equals(totalBlock.get(totalBlock.size()-1))&&totalBlock.size()>=temp&&(totalBlock.get(totalBlock.size()-temp).equals("if")||totalBlock.get(totalBlock.size()-temp).equals("else")||totalBlock.get(totalBlock.size()-temp).equals("while")||totalBlock.get(totalBlock.size()-temp).contains("switch")))
    					{
    						if(totalBlock.get(totalBlock.size()-temp).equals("switch"+SwitchCount) && CurrentBlock.getLast().name.equals("switch"+SwitchCount))
    						{
    							temp++;
    							continue;
    						}
    						if(!(temp==1&&CurrentBlock.getLast().name.equals("else")&&totalBlock.get(totalBlock.size()-temp).equals("if")))
    						{
    						if(totalBlock.size()>=temp)
        					{
    						int temp2 = ((Variables)V[i]).DefinedLines.get(totalBlock.size()-temp);
    						String str3 = ((Variables)V[i]).Type.get(totalBlock.size()-temp);
    						if(lineNumber!=temp2)
    						{
    							if(str3.equals("Program")&&type.equals("Program"))
    								OutputArray[lineNumber-1][temp2-1] = 1;
    							else if(str3.equals("Database")&&type.equals("Database"))
    								OutputArray[lineNumber-1][temp2-1] = 3;
    							else
    								OutputArray[lineNumber-1][temp2-1] = 2;
    							val = true;
    						}
    						}
    						}
    						temp++;
    					}
    					if(!(temp==1&&CurrentBlock.getLast().name.equals("else")&&totalBlock.get(totalBlock.size()-temp).equals("if")))
    					{


    					if(totalBlock.size()>=temp)
    					{
    					int temp2 = ((Variables)V[i]).DefinedLines.get(totalBlock.size()-temp);
    					//System.out.println("A4 : " + lineNumber + "  " + temp2);
    					String str3 = ((Variables)V[i]).Type.get(totalBlock.size()-temp);
    					if(lineNumber!=temp2 && temp2!=0)
    					{
    						if((temp==1&&CurrentBlock.getLast().name.equals("else")&&totalBlock.get(totalBlock.size()-temp).equals("if")))
    						{
    							temp++;
    							continue;
    						}
    						if(totalBlock.get(totalBlock.size()-temp).equals("switch"+SwitchCount) && CurrentBlock.getLast().name.equals("switch"+SwitchCount))
    						{
    							temp++;
    							continue;
    						}
    						//System.out.println("A5");
    						if(str3.equals("Program")&&type.equals("Program"))
    							OutputArray[lineNumber-1][temp2-1] = 1;
    						else if(str3.equals("Database")&&type.equals("Database"))
    						{
    							OutputArray[lineNumber-1][temp2-1] = 3;
    							System.out.println(((Variables)V[i]).DefinedLines.size()+"WW"+((Variables)V[i]).State.size());
    							for(int p=0;p<((Variables)V[i]).DefinedLines.size();p++)
    								System.out.print(((Variables)V[i]).DefinedLines.get(p)+"EWE");
    							System.out.println();
    							temp = 1;
    							while(totalBlock.size()>=temp)
    							{
    								if(!(temp==1&&CurrentBlock.getLast().name.equals("else")&&totalBlock.get(totalBlock.size()-temp).equals("if")))
    								{
    									temp++;
    									continue;
    								}
    								if(totalBlock.get(totalBlock.size()-temp).equals("switch"+SwitchCount) && CurrentBlock.getLast().name.equals("switch"+SwitchCount))
    								{
    									temp++;
    									continue;
    								}
    								System.out.println("SWITCH : " + totalBlock.get(totalBlock.size()-temp));
    								System.out.println("SWITCH2 : " + CurrentBlock.getLast().name + " " + temp);
    								str3 = ((Variables)V[i]).Type.get(totalBlock.size()-temp);
    								if(((Variables)V[i]).State.get(totalBlock.size()-temp)==1)
    								{
    									temp2 = ((Variables)V[i]).DefinedLines.get(totalBlock.size()-temp);
    									OutputArray[lineNumber-1][temp2-1] = 3;
    									break;
    								}
    								temp2 = ((Variables)V[i]).DefinedLines.get(totalBlock.size()-temp);
    								if(str3.equals("Database")&&type.equals("Database"))
    									OutputArray[lineNumber-1][temp2-1] = 3;
    								temp++;
    							}
    						}
    						else
    							OutputArray[lineNumber-1][temp2-1] = 2;
    					}
    					val = true;
    					}
    					}
                        break;
                    }
                if(!val)
                    System.out.println("Identifier Not Recognised " + str + " in line Number + " +lineNumber);
			}
		}
	}
	static void getBooleanIdentifiersSwitch(String str,int lineNumber,String type)
	{
		boolean val = false;
		if(str.contains("switch"))
			str = str.substring(6, str.length());
		if(str.charAt(0)=='('&&str.charAt(str.length()-1)==')')
		{
			str = str.substring(1, str.length()-1);
			Object[] V = Identifiers.toArray();
			for(int i=V.length-1;i>=0;i--)
			{
				if(((Variables)V[i]).name.equals(str))
				{
					((Variables)V[i]).UsedLines.add(lineNumber);
					ArrayList<Integer> lineNoDefined = new ArrayList<Integer>();
					ArrayList<String> totalBlock = ((Variables)V[i]).CurrentBlockDefined;
					int temp = 1;
					while(!CurrentBlock.getLast().equals(totalBlock.get(totalBlock.size()-1))&&totalBlock.size()>=temp&&(totalBlock.get(totalBlock.size()-temp).equals("if")||totalBlock.get(totalBlock.size()-temp).equals("else")||totalBlock.get(totalBlock.size()-temp).equals("while")||totalBlock.get(totalBlock.size()-temp).contains("switch")))
					{
						if(totalBlock.get(totalBlock.size()-temp).equals("switch"+SwitchCount) && CurrentBlock.getLast().name.equals("switch"+SwitchCount))
						{
							temp++;
							continue;
						}
						if(!(temp==1&&CurrentBlock.getLast().name.equals("else")&&totalBlock.get(totalBlock.size()-temp).equals("if")))
						{
						if(totalBlock.size()>=temp)
    					{
						int temp2 = ((Variables)V[i]).DefinedLines.get(totalBlock.size()-temp);
						String str3 = ((Variables)V[i]).Type.get(totalBlock.size()-temp);
						if(lineNumber!=temp2)
						{
							if(str3.equals("Program")&&type.equals("Program"))
								OutputArray[lineNumber-1][temp2-1] = 1;
							else if(str3.equals("Database")&&type.equals("Database"))
								OutputArray[lineNumber-1][temp2-1] = 3;
							else
								OutputArray[lineNumber-1][temp2-1] = 2;
							val = true;
						}
						}
						}
						temp++;
					}
					if(!(temp==1&&CurrentBlock.getLast().name.equals("else")&&totalBlock.get(totalBlock.size()-temp).equals("if")))
					{


					if(totalBlock.size()>=temp)
					{
					int temp2 = ((Variables)V[i]).DefinedLines.get(totalBlock.size()-temp);
					//System.out.println("A4 : " + lineNumber + "  " + temp2);
					String str3 = ((Variables)V[i]).Type.get(totalBlock.size()-temp);
					if(lineNumber!=temp2 && temp2!=0)
					{
						if((temp==1&&CurrentBlock.getLast().name.equals("else")&&totalBlock.get(totalBlock.size()-temp).equals("if")))
						{
							temp++;
							continue;
						}
						if(totalBlock.get(totalBlock.size()-temp).equals("switch"+SwitchCount) && CurrentBlock.getLast().name.equals("switch"+SwitchCount))
						{
							temp++;
							continue;
						}
						//System.out.println("A5");
						if(str3.equals("Program")&&type.equals("Program"))
							OutputArray[lineNumber-1][temp2-1] = 1;
						else if(str3.equals("Database")&&type.equals("Database"))
						{
							OutputArray[lineNumber-1][temp2-1] = 3;
							System.out.println(((Variables)V[i]).DefinedLines.size()+"WW"+((Variables)V[i]).State.size());
							for(int p=0;p<((Variables)V[i]).DefinedLines.size();p++)
								System.out.print(((Variables)V[i]).DefinedLines.get(p)+"EWE");
							System.out.println();
							temp = 1;
							while(totalBlock.size()>=temp)
							{
								if(!(temp==1&&CurrentBlock.getLast().name.equals("else")&&totalBlock.get(totalBlock.size()-temp).equals("if")))
								{
									temp++;
									continue;
								}
								if(totalBlock.get(totalBlock.size()-temp).equals("switch"+SwitchCount) && CurrentBlock.getLast().name.equals("switch"+SwitchCount))
								{
									temp++;
									continue;
								}
								System.out.println("SWITCH : " + totalBlock.get(totalBlock.size()-temp));
								System.out.println("SWITCH2 : " + CurrentBlock.getLast().name + " " + temp);
								str3 = ((Variables)V[i]).Type.get(totalBlock.size()-temp);
								if(((Variables)V[i]).State.get(totalBlock.size()-temp)==1)
								{
									temp2 = ((Variables)V[i]).DefinedLines.get(totalBlock.size()-temp);
									OutputArray[lineNumber-1][temp2-1] = 3;
									break;
								}
								temp2 = ((Variables)V[i]).DefinedLines.get(totalBlock.size()-temp);
								if(str3.equals("Database")&&type.equals("Database"))
									OutputArray[lineNumber-1][temp2-1] = 3;
								temp++;
							}
						}
						else
							OutputArray[lineNumber-1][temp2-1] = 2;
					}
					val = true;
					}
					}
					break;
				}
			}
			if(!val)
                System.out.println("Identifier Not Recognised " + str + " in line Number + " +lineNumber);
		}
	}
	static void getBooleanIdentifiers(String str,int lineNumber,String type)
	{
	   // System.out.println(str);
		if(str.contains("if"))
			str = str.substring(2, str.length());
		if(str.contains("elseif"))
			str = str.substring(6, str.length());
		if(str.contains("while"))
			str = str.substring(5, str.length());
		String str2 = str;
	//	System.out.println(str2+"  "+CurrentBlock.getLast().name);
		if(str2.charAt(0)=='('&&str2.charAt(str2.length()-1)==')')
		{
		//	System.out.println("a");
		    ArrayList<String> lines = CheckCorrespondence(str2);
		 //   System.out.println("LINE : " + lines.size());
		    for(int i=0;i<lines.size();i++)
            {
		  //  	System.out.println("LINE : " + lines.get(i));
                getBooleanIdentifiers(lines.get(i),lineNumber,str);
            }
		}
		else if(str.contains("&&")||str.contains("||")||str.contains("!"))
		{
		    boolean temp = true;
			ArrayList<Integer> positions = new ArrayList<Integer>();
			if(str.contains("&&")&&temp)
			{
				ArrayList<String> lines = new ArrayList<String>();
				for(int i=0;i<str.length()-1;i++)
				{
					if(str.charAt(i)=='&'&&str.charAt(i+1)=='&')
						positions.add(i);
				}
				int k = 0;
				for(int i=0;i<positions.size();i++)
				{
					String str3 = str.substring(0, positions.get(i));
					String str4 = str.substring(positions.get(i)+2, str.length());
					if(CheckBalanced(str3)&&CheckBalanced(str4))
					{
						getBooleanIdentifiers(str3,lineNumber,type);
						getBooleanIdentifiers(str4,lineNumber,type);
						temp = false;
						i = positions.size();
					}
					if(i<positions.size())
						k = positions.get(i);
				}
			}
            if(str.contains("||")&&temp)
            {
            	ArrayList<String> lines = new ArrayList<String>();
				for(int i=0;i<str.length()-1;i++)
				{
					if(str.charAt(i)=='|'&&str.charAt(i+1)=='|')
						positions.add(i);
				}
				int k = 0;
				for(int i=0;i<positions.size();i++)
				{
					String str3 = str.substring(0, positions.get(i));
					String str4 = str.substring(positions.get(i)+2, str.length());
					if(CheckBalanced(str3)&&CheckBalanced(str4))
					{
						getBooleanIdentifiers(str3,lineNumber,type);
						getBooleanIdentifiers(str4,lineNumber,type);
						i = positions.size();
						temp = false;
					}
					if(i<positions.size())
						k = positions.get(i);
				}
            }
            if(str.charAt(0)=='!'&&temp)
            {
            	ArrayList<String> lines = new ArrayList<String>();
				for(int i=0;i<str.length()-1;i++)
				{
					if(str.charAt(i)=='!')
						positions.add(i);
				}
				int k = 0;
				for(int i=0;i<positions.size();i++)
				{
					String str4 = str.substring(positions.get(i)+1, str.length());
					if(CheckBalanced(str4))
					{
						getBooleanIdentifiers(str4,lineNumber,type);
						i = positions.size();
					}
					if(i<positions.size())
						k = positions.get(i);
				}
            }
		}
		else
        {
			if(str.contains(".exists()"))
			//
            if(str.contains(".equals")||str.contains(".equalsIgnoreCase")||str.contains("isEmpty")||str.contains("isNumber"))
		    		getBooleanStringIdentifiers(str,lineNumber,type);
            else
                getBooleanIdentifiers2(str,lineNumber,type);
        }
	}
	static void getBooleanIdentifiers2(String str,int lineNumber,String type)
	{
		String[] lines;
		if(str.contains("<=")||str.contains(">="))
        {
			lines  = str.split("[=][=]|[!][=]|[<][=]|[>][=]");
			for(int i=0;i<lines.length;i++)
				getArithmeticIdentifiers(lines[i],lineNumber,type);
        }
		else if(str.contains("<")||str.contains(">"))
		{
			lines = str.split("[<]|[>]");
            for(int i=0;i<lines.length;i++)
            	getArithmeticIdentifiers(lines[i],lineNumber,type);
		}
		else if(str.contains("==")||str.contains("!="))
        {
			lines = str.split("[=][=]|[!][=]");
            for(int i=0;i<lines.length;i++)
            	getArithmeticIdentifiers(lines[i],lineNumber,type);
        }
		else
		{
			boolean val = false;
			Object[] V = Identifiers.toArray();
			for(int i=V.length-1;i>=0;i--)
			{
				if(((Variables)V[i]).name.equals(str))
				{
					((Variables)V[i]).UsedLines.add(lineNumber);
					ArrayList<Integer> lineNoDefined = new ArrayList<Integer>();
					ArrayList<String> totalBlock = ((Variables)V[i]).CurrentBlockDefined;
					int temp = 1;
					while(!CurrentBlock.getLast().equals(totalBlock.get(totalBlock.size()-1))&&totalBlock.size()>=temp&&(totalBlock.get(totalBlock.size()-temp).equals("if")||totalBlock.get(totalBlock.size()-temp).equals("else")||totalBlock.get(totalBlock.size()-temp).equals("while")||totalBlock.get(totalBlock.size()-temp).equals("switch")))
					{
						if(totalBlock.get(totalBlock.size()-temp).equals("switch"+SwitchCount) && CurrentBlock.getLast().name.equals("switch"+SwitchCount))
						{
							temp++;
							continue;
						}
						if(!(temp==1&&CurrentBlock.getLast().name.equals("else")&&totalBlock.get(totalBlock.size()-temp).equals("if")))
						{
						if(totalBlock.size()>=temp)
    					{
						int temp2 = ((Variables)V[i]).DefinedLines.get(totalBlock.size()-temp);
						String str3 = ((Variables)V[i]).Type.get(totalBlock.size()-temp);
						if(lineNumber!=temp2)
						{
							if(str3.equals("Program")&&type.equals("Program"))
								OutputArray[lineNumber-1][temp2-1] = 1;
							else if(str3.equals("Database")&&type.equals("Database"))
								OutputArray[lineNumber-1][temp2-1] = 3;
							else
								OutputArray[lineNumber-1][temp2-1] = 2;
							val = true;
						}
						}
						}
						temp++;
					}
					if(!(temp==1&&CurrentBlock.getLast().name.equals("else")&&totalBlock.get(totalBlock.size()-temp).equals("if")))
					{
					if(totalBlock.size()>=temp)
					{
					int temp2 = ((Variables)V[i]).DefinedLines.get(totalBlock.size()-temp);
					String str3 = ((Variables)V[i]).Type.get(totalBlock.size()-temp);
					if(lineNumber!=temp2)
					{
					if(str3.equals("Program")&&type.equals("Program"))
						OutputArray[lineNumber-1][temp2-1] = 1;
					else if(str3.equals("Database")&&type.equals("Database"))
					{
						OutputArray[lineNumber-1][temp2-1] = 3;
						temp = 1;
						while(totalBlock.size()>=temp)
						{
							str3 = ((Variables)V[i]).Type.get(totalBlock.size()-temp);
							if(((Variables)V[i]).State.get(totalBlock.size()-temp)==1)
							{
								temp2 = ((Variables)V[i]).DefinedLines.get(totalBlock.size()-temp);
								OutputArray[lineNumber-1][temp2-1] = 3;
								break;
							}

							temp2 = ((Variables)V[i]).DefinedLines.get(totalBlock.size()-temp);
							if(str3.equals("Database")&&type.equals("Database"))
								OutputArray[lineNumber-1][temp2-1] = 3;
							temp++;
						}
					}
					else
						OutputArray[lineNumber-1][temp2-1] = 2;
					}
					val = true;
					}
					}
					break;
				}
			}
			if(!val)
				 System.out.println("Identifier Not Recognised " + str + " in line Number + " +lineNumber);
		}
	}
	static void getBooleanStringIdentifiers(String str,int lineNumber,String type)
	{
		if(str.contains(".equals")||str.contains(".equalsIgnoreCase")||str.contains("isEmpty")||str.contains("isNumber"))
		{
			if(str.contains(".equalsIgnoreCase"))
			{
				String[] lines = str.split("[.][e][q][u][a][l][s][I][g][n][o][r][e][C][a][s][e][(]",2);
				String[] lines2 = lines[1].split("[)]",2);
				getStringIdentifiers(lines[0],lineNumber,type);
				getStringIdentifiers(lines2[0],lineNumber,type);
			}
			else if(str.contains("isEmpty"))
			{
				String[] lines = str.split("[(]",2);
				String[] lines2 = lines[1].split("[)]",2);
				getStringIdentifiers(lines2[0], lineNumber, type);
			}
			else if(str.contains("isNumber"))
			{
				String[] lines = str.split("[(]",2);
				String[] lines2 = lines[1].split("[)]",2);
				getStringIdentifiers(lines2[0], lineNumber, type);
			}
			else
			{
				if(str.contains(".equals"))
				{
					String[] lines = str.split("[.][e][q][u][a][l][s][(]",2);
					String[] lines2 = lines[1].split("[)]",2);
					getStringIdentifiers(lines[0],lineNumber,type);
					getStringIdentifiers(lines2[0],lineNumber,type);
				}
			}
		}

	}
	static ArrayList<String> CheckCorrespondence(String str)
	{
		boolean value = false,value2 = false;
		String temp = "";
		ArrayList<String> lines = new ArrayList<String>();
		for(int i=0;i<str.length();i++)
		{
			if(str.charAt(i)=='(')
			{
                if(i!=0&&!(str.charAt(i-1)=='&'||str.charAt(i-1)=='|'||str.charAt(i-1)=='!'))
                {
				value = true;
				value2 = true;
                temp = temp + str.charAt(i);
                stack.add(1);
                }
                else
                {
                   value = true;
                    if(!stack.isEmpty())
                        temp = temp + str.charAt(i);
                    stack.add(1);
                }
			}
			else if(str.charAt(i)==')')
			{
				stack.pop();
				if(!stack.isEmpty()||value2)
                {
                     temp = temp + str.charAt(i);
                     value2 = false;
                }
                else
                {
                    if(value2)
                    {
                        temp = temp + str.charAt(i);
                        value2 = false;
                    }
                }
			}
			else
			{
			    if(stack.isEmpty()&&(str.charAt(i)!='&'&&str.charAt(i)!='|'&&str.charAt(i)!='!'))
                    temp = temp + str.charAt(i);
                else
                {
                    if(!stack.isEmpty())
                      temp = temp + str.charAt(i);
                }
			}
			if(stack.isEmpty()&&i!=0&&str.charAt(i)==')')
			{
			    if(temp!="")
                    lines.add(temp);
				value = false;
				temp = "";
			}
		}
		return lines;
	}
	static boolean temp2 = true;
	public static void getStringIdentifiers(String str,int lineNumber,String type)
	{
	    int pos = -1;
		if(str.contains("String"))
		{
			for(int i=0;i<str.length()-6;i++)
			{
				if(str.substring(i,i+6).equals("String"))
				{
					str = str.substring(i+6,str.length());
					break;
				}
			}
		}
		if(str.contains(";"))
		{
			String[] lines = str.split("[;]");
			for(int i=0;i<lines.length;i++)
			{
				getStringIdentifiers(lines[i],lineNumber,type);
			}
		}
		// DO SOMETHING FOR , CASE //
		else if(str.contains("="))
		{
			for(int i=0;i<str.length();i++)
				if(str.charAt(i)=='=')
				{
					pos = i;
					i = str.length();
				}
		}
		getStringIdentifiers2(str.substring(pos+1,str.length()),lineNumber,type);
	}
	static void getStringIdentifiers2(String str,int lineNumber,String type)
	{
		//System.out.println("b");
		String str2 = "";
		for(int i=0;i<str.length();i++)
			if(str.charAt(i)!=' ')
				str2 = str2 + str.charAt(i);
		if(str2.charAt(0)=='"'&&str2.charAt(str2.length()-1)=='"')
		{
			return;
		}
		else if(str.contains("+"))
		{
			String[] lines = str.split("[+]");
			for(int i=0;i<lines.length;i++)
				getStringIdentifiers2(lines[i],lineNumber,type);
		}
		else
		{
		    boolean val = false;
			if(str.contains(".substring")||str.contains(".toLowerCase")||str.contains(".toUpperCase")||str.contains(".trim"))
			{
				String[] lines = str.split("[.]", 2);
				Object[] V = Identifiers.toArray();
				for(int i=V.length-1;i>=0;i--)
				{
					if(((Variables)V[i]).name.equals(lines[0]))
					{
						((Variables)V[i]).UsedLines.add(lineNumber);
						ArrayList<Integer> lineNoDefined = new ArrayList<Integer>();
						ArrayList<String> totalBlock = ((Variables)V[i]).CurrentBlockDefined;
						int temp = 1;
						while(!CurrentBlock.getLast().equals(totalBlock.get(totalBlock.size()-1))&&totalBlock.size()>=temp&&(totalBlock.get(totalBlock.size()-temp).equals("if")||totalBlock.get(totalBlock.size()-temp).equals("else")||totalBlock.get(totalBlock.size()-temp).equals("while")||totalBlock.get(totalBlock.size()-temp).contains("switch")))
						{
							if(totalBlock.get(totalBlock.size()-temp).equals("switch"+SwitchCount) && CurrentBlock.getLast().name.equals("switch"+SwitchCount))
							{
								temp++;
								continue;
							}
							if(!(temp==1&&CurrentBlock.getLast().name.equals("else")&&totalBlock.get(totalBlock.size()-temp).equals("if")))
							{
							if(totalBlock.size()>=temp)
	    					{
							int temp2 = ((Variables)V[i]).DefinedLines.get(totalBlock.size()-temp);
							String str3 = ((Variables)V[i]).Type.get(totalBlock.size()-temp);
							if(lineNumber!=temp2)
							{
								if(str3.equals("Program")&&type.equals("Program"))
									OutputArray[lineNumber-1][temp2-1] = 1;
								else if(str3.equals("Database")&&type.equals("Database"))
									OutputArray[lineNumber-1][temp2-1] = 3;
								else
									OutputArray[lineNumber-1][temp2-1] = 2;
								val = true;
							}
							}
							}
							temp++;
						}
						if(!(temp==1&&CurrentBlock.getLast().name.equals("else")&&totalBlock.get(totalBlock.size()-temp).equals("if")))
						{


						if(totalBlock.size()>=temp)
						{
						int temp2 = ((Variables)V[i]).DefinedLines.get(totalBlock.size()-temp);
						//System.out.println("A4 : " + lineNumber + "  " + temp2);
						String str3 = ((Variables)V[i]).Type.get(totalBlock.size()-temp);
						if(lineNumber!=temp2 && temp2!=0)
						{
							if((temp==1&&CurrentBlock.getLast().name.equals("else")&&totalBlock.get(totalBlock.size()-temp).equals("if")))
							{
								temp++;
								continue;
							}
							if(totalBlock.get(totalBlock.size()-temp).equals("switch"+SwitchCount) && CurrentBlock.getLast().name.equals("switch"+SwitchCount))
							{
								temp++;
								continue;
							}
							//System.out.println("A5");
							if(str3.equals("Program")&&type.equals("Program"))
								OutputArray[lineNumber-1][temp2-1] = 1;
							else if(str3.equals("Database")&&type.equals("Database"))
							{
								OutputArray[lineNumber-1][temp2-1] = 3;
								System.out.println(((Variables)V[i]).DefinedLines.size()+"WW"+((Variables)V[i]).State.size());
								for(int p=0;p<((Variables)V[i]).DefinedLines.size();p++)
									System.out.print(((Variables)V[i]).DefinedLines.get(p)+"EWE");
								System.out.println();
								temp = 1;
								while(totalBlock.size()>=temp)
								{
									if(!(temp==1&&CurrentBlock.getLast().name.equals("else")&&totalBlock.get(totalBlock.size()-temp).equals("if")))
									{
										temp++;
										continue;
									}
									if(totalBlock.get(totalBlock.size()-temp).equals("switch"+SwitchCount) && CurrentBlock.getLast().name.equals("switch"+SwitchCount))
									{
										temp++;
										continue;
									}
									System.out.println("SWITCH : " + totalBlock.get(totalBlock.size()-temp));
									System.out.println("SWITCH2 : " + CurrentBlock.getLast().name + " " + temp);
									str3 = ((Variables)V[i]).Type.get(totalBlock.size()-temp);
									if(((Variables)V[i]).State.get(totalBlock.size()-temp)==1)
									{
										temp2 = ((Variables)V[i]).DefinedLines.get(totalBlock.size()-temp);
										OutputArray[lineNumber-1][temp2-1] = 3;
										break;
									}
									temp2 = ((Variables)V[i]).DefinedLines.get(totalBlock.size()-temp);
									if(str3.equals("Database")&&type.equals("Database"))
										OutputArray[lineNumber-1][temp2-1] = 3;
									temp++;
								}
							}
							else
								OutputArray[lineNumber-1][temp2-1] = 2;
						}
						val = true;
						}
						}
						break;
					}
				}
				if(!val)
                    System.out.println("Identifier Not Recognised " + lines[0] + " in line Number " +lineNumber);

				//Identifiers.search(lines[0]);
			}
			else
			{
			    val = false;
			    Object[] V = Identifiers.toArray();
				for(int i=V.length-1;i>=0;i--)
				{
					//System.out.println("2"+((Variables)V[i]).name);
					if(((Variables)V[i]).name.equals(str))
					{
						((Variables)V[i]).UsedLines.add(lineNumber);
						ArrayList<Integer> lineNoDefined = new ArrayList<Integer>();
						ArrayList<String> totalBlock = ((Variables)V[i]).CurrentBlockDefined;
						int temp = 1;
						while(!CurrentBlock.getLast().equals(totalBlock.get(totalBlock.size()-1))&&totalBlock.size()>=temp&&(totalBlock.get(totalBlock.size()-temp).equals("if")||totalBlock.get(totalBlock.size()-temp).equals("else")||totalBlock.get(totalBlock.size()-temp).equals("while")||totalBlock.get(totalBlock.size()-temp).contains("switch")))
						{
							if(totalBlock.get(totalBlock.size()-temp).equals("switch"+SwitchCount) && CurrentBlock.getLast().name.equals("switch"+SwitchCount))
							{
								temp++;
								continue;
							}
							if(!(temp==1&&CurrentBlock.getLast().name.equals("else")&&totalBlock.get(totalBlock.size()-temp).equals("if")))
							{
							if(totalBlock.size()>=temp)
	    					{
							int temp2 = ((Variables)V[i]).DefinedLines.get(totalBlock.size()-temp);
							String str3 = ((Variables)V[i]).Type.get(totalBlock.size()-temp);
							if(lineNumber!=temp2)
							{
								if(str3.equals("Program")&&type.equals("Program"))
									OutputArray[lineNumber-1][temp2-1] = 1;
								else if(str3.equals("Database")&&type.equals("Database"))
									OutputArray[lineNumber-1][temp2-1] = 3;
								else
									OutputArray[lineNumber-1][temp2-1] = 2;
								val = true;
							}
							}
							}
							temp++;
						}
						if(!(temp==1&&CurrentBlock.getLast().name.equals("else")&&totalBlock.get(totalBlock.size()-temp).equals("if")))
						{


						if(totalBlock.size()>=temp)
						{
						int temp2 = ((Variables)V[i]).DefinedLines.get(totalBlock.size()-temp);
						//System.out.println("A4 : " + lineNumber + "  " + temp2);
						String str3 = ((Variables)V[i]).Type.get(totalBlock.size()-temp);
						if(lineNumber!=temp2 && temp2!=0)
						{
							if((temp==1&&CurrentBlock.getLast().name.equals("else")&&totalBlock.get(totalBlock.size()-temp).equals("if")))
							{
								temp++;
								continue;
							}
							if(totalBlock.get(totalBlock.size()-temp).equals("switch"+SwitchCount) && CurrentBlock.getLast().name.equals("switch"+SwitchCount))
							{
								temp++;
								continue;
							}
							//System.out.println("A5");
							if(str3.equals("Program")&&type.equals("Program"))
								OutputArray[lineNumber-1][temp2-1] = 1;
							else if(str3.equals("Database")&&type.equals("Database"))
							{
								OutputArray[lineNumber-1][temp2-1] = 3;
								System.out.println(((Variables)V[i]).DefinedLines.size()+"WW"+((Variables)V[i]).State.size());
								for(int p=0;p<((Variables)V[i]).DefinedLines.size();p++)
									System.out.print(((Variables)V[i]).DefinedLines.get(p)+"EWE");
								System.out.println();
								temp = 1;
								while(totalBlock.size()>=temp)
								{
									if(!(temp==1&&CurrentBlock.getLast().name.equals("else")&&totalBlock.get(totalBlock.size()-temp).equals("if")))
									{
										temp++;
										continue;
									}
									if(totalBlock.get(totalBlock.size()-temp).equals("switch"+SwitchCount) && CurrentBlock.getLast().name.equals("switch"+SwitchCount))
									{
										temp++;
										continue;
									}
									System.out.println("SWITCH : " + totalBlock.get(totalBlock.size()-temp));
									System.out.println("SWITCH2 : " + CurrentBlock.getLast().name + " " + temp);
									str3 = ((Variables)V[i]).Type.get(totalBlock.size()-temp);
									if(((Variables)V[i]).State.get(totalBlock.size()-temp)==1)
									{
										temp2 = ((Variables)V[i]).DefinedLines.get(totalBlock.size()-temp);
										OutputArray[lineNumber-1][temp2-1] = 3;
										break;
									}
									temp2 = ((Variables)V[i]).DefinedLines.get(totalBlock.size()-temp);
									if(str3.equals("Database")&&type.equals("Database"))
										OutputArray[lineNumber-1][temp2-1] = 3;
									temp++;
								}
							}
							else
								OutputArray[lineNumber-1][temp2-1] = 2;
						}
						val = true;
						}
						}
						break;
					}
				}
                if(!val)
                    System.out.println("Identifier Not Recognised " + str + " in line Number  " +lineNumber);
			}
		}
	}
	static boolean CheckInteger(String str)
	{
		try
		{
			int a = Integer.parseInt(str);
		}
		catch(Exception e)
		{
			return false;
		}
			return true;
	}
	static void print()
	{
		System.out.println("TOTAL LINES : " + OutputArray.length);
		int CD=0,PPD=0,PDD=0,DDD=0,TD=0;
		for(int i=0;i<ArrayControl.size();i++)
		{
			OutputArray[i][ArrayControl.get(i)] = 4;
			CD++;
		}
		for(int i=0;i<OutputArray.length;i++)
		{
			for(int j=0;j<OutputArray[0].length;j++)
			{
				if(OutputArray[i][j]!=0)
				{
					if(OutputArray[i][j]==1)
						PPD++;
					else if(OutputArray[i][j]==2)
						PDD++;
					else if(OutputArray[i][j]==3)
						DDD++;
					int a = i+1;
					int b = j+1;
					System.out.println(a+"    "+b+"    "+OutputArray[i][j]);
					str = str + a + "   "+b +"    "+OutputArray[i][j]+"\n";
					TD++;
				}
			}
		}
		System.out.println("Program-Program : " + PPD);
		str = str + "Program-Program : " + PPD + "\n";
		System.out.println("Database-Database : " + DDD);
		str = str + "Database-Database : " + DDD + "\n";
		System.out.println("Program-Database : " + PDD);
		str = str + "Program-Database : " + PDD + "\n";
		System.out.println("Total Dependencies : " + TD);
		str = str  + "Total Dependencies : " + TD + "\n";
		str = str + "Initial : " + ts1 + "\n";
		final java.sql.Timestamp ts2 = new java.sql.Timestamp(System.currentTimeMillis());
		str = str + "Final Time : " + ts2 +"\n";
	}
	public static void checkDatabase(String line,int lineno,String Type)
	{
		//System.out.println(lineno);
		//System.out.println(lineno+"edfrrrr");
		String tableName = "";
	    if(line.length()!=0)
        {
		if(line.toLowerCase().contains("select")||line.toLowerCase().contains("insert")||line.toLowerCase().contains("update")||line.toLowerCase().contains("delete"))
		{
			if(line.toLowerCase().contains("select"))
			{
				String[] lines1 = line.split("[S|s][E|e][L|l][E|e][C|c][T|t]",2);
		        String[] lines2 = lines1[1].split("[F|f][R|r][O|o][M|m]",2);
		        String[] lines3;
		        if(lines1[1].toLowerCase().contains("where"))
		        {
                lines3 = lines2[1].split("[W|w][H|h][E|e][R|r][E|e]",2);
                String[] lines4 = lines3[1].split("[;]",2);
                if(lines4[0].contains("<")||lines4[0].contains(">")||lines4[0].contains("==")||lines4[0].contains("isEmpty")||lines4[0].contains("isNumber")||lines4[0].contains("equal"))
                	getBooleanIdentifiers2(lines4[0], lineno, "Database");
                else
                	getStringIdentifiers2(lines4[0], lineno,"Database");
		        }
		        else
		        {
		        	lines3 = lines2[1].split(";",2);
		        }
                System.out.println("INI : " + lines2[0]);
                if(lines2[0].toLowerCase().contains("count")||lines2[0].toLowerCase().contains("max")||lines2[0].toLowerCase().contains("min")||lines2[0].toLowerCase().contains("avg"))
                {
                	String[] lin = lines2[0].split("[(]",2);
                	String[] lin2 = lin[1].split("[)]",2);
                	lines2[0] = lin2[0];
                }
                System.out.println("FIN : " + lines2[0]);
                tableName = lines3[0];
                String[] ntj = null;
                if(tableName.toLowerCase().contains("naturaljoin")||tableName.toLowerCase().contains("innerjoin"))
                {
                	if(tableName.contains("naturaljoin"))
                		ntj = tableName.split("naturaljoin");
                	else
                		ntj = tableName.split("NATURALJOIN");
                	if(tableName.toLowerCase().contains("innerjoin"))
                	{
                	if(tableName.contains("innerjoin"))
                		ntj = tableName.split("innerjoin");
                	else
                		ntj = tableName.split("INNERJOIN");
                	}
                	for(int i=0;i<ntj.length;i++)
                		System.out.println(" JOIN : " + ntj[i]);
                }
                else
                {
                	ntj = new String[1];
                	ntj[0] = tableName;
                }
                if(lines2[0].equals("*"))
                {
                	for(int p=0;p<ntj.length;p++)
                	{
                	int tableIndex = -1;
					for(int i=0;i<Tables.size();i++)
						if(Tables.get(i).equals(ntj[p]))
							tableIndex = i;
					if(tableIndex==-1)
					{
						System.out.println("Problem : "+ntj[p]);
						return;
					}
					tableIndex = 0;
					String[] tableAttributes = TableVariable[tableIndex];
					Object[] V = Identifiers.toArray();
					for(int j=0;j<tableAttributes.length;j++)
					{
						getBooleanIdentifiers2(TableVariable[tableIndex][j]+">=1", lineno, "Database");
					}
                	}
                }
                if(!lines2[0].equals(""))
                {
                	String[] lines12 = lines2[0].split("[,]");
                	for(int i=0;i<lines12.length;i++)
                	{
                		if(lines12[i]!=null)
                		{
                			System.out.println("SELECT : " + lines12[i]);
                			getBooleanIdentifiers2(lines12[i]+">=1", lineno, "Database");
                		}
                	}
                }
			}
			else if(line.toLowerCase().contains("insertinto"))
			{
				String[] lines1 = line.split("[I|i][N|n][S|s][E|e][R|r][T|t][I|i][N|n][T|t][O|o]", 2);
				String[] lines2 = lines1[1].split("[V|v][A|a][L|l][U|u][E|e][S|s]",2);
				if(lines2[0].contains("("))
				{
					System.out.println(lines2[0]);
					String[] lines3 = lines2[0].split("[(]",2);
				//	String[] lines4 = lines3[1].split("[)]",2);
				//	databaseCheck3(lines3[0],lineno);
					tableName = lines3[0];
					int tableIndex = -1;
					for(int i=0;i<Tables.size();i++)
						if(Tables.get(i).equals(tableName))
							tableIndex = i;
					tableIndex = 0;
					String[] tableAttributes = TableVariable[tableIndex];
					Object[] V = Identifiers.toArray();
					for(int j=0;j<tableAttributes.length;j++)
					{
					for(int i=V.length-1;i>=0;i--)
					{
						if(TableVariable[tableIndex][j].equals(((Variables)V[i]).name))
						{
							((Variables)V[i]).DefinedLines.add(lineno);
							((Variables)V[i]).Type.add("Database");
							((Variables)V[i]).CurrentBlockDefined.add(CurrentBlock.getLast().name);
							((Variables)V[i]).State.add(0);
							break;
						}
					}
					}
				}
				else
					tableName = lines2[0];
			}
			else if(line.toLowerCase().contains("deletefrom"))
			{
				String[] lines1 = line.split("[D|d][E|e][L|l][E|e][T|t][E|e][F|f][R|r][O|o][M|m]", 2);
				String[] lines2 = lines1[1].split("[W|w][H|h][E|e][R|r][E|e]",2);
				if(lines1[1].toLowerCase().contains("where"))
				{
				String[] lines4 = lines2[1].split("[;]", 2);
				 if(lines4[0].contains("<")||lines4[0].contains(">")||lines4[0].contains("==")||lines4[0].contains("isEmpty")||lines4[0].contains("isNumber")||lines4[0].contains("equal"))
	                	getBooleanIdentifiers2(lines4[0], lineno, "Database");
	                else
	                	getStringIdentifiers2(lines4[0], lineno,"Database");
				}
				 tableName = lines2[0];
				 int tableIndex = -1;
					for(int i=0;i<Tables.size();i++)
						if(Tables.get(i).equals(tableName))
							tableIndex = i;
					tableIndex = 0;
					if(tableIndex==-1)
						System.out.println(tableName);
					String[] tableAttributes = new String[TableVariable[tableIndex].length];
					tableAttributes	= TableVariable[tableIndex];
					Object[] V = Identifiers.toArray();
					for(int j=0;j<tableAttributes.length;j++)
					{
					for(int i=V.length-1;i>=0;i--)
					{
						if(TableVariable[tableIndex][j].equals(((Variables)V[i]).name))
						{
							((Variables)V[i]).DefinedLines.add(lineno);
							((Variables)V[i]).Type.add("Database");
							((Variables)V[i]).CurrentBlockDefined.add(CurrentBlock.getLast().name);
							if(line.toLowerCase().contains("where"))
								((Variables)V[i]).State.add(0);
							else
								((Variables)V[i]).State.add(1);
							break;
						}
					}
					}
			}
			else if(line.toLowerCase().contains("update"))
			{
				System.out.println("Lsss : "+line);
				String[] lines1 = line.split("[U|u][P|p][D|d][A|a][T|t][E|e]",2);
				String[] lines2 = lines1[1].split("[S|s][E|e][T|t]",2);
				String[] lines3;
				if(lines2[1].toLowerCase().contains("where"))
				{
				lines3 = lines2[1].split("[w|W][H|h][E|e][R|r][E|e]",2);
				String[] lines4 = lines3[1].split("[;]",2);
				System.out.println("LINE$ : " + lines4[0]);
				 if(lines4[0].contains("<")||lines4[0].contains(">")||lines4[0].contains("==")||lines4[0].contains("isEmpty")||lines4[0].contains("isNumber")||lines4[0].contains("equal"))
	                	getBooleanIdentifiers2(lines4[0], lineno, "Database");
	                else
	                	getStringIdentifiers2(lines4[0], lineno,"Database");
				}
				else
				{
					lines3 = lines2[1].split(";",2);
				}
				 tableName = lines2[0];
				 if(line.toLowerCase().contains("where"))
					 CheckSetAttributes(lines3[0],lineno,"Database",0);
				 else
					 CheckSetAttributes(lines3[0],lineno,"Database",1);
			}

		}
        }
	}
	static void CheckSetAttributes(String line,int lineno,String Type,int st)
	{
		System.out.println("ENTEE"+lineno);
		if(line.contains(","))
		{
			String[] lines = line.split("[,]");
			for(int i=0;i<lines.length;i++)
				CheckSetAttributes(lines[i], lineno, Type,st);
		}
		else if(line.contains("="))
		{
			String[] lines = line.split("[=]",2);
			String type = "NoTypeFound";
			getArithmeticIdentifiers(lines[1], lineno, "Database");
			Object[] V = Identifiers.toArray();
			for(int i=V.length-1;i>=0;i--)
			{
				if(((Variables)V[i]).name.equals(lines[0]))
				{
					System.out.println("WSEDED" + lineno);
					type = ((Variables)V[i]).type;
					((Variables)V[i]).DefinedLines.add(lineno);
					((Variables)V[i]).Type.add("Database");
					((Variables)V[i]).CurrentBlockDefined.add(CurrentBlock.getLast().name);
					if(st==0)
						((Variables)V[i]).State.add(0);
					else
						((Variables)V[i]).State.add(1);
					break;
				}
			}
		}
	}
	static int CountArgs(String str)
	{
		int count = 0;
		for(int i=0;i<str.length();i++)
			if(str.charAt(i)==',')
				count++;
		return count+1;
	}
}
