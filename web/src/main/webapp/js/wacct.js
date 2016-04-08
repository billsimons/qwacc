function captureData(event) {
    $.blockUI();
    $.post('rest/capture', {data: ''}, function (data) {
        $.unblockUI();
        alert("Capture Successful");
        displayCoverageList();
    });
}

function resetData(event) {
    $.blockUI();
    $.post('rest/reset', {data: ''}, function (data) {
        $.unblockUI();
        alert("Reset Successful");
    });
}

function displayCoverageList() {
    var out = "<div> <h2>Coverage List</h2> ";
    $.post('rest/list', {data: ''}, function (data) {
        console.log(data);
        var parsedData = $.parseJSON(data);
        console.log(parsedData);

        out +=  " <ul> " +

        $.each(parsedData, function (key, val) {
            var url = "#"+val.context;
            out += " <li><a href='"+url+"'> " + val.name + "</a></li> ";
        });

        out += " </ul> </div>";
        $("#coverageList").html(out);
        $("#coverageList").css({display: 'block'});
    });
}