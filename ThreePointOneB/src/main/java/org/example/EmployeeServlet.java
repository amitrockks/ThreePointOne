package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/employeeServlet")
public class EmployeeServlet extends HttpServlet {

    // --- Database Connection Details ---
    // !! Update these with your database details
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/company";
    private static final String USER = "root"; // Your DB username
    private static final String PASS = "amity7161"; // Your DB password
    // -------------------------------------

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Get the search parameter from the URL
        String empIdStr = request.getParameter("empId");

        out.println("<html><head><title>Employee Records</title></head><body>");
        out.println("<h2>Employee Database</h2>");

        // --- 1. Display the Search Form ---
        // The form action points to this servlet itself, using method GET.
        out.println("<form action='employeeServlet' method='GET'>");
        out.println("Enter Employee ID: <input type='text' name='empId'>");
        out.println("<input type='submit' value='Search'>");
        out.println("<a href='employeeServlet'>Show All</a>"); // Link to clear search
        out.println("</form><hr>");

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // 1. Load the JDBC driver
            Class.forName(JDBC_DRIVER);

            // 2. Open a connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            String sql;

            // --- 2. Create the SQL Query (Conditional) ---
            if (empIdStr != null && !empIdStr.isEmpty()) {
                // --- A. Search for a specific employee ---
                // We use PreparedStatement to prevent SQL injection
                sql = "SELECT * FROM Employee WHERE EmpID = ?";
                pstmt = conn.prepareStatement(sql);

                try {
                    int empId = Integer.parseInt(empIdStr);
                    pstmt.setInt(1, empId);
                } catch (NumberFormatException e) {
                    out.println("<p style='color:red'>Error: Employee ID must be a number.</p>");
                    out.println("</body></html>");
                    return; // Stop execution
                }

            } else {
                // --- B. Show all employees ---
                sql = "SELECT * FROM Employee";
                pstmt = conn.prepareStatement(sql);
            }

            // 3. Execute the query
            rs = pstmt.executeQuery();

            // --- 3. Display Results in a Table ---
            out.println("<table border='1' cellpadding='5'>");
            out.println("<tr><th>Employee ID</th><th>Name</th><th>Salary</th></tr>");

            boolean found = false;
            // 4. Process the result set
            while (rs.next()) {
                found = true;
                int id = rs.getInt("EmpID");
                String name = rs.getString("Name");
                double salary = rs.getDouble("Salary");

                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + name + "</td>");
                out.println("<td>" + salary + "</td>");
                out.println("</tr>");
            }

            if (!found) {
                out.println("<tr><td colspan='3' style='color:red;'>No records found.</td></tr>");
            }

            out.println("</table>");

        } catch (ClassNotFoundException e) {
            out.println("<p style='color:red'>Error: MySQL JDBC Driver not found!</p>");
            e.printStackTrace(out); // Helps in debugging
        } catch (SQLException e) {
            out.println("<p style='color:red'>Database Error: " + e.getMessage() + "</p>");
            e.printStackTrace(out);
        } finally {
            // 5. Clean-up: Close all resources
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace(out);
            }
        }

        out.println("</body></html>");
        out.close();
    }
}