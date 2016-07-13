<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
 	<link rel="stylesheet" href="css/style.css">
 	<link rel="stylesheet" href="css/modal.css">
 	<script src="js/jquery-3.0.0.min.js"></script>
 	
 	<style>
 	
 	</style>
 	<script type="text/javascript">
 	
 	var modal = (function(){
		var 
		method = {},
		$overlay,
		$modal,
		$content,
		$close;

		// Center the modal in the viewport
		method.center = function () {
			var top, left;

			top = Math.max($(window).height() - $modal.outerHeight(), 0) / 2;
			left = Math.max($(window).width() - $modal.outerWidth(), 0) / 2;

			$modal.css({
				top:top + $(window).scrollTop(), 
				left:left + $(window).scrollLeft()
			});
		};

		// Open the modal
		method.open = function (settings) {
			$content.empty().append(settings.content);

			$modal.css({
				width: settings.width || 'auto', 
				height: settings.height || 'auto'
			});

			method.center();
			$(window).bind('resize.modal', method.center);
			$modal.show();
			$overlay.show();
		};

		// Close the modal
		method.close = function () {
			$modal.hide();
			$overlay.hide();
			$content.empty();
			$(window).unbind('resize.modal');
		};

		// Generate the HTML and add it to the document
		$overlay = $('<div id="overlay"></div>');
		$modal = $('<div id="modal"></div>');
		$content = $('<div id="content"></div>');
		$close = $('<a id="close" href="#">close</a>');

		$modal.hide();
		$overlay.hide();
		$modal.append($content, $close);

		$(document).ready(function(){
			$('body').append($overlay, $modal);						
		});

		$close.click(function(e){
			e.preventDefault();
			method.close();
		});

		return method;
	}());

	// Wait until the DOM has loaded before querying the document
	$(document).ready(function(){

		$.get('index.html', function(data){
			modal.open({content: data});
		});
		

		$('a#howdy').click(function(e){
			modal.open({content: "Hows it going?"});
			e.preventDefault();
		});
	});
	
	function openModal(url){
		$.get(url, function(data){
			modal.open({content: data});
		});
	}

 	
	
 	</script>
</head>
<body>
<div id="wrapper">
	<div class="table-title">
	<h3>UCI 3D Dataset</h3>
	</div>
	
	<table class="table-fill">
		<thead>
		<tr>
		<th class="text-left">Name</th>
		<th class="text-left">Category</th>
		<th class="text-left">Date</th>
		<th class="text-left">Thumb nail</th>
		<th class="text-left">View Link</th>
		<th class="text-left">Download Link</th>
		</tr>
		</thead>
		<tbody class="table-hover">
		 <c:forEach items="${contentList}" var="content" varStatus="list_status">
	     	<tr>
	     		<td class="text-left">
	     			${content.filename}
	     		</td>
	     		<td class="text-left">
	     			${content.category}
	     		</td>
	     		<td class="text-left">
	     			${content.date}
	     		</td>
	     		<td class="text-left">
	     			<img src="http://localhost/data/${content.filename}.rgb.jpg" width="150"></img>
	     		</td>
	     		<td class="text-left">
	     			<a href="javascript:openModal('http://localhost/data/web/examples/${content.filename}.html');" target="blank">View Model</a>
	     		</td>
	     		<td class="text-left">
	     			<a href="http://localhost/data/${content.filename}.ply" target="blank">Download</a>
	     		</td>
	     	</tr>
	     </c:forEach>
	     </tbody>
	</table>
</div>





</body>
</html>

