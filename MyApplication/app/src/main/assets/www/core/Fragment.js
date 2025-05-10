class Fragment
{
    constructor()
    {
        this.layout = new Layout();
        this.activity = null;
    }
    create() { }
    close() { }
    update(dt) { }
    render(g) { } 
    debug(g) { } 
    resize(w, h) { }
    handleMouse(type, x, y, button) { return false; }

    onCreate()
    {   
        this.create();
    }
    onClose()
    {
        this.close();
    }
    onRender(g)
    {
        this.render(g);
        this.layout.render(g);
    }
    onDebug(g)
    {
        this.debug(g);
        this.layout.debug(g);
    }
    onUpdate(d)
    {
        this.update(d);
        this.layout.update(d);
    }
    onHandleMouse(type, x, y, button)
    {
        if (this.layout)
        {
            if (this.layout.handleMouse(type, x, y, button)) return true;
        }
        return this.handleMouse(type, x, y, button);
    }
}