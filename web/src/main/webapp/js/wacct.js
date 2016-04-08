function captureData(event) {
    $.blockUI();
    $.ajax({
        url: 'rest/capture',
        type: 'GET',
        dataType: 'json',
        data: '',
        success: function (data) {
            $.unblockUI();
            alert("Capture Successful");
        },
        error: function (xhr, status, error) {
            alert("There was a problem with the report.");
        }
    })
}

function resetData(event) {
    $.blockUI();
    $.ajax({
        url: 'rest/reset',
        type: 'GET',
        dataType: 'json',
        data: '',
        success: function (data) {
            $.unblockUI();
            alert("Reset Successful");
        },
        error: function (xhr, status, error) {
            alert("There was a problem resetting the report.");
        }
    })
}