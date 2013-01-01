#!/bin/sh
HOST='128.195.57.180'
USER='mhlee'
PASSWD='cvip2423'
FILE=$1
DST_PATH='~/data_from_odroid'

lftp -u $USER,$PASSWD $HOST <<EOF
cd $DST_PATH
put $FILE
bye
EOF
