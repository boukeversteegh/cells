import React, {Component} from 'react';
import './PatternRule.scss';
import CellType from "./CellType";
import Core from "./Core";

class PatternRule extends Component {
    constructor(props) {
        super(props);

        this.state = {
            selectedCellType: null,
        };
    }

    componentDidMount() {
        this.props.app.CellTypes.selected.observe(cellType => this.setState({selectedCellType: cellType}))
        this.props.app.Rules.updates.observe(rule => {
            if (rule === this.props.rule) {
                this.forceUpdate();
            }
        })
    }

    clickInputCell(x, y) {
        if (this.isEditable()) {
            this.props.app.EditablePatternRules
                .setInput(this.props.rule, Core.pos(x, y), this.state.selectedCellType);
        }
    }

    clickOutputCell(x, y) {
        if (this.isEditable()) {
            this.props.app.EditablePatternRules
                .setOutput(this.props.rule, Core.pos(x, y), this.state.selectedCellType);
        }
    }

    isEditable() {
        return this.props.rule instanceof Core.EditablePatternRule;
    }

    render() {
        const rule = this.props.rule;
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

        const self = this;

        return <div className="patterns">
            <div className="pattern input">{
                neighbors.map((pos, index) => {
                    let cellType = rule.getInputCellType(pos.x, pos.y);
                    return <CellType
                        key={index}
                        cellType={cellType}
                        onClick={() => {
                            self.clickInputCell(pos.x, pos.y)
                        }}/>
                })
            }</div>
            <div className="pattern output">{
                neighbors.map((pos, index) => {
                    let inputCellType = rule.getInputCellType(pos.x, pos.y);
                    let outputCellType = rule.getOutputCellType(pos.x, pos.y);
                    let hasOutput = outputCellType !== Core.Any;
                    let cellType = (hasOutput ? outputCellType : inputCellType);
                    return <CellType
                        key={index}
                        cellType={cellType}
                        dim={!hasOutput}
                        onClick={() => {
                            self.clickOutputCell(pos.x, pos.y)
                        }}/>
                })
            }</div>
        </div>
    }
}

export default PatternRule;