/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * TODO: Document me!
 *
 * @author Maxim Kharchenko (kharchenko.max@gmail.com)
 */

function LightDataTable(table, url) {

    function renderLoggingLevelButtons(data, full) {
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
            html += renderLoggingLevelButton(full['name'], name, clazz);
        });

        html += "</div>";

        return html;
    }

    function renderLoggingLevelButton(logger, level, clazz) {
        return "<button type=\"button\" class=\"btn btn-primary " + clazz
            + "\" data-logger=\"" + logger
            + "\" data-level=\"" + level + "\">" + level + "</button>";
    }

    function fnDrawCallback() {
        $(":button", $('.datatable table')).click(function () {
            var logger = $(this).attr('data-logger');
            var level = $(this).attr('data-level');

            $.ajax({
                type: "PUT",
                url: url,
                contentType: 'application/json',
                data: JSON.stringify({"name": logger, "level": level})
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

    function initialize() {
        $(table).dataTable({
            "sAjaxSource": url,
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
                        return renderLoggingLevelButtons(data, full);
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
    }

    return {
        initialize: function () {
            initialize();
        }
    }
}