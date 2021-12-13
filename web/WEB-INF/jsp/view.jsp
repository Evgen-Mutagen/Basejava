<%@ page import="com.urise.webapp.util.HtmlUtil" %>
<%@ page import="com.urise.webapp.model.TextSection" %>
<%@ page import="com.urise.webapp.model.ListSection" %>
<%@ page import="com.urise.webapp.model.OrganizationSection" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <h1>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h1>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.ContactType, java.lang.String>"/>
                <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
        <hr>

        <c:forEach items="${resume.sections}" var="sectionEntry">
             <jsp:useBean id="sectionEntry"
                 type="java.util.Map.Entry<com.urise.webapp.model.SectionType, com.urise.webapp.model.AbstractSection>"/>
            <c:choose>
                <c:when test="${sectionEntry.key.name() == 'OBJECTIVE' ||sectionEntry.key.name() == 'PERSONAL'}">
                    <dl style="width: 600px;">
                        <dt>${sectionEntry.key.getTitle()}</dt>
                        <dd><%=((TextSection) sectionEntry.getValue()).getTitle()%>
                        </dd>
                    </dl>
                </c:when>
                <c:when test="${sectionEntry.key.name() == 'ACHIEVEMENT' ||sectionEntry.key.name() == 'QUALIFICATIONS'}">
                    <dl style="width: 600px;">
                    <dt>${sectionEntry.key.getTitle()}</dt>
                    <dl style="display: inline-grid;margin-left: 8px;vertical-align: top; padding-top: 0">
                    <dt></dt>
                    <c:forEach var="item" items="<%=((ListSection) sectionEntry.getValue()).getList()%>">
                        <dd>${item}</dd>
                        <hr>
                    </c:forEach>
                    </dl>
                    </dl>
                </c:when>
                <c:when test="${sectionEntry.key.name() == 'EXPERIENCE' ||sectionEntry.key.name() == 'EDUCATION'}">
                    <dl style="width: 600px;">
                        <dt>${sectionEntry.key.getTitle()}</dt>
                        <dl style="display: inline-block;margin-left: 8px;vertical-align: top; padding-top: 0;width: 350px;">
                            <c:forEach var="org" items="<%=((OrganizationSection) sectionEntry.getValue()).getOrganizations()%>">
                                <dt>Название организации</dt>
                                <dd>${org.homePage.name}</dd>
                                <dt>Сайт организации</dt>
                                <dd>${org.homePage.url}</dd>
                                <c:forEach var="pos" items="${org.positions}">
                                    <hr>
                                    <dt>Дата с</dt>
                                    <dd>${pos.startDate}</dd>
                                    <dt>Дата по</dt>
                                    <dd>${pos.endDate}</dd>
                                    <dt>Наименование</dt>
                                    <dd>${pos.title}</dd>
                                    <dt>Описание</dt>
                                    <dd>${pos.description}</dd>
                                </c:forEach>
                            </c:forEach>
                        </dl>
                    </dl>
                </c:when>
            </c:choose>
        </c:forEach>
    <p>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
