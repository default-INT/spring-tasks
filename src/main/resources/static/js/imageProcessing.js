"use strict"

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
        return [imgW, imgH]
    }
    const rw = boundsW / imgW;
    const rh = boundsH / imgH;
    const r = Math.min(rw, rh);
    return [Math.round(imgW * r), Math.round(imgH * r)]
}

const sepia = function (imageData) {
    // получаем одномерный массив, описывающий все пиксели изображения
    const pixels = imageData.data;
    // циклически преобразуем массив, изменяя значения красного, зеленого и синего каналов
    for (let i = 0; i < pixels.length; i += 4) {
        const r = pixels[i];
        const g = pixels[i + 1];
        const b = pixels[i + 2];
        pixels[i]     = (r * 0.393)+(g * 0.769)+(b * 0.189); // red
        pixels[i + 1] = (r * 0.349)+(g * 0.686)+(b * 0.168); // green
        pixels[i + 2] = (r * 0.272)+(g * 0.534)+(b * 0.131); // blue
    }
    return imageData;
};

const colorChanger = function (imageData, color) {
    // получаем одномерный массив, описывающий все пиксели изображения
    const pixels = imageData.data;
    // циклически преобразуем массив, изменяя значения красного, зеленого и синего каналов
    for (let i = 0; i < pixels.length; i += 4) {
        const r = pixels[i];
        const g = pixels[i + 1];
        const b = pixels[i + 2];
        pixels[i]     = color.red ? r <= color.red ? color.red : 0 : r; // red
        pixels[i + 1] = color.green ? g <= color.green ? color.green : 0 : pixels[i]; // green
        pixels[i + 2] = color.blue ? b <= color.blue ? color.blue : 0 : pixels[i]; // blue
    }
    return imageData;
};


const effectDispatch = (imageData) => ({
    ['SEPIA'] : () => sepia(imageData),
    ['BLUR'] : () => stackBlurImage(imageData, 4),
    ['DEFAULT'] : () => imageData
})

function copyImageData(ctx, src) {
    const dst = ctx.createImageData(src.width, src.height);
    dst.data.set(src.data);
    return dst;
}