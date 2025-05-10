

class Panel extends Widget
{
    constructor(x, y, width, height, text = "", style =0)
    {
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.style = style; // "flat", "raised", "lowered"
        this.color = Theme.colors[PANEL];
        this.borderLight = Theme.colors[PANEL_BORDERLIGHT];
        this.borderDark = Theme.colors[PANEL_BORDERDARK];
        this.textColor = "#000000";
    }

    setColor(color) {
        this.color = color;
        return this;
    }

    render(g) {
        if (!this.visible) return;

        // Fundo
        g.setColor(this.color);
        g.fillRect(this.x, this.y, this.width, this.height);

        // Borda 3D
        if (this.style === 1)//"raised")
        {
          
            g.setColor(this.borderLight);
            g.drawLine(this.x, this.y, this.x + this.width, this.y); // top
            g.drawLine(this.x, this.y, this.x, this.y + this.height); // left

            g.setColor(this.borderDark);
            g.drawLine(this.x, this.y + this.height - 1, this.x + this.width, this.y + this.height - 1); // bottom
            g.drawLine(this.x + this.width - 1, this.y, this.x + this.width - 1, this.y + this.height); // right

        } else if (this.style === 2)//"lowered")
        {

            g.setColor(this.borderDark);
            g.drawLine(this.x, this.y, this.x + this.width, this.y); // top
            g.drawLine(this.x, this.y, this.x, this.y + this.height); // left

            g.setColor(this.borderLight);
            g.drawLine(this.x, this.y + this.height - 1, this.x + this.width, this.y + this.height - 1); // bottom
            g.drawLine(this.x + this.width - 1, this.y, this.x + this.width - 1, this.y + this.height); // right
        }

        // Texto central
        if (this.text)
        {
            g.ctx.font = '14px Arial';
            g.ctx.textAlign = 'left';
            g.ctx.textBaseline = 'top';
            const len = g.measureText(this.text);
            const w = len;
            const h = 14;
            
            g.setColor(this.textColor);
            g.drawText(this.text, this.x + (this.width - w) / 2, this.y + (this.height - h) / 2);
        }
    }
}