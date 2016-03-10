<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meal List</title>
    <style type="text/css">
        .exceeded {
            background-color: red;
        }
        .normal {
            background-color: green;
        }
    </style>
</head>
<body>
    <h2>Meal List</h2>
    <table>
        <thead>
            <tr>
                <th>
                    Date
                </th>
                <th>
                    Description
                </th>
                <th>
                    Calories
                </th>
                <th>Options</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="meal" items="${mealList}">
                <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.UserMealWithExceed" scope="page"></jsp:useBean>
                <tr class="${meal.exceed ? "exceeded" : "normal"}">
                    <td>
                        <%= TimeUtil.toStringDate(meal.getDateTime()) %>
                    </td>
                    <td>${meal.description}</td>
                    <td>${meal.calories}</td>
                    <td>Edit | Remove</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
