let stompClient = null;
let currentSubscription = null;
let roomId = null;
let roomUrl = null;
let memId = null;
let empId = null;
let firstName = null;
let isChatOpen = false;
let socket = null;
let msgDirection = '0';

$('.chat-room').on('click', function() {
	$('#member-name').text($(this).data('memname'));
	roomId = $(this).data('roomid');
	roomUrl = $(this).data('roomurl');
	memId = $(this).data('memid');
	empId = $('#loginEmpId').val();
	firstName = $(this).data('memname').charAt(0);
	getMessageByEmp();
    connectChatRoom(roomUrl);
	
});
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
$('#sendMessage').click(function() {
        var msgContent = $('#msgContent').val();
		if (msgContent == '') return;
        // 假設你已經有 stompClient 和 roomUrl
		if (!stompClient || !stompClient.connected || !roomUrl) {
		    console.warn('stompClient not ready');
		    return;
		}
        if (typeof stompClient === 'undefined' || !roomUrl) return;

        var msg = {
            memId: memId,
            empId: empId,
            msgContent: msgContent,
			roomId: roomId,
            roomUrl: roomUrl,
			msgDirection: msgDirection
        };
        // 發送訊息
		stompClient.publish({
		    destination: "/app/chat/" + roomUrl,
		    body: JSON.stringify(msg)
		});
        // 清空輸入框
        $('#msgContent').val('');
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
function appendMessage(msg) {
    const emp = msg.empId;
    const html = emp
        ? `<div class="message chat-right">
                <div class="message-right-text">${msg.msgContent}</div>
           </div>`
        : `<div class="message chat-left">
                <div class="avatar"><span class="first-char">${firstName}</span></div>
                <div class="message-left-text">${msg.msgContent}</div>
           </div>`;

    $('#chatMessages').append(html);
	scrollToBottom();
}

function scrollToBottom() {
	const chat = document.getElementById('chatMessages'); // 原生 DOM
  	chat.scrollTo({
    	top: chat.scrollHeight,
    	behavior: 'smooth'
	});
}

function getMessageByEmp() {
	$('#chatMessages').empty();
	$.ajax({
	    url: "/holidayDessert/getMessageByEmp",
	    method: 'POST',
		contentType: 'application/json',
		data: JSON.stringify({
			memId: memId,
		    empId: empId
		}),
		success: function(response) {
		    if (response.code === 200) {
				const list = response.result;
				let messageHtml = '';
				for (let i = 0; i < list.length; i++) {
				    const msgDirection = list[i].MSG_DIRECTION; // 1 或 0
				    const content = list[i].MSG_CONTENT;
					const memFirstName = list[i].MEM_NAME ? list[i].MEM_NAME.charAt(0) : '';
				    if (msgDirection === 1) {
				        messageHtml += `
				            <div class="message chat-left">
								<div class="avatar"><span class="first-char">${memFirstName}</span></div>
				                <div class="message-left-text">${content}</div>
				            </div>
				        `;
				    } else if (msgDirection === 0) {
				        messageHtml += `
				            <div class="message chat-right">
				                <div class="message-right-text">${content}</div>
				            </div>
				        `;
				    }
				}
				$('#chatMessages').append(messageHtml);
		    } else {
		        alert('取得客服對會員對話紀錄失敗：' + response.message);
		    }
		},
	    error: function(xhr, status, error) {
	        console.error('API調用失敗: ' + error);
			alert('取得客服對會員對話紀錄失敗，請稍後再試');
	    }
	});
}

