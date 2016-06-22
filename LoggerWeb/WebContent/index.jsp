<!DOCUENT html>
<html>
<head>
	<script src="js/jquery-3.0.0.min.js"></script>
	<script src="js/jquery-ui.min.js"></script>
	<link href="css/jquery-ui.css" rel="stylesheet">
	
<style>
* {
	padding : 0px;
	margin : 0px;
}

#wrapper {	margin : 0 auto;
	text-align : center;
}

textarea {
  width: 320px;
}

body{
	font: 62.5% "Trebuchet MS", sans-serif;
	margin: 50px;
}
.demoHeaders {
	margin-top: 2em;
}
#dialog-link {
	padding: .4em 1em .4em 20px;
	text-decoration: none;
	position: relative;
}
#indicate-icon {
	margin: 0 5px 0 0;
	position: absolute;
	left: .2em;
	top: 50%;
	margin-top: -8px;
}
#icons {
	margin: 0;
	padding: 0;
}
#icons li {
	margin: 2px;
	position: relative;
	padding: 4px 0;
	cursor: pointer;
	float: left;
	list-style: none;
}
#icons span.ui-icon {
	float: left;
	margin: 0 4px;
}
.fakewindowcontain .ui-widget-overlay {
	position: absolute;
}
select {
	width: 200px;
}
	

</style>
</head>
<body>

