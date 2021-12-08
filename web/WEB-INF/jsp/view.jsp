<%@ page import="com.urise.webapp.util.HtmlUtil" %>
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
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.ContactType, java.lang.String>"/>
            <b><%=contactEntry.getKey().getTitle()%>: </b>
            <%=HtmlUtil.getFullReference(contactEntry.getKey(), contactEntry.getValue())%>
            <br>
        </c:forEach>
        <hr>

        <c:forEach items="${resume.sections}" var="sectionEntry">
             <jsp:useBean id="sectionEntry"
                 type="java.util.Map.Entry<com.urise.webapp.model.SectionType, com.urise.webapp.model.AbstractSection>"/>
             <b><%=sectionEntry.getKey().getTitle()%>: </b>
             <dl>
                <c:forEach items="<%=HtmlUtil.sectionToHtmlList(sectionEntry.getValue())%>" var="entry">
                    <dd>${entry}</dd>
                </c:forEach>
             </dl>
            <br>
        </c:forEach>
        <button class="key" onclick="window.history.back()">Назад</button>

</section>
<jsp:include page="fragments/footer.jsp"/>

</body>
</html>
