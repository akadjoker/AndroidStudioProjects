class Layout extends Widget
{
    constructor()
    {
        super(0,0,0,0);
        this.children = [];
        this.id = "Layout";
 
    }
    add(widget)
    {
        widget.parent = this;
        this.children.push(widget);
        return widget;
    }
    remove(widget)
    {
        const idx = this.children.indexOf(widget);
        if (idx !== -1)
        {
            this.children.splice(idx, 1);
            widget.parent = null;
        }
    }
    clear()
    {
        for (let child of this.children) child.parent = null;
        this.children = [];
    }
    resize(w, h)
    {
        for (let child of this.children)
        {
            child.resize(w,h);
        }
    }
    render(renderer)
    {
 
        for (let child of this.children)
        {
            child.render(renderer);
        }
    }
    debug(renderer)
    {
        super.debug(renderer);
        for (let child of this.children)
        {
            child.debug(renderer);
        }
    }

    handleMouse(type, x, y, button)
    {
        if (type === Input.WHEEL)
        {
            for (let i = this.children.length - 1; i >= 0; i--)
            {
                const child = this.children[i];
               
                if (child.handleMouseWheel(x, y))
                {
                    break;
                }
                
            }
        }
        if (type === Input.UP)
        {
            for (let i = this.children.length - 1; i >= 0; i--)
            {
                const child = this.children[i];
                if (child.handleMouseOut(x, y))
                {
                   
                }
            }
        }
        if (type === Input.DOWN)
        {
            for (let i = this.children.length - 1; i >= 0; i--)
            {
                let child = this.children[i];
                if (child.handleMouseDown(x, y))
                {
                    break;
                }
            } 
        }
    
        let active=-1;
        if (type === Input.MOVE)
        {
            for (let i = this.children.length - 1; i >= 0; i--)
            {
                let child = this.children[i];
                {
                    if (child.handleMouseMove(x, y))
                    {
                         active = i;
                         break;
                    }
                    
                }
            }
        }
        if (active !== -1)
        {
            for (let i = this.children.length - 1; i >= 0; i--)
            {
                let child = this.children[i];
                {
                    if (i !== active)
                    {
                        child.handleMouseOut(x, y);
                    }
                
                }
            }
        }
        return false;
    }

    getById(id)
    {
        for (let child of this.children)
        {
            if (child.id === id) return child;
        }
        return null;
    }
    getByTag(tag)
    {
        for (let child of this.children)
        {
            if (child.tag === tag) return child;
        }
        return null;
    }
}