import React, {Component} from 'react';
import './Screen.css';
import Mouse from "./util/Mouse";

class Screen extends Component {
    constructor(props) {
        super(props);
        this.canvas = React.createRef();
        this.state = {
            frames: 0,
            startTime: performance.now(),
            iterateTime: 0,
            renderTime: 0,
            frameTime: 0
        }
    }

    resetStats() {
        this.setState({
            frames: 0,
            startTime: performance.now(),
            iterateTime: 0,
            renderTime: 0,
            frameTime: 0
        })
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
            let t0 = performance.now();
            self.props.automaton.iterate();
            let t1 = performance.now();
            self.updateScreen();
            let t2 = performance.now();
            self.setState({
                frames: self.state.frames + 1,
                iterateTime: t1 - t0,
                renderTime: t2 - t1,
                frameTime: t2 - t0,
                averageFps: self.state.frames / (performance.now() - self.state.startTime) * 1000
            })
        }, 50);
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
        return <div id="screen">
            <canvas
            width={this.props.width}
            height={this.props.height}
            ref={this.canvas}
        />
            <div id="fps" onClick={() => {
                this.resetStats()
            }}>
                <div>automaton: {(1000/this.state.iterateTime)|0}</div>
                <div>renderer: {(1000/this.state.renderTime)|0}</div>
                <div>{(1000/this.state.frameTime)|0}fps</div>
                <div>{this.state.averageFps | 0}avg fps</div>
            </div>
        </div>
    }
}

export default Screen;
