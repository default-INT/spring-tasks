
const SliderHTML = (colorId) => `<div class="slider-container">
  <div class="range-slider">
    <span class="rs-label" id="rs-label-${colorId}">0</span>
    <input class="rs-range" id="rs-${colorId}" type="range" value="0" min="0" max="255" 
        oninput="labelValueState('rs-label-${colorId}', this)">
  </div>
</div>`

const ModalAddImageHTML = () => `<div id="openModal" class="modal">
  <div class="modal-dialog img-setting-block">
    <div class="modal-content">
      <div class="modal-header">
        <h3 class="modal-title">Add new image</h3>
        <a title="Close" class="close" onclick="closeModal()">×</a>
      </div>
      <div class="modal-body">    
        <form class="form-block" id="imgFormLoad">
            <input type="file" placeholder="Select image" name="file">
        </form>
        <div class="end-block">
            <a class="default-btn" onclick="loadImage()">Add</a>
        </div>
      </div>
    </div>
  </div>
</div>`

const ModalImageHTML = img => {
    const title = (img.name || 'add') + (img.user ? ' / ' + img.user.username  : ' new image')

    return `<div id="openModal" class="modal">
  <div class="modal-dialog img-setting-block">
    <div class="modal-content">
      <div class="modal-header">
        <h3 class="modal-title">${title}</h3>
        <a title="Close" class="close" onclick="closeModal()">×</a>
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
                    <input type="number" placeholder="width" id="imgModalWidth" onchange="setWidth(this.value)" value="${img.width}">
                    <input type="number" placeholder="height" id="imgModalHeight" onchange="setHeight(this.height)" value="${img.height}">
                </div>
            </div>
            <div class="util-item">
                <div class="util-title">Effects</div>
                <div class="util-body">
                    <a class="default-btn" onclick="setEffect(this, 'BLUR')">Blur 4x</a>
                    <a class="default-btn" onclick="setEffect(this, 'SEPIA')">Sepia</a>
                </div>
                <div class="util-colors">
                    <div class="prop">
                        <div class="prop-name">Red: </div>
                        <div class="prop-value">${SliderHTML('red')}</div>
                    </div>
                    <div class="prop">
                        <div class="prop-name">Blue: </div>
                        <div class="prop-value">${SliderHTML('blue')}</div>
                    </div>
                    <div class="prop">
                        <div class="prop-name">Green: </div>
                        <div class="prop-value">${SliderHTML('green')}</div>
                    </div>
                    <div class="prop">
                        <a class="default-btn" onclick="setColor()" >Apply color</a>
                        <a class="default-btn" onclick="cancelColor()" >Cancel changes</a>
                    </div>
                </div>
            </div>
        </div>
        <div class="end-block">
            <a class="default-btn" onclick="applyChanges()">Apply</a>
            <a class="default-btn downland-btn" onclick="downloadClick()">Downland</a>
        </div>
      </div>
    </div>
  </div>
</div>`
}