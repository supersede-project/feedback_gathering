#README
This project contains the RESTful web service monitor for Twitter social network.
##Monitor installation
Find below the instructions to deploy the Twitter monitor from scratch in a tomcat server.
###WAR generation

To generate the .war file:
- Download the monitor project: https://github.com/supersede-project/monitor_feedback/tree/master/monitors/twitter
- From command line:
	- `cd /[path-to-monitor]`
    - `./gradlew war`
    - Generated war is placed in _/[path-to-monitor]/build/libs_
- From Eclipse:
	- Open project in Eclipse
    - Right click on source folder in _Project Explorer > Export > WAR file_
    - Select the destination of the generated file and Finish

###WAR installation

To install the .war file in Tomcat server:
- Place the .war file in _/path/to/tomcat/webapps_
- Monitor is now running on http://localhost:8080/twitter

##API reference documents

 http://docs.twittermonitor.apiary.io/


