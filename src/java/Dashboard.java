import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
@WebServlet("/Dashboard")
public class Dashboard extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            response.sendRedirect("index.html");
            return;
        }
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html><html><head><title>Dashboard</title>");
            out.println("<link rel='stylesheet' href='style.css'>"); 
            out.println("</head><body>");
            out.println("<h1>Student Management System</h1>");
            out.println("<p>Welcome, <b>" + session.getAttribute("loggedUser") + "</b>!</p>");
            out.println("<form action='Info' method='post'>");
            out.println("<button type='submit' name='action' value='Add Student'>Add Student</button><br><br>");
            out.println("<button type='submit' name='action' value='View Student'>View Student Records</button><br><br>");
            out.println("</form>");
            out.println("<a href='index.html'>Logout</a>");
            out.println("</body></html>");
        }
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { processRequest(req, resp); }
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { processRequest(req, resp); }
}