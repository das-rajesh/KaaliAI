<%-- 
    Document   : index
    Created on : Jun 20, 2018, 10:13:29 PM
    Author     : Dell
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Colors !!!</h1>
        <form action="/" method="post">

            <div>
                <label>Color Name</label>
                <input type="text" name="name" value="${color.name}"/>
            </div
            <div>
                <label>Color Code</label>
                <input type="text" name="code" value="${color.code}"/>
            </div>
            <div>
                <input type="hidden" name="id" value="<c:choose>
                           <c:when test="${color.id!=null}">
                               ${color.id}
                           </c:when>
                           <c:otherwise>
                               0
                           </c:otherwise>
                       </c:choose>"/>
                <button type="submit">Add Color</button>
            </div>

        </form>
        <ul>
            <c:forEach var="color" items="${colors}">
                <li>${color.name}
                    <a href="/edit/${color.id}">(Edit)
                    </a>
                    <a href="/delete/${color.id}" onclick="return confir('Are you sure want to delete')">(X)
                    </a>
                </li>
            </c:forEach>
        </ul>
    </body>
</html>
