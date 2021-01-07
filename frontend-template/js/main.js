// import { node } from './util'

const url = 'http://127.0.0.1:8080'

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
    let canvas = node({type: 'canvas'})
    canvas.width = 400
    canvas.height = 400

    const ctx = canvas.getContext('2d')

    let pic = new Image()
    pic.src = url + '/uploads/' + image.filePath

    pic.onload = function() {
        // // set size proportional to image
        // canvas.height = canvas.width * (pic.height / pic.width);
        // // canvas.height = canvas.width * (pic.height / pic.width);
        //
        // // step 1 - resize to 50%
        // const oc = document.createElement('canvas')
        // const octx = oc.getContext('2d');
        //
        // oc.width = pic.width * 0.5;
        // oc.height = pic.height * 0.5;
        // octx.drawImage(pic, 0, 0, oc.width, oc.height);
        //
        // // step 2
        // octx.drawImage(oc, 0, 0, oc.width * 0.5, oc.height * 0.5);
        //
        // // step 3, resize to final size
        // ctx.drawImage(oc, 0, 0, oc.width * 0.5, oc.height * 0.5,
        //     0, 0, canvas.width, canvas.height);

        // canvas.style.width = '20rem'
        // canvas.style.height = 'auto'

        // pic.height = canvas.height
        // const width = pic.width < pic.height ? canvas.height * pic.height / pic.width : canvas.width;
        // const height = pic.width > pic.width ? canvas.width * pic.width / pic.height : canvas.height;
        //
        // console.log(`canvas size (${canvas.width}x${canvas.height})`)
        // console.log(`pic size (${pic.width}x${pic.height})`)
        // // const dx = pic.width < pic.height ? width / 2 : 0
        // // const dy = pic.width > pic.height ? height / 2 : 0

        const [width, height] = resizeToBounds(pic.width, pic.height, canvas.width, canvas.height)
        // console.log(width)
        // console.log(height)

        // const width = pic.width > pic.height ? canvas.width : (canvas.height * pic.width) / pic.height
        // const height = pic.width < pic.height ? canvas.height : (canvas.width * pic.height) / pic.width
        console.log(width)
        console.log(height)
        const canvasContainerDOM = document.getElementById('canvas' + image.id)
        // canvasContainerDOM.innerHTML = ''
        // canvasContainerDOM.appendChild(resize(pic, width, height))
        ctx.drawImage(resize(pic, width, height), canvas.width / 2 - width / 2, 0)

    }

    return  node({
        id: 'image' + image.id,
        classList: ['item', 'image', 'light-shadow'],
        children: [
            node({
                type: 'a',
                classList: ['title-image', 'default-link'],
                href: "#openModal",
                onclick: () => openModal(image),
                children: image.filePath
            }),
            node({
                id: 'canvas' + image.id,
                children: canvas
            })
        ]
    })
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

imgListDOM.innerHTML = Loader;

function closeModal() {
    modalContainer.innerHTML = ''
}

function openModal(img) {
    modalContainer.innerHTML += ModalImage({
        name: "image1"
    });
}
