/**
 * JavaScript for monitoring user events - HTML events
 */

var req; var idOutput = 0; var confID = 0;

//Listener function
function fnEventListener(){
	var userID = document.getElementById("idUserID").textContent;	//Obtaining the User ID from a html element

	if (document.addEventListener) {					// For all major browsers, except IE 8 
		document.addEventListener('mousedown', function(e){fnGetEventInf(e, "mousedown", userID);}, false);
		document.addEventListener('click', function(e){fnGetEventInf(e, "click", userID);}, false);
		document.addEventListener('contextmenu', function(e){fnGetEventInf(e, "right-button mouse click", userID);}, false);
		document.addEventListener('dblclick', function(e){fnGetEventInf(e, "dblclick", userID);}, false);
	} else if (document.attachEvent) {			        // For IE 8 and earlier versions
		document.attachEvent('mousedown', function(e){fnGetEventInf(e, "mousedown", userID);}, false);
		document.attachEvent('click', function(e){fnGetEventInf(e, "click", userID);}, false);
		document.attachEvent('contextmenu', function(e){fnGetEventInf(e, "right-button mouse click", userID);}, false);
		document.attachEvent('dblclick', function(e){fnGetEventInf(e, "dblclick", userID);}, false);
	}
}

function fnGetEventInf(e, sEventType, userID){
	e = e || window.event;
    var target = e.target || e.srcElement,
        typeElement = target.nodeName || typeElement.innerText,
        idElement = target.id,
        textElement = target.textContent,
        valueElement = target.value;
    
    idOutput++;
    confID ++;
    
    fnSendMonitoredData(idOutput, confID, userID, window.location.href, typeElement, idElement, sEventType, textElement, valueElement);
}

function fnGetFullURL(){
 window.location.href;
}

//Calls servlet MonitoredDataManager and sends the information of the event
function fnSendMonitoredData(idOutput, confID, userID, sCurrentPage, sElement, sIDElement, sEventType, sElementText, sElementValue){
	var vInfoEvents = "OutputID=" + idOutput + "&ConfigurationID=" + confID + "&UserID=" + userID + "&CurrentPage=" + sCurrentPage  + "&Element=" + sElement + "&idElement=" + sIDElement + "&EventType=" + sEventType + "&Text=" + sElementText + "&Value=" + sElementValue + "&Timestamp=" + Date();
	
	var url = "MonitoredDataManager?" + vInfoEvents;
	
	fnStartRequest();

	req.onreadystatechange = fnCallback;
	req.open("POST", url, true);
	req.send(null);
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