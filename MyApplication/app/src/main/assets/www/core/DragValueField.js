

class DragValueField extends Widget
{
    constructor(label = "Value", value = 0.0, min = -Infinity, max = Infinity, step = 0.01)
    {
        super();
        this.label = label;
        this.value = value;
        this.min = min;
        this.max = max;
        this.step = step;
        this.hovered = false;
        this.dragging = false;
        this.startX = 0;
        this.startValue = value;
        this.onChange = null;
    }

    setValue(v)
    {
        this.value = Math.min(this.max, Math.max(this.min, v));
        if (this.onChange) this.onChange(this.value);
    }

 

    handleMouse(type, x, y, button)
    {
        if (!this.visible) return false;
        //if (!this.contains(x, y))  return false;
      

        if (type === 0 && this.contains(x, y))
        {
            this.dragging = true;
            this.startX =  x;
            this.startValue = this.value;
            return true;
        }

        if (type === 1)
        {
            this.dragging = false;
            this.hovered = false;
           // return true;
        }
 
        if (type === 2 && this.dragging)
        {

            const delta = x - this.startX;
            const newVal = this.startValue + delta * this.step;
            this.setValue(newVal);
            this.hovered = true;
            return true;
        }

        return false;
    }

    render(g)
    {
        if (!this.visible) return;
    
        // Fundo
        g.setColor(Theme.dragFieldBackground);
        g.fillRect(this.x, this.y, this.width, this.height);
    
        // Borda
        g.setColor(Theme.dragFieldBorder);
        g.drawRect(this.x, this.y, this.width, this.height);
    
        // Label
        const labelW = 20;
        g.setColor(Theme.dragFieldLabel);
        g.drawText(this.label, this.x + 4, this.y + this.height / 2 - 6);
    
        // Valor
        g.setColor(Theme.dragFieldText);
        const valStr = this.value.toFixed(3);
        g.drawText(valStr, this.x + labelW + 4, this.y + this.height / 2 - 6);
    
        // Hover
        if (this.hovered) {
            g.setColor(Theme.dragFieldHoverOutline);
            g.drawRect(this.x, this.y, this.width, this.height);
        }
    }
    
}