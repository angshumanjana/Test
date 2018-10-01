package Tuning;

import java.util.*;
import java.lang.*;
import java.text.*;
import java.awt.event.*;
import java.io.*;

public class Tuning {

  public Tuning () {
   


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

   }