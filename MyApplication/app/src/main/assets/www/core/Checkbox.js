
class Checkbox extends Widget
{
    constructor(label = "", checked = false, onChange = null)
    {
        super();
        this.label = label;
        this.checked = checked;
        this.onChange = onChange;
        this.padding = 5;
        this.boxSize = 18; 
        this.hovered = false;
    }



    handleMouseDown(x, y)
    {
        if (this.contains(x, y))
        {  
            this.checked = !this.checked;
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

   

    render(g) {
        if (!this.visible) return;

        const boxX = this.x + this.padding;
        const boxY = this.y + (this.height - this.boxSize) / 2;  
        // Caixa
        g.setColor(this.hovered ? Theme.colors[CHECKBOX_HOVER] : Theme.colors[CHECKBOX]);
        g.fillRect(boxX, boxY, this.boxSize, this.boxSize);

        // Borda
        g.setColor(Theme.colors[CHECKBOX_BORDER]);
        g.drawRect(boxX, boxY, this.boxSize, this.boxSize);

        
        // Check
        if (this.checked)
        {
            g.setColor(Theme.colors[CHECKBOX_CHECKED]);
            const startX = boxX + this.boxSize * 0.2;
            const startY = boxY + this.boxSize * 0.55;
            const midX   = boxX + this.boxSize * 0.45;
            const midY   = boxY + this.boxSize * 0.75;
            const endX   = boxX + this.boxSize * 0.8;
            const endY   = boxY + this.boxSize * 0.3;
        
            g.drawLine(startX, startY, midX, midY);
            g.drawLine(midX, midY, endX, endY);
        }

        // Label
        g.setColor(Theme.colors[CHECKBOX_TEXT]);
        g.ctx.font = '12px Arial';
        g.ctx.textAlign = 'left';
        g.ctx.textBaseline = 'top';
        const textSize = g.measureText(this.text);
        g.drawText(this.label, boxX + this.boxSize + this.padding, this.y + (this.height - 14) / 2);
        
        this.bound.set(boxX, boxY, this.width, this.height);

        if (this.hovered)
        {
            g.setColor(Theme.colors[CHECKBOX_OUTLINE]);
            g.drawRect(boxX, boxY, this.boxSize, this.boxSize);
        }


        g.setColor("#ff0000");
        g.drawRect(this.bound.x, this.bound.y, this.bound.width, this.bound.height);
    }


    
    
}
class CheckboxGroup extends Widget
{
    constructor(columns = 2) {
        super();
        this.columns = columns;
        this.checkboxes = [];
        this.values = new Map(); // label -> boolean
        this.spacing = 10;
    }

    add(label, defaultValue = false)
    {
        const checkbox = new Checkbox(label, defaultValue, (val) =>
        {
            this.values.set(label, val);
            if (this.onChange) this.onChange(label, val);
        });

        this.checkboxes.push(checkbox);
        this.values.set(label, defaultValue);
        return this;
    }


    setOnChange(callback)
    {
        this.onChange = callback;
        return this;
    }

    get(label)
    {
        return this.values.get(label);
    }

    getValues()
    {
        return Object.fromEntries(this.values);
    }

    update(dt)
    {
        for (const cb of this.checkboxes)
        {
            cb.update(dt);
        }
    }


    handleMouseDown(x, y)
    {
        if (!this.active || !this.visible) return false;
        if (!this.contains(x, y)) return false;
 
        for (const cb of this.checkboxes)
            {
                if (cb.handleMouseDown( x - this.x, y - this.y))
                {
                    return true;
                }
            }
            return false;
    }
    handleMouseMove(x, y)
    {
        if (!this.active || !this.visible) return false;
        if (!this.contains(x, y)) return false;
 
        for (const cb of this.checkboxes)
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
 
        for (const cb of this.checkboxes)
        {
            if (cb.handleMouseOut( x - this.x, y - this.y))
            {
                return true;
            }
        }
        return false;
    }
   



    _calculateCellSize()
    {
        const totalButtons = this.checkboxes.length;
        const rows = Math.ceil(totalButtons / this.columns);

        this.colWidth  = this.width / this.columns;
        this.rowHeight = this.height / rows;
    }

    render(g)
    {
        if (!this.visible) return;

        let maxWidth = 0;
        let maxHeight = 0;

        this._calculateCellSize();

        g.save();
        g.ctx.translate(this.x, this.y);
  

        for (let i = 0; i < this.checkboxes.length; i++)
        {
            const cb = this.checkboxes[i];
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

        this.bound.set(this.x, this.y, maxWidth, maxHeight);
        g.setColor("red");
        g.drawRect(this.bound.x, this.bound.y, this.bound.width, this.bound.height);
    }
}
