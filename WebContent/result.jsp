<%@page import="java.util.HashMap"%>
<%@page import="employees.Employee"%>
<%@page import="employees.Tuple"%>
<%@page import="java.util.Map.Entry"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html HTML5>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Employees</title>
</head>
<body>

  <%
  if(session.getAttribute("error") == null) {
  HashMap<Integer, Tuple<Employee,Employee>> maxDuration = (HashMap<Integer, Tuple<Employee,Employee>>)session.getAttribute("result");
  for (Entry<Integer, Tuple<Employee, Employee>> data : maxDuration.entrySet()) {
		
	 %>
	  <h4 id="grid1Label">
    Two colleagues who worked together the most:
  </h4>
  <table role="grid"
       aria-labelledby="grid1Label"
       class="data">
    <tbody>
      <tr>
        <th>
          Employee ID #1
        </th>
        <th>
          Employee ID #2
        </th>
        <th>
          Project ID
        </th>
        <th>
          Days worked
        </th>
      </tr>
      <tr>
        <td tabindex="-1">
          <%=data.getValue().elementOne.getEmployeeId() %>
        </td>
        <td tabindex="-1">
          <%=data.getValue().elementTwo.getEmployeeId() %>
        </td>
        <td>
          <a tabindex="-1">
            <%=data.getValue().elementOne.getProjectId() %>
          </a>
        </td>
        <td tabindex="-1">
          <%=data.getKey() %>
        </td>
      </tr>
    </tbody>
  </table>
  <%}} else { %>
	 <h1>There are unsupported date formats in the file.</h1>
	 <a href="index.html">Go back!</a>
 <% } %>
</body>
</html>