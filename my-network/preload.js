const { contextBridge, ipcRenderer } = require("electron");

contextBridge.exposeInMainWorld("api", {
	sendMessage: () => {
		const chatBox = document.getElementById("chat-box");
		ipcRenderer.send("send-message", chatBox.value);
		chatBox.value = "";
	},
	getFriends: () => {
		return ipcRenderer.invoke("get-friends");
	},
	createEntry: () => {
		const name = document.getElementById("name-network-entity");
		const tags = document.getElementById("tags-network-eintity");
		ipcRenderer.send("entity-creation", { name: name.value, tags: tags.value })
		name.value = ""
		tags.value = ""
	}
})

window.addEventListener("DOMContentLoaded", () => {
	ipcRenderer.send("main-window-ready");
})