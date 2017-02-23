<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="stripes"
	uri="http://stripes.sourceforge.net/stripes.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<title>NonStopSquid.se</title>
<link href="/style.css" rel="stylesheet" type="text/css">
	<script src="/Chart.js"></script>
</head>
<body>
	<div class="box">
		<div class="presentation"><br><br><br><br><br><br><br><br><br>
			<div class="description">
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do
				eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim
				ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut
				aliquip ex ea commodo consequat." <br><br><br>
							<table>
								<tr>
									<td><a href='${actionBean.authLink}'><button class="button">Sign in with
											Twitter</button></a></td>
									<td><a href='${actionBean.instagramLink}'><button class="button">Sign in with
											Instagram</button></a></td>
									<td><a href='${actionBean.facebookLink}'><button class="button">Sign in with
											Facebook</button></a></td>
									<td><a href='${actionBean.googleLink}'><button class="button">Sign in with
											Google</button></a></td>
								</tr>
							</table>
			</div>
		</div>
	</div>
</body>
</html>