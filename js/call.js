document.addEventListener("DOMContentLoaded", function () {
  const callBtn = document.getElementById("call");
  const callContainer = document.getElementById("call-container");

  let formVisible = false;

  callBtn.addEventListener("click", function () {
    
    if (formVisible) {
      callContainer.innerHTML = "";
      formVisible = false;
      return;
    }

    formVisible = true;

    // Create form elements
    const form = document.createElement("form");
    form.className = "call-form";

    const label = document.createElement("label");
    label.textContent = "Enter Friend's Email ID:";

    const input = document.createElement("input");
    input.type = "email";
    input.required = true;
    input.placeholder = "example@email.com";

    const submitBtn = document.createElement("button");
    submitBtn.type = "submit";
    submitBtn.textContent = "Call";

    form.appendChild(label);
    form.appendChild(input);
    form.appendChild(submitBtn);

    callContainer.innerHTML = "";
    callContainer.appendChild(form);

    form.addEventListener("submit", function (e) {
      e.preventDefault();
      const email = input.value.trim();

      if (email !== "") {
        callContainer.innerHTML = `
          <div class="call-message">
            Calling <strong>${email}</strong>...
          </div>
        `;
        formVisible = false;

             fetch("../call", {
                  method: "POST",
                  headers: {
                    "Content-Type": "application/json"
                  },
                  body: JSON.stringify({
                    friendMailId: email
                  })
             })
            .then(response => {
              if (!response.ok) {
                throw new Error("Call failed");
              }
              return response.json();
            })
            .then(data => {
              if (data.meetingUrl) {
                callContainer.innerHTML = `
                  <div class="call-message">
                    ${data.message}<br/>
                    <a href="${data.meetingUrl}" target="_blank" class="meeting-link">Join Meeting</a>
                  </div>
                `;
                
                  // Send meeting invite to friend via email
                  fetch("/vplay/mail", {
                    method: "POST",
                    headers: {
                      "Content-Type": "application/x-www-form-urlencoded"
                    },
                    body: `userName=Friend&emailID=${encodeURIComponent(email)}&subject=${encodeURIComponent("Vplay Call Invitation")}&body=${encodeURIComponent("Hi,\n\nYou’ve been invited to a Vplay meeting.\nClick to join: " + data.meetingUrl)}`
                  })
                  .then(res => res.text())
                  .then(mailResp => {
                    console.log("Call invite email sent:", mailResp);
                  })
                  .catch(err => {
                    console.error("Email send failed:", err);
                  });

               } 
               else {
                 callContainer.innerHTML = `<div class="error">${data.message}</div>`;
                 }
            })
            .catch(error => {
              console.error("Call error:", error);
              callContainer.innerHTML = `<div class="error">An error occurred: ${error.message}</div>`;
            });
         }
    });
  });
});
