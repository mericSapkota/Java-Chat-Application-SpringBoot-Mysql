'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#group-chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageform');
var messageInput = document.querySelector('#message-input');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
var groupChatButton = document.querySelector('.group-chat');
var crossButton = document.querySelector(".cross-button");
var stompClient = null;
var username = null;
var chatArea = document.querySelector(".chat-area");


var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
	event.preventDefault();
    username = document.querySelector('#name').value.trim();
	

    if(username) {
		
        usernamePage.classList.add('hidden');
		chatPage.classList.remove("hidden");
		console.log("username");
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);

    // Tell your username to the server
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )

    connectingElement.classList.add('hidden');
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
	
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();
	console.log(messageContent+"message  content");
	console.log(stompClient+"stomp client");
    if(messageContent && stompClient) {

        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT'
        };
		console.log("sendMessage");
        stompClient.send("/app/chat.sendMessage", {username},  JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');
	var divElement = document.createElement('div');
	messageElement.classList.add('flex','flex-col');
	divElement.classList.add("flex", 'gap-2','justify-start','items-center');
    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message','text-center');
        message.content = message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message','text-center');
        message.content = message.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');
		
		 

        var avatarElement = document.createElement('div');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);
		avatarElement.classList.add("h-10", "w-10", "rounded-full","flex","justify-center","items-center");
      
        var usernameElement = document.createElement('div');
        var usernameText = document.createTextNode(message.sender);
		usernameElement.appendChild(usernameText);
		divElement.appendChild(avatarElement);
		divElement.appendChild(usernameElement);
       
		
		messageElement.appendChild(divElement);
		
       
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);
	textElement.classList.add("pl-3","pt-3");
    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
	
}



function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
		console.log(messageSender.charCodeAt(i));
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

usernameForm.addEventListener('submit',connect);

messageForm.addEventListener('submit', sendMessage)



groupChatButton.addEventListener("click",()=>{
	crossButton.classList.remove("hidden");
	usernamePage.classList.remove("hidden");
	chatArea.classList.add("hidden");
});

crossButton.addEventListener("click",()=>{
	location.reload();	
})
