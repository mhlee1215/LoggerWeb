<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Install Logger Instruction</title>
</head>
<body>
<a href="https://openkinect.org/wiki/Getting_Started">https://openkinect.org/wiki/Getting_Started</a><br>
sudo apt-get install -y git-core cmake freeglut3-dev pkg-config build-essential libxmu-dev libxi-dev libusb-1.0-0-dev libopencv-dev libboost-all-dev librxtx-java<br> 
<p>Move to Download folder</p><br>
cd ~/Downloads<br>
<p>Download Logger</p><br>
git clone https://github.com/mhlee1215/Logger_libfreenect_custom.git<br>
cd Logger_libfreenect_custom<br>
cmake .<br>
make<br>

<p>Download Logger web</p><br>
git clone https://github.com/mhlee1215/LoggerWeb.git<br>
 

ln -s ~/Downloads/Logger_libfreenect_custom/bin/freenect-cvdemo ~/cvLogger <br>

sudo apt-get install apache2
cd /var/www/html (Ubuntu)<br>
cd /var/www (Pi)<br>
ln -s ~/capture Logger <br> 

</body>
</html>