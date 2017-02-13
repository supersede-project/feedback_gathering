/**
 * JavaScript for monitoring user events - HTML events
 */

var req; var confID = 1;

//Listener function
function fnEventListener(userID){
	sessionStorage.setItem('userID', userID);
	
	if (document.addEventListener) {					// For all major browsers, except IE 8 
		document.addEventListener('mousedown', function(e){fnGetEventInf(e, "mousedown", sessionStorage.getItem('userID'));}, false);
		document.addEventListener('click', function(e){fnGetEventInf(e, "click", sessionStorage.getItem('userID'));}, false);
		document.addEventListener('contextmenu', function(e){fnGetEventInf(e, "right-button mouse click", sessionStorage.getItem('userID'));}, false);
		document.addEventListener('dblclick', function(e){fnGetEventInf(e, "dblclick", sessionStorage.getItem('userID'));}, false);
	} else if (document.attachEvent) {			        // For IE 8 and earlier versions
		document.attachEvent('mousedown', function(e){fnGetEventInf(e, "mousedown", sessionStorage.getItem('userID'));}, false);
		document.attachEvent('click', function(e){fnGetEventInf(e, "click", sessionStorage.getItem('userID'));}, false);
		document.attachEvent('contextmenu', function(e){fnGetEventInf(e, "right-button mouse click", sessionStorage.getItem('userID'));}, false);
		document.attachEvent('dblclick', function(e){fnGetEventInf(e, "dblclick", sessionStorage.getItem('userID'));}, false);
	}
}

function fnGetEventInf(e, sEventType, userID){
	e = e || window.event;
    var target = e.target || e.srcElement,
        typeElement = target.nodeName || typeElement.innerText,
        idElement = target.id,
        textElement = target.textContent,
        valueElement = target.value;
    
    fnSendMonitoredData(generateUUID(), confID, userID, typeElement, idElement, sEventType, textElement, valueElement);
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
function fnSendMonitoredData(idOutput, confID, userID, sElement, sIDElement, sEventType, sElementText, sElementValue){
	var sCurrentURLPage = window.location.href;
	
	var vInfoEvents = "OutputID=" + idOutput + "&ConfigurationID=" + confID + "&UserID=" + userID + "&Element=" + sElement + "&idElement=" + sIDElement + "&EventType=" + sEventType + "&Text=" + sElementText + "&Value=" + sElementValue + "&Timestamp=" + Date();
	
	var url = "MonitoredDataManager?" + vInfoEvents;
	
	fnStartRequest();

	req.onreadystatechange = fnCallback;
	req.open("POST", url, true);
	req.send(sCurrentURLPage);
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