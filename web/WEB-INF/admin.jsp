<!-- %@ page contentType="text/html;charset=UTF-8" language="java"% -->
<%@ taglib prefix="stripes"
	uri="http://stripes.sourceforge.net/stripes.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!--  !DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" -->
<html>
<head>
<title>Scomer Service</title>
<link href="style.css" rel="stylesheet" type="text/css">

</head>
<body>
	<h1>Scomer Service - Admin</h1>

	<stripes:form beanclass="se.sthlm.jfw.mainServlet.AdminActionBean"
		focus="">
		<table border="1">
			<tr>
				<td>&nbsp;</td>
				<td colspan="6">URL:&nbsp;<stripes:text name="serverName"/> </td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td colspan="6"><stripes:submit name="save" value="Save"/></td>
			</tr>
		</table>
	</stripes:form>

	<table border="1">
		<c:forEach items="${actionBean.userList}" var="user">
			<tr>
				<td><a href='${user}'>${user}</a></td>
			</tr>
		</c:forEach>
	</table>
	
</body>
</html>

