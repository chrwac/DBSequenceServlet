
import java.util.*;
import java.lang.Math;

/* should eventually be renamed into "De Bruijn Graph"
 * 
 */
public class DeBruijnGraph 
{
	private static final char[] bases = {'A','C','G','T'};
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
	
	// loops through sequence and removes for every pair of successive sequences
	public void RemoveReverseComplementsOfSequence(String comp_sequence)
	{
		ArrayList<String> rev_complements = new ArrayList<String>();
		int str_length = comp_sequence.trim().length();
		for(int i=0;i<str_length-this.order;i++)
		{
			String curr_rev_comp = GetReverseComplementarySequence(comp_sequence.substring(i,i+this.order+1));
			DeleteEdge(curr_rev_comp.substring(0,this.order),curr_rev_comp.substring(1,this.order+1));
		}
	}
	
	public static String GetReverseComplementarySequence(String sequence)
	{
		return GetComplementarySequence(GetReverseSequence(sequence));
	}
	
	public static String GetReverseSequence(String sequence)
	{
		return new StringBuffer(sequence).reverse().toString();
	}
	
	public static String GetComplementarySequence(String sequence)
	{
		return sequence.replace("A","F").replace("T","A").replace("F","T").replace("C","F").replace("G","C").replace("F","G");
	}
	
	public void RemoveSequence(String sequence)
	{
		
		for(int i=0;i<vertices.size();i++)
		{
			String curr_string = vertices.get(i);
			if(curr_string.contains(sequence))
			{
				// Nachbarn lšschen und eingehende Sequenzen finden und aktuellen 
				int index_of_curr_vertex = i;
				edges.get(index_of_curr_vertex).clear();
				for(int j=0;j<4;j++)
				{
					String incident_string = bases[j] + curr_string.substring(1,curr_string.length()-1);
					DeleteEdge(incident_string,curr_string);
				}
			}
		}
	}
	
	public ArrayList<Integer> GetNeighbors(int index_vertex)
	{
		return this.edges.get(index_vertex);
	}
	
	// consider initial sequence (i.e. delete corresponding edges
	public void ConsiderInitialSequence(String init_sequence)
	{
		ArrayList<String> subsequences = new ArrayList<String>();
		
		int str_length = init_sequence.length();
		for(int i=0;i<str_length-this.order+1;i++)
		{
			System.out.println(init_sequence.substring(i,i+this.order));
			subsequences.add(init_sequence.substring(i,i+this.order).trim());
		}
		// loop over sequences and delete corresponding
		for(int i=0;i<subsequences.size()-1;i++)
		{
			
			int index_curr_sequ = IndexFromString(subsequences.get(i));
			
			// Delete the edges:
			
			int index1 = IndexFromString(subsequences.get(i).trim());
			int index2 = IndexFromString(subsequences.get(i+1).trim());
			
			DeleteEdge(subsequences.get(i).trim(),subsequences.get(i+1).trim());
		}
	}
	
	public String StringFromIndex(int index)
	{
		return(vertices.get(index));
	}
	
	public int IndexFromString(String sequ_a)
	{
		int index=0;
		int value=1;
		
		for(int i=sequ_a.length()-1;i>=0;i-=1)
		{
			char curr_char =sequ_a.charAt(i);
			int curr_index_value=0;
			
			switch(curr_char)
			{
				case 'A':
					curr_index_value=0;
					break;
				case 'C':
					curr_index_value=1;
					break;
				case 'G':
					curr_index_value=2;
					break;
				case 'T':
					curr_index_value=3;
					break;
			}
			index+=curr_index_value*value;
			value*=4;
		}
		return index;
	}
	
	public void DeleteEdge(String sequ_a,String sequ_b)
	{
		DeleteEdge(IndexFromString(sequ_a),IndexFromString(sequ_b));
	}
	// Delete an Edge of this Graph by passing two indices
	public void DeleteEdge(int index_first,int index_second)
	{
		// just for test-purposes, print out some stuff:
		//System.out.println(edges[index_first]);
		System.out.println(edges.get(index_first));
		//edges.get(index_first) = ArrayUtils.removeElement(edges.get(index_first),index_second);
		for(int j=0;j<edges.get(index_first).size();j++)
		{
			if(edges.get(index_first).get(j).equals(index_second))
			{
				System.out.println("found match");
				edges.get(index_first).remove(j);
			}
		}
		//edges.get(index_first).remove(index_second);
		
		
	}
	
	public void PrintVertices()
	{
		System.out.println(vertices);
	}
	
	public void PrintEdges()
	{
		System.out.println(edges);
	}
	
	
	// getters:
	public int GetOrder()
	{
		return this.order;
	}
	
	public int GetNumVertices()
	{
		return this.num_vertices;
	}
	
	public int GetNumEdges()
	{
		return this.num_edges;
	}
	
	public ArrayList<ArrayList<Integer>> GetEdges()
	{
		return this.edges;
	}
	
	public ArrayList<String> GetVertices()
	{
		return this.vertices;
	}
	
	
	private void InitVertices(int depth,String curr_string)
	{
		if(depth==order)
			this.vertices.add(curr_string);
		else
			for(int i=0;i<4;i++)
				InitVertices(depth+1,curr_string+String.valueOf(bases[i]));
	}


	
	private void InitEdges()
	{
		
		System.out.println(this.num_edges);
		// loop through all vertices:
		for(int i=0;i<this.num_vertices;i++)
		{
			String substr1 = vertices.get(i).substring(1,this.order);
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
	}
	
	private void FastInitEdges()
	{
		int curr_index=0;
		for(int i=0;i<this.num_vertices;i++)
		{
			ArrayList<Integer> temp = new ArrayList<Integer>();
			for(int j=0;j<4;j++)
				temp.add(curr_index+j);
			//temp.add(4*i+1);
			//temp.add(4*i+2);
			//temp.add(4*i+3);
			this.edges.add(temp);
			curr_index+=4;
			// set back to zero:
			if(curr_index>=this.num_vertices)
			{
				curr_index=0;
			}
				
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
