#Monitors
This project contains the set of implemented monitors (RESTful web services) for the feedback gathering project.
##Monitor installation
Find below the instructions to deploy the monitors from scratch in a tomcat server.
###WAR generation

To generate the .war file:
- Download the monitors you are interested in
- From command line:
	- _cd /[path-to-specific-monitor]_
    - _gradle war_
    - Generated war is placed in _/[path-to-specific-monitor]/build/libs_
- From Eclipse:
	- Open monitor project in Eclipse
    - Right click on source folder in Project Explorer > Export > WAR file
    - Select the destination of the generated file and Finish

###WAR installation

To install the .war file in Tomcat server:
- Place the .war file in _/path/to/tomcat/webapps_
- Monitor should be running on http://localhost:8080/monitorName