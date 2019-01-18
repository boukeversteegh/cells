import React, {Component} from 'react';
import './Screen.css';
import Mouse from "../util/Mouse";

class Screen extends Component {
    constructor(props) {
        super(props);
        this.canvas = React.createRef();
    }

    componentDidMount() {
        this.mouse = new Mouse(this.canvas.current);

        let self = this;
        this.mouse.onDrag(function () {
            self.props.onPaint(this.x, this.y);
        });

        this.mouse.onMouseDown(function () {
            self.props.onPaint(this.x, this.y);
        });

        this.mouse.onMove(function () {
            if (this.shift) {
                self.props.onPaint(this.x, this.y);
            }
        });

        this.ctx = this.canvas.current.getContext('2d');
        this.updateScreen();
        this.start();
    }

    start() {
        let self = this;
        window.setInterval(function () {
            self.props.automaton.iterate();
            self.updateScreen();
        }, 1000);
    }

    updateScreen() {
        let ctx = this.ctx;
        let automaton = this.props.automaton;
        for (let y = 0; y < automaton.h; y++) {
            for (let x = 0; x < automaton.w; x++) {
                ctx.fillStyle = automaton.getColor(x, y);
                ctx.fillRect(x, y, 1, 1);
            }
        }
    }

    render() {
        return <canvas
            width={this.props.width}
            height={this.props.height}
            ref={this.canvas}
        />
    }
}

export default Screen;
