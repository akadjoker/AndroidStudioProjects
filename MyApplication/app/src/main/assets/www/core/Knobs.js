

export class Knob extends Widget {
    constructor(min = 0, max = 1, step = 0.01, value = 0.5, onChange = null) {
        super();
        this.min = min;
        this.max = max;
        this.step = step;
        this.value = value;
        this.onChange = onChange;

        this.dragging = false;
        this.radius = 30;
        this.sensitivity = 0.005;
        this.hovered = false;
    }

 

    isPointInside(x, y)
    {
        const dx = x - (this.x + this.width / 2);
        const dy = y - (this.y + this.height / 2);
        const dist = Math.sqrt(dx * dx + dy * dy);
        return dist <= this.radius;
    }

    _clamp(val) {
        val = Math.max(this.min, Math.min(this.max, val));
        return Math.round(val / this.step) * this.step;
    }

  

    getValueRatio() {
        return (this.value - this.min) / (this.max - this.min);
    }

    setValueFromRatio(ratio) {
        const v = this.min + (this.max - this.min) * ratio;
        this.value = Math.max(this.min, Math.min(this.max, v));
        if (this.onChange) this.onChange(this.value);
    }
    handleMouse(type, x, y, button) {
        if (!this.enabled || !this.visible) return false;
        
        if (type === 0 && this.isPointInside(x, y))
        { // DOWN
            const dx = x - (this.x + this.width / 2);
            const dy = y - (this.y + this.height / 2);
            const dist = Math.sqrt(dx * dx + dy * dy);
            if (dist <= this.radius) {
                this.dragging = true;
                return true;
            }
        }

        if (type === 1) { // UP
            this.dragging = false;
        }

       

        if (type === 2 && this.dragging)
        { // MOVE
            const delta = Input.mouseDY + -Input.mouseDX; // Usa movimento vertical
            this.value += delta * this.sensitivity * (this.max - this.min);
            this.value = this._clamp(this.value);
            if (this.onChange) this.onChange(this.value);
            return true;
        }

    

        return false;
    }

    render(g) {
        if (!this.visible) return;
    
        const cx = this.x + this.width / 2;
        const cy = this.y + this.height / 2;
        let radius = Math.min(this.width, this.height) / 2 - 10;
        if (radius < 8.0)
            radius = 8;
        this.radius = radius;
    
        const valueRatio = (this.value - this.min) / (this.max - this.min);
        const angle = Math.PI - valueRatio * Math.PI * 2;
    
        const pointerX = cx + radius * Math.cos(angle);
        const pointerY = cy + radius * Math.sin(angle);

        this.hovered = this.isPointInside(Input.mouseX, Input.mouseY);
    
        // Base
        g.setColor(Theme.knobBackground);
        g.fillCircle(cx, cy, this.radius);
    
        // Ponteiro
        g.setColor(Theme.knobPointer);
        g.setLineWidth(3);
        if (this.dragging)
            g.setLineWidth(5);
        g.drawLine(cx, cy, pointerX, pointerY);
        // Texto
        g.setColor(Theme.knobText);
        const valText = this.value.toFixed(2);
        const text = g.measureText(valText);
        g.drawText(valText, cx - text.width / 2, cy + text.height / 2);
        g.setLineWidth(1);
        
        if (this.hovered || this.dragging)
        {
            g.setColor(Theme.knobHoverOutline);
          //  g.setLineWidth(4);
            g.drawCircle(cx, cy, this.radius);
        }
        
        // Marcadores (0°, 90°, 180°, 270°)
        g.setColor(Theme.knobMarkers);
        for (let a = 0; a < 360; a += 90) {
            const rad = Math.PI - (a / 360) * Math.PI * 2;
            const tx = cx + Math.cos(rad) * (radius + 10);
            const ty = cy + Math.sin(rad) * (radius + 10);
            const lbl = a.toString();
            const l = g.measureText(lbl);
            g.drawText(lbl, tx - l.width / 2, ty - l.height / 2);
        }
    }
    
}


