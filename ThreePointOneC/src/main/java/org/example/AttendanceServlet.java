package org.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/attendanceServlet")
public class AttendanceServlet extends HttpServlet {

    // --- Database Connection Details ---
    // !! Update these to use your new 'university' database
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/university";
    private static final String USER = "root"; // Your DB username
    private static final String PASS = "password"; // Your DB password
    // -------------------------------------

    // This method handles the POST request from the JSP form
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Read data from the form
        String studentIdStr = request.getParameter("studentId");
        String dateStr = request.getParameter("attDate"); // Format: yyyy-MM-dd
        String status = request.getParameter("attStatus");

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // 2. Load JDBC driver and connect
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 3. Create the SQL INSERT statement
            String sql = "INSERT INTO Attendance (StudentID, AttendanceDate, Status) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);

            // 4. Set the parameters safely
            int studentId = Integer.parseInt(studentIdStr);
            java.sql.Date sqlDate = java.sql.Date.valueOf(dateStr); // Converts yyyy-MM-dd string to sql.Date

            pstmt.setInt(1, studentId);
            pstmt.setDate(2, sqlDate);
            pstmt.setString(3, status);

            // 5. Execute the insert
            int rowsAffected = pstmt.executeUpdate();

            // 6. Redirect back to JSP with a success message
            if (rowsAffected > 0) {
                response.sendRedirect("index.jsp?success=true");
            } else {
                response.sendRedirect("index.jsp?error=true&message=NoRowsAffected");
            }

        } catch (NumberFormatException e) {
            // Redirect with a specific error if Student ID wasn't a number
            response.sendRedirect("index.jsp?error=true&message=InvalidStudentIDFormat");
        } catch (SQLException | ClassNotFoundException e) {
            // Redirect with a generic database error
            response.sendRedirect("index.jsp?error=true&message=" + e.getMessage());
        } finally {
            // 7. Clean up resources
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    // If a user just types the servlet URL, send them to the form
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("index.jsp");
    }
}