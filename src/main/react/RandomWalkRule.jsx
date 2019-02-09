import React, {Component} from 'react';
import CellType from "./CellType";

import './RandomWalkRule.scss';

class RandomWalkRule extends Component {
    constructor(props) {
        super(props);

        this.state = {
            selectedCellType: null,
        };
    }

    componentDidMount() {
        this.props.app.CellTypes.selected.observe(cellType => this.setState({selectedCellType: cellType}));
        this.props.app.Rules.onUpdate(rule => {
            if (rule === this.props.rule) {
                this.forceUpdate();
            }
        });
    }

    setCellType() {
        this.props.app.RandomWalkRules.setCellType(this.props.rule, this.state.selectedCellType)
    }

    setBackground() {
        this.props.app.RandomWalkRules.setBackground(this.props.rule, this.state.selectedCellType)
    }

    render() {
        return <div className="random-walk-rule body">
            <div className="input"><CellType cellType={this.props.rule.cellType} onClick={() => {
                this.setCellType();
            }}/> Walker
            </div>
            <div className="input"><CellType cellType={this.props.rule.background} onClick={() => {
                this.setBackground();
            }}/> Back
            </div>
        </div>
    }
}

export default RandomWalkRule;