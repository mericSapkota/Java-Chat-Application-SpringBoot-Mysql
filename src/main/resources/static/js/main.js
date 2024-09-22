export let username = document.querySelector(".username").innerHTML;
export let fullname = document.querySelector(".fullname").innerHTML;

let mainBody = document.querySelector(".main-body");
let settingsBody = document.querySelector(".settings");
let setting = document.querySelector(".setting");
let chat = document.querySelector(".chat");

document.querySelector(".setting").addEventListener("click",()=>{
	mainBody.classList.add("hidden");
	settingsBody.classList.remove("hidden");
	chat.classList.remove("border-gray-800", "bg-gray-300");
	setting.classList.add("border-gray-800", "bg-gray-300");
});
chat.addEventListener("click",()=>{
	mainBody.classList.remove("hidden");
	settingsBody.classList.add("hidden");
	chat.classList.add("border-gray-800", "bg-gray-300");
	setting.classList.remove("border-gray-800", "bg-gray-300");
})

document.addEventListener("DOMContentLoaded",()=>{
	console.log("dom loaded");
	let success = document.querySelector(".success");
	let failed = document.querySelector(".failed");
	if(success){
		setTimeout(()=>{
			success.classList.add("opacity-0","transition-opacity","duration-1000");
			setTimeout(()=>{
				success.classList.add("hidden");

			},1000) 		},3000);
	}
	if(failed){
		setTimeout(()=>{
			
			failed.classList.add("opacity-0","transition-opacity","duration-1000");
				setTimeout(()=>{
							failed.classList.add("hidden");

						},1000) 
			
		},3000);
	}
})