/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.util.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author marouli
 */
@WebServlet(name = "statistics", urlPatterns = {"/statistics"}, loadOnStartup = 1)
public class statistics extends HttpServlet implements LoginListener, RegisterListener, PhotoListener, LogoutListener {

    public HashSet<String> users;
    public HashSet<String> newUsers;
    public HashMap<String, ArrayList<String>> photos;

    @Override
    public void UserLogined(String user) {
        users.add(user);
    }

    @Override
    public void UserRegistered(String user) {
        newUsers.add(user);
    }

    @Override
    public void UserUploadedPhoto(String user, String photo) {
        ArrayList<String> userPhotos = photos.get(user);

        if (userPhotos == null) {
            userPhotos = new ArrayList();
        }

        userPhotos.add(photo);
        photos.put(user, userPhotos);
    }

    @Override
    public void UserLogout(String user) {
        users.remove(user);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        users = new HashSet();
        login.addListener(this);

        newUsers = new HashSet();
        register.addListener(this);

        photos = new HashMap();
        up.addListener(this);

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
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("select * from pictures");

            while (rs.next()) {
                UserUploadedPhoto(rs.getString(1), rs.getString(2));
            }

            stmt = con.createStatement();

            rs = stmt.executeQuery("select * from users");

            while (rs.next()) {
                UserRegistered(rs.getString(1));
            }
            con.close();
        } catch (Exception e) {
        }

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
        String display = request.getParameter("display");

        out.println("<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <head>\n"
                + "        <title></title>\n"
                + "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
                + "        <link href=\"statistics.css\" rel=\"stylesheet\" type=\"text/css\" />\n"
                + "    </head>\n"
                + "    \n"
                + "   \n");
                out.println("<body id=\"body\">"
                + "<script>\n"
               
                
                + "setInterval(\"getStats()\" ,1000);\n"
                
                + "function getStats(){\n"
                + "    var req = new XMLHttpRequest();\n"
                + "    req.open('GET', 'http://'+location.host+'/project3/statistics?display="+display+"' , false);\n"
                + "    req.send();\n"
                + "    \n"
                
                + "    if (req.status == 200) { \n"
                + "        \n"
                
                + "        var x = document.getElementById('body');\n"
                + "       if ( x.innerHTML!=req.responseText )\n"
                + "        x.innerHTML=req.responseText;\n"
                + "        \n"
                
                + "    }\n"
                + "}\n"
                + ""
                + "</script>\n");


        try {
            if (display.equals("login")) {

                out.println("<p class=\"tit1\">Users: " + users.size() + "</p>");
                out.println("<table>");
                Iterator login = users.iterator();

                for (; login.hasNext();) {
                    out.println("<tr><td>" + (String) login.next() + "</td></tr>");
                }
                out.println("</table>");
                out.println("<a href=\"index.jsp\">Go Back</a>\n");
            } else if (display.equals("register")) {
                out.println("<p class=\"title\">Users Registered: " + newUsers.size() + "</p>");
                out.println("<table>");
                Iterator reg = newUsers.iterator();

                for (; reg.hasNext();) {
                    out.println("<tr><td class=\"registered\">" + (String) reg.next() + "</td></tr>");
                }
                out.println("</table>");
                out.println("<a href=\"index.jsp\">Go Back</a>\n");
            } else if (display.equals("photos")) {
                out.println("<a href=\"index.jsp\">Go Back</a><br>\n");
                out.println("<table>");
                out.println("<tr><td class=\"tit3\">Users</td><td class=\"tit2\">Photos Uploaded</td></tr>");
                out.println("</table>");

                Set p = photos.entrySet();

                Iterator photo = p.iterator();

                for (; photo.hasNext();) {
                    Map.Entry e = (Map.Entry) photo.next();
                    ArrayList l = (ArrayList) e.getValue();
                    out.println("<div class=\"user\">");
                    out.println("<p>" + (String) e.getKey() + "</p>");

                    int i;

                    out.println("<div class=\"me\">");
                    for (i = 0; i < l.size(); i++) {
                        out.println("<img class=\"small\" src=\"images/" + l.get(i) + "\" >");
                    }
                    out.println("</div>");
                    out.println("</div>");
                }
            }
        } finally {
            
            out.println("</body>");
            out.println("</html>");
            out.close();
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
