
/**
 *
 * @author matatsi
 */
import javax.servlet.annotation.WebServlet;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.sql.*;

@WebServlet(name = "login", urlPatterns = {"/login"}, loadOnStartup=0)
public class login extends HttpServlet {
    public static ArrayList<LoginListener> listeners;
    
    public static void addListener(LoginListener stats )
    {
        if ( listeners == null )
            listeners = new ArrayList();
        listeners.add(stats);
    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();
        Statement stmt;
        ResultSet rs;

        if (username == null || password == null) {
            // lathos request

            return;
        }
        Connection con = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String connectionUrl = "jdbc:mysql://localhost/project3?"
                    + "user=root&password=marouli";
            con = DriverManager.getConnection(connectionUrl);

            if (con != null) {
                System.out.println("Ola ok me mysql");
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (ClassNotFoundException cE) {
            System.out.println("Class Not Found Exception: " + cE.toString());
        }
        try {
            stmt = con.createStatement();

            rs = stmt.executeQuery("SELECT * FROM users WHERE username='"
                    + username + "' and password='" + password + "'");
            if (rs.next()) {
                //edw petuxe to login
                session.setAttribute("user", username);
                
                int i;
                for (i=0; i<listeners.size(); i++)
                    listeners.get(i).UserLogined(username);
                
                wall.wall(username, out, this.getServletContext().getRealPath("/"));
                //out.println("<script>window.location.assign('photos.html')</script>");
            } else {
                out.println("<html><body>Could not login<br><a href=\"index.jsp\">Login</a></body></html>");
            }
            
            con.close();
        } catch (SQLException e) {
            throw new ServletException("Servlet Could not display records.", e);
        }

        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}