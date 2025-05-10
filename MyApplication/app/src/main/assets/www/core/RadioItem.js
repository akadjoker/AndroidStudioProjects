 class RadioItem extends Widget
{
    constructor(label = "",   value = null)
    {
        super();
        this.label = label;
 
        this.value = value;
        this.selected = false;
        this.radius = 8;
        this.padding = 6;
        this.height = 16;
        this.width = 16;
        this.hovered = false;

        
    }
     
     
    handleMouseDown(x, y)
    {
        if (this.contains(x, y))
        {  
            this.selected = ! this.selected;
            if (this.onChange) this.onChange(this.checked);
            return true;
        }
        
    }
    handleMouseMove(x, y)
    {
        if (this.contains(x, y)) 
        {
            this.hovered = true;
            this.focus = true;
            return true;
        }       
        
        this.hovered = false;
        this.focus = false;
        
        return false;
    }

 
   

     render(g)
     {
        if (!this.visible) return;
  
      //  this.radius = Math.max(0, Math.min(this.width, this.height) / 2);
   //        this.radius = this.height - 2 * this.padding;

        const cx = this.x + this.radius + this.padding;
        const cy = this.y + this.height / 2;
        g.setColor(Theme.colors[RADIO_OUTER]);
        g.drawCircle(cx, cy, this.radius);

        g.ctx.font = '12px Arial';
        g.ctx.textAlign = 'left';
        g.ctx.textBaseline = 'top';
        const textSize = g.measureText(this.text);
    
        if (this.selected)
        {
            g.setColor(Theme.colors[RADIO_INNER]);
            g.fillCircle(cx, cy, (this.radius*0.5));
        }
    
        const size = g.measureText(this.label);
 

         g.setColor(Theme.colors[RADIO_TEXT]);
         
         this.bound.set(this.x, this.y, this.width, this.height);


        g.drawText(this.label, cx + this.radius + this.padding, this.y + (this.height - 12) / 2);
    
        if (this.hovered)
        {
            g.setColor(Theme.colors[RADIO_OUTLINE]);
            g.drawCircle(cx, cy, this.radius);
        }
        g.drawRect(this.x, this.y, this.width, this.height);
    }
    
}


class RadioGroup extends Widget
{
    constructor(columns = 1, spacing = 10)
    {
        super();
        this.buttons = [];
        this.selected = null;
        this.onChange = null;
        this.columns = columns;
        this.spacing = spacing;
        this.x = 0;
        this.y = 0;
        this.colWidth = 100;  
        this.rowHeight = 30;
        this.outline = true;
    }

    setCollumWidth(w) {
        this.colWidth = w;
    }

    setRowHeight(h) {
        this.rowHeight = h;
    }

    add(text,selected = false)
    {
        const b = new RadioItem(text);
        this.buttons.push(b);
        if (selected)
        {
           this.select(b);
        }
 
        return this;
    }



    update(dt) {
        for (const cb of this.buttons)
        {
            cb.update(dt);
        }
    }

    handleMouseDown(x, y)
    {
       if (!this.active || !this.visible) return false;
       if (!this.contains(x, y)) return false;

        let index = -1;

        for (let i = 0; i < this.buttons.length; i++)
        {
            const cb = this.buttons[i];
            if (cb.handleMouseDown( x- this.x , y - this.y ))
            {
               
                    index = i;
                    break;
            
            }
        }
        if (index >= 0)
        {
            for (let i = 0; i < this.buttons.length; i++)
            {
                const cb = this.buttons[i];
                if (i === index)
                {
                    cb.selected = true;
                    this.selected = cb;
                }
                else
                {
                    cb.selected = false;
                }
            }
            if (this.onChange)
            {
                this.onChange(this.selected);
            }
            return true;
        }
        return false;
    }

    handleMouseMove(x, y)
    {
        if (!this.active || !this.visible) return false;
        if (!this.contains(x, y)) return false;
        
        for (const cb of this.buttons)
        {
            if (cb.handleMouseMove( x - this.x, y - this.y))
            {
                return true;
            }
        }
       
        return false;
    }
    handleMouseOut(x, y)
    {
        if (!this.active || !this.visible) return false;
        if (!this.contains(x, y)) return false;
 
        for (const cb of this.buttons)
        {
            if (cb.handleMouseOut( x - this.x, y - this.y))
            {
                return true;
            }
        }
        return false;
    }
   

    render(g) {
        this._calculateCellSize();
    
        g.save();
        g.ctx.translate(this.x, this.y);
        let maxWidth = 0;
        let maxHeight = 0;
    
        for (let i = 0; i < this.buttons.length; i++)
        {
            const cb = this.buttons[i];
            const col = i % this.columns;
            const row = Math.floor(i / this.columns);

            cb.width = this.colWidth;
            cb.height = this.rowHeight;
            cb.setPosition(col * this.colWidth, row * this.rowHeight);
    
            maxWidth = Math.max(maxWidth, cb.x + cb.width);
            maxHeight = Math.max(maxHeight, cb.y + cb.height);

            cb.render(g);
        }
        
        g.restore();
    
        if (this.outline) {
            g.setColor(Theme.radioGroupBorder);
            g.drawRect(this.x - 2, this.y - 2, this.width, this.height);
        }

        this.bound.set(this.x, this.y, maxWidth, maxHeight);
       // g.setColor("red");
        //g.drawRect(this.bound.x, this.bound.y, this.bound.width, this.bound.height);
    }
    



    _calculateCellSize() {
        const totalButtons = this.buttons.length;
        const rows = Math.ceil(totalButtons / this.columns);
    
        this.colWidth = this.width / this.columns;
        this.rowHeight = this.height / rows;
    }

    
    setOnChange(callback)
    {
        this.onChange = callback;
        return this;
    }
    select(button)
    {
        this.buttons.forEach(b => b.selected = false);
        button.selected = true;
        this.selected = button;
        if (this.onChange) this.onChange(button.value);
    }

    getValue()
    {
        return this.selected ? this.selected.value : null;
    }

    setValue(index)
    {
            if (index >= 0)
            {
                for (let i = 0; i < this.buttons.length; i++)
                {
                    const cb = this.buttons[i];
                    if (i === index)
                    {
                        cb.selected = true;
                        this.selected = cb;
                    }
                    else
                    {
                        cb.selected = false;
                    }
                }
                if (this.onChange)
                {
                    this.onChange(this.selected);
                }
                return true;
            }
            return false;
    }
}
