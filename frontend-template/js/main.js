const img = document.getElementById('image1');
const ModalImage = img => {
    return `<div id="openModal" class="modal">
  <div class="modal-dialog img-setting-block">
    <div class="modal-content">
      <div class="modal-header">
        <h3 class="modal-title">${img.name}</h3>
        <a href="#close" title="Close" class="close">×</a>
      </div>
      <div class="modal-body">    
        <div class="img-block">
            <canvas></canvas>
        </div>
        <div class="utils-block">
            <div class="title">Settings</div>
            <div class="util-item">
                <div class="util-title">Size change</div>
                <div class="util-body">
                    <input type="number" placeholder="width" value="200">
                    <input type="number" placeholder="height">
                </div>
            </div>
            <div class="util-item">
                <div class="util-title">Effects</div>
                <div class="util-body">
                    <a class="default-btn enable">Blur</a>
                    <a class="default-btn">Negative</a>
                </div>
            </div>
        </div>
        <div class="end-block">
            <a href="" class="default-btn">Apply</a>
        </div>
      </div>
    </div>
  </div>
</div>`
}

const Loader = `<div class="lds-roller">
        <div/>
        <div/>
        <div/>
        <div/>
        <div/>
        <div/>
        <div/>
        <div/>
    </div>`

fetch('/images')
    .then(response => {

    })

const imgListDOM = document.querySelector('.image-list');
const modalContainer = document.getElementById("modalContainer");

//imgListDOM.innerHTML = Loader;

function closeModal() {
    modalContainer.innerHTML = ''
}

function openModal(img) {
    modalContainer.innerHTML += ModalImage({
        name: "image1"
    });
}
