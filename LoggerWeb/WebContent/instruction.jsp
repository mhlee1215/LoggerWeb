<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Install Logger Instruction</title>
</head>
<body>

<h3>Update Kernel (for odroid)</h3>
sudo odroid-utility.sh<br>

<h3>Install required libraries</h3>
sudo apt-get update<br>
sudo apt-get install -y git-core cmake freeglut3-dev pkg-config build-essential libxmu-dev libxi-dev libusb-1.0-0-dev libopencv-dev libboost-all-dev librxtx-java apache2 freenect openjdk-8-jdk<br> 

<h3>Make LoggerHome</h3>
cd ~<br>
mkdir LoggerHome<br>
(or) ln -s (path-to-loggerHome) LoggerHome<br>

<h3>Download Logger</h3>
cd ~/LoggerHome<br>
git clone https://github.com/mhlee1215/Logger_libfreenect_custom.git<br>
cd Logger_libfreenect_custom<br>
cmake .<br>
make<br>
ln -s ~/LoggerHome/Logger_libfreenect_custom/bin/freenect-cvdemo ~/LoggerHome/cvLogger <br>
<br>

<h3>Download Logger web</h3>

cd ~/LoggerHome<br>
git clone https://github.com/mhlee1215/LoggerWeb.git<br>
(or) git clone git@github.com:mhlee1215/LoggerWeb.git<br>
wget http://www.gtlib.gatech.edu/pub/apache/tomcat/tomcat-7/v7.0.70/bin/apache-tomcat-7.0.70.tar.gz<br>
tar xvf apache-tomcat-7.0.70.tar.gz<br>
cp ~/LoggerHome/LoggerWeb/LoggerWeb/export/LoggerWeb.war ~/LoggerHome/apache-tomcat-7.0.70/webapps/<br>
~/LoggerHome/apache-tomcat-7.0.70/bin/startup.sh<br> 

<h3>Install apache for image visualization</h3>
cd /var/www/html (Ubuntu)<br>
cd /var/www (Pi)<br>
sudo ln -s ~/LoggerHome/capture /var/www/html/Logger <br> 
<br>
If port is blocked, then we need to add another port (say 81) by editing /etc/apache2/ports.conf, /etc/apache2/sites-enabled/000-default<br>
sudo apache2ctl restart<br>
<h3>References</h3>

<ul>
	<li><a href="https://openkinect.org/wiki/Getting_Started">https://openkinect.org/wiki/Getting_Started</a><br></li>
	<li><a href="https://help.github.com/articles/adding-a-new-ssh-key-to-your-github-account/">https://help.github.com/articles/adding-a-new-ssh-key-to-your-github-account/</a></li>
</ul>

</body>
</html>