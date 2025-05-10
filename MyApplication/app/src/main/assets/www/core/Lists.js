

export class ListBox extends Widget
{
    constructor() {
        super();
        this.items = [];
        this.selectedIndex = -1;
        this.hoverIndex = -1;
        this.scroll = 0;
        this.itemHeight = 30;
        this.maxVisibleItems = 6;
        this.onSelect = null;
        this.itemSelected = false;
        this.dragStartY = 0;
        this.scrollStart = 0;
        this.dragging = false;
        this.dragTimer = 0;
        this.bound = new Rectangle(0, 0, 0, 0);
    }

    addItem(text)
    {
        this.items.push(text);
        const visibleCount = Math.min(this.maxVisibleItems, this.items.length);
        this.height = visibleCount * this.itemHeight;
    }

    handleMouse(type, x, y, button) {
        if (!this.enabled || !this.visible) return false;
        
        const localY = y - this.y;
        const localX = x - this.x;
      //  if (!this.contains(localX, localY)) return false;
        
        if (type === 0 && this.contains(x, y))
        { // Mouse down
            this.dragStartY = y;
            this.scrollStart = this.scroll;
            this.dragTimer = 0;
            this.dragging = false;
            const index = Math.floor((localY + this.scroll) / this.itemHeight);
            if (index >= 0 && index < this.items.length)
                {
                    this.selectedIndex = index;
                    this.itemSelected = true;
                if (this.onSelect)
                {
                    this.onSelect(index, this.items[index]);
                }
            }
            //return true;
        }

        if (type === 1)
        { // Mouse up
            this.dragging = false;
            this.hoverIndex = -1;
            this.dragTimer = 0;
            this.itemSelected = false;
        }

        if (type === 2)
        { // Mouse move

            this.itemSelected = false;
            if (this.dragging)
            {
                this.hoverIndex = -1;
                const dy = y - this.dragStartY;
                this.scroll = this.scrollStart - dy;
                this.clampScroll();
                return true;
            }  else  if (this.bound.contains(x, y))
            {
                const index = Math.floor((localY + this.scroll) / this.itemHeight);
                this.hoverIndex = index;
            }
        }

        return false;
    }

 

    clampScroll() {
        const maxScroll = Math.max(0, this.items.length * this.itemHeight - this.maxVisibleItems * this.itemHeight);
        this.scroll = Math.max(0, Math.min(this.scroll, maxScroll));
    }

    render(g) {
        if (!this.visible) return;
    
        const tumb = 10;
        g.setColor(Theme.listBackground);
        g.fillRect(this.x, this.y, this.width, this.height);
        this.bound.set(this.x, this.y, this.width, this.height);
    
    
        g.save();
        g.clip(this.x, this.y, this.width, this.height);
        g.ctx.translate(0, -this.scroll);
    
        for (let i = 0; i < this.items.length; i++)
        {
            const itemY = this.y + i * this.itemHeight;
    
            if (i === this.selectedIndex)
            {
                g.setColor(this.dragging ? Theme.listSelectedDragging : Theme.listSelected);
                g.fillRect(this.x, itemY, this.width-tumb, this.itemHeight);
            } else if (i === this.hoverIndex)
            {
                g.setColor(Theme.listHover);
                g.fillRect(this.x, itemY, this.width-tumb, this.itemHeight);
            }
    
            g.setColor(Theme.listText);
            g.drawText(this.items[i], this.x + 10, itemY + 8);
        }
    
        g.ctx.translate(0, this.scroll);
        g.restore();

        const totalHeight = this.items.length * this.itemHeight;
        if (totalHeight > this.height)
        {
            const scrollRatio = this.scroll / (totalHeight - this.height);
            const thumbHeight = Math.max(20, (this.height / totalHeight) * this.height);
            const thumbY = this.y + scrollRatio * (this.height - thumbHeight);
            const barX = this.x + this.width - tumb;

            // Track (barra de fundo)
            g.setColor(Theme.scrollbarTrack || "rgba(255,255,255,0.1)");
            g.fillRect(barX, this.y, 4, this.height);

            // Thumb (progresso atual)
            g.setColor(Theme.scrollbarThumb || "rgba(255,255,255,0.4)");
            g.fillRect(barX, thumbY, 4, thumbHeight);
        }

    
        g.setColor(Theme.listBorder);
        g.drawRect(this.x, this.y, this.width, this.height);
    }
    

