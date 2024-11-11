const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/stomp/chats'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    // 서버 접속할때 사용자 목록 호출
    showChatrooms();
    console.log('Connected: ' + frame);
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
    $("#create").prop("disabled" , !connected);
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {

    let chatroomId = $("#chatroom-id").val();

    stompClient.publish({
        destination: "/pub/chats/" + chatroomId,
        body: JSON.stringify(
            {'message': $("#message").val()})
    });
    $("#message").val("")
}


// 채팅방 생성
function createChatroom() {
    $.ajax({
        type: 'POST',
        dataType: 'json',
        url: '/chats?title=' + $("#chatroom-title").val(),
        success: function (data) {
            console.log("data : ", data);
            showChatrooms(); // 채팅방 목록
            enterChatroom(data.id, true); // 진입
        },
        error: function (request, status, error) {
            console.log("request :", request);
            console.log("error :", error);
        }
    })
}

// 사용자가 참여한 채팅방 목록
function showChatrooms() {
    $.ajax({
        type: 'GET',
        dataType: 'json',
        url: '/chats',
        success: function (data) {
            console.log("data :", data);
            renderChatrooms(data);
        },
        error: function (request, status, error) {
            console.log("request :", request);
            console.log("error :", error);
        }
    })
}


let subscription;

// 채팅방 입장
function enterChatroom(chatroomId, newMember) {
    $("#chatroom-id").val(chatroomId); // 채팅방 아이디
    $("#messages").html(""); // 기존 메시지 지움
    showMessages(chatroomId); // 과거 메시지 가져오기
    $("#conversation").show(); // 메시지 표시 열고
    $("#send").prop("disabled", false);
    $("#leave").prop("disabled", false);

    // 채팅방에서 채팅방 옮겨가는거
    // 기존 구독을 변경해줘야함
    if (subscription != undefined) {
        subscription.unsubscribe();
    }

    // 특정 채팅방에서 구독함.
    subscription = stompClient.subscribe('/sub/chats/' + chatroomId, (chatMessage) => {
        showMessage(JSON.parse(chatMessage.body));
    });

    // 방에 입장 표시 해주기.
    if (newMember) {
        stompClient.publish({
            destination: "/pub/chats/" + chatroomId,
            body: JSON.stringify(
                {'message': "님이 방에 들어왔습니다."})
        })
    }
}

// 채팅방 목록을 이용해서 html 그려줌
function renderChatrooms(chatrooms) {
  $("#chatroom-list").html("");
  for (let i=0;  i < chatrooms.length; i++) {
    $("#chatroom-list").append(
        "<tr onclick='joinChatroom("+ chatrooms[i].id +")'><td>"
        + chatrooms[i].id + "</td><td>" + chatrooms[i].title + "</td><td>"
        + chatrooms[i].memberCount + "</td><td>" + chatrooms[i].createdAt + "</td></tr>"
    )
  }
}

function showMessages(chatroomId) {
    $.ajax({
        type:"GET",
        dataType:"json",
        url:"/chats/" + chatroomId + '/message',
        success : function (data) {
            console.log("data : " , data);
            for (let i = 0; i < data.length; i++) {
                showMessage(data[i]);
            }
        },
        error: function (request, status, error) {
            console.log("request :", request);
            console.log("error :", error);
        }
    })
}

function showMessage(chatMessage) {
    $("#messages").append(
        "<tr><td>" + chatMessage.sender + " : " + chatMessage.message
        + "</td></tr>");
}

function joinChatroom(chatroomId) {
    $.ajax({
        type : 'POST',
        dataType : 'json',
        url: '/chats/' + chatroomId,
        success: function (data) { // 방에 들어갈수 있게 처리
            console.log("data :" , data);
            enterChatroom(chatroomId , data); // 진입
        },
        error: function (request, status, error) {
            console.log("request :", request);
            console.log("error :", error);
        }
    })
}

// 채팅방 나가기
function leaveChatroom() {
    let chatroomId = $("#chatroom-id").val();
    $.ajax({
        type : "DELETE",
        dataType : "json",
        url: "/chats/" + chatroomId,
        success: function (data) {
            console.log("data : " , data);
            showChatrooms(); // 채팅방 목록 갱신
            exitChatroom(chatroomId); // 나온 채팅방
        },
        error: function (request, status, error) {
            console.log("request :", request);
            console.log("error :", error);
        }
    })
}

function exitChatroom(chatroomId) {
    $("#chatroom-id").val(""); // 채팅방 나갔으니
    $("#conversation").hide(); // 메시지 닫아줌
    $("#send").prop("disabled", true);
    $("#leave").prop("disabled", true);
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $("#connect").click(() => connect());
    $("#disconnect").click(() => disconnect());
    $("#create").click(() => createChatroom());
    $("#leave").click(() => leaveChatroom());
    $("#send").click(() => sendMessage());
});