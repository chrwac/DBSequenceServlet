import java.util.*;

public class SimpleRandomSequ 
{
	private static char[] bases = {'A','C','G','T'};
	private String sequence;
	
	public SimpleRandomSequ(int length,double[][] trans_mat)
	{
		
		int[] letter_indices = new int[length];
		sequence = "";
		// assume that the first letter is equally distributed:
		DistributedRandomNumberGenerator drng = new DistributedRandomNumberGenerator();
		drng.addNumber(0, 0.25);
        drng.addNumber(1, 0.25);
        drng.addNumber(2, 0.25);
        drng.addNumber(3, 0.25);
        
        int[] cnt = new int[4];
        letter_indices[0] = drng.getDistributedRandomNumber();
        sequence+=String.valueOf(bases[letter_indices[0]]);
        
		for(int i=1;i<length;i++)
		{
			DistributedRandomNumberGenerator dng = new DistributedRandomNumberGenerator();
			dng.addNumber(0, trans_mat[letter_indices[i-1]][0]);
			dng.addNumber(1, trans_mat[letter_indices[i-1]][1]);
			dng.addNumber(2, trans_mat[letter_indices[i-1]][2]);
			dng.addNumber(3, trans_mat[letter_indices[i-1]][3]);
			letter_indices[i] = dng.getDistributedRandomNumber();
			sequence+=String.valueOf(bases[letter_indices[i]]);
			
			//int index = drng.getDistributedRandomNumber();
			
	//		cnt[index]++;
			
			// add letter by letter:
			
			//System.out.println(index);
			
		}
		//System.out.println(sequence);
		/*System.out.println(cnt[0]);
		System.out.println(cnt[1]);
		System.out.println(cnt[2]);
		System.out.println(cnt[3]);*/
		
	}
	
	public String GetSequence()
	{
		return this.sequence;
	}
	
	
	public static void main(String[] args)
	{
		double[][] trans_probs = new double[4][4];
		trans_probs[0][0] = 0.25;
		trans_probs[0][1] = 0.25;
		trans_probs[0][2] = 0.25;
		trans_probs[0][3] = 0.25;
		trans_probs[1][0] = 0.33;
		trans_probs[1][1] = 0.33;
		trans_probs[1][2] = 0.0;
		trans_probs[1][3] = 0.33;
		
		trans_probs[2][0] = 0.25;
		trans_probs[2][1] = 0.25;
		trans_probs[2][2] = 0.25;
		trans_probs[2][3] = 0.25;
		trans_probs[3][0] = 0.25;
		trans_probs[3][1] = 0.25;
		trans_probs[3][2] = 0.25;
		trans_probs[3][3] = 0.25;
		SimpleRandomSequ srs = new SimpleRandomSequ(7560,trans_probs);
	
		
	}
}