    update(dt) {
        if (this.itemSelected)
        {
            this.dragTimer += dt;
            if (this.dragTimer > 0.5)
            {
                this.dragging = true;
            }
        }
    }
}


export class ComboBox extends Widget {
    constructor(items = [], onSelect = null) {
        super();
        this.items = items;
        this.selectedIndex = -1;
        this.hoverIndex = -1;
        this.open = false;
        this.scroll = 0;
        this.itemHeight = 30;
        this.maxVisibleItems = 4;
        this.onSelect = onSelect;
        this.dragging = false;
        this.dragTimer = 0;
        this.dragStartY = 0;
        this.scrollStart = 0;
        this.focus = false;
        this.hovered = false;
        this.pressed = false;
        this.boxSize = 25;
        this.bound = new Rectangle(0, 0, 0, 0);
        this.clicks = 0;
        this.lastItem = -1;

        // Animação
        this.listAnimation = 0;  // entre 0 e 1
        this.listTarget = 0;     // objetivo (0 fechado, 1 aberto)
        this.listSpeed = 6;      // velocidade da animação
    }

    toggleOpen() {
        this.open = !this.open;
        this.listTarget = this.open ? 1 : 0;
    }

    setSize(w, h) {
        this.width = w;
        this.height = h;
    
        this.boxSize = Math.min(this.height, this.width * 0.4);
        this.boxSize = Math.max(20, this.boxSize);
    

    }
    

    clampScroll() {
        const totalHeight = this.items.length * this.itemHeight;
        const visibleHeight = this.maxVisibleItems * this.itemHeight;
        const maxScroll = Math.max(0, totalHeight - visibleHeight);
        this.scroll = Math.max(0, Math.min(this.scroll, maxScroll));
    }

    pointInBox(px, py)
    {
        return (
            px >= this.x &&
            py >= this.y &&
            px <= this.x + this.width &&
            py <= this.y + this.boxSize
        );
    }
 
    handleMouse(type, x, y, button) {
        if (!this.enabled || !this.visible) return false;
    
        const localY = y - this.y; // y relativo ao início do ComboBox
    
        if (type === 0)
        { // Mouse Down
            if (this.open)
            {
                if (this.bound.contains(x, y))
                {
                    this.pressed = true;
                    this.dragStartY = y;//  localY - this.boxSize + this.scroll;  
                    this.scrollStart = this.scroll;
                    const index = Math.floor((localY + this.scroll - this.boxSize) / this.itemHeight);
                    if (index >= 0 && index < this.items.length)
                    {
                        this.selectedIndex = index;
                    
                    }
                    return true;
                } else
                {
                    this.toggleOpen();
                    return false;
                }
            } else
            {
                if (this.pointInBox(x, y))
                {
                    this.toggleOpen();
                    return true;
                }
            }
        }
    
        if (type === 1)
        { // Mouse Up
            if (this.dragging)
            {
                this.dragging = false;
            } else if (this.pressed && this.open)
            {
                const index = Math.floor((localY + this.scroll - this.boxSize) / this.itemHeight);
                if (index >= 0 && index < this.items.length)
                {
                     this.selectedIndex = index;
                    if (this.onSelect) this.onSelect(index, this.items[index]);
                    this.toggleOpen();
                }
            }
            this.pressed = false;
            this.hoverIndex = -1;
            this.dragTimer = 0;
        }
    
        if (type === 2)
        { // Mouse Move
            this.hovered = this.contains(x, y);
    
            if (this.open)
            {
                if (this.dragging)
                {
                    const dy = y - this.dragStartY;
                    this.scroll = this.scrollStart - dy;
                    this.clampScroll();
                    return true;
                 } 
                else
                {
                    const index = Math.floor((localY + this.scroll - this.boxSize) / this.itemHeight);
                    this.hoverIndex = index;
                }
            }
             
        }
    
        return false;
    }
    
    

