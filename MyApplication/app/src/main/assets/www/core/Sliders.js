

export class Slider extends Widget {
    constructor(min = 0, max = 1, step = 0.01, value = 0, orientation = "horizontal")
    {
        super();
        this.min = min;
        this.max = max;
        this.step = step;
        this.value = value;
        this.orientation = orientation;
        this.dragging = false;
        this.onChange = null;
    }

    setValue(val) {
        this.value = Math.min(this.max, Math.max(this.min, val));
        if (this.onChange) this.onChange(this.value);
    }

    getPercent() {
        return (this.value - this.min) / (this.max - this.min);
    }

    handleMouse(type, x, y, button) {
        if (!this.enabled) return false;
       

        const localX = x - this.x;
        const localY = y - this.y;

        if (type === 0 && this.contains(x, y))
        { // Mouse down
            this.dragging = true;
            return true;
        }

        if (type === 1) { // Mouse up
            this.dragging = false;
            
        }

        if (type === 2 && this.dragging) { // Mouse move
            const percent = this.orientation === "horizontal"
                ? localX / this.width
                : 1 - (localY / this.height);
            const rawValue = this.min + percent * (this.max - this.min);
            const stepped = Math.round(rawValue / this.step) * this.step;
            this.setValue(stepped);
            return true;
        }

        return false;
    }

   

    render(g) {
        if (!this.visible) return;


        const txt = this.value.toFixed(2);
        const size = g.measureText(txt);
        const w = size.width*2;
        const h = size.height;
        const offX = w +5
    
        // Track externo
        g.setColor(Theme.sliderBorder);
        if (this.orientation === "horizontal")
            g.drawRoundedRect(this.x - 3, this.y, (this.width + 8)-offX, this.height, 4);
        else
            g.drawRoundedRect(this.x, this.y - 4, this.width-offX, this.height + 8, 4);
    
        // Track interior
        g.setColor(Theme.sliderTrack);
        if (this.orientation === "horizontal")
            g.fillRect(this.x, this.y + this.height / 2 - 3, this.width-offX, 6);
        else
            g.fillRect(this.x + (this.width-offX) / 2 - 3, this.y, 6, this.height);
    
        // Fill
        const percent = this.getPercent();
        g.setColor(Theme.sliderFill);
        if (this.orientation === "horizontal")
            g.fillRect(this.x, this.y + this.height / 2 - 3, (this.width-offX) * percent, 6);
        else
            g.fillRect(this.x + (this.widthoffX) / 2 - 3, this.y + this.height * (1 - percent), 6, this.height * percent);
    
        // Thumb
        if (this.orientation === "horizontal")
        {
            const tx = this.x + (this.width-offX) * percent - 5;
            g.setColor(Theme.sliderThumb);
            g.fillCircle(tx + 5, this.y + this.height / 2, 8);
        } else
        {
            const ty = this.y + this.height * (1 - percent) - 5;
            g.setColor(Theme.sliderThumb);
            g.fillCircle(this.x + (this.width-offX) / 2, ty + 5, 8);
        }
    
        // Valor
        g.setColor(Theme.sliderText);
    
        g.drawText(txt, this.x + (this.width-offX)+(offX*0.5)-4.0, this.y + this.height *0.5 - (h*0.5));
    }
    
}


export class ProgressBar extends Widget {
    constructor(orientation = "horizontal") {
        super();
        this.value = 0;
        this.max = 1;
        this.orientation = orientation;
    }