export class SliderCircular extends Widget
{
    constructor( onChange)
    {
        
        super();


      this.radius = 0;
      this.value = 0.5; // 0 to 1
      this.isDragging = false;
      this.startAngle = -0.75 * Math.PI;
      this.endAngle = 0.75 * Math.PI;
      this.onChange = onChange || function() {};
    }

 

    handleMouse(type, x, y, button)
    {
        if (!this.enabled || !this.visible) return false;
        
        if (type === 0 && this.isPointInside(x, y))
        { // DOWN
            this.isDragging = true;
            return true;
        }
  
        
        if (type === 1)
        { // UP
 
            this.isDragging = false;
        }

       

        if (type === 2 )
        { // MOVE

                if (this.isDragging)
                {
                    const oldValue = this.value;
                    this.value = this._calculateValue(x, y);
                
                    if (oldValue !== this.value)
                    {
                      this.onChange(this.value);
                    }
                return true;
              }
            
        }

        
        return false;
    }
    
    render(g) {
        const ctx = g.ctx;
        g.save();
    
        const cx = this.x + this.width / 2;
        const cy = this.y + this.height / 2;
        const radius = Math.max(0, Math.min(this.width, this.height) / 2);
        const innerRadius = Math.max(0, radius - 15);
        this.radius = radius;
    
        // Fundo
        ctx.fillStyle = '#ecf0f1';
        ctx.beginPath();
        ctx.arc(cx, cy, radius, 0, Math.PI * 2);
        ctx.fill();
    
        // Borda
        ctx.strokeStyle = '#bdc3c7';
        ctx.lineWidth = 2;
        ctx.beginPath();
        ctx.arc(cx, cy, radius, 0, Math.PI * 2);
        ctx.stroke();
    
        // Trilho
        if (innerRadius > 0) {
            ctx.strokeStyle = '#95a5a6';
            ctx.lineWidth = 10;
            ctx.beginPath();
            ctx.arc(cx, cy, innerRadius, this.startAngle, this.endAngle);
            ctx.stroke();
    
            // Valor atual
            const valueAngle = this.startAngle + (this.endAngle - this.startAngle) * this.value;
            ctx.strokeStyle = '#3498db';
            ctx.beginPath();
            ctx.arc(cx, cy, innerRadius, this.startAngle, valueAngle);
            ctx.stroke();
    
            // Marcador
            ctx.fillStyle = '#2c3e50';
            ctx.beginPath();
            const markerX = cx + Math.cos(valueAngle) * innerRadius;
            const markerY = cy + Math.sin(valueAngle) * innerRadius;
            ctx.arc(markerX, markerY, 8, 0, Math.PI * 2);
            ctx.fill();
        }
        
        
        // Texto
        ctx.fillStyle = '#2c3e50';
        ctx.font = 'bold 16px Arial';
        ctx.textAlign = 'center';
        ctx.textBaseline = 'middle';
        ctx.fillText(Math.round(this.value * 100) + '%', cx, cy);
    
        ctx.lineWidth = 1;
        g.restore();
    }
    
    isPointInside(x, y)
    {
        const dx = x - (this.x + this.width / 2);
        const dy = y - (this.y + this.height / 2);
        const dist = Math.sqrt(dx * dx + dy * dy);
        return dist <= this.radius;
    }
    
    _calculateValue(x, y)
    {
        const dx = x - (this.x + this.width / 2);
        const dy = y - (this.y + this.height / 2);
      let angle = Math.atan2(dy, dx);
    
 
        if (angle < this.startAngle)
        {
        // angle += Math.PI * 2;
        }
      
        //max 2.3608456194434537
      angle = Math.max(this.startAngle, Math.min(this.endAngle, angle));
      
 
      return (angle - this.startAngle) / (this.endAngle - this.startAngle);
    }
    
 
    
    
    
   
}