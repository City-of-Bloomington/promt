package program.web;
import java.util.*;
import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = {"/Logout"})
public class Logout extends TopServlet{

    /**
     * Handles the logout process and frees out the session info.
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */

    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException{

	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	Enumeration values = req.getParameterNames();
	String name= "";
	String value = "";
	String username = "";
	boolean testFlagOnPC = false;
	HttpSession session = null; 
	session = req.getSession();
	if(session != null){
	    session.invalidate();
	}
	String str = url+"Login";
	res.sendRedirect(str);
	return;

    }

}






















































