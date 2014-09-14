var lightDataTable = new LightDataTable('.datatable table', applicationBaseUrl + "/rest/logs");

var loggingPresenter = new LoggingPresenter("#logger-output");

$(function() {
    lightDataTable.initialize();

    loggingPresenter.initialize(applicationBaseUrl + '/ws');

    showHostName();
});

$( window ).unload(function() {
    loggingPresenter.destroy();
});

function showHostName() {
    $.ajax({
        type: "GET",
        url: applicationBaseUrl + "/rest/logs/hostname",
        contentType: 'application/json'
    }).complete(function (data) {
        $('#hostname').children().text(data.responseText)
    });
}

function donate() {
    document.location.href = "http://wings-phoenix.org.ua/en/donate-instructions";
}