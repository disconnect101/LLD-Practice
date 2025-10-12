const { app, BrowserWindow } = require("electron");
const path = require("path");
const mongoose = require("mongoose")

const mainWindow = () => {
	const window = new BrowserWindow({
		width: 1280,
		height: 720,
		webPreferences: {
			enableRemoteModule: true,
			nodeIntegration: true,
			preload: path.join(__dirname, "./preload.js")
		}
	});

	window.on("ready-to-show", window.show);

	window.loadFile("index.html");

}

app.whenReady().then(() => {
	mainWindow();
})

app.on("window-all-closed", () => {
	if (process.platform !== "darwin") app.quit();
})



const { ipcMain } = require("electron");

const getMyFriends = () => {
	return [
		{id: 6030, name: "Twilley"},
		{id: 967, name: "Wickkiser"},
		{id: 5073, name: "Essick"},
		{id: 8886, name: "Marotta"},
		{id: 7416, name: "Banh"}
	];
}

const sendMessage = (message) => {
	console.log(message);
}

ipcMain
	.on("main-window-ready", (e) => {
		console.log("Main window is ready");
	})
	.on("send-message", (e, message) => {
		sendMessage(message);
	})

ipcMain.handle("get-friends", (e) => {
	return getMyFriends();
})

mongoose.Promise = global.Promise;
mongoose.connect("mongodb://127.0.0.1:27017/my-network")
.then(() => console.log("Connected!!"))
.catch((err) => console.log("failed connection", err));

var entitySchema = new mongoose.Schema({
	name: String,
	tags: String
  }, {collection: "Entity"});

var Entity = mongoose.model("Entity", entitySchema)

ipcMain.on("entity-creation", (e, entity) => {
	console.log(entity)
	// save to DB
	var newEntity = new Entity(entity);
	newEntity.save().then(item => {
		console.log("entity saved")
	})
	.catch(err => {
		console.log("Couldn't save entry", err)
	})
})