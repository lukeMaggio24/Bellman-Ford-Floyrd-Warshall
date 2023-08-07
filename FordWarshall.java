import java.util.*;
import java.io.*;
//Code written by Luke Maggio
//date finished: 7/19/23

//This class takes in input, then creates a graph, then does the Bellman-Ford and Floyd-Warshall algorithms
//Then both of these algorithms are printed out to a .txt file
public class FordWarshall
{

    private class identityMatrix
    {
        int weight = 0;
        int nodeNum = 0;
        int parent = 0;
        int edge = 0;

        identityMatrix prev = null;
        boolean flag = false;

        public identityMatrix()
        {

        }
    }

    //reads in the input file, creating and returning a scanner
    private static Scanner readFile(String filename)
    {
        try
        {
            Scanner in = new Scanner(new File(filename));
            return in;
        }
        catch(Exception e) //file does not exist
        {
            System.out.println("Could not find file " + e);
            return null;
        }        
    }

    //creates the graph
    public void createGraph(int numVertice, int numEdges, identityMatrix graph[][], Scanner in)
    {
        //used to keep create the graph
        int node = 0;
        int node2 = 0;
        int edgeWeight = 0;
        
        for(int j = 0; j < numVertice+1; j++)
            for(int k = 0; k < numVertice+1; k++)
                graph[j][k] = new identityMatrix();

        //creates the graph from the input file
        for(int i = 0; i < numEdges; i++)
        {
            node = in.nextInt();
            node2 = in.nextInt();
            edgeWeight = in.nextInt();

            //creates edge values and make edge = 1, thus present
            graph[node][node2].weight = edgeWeight;
            graph[node][node2].edge = 1;

            graph[node2][node].weight = edgeWeight;
            graph[node2][node].edge = 1;
        }
    
    }   
    
    //perfomrs Bellman-Ford algorithm
    public void bellAlgorithm(int sourceVertex, int numVertice, identityMatrix graph[][])
    {
        //sets all weights on nodes to infinity, except current node

        //sets all nodes weights to infinity
        for(int i = 1; i <= numVertice; i++)
        {
            if(i != sourceVertex)
            {
                graph[i][i].weight = Integer.MAX_VALUE;
                graph[i][i].nodeNum = i;
            }
            else
                graph[i][i].nodeNum = i;
        }
        
        //adds weights with all adjacanet vertices
        for(int l = 1; l <= numVertice-1; l++)
            for(int k = 1; k <= numVertice; k++)
                for(int j = 1; j <= numVertice; j++)
                {
                    if(graph[k][j].edge > 0 && graph[k][k].weight != Integer.MAX_VALUE) //an edge is present
                    {
                        if(graph[k][k].weight + graph[k][j].weight < graph[j][j].weight)
                        {
                            graph[j][j].weight = graph[k][k].weight + graph[k][j].weight; //adds the current vertex+edge weight
                            graph[j][j].parent = graph[k][k].nodeNum;  
                            graph[j][j].prev = graph[k][k];
                        }
                    }
                }
        

        //Bellman Ford checking for negative cycles
            for (int m = 1; m <= numVertice; m++)
            {
                if(graph[graph[m][m].parent][m].weight + graph[graph[m][m].parent][graph[m][m].parent].weight < graph[m][m].weight)
                {
                    System.out.println("Exception: Negative Weight-Cycle");
                    System.exit(0);
                }
            }

    }
    
    //Floyd-Warshall agorithm
    public void floydAlgorithm(int numVertice, identityMatrix graph2[][])
    {
        //sets all nodes weights and unused edges to infinity
        for(int m = 1; m <= numVertice; m++)
            for(int n = 1; n <=numVertice; n++)
                    if(graph2[m][n].weight == 0 && m != n)
                        graph2[m][n].weight = Integer.MAX_VALUE;
            

        //Floyds algorithm
        for(int k = 1; k <= numVertice; k++)
            for(int i = 1; i <= numVertice; i++)
                for(int j = 1; j <= numVertice; j++)
                    if((graph2[i][k].weight != Integer.MAX_VALUE && graph2[k][j].weight != Integer.MAX_VALUE))
                        if(graph2[i][j].weight > graph2[i][k].weight + graph2[k][j].weight)
                            graph2[i][j].weight = graph2[i][k].weight + graph2[k][j].weight;
        
    }

    //prints out the the result of dijkstra algorithm
    public static void printPath(int sourceVertex, int numVertice, identityMatrix graph[][], identityMatrix graph2[][])
    {
        try
        {
            FileWriter out = new FileWriter("cop3503-asn3-output-maggio-luke-bf.txt");
            FileWriter out2 = new FileWriter("cop3503-asn3-output-maggio-luke-fw.txt");
            
        
            out.write("" + numVertice + "\n");
            //Prints out the Bellman-Ford algorithm. CAN handle negatives. writes to bf
            for(int i = 1; i <= numVertice; i++)
            {
                if(i != sourceVertex)
                    out.write("" + i + " " + graph[i][i].weight + " " + graph[i][i].parent);
                else
                    out.write(i + " " + graph[sourceVertex][sourceVertex].weight + " " + "0"); //we just literally print -1, instead of the value from the current node
                    //since the current node value needs to stay 0, for correct calculations.

                if(i != numVertice)
                    out.write("\n");
            }
            out.close();


            //writes to fw
            out2.write("" + numVertice);
            out2.write("\n");
            for(int i = 1; i <= numVertice; i++)
            {
                for(int j = 1; j <= numVertice; j++)
                        out2.write("" + graph2[i][j].weight + " ");

                if(i != numVertice)
                    out2.write("\n");
            }    
            
            out2.close();
        }
        catch(Exception e)
        {
            System.out.println("ERROR WRITING TO FILE");
        }
    }

    //copys array
    public void copyArray(int numVertice, identityMatrix graph[][], identityMatrix graph2[][])
    {
        for(int l = 0; l < numVertice+1; l++)
            for(int k = 0; k < numVertice+1; k++)
                graph2[l][k] = new identityMatrix();
        
        for(int i = 0; i <= numVertice; i++)
            for(int j = 0; j <= numVertice; j++)
            {
                graph2[i][j].weight = graph[i][j].weight;
                graph2[i][j].edge = graph[i][j].edge;
            }
    }

    public static void main(String args[])
    {
        //go our input file and read in our first three values
        FordWarshall temp = new FordWarshall();

        Scanner in = readFile("cop3503-asn3-input.txt");
        if(in == null) //if a file not found exception was thrown earlier than we will exit the code
            return;

        int numVertice = in.nextInt(); //number of vertices
        int sourceVertex = in.nextInt(); //the starting vertices
        int numEdges = in.nextInt(); //the number of edges

        identityMatrix [][] graph = new identityMatrix[numVertice+1][numVertice+1]; //used to represent our graph
        identityMatrix [][] graph2 = new identityMatrix[numVertice+1][numVertice+1];
        temp.createGraph(numVertice, numEdges, graph, in);
        temp.copyArray(numVertice, graph, graph2);



        temp.bellAlgorithm(sourceVertex, numVertice, graph);
        temp.floydAlgorithm(numVertice, graph2);
        printPath(sourceVertex, numVertice, graph, graph2);
    }

}
