import React, {Component} from 'react';
import CellType from "./CellType";
import './Rule.css';

const cells = window.cells;

class Rule extends Component {
    constructor(props) {
        super(props);
        this.state = {
        }
    }

    isEditable() {
        return this.props.rule instanceof cells.interactive.CustomPatternRule;
    }

    clickInputCell(x, y) {
        console.log(this);
        if (this.isEditable()) {
            this.props.rule.setInput(x, y, this.props.selectedCellType);
            this.forceUpdate();
        } else {
            console.log("Not editable")
        }
    }

    clickOutputCell(x, y) {
        if (this.isEditable()) {
            this.props.rule.setOutput(x, y, this.props.selectedCellType);
            this.forceUpdate();
        } else {
            console.log("Not editable")
        }
    }

    render() {
        const rule = this.props.rule;
        const self = this;
        const editable = this.isEditable();
        const isPattern = rule instanceof cells.interactive.PatternRule;

        const neighbors = [
            {x: -1, y: -1},
            {x: 0, y: -1},
            {x: 1, y: -1},

            {x: -1, y: 0},
            {x: 0, y: 0},
            {x: 1, y: 0},

            {x: -1, y: 1},
            {x: 0, y: 1},
            {x: 1, y: 1},
        ];


        if (isPattern) {
            // if (rule instanceof cells.interactive.CustomPatternRule) {
            //     // const minPosition = rule.getMinPosition();
            //     // const maxPosition = rule.getMaxPosition();
            //     //
            //     // console.log(minPosition, maxPosition);
            //     // for (let y = Math.min(-1, minPosition.y); y <= Math.max(1, maxPosition.y); y++) {
            //     //     for (let x = Math.min(-1, minPosition.x); x <= Math.max(1, maxPosition.x); x++) {
            //     //         console.log(y, x);
            //     //     }
            //     // }
            //
            //     for (let row of rule.getArea()) {
            //         console.log(row);
            //     }
            // }
            console.log(rule.getInput());

            return <div className="rule" data-editable={editable | 0} onClick={() => {
                this.props.onClick(rule)
            }}>
                <div className="pattern input">{
                    neighbors.map((pos, index) => {
                        let cellType = rule.getInputCellType(pos.x, pos.y);
                        return <CellType
                            key={index}
                            cellType={cellType}
                            // selected={pos.x === 0 && pos.y === 0}
                            onClick={() => {
                                self.clickInputCell(pos.x, pos.y)
                        }}/>
                    })
                }</div>
                <div className="pattern output">{
                    neighbors.map((pos, index) => {
                        let inputCellType = rule.getInputCellType(pos.x, pos.y);
                        let outputCellType = rule.getOutputCellType(pos.x, pos.y);
                        let hasOutput = outputCellType !== cells.interactive.Any;
                        let cellType = (hasOutput ? outputCellType : inputCellType);
                        console.log(outputCellType);
                        return <CellType
                            key={index}
                            cellType={cellType}
                            dim={!hasOutput}
                            // selected={pos.x === 0 && pos.y === 0}
                            onClick={() => {
                                self.clickOutputCell(pos.x, pos.y)
                            }}/>
                    })
                }</div>
            </div>
        }
        return (<div className="rule" data-editable={editable | 0}>{

        }</div>)
    }
}

export default Rule;