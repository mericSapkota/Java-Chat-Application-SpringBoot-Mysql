
'use strict';
import * as attribute from "./main.js";

const chatPage = document.querySelector('#chat-page');
const usernameForm = document.querySelector('#usernameForm');
const messageForm = document.querySelector('#individualmessageForm');
const messageInput = document.querySelector('#message');
const connectingElement = document.querySelector('.connecting');
const chatArea = document.querySelector('#chat-messages');
const logout = document.querySelector('#logout');
var stompClient = null;
let selectedUserId = null;

let fullname=attribute.fullname;
let nickname= attribute.username;

 console.log("fullname");
 
 function connect(event) {
	console.log("hio");
  
	
	
    if (nickname && fullname) {
       
        chatPage.classList.remove('hidden');

        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {
    stompClient.subscribe(`/user/${nickname}/queue/messages`, onMessageReceived);
    stompClient.subscribe(`/user/public`, onMessageReceived);
	console.log("hio");
    // register the connected user
    stompClient.send("/app/user.addUser",
        {},
        JSON.stringify({nickName: nickname, fullName: fullname, status: 'ONLINE'})
    );
  
    findAndDisplayConnectedUsers().then();
}

async function findAndDisplayConnectedUsers() {
    const connectedUsersResponse = await fetch('/users');
    let connectedUsers = await connectedUsersResponse.json();
    connectedUsers = connectedUsers.filter(user => user.nickName !== nickname);
    const connectedUsersList = document.getElementById('connectedUsers');
    connectedUsersList.innerHTML = '';

    connectedUsers.forEach(user => {
		
		let username = user.nickName;
					let url= `http://localhost:8080/api/getUser/${username}`;
					fetch(url).then(response=>{
						if(!response.ok){
							console.log("error in restapi");
						}
						return response.json();
					}).then(			data => {
					
						    console.log('User data:', data.img);
							user.img = data.img;
							appendUserElement(user, connectedUsersList);
					})
		
       
        if (connectedUsers.indexOf(user) < connectedUsers.length - 1) {
            const separator = document.createElement('li');
            separator.classList.add('separator');
            connectedUsersList.appendChild(separator);
        }
		
		
    });
	
}


function appendUserElement(user, connectedUsersList) {
	

	
	const listItem = document.createElement('li');
	  listItem.classList.add('user-item');

	  const userImage = document.createElement('img');
	  let userImagebe = user.img 
	  console.log("user img"+ userImagebe)   // Use fetched image or default icon
	  userImage.alt = user.fullName;

    const usernameSpan = document.createElement('span');
    usernameSpan.textContent = user.fullName;

    const receivedMsgs = document.createElement('span');
    receivedMsgs.textContent = '0';
    receivedMsgs.classList.add('nbr-msg',"hidden");
	let messageList = "";

	messageList = `
		<div id="${user.nickName}" class="entry cursor-pointer transform hover:scale-105 duration-300 transition-transform bg-white mb-4 rounded p-4 flex shadow-md">
		                                       <div class="flex-2">
		                                           <div class="w-12 h-12 relative">
		                                               <img class="w-12 h-12 rounded-full mx-auto object-cover" src="${userImagebe!=null? 'data:image/jpeg;base64,' + userImagebe : "/img/user_icon.png"}"  alt="chat-user" />
		                                               <span class="absolute w-4 h-4 bg-green-400 rounded-full right-0 bottom-0 border-2 border-white"></span>
		                                           </div>
		                                       </div>
		                                       
		                                       
		                                       <div class="flex-1 px-2">
		                                           <div class="truncate w-32"><span class="text-gray-800">${user.fullName}</span></div>
		                                           <div><small class="text-gray-600">Yea, Sure!</small></div>
		                                       </div>
		                                       <div class="flex-2 text-right">
		                                           <div><small class="text-gray-500">15 April</small></div>
		                                           <div>
		                                               <small class="text-xs bg-red-500 text-white rounded-full h-6 w-6 leading-6 text-center inline-block">
		                                                   10
		                                               </small>
		                                           </div>
		                                       </div>
		                                   </div>
		`
    listItem.innerHTML += messageList;
	listItem.id = user.nickName;
	listItem.appendChild(receivedMsgs);
    listItem.addEventListener('click', userItemClick);

    connectedUsersList.appendChild(listItem);
}

function userItemClick(event) {
    document.querySelectorAll('.user-item').forEach(item => {
        item.classList.remove('active');
    });
    messageForm.classList.remove('hidden');

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    selectedUserId = clickedUser.getAttribute('id');
    fetchAndDisplayUserChat().then();

    const nbrMsg = clickedUser.querySelector('.nbr-msg');
    nbrMsg.classList.add('hidden');
    nbrMsg.textContent = '0';

}

function displayMessage(senderId, content) {
    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message','flex');
    if (senderId === nickname) {
        messageContainer.classList.add('sender','justify-center',"text-left","bg-green-300","w-max","rounded-xl","p-2","m-5");
    } else {
        messageContainer.classList.add('receiver',"text-right","bg-yellow-200","w-max","rounded-xl","p-2",'m-5',"ml-auto");
    }
    const message = document.createElement('p');
    message.textContent = content;
    messageContainer.appendChild(message);
    chatArea.appendChild(messageContainer);
}

async function fetchAndDisplayUserChat() {
    const userChatResponse = await fetch(`/messages/${nickname}/${selectedUserId}`);
    const userChat = await userChatResponse.json();
    chatArea.innerHTML = '';
    userChat.forEach(chat => {
        displayMessage(chat.senderId, chat.content);
    });
    chatArea.scrollTop = chatArea.scrollHeight;
}


function onError() {
	console.log("here");
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    const messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        const chatMessage = {
            senderId: nickname,
            recipientId: selectedUserId,
            content: messageInput.value.trim(),
            timestamp: new Date()
        };
        stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
        displayMessage(nickname, messageInput.value.trim());
        messageInput.value = '';
    }
    chatArea.scrollTop = chatArea.scrollHeight;
    event.preventDefault();
}


async function onMessageReceived(payload) {
    await findAndDisplayConnectedUsers();
    console.log('Message received', payload);
    const message = JSON.parse(payload.body);
    if (selectedUserId && selectedUserId === message.senderId) {
        displayMessage(message.senderId, message.content);
        chatArea.scrollTop = chatArea.scrollHeight;
    }

    if (selectedUserId) {
        document.querySelector(`#${selectedUserId}`).classList.add('active');
    } else {
        messageForm.classList.add('hidden');
    }

    const notifiedUser = document.querySelector(`#${message.senderId}`);
    if (notifiedUser && !notifiedUser.classList.contains('active')) {
        const nbrMsg = notifiedUser.querySelector('.nbr-msg');
        nbrMsg.classList.remove('hidden');
        nbrMsg.textContent = '';
    }
}

function onLogout() {
    stompClient.send("/app/user.disconnectUser",
        {},
        JSON.stringify({nickName: nickname, fullName: fullname, status: 'OFFLINE'})
    );
    window.location.reload();
}

messageForm.addEventListener('submit', sendMessage, true);
window.onload=function(){
	setTimeout(connect,2000);
}

window.onbeforeunload = () => onLogout();
