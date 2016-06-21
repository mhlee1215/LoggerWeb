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
</style>
</head>
<body>

<script type="text/javascript">
$(document).ready(function(){
	var myUrl = '<%=request.getServerName()%>';
	var port = '<%=request.getAttribute("apachePort")%>';
	myUrl = myUrl + ':'+port;
	
	setInterval(function(){
		//alert('hi');
	    $("#imageDepth").attr("src", "http://"+myUrl+"/Logger/depth.jpg?"+new Date().getTime());
	},500);
	
	setInterval(function(){
	    $("#imageRgb").attr("src", "http://"+myUrl+"/Logger/rgb.jpg?"+new Date().getTime());
	},500);
	
	
	
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
	
	$("#moveUp").button({
		icons: {
	        primary: "ui-icon-arrowthick-1-n"
	      }
	}).on("click", function(event){
		//alert('go UP');
		move(2, 1);
	})
	
	$("#moveDown").button({
		icons: {
			primary: "ui-icon-arrowthick-1-s"
	      }
	}).on("click", function(event){
		move(2, -1);
	})
	
	$("#moveLeft").button({
		icons: {
			primary: "ui-icon-arrowthick-1-w"
	      }
	}).on("click", function(event){
		//alert('go UP');
		move(1, 1);
	})
	
	$("#moveRight").button({
		icons: {
			primary: "ui-icon-arrowthick-1-e"
	      }
	}).on("click", function(event){
		move(1, -1);
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
		}
		else{
			clearInterval(loggerFlushInterval);
		}
		
		$.ajax({
			url: "logger.do",
		    data: {
      		    isStart: isStart,
		    },
		  
		  success: function( result ) {
		    //alert(result);
			$("#kinectLogArea").text($("#kinectLogArea").text()+"\n"+result);
			$("#kinectLogArea").animate({
			    scrollTop:$("#kinectLogArea")[0].scrollHeight - $("#kinectLogArea").height()}, 100);
		  }
		});
	}
	
	function loggerFlush(){
		$.ajax({
			url: "loggerFlush.do",
		   
		  
		   success: function( result ) {
		    //alert(result);
			$("#kinectLogArea").text($("#kinectLogArea").text()+result);
			$("#kinectLogArea").animate({
			    scrollTop:$("#kinectLogArea")[0].scrollHeight - $("#kinectLogArea").height()}, 100);
		  }
		});
	}
	
	function init(){
		$.ajax({
		  url: "init.do",
		  
		  success: function( result ) {
		    //alert(result);
			$("#emotimoLogArea").text($("#emotimoLogArea").text()+result);
			$("#emotimoLogArea").animate({
			    scrollTop:$("#emotimoLogArea")[0].scrollHeight - $("#emotimoLogArea").height()}, 100);
		  }
		});
	}
	
	function flush(){
		$.ajax({
		  url: "flush.do",
		  
		  success: function( result ) {
		    //alert(result);
			$("#emotimoLogArea").text($("#emotimoLogArea").text()+result);
			$("#emotimoLogArea").animate({
			    scrollTop:$("#emotimoLogArea")[0].scrollHeight - $("#emotimoLogArea").height()}, 100);
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
		    //alert(result);
			$("#emotimoLogArea").text($("#emotimoLogArea").text()+result);
			$("#emotimoLogArea").animate({
			    scrollTop:$("#emotimoLogArea")[0].scrollHeight - $("#emotimoLogArea").height()}, 100);
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
		    //alert(result);
			$("#emotimoLogArea").text($("#emotimoLogArea").text()+result);
			$("#emotimoLogArea").animate({
			    scrollTop:$("#emotimoLogArea")[0].scrollHeight - $("#emotimoLogArea").height()}, 100);
		  }
		});
	}
	
	
})

</script>

<div id="wrapper">
	Home: <%=System.getProperty("user.home")%><br>
	<button id="init">Init</button><button id="flush">flush</button><button id="zeroPos1">zero Pos 1</button><button id="zeroPos2">zero Pos 2</button><br>
	<button id="moveUp">up</button>
	<button id="moveDown">down</button><br>
	<button id="moveLeft">left</button>
	<button id="moveRight">right</button><br>
	<button id="loggerStart">LoggerStart</button><button id="loggerStop">LoggerStop</button><br>
	
	
	
	<div align="center">
	<table>
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