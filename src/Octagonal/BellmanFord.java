package Octagonal;
public class BellmanFord 
{
    /*public static void main(int graph[][])
    {
        int graph[][] = {{0,-1,0,0,0,0,0,0},{15,0,0,0,0,0,0,0},{0,0,0,-1,0,0,0,3000},{0,0,50,0,0,0,0,0},{0,0,0,0,0,-1,0,0},{0,0,0,0,8,0,0,0},{0,0,0,0,0,0,0,-4950},{0,0,-5000,0,0,0,2999,0}};
        System.out.println(Method(graph));
    }*/
    public static int Method(int graph[][])
    {
        int distance[] = new int[graph.length];
        for(int l=0;l<distance.length;l++)
        {
        for(int i=0;i<distance.length;i++)
        {
            distance[i] = 0;
            if(i!=l)
                distance[i] = Integer.MAX_VALUE;
        }
        for(int i=1;i<distance.length;i++)
        {
            for(int j=0;j<graph.length;j++)
                for(int k=0;k<graph.length;k++)
                    if(graph[j][k]!=0)
                    {
                        int weight = graph[j][k];
                        if(distance[j]!=Integer.MAX_VALUE && distance[j]+weight<distance[k])
                        {
                            distance[k] = distance[j]+weight;
                        }
                    }
        }
        
        for(int j=0;j<graph.length;j++)
            for(int k=0;k<graph.length;k++)
                if(graph[j][k]!=0)
                {
                    int weight = graph[j][k];
                    if(distance[j]!=Integer.MAX_VALUE && distance[j]+weight<distance[k])
                    {
                        System.out.println("Graph contains negative weight cycle");
                        return 0;
                    }
                }
        }
        return 1;
        //for(int i=0;i<distance.length;i++)
       //     System.out.println(i+"  "+distance[i]);
    }
}