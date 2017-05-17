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
<div class="flex-grid">
<div class="top">
<div class="cartoon"><img src="/squid-51x38.jpg" class="iconDetails"/><div class="text">NonStopSquid.se - Social Statistics!</div></div>
</div>
<div class="background-header">
<div class="header">
    <!-- div class="image"><img src="http://placehold.it/64x64"/></div -->

    <div class="image"><img src="${actionBean.twitterUser.profileImageURL}" border="6px solid white" /></div>
    <div class="text1">${actionBean.twitterUser.name} - @${actionBean.twitterUser.screenName}</div>
    <div class="text2">${actionBean.twitterUser.description}</div>
</div>
</div>
<div class="white-header">&nbsp;
</div>
<div class="info">
Data collected ${actionBean.dataDate}
</div>
<div class="main">
	<br><br>
	<br><br>
	<div class="account-info-col-1">
	Account started:<br>
	Tweets:<br>
	Followers:<br>
	Friends:<br>
	</div>
	<div class="account-info-col-2">
	${actionBean.twitterUser.createdAt}<br>
	${actionBean.twitterUser.statusesCount}<br>
	${actionBean.twitterUser.followersCount}<br>
	${actionBean.twitterUser.friendsCount}<br>
	</div>

	</div>
	<div class="line">&nbsp;</div>
		<br>
	<div class="canvas-div">
	<div class="canvas1"><canvas id=myChart1></canvas></div>
	<div class="canvas2"><canvas id=myChart2></canvas></div>

	</div>
			<br><br>
		<div class="line">&nbsp;</div>
		<div class="linktext">
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
</div>
</body>
</html>

