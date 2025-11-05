<%--
  Created by IntelliJ IDEA.
  User: amityadav
  Date: 05/11/25
  Time: 10:32 pm
  To change this template use File | Settings | File Templates.
--%>
       <%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Attendance Portal</title>
</head>
<body>

<h2>Mark Student Attendance</h2>

<%
    // Check for success/error messages from the servlet
    String success = request.getParameter("success");
    if ("true".equals(success)) {
        out.println("<p style='color:green;'><b>Attendance recorded successfully!</b></p>");
    }

    String error = request.getParameter("error");
    if (error != null) {
        out.println("<p style='color:red;'><b>Error: " + request.getParameter("message") + "</b></p>");
    }
%>

<form action="attendanceServlet" method="POST">
    <table cellpadding="5">
        <tr>
            <td>Student ID:</td>
            <td><input type="text" name="studentId" required></td>
        </tr>
        <tr>
            <td>Date:</td>
            <td><input type="date" name="attDate" required></td>
        </tr>
        <tr>
            <td>Status:</td>
            <td>
                <select name="attStatus" required>
                    <option value="Present">Present</option>
                    <option value="Absent">Absent</option>
                </select>
            </td>
        </tr>
        <tr>
            <td colspan="2"><input type="submit" value="Mark Attendance"></td>
        </tr>
    </table>
</form>

</body>
</html>