    update(dt) {
        if (this.listAnimation !== this.listTarget)
        {
            const dir = Math.sign(this.listTarget - this.listAnimation);
            this.listAnimation += dir * this.listSpeed * dt;
            if (dir > 0 && this.listAnimation > this.listTarget) this.listAnimation = this.listTarget;
            if (dir < 0 && this.listAnimation < this.listTarget) this.listAnimation = this.listTarget;
        }
    
        if (this.open && this.pressed)
        {
            this.dragTimer += dt;
            if (this.dragTimer > 0.5)
            { // 0.5 segundos para ativar dragging
                this.dragging = true;
            }
        } else {
            this.dragTimer = 0;
        }
    }
    

    render(g) {
        if (!this.visible) return;

        const tumb = 10;


                // Atualiza dinamicamente quantos itens visíveis cabem abaixo do botão
        const availableHeight = this.height - this.boxSize;
        this.maxVisibleItems = Math.floor(availableHeight / this.itemHeight);
        this.maxVisibleItems = Math.max(3, this.maxVisibleItems); // garantir pelo menos 3

        // Caixa principal (fechada)
        g.setColor(Theme.comboBackground);
        g.fillRect(this.x, this.y, this.width, this.boxSize);

        g.setColor(Theme.comboText);
        const label = this.selectedIndex >= 0 ? this.items[this.selectedIndex] : "Selecionar...";

        const textY = this.y + (this.boxSize - 14) / 2; // Centro vertical
        g.drawText(label, this.x + 10, textY);

        // Seta
        g.setColor(Theme.comboArrow);
        g.drawText(this.open ? "▲" : "▼", this.x + this.width - 20, textY);

        // Borda
        g.setColor(Theme.comboBorder);
        g.drawRect(this.x, this.y, this.width, this.boxSize);

        this.bound.set(this.x, this.y, this.width, this.boxSize);

        if (this.listAnimation > 0)
        {
            const visibleCount = Math.min(this.maxVisibleItems, this.items.length);
            const fullListHeight = visibleCount * this.itemHeight;
            const listHeight = fullListHeight * this.listAnimation;

            // Fundo da lista
            g.setColor(Theme.comboListBackground);
            g.fillRect(this.x, this.y + this.boxSize, this.width, listHeight);

            g.save();
            g.ctx.translate(0, -this.scroll);

            for (let i = 0; i < this.items.length; i++)
            {
                const itemY = this.y + this.boxSize + i * this.itemHeight;

                if (itemY + this.itemHeight - this.scroll > this.y + this.boxSize + listHeight) break;
                if (itemY - this.scroll < this.y + this.boxSize) continue;

                if (i === this.selectedIndex)
                {
                    g.setColor(this.dragging ? Theme.comboSelectedDragging : Theme.comboListSelect);
                    g.fillRect(this.x, itemY, this.width-tumb, this.itemHeight);
                } else if (i === this.hoverIndex)
                {
                    g.setColor(Theme.comboListHover);
                    g.fillRect(this.x, itemY, this.width-tumb, this.itemHeight);
                }

                g.setColor(Theme.comboText);
                const itemTextY = itemY + (this.itemHeight - 14) / 2;
                g.drawText(this.items[i], this.x + 10, itemTextY);
            }

            g.ctx.translate(0, this.scroll);
            g.restore();

            g.setColor(Theme.comboBorder);
            g.drawRect(this.x, this.y + this.boxSize, this.width, listHeight);

            this.bound.set(this.x, this.y, this.width, this.boxSize + listHeight);


            const totalHeight = this.items.length * this.itemHeight;
            if (totalHeight > fullListHeight && this.open)
            {
                const scrollRatio = this.scroll / (totalHeight - fullListHeight);
                const thumbHeight = Math.max(20, (fullListHeight / totalHeight) * listHeight);
                const thumbY = this.y + this.boxSize + scrollRatio * (listHeight - thumbHeight);
                const barX = this.x + this.width - 8;
            
                // Track (barra de fundo)
                g.setColor(Theme.scrollbarTrack || "rgba(255,255,255,0.1)");
                g.fillRect(barX, this.y + this.boxSize, 4, listHeight);
            
                // Thumb (indicador de progresso)
                g.setColor(Theme.scrollbarThumb || "rgba(255,255,255,0.4)");
                g.fillRect(barX, thumbY, 4, thumbHeight);
            }
            


     
        }
        
    }
}

