import React, {Component} from 'react';
import './RuleDetails.css';

import Core from "./Core"

import Cells from "../../build/cells";

class RuleDetails extends Component {
    constructor(props) {
        super(props);

        this.state = {
            rule: props.rule,
            name: null,
        };

        this.onChangeName = this.onChangeName.bind(this);
        this.onChangeRotatable = this.onChangeRotatable.bind(this);
    }

    componentDidMount() {
        this.props.app.Rules.changes.observe(rules => this.setState({rules: rules}));
        this.props.app.Rules.selected.observe(rule => this.loadRule(rule));
        this.props.app.Rules.updates.observe(rule => this.loadRule(rule));
    }

    loadRule(rule) {
        this.setState({
            rule: rule,
            name: rule && rule.name ? rule.name : 'Rule',
            rotatable: rule && rule instanceof Core.PatternRule && rule.rotatable
        });
    }

    onChangeName(event) {
        this.props.app.Rules.setName(this.state.rule, event.target.value);
    }

    onChangeRotatable(event) {
        this.state.rule.rotatable = event.target.checked;
        this.props.app.Rules.update(this.state.rule);
    }

    render() {
        let rule = this.state.rule;
        let name = this.state.name;
        let rotatable = this.state.rotatable;
        let isCustomPatternRule = (rule instanceof Core.EditablePatternRule);

        if (rule) {
            return <div id="rule-details">
                <input type="text" value={name} onChange={this.onChangeName}
                       readOnly={!isCustomPatternRule}/>
                {
                    isCustomPatternRule && <label>Rotatable<input
                        type="checkbox"
                        value={true}
                        onChange={this.onChangeRotatable}
                        checked={rotatable}
                        readOnly={!isCustomPatternRule}
                    /></label>
                }
                <button
                    onClick={() => this.props.app.Rules.delete(rule)}
                >
                    Delete Rule
                </button>
            </div>
        } else {
            return <div id="rule-details"></div>
        }
    }
}

export default RuleDetails;
