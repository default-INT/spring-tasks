const url = 'http://127.0.0.1:8080'

const elementSelector = {
    'string': child => document.createTextNode(child),
    'function': child => document.createElement(child),
    'object': child => child
}

function clearForms() {
    const forms = Array.from(document.querySelectorAll('input'));
    const msgs = Array.from(document.querySelectorAll('.msg'));

    forms.forEach((form, index, forms) => form.value = "");

    msgs.forEach((msg, index, msgs) => msg.innerHTML = "Input data to input forms");
}

const addZeroDate = num => num < 10 ? '0' + num : num.toString();

/**
 *
 * @param date {Date}
 * @returns {string}
 */
const dateFormatter = date => addZeroDate(date.getMonth() + 1) + "." + addZeroDate(date.getDate());

/**
 *
 * @param option {object}
 * @returns {HTMLDivElement}
 */
const node = function (option) {
    const type = option.type ? option.type : 'div';
    const element = document.createElement(type);

    if (option.children) {
        if (option.children instanceof Array) {
            option.children.forEach(child => element.appendChild(elementSelector[typeof child](child)));
        } else {
            element.appendChild(elementSelector[typeof option.children](option.children));
        }
    }

    if (option.href) {
        element.href = option.href;
    }

    if (option.value) {
        element.value = option.value;
    }

    if (option.selected) {
        element.selected = option.selected;
    }

    if (option.inputType) {
        element.type = option.inputType;
    }

    element.onclick = option.onclick ? (e) => option.onclick(e) : element.onclick;
    element.onload = option.onload ? option.onload : element.onload;

    if (typeof element.onload === "function") {
        element.onload();
    }

    if (option.classList) {
        option.classList.forEach(c => element.classList.add(c));
    }

    if (option.id) {
        element.id = option.id;
    }

    if (option.name) {
        element.name = option.name;
    }

    if (option.for) {
        element.htmlFor = option.for;
    }

    if (option.placeholder) {
        element.placeholder = option.placeholder;
    }

    if (option.disabled) {
        element.disabled = option.disabled;
    }

    if (option.styles) {
        const optionStyles = option.styles;
        const styles = element.style;
        if (optionStyles.background) {
            styles.background = optionStyles.background;
        }
        if (optionStyles.color) {
            styles.color = optionStyles.color;
        }
    }

    return element;
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

const colorDispatch =(imageData, colorValue) => ({
    ['RED'] : () => colorChanger(imageData, {red: colorValue}),
    ['BLUE'] : () => colorChanger(imageData, {blue: colorValue}),
    ['GREEN'] : () => colorChanger(imageData, {green: colorValue}),
})

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