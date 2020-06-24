@echo off
Rem set urlPrefix=https://docs.oracle.com/javase/8/docs/api
Rem set seedUrl=%urlPrefix%/allclasses-noframe.html
Rem echo Seed URL: %seedUrl%
Rem echo URL prefix: %urlPrefix%
java -cp %0\..\lib\*;%0\..\web-crawler-1.0-SNAPSHOT.jar helvidios.search.webcrawler.App %1 %2 %3