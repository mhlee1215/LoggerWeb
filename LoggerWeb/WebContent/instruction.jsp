<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Install Logger Instruction</title>
</head>
<body>

<h3>Install required libraries</h3>
sudo apt-get install -y git-core cmake freeglut3-dev pkg-config build-essential libxmu-dev libxi-dev libusb-1.0-0-dev libopencv-dev libboost-all-dev librxtx-java<br> 

<h3>Download Logger</h3>
cd ~/Downloads<br>
git clone https://github.com/mhlee1215/Logger_libfreenect_custom.git<br>
cd Logger_libfreenect_custom<br>
cmake .<br>
make<br>
ln -s ~/Downloads/Logger_libfreenect_custom/bin/freenect-cvdemo ~/cvLogger <br>
<br>
<h3>Download Logger web</h3>
cd ~/Downloads<br>
git clone https://github.com/mhlee1215/LoggerWeb.git<br>
wget http://mirror.symnds.com/software/Apache/tomcat/tomcat-7/v7.0.69/bin/apache-tomcat-7.0.69.tar.gz<br>
tar xvf apache-tomcat-7.0.69.zip<br>
cp ~/Downloads/LoggerWeb/LoggerWeb/export/LoggerWeb.war ~/Downloads/apache-tomcat-7.0.69/webapps/ 

<h3>Install apache for image visualization</h3>
sudo apt-get install apache2
cd /var/www/html (Ubuntu)<br>
cd /var/www (Pi)<br>
ln -s ~/capture Logger <br> 
<br>
If port is blocked, then we need to add another port (say 81) by editing /etc/apache2/ports.conf, /etc/apache2/sites-enabled/000-default
<h3>References</h3>
<ul>
	<li><a href="https://openkinect.org/wiki/Getting_Started">https://openkinect.org/wiki/Getting_Started</a><br></li>
</ul>

</body>
</html>