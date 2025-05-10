export class ImageBox extends Widget {
    constructor(image = null, fit = "contain") {
        super();
        this.image = image;  // Objeto Image()
        this.fit = fit;      // "contain" | "cover" | "stretch"
    }

    setImage(src) {
        this.image = new Image();
        this.image.src = src;
    }

    setFitMode(mode) {
        this.fit = mode;
    }

    render(g) {
        if (!this.visible || !this.image) return;

        const img = this.image;
        const { width, height } = this;

        let drawWidth = width;
        let drawHeight = height;
        let offsetX = 0;
        let offsetY = 0;

        if (this.fit === "contain") {
            const scale = Math.min(width / img.width, height / img.height);
            drawWidth = img.width * scale;
            drawHeight = img.height * scale;
            offsetX = (width - drawWidth) / 2;
            offsetY = (height - drawHeight) / 2;
        }
        else if (this.fit === "cover") {
            const scale = Math.max(width / img.width, height / img.height);
            drawWidth = img.width * scale;
            drawHeight = img.height * scale;
            offsetX = (width - drawWidth) / 2;
            offsetY = (height - drawHeight) / 2;
        }
        else if (this.fit === "stretch") {
            drawWidth = width;
            drawHeight = height;
        }

        g.ctx.drawImage(
            img,
            this.x + offsetX,
            this.y + offsetY,
            drawWidth,
            drawHeight
        );

        g.setColor(Theme.imageBorder);
        g.drawRect(this.x, this.y, width, height);
    }
}