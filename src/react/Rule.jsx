import React, {Component} from 'react';
import CellType from "./CellType";
import './Rule.css';

const cells = window.cells;

class Rule extends Component {
    constructor(props) {
        super(props);
        this.state = {
            rule: props.rule,
            isEditable: props.rule instanceof cells.interactive.CustomPatternRule,
            onClick: props.onClick,
            selectedCellType: props.selectedCellType,
        }
    }

    clickInputCell(x, y) {
        console.log(this);
        if (this.state.isEditable) {
            this.state.rule.setInput(x, y, this.props.selectedCellType);
            this.forceUpdate();
        } else {
            console.log("Not editable")
        }
    }

    clickOutputCell(x, y) {
        if (this.state.isEditable) {
            this.state.rule.setOutput(x, y, this.props.selectedCellType);
            this.forceUpdate();
        } else {
            console.log("Not editable")
        }
    }

    render() {
        const self = this;
        const editable = this.state.isEditable;
        const isPattern = this.state.rule instanceof cells.interactive.PatternRule;

        const rule = this.state.rule;
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
            return <div className="rule" data-editable={editable | 0} onClick={() => {
                this.state.onClick(rule)
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
                        let outputCellType = rule.getOutputCellType(pos.x, pos.y);
                        let inputCellType = rule.getInputCellType(pos.x, pos.y);

                        let cellType = (outputCellType === cells.interactive.Any ? inputCellType : outputCellType);
                        return <CellType
                            key={index}
                            cellType={cellType}
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