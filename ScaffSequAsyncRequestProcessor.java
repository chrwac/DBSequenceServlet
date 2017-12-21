
import javax.servlet.AsyncContext;
import javax.servlet.ServletRequest;
import java.io.PrintWriter;
import java.util.Date;
import java.util.*;
import javax.servlet.*;

public class ScaffSequAsyncRequestProcessor implements Runnable
{
	private AsyncContext asyncContext;
	private DeBruijnGraph m_db_graph;
	private boolean m_is_circular;
	private boolean m_is_rev_comp_free;
	private int m_scaff_length;
	private String m_init_sequence;
	private ServletOutputStream m_servlet_output;
	private String m_email_to;
	private String m_forbidden_sequences;
	
	//is_circular,scaff_length,init_sequence,is_rev_comp_free
	
	
	public ScaffSequAsyncRequestProcessor(AsyncContext asyncContext,DeBruijnGraph db_graph,boolean is_circular,
			int scaff_length,String init_sequence,boolean is_rev_comp_free,String email_to,String forbidden_sequences)
	{
		this.asyncContext = asyncContext;
		this.m_db_graph=db_graph;
		this.m_is_circular=is_circular;
		this.m_is_rev_comp_free=is_rev_comp_free;
		this.m_init_sequence=init_sequence;
		this.m_scaff_length=scaff_length;
		this.m_email_to=email_to;
		this.m_forbidden_sequences=forbidden_sequences;
		
		//m_servlet_output=asyncContext.getResponse().getOutputStream();
	}
	
	public void run()
	{
		System.out.println("running the Asynchronous request..");
		this.asyncContext.complete();
		longRunningProcess();
		
		
	}
	
	public String longRunningProcess()
	{
		System.out.println("processing sequence");
		String scaff_sequence="";
		boolean sequence_found=false;
		for(int i=0;i<30000;i++)
		{
			DeBruijnSequence db_sequence_object = new DeBruijnSequence(m_db_graph,m_is_circular,m_scaff_length,m_init_sequence,m_is_rev_comp_free);
			if(db_sequence_object.SolutionFound()==true)
			{
				scaff_sequence=db_sequence_object.GetSequence();
				sequence_found=true;
				break;
			}
			System.out.println("iteration: ");
			System.out.println(i);
		}
		if(sequence_found==true)
		{
			String message_text="";
			message_text+="de Bruijn order: " + (this.m_db_graph.GetOrder()+1) +"\n";
			if(this.m_is_circular==true)
			{
				message_text+="circular sequence\n";
			}
			else
			{
				message_text+="linear sequence\n";
			}
			
			message_text+="number of bases: " + this.m_scaff_length +"\n";
			if(this.m_is_rev_comp_free==true)
			{
				message_text+="additional sequence is free of reverse complements (db_order and longer)" + "\n";
			}
			
			if(!m_init_sequence.isEmpty())
			{
				message_text+="initial sequence: \n" + this.m_init_sequence+"\n\n";
			}
			
			if(!m_forbidden_sequences.isEmpty())
			{
				message_text+="the following sequences were avoided: \n";
				message_text+=m_forbidden_sequences + "\n";
			}
			message_text += scaff_sequence;
			String subject = "your scaffold sequence";
			
			System.out.println(scaff_sequence);
			SendEmail em = new SendEmail(this.m_email_to,subject,message_text);
			// send confirmational email:
			System.out.println("your sequence has been found.");
			System.out.println("an email was sent to you you.");
		}
		else
		{
			System.out.println("no solution was found");
			String subject = "scaffold sequence serve: no solution was found";
			String message_text = "We are sorry,no solution was found. Please try again.";
			SendEmail em = new SendEmail(this.m_email_to,subject,message_text);
		}
		return "";
	}
}
