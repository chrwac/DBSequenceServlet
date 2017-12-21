

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import javax.servlet.*;
/**
 * Servlet implementation class scaff_sequ_servlet
 */

public class scaff_sequ_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public scaff_sequ_servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//System.out.println("Hello World");
		
		// create an object of "SimpleRandomSequ" - type:
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
		
		
		
		String str_scaff_length="";
		String scaff_sequence="";
		boolean is_circular=false;
		boolean is_rev_comp_free = false;
		
		if(request.getParameter("CGf")!=null)
		{
			str_scaff_length = request.getParameter("scafflength");
			int scaff_length = Integer.parseInt(str_scaff_length);
			SimpleRandomSequ srs = new SimpleRandomSequ(scaff_length,trans_probs);
			scaff_sequence = srs.GetSequence();
		}
		else if(request.getParameter("DBS")!=null)
		{
			
			
			String email_to = request.getParameter("emailaddress");
			String str_db_order = request.getParameter("DBorder");
			String str_circularity = request.getParameter("circularity");
			String init_sequence = request.getParameter("constpart").toUpperCase().replaceAll("\\s",""); // remove whitespaces...
			String string_forbidden_sequs = request.getParameter("forbiddenSequs").toUpperCase();
			str_scaff_length = request.getParameter("scafflength");
			String rc_free_string = request.getParameter("rcsequences").replaceAll("\\s","");
			

			int scaff_length = Integer.parseInt(str_scaff_length);
			int db_order = Integer.parseInt(str_db_order);
			
			int max_length = (int)Math.pow(4.0,(double)(db_order+1.0));
			System.out.println("the maximal possible length of a sequence of this order is: ");
			System.out.println(max_length);
			
			if(email_to.replaceAll("\n","").isEmpty())
			{
				response.getWriter().println("<html>");
				response.getWriter().println("<body>");
				response.getWriter().println("please enter an email-address.");
				response.getWriter().println("</body>");
				response.getWriter().println("</html>");
				return;
			}
			if(scaff_length>max_length)
			{
				response.getWriter().println("<html>");
				response.getWriter().println("<body>");
				response.getWriter().println("scaffold of this dB-order can have a maximal length of: " + max_length);
				response.getWriter().println("</body>");
				response.getWriter().println("</html>");
				return;
			}
			String[] string_list_forbidden_sequs = request.getParameter("forbiddenSequs").toUpperCase().split(";");
			
			
		
			// create DeBruijnGraph - Object:
			DeBruijnGraph db_graph = new DeBruijnGraph(db_order);
			
			for(String forbsequ:string_list_forbidden_sequs)
			{
				forbsequ=forbsequ.replaceAll("\\s","");
				System.out.println(forbsequ);
				int length = forbsequ.length();
				System.out.println(length);
				if(length==(db_order+1))
				{
					String prefix_string = forbsequ.substring(0,length-1);
					String postfix_string = forbsequ.substring(1,length);
					System.out.println(prefix_string);
					System.out.println(postfix_string);
					db_graph.DeleteEdge(prefix_string,postfix_string);
				}
				else if((length<(db_order+1)) && (length>0))
				{
					db_graph.RemoveSequence(forbsequ);
				}
			}
			
			// if reverse complementary - free sequence is desired:
			if(rc_free_string.equals("yes"))
			{
				System.out.println("reverse complementary sequence will be generated");
				db_graph.RemoveReverseComplementsOfSequence(init_sequence);
				is_rev_comp_free=true;
			}
			
			else if(rc_free_string.equals("no"))
			{
				System.out.println("reverse complements allowed");
			}
			
			if(str_circularity.equals("circular"))
			{
				is_circular=true;
				System.out.println("circular sequence desired!");
			}
				
		//	int num_vertices = db_graph.GetNumVertices();
			//int num_edges = db_graph.GetNumEdges();
			
			db_graph.ConsiderInitialSequence(init_sequence);
			scaff_sequence="NO SOLUTION FOUND";
			boolean sequence_found=false;
			
			AsyncContext asyncContext=request.startAsync();
			asyncContext.addListener(new ScaffSequAsyncListener());
			
			final ExecutorService pool;
			pool = Executors.newFixedThreadPool(4);
			pool.execute(new ScaffSequAsyncRequestProcessor(asyncContext,db_graph,is_circular,scaff_length,init_sequence,is_rev_comp_free,email_to,string_forbidden_sequs));
			response.getWriter().println("<html>");
			response.getWriter().println("<body>");
			response.getWriter().println("your request is being processed.You will receive an email.");
			response.getWriter().println("</body>");
			response.getWriter().println("</html>");
			//asyncContext.complete();
		}
		
		
	
	}

}
