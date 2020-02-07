let myImage = document.getElementById("githubImgId");

myImage.onclick = function() {
    window.open("http://127.0.0.1:8443/oauth/render/github");
}

function loadWebAssembly(fileName) {
    return fetch(fileName)
        .then(response => response.arrayBuffer())
        .then(bits => WebAssembly.compile(bits))
        .then(module => { return new WebAssembly.Instance(module) });
};

loadWebAssembly('wasm/test.wasm').then(instance => {
    squarer = instance.exports._Z5helloid;
    console.log('Finished compiling! Ready when you are...');
});