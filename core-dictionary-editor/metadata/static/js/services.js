/**
 * Created by bush on 14.07.2016.
 */
angular.module('winsocket.services', [])
    //.constant('sockJsProtocols', ["xhr-streaming", "xhr-polling"]) // only allow XHR protocols
    .constant('sockJsProtocols', [])
    //this client for rest services Websocket impl Stomp (in param: protocol, defered)
    .factory('StompClient', ['sockJsProtocols', '$q', function (sockJsProtocols, $q) {
        var stompClient;
        var wrappedSocket = {
            init: function (url) {
                if (sockJsProtocols.length > 0) {
                    stompClient = Stomp.over(new SockJS(url, null, {transports: sockJsProtocols}));
                }
                else {
                    stompClient = Stomp.over(new SockJS(url));
                }
            },
            //connect to rest serv - winsocket
            connect: function () {
                return $q(function (resolve, reject) {
                    if (!stompClient) {
                        reject("STOMP client not created");
                    } else {
                        stompClient.connect({}, function (frame) {
                            resolve(frame);
                        }, function (error) {
                            reject("STOMP protocol error " + error);
                        });
                    }
                });
            },
            //disconnect to rest serv - winsocket
            disconnect: function() {
                stompClient.disconnect();
            },
            //consumer to rest serv - winsocket
            subscribe: function (destination) {
                var deferred = $q.defer();
                if (!stompClient) {
                    deferred.reject("STOMP client not created");
                } else {
                    stompClient.subscribe(destination, function (message) {
                        deferred.notify(JSON.parse(message.body));
                    });
                }
                return deferred.promise;
            },
            //consumer to rest serv - winsocket
            subscribeSingle: function (destination) {
                return $q(function (resolve, reject) {
                    if (!stompClient) {
                        reject("STOMP client not created");
                    } else {
                        stompClient.subscribe(destination, function (message) {
                            resolve(JSON.parse(message.body));
                        });
                    }
                });
            },
            //send message to rest serv - winsocket
            send: function (destination, headers, object) {
                stompClient.send(destination, headers, object);
            }
        };
        return wrappedSocket;
    }]);
