/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.servlet.annotation.WebServlet;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;
/**
 *
 * @author zeuc
 */
public class next_transformation extends HttpServlet {
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
        String photo = request.getParameter("photo");
        HttpSession session = request.getSession();
        String username = (String)session.getAttribute("user");

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            String filename = this.getServletContext().getRealPath("/") + "/images/" + photo + ".xml";
            Document my_doc = db.parse(new File(filename));
            NodeList challenges = my_doc.getElementsByTagName("image");
            
            Node _ch = challenges.item(0);
            if (_ch.getNodeType() == Node.ELEMENT_NODE) {
                Element ch = (Element) _ch;

                String caption = ch.getElementsByTagName("caption").item(0).getAttributes().item(0).getNodeValue();
                String transform = ch.getElementsByTagName("transform").item(0).getAttributes().item(0).getNodeValue();

                if (transform.equals("rotate_270")) {
                    transform = "rotate_180";
                } else if (transform.equals("rotate_180")) {
                    transform = "rotate_90";
                } else if (transform.equals("rotate_90")) {
                    transform = "border_one";
                } else if (transform.equals("border_one")) {
                    transform = "border_two";
                } else if (transform.equals("border_two")) {
                    transform = "border_three";
                } else if (transform.equals("border_three")) {
                    transform = "border_four";
                } else if (transform.equals("border_four")) {
                    transform = "border_none";
                } else {
                    transform = "rotate_270";
                }


                db = dbf.newDocumentBuilder();
                my_doc = db.newDocument();

                Element image = my_doc.createElement("image");
                my_doc.appendChild(image);
                Element _caption = my_doc.createElement("caption");
                _caption.setAttribute("value", caption);
                Element _transform = my_doc.createElement("transform");
                _transform.setAttribute("value", transform);
                image.appendChild(_caption);
                image.appendChild(_transform);

                Source source = new DOMSource(my_doc);
                // Prepare the output file
                File file = new File(filename);
                Result result = new StreamResult(file);

                // Write the DOM document to the file
                Transformer xformer = TransformerFactory.newInstance().newTransformer();
                xformer.transform(source, result);
                System.out.println("Changed " + photo);
            }
        } catch (Exception e) {
        }
        
        wall.wall(username, out, this.getServletContext().getRealPath("/"));
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
