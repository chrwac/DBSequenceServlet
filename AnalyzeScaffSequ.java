import java.util.*;

public class AnalyzeScaffSequ 
{
	private static char[] bases = {'A','C','G','T'};
	private int letter_frequencies[];
	private String sequ;
	
	private ArrayList<ArrayList<Integer>> indices_of_multiples;
	private ArrayList<String> m_list_of_cont_reverse_complements_forward;
	private ArrayList<String> m_list_of_cont_reverse_complements_backward;
	private ArrayList<ArrayList<Integer>> m_indices_of_reverse_complements;
	
	public AnalyzeScaffSequ(String sequ)
	{
		this.sequ=sequ;
		letter_frequencies=new int[4];
		int length = sequ.length();
		for(int i=0;i<length;i++)
		{
			switch(sequ.charAt(i))
			{
				case 'A':
					letter_frequencies[0]+=1;
					break;
				case 'C':
					letter_frequencies[1]+=1;
					break;
				case 'G':
					letter_frequencies[2]+=1;
					break;
				case 'T':
					letter_frequencies[3]+=1;
					break;
			}
		}
	}
	
	
	public boolean CheckDBProperty(int order)
	{
		ArrayList<String> list_of_tupels = new ArrayList<String>();
		Set<String> set_of_tupels = new HashSet<String>();
		int length = this.sequ.length();
		for(int i=0;i<length-order+1;i++)
		{
			list_of_tupels.add(this.sequ.substring(i,i+order));
		}
		
		// loop over elements in list, check for duplicates:
		for(int i=0;i<list_of_tupels.size();i++)
		{
				if(!set_of_tupels.add(list_of_tupels.get(i)))
				{
					System.out.println("the following tuple already exists: ");
					System.out.println(list_of_tupels.get(i));
					return false;
				}
		}
		System.out.println(list_of_tupels);
		return true;
	}
	
	// order is refering to the order of the de Bruijn sequence (and not the graph... )! 
	public ArrayList<String> Return_Reverse_Complements(int order)
	{
		ArrayList<String> rev_comp_sequences = new ArrayList<String>();
		ArrayList<String> comp_list_tuples = new ArrayList<String>();
		ArrayList<String> list_of_cont_revcomps = new ArrayList<String>();
		
		int length=this.sequ.trim().length();
		for(int i=0;i<length-order+1;i++)
		{
			comp_list_tuples.add(this.sequ.substring(i,i+order));
			rev_comp_sequences.add(DeBruijnGraph.GetReverseComplementarySequence(this.sequ.substring(i,i+order)));
		}
		
		for(int i=0;i<rev_comp_sequences.size();i++)
		{
			if(comp_list_tuples.contains(rev_comp_sequences.get(i)))
			{
				list_of_cont_revcomps.add(rev_comp_sequences.get(i));
			}
		}
		return list_of_cont_revcomps;
	}
	
