/**
 * JavaScript for monitoring user events - HTML events
 */

var req; var confID = 1; 

function SupersedeHTMLMonitor(){
	this.watchUser = function(userID){
		configureMonitoringEvents(userID);
	};
}

//a function that gets the information needed from the configuration file
function configureMonitoringEvents(userID) {
	
    var xhttp;
    
    //the configuration file is saved locally
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
	
	var ListOfEvents = config.ListOfEvents;
	
	document.getElementById("SelectedServer").value = config.server;
	document.getElementById("SelectedProtocol").value = config.protocol;
	
	for (var i = 0; i < ListOfEvents.length; i++) {
		(function (i) {
			if(document.addEventListener){  // For all major browsers, except IE 8
				document.addEventListener(ListOfEvents[i].trim(), function(e){fnGetEventInf(e, ListOfEvents[i].trim(), sessionStorage.getItem('userID'), config);}, false);
			}
			else if (document.attachEvent){  // For IE 8 and earlier versions
				document.attachEvent(ListOfEvents[i].trim(), function(e){fnGetEventInf(e, ListOfEvents[i].trim(), sessionStorage.getItem('userID'), config);}, false);
			}			
		}(i));
	}
	
}

function fnGetEventInf(e, sEventType, userID, config){
	e = e || window.event;
    var target = e.target || e.srcElement,
        typeElement = target.nodeName || typeElement.innerText,
        idElement = target.id,
        textElement = target.textContent,
        valueElement = target.value;

    
    textElement = textElement.replace(/(\r\n|\n|\r)/gm,"");
    textElement = textElement.substring(0, config.textContentSize);
    
    fnSendMonitoredData(generateUUID(), confID, userID, typeElement, idElement, sEventType, textElement, valueElement, config);
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
function fnSendMonitoredData(idOutput, confID, userID, sElement, sIDElement, sEventType, sElementText, sElementValue, config){
	var sCurrentURLPage = window.location.href;
	
	//Local
	//var url = "MonitoredDataManager?";
	
	//Remoto	
	if (config.server=='production'){
		if (protocol=='HTTPS'){
			var url = "https://platform.supersede.eu:8443/PrjMonitoringUserEvents/MonitoredDataManager?";
		}
		if (config.protocol=='HTTP'){
			var url = "http://platform.supersede.eu:8081/PrjMonitoringUserEvents/MonitoredDataManager?";
		}
	}
	if (config.server=='development'){
		if (protocol=='HTTPS'){
			var url = "https://supersede-develop.atosresearch.eu:8443/PrjMonitoringUserEvents/MonitoredDataManager?";
		}
		if (config.protocol=='HTTP'){
			var url = "http://supersede.es.atos.net:8081/PrjMonitoringUserEvents/MonitoredDataManager?";
		}
	}
	
	
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
	data.kafkaTopic = config.kafkaTopic;
	
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
