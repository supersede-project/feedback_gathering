var labelFontType = "Helvetica";
var labelFontColor = "#000";
var labelFontSize = 14;
var submitText = "Success";

$(document).ready(function () {
    /* preview update on generalField setting font and fontcolor */
    $("select#setFont").change(setFontFamily);

    function setFontFamily() {
        labelFontType = $("#setFont").val();
        $( "#preview #viewDiv" ).removeClass().addClass(labelFontType);
    }

    $("#setFontColor").change(function () {
        labelFontColor = $("#setFontColor").val();
        $("#preview #viewDiv").css("color", labelFontColor);
    });

    $("#fontSize").change(function() {
        labelFontSize = $("#fontSize").val();
        $("#preview #viewDiv").css("font-size", labelFontSize + "px");
    });

    $("#submitText").change(function() {
        submitText = $("#submitText").val();
    });
});