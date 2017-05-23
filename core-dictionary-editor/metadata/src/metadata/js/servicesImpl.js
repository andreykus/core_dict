/**
 * Created by bush on 14.07.2016.
 */
angular.module('winsocketImpl.services', ['winsocket.services'])
    .factory('TradeService', ['StompClient', '$q', function (stompClient, $q) {
        return {
            connect: function (url) {
                stompClient.init(url);
                return stompClient.connect().then(function (frame) {
                    return frame.headers['user-name'];
                });
            },
            disconnect: function() {
                stompClient.disconnect();
            },
            loadPositions: function() {
                return stompClient.subscribeSingle("/app/positions");
            },
            fetchQuoteStream: function () {
                return stompClient.subscribe("/topic/price.stock.*");
            },
            fetchPositionUpdateStream: function () {
                return stompClient.subscribe("/user/queue/position-updates");
            },
            fetchErrorStream: function () {
                return stompClient.subscribe("/user/queue/errors");
            },
            sendTradeOrder: function(tradeOrder) {
                return stompClient.send("/app/trade", {}, JSON.stringify(tradeOrder));
            }
        };

    }]);
