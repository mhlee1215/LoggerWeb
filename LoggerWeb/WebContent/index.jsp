<!DOCUENT html>
<html>
<head>
	<script src="js/jquery-3.0.0.min.js"></script>
	<script src="js/jquery-ui.min.js"></script>
	<script src="js/jquery.numeric.min.js"></script>
	<script src="js/jquery.isloading.min.js"></script>
	<link href="css/jquery-ui.css" rel="stylesheet">
	<link href="css/jquery.isloading.css" rel="stylesheet">
	
	<link href="http://netdna.bootstrapcdn.com/font-awesome/3.0.2/css/font-awesome.css" rel="stylesheet">	
	
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
	
.ui-widget{font-size:12px;}

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
		//updateMotorPos(1, 0);
		zeroPos(1);
	})
	
	$("#zeroPos2").button({
			icons: {
		        primary: "ui-icon-refresh"
		      }
	}).on("click", function(event){
		//alert('go UP');
		//updateMotorPos(2, 0);
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
	
	$("#plannedRecord").button({
		icons: {
			primary: "ui-icon-arrowthick-1-e"
	      }
		
	}).on("click", function(event){
		motorLoggerFlushInterval = setInterval(function(){
			updateMotorLog();
		},500);	
		//updateLoggerInfo(true);
		var movePlan = $( "#movePlan" ).val();
		goPlannedRecordingInit(1, movePlan);
		
		
		
	})
	
	
	//$("#emotimoLogArea").text("aa");
	
	function goPlannedRecordingInit(index, movePlan){
		goPlannedRecording(1, movePlan);
		/*
		$.ajax({
			url: "goToOrigin.do",
		  	success: function( result ) {
		  		//logger(true);
		  		updateLoggerInfo(true);
				var movePlan = $( "#movePlan" ).val();
				goPlannedRecording(1, movePlan);
		  }
		});
		*/
	}
	
	function goPlannedRecording(index, movePlan){
		$("#plannedRecord").button("disable");
		$("#logInterval").attr('disabled', 'diabled');
		$("#logTimes").attr('disabled', 'diabled');
		$("#movePlan").attr('disabled', 'diabled');
		updateLoggerInfo(true);
		$.ajax({
			url: "doPlannedLogging.do",
		    data: {
		    	movePlan:movePlan,
		    	index: index, 
		    	logInterval:$("#logInterval").val(),
		    	logTimes:$("#logTimes").val(),
		    	logPrefix:$("#logPrefix").val(),
		    },
		    
		    
		  
		  success: function( result ) {
			  //updateLog(result, "#kinectLogArea");
			  clearInterval(motorLoggerFlushInterval);
			  //updateLoggerInfo(false);
			  updateLoggerInfo(false);
			  $("#plannedRecord").button("enable");
			  $("#logInterval").removeAttr('disabled', 'diabled');
			  $("#logTimes").removeAttr('disabled', 'diabled');
			  $("#movePlan").removeAttr('disabled', 'diabled');
				
		  }
		});
	}
	
	
	
	
	function logger(isStart){
		if(isStart)
			updateLoggerInfo(isStart);
		
		if(!isStart){
			transferFinishedCallback();
		}
		
		$.ajax({
			url: "logger.do",
		    data: {
      		    isStart: isStart,
		    },
		  
		  success: function( result ) {
			  updateLog(result, "#kinectLogArea");
			  
			  if(!isStart)
					updateLoggerInfo(isStart);
		  }
		});
	}
	
	function updateLoggerInfo(isStart){
		if(isStart){
			motorLoggerFlushInterval = setInterval(function(){
				updateMotorLog();
			},500);
			
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
			
			$("#loggerStart").button("disable");
		}
		else{
			//clearInterval(loggerFlushInterval);
			
			clearInterval(depthImageInterval);
			clearInterval(rgbImageInterval);
			clearInterval(motorLoggerFlushInterval);
			
			//$("#loggerStart").button("enable");
		}
	}
	
	function transferFinishedCallback(){
		$("#loggerPanel").isLoading({ text: "Transfering..",position:   "overlay" });
		//$("#loggerPanel").removeClass("alert-success");
		$.ajax({
			url: "loggerWaitUntilTransfer.do",
		  
		   success: function( result ) {
			   //refreshing log info stopped.
			   //updateLoggerInfo(false);
			   //updateLog(result, "#kinectLogArea");
			   $("#loggerStart").button("enable");
			   //clearInterval(loggerFlushInterval);
			   $("#loggerPanel").isLoading( "hide" );
			   //$("#loggerPanel").addClass("alert-success");
		  }
		});
	}
	
	
	
	function loggerInit(){
		updateLoggerInfo(true);
		$("#loggerStart").button("disable");
		$("#loggerStop").button("disable");		
		$("#loggerPanel").isLoading({ text: "Initializing..",position:   "overlay" });
		$.ajax({
			url: "initLogger.do",
		   success: function( result ) {
			   $("#loggerStart").button("enable");
			   $("#loggerStop").button("enable");
			   updateLog(result, "#kinectLogArea");
			   $("#loggerPanel").isLoading( "hide" );
			   updateLoggerInfo(false);
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
			  if(result == 'success'){
				  isInitialized = true;
			  }else{
				  isInitialized = false;
			  }
			  
			  if(isInitialized) initIcon = "ui-icon-check";
			  else initIcon = "ui-icon-notice"; 
			  createIndicatorWithIcon("is-serial-init-indicator", initIcon);
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
			  updateMotorPos(motor, 0);
			  updateLog(result, "#emotimoLogArea");
		  }
		});
	}
	
	function moveTo(motor, pos){
		$.ajax({
			  url: "action.do",
			  data: {
			    motor: motor,
			    pos:pos
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
			  updateMotorPosFromServer(motor);
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
	      max: 20000,
	      value: <%=request.getAttribute("pulse1")%>,
	      slide: function( event, ui ) {
	        //$( "#amount" ).val( ui.value );
	        $("#motor-1-pulse-value").val(ui.value);
	        setPulse(1, ui.value);
	      }
	});
	
	$( "#motor-1-pulse-value").val(<%=request.getAttribute("pulse1")%>);
	
	$( "#motor-2-pulse-slider" ).slider({
	      min: 100,
	      max: 20000,
	      value: <%=request.getAttribute("pulse2")%>,
	      slide: function( event, ui ) {
	    	  $("#motor-2-pulse-value").val(ui.value);
	    	  setPulse(2, ui.value);
	        //$( "#amount" ).val( ui.value );
	      }
	});
	
	$( "#motor-2-pulse-value").val(<%=request.getAttribute("pulse2")%>);
	
	
	var isInitialized = <%=request.getAttribute("isInitialized")%>;
	var initIcon = '';
	if(isInitialized) initIcon = "ui-icon-check";
	else initIcon = "ui-icon-notice";
	
	createIndicatorWithIcon("is-serial-init-indicator", initIcon);

	
	var motor1_pos = <%=request.getAttribute("motorPos1")%>;
	var motor2_pos = <%=request.getAttribute("motorPos2")%>;
	
	$( "#motor-1-pos-slider" ).slider({
      orientation: "horizontal",
      min: -20000,
      max: 20000,
      value: motor1_pos,
      animate: true,
      slide: function( event, ui ) {
    	  moveTo(1, ui.value);
    	  updateMotorPos(1, ui.value);
      }
    });
	
	$( "#motor-2-pos-slider" ).slider({
	      orientation: "vertical",
	      min: -10000,
	      max: 10000,
	      value: motor2_pos,
	      animate: true,
	      slide: function( event, ui ) {
	    	  moveTo(2, ui.value);
	    	  updateMotorPos(2, ui.value);
	      }
	    });
	
	createButtonWithIcon("is-rgb2bgr-indicator", isRGB2BGR, toggleRGB2BGR, true);
	createButtonWithIcon("is-upsidedown-indicator", isUpsideDown, toggleUpsideDown, true);
	
	function updateMotorLog(){
		$.ajax({
		  url: "eMotimoflush.do",
		  success: function( result ) {
			  updateLog(result, "#emotimoLogArea");
		  }
		});
	}
	
	function updateMotorPosFromServer(motor){
		$.ajax({
		  url: "getMotorPos.do",
		  data: {
			  motor:motor
		  },
		  success: function( result ) {
			  updateMotorPos(motor, result);
		  }
		});
	}
	
	function createIndicatorWithIcon(id, icon){
		$("#"+id).button({
			icons: {
				primary: icon//"ui-icon-circle-check"
		      },
		    text: false
		});
	}
	
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
	
	updateMotorPos(1, motor1_pos);
	updateMotorPos(2, motor2_pos);
	function updateMotorPos(motor, pos){
		$("#motor-"+motor+"-pos-value").val(pos);
		//$("#motor-"+motor+"-pos-slider").val(pos);
		if(motor == 2) pos = -pos;
		$("#motor-"+motor+"-pos-slider").slider('value',pos);
		//alert(motor+' '+pos);
	}
	
	

	//$('#loggerStart').button("disable");
	//$("#loggerStart").prop('disabled', false);
	//   $("#loggerStop").prop('disabled', false);
	
	$( "#controlTabs" ).tabs();
	$("#controlTabs" ).tabs({ active: 1 });
		
	
	$("#logInterval").numeric();
	$("#logTimes").numeric();
	
	
	if(<%=request.getAttribute("isTransferProgress")%> == true){
		transferFinishedCallback();
	}
	
	if(<%=request.getAttribute("isPlannedLogProgress")%> == true){
		$("#plannedRecord").button("disable");
		
		$("#movePlan").attr('disabled', 'diabled');
		$("#logInterval").attr('disabled', 'diabled');
		$("#logTimes").attr('disabled', 'diabled');
		var logTimes = <%=request.getAttribute("logTimes")%>;
		var logCurTimes = <%=request.getAttribute("logCurTimes")%>+1;
		$("#planned-reps-value").val(logCurTimes+"/"+logTimes);
		$("#loggerStart").button("disable");
		updateLoggerInfo(true);
	}
	

	if(!<%=request.getAttribute("isLoggerInitialized")%>){
		loggerInit();
	}
	
	if(<%=request.getAttribute("isLoggerStarted")%> == true){
		$("#loggerStart").button("disable");
		updateLoggerInfo(true);
   }
	
	
	//$("#plannedRecord").text("");
	//logCurTimes
	//$("#loggerPanel").isLoading({ text: "Loading",position:   "overlay" });
	//$("#loggerPanel").removeClass("alert-success");
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
			<td align="center">
				<label>V-Motor-pos: </label><input type="text" id="motor-2-pos-value" readonly style="width:50px;border:0; color:#f6931f; font-weight:bold;" value="0">
			</td>
			<td align="center">
				<label>H-Motor-pos: </label><input type="text" id="motor-1-pos-value" readonly style="width:50px;border:0; color:#f6931f; font-weight:bold;" value="0">
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<div id="controlTabs">
					<ul>
					    <li><a href="#controlTabs-1">Manual</a></li>
					    <li><a href="#controlTabs-2">Planned</a></li>
					</ul>
					<div id="controlTabs-1">
						<table>
							<tr>
								<td>
									<label>Step: </label><input type="text" id="move-step-value" readonly style="width:250px;border:0; color:#f6931f; font-weight:bold;" value="500">
									<div id="move-step-slider"></div>
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
								<td colspan="2" align="center">
									
									<div id="motor-1-pos-slider"></div>
								</td>
							</tr>
							<tr>
								<td colspan="2" align="center">
									<div id="motor-2-pos-slider"></div>
								</td>
							</tr>
						</table>
					</div>
					<div id="controlTabs-2">
						<label for="tags">Log Prefix: </label><input id="logPrefix" style="width:100px;" value="<%=request.getAttribute("logPrefix")%>">
						<label for="tags">Move Plan: </label><input id="movePlan" style="width:300px;" value="<%=request.getAttribute("movePlan")%>"><br>
						<label for="tags">Interval (min): </label>
  						<input id="logInterval" style="width:30px;" value="<%=request.getAttribute("logInterval")%>">
  						<label for="tags">Times (0=infinite): </label>
  						<input id="logTimes" style="width:30px;" value="<%=request.getAttribute("logTimes")%>">
  						<label>Reps: </label><input type="text" id="planned-reps-value" readonly style="width:50px;border:0; color:#f6931f; font-weight:bold;" value="0">
  						<button id="plannedRecord">Start Planned Record</button>
					</div>
				</div>
			</td>
		</tr>
		
		<tr>
			<td colspan="2">
				<div id="loggerPanel">
					<table>
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
					</table>
				</div>	
			</td>
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