
class Widget
{
    constructor()
    {
        this.x = 0;
        this.y = 0;
        this.width = 1;
        this.height = 1;
        this.minWidth = 0;
        this.minHeight = 0;
        this.maxWidth = Infinity;
        this.maxHeight = Infinity;
        this.alignVertical = AlignVertical.Top;
        this.alignHorizontal = AlignHorizontal.Left;
        this.expandWidth = true;
        this.expandHeight = true;
        this.padding = new Padding();
        this.percent = 0;
        this.parent = null;
        this.id = "Button";
        this.tag = 0;
        this.focus = false;
        this.visible = true;
        this.active = true;
        this.lines = false;
        this.bound = new Rectangle(0, 0, 0, 0);
    }

    clampSize()
    {
        this.width = Math.max(this.minWidth, Math.min(this.width, this.maxWidth));
        this.height = Math.max(this.minHeight, Math.min(this.height, this.maxHeight));
    }

    resize(w,h) {}
    
    update(dt) { }
    
    render(g) { }
    handleMouseDown(x, y)
    {
        return false;
    }
    handleMouseMove(x, y)
    {
        return false;
    }
    handleMouseOut(x, y)
    {
        return false;
    }
   

    getX() { return  this.x; }
    getY() { return  this.y; }
    
    setPosition(x, y) { this.x = x; this.y = y; return this; }
    setSize(width, height) { this.width = width; this.height = height; return this; }
    setId(id) { this.id = id; return this; }
    setTag(tag) { this.tag = tag; return this; }
    
    contains(px, py)
    {
        return this.bound.contains(px, py);
        // return (
        //     px >= this.x &&
        //     py >= this.y &&
        //     px <= this.x + this.width &&
        //     py <= this.y + this.height
        // );
    }

    debug(g)
    {
        if (!this.lines) return;
        g.setColor("#ff0000");
        g.drawRect(this.x, this.y, this.width, this.height);
    }
}