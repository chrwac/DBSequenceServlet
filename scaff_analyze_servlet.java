

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

/**
 * Servlet implementation class scaff_analyze_servlet
 */
public class scaff_analyze_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public scaff_analyze_servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(request.getParameter("CREVCOMP")!=null)
		{
			System.out.println("Rev comp pressed");
			String complete_sequ = request.getParameter("complete_sequence").toUpperCase();
			AnalyzeScaffSequ anscas = new AnalyzeScaffSequ(complete_sequ);
			int DB_order = Integer.parseInt(request.getParameter("DBorderAn"));
			
			String string_linearity = request.getParameter("AnCircular");
			boolean is_circular=false;
			if(string_linearity.equals("linear"))
			{
				System.out.println("String is linear");
				is_circular=false;
			}
			else if(string_linearity.equals("circular"))
			{
				System.out.println("String is circular");
				// sequence to check is by a length of "DBorder-1" longer...
				complete_sequ=complete_sequ+complete_sequ.substring(0,DB_order-1);
				is_circular=true;
			}
			HashMap<String,ArrayList<Integer>> hm = anscas.GetRCsFast(DB_order);
			
			if(hm.isEmpty())
			{
				System.out.println("empty list");
				response.getWriter().println("<html>");
				response.getWriter().println("<head>");
				response.getWriter().println("<title>Result</title>");
				response.getWriter().println("</head>");
				response.getWriter().println("<body>");
				response.getWriter().println("No reverse complementary sequences of a length of the DB sequence order found");
				response.getWriter().println("</body>");
				response.getWriter().println("</html>");
			}
			else
			{
				String message_string="";
				for(Map.Entry<String,ArrayList<Integer>> entry:hm.entrySet())
				{
					message_string+=entry.getKey() + " ";
					message_string+=entry.getValue()+"\n";
				}
				response.getWriter().println("<html>");
				response.getWriter().println("<head>");
				response.getWriter().println("<title>Result</title>");
				response.getWriter().println("</head>");
				response.getWriter().println("<body>");
				response.getWriter().println("The following reverse comp. sequences were found: ");
				response.getWriter().println("<textarea style=\"top: 100px;left: 200 px;position:absolute\" cols=\"60\" rows=\"100\">" + message_string);
				response.getWriter().println("</textarea>");
		
				response.getWriter().println("</body>");
				response.getWriter().println("</html>");
			}
			//rc_free_string.equals("yes")
			/*ArrayList<String> rev_comps_cont = anscas.Return_Reverse_Complements(DB_order);
			System.out.println(rev_comps_cont);
			
			if(rev_comps_cont.isEmpty())
			{
				System.out.println("empty list");
				response.getWriter().println("<html>");
				response.getWriter().println("<head>");
				response.getWriter().println("<title>Result</title>");
				response.getWriter().println("</head>");
				response.getWriter().println("<body>");
				response.getWriter().println("No reverse complementary sequences of a length of the DB sequence order found");
				response.getWriter().println("</body>");
				response.getWriter().println("</html>");
			}
			else
			{
				String message_string="";
				for(String rc_string:rev_comps_cont)
				{
					message_string+=rc_string+"\n";
				}
				response.getWriter().println("<html>");
				response.getWriter().println("<head>");
				response.getWriter().println("<title>Result</title>");
				response.getWriter().println("</head>");
				response.getWriter().println("<body>");
				response.getWriter().println("The following reverse comp. sequences were found: ");
				response.getWriter().println("<textarea style=\"top: 100px;left: 200 px;position:absolute\" cols=\"20\" rows=\"50\">" + message_string);
				response.getWriter().println("</textarea>");
		
				response.getWriter().println("</body>");
				response.getWriter().println("</html>");
			}
				*/
		}
		else if(request.getParameter("CDBPPTY")!=null)
		{
			System.out.println("Checking for de Bruijn - property");
		
			// obtain the sequence-string:
			String complete_sequ = request.getParameter("complete_sequence").toUpperCase();
			int DB_order = Integer.parseInt(request.getParameter("DBorderAn"));
			String string_linearity = request.getParameter("AnCircular");
			boolean is_circular=false;
			if(string_linearity.equals("linear"))
			{
				System.out.println("String is linear");
				is_circular=false;
			}
			else if(string_linearity.equals("circular"))
			{
				System.out.println("String is circular");
				// sequence to check is by a length of "DBorder-1" longer...
				complete_sequ=complete_sequ+complete_sequ.substring(0,DB_order-1);
				is_circular=true;
			}
			
			System.out.println(complete_sequ);
			AnalyzeScaffSequ anscas = new AnalyzeScaffSequ(complete_sequ);
			
			HashMap<String,ArrayList<Integer>> hm = anscas.GetMultiplesFast(DB_order);
			System.out.println(hm);
			
			
			if(hm.isEmpty())
			{
				response.getWriter().println("<html>");
				response.getWriter().println("<head>");
				response.getWriter().println("<title>Result</title>");
				response.getWriter().println("</head>");
				response.getWriter().println("<body>");
				response.getWriter().println("No duplicates found");
				response.getWriter().println("</body>");
				response.getWriter().println("</html>");
				
			}
			else
			{
				String message_string="";
				for(Map.Entry<String,ArrayList<Integer>> entry:hm.entrySet())
				{
					ArrayList<Integer> indices = entry.getValue();
					message_string+=entry.getKey() + " ";
					message_string+=entry.getValue()+"\n";
				}
				response.getWriter().println("<html>");
				response.getWriter().println("<head>");
				response.getWriter().println("<title>Result</title>");
				response.getWriter().println("</head>");
				response.getWriter().println("<body>");
				response.getWriter().println("<textarea style=\"top: 100px;left: 200 px;position:absolute\" cols=\"50\" rows=\"100\">" + message_string);
				response.getWriter().println("</textarea>");
				response.getWriter().println("</body>");
				response.getWriter().println("</html>");
				
			}
			
			
		}
	}
}