    setValue(val) {
        this.value = Math.max(0, Math.min(this.max, val));
    }
    render(g) {
        if (!this.visible) return;
    
        const percent = this.value / this.max;
    
        // Fundo
        g.setColor(Theme.progressBackground);
        g.fillRect(this.x, this.y, this.width, this.height);
    
        // Barra de progresso
        if (this.orientation === "horizontal")
            {
            const pos = this.width * percent;
            
            const txt = this.value.toFixed(2);
            const size = g.measureText(txt);
            const w = size.width;
            const h = size.height;
            g.setColor(Theme.progressFill);
            g.fillRect(this.x, this.y, pos, this.height);
            g.setColor(Theme.progressBorder);
            
            g.drawLine(this.x + pos, this.y , this.x +pos, this.y + this.height);
 
            g.setColor(Theme.progressText);
            g.drawText(txt, this.x + (this.width*0.5) - (w * 0.5) , this.y + (this.height *0.5) - (h * 0.5));
        } else
        {
            const pos = this.y + this.height * (1 - percent);
            g.setColor(Theme.progressFill);
            g.fillRect(this.x, pos, this.width, this.height * percent);
            g.setColor(Theme.progressBorder);
            g.drawLine(this.x, pos, this.x + this.width, pos);
        }
    }
    
}




export class Stepper extends Widget
{
    constructor(min = 0, max = 10, step = 1, value = 0, onChange = null) {
        super();
        this.min = min;
        this.max = max;
        this.step = step;
        this.value = value;
        this.onChange = onChange;
        this.focus = false;
        this.btnSize = 20;
        this.hoverMinus = false;
        this.hoverPlus = false;
        this.state = 0;
        this.progressive = false;
    }

    handleMouse(type, x, y, button) {
        if (!this.visible || !this.enabled) return false;

        const localX = x - this.x;
        const localY = y - this.y;
        
        
            if (type == 2)
            {
                this.focus = this.contains(x, y);
            }

    
            // Botão menos
            if (type === 0 && localX >= 0 && localX <= this.btnSize)
            {
                this.value = Math.max(this.min, this.value - this.step);
                if (this.onChange) this.onChange(this.value);
                this.focus = true;
                this.state = 1;
                return true;
            }

            // Botão mais
            if (type === 0 && localX >= this.width - this.btnSize && localX <= this.width) {
                this.value = Math.min(this.max, this.value + this.step);
                if (this.onChange) this.onChange(this.value);
                this.focus = true;
                this.state = 2;
                return true;
            }
        
        this.state = 0;
            

        return false;
    }

    render(g) {
        if (!this.visible) return;


        if (this.progressive)
        {
            if (this.focus && Input.isMouseDown(0))
            {
                if (this.state === 2) {
                    this.value = Math.min(this.max, this.value + this.step);
                    if (this.onChange) this.onChange(this.value);
                    this.focus = true;
                }
                if (this.state === 1) {
                    this.value = Math.max(this.min, this.value - this.step);
                    if (this.onChange) this.onChange(this.value);
                }
            }
        }

        // Caixa principal
         g.setColor(Theme.stepperBackground);
         g.fillRect(this.x, this.y, this.width, this.height);
         g.setColor(Theme.stepperBorder);
         g.drawRect(this.x, this.y, this.width, this.height);

        // Botão menos
        g.setColor(Theme.stepperButton);
        g.fillRect(this.x, this.y, this.btnSize, this.height);
        g.setColor(Theme.stepperButtonText);
        g.drawText("−", this.x + 6, this.y + 4);

        // Botão mais
        g.setColor(Theme.stepperButton);
        g.fillRect(this.x + this.width - this.btnSize, this.y, this.btnSize, this.height);
        g.setColor(Theme.stepperButtonText);
        g.drawText("+", this.x + this.width - this.btnSize + 4, this.y + 4);


        if (this.focus)
        {
            g.setColor("red");
            g.drawRect(this.x, this.y, this.width, this.height);
        }

        // Valor
        const valText = this.value.toString();
        const text = g.measureText(valText);
        g.setColor(Theme.stepperText);
        g.drawText(valText, this.x + this.width / 2 - text.width / 2, this.y + (this.height *0.5) - (text.height*0.5));
    }
}



class StringStepper extends Widget {
    constructor(items = [], index = 0, onChange = null) {
        super();
        this.items = items;
        this.index = index;
        this.onChange = onChange;

        this.btnSize = 20;
    }

