
export class TextView extends Widget
{
    constructor(content)
    {
      super();
      this.content = content;
      this.scrollPosition = 0;
      this.maxScroll = 0;
      this.isDraggingScroll = false;
      this.scrollBarWidth = 15;
      this.lineHeight = 20;
    }
    
    render(g)
    {
      const ctx = g.ctx;
      // Fundo
      ctx.fillStyle = '#ecf0f1';
      ctx.fillRect(this.x, this.y, this.width, this.height);
      
      // Área de conteúdo
      ctx.save();
      ctx.beginPath();
      ctx.rect(this.x, this.y, this.width, this.height);
      ctx.clip();
      
      // Renderizar texto
      this._renderContent(ctx);
      
      ctx.restore();
      
      // Barra de rolagem
      this._drawScrollBar(ctx);
    }
    
    _renderContent(ctx)
    {
      ctx.fillStyle = '#34495e';
      ctx.font = '14px Arial';
      ctx.textAlign = 'left';
      ctx.textBaseline = 'top';
      
      const maxWidth = this.width - this.scrollBarWidth - 10;
      const words = this.content.split(' ');
      
      let line = '';
      let y = this.y + 10 - this.scrollPosition;
      
        for (let i = 0; i < words.length; i++)
        {
        const testLine = line + words[i] + ' ';
        const metrics = ctx.measureText(testLine);
        
            if (metrics.width > maxWidth && i > 0)
            {
          ctx.fillText(line, this.x + 10, y);
          line = words[i] + ' ';
          y += this.lineHeight;
        } else {
          line = testLine;
        }
      }
      
      ctx.fillText(line, this.x + 10, y);
      
      // Calcular altura total do conteúdo
      const contentHeight = y + this.lineHeight - this.y + this.scrollPosition;
      this.maxScroll = Math.max(0, contentHeight - this.height + 20);
    }
    
    _drawScrollBar(ctx)
    {
        if (this.maxScroll > 0)
        {
        const contentHeight = this.scrollPosition + this.height + this.maxScroll;
        const scrollHeight = Math.max(30, this.height * (this.height / contentHeight));
        const scrollPosition = (this.scrollPosition / this.maxScroll) * (this.height - scrollHeight);
        
        // Trilho da barra de rolagem
        ctx.fillStyle = '#bdc3c7';
        ctx.fillRect(this.x + this.width - this.scrollBarWidth, this.y, this.scrollBarWidth, this.height);
        
        // Barra de rolagem
        ctx.fillStyle = this.isDraggingScroll ? '#7f8c8d' : '#95a5a6';
        ctx.fillRect(
          this.x + this.width - this.scrollBarWidth + 2, 
          this.y + scrollPosition, 
          this.scrollBarWidth - 4, 
          scrollHeight
        );
      }
    }
    
    _isPointInScrollBar(x, y)
    {
      return x >= this.x + this.width - this.scrollBarWidth && 
             x <= this.x + this.width && 
             y >= this.y && 
             y <= this.y + this.height;
    }



    handleMouse(type, x, y, button)
    {
        if (!this.enabled || !this.visible) return false;
        
        if (type === 0 && this.contains(x, y))
        { // DOWN
            if (this.handleMouseDown(x, y)) {
                return true;
            }
        }

        if (type === 1) { // UP
            this.isDraggingScroll = false;
        }

       

        if (type === 2 )
        { // MOVE

            if (this.handleMouseMove(x, y)) {
                return true;
            }
            
        }

        
        return false;
    }
    
    handleMouseMove(x, y)
    {
        if (this.isDraggingScroll)
        {
        const scrollableHeight = this.height - (this.height * (this.height / (this.maxScroll + this.height)));
        const scrollRatio = (y - this.y) / this.height;
        this.scrollPosition = Math.max(0, Math.min(this.maxScroll, this.maxScroll * scrollRatio));
        return true;
      }
      return this.contains(x, y);
    }
    
    handleMouseDown(x, y)
    {
        if (this._isPointInScrollBar(x, y))
        {
        this.isDraggingScroll = true;
        return true;
      }
      return this.contains(x, y);
    }
    
 
    
    handleWheel(deltaY)
    {
        if (this.contains(this.lastMouseX, this.lastMouseY))
        {
        this.scrollPosition += deltaY * 0.5;
        this.scrollPosition = Math.max(0, Math.min(this.maxScroll, this.scrollPosition));
        return true;
      }
      return false;
    }
  }
