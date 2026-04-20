import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
@WebServlet(urlPatterns = {"/Login"})
public class Login extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String username = request.getParameter("uname");
        String password = request.getParameter("pass");
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            try (Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "system")) {
                PreparedStatement ps = con.prepareStatement("select * from login where uname=? and password=?");
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                PrintWriter out = response.getWriter();
                if (rs.next()) {
                    HttpSession session = request.getSession(); 
                    session.setAttribute("loggedUser", username);
                    out.println("<html><head><title>Login</title>");
                    out.println("<link rel='stylesheet' href='style.css'></head>");
                    out.println("<script>alert('Login Successful!\\nWelcome to Dashboard');");
                    out.println("window.location.href='Dashboard';</script>");
                } 
                else {
                    out.println("<html><head>");
                    out.println("<link rel='stylesheet' href='style.css'>");
                    out.println("<title>Login Failed</title></head><body>");
                    out.println("<form style='text-align: center;'>"); 
                    out.println("<h2 style='color: #e74c3c;'>Invalid Username or Password</h2>");
                    out.println("<p>Please check your credentials and try again.</p>");
                    out.println("<br><a href='index.html' class='btn'>Go Back to Login</a>");
                    out.println("</form>");
                    out.println("</body></html>");
                }
            }
        } catch (Exception e) {
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { processRequest(req, resp); }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { processRequest(req, resp); }
}