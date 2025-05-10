

class Graphics
{
    constructor(ctx)
    {
        this.ctx = ctx;
        
        
 
        this.lineWidth = 1; 
 
    }
 

    save()
    {
         
        this.lineWidth = this.ctx.lineWidth;
        this.ctx.save(); 
        
    }
    restore()
    {
        this.ctx.restore(); 
       
        this.ctx.lineWidth = this.lineWidth;
        
    }

    setLineWidth(width) { this.ctx.lineWidth = width; this.lineWidth = width; }

    getLineWidth() { return this.ctx.lineWidth; }

    setColor(color)
    {
        this.ctx.fillStyle = color;
        this.ctx.strokeStyle = color;
    }

    setStrokeColor(color)
    {
        this.ctx.strokeStyle = color;
    }

 
  

    drawText(text, x, y)
    {
        this.ctx.fillText(text, x, y);
    }

    measureText(text)
    {
        const metrics = this.ctx.measureText(text);
        return metrics.width;
    }

    clear(color = "#000000")
    {
        this.setColor(color);
        this.ctx.fillRect(0, 0, this.ctx.canvas.width, this.ctx.canvas.height);
    }

    fillRect(x, y, w, h)
    {
        this.ctx.fillRect(x, y, w, h);
    }

    drawRect(x, y, w, h)
    {
        this.ctx.strokeRect(x, y, w, h);
    }
    setMatrix(m) { this.ctx.setTransform(m.a, m.b, m.c, m.d, m.e, m.f); }
  setTransform(a, b, c, d, e, f) { this.ctx.setTransform(a, b, c, d, e, f); }
  
    drawCircle(x, y, r)
    { this.ctx.beginPath(); this.ctx.arc(x, y, r, 0, 2 * Math.PI); this.ctx.stroke(); }
    fillCircle(x, y, r)
    {
        this.ctx.beginPath();
        this.ctx.arc(x, y, r, 0, Math.PI * 2);
        this.ctx.fill();
    }

    drawLine(x1, y1, x2, y2)
    {
        this.ctx.beginPath();
        this.ctx.moveTo(x1, y1);
        this.ctx.lineTo(x2, y2);
        this.ctx.stroke();
    }
    drawRoundedRect(x, y, w, h, r)
    {
        const ctx = this.ctx;
        ctx.beginPath();
        ctx.moveTo(x + r, y);
        ctx.lineTo(x + w - r, y);
        ctx.quadraticCurveTo(x + w, y, x + w, y + r);
        ctx.lineTo(x + w, y + h - r);
        ctx.quadraticCurveTo(x + w, y + h, x + w - r, y + h);
        ctx.lineTo(x + r, y + h);
        ctx.quadraticCurveTo(x, y + h, x, y + h - r);
        ctx.lineTo(x, y + r);
        ctx.quadraticCurveTo(x, y, x + r, y);
        ctx.closePath();
        ctx.fill();
    }
    drawRoundedLinesRect(x, y, w, h, r)
    {
        const ctx = this.ctx;
        ctx.beginPath();
        ctx.moveTo(x + r, y);
        ctx.lineTo(x + w - r, y);
        ctx.quadraticCurveTo(x + w, y, x + w, y + r);
        ctx.lineTo(x + w, y + h - r);
        ctx.quadraticCurveTo(x + w, y + h, x + w - r, y + h);
        ctx.lineTo(x + r, y + h);
        ctx.quadraticCurveTo(x, y + h, x, y + h - r);
        ctx.lineTo(x, y + r);
        ctx.quadraticCurveTo(x, y, x + r, y);
        ctx.closePath();
        ctx.stroke();
      }
    clip(x, y, w, h)
    {
        const ctx = this.ctx;
        ctx.beginPath();
        ctx.rect(x, y, w, h);
        ctx.clip();
      }
    drawTriangle(x, y, size = 10, direction = 0)
    {
        const half = size / 2;
        this.ctx.beginPath();
    
          if (direction === 0)
          {
            this.ctx.moveTo(x - half, y - half);
            this.ctx.lineTo(x + half, y - half);
            this.ctx.lineTo(x, y + half);
        }
          else if (direction === 1)
          {
            this.ctx.moveTo(x - half, y + half);
            this.ctx.lineTo(x + half, y + half);
            this.ctx.lineTo(x, y - half);
        }
          else if (direction === 2)
          {
            this.ctx.moveTo(x + half, y - half);
            this.ctx.lineTo(x + half, y + half);
            this.ctx.lineTo(x - half, y);
        }
          else if (direction === 3)
          {
            this.ctx.moveTo(x - half, y - half);
            this.ctx.lineTo(x - half, y + half);
            this.ctx.lineTo(x + half, y);
        }
    
        this.ctx.closePath();
        this.ctx.fill();
    }
    
}
