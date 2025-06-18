document.addEventListener("DOMContentLoaded", function () {
  const addFriendBtn = document.getElementById("addFriend");
  const requestContainer = document.getElementById("request-container");

  let isVisible = false;

  addFriendBtn.addEventListener("click", function () {
    if (isVisible) {
      requestContainer.innerHTML = "";
      isVisible = false;
      return;
    }
    isVisible = true;

    const form = document.createElement("form");
    form.className = "request-form";

    const label = document.createElement("label");
    label.textContent = "Enter the Mail ID to send Request";

    const input = document.createElement("input");
    input.type = "email";
    input.required = true;
    input.placeholder = "example@email.com";

    const submitBtn = document.createElement("button");
    submitBtn.type = "submit";
    submitBtn.textContent = "Request";

    form.appendChild(label);
    form.appendChild(input);
    form.appendChild(submitBtn);

    requestContainer.innerHTML = ""; 
    requestContainer.appendChild(form);

    form.addEventListener("submit", function (e) {
  e.preventDefault();
  const email = input.value.trim();

  if (email !== "") {
    // Make request
    fetch("../sendRequest", {
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
          throw new Error("Network response was not ok");
        }
        return response.json(); 
      })
      .then(data => {
        requestContainer.innerHTML = `
          <div class="request-message">
            ${data.message}
          </div>
        `;
        isVisible = false;
      })
      .catch(error => {
        console.error("Fetch error:", error);
        requestContainer.innerHTML = `
          <div class="error-message">
            Something went wrong: ${error.message}
          </div>
        `;
        isVisible = false;
      });
  }
});

  });
});
