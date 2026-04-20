import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
@WebServlet("/Info")
public class Info extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");
        String rno = request.getParameter("rno");
        out.println("<!DOCTYPE html><html><head>");
        out.println("<link rel='stylesheet' href='style.css'>"); 
        out.println("</head><body>");
        if ("Add Student".equals(action)) {
            out.println("<h1>Add Student</h1>");
            out.println("<form action='Ins' method='post' autocomplete='off'><center>");
            out.println("<input type='hidden' name='dbAction' value='insert'>");
            out.println("Name: <input type='text' name='uname' required><br><br>");
            out.println("Roll No: <input type='text' name='rno' required><br><br>");
            out.println("Dept: <input type='text' name='dept' required><br><br>");
            out.println("Email: <input type='email' name='email'><br><br>");
            out.println("Phone: <input type='text' name='phone'><br><br><br>");
            out.println("<input type='submit' value='Submit Registration'>");
            out.println("</form></center>");
        }
        else if ("View Student".equals(action)) {
            out.println("<h1>Student Records</h1><table border='1'><tr><th>Name</th><th>Roll No</th><th>Dept</th><th>Email</th><th>Phone</th><th>Edit</th><th>Delete</th></tr>");
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                try (Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "system");
                     Statement st = con.createStatement();
                     ResultSet rs = st.executeQuery("SELECT * FROM students ORDER BY uname")) {
                    while (rs.next()) {
                        String roll = rs.getString(2);
                        out.println("<tr><td>"+rs.getString(1)+"</td><td>"+roll+"</td><td>"+rs.getString(3)+"</td><td>"+rs.getString(4)+"</td><td>"+rs.getString(5)+"</td>");
                        out.println("<td><a href='Info?rno=" + roll + "'>✏️</a></td>");
                        out.println("<td><a href='Info?action=Delete&rno=" + roll + "' onclick='return confirm(\"Delete this record?\")'>🗑️</a></td></tr>");
                    }
                }
            } catch (Exception e) { out.println("Error: " + e.getMessage()); }
            out.println("</table>");
        }
        else if ("Delete".equals(action) && rno != null) {
            try (Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "system");
                 PreparedStatement ps = con.prepareStatement("DELETE FROM students WHERE rno = ?")) {
                ps.setString(1, rno);
                ps.executeUpdate();
                response.sendRedirect("Info?action=View Student");
            } catch (Exception e) { out.println("Delete Error: " + e.getMessage()); }
        }
        else if (rno != null && action == null) {
            try (Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "system");
                 PreparedStatement ps = con.prepareStatement("SELECT * FROM students WHERE rno = ?")) {
                ps.setString(1, rno);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    out.println("<h1>Edit Student</h1><form action='Info' method='post'>");
                    out.println("<input type='hidden' name='action' value='UpdateRecord'>");
                    out.println("Roll No: <input type='text' name='rno' value='"+rs.getString(2)+"' readonly><br><br>");
                    out.println("Name: <input type='text' name='uname' value='"+rs.getString(1)+"'><br><br>");
                    out.println("Dept: <input type='text' name='dept' value='"+rs.getString(3)+"'><br><br>");
                    out.println("Email: <input type='email' name='email' value='"+rs.getString(4)+"'><br><br>");
                    out.println("Phone: <input type='text' name='phone' value='"+rs.getString(5)+"'><br><br>");
                    out.println("<input type='submit' value='Update Record'></form>");
                }
            } catch (Exception e) { out.println("Edit Error: " + e.getMessage()); }
        }
        else if ("UpdateRecord".equals(action)) {
            try (Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "system");
                 PreparedStatement ps = con.prepareStatement("UPDATE students SET uname=?, dept=?, email=?, phone=? WHERE rno=?")) {
                ps.setString(1, request.getParameter("uname"));
                ps.setString(2, request.getParameter("dept"));
                ps.setString(3, request.getParameter("email"));
                ps.setString(4, request.getParameter("phone"));
                ps.setString(5, request.getParameter("rno"));
                ps.executeUpdate();
                out.println("<script>alert('Updated!'); window.location='Info?action=View Student';</script>");
            } catch (Exception e) { out.println("Update Error: " + e.getMessage()); }
        }
        out.println("<br><a href='Dashboard'>Back to Dashboard</a></body></html>");
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { processRequest(req, resp); }
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { processRequest(req, resp); }
}