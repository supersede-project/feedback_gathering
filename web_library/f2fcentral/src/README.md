# Introduction

Is a jQuery plugin that provides the integration of backward-feedback mechanisms for web applications.

It offers the following features:

Admin console:
- Review received feedbacks
- Block threads
- Add/delete comments
- Chat with feedback sender
- Add/promote/unpromote company news (e.g. releases)
- Publish feedbacks to forum
- Upload/update avatar to API User account

End-user View:
- Review sent feedbacks
- Review summary of forum activities for publicly visible feedbacks
- Chat with API User
- Add comments to feedbacks in forum
- Add like/dislike to feedbacks in forum
- Review promoted company news


# How to start

In order to get the project running use:

```bash
git clone https://github.com/supersede-project/monitor_feedback/tree/master/web_library
cd web_library
# install the project's dependencies
yarn install
# run f2f central
yarn webpack
# [optional] deploy stylings in case UI is not displayed appropriately
sass app/css/main.scss:dist/main.min.css
```
# Directory Structure

```
.
├── f2fcentral                                      <- source code of the application
│   ├── src
        ├── adminView                               <- contains classes for admin view console
        ├── css                                     <- style sheets for f2f central, dropdown, etc.
        ├── end-user view                           <- contains classes for end-user view
        ├── helpers                                 <- helpers for f2f central modules
│   ├── App.js                                      <- main class initialising all views

```


# License

React 16.2.0















