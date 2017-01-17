<!-- %@ page contentType="text/html;charset=UTF-8" language="java"% -->
<%@ taglib prefix="stripes"
	uri="http://stripes.sourceforge.net/stripes.tld"%>

<!--  !DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" -->
<html>
<head>
<title>Scomer Service</title>
<link href="style.css" rel="stylesheet" type="text/css">
<script>
	function addOption() {
		text = document.getElementById('retweetKeyword');
		select = document.getElementById('retweetKeywords');
		var opt = document.createElement('option');
		opt.value = text.value;
		opt.innerHTML = text.value;
		select.appendChild(opt);
		text.value = '';
		return true;
	}

	function removeOption() {
		select = document.getElementById('retweetKeywords');
		value = select.selectedIndex;
		select.removeChild(select[value]);
		return true;
	}

	function selectAll() {
		select = document.getElementById('retweetKeywords');
		for (i = 0; i < select.options.length; i++) {

			select.options[i].selected = "selected";

		}
	}
</script>
</head>
<body>
	<h1>Scomer Service - Twitter</h1>
	Welcome ${actionBean.accountName}!
	<br></br>
	<!--     <br></br> -->
	<!--     <br></br> -->
	<!--     Available services: -->

	<stripes:form beanclass="se.sthlm.jfw.mainServlet.ConsumerActionBean"
		focus="">
		<table border="1">
			<tr>
				<td><stripes:checkbox name="retweet"
						checked="${actionBean.retweet}" value="true" /></td>
				<td colspan="6">Retweet tweets with specified keyword!</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>Keyword:</td>
				<td><stripes:text id="retweetKeyword" name="retweetKeyword"
						value="${actionBean.retweetKeyword}" /></td>
				<td><stripes:button name="add" value="Add keyword"
						onclick="addOption()" /></td>
				<td><stripes:select id="retweetKeywords" name="retweetKeywords"
						multiple="true" size="4">
						<stripes:options-collection
							collection="${actionBean.retweetKeywords}" />
					</stripes:select></td>
				<td><stripes:button name="remove" value="Remove keyword"
						onclick="removeOption()" /></td>
				<td><stripes:select id="retweetKeywordsTiming"
						name="retweetKeywordsTiming">
						<stripes:option value="3">Every 15 minute</stripes:option>
						<stripes:option value="6">Every half hour</stripes:option>
						<stripes:option value="12">Every hour</stripes:option>
					</stripes:select></td>
			</tr>
			<!--         <tr> -->
			<%--         	<td><stripes:checkbox name="${actionBean.receiveDMOnUnfollow}" value="${actionBean.receiveDMOnUnfollow}" /></td> --%>
			<!--             <td>Receive DM when someone unfollows.</td> -->
			<!--             <td></td> -->
			<!--             <td></td> -->
			<!--         </tr> -->
			<tr>
				<td colspan="2">&nbsp;</td>
				<td><stripes:text id="keyword1" name="keyword" value="keyword" readonly="true" class="noborder"/></td>
				<td colspan="4"><img src="delete.gif" onclick=""></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td colspan="6"><stripes:submit name="save" value="Save"
						onclick="selectAll()" /> <stripes:submit name="logout"
						value="Logout" /></td>
			</tr>
		</table>
	</stripes:form>

</body>
</html>

