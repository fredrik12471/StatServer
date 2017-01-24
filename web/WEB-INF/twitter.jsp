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
	<h1>Scomer Service - Twitter</h1>
	Welcome ${actionBean.twitterUser.name} - @${actionBean.twitterUser.screenName}!
	<br>
	${actionBean.twitterUser.description}
	<br><br>
	You have tweeted ${actionBean.twitterUser.statusesCount} tweets since ${actionBean.twitterUser.createdAt}
	
	<canvas id="myChart" width="40" height="40"></canvas>
	<script>
var ctx = document.getElementById("myChart");
var data = {
	    labels: ["January", "February", "March", "April", "May", "June", "July"],
	    datasets: [
	        {
	            label: "My First dataset",
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
	            data: [${actionBean.data}],
	            spanGaps: false,
	        }
	    ]
	};
var myLineChart = new Chart(ctx, {
    type: 'line',
    data: data
});

</script>
</body>
</html>

