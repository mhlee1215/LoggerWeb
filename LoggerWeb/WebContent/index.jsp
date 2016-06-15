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
</style>
</head>
<body>

<script type="text/javascript">
$(document).ready(function(){
	var myUrl = '<%=request.getServerName()%>';//"128.195.57.180";
	setInterval(function(){
		//alert('hi');
	    $("#imageDepth").attr("src", "http://"+myUrl+"/Logger/depth.jpg?"+new Date().getTime());
	},500);
	
	setInterval(function(){
	    $("#imageRgb").attr("src", "http://"+myUrl+"/Logger/rgb.jpg?"+new Date().getTime());
	},500);
	
	
	$("#flush").button({
			icons: {
		        primary: "ui-icon-refresh"
		      }
	}).on("click", function(event){
		//alert('go UP');
		flush();
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
	
	//$("#logArea").text("aa");
	
	
	function flush(){
		$.ajax({
		  url: "flush.do",
		  
		  success: function( result ) {
		    //alert(result);
			$("#logArea").text($("#logArea").text()+"\n"+result);
			$("#logArea").animate({
			    scrollTop:$("#logArea")[0].scrollHeight - $("#logArea").height()}, 100);
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
			$("#logArea").text($("#logArea").text()+"\n"+result);
			$("#logArea").animate({
			    scrollTop:$("#logArea")[0].scrollHeight - $("#logArea").height()}, 100);
		  }
		});
	}
})

</script>

<div id="wrapper">
	<button id="flush">flush</button><br>
	<button id="moveUp">up</button>
	<button id="moveDown">down</button><br>
	<button id="moveLeft">left</button>
	<button id="moveRight">right</button><br>
	
	<img id="imageDepth" alt="" src="" width="320" height="240" border="1">
	<img id="imageRgb" alt="" src="" width="320" height="240" border="1"> <br>
	<textarea id="logArea" rows="10" cols="75"></textarea>
</div>
</body>
</html>