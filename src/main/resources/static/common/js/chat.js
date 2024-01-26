window.addEventListener("DOMContentLoaded", function() {
    const webSocket = new WebSocket("ws://localhost:3000/chat");

    webSocket.onopen = function(e) {
        console.log("연결 성립!", e);

        webSocket.send("메세지!");
    };

    webSocket.onclose = function(e) {
        console.log("연결 종료!", e);
    };

    // 서버쪽에서 전송 -> 수신
    webSocket.onmessage = function(message) {
        console.log(message);
    };


});