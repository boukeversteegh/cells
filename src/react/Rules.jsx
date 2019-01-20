import React, {Component} from 'react';
import Rule from "./Rule";
import './Rules.css';

class Rules extends Component {
    render() {
        let rules = this.props.rules;
        let self = this;
        return (<div id="rules">{
            rules.map(function (rule, index) {
                return <Rule key={index} rule={rule} selectedCellType={self.props.selectedCellType} onClick={() => {
                    self.selectRule(rule)
                }}/>;
            })
        }
            <button onClick={this.props.onAddRule}>➕</button>
        </div>)
    }

    selectRule(rule) {

    }
}

export default Rules;