<script type="text/javascript">
$(document).ready(function(){
	var myUrl = '<%=request.getServerName()%>';
	var port = '<%=request.getAttribute("apachePort")%>';
	myUrl = myUrl + ':'+port;
	
	
	$("#init").button({
		icons: {
	        primary: "ui-icon-refresh"
	      }
	}).on("click", function(event){
		//alert('go UP');
		init();
	})

	$("#flush").button({
			icons: {
		        primary: "ui-icon-refresh"
		      }
	}).on("click", function(event){
		//alert('go UP');
		flush();
	})
	
	$("#zeroPos1").button({
			icons: {
		        primary: "ui-icon-refresh"
		      }
	}).on("click", function(event){
		//alert('go UP');
		zeroPos(1);
	})
	
	$("#zeroPos2").button({
			icons: {
		        primary: "ui-icon-refresh"
		      }
	}).on("click", function(event){
		//alert('go UP');
		zeroPos(2);
	})
	
	$("#moveStop").button({
		icons: {
	        primary: "ui-icon-pause"
	      },
	      text: false
	}).on("click", function(event){
		stopMotorAll();
	});
	
	$("#moveUp").button({
		icons: {
	        primary: "ui-icon-arrowthick-1-n"
	      },
	      text: false
	}).on("mousedown", function(event){
		moveInterval = setInterval(function(){
			move(2, -1);
		},200);
	}).on("mouseup", function(event){
		clearInterval(moveInterval);
	})
	
	$("#moveDown").button({
		icons: {
			primary: "ui-icon-arrowthick-1-s"
	      },
	      text: false
	}).on("mousedown", function(event){
		moveInterval = setInterval(function(){
			move(2, 1);
		},200);
	}).on("mouseup", function(event){
		clearInterval(moveInterval);
	})
	
	$("#moveLeft").button({
		icons: {
			primary: "ui-icon-arrowthick-1-w"
	      },
	      text: false
	}).on("mousedown", function(event){
		moveInterval = setInterval(function(){
			move(1, -1);
		},200);
	}).on("mouseup", function(event){
		clearInterval(moveInterval);
	})
	
	$("#moveRight").button({
		icons: {
			primary: "ui-icon-arrowthick-1-e"
	      },
	      text: false
	}).on("mousedown", function(event){
		moveInterval = setInterval(function(){
			move(1, 1);
		},200);
	}).on("mouseup", function(event){
		clearInterval(moveInterval);
	})
	
	$("#loggerStart").button({
		icons: {
			primary: "ui-icon-arrowthick-1-e"
	      }
	}).on("click", function(event){
		logger(true);
	})
	
	$("#loggerStop").button({
		icons: {
			primary: "ui-icon-arrowthick-1-e"
	      }
	}).on("click", function(event){
		logger(false);
	})
	
	//$("#emotimoLogArea").text("aa");
	
	function logger(isStart){
		if(isStart){
			loggerFlushInterval = setInterval(function(){
				loggerFlush();
			},500);	
			
			depthImageInterval = setInterval(function(){
				//alert('hi');
			    $("#imageDepth").attr("src", "http://"+myUrl+"/Logger/depth.jpg?"+new Date().getTime());
			},500);
			
			rgbImageInterval = setInterval(function(){
			    $("#imageRgb").attr("src", "http://"+myUrl+"/Logger/rgb.jpg?"+new Date().getTime());
			},500);
		}
		else{
			clearInterval(loggerFlushInterval);
			clearInterval(depthImageInterval);
			clearInterval(rgbImageInterval);
		}
		
		$.ajax({
			url: "logger.do",
		    data: {
      		    isStart: isStart,
		    },
		  
		  success: function( result ) {
			  updateLog(result, "#kinectLogArea");
		  }
		});
	}
	
	function loggerFlush(){
		$.ajax({
			url: "loggerFlush.do",
		   
		  
		   success: function( result ) {
				updateLog(result, "#kinectLogArea");
		  }
		});
	}
	
	function init(){
		$.ajax({
		  url: "init.do",
		  
		  success: function( result ) {
			  updateLog(result, "#emotimoLogArea");
		  }
		});
	}
	
	function flush(){
		$.ajax({
		  url: "flush.do",
		  
		  success: function( result ) {
			  updateLog(result, "#emotimoLogArea");
		  }
		});
	}
	
	function stopMotorAll(){
		$.ajax({
		  url: "stopMotorAll.do",
		  success: function( result ) {
			  updateLog(result, "#emotimoLogArea");
		  }
		});
	}
	
	function zeroPos(motor){
		$.ajax({
			url: "zeroPos.do",
		    data: {
      		    motor: motor,
		    },
		  
		  success: function( result ) {
			  updateLog(result, "#emotimoLogArea");
		  }
		});
	}
	
	function move(motor, direction){
		$.ajax({
		  url: "action.do",
		  data: {
		    motor: motor,
		    direction:direction
		  },
		  success: function( result ) {
			  updateLog(result, "#emotimoLogArea");
		  }
		});
	}
	
	function setPulse(motor, pulse){
		$.ajax({
		  url: "setPulse.do",
		  data: {
		    motor: motor,
		    pulse:pulse
		  },
		  success: function( result ) {
			  updateLog(result, "#emotimoLogArea");
		  }
		});
	}
	
	function setStep(step){
		$.ajax({
		  url: "setMotorStep.do",
		  data: {
		    step:step
		  },
		  success: function( result ) {
			  updateLog(result, "#emotimoLogArea");
		  }
		});
	}
	
	var isRGB2BGR = <%=request.getAttribute("isRGB2BGR")%>;
	function toggleRGB2BGR(){
		isRGB2BGR = !isRGB2BGR;
		$.ajax({
		  url: "loggerSettings.do",
		  data: {
		    isRGB2BGR:isRGB2BGR
		  },
		  success: function( result ) {
			  updateLog(result, "#kinectLogArea");
		  }
		});
	}
	
	var isUpsideDown = <%=request.getAttribute("isUpsideDown")%>;
	function toggleUpsideDown(){
		isUpsideDown = !isUpsideDown;
		$.ajax({
		  url: "loggerSettings.do",
		  data: {
			  isUpsideDown:isUpsideDown
		  },
		  success: function( result ) {
			  updateLog(result, "#kinectLogArea");
		  }
		});
	}
	
	function updateLog(result, areaId){
		$(areaId).text($(areaId).text()+result);
		$(areaId).animate({
		    scrollTop:$(areaId)[0].scrollHeight - $(areaId).height()}, 100);
	}
	
	$( "#move-step-slider" ).slider({
	      min: 100,
	      max: 3000,
	      value: <%=request.getAttribute("motorStep")%>,
	      slide: function( event, ui ) {
	        //$( "#amount" ).val( ui.value );
	        $("#move-step-value").val(ui.value);
	        setStep(ui.value);
	        //setPulse(1, ui.value);
	      }
	});
	
	$( "#move-step-value").val(<%=request.getAttribute("motorStep")%>);
	
	$( "#motor-1-pulse-slider" ).slider({
	      min: 100,
	      max: 3000,
	      value: <%=request.getAttribute("pulse1")%>,
	      slide: function( event, ui ) {
	        //$( "#amount" ).val( ui.value );
	        $("#motor-1-pulse").val(ui.value);
	        setPulse(1, ui.value);
	      }
	});
	
	$( "#motor-1-pulse-value").val(<%=request.getAttribute("pulse1")%>);
	
	$( "#motor-2-pulse-slider" ).slider({
	      min: 100,
	      max: 3000,
	      value: <%=request.getAttribute("pulse2")%>,
	      slide: function( event, ui ) {
	    	  $("#motor-2-pulse").val(ui.value);
	    	  setPulse(2, ui.value);
	        //$( "#amount" ).val( ui.value );
	      }
	});
	
	$( "#motor-2-pulse-value").val(<%=request.getAttribute("pulse2")%>);
	
	$("#is-serial-init-indicator").button({
		icons: {
			primary: "ui-icon-notice"
	      },
	    text: false
	});
	
	createButtonWithIcon("is-rgb2bgr-indicator", isRGB2BGR, toggleRGB2BGR, true);
	createButtonWithIcon("is-upsidedown-indicator", isUpsideDown, toggleUpsideDown, true);
	
	function createButtonWithIcon(id, isEnabled, fun, recur){
		if(isEnabled) icon = "ui-icon-circle-check";
		else icon = "ui-icon-circle-close";
		
		$("#"+id).button();
		$("#"+id).button({
			icons: {
				primary: icon//"ui-icon-circle-check"
		      },
		    text: false
		}).on("click", function(){
			createButtonWithIcon(id, !isEnabled, fun, false);
			if(recur){
				fun();
			}
			
		});
	}
})

