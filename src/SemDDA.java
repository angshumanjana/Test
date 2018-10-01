import java.applet.*;
import java.awt.*;
import java.util.*;
import java.lang.*;
import java.text.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SemDDA extends Applet implements ActionListener
{
	TextField name,pass;
	Button b1,b2,b3,b4,b11,b12,b13,b14;
	static File inputMain,databaseMain;
	JFrame frame;
	public SemDDA()
	{
		Label n=new Label("Input File:",Label.LEFT);
		Label p=new Label("Database File:",Label.LEFT);
		name=new TextField(20);
		pass=new TextField(20);
		b1=new Button("browse");
		b2=new Button("browse");
		b3 = new Button("Syntax");
		b4 = new Button("Semantics");
		add(n);
		add(name);
		add(b1);
		add(p);
		add(pass);
		add(b2);
		add(b3);
		add(b4);
		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
		b4.addActionListener(this);
	}
	public boolean action(Button b, String arg) {
		if (arg.equals("OPEN")) {
			System.out.println("OPEN CLICKED");

			int arrlen = 10000;
			byte[] infile = new byte[arrlen];
			Frame parent = new Frame("SemDDA");
			FileDialog fd = new FileDialog(parent, "Please choose a file:",
					FileDialog.LOAD);
			fd.show();
			String selectedItem = fd.getFile();
			if (selectedItem == null) {
				// no file selected
			} else {
				if(b.equals(b1))
				{
					name.setText(fd.getFile()+"");
					inputMain = new File( fd.getDirectory() + File.separator +fd.getFile());
				}
				else if(b.equals(b2))
				{
					pass.setText(fd.getFile()+"");
					databaseMain = new File( fd.getDirectory() + File.separator + fd.getFile());
					databaseMain = mergeMultipleTables(databaseMain);
				}
			}

		} else return false;
		return true;
	}
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==b1)
			action(b1,"OPEN");
		else if(e.getSource()==b2)
			action(b2,"OPEN");
		else if(e.getSource()==b3)
		{
			b3.setBackground(Color.cyan);
			long ts1 = System.currentTimeMillis();
			SyntaxBased.InputHandler iw = new SyntaxBased.InputHandler();
			int numberOfDatabaseStatements = Proformat.CountDatabaseStatements.main(inputMain);
			iw.main(databaseMain);
			SyntaxBased.DOPDG d = new SyntaxBased.DOPDG();
			d.main(inputMain, iw);
			int OutputArray[][] = d.main(inputMain,iw);
			//String str = DOPDG.main(inputMain,iw);
			//System.out.println(str);
			String str = "";
			int PPD = 0,DDD = 0,PDD = 0,TD = 0;
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
						//System.out.println(a+"    "+b+"    "+OutputArray[i][j]);
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
			str = str + "Initial : " + ts1 + "\n";
			long ts2 = System.currentTimeMillis();
			str +=  "Time : " + (ts2-ts1) + "ms"+"\n";
			str += "No of Database Statements : " + numberOfDatabaseStatements;
			System.out.println("DOPDG completed");
			SyntaxOut out = new SyntaxOut();
			TextArea tf = out.main(str);
			add(tf);
		}
		else if(e.getSource()==b4)
		{
			b4.setBackground(Color.cyan);
			frame = new JFrame("Select option");
			frame.setSize(300,300);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			JPanel panel = new JPanel();
			frame.add(panel);
			panel.setBackground(Color.WHITE);
			b11=new Button("Polyhedron");
			b12=new Button("Octagonal");
			b13=new Button("Interval Domain");
			b14 = new Button("Tuning");

			panel.add(b11);
			panel.add(b12);
			panel.add(b13);
			panel.add(b14);
			b11.addActionListener(this);
			b12.addActionListener(this);
			b13.addActionListener(this);
			b14.addActionListener(this);
			frame.setVisible(true);

		}
		else if(e.getSource()==b11)
		{
			frame.setVisible(false);
			frame.dispose();
			SyntaxBased.InputHandler iw = new SyntaxBased.InputHandler();
			iw.main(databaseMain);
			SyntaxBased.DOPDG d = new SyntaxBased.DOPDG();
			int OutputArray[][] = d.main(inputMain,iw);
			File outputMain = new File("output2.txt");
			System.out.println("HERE0");
			long ts1 = System.currentTimeMillis();
			String time1 = ts1+"";
			int array[][] = Polyhedra.SemanticAnalysis.main(outputMain);
			String str = "";
			int count = 0;
			HashMap<String,Integer> mp = new HashMap<String,Integer>();
			for(int i=0;i<array.length;i++)
				for(int j=0;j<array[0].length;j++)
					if(array[i][j]!=0)
					{
						if(array[i][j]==3)
						{
							count++;
							mp.put(i+" "+j,1);
						}
					}
			int count2 = 0;
			str = "Semantic Dependencies : " + "\n" ;
			for(int i=0;i<OutputArray.length;i++)
			{
				for(int j=0;j<OutputArray[0].length;j++)
				{
					if(OutputArray[i][j]==3 && mp.get((j+1)+" "+(i+1))==null)
					{
						str = str + i + "-->" + j + "\n";
						count2++;
					}
				}
			}
			str += "Total Semantic Dependencies : " + count2+"\n";
			str += "False Dependencies : "+count+"\n";
			for(int i=0;i<array.length;i++)
				for(int j=0;j<array[0].length;j++)
					if(array[i][j]!=0)
					{
						if(array[i][j]==3)
						{
							str += i + "-->" + j + "\n";
						}
					}
			SyntaxOut out = new SyntaxOut();
			System.out.println("Semantic Dependencies : "+ (count2-count));
			System.out.println("False Dependencies : "+count);
			long ts2 = System.currentTimeMillis();
			str += "Time : " + (ts2-ts1) + "ms";
			TextArea tf = out.main(str);
			add(tf);
		}
		else if(e.getSource()==b12)
		{
			frame.setVisible(false);
			frame.dispose();
			SyntaxBased.InputHandler iw = new SyntaxBased.InputHandler();
			iw.main(databaseMain);
			SyntaxBased.DOPDG d = new SyntaxBased.DOPDG();
			int OutputArray[][] = d.main(inputMain,iw);
			File outputMain = new File("output2.txt");
			System.out.println("HERE0");
			long ts1 = System.currentTimeMillis();
			String time1 = ts1+"";
			int array[][] = Octagonal.SemanticAnalysis.main(outputMain);
			if(array==null)
			{
				String str = "WARNING : Computation Result is not precise.\nSo,this is an over approximated result due to unsuitable domain. \n";
				int PPD = 0,DDD = 0,PDD = 0,TD = 0;
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
				System.out.println("Total Semantic Dependencies : " + DDD);
				str = str  + "Total Semantic Dependencies : " + DDD + "\n";
				str = str + "Initial : " + ts1 + "\n";
				long ts2 = System.currentTimeMillis();
				str +=  "Time : " + (ts2-ts1) + "ms";
				SyntaxOut out = new SyntaxOut();
				TextArea tf = out.main(str);
				add(tf);
			}
			else
			{
				String str = "";
				int count = 0;
				HashMap<String,Integer> mp = new HashMap<String,Integer>();
				for(int i=0;i<array.length;i++)
					for(int j=0;j<array[0].length;j++)
						if(array[i][j]!=0)
						{
							if(array[i][j]==3)
							{
								count++;
								mp.put(i+" "+j,1);
							}
						}
				int count2 = 0;
				str = "Semantic Dependencies : " + "\n" ;
				for(int i=0;i<OutputArray.length;i++)
				{
					for(int j=0;j<OutputArray[0].length;j++)
					{
						if(OutputArray[i][j]==3 && mp.get((j+1)+" "+(i+1))==null)
						{
							str = str + i + "-->" + j + "\n";
							count2++;
						}
					}
				}
				str += "Total Semantic Dependencies : " + count2+"\n";
				str += "False Dependencies : "+count+"\n";
				for(int i=0;i<array.length;i++)
					for(int j=0;j<array[0].length;j++)
						if(array[i][j]!=0)
						{
							if(array[i][j]==3)
							{
								str += i + "-->" + j + "\n";
							}
						}
				Octagonal.SyntaxOut out = new Octagonal.SyntaxOut();
				System.out.println("Semantic Dependencies : "+ (count2-count));
				System.out.println("False Dependencies : "+count);
				long ts2 = System.currentTimeMillis();
				str += "Time : " + (ts2-ts1) + "ms";
				TextArea tf = out.main(str);
				add(tf);
			}
		}
		else if(e.getSource()==b13)
		{
			frame.setVisible(false);
			frame.dispose();
			SyntaxBased.InputHandler iw = new SyntaxBased.InputHandler();
			iw.main(databaseMain);
			SyntaxBased.DOPDG d = new SyntaxBased.DOPDG();
			int OutputArray[][] = d.main(inputMain,iw);
			File outputMain = new File("output2.txt");
			long ts1 = System.currentTimeMillis();
			String time1 = ts1+"";
			int array[][] = Interval.SemanticAnalysis.main(outputMain);
			String str = "";
			int count = 0;
			HashMap<String,Integer> mp = new HashMap<String,Integer>();
			if(array==null)
				System.out.println("Not possible to compute in Interval Domain");
			if(array==null)
			{
				str = "WARNING : Computation Result is not precise.\nSo,this is an over approximated result due to unsuitable domain. \n";
				int PPD = 0,DDD = 0,PDD = 0,TD = 0;
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
				System.out.println("Total Semantic Dependencies : " + DDD);
				str = str  + "Total Semantic Dependencies : " + DDD + "\n";
				str = str + "Initial : " + ts1 + "\n";
				long ts2 = System.currentTimeMillis();
				str +=  "Time : " + (ts2-ts1) + "ms";
				SyntaxOut out = new SyntaxOut();
				TextArea tf = out.main(str);
				add(tf);
			}
			else
			{
				for(int i=0;i<array.length;i++)
					for(int j=0;j<array[0].length;j++)
						if(array[i][j]!=0)
						{
							if(array[i][j]==3)
							{
								count++;
								mp.put(i+" "+j,1);
							}
						}
				int count2 = 0;
				str = "Semantic Dependencies : " + "\n" ;
				for(int i=0;i<OutputArray.length;i++)
				{
					for(int j=0;j<OutputArray[0].length;j++)
					{
						if(OutputArray[i][j]==3 && mp.get((j+1)+" "+(i+1))==null)
						{
							str = str + i + "-->" + j + "\n";
							count2++;
						}
					}
				}
				str += "Total Semantic Dependencies : " + count2+"\n";
				str += "False Dependencies : "+count+"\n";
				for(int i=0;i<array.length;i++)
					for(int j=0;j<array[0].length;j++)
						if(array[i][j]!=0)
						{
							if(array[i][j]==3)
							{
								str += i + "-->" + j + "\n";
							}
						}
				Interval.SyntaxOut out = new Interval.SyntaxOut();
				System.out.println("Semantic Dependencies : "+ (count2-count));
				System.out.println("False Dependencies : "+count);
				long ts2 = System.currentTimeMillis();
				str += "Time : " + (ts2-ts1) + "ms";
				TextArea tf = out.main(str);
				add(tf);
			}
		}
		else if(e.getSource()==b14)
		{
			int opt = Method(inputMain);
			frame.setVisible(false);
			frame.dispose();
			SyntaxBased.InputHandler iw = new SyntaxBased.InputHandler();
			iw.main(databaseMain);
			SyntaxBased.DOPDG d = new SyntaxBased.DOPDG();
			int OutputArray[][] = d.main(inputMain,iw);
			File outputMain = new File("output2.txt");
			long ts1 = System.currentTimeMillis();
			String time1 = ts1+"";
			int array[][] = null;
			if(opt==1)
				array = Interval.SemanticAnalysis.main(outputMain);
			else if(opt==2)
				array = Octagonal.SemanticAnalysis.main(outputMain);
			else
				array = Polyhedra.SemanticAnalysis.main(outputMain);

			String str = "";
			int count = 0;
			HashMap<String,Integer> mp = new HashMap<String,Integer>();
			for(int i=0;i<array.length;i++)
				for(int j=0;j<array[0].length;j++)
					if(array[i][j]!=0)
					{
						if(array[i][j]==3)
						{
							count++;
							mp.put(i+" "+j,1);
						}
					}
			int count2 = 0;
			str = "Semantic Dependencies : " + "\n" ;
			for(int i=0;i<OutputArray.length;i++)
			{
				for(int j=0;j<OutputArray[0].length;j++)
				{
					if(OutputArray[i][j]==3 && mp.get((j+1)+" "+(i+1))==null)
					{
						str = str + i + "-->" + j + "\n";
						count2++;
					}
				}
			}
			str += "Total Semantic Dependencies : " + count2+"\n";
			str += "False Dependencies : "+count+"\n";
			for(int i=0;i<array.length;i++)
				for(int j=0;j<array[0].length;j++)
					if(array[i][j]!=0)
					{
						if(array[i][j]==3)
						{
							str += i + "-->" + j + "\n";
						}
					}

			if(opt==1)
			{
				System.out.println("Choosing Interval");
				Interval.SyntaxOut out = new Interval.SyntaxOut();
				System.out.println("False Dependencies : "+count);
				long ts2 = System.currentTimeMillis();
				str += "Time : " + (ts2-ts1) + "ms";
				TextArea tf = out.main(str);
				add(tf);
			}
			else if(opt==2)
			{
				System.out.println("Choosing Octagonal");
				Octagonal.SyntaxOut out = new Octagonal.SyntaxOut();
				System.out.println("False Dependencies : "+count);
				long ts2 = System.currentTimeMillis();
				str += "Time : " + (ts2-ts1) + "ms";
				TextArea tf = out.main(str);
				add(tf);
			}
			else
			{
				System.out.println("Choosing Polyhedron");
				SyntaxOut out = new SyntaxOut();
				System.out.println("False Dependencies : "+count);
				long ts2 = System.currentTimeMillis();
				str += "Time : " + (ts2-ts1) + "ms";
				TextArea tf = out.main(str);
				add(tf);
			}
		}
	}

	public static int Method(File inputMain)
	{
		int count = 0;
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputMain)));
			String line ="";
			while((line = br.readLine())!=null)
			{
				if(line.contains("where"))
				{
					String[] lines = line.split("where");
					String required1 = lines[1];
					if(required1.contains("="))
					{
						String[] required2 = lines[1].split("[=]",2);
						String[] lines2 = required2[0].split("[+-/*]");
						count = count>lines2.length?count:lines2.length;
						System.out.println("COUNT " + count +"    "+line);
						if(count>=2)
						{
							br.close();
							break;
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return count;
	}

	private File mergeMultipleTables(File databaseFile) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(databaseFile)));
			String line;
			int numberOfDatabaseTables = 0;
			while((line=br.readLine())!=null) {
				numberOfDatabaseTables++;
			}
			br.close();
			numberOfDatabaseTables = numberOfDatabaseTables/3;
			String[][] lines = new String[numberOfDatabaseTables][3];
			int i=0;
			BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(databaseFile)));
			while((line=br2.readLine())!=null) {
				lines[i/3][i%3] = line;
				i++;
			}
			br2.close();
			String finalTableDetails = mergeMultipleTables(lines);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("database.txt")));
			bw.write(finalTableDetails);
			bw.close();
			return new File("database.txt");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String mergeMultipleTables(String[][] lines) {
		final String tableName = "MainTable";
		String listOfVariables = "";
		String rangesOfVariables = "";
		for(int i=0;i<lines.length;i++) {
			listOfVariables += lines[i][1] + ",";
			rangesOfVariables += lines[i][2] + ",";
		}
		return tableName + "\n" + listOfVariables.substring(0,listOfVariables.length()-1) + "\n" + rangesOfVariables.substring(0, rangesOfVariables.length()-1);

	}

}