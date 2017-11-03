
import java.util.*;
import java.lang.Math;

/* should eventually be renamed into "De Bruijn Graph"
 * 
 */
public class DeBruijnGraph 
{
	private static char[] bases = {'A','C','G','T'};
	private ArrayList<ArrayList<Integer>> edges; // = new ArrayList<ArrayList<Integer>>();
	private ArrayList<String> vertices; // = new ArrayList<String>();
	private int order;
	private int num_vertices;
	private int num_edges;
	
	public DeBruijnGraph(int order)
	{
		edges = new ArrayList<ArrayList<Integer>>();
		vertices = new ArrayList<String>();
		this.order = order;
		this.num_edges = (int)Math.pow(4.0,(double)order+1.0);
		this.num_vertices = this.num_edges/4;
		InitVertices(0,"");
		FastInitEdges();
	}
	
	public void PrintVertices()
	{
		System.out.println(vertices);
	}
	
	public void PrintEdges()
	{
		System.out.println(edges);
	}
	
	private void InitVertices(int depth,String curr_string)
	{
		if(depth==order)
			this.vertices.add(curr_string);
		else
			for(int i=0;i<4;i++)
				InitVertices(depth+1,curr_string+String.valueOf(bases[i]));
	}
	
	/*private void InitEdges()
	{
		
		System.out.println(this.num_edges);
		// loop through all vertices:
		for(int i=0;i<this.num_vertices;i++)
		{
			String substr1 = vertices.get(i).substring(1,this.order);
			System.out.println(substr1);
			ArrayList<Integer> temp_indices = new ArrayList<Integer>();
			
			for(int j=0;j<this.num_vertices;j++)
			{
				String substr2 = vertices.get(j).substring(0,this.order-1);
				if(substr1.equals(substr2))
				{
					temp_indices.add(j);
				}
			}
			this.edges.add(temp_indices);	
		}	
	}*/
	
	private void FastInitEdges()
	{
		for(int i=0;i<this.num_vertices;i++)
		{
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp.add(4*i);
			temp.add(4*i+1);
			temp.add(4*i+2);
			temp.add(4*i+3);
			this.edges.add(temp);
		}
	}
	
	public static void main(String[] args)
	{
		ArrayList<Integer> list_of_ints = new ArrayList<Integer>();
		list_of_ints.add(12);
		list_of_ints.add(14);
		DeBruijnGraph tstcl = new DeBruijnGraph(3);
		tstcl.PrintVertices();
		tstcl.PrintEdges();

	}
}
