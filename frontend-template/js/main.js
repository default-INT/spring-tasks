'use strict'


const lastCanvasCtx = {}
let lastCanvasValue;
let actualColor;
const activeImg = {

}

const drawImageToCanvas = (canvas, pic, canvasWidth, canvasHeight) => {
    canvas.width = canvasWidth
    canvas.height = canvasHeight

    const ctx = canvas.getContext('2d')

    pic.onload = function() {
        const [width, height] = resizeToBounds(pic.width, pic.height, canvas.width, canvas.height)
        ctx.drawImage(resize(pic, width, height), canvas.width / 2 - width / 2, 0)
        const imgData = ctx.getImageData(0, 0, canvas.width, canvas.height)
        lastCanvasValue = copyImageData(ctx, imgData)
    }
}

const modalAddImageShow = () => {
    modalContainer.innerHTML += ModalAddImageHTML()
}

const modalImageComponentShow = img => {
    modalContainer.innerHTML += ModalImageHTML(img);
    const canvas = document.getElementById('imgCanvas')
    const pic = new Image()

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
        lastCanvasValue = copyImageData(ctx, imgData)
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
    lastCanvasValue = copyImageData(ctx, effectImgData)

    e.classList.add('enable')
}


const setColor = () => {
    const redSliderDOM = document.getElementById('rs-red')
    const greenSliderDOM = document.getElementById('rs-green')
    const blueSliderDOM = document.getElementById('rs-blue')

    const canvas = document.getElementById('imgCanvas')
    const ctx = canvas.getContext('2d')
    const imgData = copyImageData(ctx, lastCanvasValue)
    actualColor = {red: +redSliderDOM.value, green: +greenSliderDOM.value, blue: +blueSliderDOM.value}
    const newColorData = colorChanger(imgData, actualColor)
    ctx.putImageData(newColorData, 0, 0)
}

const setWidth = width => activeImg.image.width = width;
const setHeight = height => activeImg.image.height = height;

const getActualImgCanvas = () => {
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
    if (actualColor) {
        const newColorData = colorChanger(imgData, actualColor)
        ctx.putImageData(newColorData, 0, 0)
    }
    return canvas
}


const applyChanges = async () => {
    const newImgName = prompt('Input new image name');
    const canvas = getActualImgCanvas()
    const imageBlob = await new Promise(resolve => canvas.toBlob(resolve, 'image/png'));
    const formData = new FormData()
    formData.append('file', imageBlob, newImgName + '.jpg')
    formData.append('name', newImgName)
    await fetch(url + '/uploads/load-img', {
        method: 'POST',
        body: formData
    })
    loadData()
    closeModal()
}
const downloadClick = () => {
    const canvas = getActualImgCanvas()
    const dataURL = canvas.toDataURL("image/jpeg");
    const link = document.createElement("a");
    link.href = dataURL;
    link.download = "my-image-name.jpg";
    link.click();
}

const labelValueState = (labelId, valueDomEl) => {
    document.getElementById(labelId).innerHTML = valueDomEl.value
}

const loadImage = async () => {
    const imgFormLoadDOM = document.getElementById('imgFormLoad')
    const response = await fetch(url + '/uploads/load-img', {
        method: 'POST',
        body: new FormData(imgFormLoadDOM)
    })
    loadData()
    closeModal()
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

const loadData = () => {
    imgListDOM.innerHTML = ''
    imgListDOM.appendChild(Loader)
    fetch(url + '/uploads')
        .then(response => {
            if (!response.ok) {
                throw new Error(response.statusText)
            }
            return  response.json()
        }).then(images => {
        imgListDOM.innerHTML = ''
        if (images.length === 0) {
            imgListDOM.innerHTML = 'No images'
            return;
        }
        images.forEach(img => {
            imgListDOM.appendChild(ItemImg(img))
        })
    }).catch(resolve => {
        imgListDOM.innerHTML = 'Something went wrong...'
    })
}


loadData()


function closeModal() {
    modalContainer.innerHTML = ''
    activeImg.image = undefined
}

function openModal(img) {
    modalImageComponentShow(img)
}