</script>

<div id="wrapper">
	<H1>Logger Controlling System</H1>
	contact info: <a href="mailto:minhaenl@ics.uci.edu" target="_top">minhaenl_at_ics_dot_uci_dot_edu</a><br>
	Home: <%=System.getProperty("user.home")%><br>
	<a href="instruction.do">Install Instruction</a><br>
	
	
	
	
	<div align="center">
	<table>
		<tr>
			<td colspan="2">
				<label style="width:100%">Init: </label><button id="is-serial-init-indicator">O</button>,
				<button id="init">Init</button>
				<button id="flush">flush</button>
				<button id="zeroPos1">zero Pos 1</button>
				<button id="zeroPos2" >zero Pos 2</button><br>		
			</td>
		</tr>
		<tr>
			<td>
				<label>Step: </label><input type="text" id="move-step-value" readonly style="border:0; color:#f6931f; font-weight:bold;" value="500"><div id="move-step-slider"></div>
			</td>
			<td align="right">
				<button id="moveStop">Stop</button>
				<button id="moveUp">up</button>
				<button id="moveDown">down</button>
				<button id="moveLeft">left</button>
				<button id="moveRight">right</button>
			</td>
		</tr>
		<tr>
			<td>
			<label>RGB2BGR: </label><button id="is-rgb2bgr-indicator">1</button>
	
			<label>UpsideDown: </label><button id="is-upsidedown-indicator">1</button>
			</td>
			<td>
			<button id="loggerStart">LoggerStart</button><button id="loggerStop">LoggerStop</button>
			</td>
		</tr>

		
		<tr>
			<td>
				<label>Pulse1: </label><input type="text" id="motor-1-pulse-value" readonly style="border:0; color:#f6931f; font-weight:bold;" value="500"><div id="motor-1-pulse-slider"></div>
			</td>
			<td>
				<label>Pulse2: </label><input type="text" id="motor-2-pulse-value" readonly style="border:0; color:#f6931f; font-weight:bold;" value="500"><div id="motor-2-pulse-slider"></div>
			</td>
		</tr>
		<tr>
			<td>
				<p>Depth</p>
			</td>
			<td>
				<p>RGB</p>
			</td>
		</tr>
		<tr>
			<td width="322"><img id="imageDepth" alt="" src="" width="320" height="240" border="1"></td>
			<td width="322"><img id="imageRgb" alt="" src="" width="320" height="240" border="1"> <br></td>
		</tr>
		<tr>
			<td>
				<p>Motor Log</p>
			</td>
			<td>
				<p>Camera Log</p>
			</td>
		</tr>
		<tr>
			<td>
				<textarea id="emotimoLogArea" rows="10"></textarea>
			</td>
			<td><textarea id="kinectLogArea" rows="10" cols="37"></textarea></td>
		</tr>
		
	</table>
	</div>
	
</div>
</body>
</html>