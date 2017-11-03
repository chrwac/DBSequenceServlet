

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class scaff_sequ_servlet
 */
//@WebServlet("/scaff_sequ_servlet")
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
		
		
		
		String scaff_length="";
		if(request.getParameter("CGf")!=null)
		{
			System.out.println("CGf-button pushed");
			scaff_length = request.getParameter("scaff_length");
			System.out.println(scaff_length);
		}
		else if(request.getParameter("DBS")!=null)
		{
			
			String DB_order = request.getParameter("DBorder");
			scaff_length = request.getParameter("scaff_length");
			System.out.println("DBS-button pushed");
			System.out.println(scaff_length);
		}
		//int length = Integer.parseInt(scaff_length);
		
		SimpleRandomSequ srs = new SimpleRandomSequ(7560,trans_probs);
		String scaff_sequ = srs.GetSequence();
		
		String name = request.getParameter("userName");
		String email = request.getParameter("email");
		String ip = request.getRemoteAddr();
		response.getWriter().println("<html>");
		response.getWriter().println("<head>");
		response.getWriter().println("<title>This is the response</title>");
		response.getWriter().println("</head>");
		response.getWriter().println("<body>");
		//response.getWriter().println("<form>");
		
		response.getWriter().println("<textarea cols=\"150\" rows=\"20\">" + scaff_sequ);
		response.getWriter().println("</textarea>");
		
		//response.getWriter().println("Your name is: " + name);
		//response.getWriter().println("<br> Your email is: " + email);
		//response.getWriter().println("Your IP Address is: " + ip);
		//response.getWriter().println(scaff_sequ);
		response.getWriter().println("</body>");
		response.getWriter().println("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
