# monitor_feedback

This prototype enables end-users to communicate their feedback about a service or application in a flexible and multi-modal way. This means that end-users can document their feedback using text input, audio recording, ratings and screenshot annotations. The developed solution is available for Android and web applications and deployed on the SUPERSEDE platform.


### android_library

Android library to include feedback gathering mechanisms in an Android application.

### monitors

This project contains the set of implemented monitors (RESTful web services) for the feedback gathering project.

### orchestrator

Is a RESTfull Web API that provides endpoints for storing, receiving and updating the feedback configuration.

### reporting_frontend

This is an Angular2 application that serves as a frontend for the repository. It allows to view and manage submitted feedbacks.

### repository

Is a RESTfull Web API that provides endpoints for storing, receiving and deleting user feedbacks.

### server_library

Functionality extracted into a library, that is used by orchestrator and repository.

### web_library
 
jQuery Plugin to include Feedback Gathering on a Website



_Note: javascript, stylesheets, index.html and params.json belong to the github webpage http://supersede-project.github.io/monitor_feedback/._  