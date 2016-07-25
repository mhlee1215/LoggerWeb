<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <link rel="stylesheet" href="css/style.css">
  <link rel="stylesheet" href="css/modal.css">
  <link rel="stylesheet" href="css/jquery-ui.css">
  <script src="js/jquery-3.0.0.min.js"></script>
  <script src="js/jquery-ui.min.js"></script>
  
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
    method.open = function (url, name, width, height) {
      $content.empty().append(
    	  '<h3>'+name+'<h3>'+
          '<iframe src="'+url+'" width="'+width+'" height="'+height+'" style="border:0px;"></iframe><br>'+
          '<a href="'+url+'" target="blank">Open in new window</a>'
      );

      $modal.css({
        width: 'auto', 
        height: 'auto'
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

    //$.get('index.html', function(data){
//      modal.open({content: data});
    //});
    

    $('a#howdy').click(function(e){
      modal.open({content: "Hows it going?"});
      e.preventDefault();
    });
  });
  
  function openModal(url, name){
	 
	var height = $(window).height()-200;
	var width = $(window).width()-200;
    modal.open(url, name, width, height);
    /*
    $("#dialog").html('<iframe src="'+url+'" width="600" height="600"></iframe><br>'+
            '<a href="'+url+'" target="blank">Open in new window</a>');
    $( "#dialog" ).dialog({
        modal: true,
        width:"auto",
        buttons: {
          Ok: function() {
            $( this ).dialog( "close" );
          }
        }
      });
    
    $( ".selector" ).dialog( "option", "position", { my: "center", at: "center", of: button } );
    */
  }
  
  function reload(cat){
	  window.location = 'index.do?category='+cat;
  }
  
  function reloadByDate(date){
	  window.location = 'index.do?category=${cur_cat}&date='+date;
  }
  
  function remove(name){
	  var r = confirm("Delete "+name);
	  if (r == true) {
		  window.location = 'remove.do?category=${cur_cat}&filename='+name;
	  } else {
	  }
	  
  }
  
  function undo(name){
	  var r = confirm("Undo "+name);
	  if (r == true) {
		  window.location = 'undoEF.do?category=${cur_cat}&filename='+name;
	  } else {
	  }
	  
  }
  
  function changeCategory(filename, toCategory){
	  var r = confirm("Change category "+filename+' to '+toCategory);
	  if (r == true) {
		  window.location = 'changeCategory.do?toCategory='+toCategory+'&filename='+filename;
	  } else {
	  }
	  
  }

  
  
  </script>
</head>
<body>
<div id="wrapper">
  <div class="table-title">
  <h3><a href="index.do" style="text-decoration:none">UCI 3D Indoor Dataset</a></h3>
  </div>
  <c:if test="${isAdmin == 'Y' }">
  <div align="right">
  <a href="logout.do">logout</a>
  </div>
  </c:if>
  <table class="table-fill">
    <thead>
    <tr>
    <th class="text-left" width="200px">Name</th>
    <th class="text-left">Category
    
    <select onchange="javascript:reload(this.value)">
    	<option ${"" == cur_cat ? "selected" : "" } value="">All</option>
    	<c:forEach items="${cat}" var="c" varStatus="list_status">
	  	<option ${c.category == cur_cat ? "selected" : "" } value="${c.category}">${c.category} (${c.size})</option>
	  	</c:forEach>
	</select>
    </th>
    <th class="text-left">Date
    
    <select onchange="javascript:reloadByDate(this.value)">
    	<option ${"" == cur_date ? "selected" : "" } value="">All</option>
    	<c:forEach items="${date}" var="d" varStatus="list_status">
	  	<option ${d.date == cur_date ? "selected" : "" } value="${d.date}">${d.date} (${d.size})</option>
	  	</c:forEach>
	</select>
    
    </th>
    <th class="text-left">Thumb nail</th>
    <th class="text-left">View Link</th>
    <th class="text-left">Download Link</th>
    <c:if test="${isAdmin == 'Y' }">
    <th class="text-left">Delete</th>
    <th class="text-left">Undo</th>
    </c:if>
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
            <c:if test="${isAdmin == 'Y' }">
            <select onchange="javascript:changeCategory('${content.filename}', this.value)">
		    	<c:forEach items="${cat}" var="c" varStatus="list_status">
			  	<option ${c.category == content.category ? "selected" : "" } value="${c.category}">${c.category}</option>
			  	</c:forEach>
			</select>
            </c:if>
          </td>
          <td class="text-left">
            ${content.date}
          </td>
          <td class="text-left">
            <img src="http://mhlee-pc.ics.uci.edu/data/complete/${content.filename}.rgb.jpg" width="150"></img>
          </td>
          <td class="text-left">
            <a href="javascript:openModal('http://mhlee-pc.ics.uci.edu//data/web/examples/${content.filename}.html', '${content.filename}');" target="blank">View Model</a>
          </td>
          <td class="text-left">
            <a href="http://mhlee-pc.ics.uci.edu/data/complete/${content.filename}.ply" target="blank">Download</a>
          </td>
          <c:if test="${isAdmin == 'Y' }">
          <td class="text-left">
            <a href="javascript:remove('${content.filename}');" target="blank">Delete</a>
          </td>
          <td class="text-left">
            <a href="javascript:undo('${content.filename}');" target="blank">Undo</a>
          </td>
          </c:if>
        </tr>
       </c:forEach>
       </tbody>
  </table>
</div>

<div id="dialog" title="Model Viewer">
  
</div>



</body>
</html>