    handleMouse(type, x, y, button) {
        if (!this.visible || !this.enabled) return false;

        const localX = x - this.x;

        if (type === 0) {
            // Botão esquerdo ‹
            if (localX >= 0 && localX <= this.btnSize) {
                if (this.index > 0) {
                    this.index--;
                    if (this.onChange) this.onChange(this.index, this.items[this.index]);
                }
                return true;
            }

            // Botão direito ›
            if (localX >= this.width - this.btnSize && localX <= this.width) {
                if (this.index < this.items.length - 1) {
                    this.index++;
                    if (this.onChange) this.onChange(this.index, this.items[this.index]);
                }
                return true;
            }
        }

        return false;
    }

    render(g) {
        if (!this.visible) return;

        g.setColor(Theme.stepperBackground);
        g.fillRect(this.x, this.y, this.width, this.height);
        g.setColor(Theme.stepperBorder);
        g.drawRect(this.x, this.y, this.width, this.height);

        // Botões ‹ e ›
        g.setColor(Theme.stepperButton);
        g.fillRect(this.x, this.y, this.btnSize, this.height);
        g.fillRect(this.x + this.width - this.btnSize, this.y, this.btnSize, this.height);

        g.setColor(Theme.stepperButtonText);
        g.drawText("‹", this.x + 6, this.y + 4);
        g.drawText("›", this.x + this.width - this.btnSize + 6, this.y + 4);

        // Texto atual
        const text = this.items[this.index] || "";
        g.setColor(Theme.stepperText);
        const m = g.measureText(text);
        g.drawText(text, this.x + this.width / 2 - m.width / 2, this.y + this.height / 2 + m.height / 2 - 6);
    }

    getValue() {
        return this.items[this.index];
    }

    setValue(str) {
        const i = this.items.indexOf(str);
        if (i !== -1) this.index = i;
    }
}

export class Spinner extends Widget {
    constructor(size = 24) {
        super();
        this.size = size;
        this.angle = 0;
    }

    update(dt) {
        this.angle += dt * 6; // 6 rad/s
    }

    render(g) {
        if (!this.visible) return;

        const cx = this.x + this.size / 2;
        const cy = this.y + this.size / 2;
        const r = this.size / 2 - 2;

        g.setLineWidth(3);
        g.setColor(Theme.spinnerTrack);
        g.drawCircle(cx, cy, r);

        g.setColor(Theme.spinnerHead);
        const x = cx + Math.cos(this.angle) * r;
        const y = cy + Math.sin(this.angle) * r;
        g.drawLine(cx, cy, x, y);

        g.setLineWidth(1);
    }
}


export class NotificationBox extends Widget {
    constructor(text = "", duration = 2.5) {
        super();
        this.text = text;
        this.duration = duration; // segundos visível
        this.timer = 0;
        this.active = false;
    }

    show(text, duration = null) {
        this.text = text;
        this.duration = duration !== null ? duration : this.duration;
        this.timer = this.duration;
        this.active = true;
        this.visible = true;
    }

    update(dt) {
        if (this.active) {
            this.timer -= dt;
            if (this.timer <= 0) {
                this.active = false;
                this.visible = false;
            }
        }
    }

    render(g) {
        if (!this.visible) return;

        const padding = 10;
      //  g.setFont("bold 14px Arial");
        const textMetrics = g.measureText(this.text);
        const boxWidth = textMetrics.width + padding * 2;
        const boxHeight = 30;

        const x = (640 - boxWidth) / 2;
        const y = 200; // Topo da tela

        g.setColor(Theme.toastBackground);
        g.fillRect(x, y, boxWidth, boxHeight);

        g.setColor(Theme.toastBorder);
        g.drawRect(x, y, boxWidth, boxHeight);

        g.setColor(Theme.toastText);
        g.drawText(this.text, x + padding, y + (boxHeight - 14) / 2 + 7);
    }
}

