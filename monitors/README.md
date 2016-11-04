#Monitors
This project contains the set of implemented monitors (RESTful web services) for the feedback gathering project.
##Monitor installation
Find below the instructions to deploy the monitors from scratch in a tomcat server.
###WAR generation

To generate the .war file:
- Download the monitors you are interested in
	- Twitter: https://github.com/supersede-project/monitor_feedback/tree/master/monitors/twitter
    - Google Play: https://github.com/supersede-project/monitor_feedback/tree/master/monitors/googlePlay
	- App Store: https://github.com/supersede-project/monitor_feedback/tree/master/monitors/appStore
- From command line:
	- `cd /[path-to-specific-monitor]`
    - `./gradlew war`
    - Generated war is placed in _/[path-to-specific-monitor]/build/libs_
- From Eclipse:
	- Open monitor project in Eclipse
    - Right click on source folder in _Project Explorer > Export > WAR file_
    - Select the destination of the generated file and Finish

###WAR installation

To install the .war file in Tomcat server:
- Place the .war file in _/path/to/tomcat/webapps_
- Monitor should be running on http://localhost:8080/monitorName
