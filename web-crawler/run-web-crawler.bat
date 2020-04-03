@echo off
set urlPrefix=https://docs.oracle.com/javase/8/docs/api
set seedUrl=%urlPrefix%/allclasses-noframe.html
echo Seed URL: %seedUrl%
echo URL prefix: %urlPrefix%
java -cp %0\..\lib\*;%0\..\web-crawler-1.0-SNAPSHOT.jar helvidios.search.webcrawler.App %seedUrl% %urlPrefix%