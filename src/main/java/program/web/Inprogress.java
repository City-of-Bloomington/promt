package program.web;

import java.util.*;
import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Inprogress extends HttpServlet{

    /**
     * The main class method.
     *
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

	String name, value, username;
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    value = (req.getParameter(name)).trim();
	    if(name.equals("username")){
		username = value.toLowerCase();
	    }
	}
	out.println("<HTML><HEAD><TITLE>Work in Progress </TITLE>");
	out.println("</HEAD><BODY>");
	out.println("<br><br>");
	out.println("<center><table border=3 width=75%><tr><td>");
	out.println("<center><H1><font color=blue>Page Under "+
		    "Construction</font></H1>");
	out.println("<p><font size=+2 color=red><br>"+
		    "This option is not ready yet.<br> Please check"+
		    " again later </font><br>");
	out.println("<br><br></center>");
	out.println("<p align=right>");

	out.println("<font size=+3 color=purple>**");
	out.println("</font><font size=+3 color=yellow>I");
	out.println("</font><font size=+3 color=blue>T");
	out.println("</font><font size=+3 color=green>S");
	out.println("</font><font size=+3 color=purple>**");
	out.println("</font></td></tr></table>");
	out.println("</BODY></HTML>");
	out.close();
    }


}























































