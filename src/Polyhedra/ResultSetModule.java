package Polyhedra;

import java.util.ArrayList;
import java.util.Deque;
public class ResultSetModule 
{
	public static void main(int[][] array,Deque<Variables> Identifiers)
	{
		ArrayList<Variables> Identifier = new ArrayList<Variables>(Identifiers);
		ArrayList<Variables> temp = new ArrayList<Variables>();
		for(int i=0;i<Identifiers.size();i++)
		{
			Variables identifier = Identifier.get(i);
			if(identifier.type.equals("ResultSet"))
			{
				System.out.println("Name : " + identifier.name);
				System.out.println("Size : " + identifier.DefinedLines.size());
				for(int k=0;k<identifier.DefinedLines.size();k++)
				{
					int lineNumberDefined = identifier.DefinedLines.get(k);
				System.out.println(lineNumberDefined);
				for(int j=0;j<Identifier.size();j++)
				{
					if(Identifier.get(j).UsedLines.contains(lineNumberDefined-1))
					{
						FindDependency(temp,array,Identifier,Identifier.get(j),lineNumberDefined-1);
					}
						
				}
				}
			}
		}
		for(int i=0;i<temp.size();i++)
			System.out.println("HHHH " + temp.get(i).name);
	}
	
	public static void FindDependency(ArrayList<Variables> temp,int[][] array,ArrayList<Variables> Identifier,Variables identifier,int lineNumberDefined)
	{
		int lastDefinedBeforeUse = -1;
		for(int i=0;i<identifier.DefinedLines.size();i++)
		{
			if(identifier.DefinedLines.get(i)<lineNumberDefined)
				lastDefinedBeforeUse = identifier.DefinedLines.get(i);
			else
				break;
		}
		temp.add(identifier);
		if(lastDefinedBeforeUse==-1)
			return;
		for(int i=0;i<Identifier.size();i++)
		{
			if(Identifier.get(i).UsedLines.contains(lastDefinedBeforeUse-1))
			{
				FindDependency(temp,array,Identifier,Identifier.get(i),lastDefinedBeforeUse-1);
			}
		}
	}
	
}
