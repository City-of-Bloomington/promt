package program.web;

import java.util.*;
import java.util.HashSet;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.list.*;
import program.model.*;
import program.util.*;

@WebServlet(urlPatterns = {"/MarketTable","/MarketResult"})
public class MarketResult extends TopServlet{

    static Logger logger = LogManager.getLogger(MarketResult.class);

    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException{

	doPost(req, res);
    }
    /**
     * Processes the marketing search request. 
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{
	PrintWriter out;
	String message="";
	boolean success = true;
	res.setStatus(HttpServletResponse.SC_OK);
	res.setContentType("text/html");
	out = res.getWriter();
	Enumeration values = req.getParameterNames();
	String name, value;
	out.println("<html>");
	User user = null;
	HttpSession session = null;
	session = req.getSession(false);		
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	if(user == null){
	    String str = url+"Login";
	    res.sendRedirect(str);
	    return;
	}		
	MarketList markets = new MarketList(debug);
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    value = (req.getParameter(name)).trim();
	    if (name.equals("date_from")){
		markets.setDate_from(value);
	    }
	    else if (name.equals("date_to")){
		markets.setDate_to(value);
	    }
	    else if (name.equals("which_date")){
		markets.setWhich_date(value);
	    }			
	    else if (name.equals("id")){
		markets.setId(value);
	    }
	    else if (name.equals("prog_id")){
		markets.setProg_id(value);
	    }			
	    else if (name.equals("year")){
		markets.setYear(value);
	    }			
	    else if (name.equals("season")){
		markets.setSeason(value);
	    }
	    else if (name.equals("market_ad")){
		markets.setMarket_ad(value);
	    }
	    else if (name.equals("category_id")){
		markets.setCategory_id(value);
	    }
	    else if (name.equals("market_announce")){
		markets.setMarket_announce(value);
	    }
	    else if (name.equals("market_type")){
		markets.setMarket_type(value);
	    }
	    else if (name.equals("sortBy")){
		markets.setSortBy(value);
	    }
	}
	String back = markets.find();
	if(!back.equals("")){
	    message += back;
	    success = false;
	}

	out.println("<head><title> Programs Marketing " + 
		    "</title>");
	Helper.writeWebCss(out, url);
	out.println("</head><body><center>");
	out.println("<font size=+2>Parks and Recreation"+
		    "</font><br>");	
	Helper.writeTopMenu(out, url);
	out.println("<h2>Marketing Search Results</h2>");
	if(!success){		
	    if(!message.equals("")){
		out.println("<font color=red>"+message+"</font><br />");
	    }
	}
	else{
	    out.println("Matching total records :"+ markets.size() + "<br />");
	    out.println("<table width=\"100%\">");
	    for(Market market:markets){
		// market id (same as program)
		out.println("<tr><td align=\"right\"><b>Marketing ID:</b></td>");
		out.println("<td align=\"left\">");
		out.print("<a href=\""+url+"Market.do?id="+market.getId()+
			  "\">"+market.getId()+"</a>");
		out.println("</td></tr>");
		Program prog = market.getProgram();
		Facility facility = market.getFacility();
		General general = market.getGeneral();
		if(prog != null){
		    out.println("<tr><td align=\"right\"><b>Program</b></td>");
		    out.println("<td align=\"left\">");
		    out.println("<a href=\""+url+"Program.do?id="+prog.getId()+"&action=zoom\">"+prog.getTitle()+" ("+prog.getSeason()+"/"+prog.getYear()+")</a></td></tr>");
		}
		else if(facility != null){
		    out.println("<tr><td align=\"right\"><b>Facility</b></td>");
		    out.println("<td align=\"left\">");
		    out.println("<a href=\""+url+"Facility?id="+facility.getId()+"&action=zoom\">"+facility.getName()+"</a></td></tr>");
		}
		else if(general != null){
		    out.println("<tr><td align=\"right\"><b>General Listing</b></td>");
		    out.println("<td align=\"left\">");
		    out.println("<a href=\""+url+"General.do?id="+general.getId()+"&action=zoom\">"+general.getTitle()+"</a></td></tr>");

		}
		//
		List<MarketAd> ads = market.getAds();
		if(ads != null && ads.size() > 0){
		    out.println("<tr><td align=\"right\"><b>Ads</b></td>");
		    String all = "";
		    for(MarketAd one:ads){
			if(!all.equals("")) all += ", ";
			all += one;
			if(!one.getDue_date().equals("")){
			    all += " Due:"+one.getDue_date();
			}
			if(!one.getDetails().equals("")){
			    all += " Details: "+one.getDetails();
			}
		    }
		    out.println("<td align=\"left\">"+all+"</td></tr>");
		}
		List<Type> announces = market.getAnnounces();
		if(announces != null && announces.size() > 0){
		    out.println("<tr><td align=\"right\"><b>Announcements</b></td>");
		    String all = "";
		    for(Type one:announces){
			if(!all.equals("")) all += ", ";
			all += one;
		    }
		    out.println("<td align=\"left\">"+all+"</td></tr>");
		}
		List<MarketItem> items = market.getItems();
		if(items != null && items.size() > 0){
		    out.println("<tr><td align=\"right\" valign=\"top\"><b>Marketing Pieces</b></td>");
		    String all = "";
		    for(MarketItem one:items){
			if(!all.equals("")) all += "<br />";
			all += one.getType()+" ("+one.getQuantity()+")";
			if(!one.getDue_date().equals("")){
			    all += " Due:"+one.getDue_date();
			}
			if(!one.getDetails().equals("")){
			    all += ", Details: "+one.getDetails();
			}						
		    }
		    out.println("<td align=\"left\">"+all+"</td></tr>");
		}
		if(!market.getOther_market().equals("")){
		    out.println("<tr><td align=\"right\"><b>Other Marketing</b></td>");
		    out.println("<td align=\"left\">"+market.getOther_market()+"</td></tr>");					
		}
		if(!market.getSpInstructions().equals("")){
		    out.println("<tr><td align=\"right\"><b>Special Instructions</b></td>");
		    out.println("<td align=\"left\">"+market.getSpInstructions()+"</td></tr>");
		}
		out.println("<tr><td colspan=\"2\">&nbsp;</td></tr>");
	    }
	    out.println("</table><br />");
	}
	out.println("</body>");
	out.println("</html>");
	out.flush();
	out.close();

    }



}






















































