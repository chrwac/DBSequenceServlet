// class DeBruijnSequence
// takes an object of 
import java.util.*;
public class DeBruijnSequence 
{
	private boolean is_circular;
	private DeBruijnGraph db_graph;
	private int scaffold_length;
	private String scaffold_sequence;
	private boolean solution_found;
	private boolean end_reached; // was the end of the sequence reached, if so 
	private int initial_tuple_index;
	private long initial_time;
	
	
	public DeBruijnSequence(DeBruijnGraph dbg,boolean circular,int length,String initial_sequence,boolean rev_comp_free)	
	{
		//System.out.println("constructor of DeBruijnSequence");
		is_circular=circular;
		db_graph=dbg;
		scaffold_length=length;
		
		scaffold_sequence=""; // one should pass the "initial sequence" as a parameter eventually, so as to consider the initial part...
		
		// start with random index:
		
		Random rnd = new Random();
		int cti = rnd.nextInt(this.db_graph.GetNumVertices());
		
		
		//System.out.println("starting with initial vertex: ");
		//System.out.println(cti);
		solution_found=false; // was a 
		end_reached=false;
		initial_time=System.currentTimeMillis();
	//	System.out.println("initial time:");
		//System.out.println(initial_time);
		
		
		
		
		int initial_depth=0;
		// set the initial sequence and the starting index (not randomly, but by starting sequence
		
		// is the sequence empty ?
		if(initial_sequence.isEmpty())
		{
			// set the current tuple index as the initial (and thus final) tuple index
			//System.out.println("No starting sequence was passed:");
			
	/*		System.out.println("The first tuple index is: ");
			System.out.println(cti);
			String first_tuple_string = dbg.StringFromIndex(cti);
			System.out.println("Corresponding to string:");
			System.out.println(first_tuple_string);
*/
			initial_tuple_index=cti;
		}
		else if(!initial_sequence.isEmpty())
		{
			String first_tuple_string = initial_sequence.substring(0,dbg.GetOrder());
			initial_tuple_index = dbg.IndexFromString(first_tuple_string);
			scaffold_sequence=initial_sequence.substring(0,initial_sequence.length()-dbg.GetOrder());
			String sequ_last_tuple = initial_sequence.substring(initial_sequence.length()-dbg.GetOrder(),initial_sequence.length());
			cti = dbg.IndexFromString(sequ_last_tuple);
			initial_depth=scaffold_sequence.length();
		}
		
		
		
		CreateDBSequenceRecursively(scaffold_sequence,cti,initial_depth,rev_comp_free);
	}
	
	public String GetSequence()
	{
		return scaffold_sequence;
	}
	
	public boolean SolutionFound()
	{
		return solution_found;
	}
	
	private void CreateDBSequenceRecursively(String curr_sequence,int curr_tuple_index,int curr_depth,boolean rev_comp_free)
	{
		// just check whether the final depth has been reached:
		
		//if(curr_depth==2048)
		// abort after certain time:
		if((System.currentTimeMillis()-initial_time) >3000)
		{
			long end_time = System.currentTimeMillis();
			
			System.out.println("TIME OUT");
			System.out.println(end_time);
			end_reached=true;
			solution_found=false;
		}
		if(curr_depth==this.scaffold_length)
		{
			//System.out.println("end reached: ");
			//System.out.println(curr_sequence);
			scaffold_sequence=curr_sequence;
			end_reached=true;
			if(is_circular==true)
			{
				if(curr_tuple_index==initial_tuple_index)
				{
					System.out.println("circular solution found");
					solution_found=true;
				}
				else
				{
					//System.out.println("no circular solution");
					solution_found=false;
				}
			}
			else
			{
				System.out.println("linear solution found");
				solution_found=true;
			}
			return;
		}
		else
		{
			// add letter of current tuple to the sequence:
			if(end_reached==false)
			{
				curr_sequence+=db_graph.StringFromIndex(curr_tuple_index).substring(0,1);
				ArrayList<Integer> curr_neighbors=db_graph.GetNeighbors(curr_tuple_index);
				int num_neighbors = curr_neighbors.size();
				Collections.shuffle(curr_neighbors);
				ArrayList<Integer> curr_neighbors2=db_graph.GetNeighbors(curr_tuple_index);
				for(int i=0;i<num_neighbors;i++)
				{	
					int first_element = curr_neighbors.get(0);
					
					// only relevant if "rev_comp_free==true"
					int index_rev_comp_first=0;
					int index_rev_comp_second=0;
					boolean contained_tuple=false;
					if(rev_comp_free==true)
					{
						// check for self-complementarity, return if true:
						
						String curr_tup_sequence = db_graph.StringFromIndex(curr_tuple_index).substring(0,1) + db_graph.StringFromIndex(first_element).substring(0,db_graph.GetOrder());					
						String curr_revcomp_sequence = DeBruijnGraph.GetReverseComplementarySequence(curr_tup_sequence);
						index_rev_comp_first = db_graph.IndexFromString(curr_revcomp_sequence.substring(0,db_graph.GetOrder()));
						index_rev_comp_second = db_graph.IndexFromString(curr_revcomp_sequence.substring(1,db_graph.GetOrder()+1));
						ArrayList<Integer> curr_nbs_rc = db_graph.GetNeighbors(index_rev_comp_first);
						contained_tuple=curr_nbs_rc.contains(Integer.valueOf(index_rev_comp_second));
						if((curr_tup_sequence.equals(curr_revcomp_sequence)) && contained_tuple==true)
						{
							curr_nbs_rc.remove(Integer.valueOf(index_rev_comp_second));
							curr_nbs_rc.add(Integer.valueOf(index_rev_comp_second));
							continue;
						}
						if(contained_tuple==true)
						{
							curr_nbs_rc.remove(Integer.valueOf(index_rev_comp_second));
						}
					}
					
					curr_neighbors.remove(0); // ???????? CAREFUL, SHOULDN'T THIS BE "curr_neighbors.remove(first_element)???????
					// if rev_comp_free is true, then also delete the neighbor, otherwise add it:
					// BE VERY CAREFUL HERE with palindromic sequences...
					CreateDBSequenceRecursively(curr_sequence,first_element,curr_depth+1,rev_comp_free);

					// add curr_neighbors again....
					
					curr_neighbors.add(first_element);
					
					if(rev_comp_free==true)
					{
						ArrayList<Integer> curr_nbs_rc = db_graph.GetNeighbors(index_rev_comp_first);
						// check whether value is already there:
						if((!curr_nbs_rc.contains(Integer.valueOf(index_rev_comp_second))) &&(contained_tuple==true))
						{
							curr_nbs_rc.add(Integer.valueOf(index_rev_comp_second));
						}
					}
				}	
			}
			else
			{
				return;
			}
		}
	}
}
