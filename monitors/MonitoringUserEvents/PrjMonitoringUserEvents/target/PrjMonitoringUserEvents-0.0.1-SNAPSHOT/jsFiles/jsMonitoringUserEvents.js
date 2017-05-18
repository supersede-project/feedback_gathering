/**
 * JavaScript for monitoring user events - HTML events
 */

var req; var confID = 1; 

function configureMonitoringEvents(userID) {
	
    var xhttp;
    
    var url = "MonitoredDataManager?";
    
    if (window.XMLHttpRequest) {
      xhttp = new XMLHttpRequest();
    } 
    else if (window.ActiveXObject) {
      xhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }        
    
    xhttp.onreadystatechange = function () {
        if(xhttp.readyState === 4)
        {
            if(xhttp.status === 200 || xhttp.status == 0)
            {
                var config = JSON.parse(xhttp.responseText);
                fnEventListener(userID, config);
            }
        }
      };
      
      xhttp.open("GET", url, true);

      xhttp.send();
      
}


//Listener function
function fnEventListener(userID, config){
	
	sessionStorage.setItem('userID', userID);
	//sessionStorage.setItem('server', server);
	var server = config.server;
	var protocol = config.protocol;
	var ListOfEvents = config.ListOfEvents;
	var textContentSize = config.textContentSize;
	
	document.getElementById("SelectedServer").value = server;
	document.getElementById("SelectedProtocol").value = protocol;
	
	for (var i = 0; i < ListOfEvents.length; i++) {
		(function (i) {
			if(document.addEventListener){  // For all major browsers, except IE 8
				document.addEventListener(ListOfEvents[i].trim(), function(e){fnGetEventInf(e, ListOfEvents[i].trim(), sessionStorage.getItem('userID'), server, textContentSize, protocol);}, false);
			}
			else if (document.attachEvent){  // For IE 8 and earlier versions
				document.attachEvent(ListOfEvents[i].trim(), function(e){fnGetEventInf(e, ListOfEvents[i].trim(), sessionStorage.getItem('userID'), server, textContentSize, protocol);}, false);
			}
			
			}(i));
		}
	
}

function fnGetEventInf(e, sEventType, userID, server, textContentSize, protocol){
	e = e || window.event;
    var target = e.target || e.srcElement,
        typeElement = target.nodeName || typeElement.innerText,
        idElement = target.id,
        textElement = target.textContent,
        valueElement = target.value;

    
    textElement = textElement.replace(/(\r\n|\n|\r)/gm,"");
    textElement = textElement.substring(0, textContentSize);
    
    fnSendMonitoredData(generateUUID(), confID, userID, typeElement, idElement, sEventType, textElement, valueElement, server, protocol);
}

//Function for generating the output id
function generateUUID(){
    var d = new Date().getTime();
    if(window.performance && typeof window.performance.now === "function"){
        d += performance.now(); //use high-precision timer if available
    }
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = (d + Math.random()*16)%16 | 0;
        d = Math.floor(d/16);
        return (c=='x' ? r : (r&0x3|0x8)).toString(16);
    });
    return uuid;
}

//Calls servlet MonitoredDataManager and sends the information of the event
function fnSendMonitoredData(idOutput, confID, userID, sElement, sIDElement, sEventType, sElementText, sElementValue, server, protocol){
	var sCurrentURLPage = window.location.href;
	
	//var vInfoEvents = "OutputID=" + idOutput + "&ConfigurationID=" + confID + "&UserID=" + userID + "&Element=" + sElement + "&idElement=" + sIDElement + "&EventType=" + sEventType + "&Text=" + sElementText + "&Value=" + sElementValue + "&Timestamp=" + Date();
	
	//Local
	//var url = "MonitoredDataManager?" + vInfoEvents;
	var url = "MonitoredDataManager?";
	
	//Remoto
	//var url = "http://supersede.es.atos.net:8081/PrjMonitoringUserEvents/MonitoredDataManager?" + vInfoEvents;
	//var url = "http://supersede.es.atos.net:8081/PrjMonitoringUserEvents/MonitoredDataManager?";
	
	
	/*if (server=='production'){
		if (protocol=='HTTPS'){
			var url = "https://platform.supersede.eu:8443/PrjMonitoringUserEvents/MonitoredDataManager?";
		}
		if (protocol=='HTTP'){
			var url = "http://platform.supersede.eu:8081/PrjMonitoringUserEvents/MonitoredDataManager?";
		}
	}
	if (server=='development'){
		if (protocol=='HTTPS'){
			var url = "https://supersede-develop.atosresearch.eu:8443/PrjMonitoringUserEvents/MonitoredDataManager?";
		}
		if (protocol=='HTTP'){
			var url = "http://supersede.es.atos.net:8081/PrjMonitoringUserEvents/MonitoredDataManager?";
		}
	}*/
	
	
	fnStartRequest();

	req.onreadystatechange = fnCallback;
	req.open("POST", url, true);
	
	var data = new Object();
	data.confId = confID;
	data.idUser  = userID;
	data.timeStamp = Date();
	data.idElement = sIDElement;
	data.elementText = sElementText;
	data.eventType = sEventType;
	data.elementValue = sElementValue;
	data.idOutput = idOutput;
	data.currentPage = sCurrentURLPage;
	data.elementType = sElement;
	
	
	//req.send(confID + "&&" + userID + "&&" + Date() + "&&" + sIDElement + "&&" + sElementText + "&&" + sEventType + "&&" + sElementValue + "&&" + idOutput + "&&" + sCurrentURLPage + "&&" + sElement);
	req.send(JSON.stringify(data));
}

//Create the XMLHttpRequest object
function fnStartRequest() {
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		req = new ActiveXObject("Microsoft.XMLHTTP");
	}
}

//Function that obtains the response from the servlet MonitoredDataManager (Asynchronously)
function fnCallback() {
	if (req.readyState == 4) {
		if (req.status == 200) {
			//alert("done");
		}
	}
}








/*------------------------------------------------------*/
/* Other form to get implement the Listener
 * //Listener function
function fnEventListener(){
	
	//Listener for buttons
	/*
	for (var iI = 0; iI < document.getElementsByTagName("button").length; iI++){
		document.getElementsByTagName("button")[iI].addEventListener("click", function(){fnSendMonitoredData("button", this.id, "click", this.textContent);});
	}
	
	for (var iI = 0; iI < document.getElementsByTagName("button").length; iI++){
		document.getElementsByTagName("button")[iI].addEventListener("contextmenu", function(){fnSendMonitoredData("button", this.id, "right-button mouse click", this.textContent);});
	}
	
	for (var iI = 0; iI < document.getElementsByTagName("button").length; iI++){
		document.getElementsByTagName("button")[iI].addEventListener("dblclick", function(){fnSendMonitoredData("button", this.id, "dblclick", this.textContent);});
	}
}
 */

//Other for to implement fnSendMonitoredData
/*
function fnSendMonitoredData(sElement, sIDElement, sEventType, sElementText) {
	var sElementValue = document.getElementById(sIDElement).value;
	var vInfoEvents = "Element=" + sElement + "&idElement=" + sIDElement + "&EventType=" + sEventType + "&Text=" + sElementText + "&Value=" + sElementValue + "&Timestamp=" + Date();
	
	var url = "MonitoredDataManager?" + vInfoEvents;
	
	fnStartRequest();
	
	req.onreadystatechange = fnCallback;
	req.open("POST", url, true);
	req.send(null);
}*/