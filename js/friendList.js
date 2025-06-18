document.addEventListener("DOMContentLoaded", function(){
    const friendsBtn = document.getElementById("friendList");
    const container  = document.getElementById("friendList-container");
    
    let isVisible = false;
    
    friendsBtn.addEventListener("click", function(){
        if(isVisible){
            container.innerHTML = "";
            container.style.display = "none"; 
            isVisible = false;
            return;
        }

        fetch("../friends")  
        .then(response => response.json())
        .then(data => {
            container.innerHTML = "";
            container.style.display = "block";
            isVisible = true;

            if (data.error) {
                const p = document.createElement("p");
                p.textContent = "Error: " + data.error;
                p.style.color = "red";
                container.appendChild(p);
                return;
            }

            data.forEach(friend => {
                const div = document.createElement("div");
                div.className = "friend-card"; 
                div.innerHTML = `
                    <h3>${friend.userName}</h3>
                    <p>${friend.emailId}</p>
                `;
                container.appendChild(div);
            });
        })
        .catch(err => {
            console.error("Fetch error:", err);
        });
    });
});

