

export class ToggleButton extends Widget
{
    constructor(checked = false, onChange = null) {
        super();
        this.checked = checked;
        this.onChange = onChange;
        this.width = 50;
        this.height = 25;
        this.knobRadius = 10;
        this.padding = 3;
        this.hovered = false;

        this.labelOn = "ON";
        this.labelOff = "OFF";
        this.textColor = "#fff";
        this.fontSize = 12;
    }

   

    handleMouse(type, x, y, button) {
        if (!this.enabled || !this.visible) return false;
        this.hovered = false;
        
        if (type === 0 && this.contains(x, y))
        { // Mouse Down
            this.checked = !this.checked;
            if (this.onChange) this.onChange(this.checked);
            return true;
        }

            
        
       
        if (type === 2 && this.contains(x, y))
        { // Mouse Move
            this.hovered = true;
            return true;
        }


        return false;
    }

    render(g) {
        if (!this.visible) return;
    
        const knobX = this.x + (this.checked ? this.width - this.height : 0) + this.padding;
        const knobY = this.y + this.padding;
        let knobSize = this.height - 2 * this.padding;
        if (knobSize < 1.0) knobSize = 1.0;
      
    
        // Fundo (base do botão)
        g.setColor(this.checked ? Theme.toggleBackgroundChecked : Theme.toggleBackground);
        g.drawRoundedRect(this.x, this.y, this.width, this.height, this.height / 2);
    
        // Texto (ON/OFF)
        const label = this.checked ? this.labelOn : this.labelOff;
        g.setColor(Theme.toggleText);
        const textSize = g.measureText(label);
        g.drawText(label, this.x + (this.width - textSize.width) / 2, this.y + (this.height - textSize.height) / 2);
    
        // Knob
        g.setColor(Theme.toggleKnob);
        g.fillCircle(knobX + knobSize / 2, knobY + knobSize / 2, knobSize / 2);
    
        // Hover outline
        if (this.hovered) {
            g.setColor(Theme.toggleHoverOutline);
            g.drawRect(this.x, this.y, this.width, this.height);
        }
    }
    
}
