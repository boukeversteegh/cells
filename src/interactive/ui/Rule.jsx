'use strict';

class Rule extends React.Component {
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
                    neighbors.map((position, index) => {
                        let cellType = rule.getInputCellType(position.x, position.y);
                        return <CellType key={index} cellType={cellType} onClick={() => {
                            self.clickInputCell(position.x, position.y)
                        }}/>
                    })
                }</div>
                <div className="pattern output">{
                    neighbors.map((position, index) => {
                        let cellType = rule.getOutputCellType(position.x, position.y);
                        return <CellType key={index} cellType={cellType} onClick={() => {
                            self.clickOutputCell(position.x, position.y)
                        }}/>
                    })
                }</div>
            </div>
        }
        return (<div className="rule" data-editable={editable | 0}>{

        }</div>)
    }
}