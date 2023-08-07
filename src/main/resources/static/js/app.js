let port = window.location.port;
let url = window.location.hostname + (port ? ':' + port : '');
//let httpUrl = window.location.protocol + '//' + window.location.hostname + (port ? ':' + port : '') + '/';
let socket = "ws://"+url+"/holidayDessert/chat"

const stompClient = new StompJs.Client({
	brokerURL: socket
//    brokerURL: 'ws://localhost:8080/holidayDessert/chat'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/greetings', (greeting) => {
        showGreeting(greeting.body);
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendMsgContent() {
    stompClient.publish({
        destination: "/app/chat",
        body: JSON.stringify({'memId': $("#memId").val(),'msgContent': $("#msgContent").val(),'msgDirection': 1})
    });
}

function showGreeting(greeting) {
	let msgContent = JSON.parse(greeting).msgContent
	let msgTime = JSON.parse(greeting).msgTime
    $("#greetings").append("<tr><td>" + msgContent + "</td><td>" + msgTime + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $("#connect").click(() => connect());
    $("#disconnect").click(() => disconnect());
    $("#send").click(() => sendMsgContent());
});