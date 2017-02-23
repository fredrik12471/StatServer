<!-- %@ page contentType="text/html;charset=UTF-8" language="java"% -->
<%@ taglib prefix="stripes"
	uri="http://stripes.sourceforge.net/stripes.tld"%>

<!--  !DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" -->
<html>
<head>
<title>NonStopSquid.se</title>
<link href="/style.css" rel="stylesheet" type="text/css">
<script src="/Chart.js"></script>
</head>
<body>
<div class="top">
<div class="cartoon"><img src="/squid-51x38.jpg" class="iconDetails"/><div class="text">NonStopSquid.se - Social Statistics!</div></div>
</div>
<div class="background-header">
<div class="header">
    <!-- div class="image"><img src="http://placehold.it/64x64"/></div -->
    <div class="image"><img src="${actionBean.twitterUser.profileImageURL}"/></div>
    <div class="text1">${actionBean.twitterUser.name} - @${actionBean.twitterUser.screenName}</div>
    <div class="text2">${actionBean.twitterUser.description}</div>
</div>
</div>
<div class="main">
	<br><br>
	<br><br>
	You have tweeted ${actionBean.twitterUser.statusesCount} tweets since ${actionBean.twitterUser.createdAt}
	<br><br>
	You have ${actionBean.twitterUser.followersCount} followers and ${actionBean.twitterUser.friendsCount} friends
	<br><br>
	</div>
	<div class="line">&nbsp;</div>
		<br><br>
	<div class="canvas-div">
	<canvas id=myChart1 class="canvas1"></canvas>
	<canvas id=myChart2 class="canvas2"></canvas>
	</div>
		<div class="line">&nbsp;</div>
		<div class="main">
	<script>
	var ctx1 = document.getElementById("myChart1").getContext("2d");
	ctx1.canvas.width = 200;
	ctx1.canvas.height = 200;

var data1 = {
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
	            
	        }
	    ]
	};
var myLineChart1 = new Chart(ctx1, {
    type: 'line',
    data: data1,
    options: {
        responsive: false
    }
});



var ctx2 = document.getElementById("myChart2").getContext("2d");
ctx2.canvas.width = 200;
ctx2.canvas.height = 200;

var data2 = {
	labels: [${actionBean.friendData}],
    datasets: [
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
var myLineChart2 = new Chart(ctx2, {
type: 'line',
data: data2,
options: {
    responsive: false
}
});

</script>
	<br><br>
<stripes:link href="/twitter" event="downloadCsvFile" >Download spreadsheet with information about all your followers
<stripes:param name="userId" value="${actionBean.twitterUser.id}"/>
</stripes:link>
</div>
</body>
</html>

