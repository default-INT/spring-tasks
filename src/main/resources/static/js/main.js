
const drawImageToCanvas = (canvas, imgPath, canvasWidth, canvasHeight) => {
    canvas.width = canvasWidth
    canvas.height = canvasHeight

    const ctx = canvas.getContext('2d')

    const pic = new Image()
    pic.src = url + '/uploads/' + imgPath

    pic.onload = function() {
        const [width, height] = resizeToBounds(pic.width, pic.height, canvas.width, canvas.height)
        ctx.drawImage(resize(pic, width, height), canvas.width / 2 - width / 2, 0)

    }
}

const modalImageComponentShow = img => {
    const state = {
        width: img.width,
        height: img.height
    }
    modalContainer.innerHTML += ModalImageHTML(img);
    const canvas = document.getElementById('imgCanvas')
    drawImageToCanvas(canvas, img.filePath, 800, 800)
}

const applyChanges = (img) => {
    console.log(img)
}

const ModalImageHTML = img => {
    return `<div id="openModal" class="modal">
  <div class="modal-dialog img-setting-block">
    <div class="modal-content">
      <div class="modal-header">
        <h3 class="modal-title">${img.name}</h3>
        <a href="#close" title="Close" class="close" onclick="closeModal()">×</a>
      </div>
      <div class="modal-body">    
        <div class="img-block">
            <canvas id="imgCanvas"></canvas>
        </div>
        <div class="utils-block">
            <div class="title">Settings</div>
            <div class="util-item">
                <div class="util-title">Size change</div>
                <div class="util-body">
                    <input type="number" placeholder="width" value="${img.width}">
                    <input type="number" placeholder="height" value="${img.height}">
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
            <a class="default-btn" onclick="${applyChanges(img)}">Apply</a>
            <a class="default-btn downland-btn">Downland</a>
        </div>
      </div>
    </div>
  </div>
</div>`
}

function resize(img, w, h) {
    let sW = img.width;
    let sH = img.height;
    let x = 2;

    while (sW > w) {
        sW = Math.round(sW / x);
        sH = Math.round(sH / x);
        if (sW < w) {
            sW = w;
            sH = h;
        }
        let canvas = document.createElement('canvas');
        canvas.width = sW;
        canvas.height = sH;
        canvas.getContext('2d').drawImage(img, 0, 0, sW, sH);
        img = canvas;
    }
    return img;
}

function resizeToBounds(imgW, imgH, boundsW, boundsH) {
    if (imgW <= boundsW && imgH <= boundsH) {
        return null;
    }
    const rw = boundsW / imgW;
    const rh = boundsH / imgH;
    const r = Math.min(rw, rh);
    return [Math.round(imgW * r), Math.round(imgH * r)]
}




const ItemImg = image => {
    const canvas = node({type: 'canvas'})
    drawImageToCanvas(canvas, image.filePath, 400, 400)

    return  node({
        id: 'image' + image.id,
        classList: ['item', 'image', 'light-shadow'],
        children: [
            node({
                type: 'a',
                classList: ['title-image', 'default-link'],
                href: "#openModal",
                onclick: () => openModal(image),
                children: `${image.name} / (${image.contentType.split('/')[1]})`
            }),
            node({
                id: 'canvas' + image.id,
                children: canvas
            })
        ]
    })
}

const Loader = node({
    classList: ['lds-ring'],
    children: [ node({}),  node({}),  node({}),  node({}) ]
})

const imgListDOM = document.querySelector('.image-list');

const modalContainer = document.getElementById("modalContainer");


fetch(url + '/uploads')
    .then(response => {
        if (!response.ok) {
            throw new Error(response.statusText)
        }
        return  response.json()
    }).then(images => {
        imgListDOM.innerHTML = ''
        images.forEach(img => {
            imgListDOM.appendChild(ItemImg(img))
        });
        if (images.length === 0) {
            imgListDOM.innerHTML = 'No images'
        }
})

imgListDOM.appendChild(Loader)

function closeModal() {
    modalContainer.innerHTML = ''
}

function openModal(img) {
    modalImageComponentShow(img)
}
