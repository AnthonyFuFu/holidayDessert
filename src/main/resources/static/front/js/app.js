
// 自動判斷 ws / wss
const protocol = location.protocol === 'https:' ? 'wss://' : 'ws://';
let port = window.location.port;
let url = window.location.hostname + (port ? ':' + port : '');
//let httpUrl = window.location.protocol + '//' + window.location.hostname + (port ? ':' + port : '') + '/';
let socket = "ws://"+url+"/holidayDessert/chat"
console.log(socket);
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
        body: JSON.stringify({'empId': $("#empId").val(),'memId': $("#memId").val(),'msgContent': $("#msgContent").val(),'msgDirection': $("#msgDirection").val(),'msgPic': $("#msgPic").val()})
    });
}

function showGreeting(greeting) {
	let msgContent = JSON.parse(greeting).msgContent
	let msgTime = JSON.parse(greeting).msgTime
	let msgPic = JSON.parse(greeting).msgPic
    $("#greetings").append("<tr><td>" + msgContent + "</td><td>" + msgTime + "</td><td>" + msgPic + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $("#connect").click(() => connect());
    $("#disconnect").click(() => disconnect());
    $("#send").click(() => sendMsgContent());
});