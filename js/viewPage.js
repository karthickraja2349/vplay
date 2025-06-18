document.addEventListener("DOMContentLoaded", function () {
  const notificationsBtn = document.getElementById("notifications");
  const container = document.getElementById("notification-container");
  
  let isVisible = false;

  //notification
  notificationsBtn.addEventListener("click", function () {
    if (isVisible) {
      container.innerHTML = "";
      container.style.display = "none";
      isVisible = false;
      return;
    }

    fetch("/vplay/notifications")
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

        if (Array.isArray(data)) {
          data.forEach(item => {
            const div = document.createElement("div");
            div.classList.add("notification-item");

            div.innerHTML = `
              <div class="notif-text">
              
                <strong>${item.sender_name}</strong> (${item.sender_email})<br> 
                
                Status: <span class="status">${item.status}</span>
              </div>
              <div class="notif-actions">
                <button class="accept-btn">Accept</button>
                <button class="reject-btn">Reject</button>
              </div>
            `;

         div.querySelector(".accept-btn").addEventListener("click", () => {
              alert(`Accepted request from ${item.sender_name} (ID: ${item.notification_id})`);
              
             
             fetch("/vplay/accept", {
                   method: "POST",
                   headers: {
                    "Content-Type": "application/json"
                  },
                 body: JSON.stringify({
                   notification_id: item.notification_id,
                   action: "accept",
                   sender_email: item.sender_email
                })
              }).then(res => {
                if (res.ok) {
                  div.querySelector(".status").textContent = "accepted";
                }
              });
            });

              div.querySelector(".reject-btn").addEventListener("click", () => {
              alert(`Reject request from ${item.sender_name} (ID: ${item.notification_id})`);
              
             
             fetch("/vplay/accept", {
                   method: "POST",
                   headers: {
                    "Content-Type": "application/json"
                  },
                 body: JSON.stringify({
                   notification_id: item.notification_id,
                   action: "reject"
                })
              }).then(res => {
                if (res.ok) {
                  div.querySelector(".status").textContent = "rejected";
                }
              });
            });


            container.appendChild(div);
          });
        } else {
          container.textContent = "Unexpected response format.";
        }
      })
      .catch(error => {
        console.error("Error fetching notifications:", error);
      });
  });
});

