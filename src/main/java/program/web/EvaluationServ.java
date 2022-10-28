package program.web;

import java.util.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import program.list.*;
import program.model.*;
import program.util.*;

@WebServlet(urlPatterns = {"/Evaluation.do","/Evaluation"})
public class EvaluationServ extends TopServlet{

    String staffSelectArr[] = Helper.staffSelectArr;
    static Logger logger = LogManager.getLogger(EvaluationServ.class);

    public void doGet(HttpServletRequest req, 
		      HttpServletResponse res) 
	throws ServletException, IOException{

	doPost(req, res);
    }
    /**
     * Generates and processes the evaluation form.
     * Handls view, add, update, delete operations on this form.
     * @param req request input parameters
     * @param res reponse output parameters
     * @throws IOException
     * @throws ServletException
     */
    public void doPost(HttpServletRequest req, 
		       HttpServletResponse res) 
	throws ServletException, IOException{

	res.setContentType("text/html");
	PrintWriter out = res.getWriter();
	String id = "";
	Enumeration values = req.getParameterNames();
	String name, value;
	String action="", message="", goals="";
	boolean	success = true;
	String [] vals;
	HttpSession session = null;
	session = req.getSession(false);
	User user = null;
	if(session != null){
	    user = (User)session.getAttribute("user");
	}
	if(user == null){
	    String str = url+"Login";
	    res.sendRedirect(str);
	    return;
	}								
	Evaluation eval = new Evaluation(debug);
	while (values.hasMoreElements()){
	    name = ((String)values.nextElement()).trim();
	    vals = req.getParameterValues(name);
	    value = vals[vals.length-1].trim();		    

	    if(name.equals("id")){
		id = value;
		eval.setId(id);
	    }
	    else if(name.equals("sponsorship")){
		eval.setSponsorship(value);
	    }
	    else if(name.equals("action")){
		action = value;
	    }
	    else if(name.equals("attendance")){
		eval.setAttendance(value);
	    }
	    else if(name.equals("assignment")){
		eval.setAssignment(value);
	    }
	    else if(name.equals("life_cycle")){
		eval.setLife_cycle(value);
	    }
	    else if(name.equals("life_cycle_info")){
		eval.setLife_cycle_info(value);
	    }						
	    else if(name.equals("recommend")){
		eval.setRecommend(value);
	    }
	    else if(name.equals("partnership")){
		eval.setPartnership(value);
	    }
	    else if(name.equals("flier_points")){
		eval.setFlier_points(value);
	    }
	    else if(name.equals("other")){
		eval.setOther(value);
	    }
	    else if(name.equals("wby")){
		eval.setWby(value);
	    }
	    else if(name.equals("sponsorship")){
		eval.setSponsorship(value);
	    }
	    else if(name.startsWith("outcome")){
		eval.addOutcome(name, value);
	    }
	    else if(name.startsWith("update-outcome")){
		eval.updateOutcome(name, value);
	    }			
	    else if(name.startsWith("quantity")){
		eval.addEvalStaff(name, value);
	    }
	    else if(name.startsWith("update-quantity")){
		eval.updateEvalStaff(name, value);
	    }			
	    else if(name.equals("date")){
		eval.setDate(value);
	    }
	}

	//
	if(action.equals("Save")){
	    //
	    String back = eval.doSave();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    else{
		message ="Save Successfully";
	    }			
	}
	else if (action.equals("Update")){
	    String back = eval.doUpdate();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    else{
		message ="Updated Successfully";
	    }
	}
	else if (action.equals("Delete")){
	    String back = eval.doDelete();
	    if(!back.equals("")){
		message += back;
		success = false;
	    }
	    else{
		message ="Deleted Successfully";
	    }
	}
	else if (action.equals("zoom") || action.equals("")){
	    String back = eval.doSelect();
	}
	Program prog = eval.getProgram();
	Plan plan = null;
	List<Objective> objectives = null;
	List<Staff> staffs = null;
	if(prog != null){
	    plan = prog.getPlan();
	}
	if(plan != null){
	    objectives = plan.getObjectives();
	    staffs = plan.getStaffs();
	    goals = plan.getGoals();
	}
	//
	// else start empty form, startNew
	//
	out.println("<html><head><title>Promt Evaluation</title>");
	Helper.writeWebCss(out, url);
	out.println("<script type='text/javascript'>");
	out.println("/*<![CDATA[*/");						
	//
	// This script validate 
	//
	out.println("  function validateForm(){         ");
	out.println("  with(document.myForm){           ");
	out.println("  if(attendance.value.length == 0){ ");
	out.println("       alert(\"Actual Attendance field is required \");");
	out.println("    attendance.focus();             ");
	out.println("    return false;                     ");
	out.println("   }	                        ");
	out.println("  if(assignment.value.length == 0){ ");
	out.println("       alert(\"Staff assignment field is required \");");
	out.println("    assignment.focus();             ");
	out.println("    return false;                     ");
	out.println("   }	                        ");
	out.println("  if(partnership.value.length == 0){ ");
	out.println("       alert(\"Partnership field is required \");");
	out.println("    partnership.focus();             ");
	out.println("    return false;                     ");
	out.println("   }	                        ");
	out.println("  if(sponsorship.value.length == 0){ ");
	out.println("       alert(\"Sponsorship field is required \");");
	out.println("    sponsorship.focus();             ");
	out.println("    return false;                     ");
	out.println("   }	                        ");
	out.println("  if(recommend.value.length == 0){ ");
	out.println("       alert(\"Gemeral Recommendations field is required \");");
	out.println("    recommend.focus();             ");
	out.println("    return false;                     ");
	out.println("   }	                        ");
	out.println("  if(flier_points.value.length == 0){ ");
	out.println("       alert(\"Flier Distribution Points field is required \");");
	out.println("    flier_points.focus();             ");
	out.println("    return false;                     ");
	out.println("   }	                        ");		
	out.println("  if(wby.value.length == 0){ ");
	out.println("       alert(\"Completed by field is required \");");
	out.println("    wby.focus();             ");
	out.println("    return false;                     ");
	out.println("    }	                        ");
	out.println("   } // end with	               ");		
	out.println("  return true;                     ");
	out.println("  }	                        ");
	out.println("  function validateDeleteForm(){   ");            
	out.println("  var x = false;                   ");
	out.println("   x = window.confirm(\"Are you sure you want to delete\"); ");
	out.println("   return x;                       ");
	out.println(" }	                                ");
		
	out.println(" function validateTextarea(ss, len){   ");
        out.println("    if (ss.value.length > len ){  "); 
	out.println("       alert(\"Text Area should not be more than"+
		    " \"+ len + \" chars\"); ");
	out.println("       ss.value = ss.value.substring(0,len); ");
	out.println("       }                  ");
	out.println("   }                      ");
	out.println("/*]]>*/\n");			
	out.println(" </script>                         ");   
	out.println("</head><body  onLoad=\"showStatus();\">");
	out.println("<center>");
	if(!eval.hasRecord()){
	    out.println("<h3>Add Evaluation</h3>");
	}
	else{ // add update
	    out.println("<h3>Edit Evaluation</h3>");
	}
	if(prog != null){
	    out.println("<h2>"+prog.getTitle()+"</h2>");
	    out.println("<br>");
	}
	if(!message.equals("")){
	    if(!success)
		out.println("<font color=\"red\">"+message+"</font><br />");
	}
	//
	out.println("<form name=\"myForm\" method=\"post\" id=\"form_id\" "+
		    "onSubmit=\"return validateForm();\">");
	out.println("<input type=\"hidden\" name=\"id\" value=\""+id+"\" />");
	//the real table
	out.println("<table width=\"90%\" border=\"1\" >");
	out.println("<tr bgcolor=\"#CDC9A3\"><td align=\"left\">");
	out.println("* indicates a required field.");
	out.println("</td></tr>");
	out.println("<tr bgcolor=\"#CDC9A3\"><td align=\"center\">");
	out.println("<table>");
	//
	// program, year, season
	if(prog != null){ 
	    out.println("<tr><td align=\"right\"><b>Program</b></td>");
	    out.println("</b></td><td align=\"left\">");
	    out.println(prog.getTitle()+",&nbsp;");
	    out.println(" (");
	    out.println(prog.getSeasons());
	    out.println(" /"+prog.getYear()+")");
	    out.println("</td></tr>");
	}
	out.println("<tr><td align=\"right\"><b>Program ID:"); //same as program
	out.println("</b></td><td><left><a href=\""+url+"Program.do?id="+id+"&action=zoom\">"+id+"</a></td></tr>");
	out.println("</table></td></tr>");

	out.println("<tr bgcolor=\"#CDC9A3\"><td align=\"center\">");
	out.println("<table width=\"100%\">");		
	out.println("<tr><td colspan=\"2\" align=\"center\" bgcolor=\"navy\" "+
		    "><h3><font color=\"white\">"+
		    "Program Objectives </font></h3></td></tr>");
	out.println("<tr><th>Program Goals</th><td>"+goals+"<br /></td></tr>");
	//
	// Program Objective (purpose)		
	out.println("<tr><th width=\"40%\">Objectives</th><th>Outcomes</th></tr>");
	if(eval.hasRecord()){
	    Set<String> objSet = new HashSet<>();
	    List<Outcome> outcomes = eval.getOutcomes();
	    int jj=1;						
	    if(outcomes != null && outcomes.size() > 0){
		for(Outcome one:outcomes){
		    objSet.add(one.getObjective().getObjective());
		    out.println("<tr><td valign=\"top\">"+jj+" - "+one.getObjective().getObjective()+"</td><td>");
		    out.print("<textarea name=\"update-outcome_"+one.getId()+"\" rows=\"5\" cols=\"50\" "+ 
			      "wrap onChange=\"validateTextarea(this,1000);\">");
		    out.print(one.getOutcome());
		    out.println("</textarea></td></tr>");
		    jj++;
		}
	    }
	    // in case some of the outcomes were not filled in first save
	    if(objectives != null && objectives.size() > 0){
		for(Objective obj:objectives){
		    if(!objSet.contains(obj.getObjective())){ 
			out.println("<tr><td valign=\"top\">"+jj+" - "+obj.getObjective()+"</td><td>");
			out.println("<textarea name=\"outcome_"+obj.getId()+"\" rows=\"5\" cols=\"50\" "+ 
				    "wrap onchange=\"validateTextarea(this,1000);\">");
			out.println("</textarea></td></tr>");
			jj++;
		    }
		}								
	    }
	}
	else if(objectives != null && objectives.size() > 0){
	    int jj=1;
	    for(Objective obj:objectives){
		out.println("<tr><td valign=\"top\">"+jj+" - "+obj.getObjective()+"</td><td>");
		out.println("<textarea name=\"outcome_"+obj.getId()+"\" rows=\"5\" cols=\"50\" "+ 
			    "wrap onchange=\"validateTextarea(this,1000);\">");
		out.println("</textarea></td></tr>");
		jj++;
	    }
	}
	//
	// Profit Objective 
	out.println("<tr><td align=\"right\" valign=\"top\"><b>Profit "+
		    "Objective: ");
	out.println("</b></td><td valign=\"top\">");
	out.println(plan.getProfit_obj());
	out.println("</td></tr>");
	//
	// Target Market
	out.println("<tr><td align=\"right\" valign=\"top\"><b>Target Market: ");
	out.println("</b></td><td valign=\"top\">");
	out.println(plan.getMarket());
	out.println("</td></tr>");
	out.println("<tr><td align=\"right\" valign=\"bottom\"><b>Attendance: </b></td><td align=\"left\">");
	out.println("<table border=\"1\" width=\"40%\">");		
	out.println("<tr><td>"+
		    "Planned Min/Max</td><td>Actual #</td></tr>");
	out.println("<tr><td>");
	out.println(plan.getMin_max());
	out.println("</td><td>");
	out.println("<input name=\"attendance\" size=\"6\" maxlength=\"6\" value=\""+eval.getAttendance()+"\" />");
	out.println("</td></tr></table></td></tr>");
	out.println("<tr><td align=\"right\"><b>Life Cycle:</b></td><td>");
	out.println("<select name=\"life_cycle\">");
	out.println("<option value=\"\"></option>");
	for(String one:Evaluation.life_cycle_options){
	    String selected = one.equals(eval.getLife_cycle())?"selected=\"selected\"":"";
	    out.println("<option "+selected+">"+one+"</option>");
	}
	out.println("</select>");
	out.println("</td></tr>");
	out.println("<tr><td align=\"right\" valign=\"top\"><b>Life Cycle Comments:</b></td><td>");
	out.println("<textarea name=\"life_cycle_info\" rows=\"5\" cols=\"50\" onchange=\"validateTextarea(this,500);\">");
	out.println(eval.getLife_cycle_info());
	out.println("</textarea>");
	out.println("</td></tr>");				
	out.println("</table></td></tr>");
	//
	out.println("<tr bgcolor=\"#CDC9A3\"><td align=\"center\">");
	out.println("<table width=\"100%\">");
		
	// staff consideration
	out.println("<tr><td colspan=\"2\" align=\"center\" bgcolor=\"navy\" "+
		    "><h3><font color=\"white\">"+
		    "Staff Consideration </font></h3></td></tr>");
	out.println("<tr><td width=\"40%\"></td><td align=\"left\"><table border=\"1\" width=\"50%\">"); 
	out.println("<tr><th>Staff</th><th>Planned </th><th>"+
		    "Actual</th></tr>");
	if(eval.hasRecord()){
	    int jj=1;
	    List<EvalStaff> estaffs = eval.getEvalStaffs();
	    if(estaffs != null && estaffs.size() >  0){
		for(EvalStaff one:estaffs){
		    Staff staff = one.getStaff();
		    out.println("<tr><td>"+(jj++)+" - "+staff.getStaff_type().getName()+"</td>");
		    out.println("<td>"+staff.getQuantity()+"</td>");
		    out.println("<td><input type=\"text\" name=\"update-quantity_"+one.getId()+"\" value=\""+one.getQuantity()+"\" size=\"4\" /></td></tr>");
		}
	    }
	}
	else if(staffs != null && staffs.size() > 0){
	    int jj=1;
	    for(Staff one:staffs){
		out.println("<tr><td>"+(jj++)+" - "+one.getStaff_type().getName()+"</td>");
		out.println("<td>"+one.getQuantity()+"</td>");
		out.println("<td><input type=\"text\" name=\"quantity_"+one.getId()+"\" value=\"\" size=\"4\" /></td></tr>");
	    }
	}
	out.println("</table></td></tr>");
	//
	// staff assignments
	out.println("<tr><td align=\"right\" valign=\"top\"><b><br>*Staff Assignments:");
	out.println("</b></td><td align=\"left\">");
	out.println("<font color=\"green\" size=\"-1\">1000 characters maximum<br></font>");
	out.print("<textarea name=\"assignment\" rows=\"8\" cols=\"60\" "+
		  "wrap onchange=\"validateTextarea(this,1000);\">");
	out.print(eval.getAssignment());
	out.println("</textarea>");
	out.println("</td></tr></table></td></tr>");
	//
	// General Recommendation
	out.println("<tr bgcolor=\"#CDC9A3\"><td align=\"center\">");
	out.println("<table width=\"100%\">");
	//
	out.println("<tr><td colspan=\"2\" align=\"center\" bgcolor=\"navy\" "+
		    "><h3><font color=\"white\">"+
		    "Partnership & Sponsorship </font></h3></td></tr>");
	//
	// partnerships
	out.println("<tr><td align=\"right\" valign=\"top\" width=\"40%\"><br>*<b>Partnerships:<b>");
	out.println("</td><td align=\"left\"><font size=\"-1\" color=\"green\">500 characters maximum<br /></font>");
	out.print("<textarea name=\"partnership\" wrap rows=\"7\" cols=\"60\" onChange=\"validateTextarea(this,500);\">");
	out.print(eval.getPartnership());
	out.println("</textarea>");
	out.println("</td></tr>");
	//
	// sponsors
	out.println("<tr><td align=\"right\" valign=\"top\"><br>*<b>Sponsorships:<b>");
	out.println("</td><td align=\"left\"><font size=\"-1\" color=\"green\">1000 characters maximum<br /></font>");
	out.print("<textarea name=\"sponsorship\" wrap rows=\"7\" cols=\"60\" onChange=\"validateTextarea(this,1000);\">");
	out.print(eval.getSponsorship());
	out.println("</textarea></td></tr>");
	out.println("</table></td></tr>");
	//
	out.println("<tr bgcolor=\"#CDC9A3\"><td align=\"center\">");
	out.println("<table width=\"100%\">");
	out.println("<tr><td colspan=\"2\" align=\"center\" bgcolor=\"navy\" "+
		    "><h3><font color=\"white\">"+
		    "Other Considerations </font></h3></td></tr>");
	out.println("<tr><td align=\"right\" valign=\"top\" width=\"40%\"><b>*General"+
		    " Recommendations: ");
	out.println("</b></td><td align=\"left\">");
	out.print("<textarea name=\"recommend\" rows=\"12\" cols=\"60\" "+
		  "wrap>");
	out.print(eval.getRecommend());
	out.println("</textarea>");
	out.println("</td></tr>");
	out.println("<tr><td align=\"right\" valign=\"top\"><br>*<b>Flyer "+
		    "Distribution Points: ");
	out.println("</b></td><td align=\"left\">");
	out.println("<font color=\"green\" size=\"-1\">180 characters maximum. Please specify location for poster distribution list<br /></font>");
	out.print("<textarea name=\"flier_points\" rows=\"3\" cols=\"60\" "+
		  "wrap onChange=\"validateTextarea(this,180);\">");
	out.print(eval.getFlier_points());
	out.println("</textarea>");
	out.println("</left></td></tr>");
	//
	// Other
	out.println("<tr><td align=\"right\" valign=\"top\"><br /><b>Other:");
	out.println("</b></td><td align=\"left\">");
	out.println("<font color=\"green\" size=\"-1\">1500 characters maximum<br></font>");
	out.print("<textarea name=\"other\" rows=\"12\" cols=\"60\" "+ 
		  "wrap onChange=\"validateTextarea(this,1500);\">");
	out.print(eval.getOther());
	out.println("</textarea>");
	out.println("</td></tr>");
	//
	out.println("</table>");
	out.println("</td></tr>");
	out.println("<tr><td>"); 
	out.println("*<b>Completed by:</b>");
	out.println("<input type=\"text\" name=\"wby\" value=\""+eval.getWby()+
		    "\" size=\"40\" maxlingth=\"60\"></input>");
	out.println("<b>Date </b>:");
	out.print("<input type=\"text\" name=\"date\" value=\""+eval.getDate()+
		  "\" size=\"10\" maxlingth=\"10\" "+
		  "id=\"date\" /></input>");
	out.println("</td></tr>"); 
	//
	if(!eval.hasRecord()){
	    if(user.canEdit()){
		out.println("<tr><td align=\"right\"><input type=\"submit\" "+
			    "name=\"action\" value=\"Save\">&nbsp;&nbsp;"+
			    "&nbsp;&nbsp;&nbsp;" +
			    "&nbsp;&nbsp;<input type=\"reset\" value=\"Clear\">"+
			    "</td></tr>");
	    }
	    out.println("</form>");
	}
	else{ // add zoom update
	    out.println("<tr><td><table width=\"100%\"><tr>");
	    if(user.canEdit()){
		out.println("<td valign=top><input type=\"submit\" "+
			    "name=\"action\" value=\"Update\"> "+
			    "</td>");
	    }
	    out.println("<td valign=top>"+
			"<input type=\"button\" "+
			" value=\"Printable Report\" "+
			" onClick=\"window.open('"+url+"EvaluationPrint?id="+id+
			"','report');\" /></td>");
	    out.println("<td valign=\"top\">"+
			"<input type=\"button\" "+
			" value=\"Budget\" "+
			" onClick=\"document.location='"+url+
			"EvalBudget.do?id="+id+
			"'\"; /></td>");
	    //  
	    out.println("<td valign=\"top\">"+
			"<input type=\"button\" "+
			" value=\"Marketing\" "+
			" onClick=\"document.location='"+url+
			"Market.do?prog_id="+id+
			"'\"; /></td>");
	    //
	    out.println("</form>");
	    //
	    if(user.canDelete()){
		out.println("<td valign=\"top\">");
		out.println("<form name=\"myForm2\" method=\"post\" "+
			    "onSubmit=\"return validateDeleteForm()\">");
		out.println("<input type=\"hidden\" name=\"id\" value=\"" + id + "\">");
		out.println("<td align=\"right\" valign=\"top\">");
		out.println("<input type=\"submit\" name=\"action\" "+
			    "value=\"Delete\">");
		out.println("</form>");
		out.println("</td>");
	    }
	    out.println("</tr></table>");
	    out.println("</td></tr>");
	}	    
	out.println("</table>");
	out.println("<hr />");
	out.println("<hr />");
	out.println("<LI><a href=\""+url+"Logout\""+
		    " target=\"_parent\" >Log Out </a>");

	Helper.writeWebFooter(out, url);
	out.println("</center>");
	out.print("</body></html>");
	out.flush();
	out.close();

    }

}















































