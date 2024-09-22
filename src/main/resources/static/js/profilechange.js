let profilo = document.querySelector('.change-profile-js');
let optiono = document.getElementById("change-profile");
let pp = document.querySelector(".pp");
let profileOptionOn= 0;


function profileChange(){
	if(profileOptionOn===0){
		optiono.classList.remove("hidden");	
		profileOptionOn++;
	}
	else{
		optiono.classList.add("hidden");
		profileOptionOn = 0;
	}
	
}


profilo.addEventListener("click",()=>{
	profileChange();
	let profilepic = pp.querySelector("img");
	let profilepicture = profilepic.src;
	console.log(profilepicture);
	
	let profile =profilepicture.src ? profilepicture: '<svg  xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class=" h-12 w-12">'+
					'<path stroke-linecap="round" stroke-linejoin="round" d="M17.982 18.725A7.488 7.488 0 0 0 12 15.75a7.488 7.488 0 0 0-5.982 2.975m11.963 0a9 9 0 1 0-11.963 0m11.963 0A8.966 8.966 0 0 1 12 21a8.966 8.966 0 0 1-5.982-2.275M15 9.75a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z" />'+
				  '</svg>';
	optiono.innerHTML = "";			  
	optiono.innerHTML += `
		<div class="h-20 w-20 rounded-full  flex justify-center items-center">
		${profile}
		</div>
		<form method="post" action="changeProfile" enctype="multipart/form-data" class="flex flex-col gap-4 items-center w-max">
				    <label for="profile" class="text-lg font-medium">Change Image</label>
				    
				    <input type="file" name="profile" id="profile" class="w-3/4 border p-2 rounded" required accept="image/*">
				    
				    <button type="submit" class="bg-red-500 hover:bg-red-600 text-white font-bold py-2 px-4 rounded">
				        Upload
				    </button>
					
			    </form>
	`;
})
