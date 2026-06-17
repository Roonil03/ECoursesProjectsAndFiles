<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Exercise 2: Customized Hello World</title>
</head>
<body>
    <% 
        String username = request.getParameter("name");
        if (username == null || username.trim().isEmpty()) {
            username = "Guest";
        }
    %>
    <h1>Hello, <%= username %>!</h1>
    
    <p>Welcome to your customized JSP lab application.</p>
    <p>The current server time is: <strong><%= new java.util.Date() %></strong></p>
</body>
</html>