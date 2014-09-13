function donate() {
    document.location.href = "http://wings-phoenix.org.ua/en/donate-instructions";
}

function fnDrawCallback() {
    $(":button", $('.datatable table')).click(function () {
        var logger = $(this).attr('data-logger');
        var level = $(this).attr('data-level');

        $.ajax({
            type: "PUT",
            url: applicationBaseUrl + "/rest/logs",
            data: JSON.stringify({
                "name": logger,
                "level": level
            }),
            contentType: 'application/json'
        }).done(function (data) {
            var name = data['name'];
            var level = data['level'];

            $("button[data-logger='" + name + "']").removeClass('active');
            $("button[data-logger='" + name + "'][data-level='" + level + "']").addClass('active');

            $.jGrowl(null,
                { sticky: false, theme: 'growl-success', header: 'Logging level changed' }
            );
        }).fail(function (jqXHR, textStatus, errorThrown) {
            $.jGrowl(null, { sticky: false, theme: 'growl-error', header: errorThrown });
        });
    });
}

function renderLoggingLevelButtons(data, type, full) {
    var loggingLevels = [
        { name: 'TRACE', class: 'btn-default'},
        { name: 'DEBUG', class: 'btn-success'},
        { name: 'INFO', class: 'btn-info'},
        { name: 'WARN', class: 'btn-warning'},
        { name: 'ERROR', class: 'btn-danger'}
    ];

    var html = "<div class=\"btn-group\">";

    $.each(loggingLevels, function (idx, value) {
        var name = value['name'];
        var clazz = value['class'];
        if (name == data) {
            clazz += ' active';
        }

        var button_html = "<button type=\"button\" class=\"btn btn-primary " + clazz
            + "\" data-logger=\"" + full['name']
            + "\" data-level=\"" + name + "\">" +
            name
            + "</button>";

//        button_html += "<input type=\"radio\" name=\"options\" id=\"" + name
//            + "\" data-logger=\"" + full['name']
//            + "\" data-level=\"" + name + "\">" + name + "</input>";

        html += button_html;
    });

    html += "</div>";

    return html;
}

var stompClient = null;

$(function () {
    $.ajax({
        type: "GET",
        url: applicationBaseUrl + "/rest/logs/hostname",
        contentType: 'application/json'
    }).complete(function (data) {
        $('#hostname').children().text(data.responseText)
    });

    $('.datatable table').dataTable({
        "sAjaxSource": applicationBaseUrl + "/rest/logs",
        "sAjaxDataProp": "",
        "bStateSave": true,
        "bJQueryUI": false,
        "bAutoWidth": false,
        "sPaginationType": "full_numbers",
        "sDom": '<"datatable-header"fl><"datatable-scroll"t><"datatable-footer"ip>',
        "oLanguage": {
            "sSearch": "<span>Filter:</span> _INPUT_",
            "sLengthMenu": "<span>Show entries:</span> _MENU_",
            "oPaginate": { "sFirst": "First", "sLast": "Last", "sNext": ">", "sPrevious": "<" }
        },
        "aoColumnDefs": [
            {
                "bSortable": true,
                "bSearchable": true,
                "aTargets": [ 0 ],
                "sClass": "text-left",
                "mData": 'name'
            },
            {
                "bSortable": false,
                "bSearchable": false,
                "aTargets": [ 1 ],
                "sClass": "text-center",
                "sWidth": "450px",
                "mData": 'level',
                "mRender": function (data, type, full) {
                    return renderLoggingLevelButtons(data, type, full);
                }
            }
        ],
        "aaSorting": [
            [0, 'desc']
        ],
        "fnDrawCallback": fnDrawCallback
    });

    $(".dataTables_length select").select2({
        minimumResultsForSearch: "-1"
    });

    $('.dataTables_filter input[type=text]').attr('placeholder', 'Type to filter...');

    connect();
});

function connect() {
    if(window.WebSocket) {
        var socket = new SockJS(applicationBaseUrl + '/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            if (console.log) {
                console.log('Connected: ' + frame);
            }
            stompClient.subscribe('/topic/logs', function (greeting) {
                show(JSON.parse(greeting.body));
            });
        });
    } else {
        $("#logger-output").parent().html(
                "<div class=\"alert alert-block alert-danger fade in widget-inner\">"
            +"<h5>Get a new Web Browser!</h5>"
            +"<hr>"
            + "<p>Your browser does not support WebSockets. You won\'t be able to receive logging events.<br>"
            +"Please use a Web Browser with WebSockets support (WebKit or Google Chrome).</p>"
            +"</div>"
        );
    }
}

$( window ).unload(function() {
    disconnect();
});

function disconnect() {
    stompClient.disconnect();
    console.log("Disconnected");
}

function show(loggingEvent) {
    $("#logger-output").append(loggingEvent['message'] + '\n');
}