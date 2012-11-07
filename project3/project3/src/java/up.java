/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

import org.w3c.dom.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import javax.servlet.http.*;

import java.sql.*;

import java.io.File;
import org.w3c.dom.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMSource;

/**
 *
 * @author matatsi
 */
@WebServlet(name = "up", urlPatterns = {"/up"})
public class up extends HttpServlet {

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
        /*response.setContentType("text/html;charset=UTF-8");
         PrintWriter out = response.getWriter();
         try {
         /* TODO output your page here. You may use following sample code. *//*
         out.println("<html>");
         out.println("<head>");
         out.println("<title>Servlet NewServlet</title>");            
         out.println("</head>");
         out.println("<body>");
         out.println("<h1>Servlet NewServlet at " + request.getContextPath() + "</h1>");
         out.println("</body>");
         out.println("</html>");
         } finally {            
         out.close();
         }
         */

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String caption = request.getParameter("caption");
        HttpSession session = request.getSession();
        String finalimage = "makhs";

        System.out.println(request);
        String username = (String) session.getAttribute("user");
        if (username == null) {
            return;
        }

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);


        if (!isMultipart) {
            System.out.println("File Not Uploaded");
        } else {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload uploadimage = new ServletFileUpload(factory);
            List items = null;

            try {
                items = uploadimage.parseRequest(request);
            } catch (FileUploadException e) {
                e.printStackTrace();
            }
            Iterator itr = items.iterator();
            while (itr.hasNext()) {
                FileItem item = (FileItem) itr.next();
                if (item.isFormField()) {
                    String name = item.getFieldName();
                    String value = item.getString();

                    if (name.equals("caption")) {
                        caption = value;
                    }

                } else {

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
                        String itemName = item.getName();
                        Random generator = new Random();
                        int r = Math.abs(generator.nextInt());

                        String reg = "[.*]";
                        String replacingtext = "";
                        Pattern pattern = Pattern.compile(reg);
                        Matcher matcher = pattern.matcher(itemName);
                        StringBuffer buffer = new StringBuffer();

                        while (matcher.find()) {
                            matcher.appendReplacement(buffer, replacingtext);
                        }
                        System.out.println(itemName);
                        int IndexOf = itemName.indexOf(".");
                        if (IndexOf == -1) {
                            con.close();
                            wall.wall(username,  out, this.getServletContext().getRealPath("/"));
                            return;
                        }
                        String domainName = itemName.substring(IndexOf);
                        finalimage = buffer.toString() + "_" + r + domainName;

                        System.out.println(this.getServletContext().getRealPath("/") + "/images/" + finalimage);


                        File savedFile = new File(this.getServletContext().getRealPath("/") + "/images/" + finalimage);
                        item.write(savedFile);

                        Statement stmt = con.createStatement();

                        stmt.execute("insert into pictures VALUES('" + username + "','" + finalimage + "')");
                        con.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                Document doc = docBuilder.newDocument();

                Element image = doc.createElement("image");
                doc.appendChild(image);
                Element _caption = doc.createElement("caption");
                _caption.setAttribute("value", caption);
                Element _transform = doc.createElement("transform");
                _transform.setAttribute("value", "none");
                image.appendChild(_caption);
                image.appendChild(_transform);
                writeXmlFile(doc, this.getServletContext().getRealPath("/") + "/images/" + finalimage + ".xml");

                wall.wall(username, out, this.getServletContext().getRealPath("/"));
            } catch (Exception e) {
            }
        }


    }

    static void writeXmlFile(Document doc, String filename) {
        try {
            // Prepare the DOM document for writing
            Source source = new DOMSource(doc);
            // Prepare the output file
            File file = new File(filename);
            Result result = new StreamResult(file);

            // Write the DOM document to the file
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
        } catch (TransformerException e) {
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}