<!-- %@ page contentType="text/html;charset=UTF-8" language="java"% -->
<%@ taglib prefix="stripes"
	uri="http://stripes.sourceforge.net/stripes.tld"%>

<!--  !DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" -->
<html>
<head>
<title>Scomer Service</title>
<link href="style.css" rel="stylesheet" type="text/css">
<script src="Chart.js"></script>
</head>
<body>
<div class="header">
    <div class="image"><img src="http://placehold.it/64x64"/></div>
    <div class="text1">Header Text</div>
    <div class="text2">Header Text</div>
</div>
	<div class="description">Scomer Service - Twitter</div>
	Welcome ${actionBean.twitterUser.name} - @${actionBean.twitterUser.screenName}!
	<br>
	${actionBean.twitterUser.description}
	<br><br>
	You have tweeted ${actionBean.twitterUser.statusesCount} tweets since ${actionBean.twitterUser.createdAt}
	<br><br>
	You have ${actionBean.twitterUser.followersCount} followers and ${actionBean.twitterUser.friendsCount} friends
	<br><br>
	<canvas id=myChart></canvas>
	<script>
	var ctx = document.getElementById("myChart").getContext("2d");
	ctx.canvas.width = 200;
	ctx.canvas.height = 200;

var data = {
		labels: [${actionBean.followerData}],
	    datasets: [
	        {
	    	    labels: [${actionBean.followerData}],
	            label: "Followers",
	            fill: false,
	            lineTension: 0.1,
	            backgroundColor: "rgba(75,192,192,0.4)",
	            borderColor: "rgba(75,192,192,1)",
	            borderCapStyle: 'butt',
	            borderDash: [],
	            borderDashOffset: 0.0,
	            borderJoinStyle: 'miter',
	            pointBorderColor: "rgba(75,192,192,1)",
	            pointBackgroundColor: "#fff",
	            pointBorderWidth: 1,
	            pointHoverRadius: 5,
	            pointHoverBackgroundColor: "rgba(75,192,192,1)",
	            pointHoverBorderColor: "rgba(220,220,220,1)",
	            pointHoverBorderWidth: 2,
	            pointRadius: 1,
	            pointHitRadius: 10,
	            data: [${actionBean.followerData}],
	            spanGaps: false,
	            
	        },
	        {
	    	    labels: [${actionBean.friendData}],
	            label: "Friends",
	            fill: false,
	            lineTension: 0.1,
	            backgroundColor: "rgba(75,192,75,0.4)",
	            borderColor: "rgba(75,192,75,1)",
	            borderCapStyle: 'butt',
	            borderDash: [],
	            borderDashOffset: 0.0,
	            borderJoinStyle: 'miter',
	            pointBorderColor: "rgba(75,192,75,1)",
	            pointBackgroundColor: "#fff",
	            pointBorderWidth: 1,
	            pointHoverRadius: 5,
	            pointHoverBackgroundColor: "rgba(75,192,75,1)",
	            pointHoverBorderColor: "rgba(220,220,220,1)",
	            pointHoverBorderWidth: 2,
	            pointRadius: 1,
	            pointHitRadius: 10,
	            data: [${actionBean.friendData}],
	            spanGaps: false,
	            
	        }
	    ]
	};
var myLineChart = new Chart(ctx, {
    type: 'line',
    data: data,
    options: {
        responsive: false
    }
});

</script>
<stripes:link href="/twitter" event="downloadCsvFile" >Download spreadsheet with information about all your followers
<stripes:param name="userId" value="${actionBean.twitterUser.id}"/>
</stripes:link>
</body>
</html>

