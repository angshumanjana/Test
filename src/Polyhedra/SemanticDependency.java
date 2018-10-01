package Polyhedra;

import java.util.ArrayList;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.lang.*;


public class SemanticDependency{

	public static int[][] main(){
    //long timeStamp1 = System.currentTimeMillis();
		int blockLine = -1;
    int[][] DependencyArray = null;
		try{
  			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("output.txt"));
        int totLines = (int)ois.readObject();
    		ArrayList<dependencyStructure> structList = (ArrayList<dependencyStructure>) ois.readObject();
   			dependencyStructure struct;
        DependencyArray = new int[totLines][totLines];
   			for(int cnt=0;cnt<structList.size();cnt++){
    		    struct = structList.get(cnt);
   				int line1 = struct.firstStmt;
   				int line2 = struct.secondStmt;
    			System.out.println("Line 1:"+line1);
   				System.out.println("Line 2:"+line2);
   				boolean intersection = true;
   				if(struct.defVar1!=null&&struct.useVar2!=null)
   				{
    				for(int zz=0;zz<struct.defVar1.size();zz++)
    			  		for(int zy=0;zy<struct.useVar2.size();zy++)
        					if(struct.defVar1.get(zz).equals(struct.useVar2.get(zy)))
        					{
          						intersection = false;
          						break;
       						}
      				//System.out.println(struct.defVar1.toString() + "  ****   " + struct.useVar2.toString());
   				}

   				int xx=0;
   				int yy=0;

   				if(!intersection){
   					if(line1!=blockLine){
   						int[][] m = (int[][])struct.defineUsed;
   						int[][] mdef1 = (int[][])struct.def1;
   						int[][] mdef2 = (int[][])struct.def2;
   						int[][] m2 = (int[][])struct.defineUsed2;

   						Dependency dep =  new Dependency();
   						if(m!=null)
   							xx = dep.cre(m);
   						if(m2!=null)
   							yy = dep.cre(m2);
   						boolean flag = false;
 	   					System.out.println("xx  "+xx+"  yy  "+yy);
   						if(xx==0&&yy==0)
    						flag = false;
 						else
    						flag = true;
    					if(flag&&mdef1!=null&&mdef2!=null){
    						if(overlap(mdef1,mdef2))
    						{
    							blockLine = line1;
    						}
    					}

   					}
   					else
            {
   						System.out.println("Block Line");
              xx = 1;
              yy = 1;
            }
   					if(xx==0&&yy==0)
            {
              System.out.println("No dependency exists");
              DependencyArray[line1][line2] = 3;
            }
            else
            {
              System.out.println("Dependency exists");
              DependencyArray[line1][line2] = 2;
            }
          }
          else
          {
            System.out.println("No Syntactic Dependency");
            DependencyArray[line1][line2] = 1;
          }

			}

		}
		catch(Exception e){
  			System.out.println(e);
		}
    return DependencyArray;
	}


	public static boolean overlap(int[][] arr1, int[][] arr2){

      int var = arr1[0].length-1;
      double[][] array = new double[arr1.length][var];
      double[] constants = new double[arr1.length];
      int cnt = 0;
      for(int i=0;i<arr1.length;i++)
      {
        for (int j=0;j<var;j++) {
          array[i][j] = arr1[i][j];
        }
        constants[i] = arr1[i][var];
      }
      
      double[] coeff = new double[var];

      for(int i=0;i<arr2.length;i++)
      {
        double[][] array1 = new double[array.length][var];
        double[] constants1 = new double[array.length];
        for(int p=0;p<array.length;p++)
        {
          for(int q=0;q<var;q++)
            array1[p][q] = array[p][q];
          constants1[p] = constants[p];
        }
        for(int j=0;j<var;j++)
        {
          coeff[j]=arr2[i][j];
        }
        int res = (int) SimplexMethod(array,constants,coeff);
        if(res>=arr2[i][var])
          cnt++;
      }
      if(cnt==arr2.length)
        return true;
    	return false;

	}

  public static double SimplexMethod(double[][] array,double[] constants,double[] coeff2)       //returns maximum value
  {
    int bigM = -99999;
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
    double[] result = new double[output[0].length];
    result = computeZ(constants,coeff,output, basic);
    int index = -1;
    double indexValue = 99999;
    for(int i=0;i<result.length;i++)
    {
      if(indexValue>result[i])
      {
        index = i;
        indexValue = result[i];
      }
    }
    int leavingIndex = -1;
    while(indexValue<0)
    {
      double min = 99999;
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
      if(leavingIndex==-1)
      {
        return 99999;
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

      for(int i=0;i<result.length;i++)
      {
        if(indexValue>result[i])
        {
          index = i;
          indexValue = result[i];
        }
  //      System.out.print(result[i]+"   ");
      }
  //    System.out.println();
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

}