class Mouse {
    constructor(canvas) {
        this.x = 0;
        this.y = 0;
        this.shift = false;
        this.canvas = canvas;

        let self = this;

        document.onkeydown = function (evt) {
            self.shift = evt.shiftKey
        };

        document.onkeyup = function (evt) {
            self.shift = evt.shiftKey
        };

        this.canvas.onmousedown = function (evt) {
            self.down = true;
        };
        this.canvas.onmouseup = function (evt) {
            self.down = false;
        };
        this.canvas.onmousemove = function (evt) {
            let rect = self.canvas.getBoundingClientRect();
            let scale = rect.width / self.canvas.width;
            self.x = Math.trunc((evt.clientX - rect.left) / scale);
            self.y = Math.trunc((evt.clientY - rect.top) / scale);
        };

    }

    onMove(callback) {
        let mouse = this;
        this.canvas.addEventListener('mousemove', function () {
            callback.apply(mouse);
        });
    }

    onDrag(callback) {
        let mouse = this;
        this.onMove(function () {
            if (mouse.down) {
                callback.apply(mouse)
            }
        });
    }

    onClick(callback) {
        let mouse = this;
        this.canvas.addEventListener('click', function() {
            callback.apply(mouse);
        })
    }


    onMouseDown(callback) {
        let mouse = this;
        this.canvas.addEventListener('mousedown', function() {
            callback.apply(mouse);
        })
    }
}

export default Mouse;