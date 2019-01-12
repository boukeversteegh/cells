'use strict';

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

class UI {
    constructor() {
        this.automaton = cells.interactive.init();

        this.canvas = document.getElementById('display');
        this.canvas.width = this.automaton.w;
        this.canvas.height = this.automaton.h;
        this.ctx = this.canvas.getContext('2d');
        this.selectedCellType = cells.interactive.Dirt;

        this.render();

        let self = this;

        this.mouse = new Mouse(this.canvas);
        this.mouse.onDrag(function () {
            self.paint(this.x, this.y);
        });

        this.mouse.onMouseDown(function () {
            self.paint(this.x, this.y);
        });

        this.mouse.onMove(function () {
            if (this.shift) {
                self.paint(this.x, this.y);
            }
        });

        this.start();

        self.initReact();
    }

    paint(x, y) {
        this.automaton.getLayers()[0].set(x, y, this.selectedCellType)
    }

    render() {
        for (let y = 0; y < this.automaton.h; y++) {
            for (let x = 0; x < this.automaton.w; x++) {
                this.ctx.fillStyle = this.automaton.getColor(x, y);
                this.ctx.fillRect(x, y, 1, 1);
            }
        }
    }

    start() {
        let self = this;
        window.setInterval(function () {
            self.automaton.iterate();
            self.render();
        }, 100);
    }

    initReact() {
        const e = React.createElement;

        const toolBar = document.querySelector('#toolbar');
        let layer = this.automaton.getLayers()[0];
        ReactDOM.render(e(ToolBar, {
            layer: layer,
            cellTypes: layer.getCellTypes(),
            rules: layer.getRules(),
            onSelectCellType: (cellType) => {
                this.selectedCellType = cellType;
            },
        }), toolBar);
    }
}
