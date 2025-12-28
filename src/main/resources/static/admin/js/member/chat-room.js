let stompClient = null;
let currentSubscription = null;

$('.chat-room').on('click', function() {
	let roomUrl = $(this).data('roomurl')
    connectChatRoom(roomUrl);
});

function connectChatRoom(roomUrl) {
    if (!roomUrl) return;
	isChatOpen = true;
	$('.chat-container').show();
    // 若已有連線，先清掉
    if (stompClient) {
        if (currentSubscription) {
            currentSubscription.unsubscribe();
            currentSubscription = null;
        }
        stompClient.deactivate();
        stompClient = null;
    }
    stompClient = new StompJs.Client({
		webSocketFactory: () => new SockJS('/holidayDessert/ws-chat'),
        reconnectDelay: 5000,
        debug: (msg) => console.log(msg)
    });
    stompClient.onConnect = () => {
        console.log('Connected room:', roomUrl);
        currentSubscription = stompClient.subscribe(
            '/topic/chat/' + roomUrl,
            (message) => {
                const data = JSON.parse(message.body);
				appendMessage(data);
            }
        );
    };
    stompClient.onWebSocketError = (error) => {
        console.error('WebSocket error', error);
    };
    stompClient.onStompError = (frame) => {
        console.error('STOMP error', frame.headers['message']);
    };
    stompClient.activate();
}


let isChatOpen = false;
let socket = null;
let roomUrl = null;

// Thymeleaf 塞進來
const memId = /*[[${memId}]]*/ '';
const empId = /*[[${empId}]]*/ '';
const memberSession = /*[[${memberSession}]]*/ '';


$('#closeChatRoom').on('click', function () {
    isChatOpen = false;
    $('.chat-container').hide();

    if (stompClient) {
        stompClient.deactivate();
        stompClient = null;
    }

    if (socket) {
        socket.close();
        socket = null;
    }
});

function appendMessage(msg) {
    const mem = msg.memId === memId;

    const html = mem
        ? `<div class="message right">
                <div class="message-right-text">${msg.content}</div>
           </div>`
        : `<div class="message left">
                <div class="avatar"><img src="user1_avatar.jpg"></div>
                <div class="message-left-text">${msg.content}</div>
           </div>`;

    $('#chatMessages').append(html);
}

$('#sendMessage').click(function() {
//        var empId = $('#loginEmpId').val();
//        var memId = $('#loginMemId').val();
        var msgContent = $('#msgContent').val();
//        if (!memId) {
//            alert('請登入');
//            return;
//        }
        // 假設你已經有 stompClient 和 roomUrl
        if (typeof stompClient === 'undefined' || !roomUrl) return;

        var msg = {
            memId: memId,
            empId: empId,
            content: msgContent,
            roomUrl: roomUrl
        };
        // 發送訊息
        stompClient.send('/topic/chat/' + roomUrl, {}, JSON.stringify(msg));
        // 清空輸入框
        $('#msgContent').val('');
});

//function connectWebSocket(roomUrl) {
//	
//    if (!roomUrl) {
//        alert('找不到聊天室連線位址');
//        return;
//    }
//
//    ws = new WebSocket(roomUrl);
//    // 連線成功
//    ws.onopen = function () {
//        console.log('WebSocket 已連線：' + roomUrl);
//    };
//    // 接收訊息
//    ws.onmessage = function (event) {
//        console.log('收到訊息：', event.data);
//        // 這裡可以把訊息顯示到畫面上
//    };
//    // 關閉連線
//    ws.onclose = function () {
//        console.log('WebSocket 已關閉');
//    };
//    // 發生錯誤
//    ws.onerror = function (error) {
//        console.error('WebSocket 發生錯誤', error);
//    };
//}