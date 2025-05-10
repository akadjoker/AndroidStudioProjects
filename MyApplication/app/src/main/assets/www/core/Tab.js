

export class TabItem
{
    constructor(label, closable = false)
    {
        this.label = label;
        this.closable = closable;
        this.content = new Layout();
        this.bounds = new Rectangle(); 
        this.closeBounds = new Rectangle(); // Para detectar o clique no [x]
        this.over = false;
    }
}


export class TabView extends Widget {
    constructor(position = "top") {
        super();
        this.tabs = [];
        this.activeIndex = 0;
        this.position = position;
        this.headerHeight = 30;
        this.tabWidth = 80;
        this.scrollOffset = 0;
        this.scrollStep = 50;
        this.maxScroll = 0;

        this.scale = 0;
        this.tweenOut = new Tween(this, "scale", 0, 1, 0.5, Tween.EASE_OUT_BOUNCE, Tween.MODE_PERSIST, false);
        this.tweenOut.play();
    }

    setTabeWidth(width) {
        this.tabWidth = width;
    }

    setTabHeaderHeight(height) {
        this.headerHeight = height;
    }

    update(dt) {
        if (!this.visible) return;
        this.tweenOut.update(dt);

        const active = this.tabs[this.activeIndex];
        if (active) active.content.update(dt);
    }

    addTab(label, closable = false)
    {
        const tab = new TabItem(label, closable);
        this.tabs.push(tab);
      //  if (this.tabs.length === 1) this.activeIndex = 0;
        return tab.content;
    }

    render(g) {
        if (!this.visible) return;
    
        const headerY = this.position === "top" ? this.y : (this.y + this.height- this.headerHeight);
        const visibleWidth = this.width - 40;
        const totalTabWidth = this.tabs.length * this.tabWidth;
        this.maxScroll = Math.max(0, totalTabWidth - visibleWidth);
        this.scrollStep = this.tabWidth;
    
        // Fundo geral
        g.setColor(Theme.tabBackground);
        g.fillRect(this.x, this.y, this.width, this.height);
    
        for (let i = 0; i < this.tabs.length; i++) {
            const tab = this.tabs[i];
            const tx = this.x + 20 + i * this.tabWidth - this.scrollOffset;
            tab.bounds.set(tx, headerY, this.tabWidth, this.headerHeight);
    
            if (tx + this.tabWidth < this.x + 20 || tx > this.x + this.width - 20) continue;
    
            g.save();
            g.clip(this.x, headerY, this.width, this.headerHeight);
    
            // Cor da aba
            g.setColor(i === this.activeIndex ? Theme.tabActive : Theme.tabInactive);
            g.fillRect(tx, headerY, this.tabWidth, this.headerHeight);
    
            // Texto da aba
            g.setColor(Theme.tabLabel);
            g.drawText(tab.label, tx + 10, headerY + 8);
    
            // Botão fechar (se aplicável)
            if (tab.closable) {
                tab.closeBounds.set(tab.bounds.x + tab.bounds.width - 18, tab.bounds.y + 6, 12, 12);
                g.setColor(Theme.tabCloseIcon);
                g.drawText("×", tab.closeBounds.x, tab.closeBounds.y);
            }
    
            // Hover
            if (tab.over) {
                g.setColor(Theme.tabHoverOutline);
                g.drawRect(tx, headerY, this.tabWidth, this.headerHeight);
            }
    
            g.restore();
        }
    
        // Setas de scroll
        if (this.maxScroll > 0) {
            g.setColor(Theme.tabScrollButton);
            g.fillRect(this.x, headerY, 20, this.headerHeight);
            g.setColor(Theme.tabScrollIcon);
            g.drawText("\u2039", this.x + 6, headerY + 8);
    
            g.setColor(Theme.tabScrollButton);
            g.fillRect(this.x + this.width - 20, headerY, 20, this.headerHeight);
            g.setColor(Theme.tabScrollIcon);
            g.drawText("\u203A", this.x + this.width - 14, headerY + 8);
        }
    
        // Conteúdo ativo
        const active = this.tabs[this.activeIndex];
        if (active) {
            const Y = this.position === "top" ? this.headerHeight : 0;
            g.save();
            g.clip(this.x, this.y, this.width, this.height);
            g.ctx.translate(this.x + 5, Y + this.y + 5);
            g.ctx.scale(this.scale, this.scale);
            active.content.x = 0;
            active.content.y = 0;
            active.content.width = this.width;
            active.content.height = this.height;
            active.content.render(g);
            g.restore();
        }
    }
    

    handleMouseOut(x, y)
    {
     

        const localX = x - this.x;
        const localY = y - this.y;
        const Y = this.position === "top" ? this.headerHeight : 0;
        const active = this.tabs[this.activeIndex];
        // if (active && active.content)
        // {
        //     active.content.handleMouseOut(localX - active.content.x, localY - active.content.y - Y);
        // }

        for (let i = 0; i < this.tabs.length; i++)
        {
            if (!this.tabs[i].bounds.contains(x, y)) this.tabs[i].over = false;
        }
    }

    handleMouse(type, x, y, button)
    {
        if (!this.visible) return false;
        const headerY = this.position === "top" ? this.y : (this.y + this.height - this.headerHeight);
        const localX = x - this.x;
        const localY = y - this.y;
        const Y = this.position === "top" ? this.headerHeight : 0;

        if (type === 0)
        { // Mouse Down
            if (this.maxScroll > 0)
            {
                if (x >= this.x && x <= this.x + 20)
                {
                    this.scrollOffset = Math.max(0, this.scrollOffset - this.scrollStep);
                    return true;
                }
                if (x >= this.x + this.width - 20 && x <= this.x + this.width)
                {
                    this.scrollOffset = Math.min(this.maxScroll, this.scrollOffset + this.scrollStep);
                    return true;
                }
            }

            for (let i = 0; i < this.tabs.length; i++)
            {
                const tab = this.tabs[i];
                if (tab.closable && tab.closeBounds.contains(x, y))
                {
                    this.tabs.splice(i, 1);
                    if (this.activeIndex >= this.tabs.length)
                    {
                        this.activeIndex = this.tabs.length - 1;
                    }
                    return true;
                }
                
                if (this.tabs[i].bounds.contains(x, y) && i !== this.activeIndex)
                {
                    if (!this.tweenOut.playing)
                    {
                        this.tweenOut.reset();
                        this.tweenOut.play();
                        this.activeIndex = i;
                    }
                    return true;
                }
            }
        }

        if (type === 1)
        { // Mouse Up
            this.handleMouseOut(x, y);
        }

        if (type === 2)
        { // Mouse Move
            for (let i = 0; i < this.tabs.length; i++)
            {
                this.tabs[i].over = false;
                if (this.tabs[i].bounds.contains(x, y) && i !== this.activeIndex)
                {
                    this.tabs[i].over = true;
                    return true;
                }
            }
        }

        const active = this.tabs[this.activeIndex];
        if (active && active.content)
        {
            return active.content.handleMouse(type, localX - active.content.x, localY - active.content.y - Y, button);
        }
        return false;
    }

    setActiveIndex(index) {
        if (index >= 0 && index < this.tabs.length) {
            this.activeIndex = index -1;
        }
    }
}
