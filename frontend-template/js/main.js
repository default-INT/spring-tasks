'use strict'

const lastCanvasCtx = {}
const activeImg = {

}

const drawImageToCanvas = (canvas, pic, canvasWidth, canvasHeight) => {
    canvas.width = canvasWidth
    canvas.height = canvasHeight

    const ctx = canvas.getContext('2d')

    pic.onload = function() {
        const [width, height] = resizeToBounds(pic.width, pic.height, canvas.width, canvas.height)
        ctx.drawImage(resize(pic, width, height), canvas.width / 2 - width / 2, 0)
    }
}

const modalImageComponentShow = img => {
    modalContainer.innerHTML += ModalImageHTML(img);
    const canvas = document.getElementById('imgCanvas')
    const pic = new Image()

    // pic.setAttribute('crossOrigin', '');
    pic.crossOrigin = 'Anonymous'
    pic.src = url + '/uploads/' + img.filePath
    activeImg.image = pic
    drawImageToCanvas(canvas, pic, 800, 800)
}

const setEffect = (e, effect) => {
    const canvas = document.getElementById('imgCanvas')
    const ctx = canvas.getContext('2d')

    if (e.classList.contains('enable')) {
        const imgData = lastCanvasCtx[effect]
        lastCanvasCtx[effect] = undefined
        ctx.putImageData(imgData, 0, 0)
        e.classList.remove('enable')
        return;
    }

    const [width, height] = [canvas.width, canvas.height]
    const imgData = ctx.getImageData(0, 0, width, height)
    lastCanvasCtx[effect] = copyImageData(ctx, imgData)
    const effectImgData = effectDispatch(imgData)[effect]() || effectDispatch(imgData)['DEFAULT']()
    ctx.putImageData(effectImgData, 0, 0)

    e.classList.add('enable')
}

const applyChanges = (img) => {
    console.log(img)
}

const downloadClick = () => {
    const canvas = document.createElement('canvas')
    canvas.width = activeImg.image.width
    canvas.height = activeImg.image.height
    const ctx = canvas.getContext('2d')

    ctx.drawImage(activeImg.image, 0, 0, activeImg.image.width, activeImg.image.height)
    const [width, height] = [canvas.width, canvas.height]
    const imgData = ctx.getImageData(0, 0, width, height)

    for (let prop in lastCanvasCtx) {
        const effectImgData = effectDispatch(imgData)[prop]() || effectDispatch(imgData)['DEFAULT']()
        ctx.putImageData(effectImgData, 0, 0)
    }
    const dataURL = canvas.toDataURL("image/jpeg");
    const link = document.createElement("a");
    link.href = dataURL;
    link.download = "my-image-name.jpg";
    link.click();
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
                    <a class="default-btn" onclick="setEffect(this, 'BLUR')">Blur 4x</a>
                    <a class="default-btn" onclick="setEffect(this, 'SEPIA')">Sepia</a>
                    <a class="default-btn">Negative</a>
                </div>
            </div>
        </div>
        <div class="end-block">
            <a class="default-btn" onclick="${applyChanges(img)}">Apply</a>
            <a class="default-btn downland-btn" onclick="downloadClick()">Downland</a>
        </div>
      </div>
    </div>
  </div>
</div>`
}


const ItemImg = image => {
    const canvas = node({type: 'canvas'})

    const pic = new Image()
    pic.crossOrigin = 'Anonymous'
    pic.src = url + '/uploads/' + image.filePath

    drawImageToCanvas(canvas, pic, 400, 400)

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
})

imgListDOM.appendChild(Loader)

function closeModal() {
    modalContainer.innerHTML = ''
    activeImg.image = undefined
}

function openModal(img) {
    modalImageComponentShow(img)
}
