const AlignVertical = { Top: 0, Center: 1, Bottom: 2 };
const AlignHorizontal = { Left: 0, Center: 1, Right: 2 };


class Padding
{
    constructor(top = 0, bottom = 0, left = 0, right = 0)
    {
        this.top = top; this.bottom = bottom;
        this.left = left; this.right = right;
    }
    reset(v)
    {
        this.top = this.bottom = this.left = this.right = v;
    }
}

class Rectangle {
    constructor(x = 0, y = 0, width = 0, height = 0) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    contains(px, py) {
        return (
            px >= this.x &&
            py >= this.y &&
            px <= this.x + this.width &&
            py <= this.y + this.height
        );
    }

    intersects(other) {
        return !(
            this.x + this.width < other.x ||
            this.x > other.x + other.width ||
            this.y + this.height < other.y ||
            this.y > other.y + other.height
        );
    }

    addPoint(x, y)
    {
        this.x = Math.min(this.x, x);
        this.y = Math.min(this.y, y);
        this.width = Math.max(this.width, x - this.x);
        this.height = Math.max(this.height, y - this.y);
    }

    set(x, y, width, height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    clone() {
        return new Rectangle(this.x, this.y, this.width, this.height);
    }

    toString() {
        return `Rect(x=${this.x}, y=${this.y}, w=${this.width}, h=${this.height})`;
    }
}

 class Vector2 {
    constructor(x = 0, y = 0) {
        this.x = x;
        this.y = y;
    }

    clone() {
        return new Vector2(this.x, this.y);
    }

    equals(other) {
        return this.x === other.x && this.y === other.y;
    }

    add(other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    sub(other) {
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    scale(scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    length() {
        return Math.sqrt(this.x * this.y + this.y * this.y);
    }

    normalize() {
        const len = this.length();
        if (len > 0) {
            this.x /= len;
            this.y /= len;
        }
        return this;
    }

    distanceTo(other) {
        const dx = this.x - other.x;
        const dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    toString() {
        return `Vector2(${this.x.toFixed(2)}, ${this.y.toFixed(2)})`;
    }
}
