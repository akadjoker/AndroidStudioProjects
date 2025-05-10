

 class Label extends Widget
{
    constructor(x,y,text = "")
    {
        super();
        this.x = x;
        this.y = y;
        this.width = 100;
        this.height = 16;
        this.text = text;
        this.align = AlignHorizontal.Left;
        this.color = Theme.colors[LABEL];
        this.autoSize = false;
        this.firstFrame = true;
    }

     render(g)
     {
        if (!this.visible) return;
        g.ctx.font = '16px Arial';
        g.ctx.textAlign = 'left';
        g.ctx.textBaseline = 'top';
        const textSize = g.measureText(this.text);

         this._length = textSize;
         if (this.firstFrame)
         {
            this.width = textSize;
            this.firstFrame = false;
         }
         if (this.autoSize)
         {
            this.width = textSize;
         }

        
        const padding = this.padding;
  
         
        let drawX = this.x;

         if (this.align === AlignHorizontal.Center)
         {
            drawX = this.x -(this.width*0.5);
         } else if (this.align === AlignHorizontal.Right)
         {
             drawX = this.x + this.width;
        }

        
   
        
        g.setColor(this.color);
        g.drawText(this.text, drawX, this.y -  (this.height     * 0.5 ));

     }
     
     debug(g)
     {
        if (!this.lines) return;
        g.setColor("#ff0000");
        g.drawRect(this.x, this.y-(this.height*0.5), this.width, this.height );
     }

     setText(text)
     {
        this.text = text;
        return this;
    }

     setAlign(align)
     {
        this.align = align;
        return this;
    }

     setColor(color)
     {
        this.color = color;
        return this;
     }
     getlength()
     {
        return this._length;
     }
}
