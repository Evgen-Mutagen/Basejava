package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResumeServlet extends HttpServlet {
    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
//        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        //      String name = request.getParameter("name");
        //     response.getWriter().write(name == null ? "Hello Resumes!" : "Hello " + name + '!');

        String text = "" +
                "<table border=\"2\" width=\"200\" height=\"100\" bgcolor=\"\t#AFEEEE\">" +
                "<tr>" +
                "<th>uuid</th>" +
                "<th>full_name</th>" +
                "</tr>";

        for (Resume r : storage.getAllSorted()) {
            text += "<tr>" +
                    "<td align=center>" + r.getUuid() + "</td>" +
                    "<td align=center>" + r.getFullName() + "</td>" +
                    "</tr>";
        }
        response.getWriter().write(text + "</table>");
    }
}
