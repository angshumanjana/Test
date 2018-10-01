package Polyhedra;

import java.util.Scanner;
import java.lang.*;
import java.util.ArrayList;


public class Dependency {

	Dependency(){

	}

  public static int cre(int[][] m){

  	m = checkM(m);

  	for(int h1=0;h1<m.length;h1++)
   	{
      for(int h2=0;h2<m[0].length;h2++)
      {
        System.out.print(m[h1][h2]+"   ");
      }
      System.out.println();
    }

   int k = 0, k1 = 0, k5 = 0, pm=0;
   int j, f, l, ax=0, k2=0;
   int g=0, tot1=0;

   int att = m[0].length-1;

   int [] v = new int [att];
   
   int [] v1 = new int [att];

   int [] n = new int [att];
 
   int att1 =(int) Math.pow(2, att);

   int[][] p = new int[att1 * att1][att];
   
   
   int[] px = new int[att];
 
   int[][] r= new int [2*att][att];       // rays.......................

 for(int h1=0; h1<r[0].length; h1++){
    r[2*h1][h1] = 1;
    r[2*h1+1][h1] = -1;
  }
  
  //...............................Algorithm....................................

      for(int h=0; h< att; h++)
        p[0][h]= 0;
     //int f2=0;

//try{
	
   
        for (int i=0; i<(m.length); i++ ) { //............this loop  for linear equations............
          //for(int az=0; az<m[i].length-1; az++){
			//  if(m[i][az]!= 0)
		  
			 // tot1++;
		 //}
         //pe = p;
         g=0;
         int f1, f2 =0 ;
            
            for(int z=0; z< p.length; z++){   //...........this for loop for vertices computation.....

             f = z;
             l = 0;
			 
   
                 for (j=0;j<m[i].length-1;j++){ //start first if of chernikova algo for vertices computation............

                      k = k + p[f][j] * m[i][j];
 
                      v[j]= p[f][j];
					  //tot = tot * m[i][j];
  
                    }

                 if( k >= m[i][j]  ) {
   
                    for (int t=0; t < v.length; t++)
 
                       p[f][t] = v[t];
        
                        l++;
         
  
                    }		
				 
     
    for(int x = 0; x < r.length; x++){   //start second if of chernikova algo for vertices computation............
     for (int w=0;w < r[x].length; w++){

      k1= k1 + m[i][w] * r[x][w];

      k2 = k2 + p[f][w] * m[i][w];
 
      n[w]= r[x][w];
  
     }
	
     if ((k2 > m[i][j] && k1 < 0) || (k2 < m[i][j] && k1 > 0)) {

        int c = (m[i][j]-k2)/k1 ;  
         g++;
        
         if(l>0){

             for(int x1 = p.length-1; x1>=f+1 ; x1--){
                for (int w1=0;w1 < p[x1].length; w1++){  

                 p[x1][w1] = p[x1-1][w1]; }}

        // f++;  
         z++;
           }
        
        for (int e = 0 ; e < n.length; e++)
        v[e]= v[e]+ (c * n[e]);


        for (int t=0; t < v.length; t++)
 
         p[f][t] = v[t];
        
        // f++;
 
      }
       
       k1=0; k2=0;
     }

	 
	 
	    
     
      k=0;

    //if(p[f][0]==0)
    // break;
    f1 =z+1;
	if( f1< p.length){
	for(int f3=0;f3<p[f1].length;f3++)
	f2=f2+p[f1][f3];}
    if(f2 == 0)
	 break;
    else
	 f2=0;	
     }
	 
	 // chernikova algo for vertices computation..................................................
	 int s1 =0, s5 =0, s6 = 0, s7, f4, f6=0, f7, f8=0, tot =0, kx=0, ky=0 , sx;
	 double [] v2 = new double [att];
	 double [] v3 = new double [att];
	 double [] v4 = new double [att];
	 double [] v5 = new double [att];
	 double [] v6 = new double [att];
	 double [] v7 = new double [att];
	 for (int s=0; s < p[1].length; s++ )
		  s1 = s1 + p[1][s];
	  if(s1 != 0){
		  
		for(int s2 =0; s2 < p.length-1; s2++){
			f4 =s2+1;
	        if( f4< p.length){
	         for(int f5=0;f5<p[f4].length;f5++)
	          f6=f6+p[f4][f5];}
            if(f6 == 0)
	        break;
            else
	         f6=0;
		  for(int s4 = 0; s4 < p[s2].length; s4++ ){
			  
		      v2[s4] = p[s2][s4];
			  s5 = s5 + m[i][s4] * p[s2][s4];
		      }
			 for(int s3 = s2+1; s3 < p.length; s3++ ){
				for( s7 = 0; s7 < p[s3].length; s7++ ){
				
				v3[s7]=p[s3][s7];
				s6 = s6 + m[i][s7] * p[s3][s7];
				}
				 int ac =0;
				if(s5> m[i][s7] && s6 < m[i][s7] ){
					
				double sum = (m[i][s7] - s6)/(double)(s5-s6); 
                				
				for(int s8 =0; s8<v2.length; s8++)
					v2[s8]= v2[s8]* sum;
				double sum1 = (m[i][s7] - s5)/(double)(s5-s6); 
      		
				for(int s8 =0; s8<v3.length; s8++)
					v3[s8]= v3[s8]* sum1;
				for(int s8 =0; s8<v3.length; s8++)
					v4[s8] = v2[s8] - v3[s8]; 
			    
				for(int x1 = p.length-1; x1>=s2+1 ; x1--){
                for (int w1=0;w1 < p[x1].length; w1++){  

                 p[x1][w1] = p[x1-1][w1]; }}
				 
				 
					   for (int t=0; t < v4.length; t++)
					    
					      p[s2][t] = (int) v4[t]; 
				  s2++;
		          s3++;
					
				}
					
				else if(s5< m[i][s7] && s6 > m[i][s7] ){
					
				double sum = (m[i][s7] - s5)/(double) (s6-s5); 	
				
				for(int s8 =0; s8<v2.length; s8++)
					v2[s8]= v2[s8]* sum;
				double sum1 = (m[i][s7] - s6)/(double) (s6-s5); 
                  			
				for(int s8 =0; s8<v3.length; s8++)
					v3[s8]= v3[s8]* sum1;
				for(int s8 =0; s8<v3.length; s8++)
					v4[s8] = v2[s8] - v3[s8]; 
					
			
				for(int x1 = p.length-1; x1>=s2+1 ; x1--){
                for (int w1=0;w1 < p[x1].length; w1++){  

                 p[x1][w1] = p[x1-1][w1]; }}
				 
				 
					   for (int t=0; t < v4.length; t++)
					    
					      p[s2][t] = (int) v4[t]; 
				  s2++;
		          s3++;
					
				} 
				
			s6=0;	
			f7 =s3+1;
	        if( f7< p.length){
	         for(int f5=0;f5<p[f7].length;f5++)
	           f8=f8+p[f7][f5];}
		   
            if(f8 == 0)
			 break;
            else
			 f8=0;
		    
			}
		  s5=0;	
		
		}  
	  }
		
		
		
	 
	for(int z1=0; z1<r.length; z1++){ //...........this for loop for rays computation......
			for ( int j1=0;j1<r[z1].length;j1++){ //start first if of chernikova algo for rays computation............

                      k5 = k5 + r[z1][j1] * m[i][j1];
 
                      v1[j1]= r[z1][j1];
  
                    }

                 if( k5 >= 0 ) {
  
                    }
					
				 else {
   
                    for (int t=0; t < v1.length; t++)
 
                       r[z1][t] = 0;
         
  
                    }
					k5 = 0;
                 } 
              tot1=0;
   }

	

//.............for emptyness checking............................................................
int kz = 0, jx;
int [] vx = new int [att];
for (int ix=0; ix<(m.length); ix++ ) { //............this loop  for linear equations............
          
        
         //int f1, f2 =0 ;
            
            for(int zx=0; zx< p.length; zx++){   //...........this for loop for vertices computation.....

             
			 
   
                 for (jx=0;jx<m[ix].length-1;jx++){ 

                      kz = kz + p[zx][jx] * m[ix][jx];
 
                      vx[jx]= p[zx][jx];
					  //tot = tot * m[i][j];
  
                    }

                 if( kz < m[ix][jx]  ) {
   
                    for (int t=0; t < vx.length; t++)
 
                       p[zx][t] = 0;
        
                        
         
  
                    }
				kz=0;	
			}
}			
					 
int xx = 0;					 
for (int i=0; i<(p.length); i++ ) {

    for (int b=0; b<p[i].length; b++){
		
		if(p[i][b] != 0){
			
		xx=1;
        break;		
			
		}
   
   }
     }
     return xx;

}

public static int[][] checkM(int[][] m){
	int att = m[0].length-1;
	int[][] m1 = new int[m.length][m[0].length];
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
		iniOrder.remove( (Integer) (order.get(i)) );
	}
	//System.out.println(iniOrder.toString());
	for(int i=0;i<diff;i++){
		m1[i+size] = m[iniOrder.get(i)].clone();
	}

	return m1;

}

}