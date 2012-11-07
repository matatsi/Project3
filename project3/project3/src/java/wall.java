/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author marouli
 */
import java.io.*;
import java.sql.*;
import java.util.*;
import java.text.*;
import java.util.regex.*;
import javax.servlet.ServletConfig;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.ServletException;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.InputSource;
import java.net.URLEncoder;

public class wall {

    public static void wall(String username, PrintWriter out, String path) {
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
        
        out.println("<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <head>\n"
                + "        <title></title>\n"
                + "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
                + "        <link href=\"style.css\" rel=\"stylesheet\" type=\"text/css\" />\n"
                + "    </head>\n"
                + "    \n"
                + "   \n"
                + "    <body>\n"
                + "        <div id=\"f1\">\n"
                + "        <h1>PHOTOS</h1>\n"
                + "        </div>\n"
                + "        <p>Dear user, you can :</p><a href = \"up.html\">Upload a photo </a><br>\n"
                + "        <a href=\"index.jsp\">Go Back</a>\n"
                + "        \n"
                + "       \n"
                + "            \n"
                + "         \n"
                + "        <ul class=\"gallery\">\n" /*+"			<li>\n" +
                 "				<img src=\"pictures/linguine_porcini.jpg\" alt=\"Linguine with porcini mushrooms\" />\n" +
                 "				<p><a href=\"http://www.flickr.com/photos/mcpig/2079437797/\">Linguine with porcini mushrooms</a></p>\n" +
                 "			</li>\n" +
                 "			<li>\n" +
                 "				<img src=\"pictures/crème_brûlée.jpg\" alt=\"Crème brûlée\" />\n" +
                 "				<p><a href=\"http://www.flickr.com/photos/roboppy/240881250/\">Crème brûlée</a></p>\n" +
                 "			</li>\n" +
                 "			<li>\n" +
                 "				<img src=\"pictures/sushi_variety.jpg\" alt=\"Sushi\" />\n" +
                 "				<p><a href=\"http://www.flickr.com/photos/zeetzjones/442805346/\">Sushi</a></p>\n" +
                 "			</li>\n" +
                 "			<li>\n" +
                 "				<img src=\"pictures/mango_cheesecake.jpg\" alt=\"Mango Cheesecake\" />\n" +
                 "				<p><a href=\"http://www.flickr.com/photos/santos/126910029/\">Mango Cheesecake</a></p>\n" +
                 "			</li>\n" +
                 "			<li>\n" +
                 "				<img src=\"pictures/salmon_teriyaki.jpg\" alt=\"Salmon Teriyaki\" />\n" +
                 "				<p><a href=\"http://www.flickr.com/photos/hermansaksono/4137053327/\">Salmon Teriyaki</a></p>\n" +
                 "			</li>\n" +
                 "			<li>\n" +
                 "				<img src=\"pictures/pancakes.jpg\" alt=\"Pancakes with maple syrup\" />\n" +
                 "				<p><a href=\"http://www.flickr.com/photos/strausser/131236358/\">Pancakes with maple syrup</a></p>\n" +
                 "			</li>\n" +
                 "			<li>\n" +
                 "				<img src=\"pictures/seaweed_salad.jpg\" alt=\"Seaweed salad\" />\n" +
                 "				<p><a href=\"http://www.flickr.com/photos/veganfeast/4052214994/\">Seaweed salad</a></p>\n" +
                 "			</li>\n" +
                 "			<li>\n" +
                 "				<img src=\"pictures/apple_pie.jpg\" alt=\"Apple pie with whipped cream\" />\n" +
                 "				<p><a href=\"http://www.flickr.com/photos/_pixelmaniac_/3471348748/\">Apple pie with whipped cream</a></p>\n" +
                 "			</li>\n"
                 */);
        try {
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("select picture from pictures where user='" + username + "'");

            while (rs.next()) {
                out.println("<li>");

                try {
                    out.println("<div>");
                    out.println("<form action=\"edit\"  method=\"post\">"
                            + "    <input type=\"submit\" value=\"Edit XML\">"
                            + "    <input type=\"hidden\" name=\"photo\" value=\"" + rs.getString(1) + "\">"
                            + "</form>");
                    out.println("<form action=\"next_transformation\"  method=\"post\">"
                            + "    <input type=\"submit\" value=\"Next transformation\">"
                            + "    <input type=\"hidden\" name=\"photo\" value=\"" + rs.getString(1) + "\">"
                            + "</form>");
                    out.println("</div>");
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db = dbf.newDocumentBuilder();

                    Document my_doc = db.parse(new File(path + "/images/" + rs.getString(1) + ".xml"));
                    NodeList challenges = my_doc.getElementsByTagName("image");

                    Node _ch = challenges.item(0);
                    if (_ch.getNodeType() == Node.ELEMENT_NODE) {
                        Element ch = (Element) _ch;

                        String caption = ch.getElementsByTagName("caption").item(0).getAttributes().item(0).getNodeValue();

                        String transform = ch.getElementsByTagName("transform").item(0).getAttributes().item(0).getNodeValue();

                        if (transform.equals("rotate_270")) {
                            out.println("<img class=\"rotate_270\" src=\"images/" + rs.getString(1) + "\">");
                        } else if (transform.equals("rotate_180")) {
                            out.println("<img class=\"rotate_180\" src=\"images/" + rs.getString(1) + "\">");
                        } else if (transform.equals("rotate_90")) {
                            out.println("<img class=\"rotate_90\" src=\"images/" + rs.getString(1) + "\">");
                        } else if (transform.equals("border_one")) {
                            out.println("<img class=\"border_one\" src=\"images/" + rs.getString(1) + "\">");
                        } else if (transform.equals("border_two")) {
                            out.println("<img class=\"border_two\" src=\"images/" + rs.getString(1) + "\">");
                        } else if (transform.equals("border_three")) {
                            out.println("<img class=\"border_three\" src=\"images/" + rs.getString(1) + "\">");
                        } else if (transform.equals("border_four")) {
                            out.println("<img class=\"border_four\" src=\"images/" + rs.getString(1) + "\">");
                        } else {
                            out.println("<img src=\"images/" + rs.getString(1) + "\">");
                        }


                        out.println("<p><a href='.'>" + caption + "</a></p>");


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }



                out.println("</li>");
            }
            con.close();
        } catch (Exception e) {
        }

        out.println("</ul>");
        out.println("</body>");
        out.println("</html>");
    }
}

