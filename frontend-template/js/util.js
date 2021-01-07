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