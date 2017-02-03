<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
  	<title>Scomer Service</title>
  	<link href="style.css" rel="stylesheet" type="text/css">
  </head>
  <body>
    <div class="description">Scomer Service Login</div>
    <br><br><br>
	<a href='${actionBean.authLink}'>Sign in with Twitter</a>
	<br>
	<a href='${actionBean.instagramLink}'>Sign in with Instagram</a>
	<br>
	<a href='${actionBean.facebookLink}'>Sign in with Facebook</a>
	<br>
	<a href='${actionBean.googleLink}'>Sign in with Google</a>
  </body>
</html>