function captureData(event) {
    $.blockUI();
    var url = 'rest/capture';
    var jqxhr = $.post(url, data);

    jqxhr.success(function (result) {
        alert("ajax success");
    });
    jqxhr.error(function () {
        alert("ajax error");
    });
    jqxhr.complete(function () {
        $.unblockUI();
        alert("Capture Successful");
        displayCoverageList();
    });
}

function resetData(event) {
    $.blockUI();
    $.post('rest/reset', function (data) {
        $.unblockUI();
        alert("Reset Successful");
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
        });

        out += " </ul> </div>";
        $("#coverageList").html(out);
        $("#coverageList").css({display: 'block'});
    });
}