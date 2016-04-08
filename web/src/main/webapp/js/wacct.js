function captureData(event) {
    $.blockUI();

    $.post('rest/capture', function (data) {
        $.unblockUI();
        displayCoverageList();
    });
}

function resetData(event) {
    $.blockUI();
    $.post('rest/reset', function (data) {
        $.unblockUI();
    });
}

function displayCoverageList() {
    var out = "<div> <h2>Coverage List</h2> ";
    $.post('rest/list', function (data) {
        console.log(data);

        out +=  " <ul> " +

        $.each(data, function (key, val) {
            console.log(val.context);
            console.log(val.name);
            var url = "#"+val.context;
            out += " <li><a href='"+url+"'> " + val.name + "</a></li> ";
            console.log(out);
        });

        out += " </ul> </div>";
        console.log(out);
        $("#coverageList").html(out);
        $("#coverageList").css({display: 'block'});
    });
}