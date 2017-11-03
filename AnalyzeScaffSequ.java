import java.util.*;

public class AnalyzeScaffSequ 
{
	private static char[] bases = {'A','C','G','T'};
	private int letter_frequencies[];
	private String sequ;
		
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
