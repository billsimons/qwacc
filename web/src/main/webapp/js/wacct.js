function captureData(event) {
    //$.blockUI();
    $.ajax({
        url: 'rest/capture',
        type: 'POST',
        dataType: 'json',
        data: '',
        success: function (data) {
            //$.unblockUI();
            alert("Capture Successful");
        }
    })
}

function resetData(event) {
    $.blockUI();
    $.ajax({
        url: 'rest/reset',
        type: 'POST',
        dataType: 'json',
        data: '',
        success: function (data) {
            //$.unblockUI();
            alert("Reset Successful");
        }
    })
}