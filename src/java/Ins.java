import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
@WebServlet("/Ins")
public class Ins extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String dbAction = request.getParameter("dbAction");
        if ("insert".equals(dbAction)) {
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                try (Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "system");
                        PreparedStatement ps = con.prepareStatement("INSERT INTO students (uname, rno, dept, email, phone) VALUES(?,?,?,?,?)")) {
                    ps.setString(1, request.getParameter("uname"));
                    ps.setString(2, request.getParameter("rno"));
                    ps.setString(3, request.getParameter("dept"));
                    ps.setString(4, request.getParameter("email"));
                    ps.setString(5, request.getParameter("phone"));
                    int i = ps.executeUpdate();
                    if(i > 0) {
                        out.println("<script>alert('Student Added Successfully!'); window.location='Dashboard';</script>");
                    }
                }
            } catch (Exception e) {
                out.println("Error: " + e.getMessage());
            }
        }
    }
}