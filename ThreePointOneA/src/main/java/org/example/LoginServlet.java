package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A Java Servlet to handle user login.
 * The @WebServlet annotation maps this servlet to the URL pattern "/loginServlet".
 */
@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {

    // We override doPost since the HTML form method is "POST"
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Set the response content type
        response.setContentType("text/html");

        // 2. Get the PrintWriter to send a response to the client
        PrintWriter out = response.getWriter();

        // 3. Retrieve parameters from the HTTP request
        // The string "username" must match the 'name' attribute in the HTML <input> tag
        String user = request.getParameter("username");
        String pass = request.getParameter("password");

        // 4. Validate the credentials (hardcoded for this example)
        // In a real application, you would check this against a database.
        if ("admin".equals(user) && "password123".equals(pass)) {
            // 5a. If valid: Display a personalized welcome message
            out.println("<html><body>");
            out.println("<h2>Welcome, " + user + "!</h2>");
            out.println("<p>You have successfully logged in.</p>");
            out.println("</body></html>");
        } else {
            // 5b. If invalid: Show an error message
            out.println("<html><body>");
            out.println("<h2>Login Failed</h2>");
            out.println("<p style='color:red;'>Invalid username or password.</p>");
            out.println("<p><a href='index.html'>Try again</a></p>");
            out.println("</body></html>");
        }

        // 6. Close the writer
        out.close();
    }
}