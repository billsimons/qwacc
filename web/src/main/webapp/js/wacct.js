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
    $.post('rest/list', function (data) {
        var out = "<div> <h2>Coverage List</h2> <ul> ";
        $.each(data, function (key, val) {
            var url = "/"+val.context+"/"+val.name;
            out += " <li><a href='"+url+"'> " + val.name + "</a></li> ";
            console.log(out);
        });

        out += " </ul> </div>";
        console.log(out);
        $("#coverageLists").html(out);
        $("#coverageLists").css({display: 'block'});
    });
}