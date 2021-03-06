package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.Storage;
import com.urise.webapp.util.DateUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
    }
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Resume r;
        final boolean isCreate = (uuid == null || uuid.length() == 0);

        if (isCreate) {
            r = new Resume(fullName);
        } else {
            r = storage.get(uuid);
            r.setFullName(fullName);
        }

        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (isEmpty(value)) {
                r.getContacts().remove(type);
            } else {
                r.addContact(type, value);
            }
        }
        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            String[] values = request.getParameterValues(type.name());
            if (isEmpty(value) && values.length < 2) {
                r.getSections().remove(type);
            } else {
                switch (type) {
                    case OBJECTIVE:
                    case PERSONAL:
                        r.addSection(type, new TextSection(value));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        r.addSection(type, new ListSection(value.split("\\n")));
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        List<Organization> orgs = new ArrayList<>();
                        String[] urls = request.getParameterValues(type.name() + "url");
                        for (int i = 0; i < values.length; i++) {
                            String name = values[i];
                            if (!isEmpty(name)) {
                                List<Organization.Position> positions = new ArrayList<>();
                                String pfx = type.name() + i;
                                String[] startDates = request.getParameterValues(pfx + "startDate");
                                String[] endDates = request.getParameterValues(pfx + "endDate");
                                String[] titles = request.getParameterValues(pfx + "title");
                                String[] descriptions = request.getParameterValues(pfx + "description");
                                for (int j = 0; j < titles.length; j++) {
                                    if (!isEmpty(titles[j])) {
                                        positions.add(new Organization.Position(DateUtil.parse(startDates[j]), DateUtil.parse(endDates[j]), titles[j], descriptions[j]));
                                    }
                                }
                                orgs.add(new Organization(new Link(name, urls[i]), positions));
                            }
                        }
                        r.addSection(type, new OrganizationSection(orgs));
                        break;
                }
            }
        }

            if (isCreate) {
                storage.save(r);
            } else {
                storage.update(r);
            }
            response.sendRedirect("resume");
        }


        protected void doGet (HttpServletRequest request, HttpServletResponse response) throws
        javax.servlet.ServletException, IOException {
            String uuid = request.getParameter("uuid");
            String action = request.getParameter("action");
            if (action == null) {
                request.setAttribute("resumes", storage.getAllSorted());
                request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
                return;
            }
            Resume r;
            switch (action) {
                case "delete":
                    storage.delete(uuid);
                    response.sendRedirect("resume");
                    return;
                case "view":
                    r = storage.get(uuid);
                    break;
                case "edit":
                    r = storage.get(uuid);
                    for (SectionType type : SectionType.values()) {
                        AbstractSection section = r.getSection(type);
                        switch (type) {
                            case OBJECTIVE:
                            case PERSONAL:
                                if (section == null) {
                                    section = TextSection.NEW;
                                }
                                break;
                            case ACHIEVEMENT:
                            case QUALIFICATIONS:
                                if (section == null) {
                                    section = ListSection.NEW;
                                }
                                break;
                            case EXPERIENCE:
                            case EDUCATION:
                                OrganizationSection orgSection = (OrganizationSection) section;
                                List<Organization> emptyFirstOrganizations = new ArrayList<>();
                                emptyFirstOrganizations.add(Organization.NEW);
                                if (orgSection != null) {
                                    for (Organization org : orgSection.getOrganizations()) {
                                        List<Organization.Position> emptyFirstPositions = new ArrayList<>();
                                        emptyFirstPositions.add(Organization.Position.NEWPOS);
                                        emptyFirstPositions.addAll(org.getPositions());
                                        emptyFirstOrganizations.add(new Organization(org.getHomePage(), emptyFirstPositions));
                                    }
                                }
                                section = new OrganizationSection(emptyFirstOrganizations);
                                break;
                        }
                        r.addSection(type, section);
                    }
                    break;
                case "add_resume":
                    r = Resume.NEWR;
                    break;
                default:
                    throw new IllegalArgumentException("Action " + action + " is illegal");
            }
            request.setAttribute("resume", r);
            request.getRequestDispatcher(
                    ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
            ).forward(request, response);
        }
    }
