let myImage = document.getElementById("githubImgId");

myImage.onclick = function() {
    window.open("http://127.0.0.1:8443/oauth/render/github");
}