/*
let localStream;
let remoteStream;
let peerConnection;


let socketReady = false;

const config = {
  iceServers: [
    { urls: "stun:stun.l.google.com:19302" }
  ]
};

// Create WebSocket and set ready flag
//signalingSocket = new WebSocket("ws://192.168.226.108:8080/vplay/callConnect");
const signalingSocket = new WebSocket("ws://" + location.host + "/call/signaling");


signalingSocket.onopen = () => {
  console.log("WebSocket connected.");
  socketReady = true;
  document.getElementById("callBtn"); 
};

signalingSocket.onerror = (e) => {
  console.error("WebSocket error:", e);
};

signalingSocket.onclose = () => {
  console.log("WebSocket closed.");
  socketReady = false;
};

// Handle incoming messages
signalingSocket.onmessage = async (message) => {
  const data = JSON.parse(message.data);

  // Create peer connection if needed
  if (!peerConnection) {
    createPeerConnection();
  }

  if (data.offer) {
    await peerConnection.setRemoteDescription(new RTCSessionDescription(data.offer));
    const answer = await peerConnection.createAnswer();
    await peerConnection.setLocalDescription(answer);
    if (socketReady) {
      signalingSocket.send(JSON.stringify({ answer }));
    }
  }

  if (data.answer) {
    await peerConnection.setRemoteDescription(new RTCSessionDescription(data.answer));
  }

  if (data.candidate) {
    await peerConnection.addIceCandidate(new RTCIceCandidate(data.candidate));
  }
};

function start() {
  navigator.mediaDevices.getUserMedia({ video: true, audio: true })
    .then(stream => {
      localStream = stream;
      document.getElementById("localVideo").srcObject = stream;
    })
    .catch(err => {
      console.error("Media error:", err);
    });
}

function call() {
  if (!socketReady) {
    console.warn("WebSocket not connected yet.");
    return;
  }

  createPeerConnection();

  peerConnection.createOffer()
    .then(offer => peerConnection.setLocalDescription(offer))
    .then(() => {
      if (socketReady) {
        signalingSocket.send(JSON.stringify({ offer: peerConnection.localDescription }));
      }
    })
    .catch(err => {
      console.error("Offer error:", err);
    });
}

// Create PeerConnection helper
function createPeerConnection() {
  peerConnection = new RTCPeerConnection(config);

  if (localStream) {
    localStream.getTracks().forEach(track => {
      peerConnection.addTrack(track, localStream);
    });
  }

  peerConnection.ontrack = (event) => {
    remoteStream = event.streams[0];
    document.getElementById("remoteVideo").srcObject = remoteStream;
  };

  peerConnection.onicecandidate = (event) => {
    if (event.candidate && socketReady) {
      signalingSocket.send(JSON.stringify({ candidate: event.candidate }));
    }
  };
}
*/

let localStream;
let remoteStream;
let peerConnection;

    const signalingSocket = new WebSocket("ws://" + location.host + "/call/signaling");//new WebSocket("ws://192.168.159.108:8080/call/signaling")
//
const config = {
  iceServers: [
    { urls: "stun:stun.l.google.com:19302" }
  ]
};

signalingSocket.onopen = () => {
  console.log("WebSocket connected.");
};

signalingSocket.onmessage = async (message) => {
  const data = JSON.parse(message.data);
    // Ensure peerConnection exists
  if (!peerConnection) {
    peerConnection = new RTCPeerConnection(config);

    // Add local stream tracks (if already started)
    if (localStream) {
      localStream.getTracks().forEach(track => {
        peerConnection.addTrack(track, localStream);
      });
    }

    // Setup remote stream
    peerConnection.ontrack = (event) => {
      remoteStream = event.streams[0];
      document.getElementById("remoteVideo").srcObject = remoteStream;
    };

    // ICE handling
    peerConnection.onicecandidate = (event) => {
      if (event.candidate) {
        signalingSocket.send(JSON.stringify({ candidate: event.candidate }));
      }
    };
  }

  if (data.offer) {
    await peerConnection.setRemoteDescription(new RTCSessionDescription(data.offer));
    const answer = await peerConnection.createAnswer();
    await peerConnection.setLocalDescription(answer);
    signalingSocket.send(JSON.stringify({ answer }));
  }

  if (data.answer) {
    await peerConnection.setRemoteDescription(new RTCSessionDescription(data.answer));
  }

  if (data.candidate) {
    await peerConnection.addIceCandidate(new RTCIceCandidate(data.candidate));
  }
};

function start() {
  navigator.mediaDevices.getUserMedia({ video: true, audio: true })
    .then(stream => {
      localStream = stream;
      document.getElementById("localVideo").srcObject = stream;
    });
}

function call() {
  peerConnection = new RTCPeerConnection(config);

  // Add local stream tracks
  localStream.getTracks().forEach(track => {
    peerConnection.addTrack(track, localStream);
  });

  // Display remote stream
  peerConnection.ontrack = (event) => {
    remoteStream = event.streams[0];
    document.getElementById("remoteVideo").srcObject = remoteStream;
  };

  // Handle ICE candidates
  peerConnection.onicecandidate = (event) => {
    if (event.candidate) {
      signalingSocket.send(JSON.stringify({ candidate: event.candidate }));
    }
  };

  // Create offer
  peerConnection.createOffer()
    .then(offer => peerConnection.setLocalDescription(offer))
    .then(() => {
      signalingSocket.send(JSON.stringify({ offer: peerConnection.localDescription }));
    });
}