	public HashMap<String,ArrayList<Integer>> GetRCsFast(int order)
	{
		HashMap<String,ArrayList<Integer>> hm_tuples = new HashMap<String,ArrayList<Integer>>();
		HashMap<String,ArrayList<Integer>> hm_tuple_rc_pair = new HashMap<String,ArrayList<Integer>>();
		int length = this.sequ.length();
		for(int i=0;i<length-order+1;i++)
		{
			ArrayList<Integer> all_indices = new ArrayList<Integer>();
			String curr_substring = this.sequ.substring(i,i+order);
			
			if(hm_tuples.containsKey(curr_substring))
			{
				all_indices = hm_tuples.get(curr_substring);
				hm_tuples.remove(curr_substring);
				all_indices.add(i);
				hm_tuples.put(curr_substring, all_indices);
			}
			else
			{
				all_indices.add(i);
				hm_tuples.put(curr_substring, all_indices);
			}
		}
		
		// for each entry, check whether reverse complement is also in the hashmap:
		for(Map.Entry<String,ArrayList<Integer>> entry:hm_tuples.entrySet())
		{
			String tuple_seq = entry.getKey();
			ArrayList<Integer> alist1 = entry.getValue();
		
			String rc_seq = DeBruijnGraph.GetReverseComplementarySequence(tuple_seq);
			if(hm_tuples.containsKey(rc_seq))
			{
				ArrayList<Integer> alist2 = hm_tuples.get(rc_seq);
				alist1.addAll(alist2);
				hm_tuple_rc_pair.put(tuple_seq, alist1);
			}
		}
		return hm_tuple_rc_pair;
}
	
	
	public HashMap<String,ArrayList<Integer>> GetMultiplesFast(int order)
	{
		HashMap<String,ArrayList<Integer>> hm_tuples=new HashMap<String,ArrayList<Integer>>();
		HashMap<String,ArrayList<Integer>> hm_multiples = new HashMap<String,ArrayList<Integer>>();
		
		int length = this.sequ.length();
		for(int i=0;i<length-order+1;i++)
		{
			ArrayList<Integer> all_indices = new ArrayList<Integer>();
			String curr_substring = this.sequ.substring(i,i+order);
			
			if(hm_tuples.containsKey(curr_substring))
			{
				all_indices = hm_tuples.get(curr_substring);
				hm_tuples.remove(curr_substring);
				all_indices.add(i);
				hm_tuples.put(curr_substring, all_indices);
			}
			else
			{
				all_indices.add(i);
				hm_tuples.put(curr_substring, all_indices);
			}
		}
		// return only elements that occur more than once:
		for(Map.Entry<String,ArrayList<Integer>> entry:hm_tuples.entrySet())
		{
			ArrayList<Integer> indices = entry.getValue();
			if(indices.size()>1)
			{
				hm_multiples.put(entry.getKey(),entry.getValue());
				//hm_tuples.remove(entry.getKey());
			}
		}
		return hm_multiples;
	}
	
	public ArrayList<String> Return_Multiples(int order)
	{
		ArrayList<String> list_of_tupels = new ArrayList<String>();
		ArrayList<String> list_of_duplicates = new ArrayList<String>();
		
		Set<String> set_of_tupels = new HashSet<String>();
		
		int length = this.sequ.length();
		for(int i=0;i<length-order+1;i++)
		{
			list_of_tupels.add(this.sequ.substring(i,i+order));
		}
		
		// loop over elements in list, check for duplicates:
		for(int i=0;i<list_of_tupels.size();i++)
		{
				if(!set_of_tupels.add(list_of_tupels.get(i)))
				{
					System.out.println("the following tuple already exists: ");
					System.out.println(list_of_tupels.get(i));
					list_of_duplicates.add(list_of_tupels.get(i));
				}
		}
		this.indices_of_multiples=new ArrayList<ArrayList<Integer>>();
		// loop thorught list of multiples and find the indices:
		for(int i=0;i<list_of_duplicates.size();i++)
		{
			ArrayList<Integer> curr_index_list = new ArrayList<Integer>();
			int index = this.sequ.indexOf(list_of_duplicates.get(i));
			
			while(index>=0)
			{
				curr_index_list.add(index);
				index=this.sequ.indexOf(list_of_duplicates.get(i),index+1);
				
			}
			indices_of_multiples.add(curr_index_list);
		}
	//	System.out.println(list_of_tupels);
		return list_of_duplicates;
	}
	
	public ArrayList<ArrayList<Integer>> GetIndicesOfMultiples()
	{
		return this.indices_of_multiples;
	}
	
	public void PrintLetterFrequencies()
	{
		for(int i=0;i<4;i++)
		{
			System.out.println("frequency of: ");
			System.out.println(bases[i]);
			System.out.println(letter_frequencies[i]);
		}
	}
	
	public static void main(String[] args)
	{
		String scaff_sequ = new String("ACTCTCCAGCAATCGCCCTAAATCAGTC");
		AnalyzeScaffSequ anscas = new AnalyzeScaffSequ(scaff_sequ);
		anscas.PrintLetterFrequencies();
		anscas.CheckDBProperty(5);
	}
}
