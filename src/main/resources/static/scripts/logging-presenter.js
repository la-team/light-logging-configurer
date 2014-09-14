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
function LoggingPresenter(view) {

    var stompClient = null;

    function showLegacyBrowserMessage() {
        $(view).parent().html(
                "<div class=\"alert alert-block alert-danger fade in widget-inner\">"
                + "<h5>Get a new Web Browser!</h5>"
                + "<hr>"
                + "<p>Your browser does not support WebSockets. You won\'t be able to receive logging events.<br>"
                + "Please use a Web Browser with WebSockets support (WebKit or Google Chrome).</p>"
                + "</div>"
        );
    }

    function showLegacyContainerMessage(errorMessage) {
        $(view).parent().html(
                "<div class=\"alert alert-block alert-danger fade in widget-inner\">"
                + "<h5>Something bad happened!</h5>"
                + "<hr>"
                + "<p>" + errorMessage + "</p>"
                + "</div>"
        );
    }

    function showLoggingEvent(loggingEvent) {
        $(view).append(loggingEvent['message'] + '\n');
    }

    function successCallback(frame) {
        if (console.log) {
            console.log('Connected: ' + frame);
        }
        stompClient.subscribe('/topic/logs', function (data) {
            showLoggingEvent(JSON.parse(data.body));
        });
    }

    function failureCallback(error) {
        showLegacyContainerMessage(error);
    }

    function connect(url) {
        var socket = new SockJS(url);
        stompClient = Stomp.over(socket);
        stompClient.connect({}, successCallback, failureCallback);
    }

    function disconnect() {
        if (stompClient !== null) {
            stompClient.disconnect();
            if (console.log) {
                console.log("Disconnected");
            }
        }
    }

    return {
        initialize: function (url) {
            if (window.WebSocket) {
                connect(url);
            } else {
                showLegacyBrowserMessage();
            }
        },
        destroy: function () {
            disconnect();
        }
    }
}