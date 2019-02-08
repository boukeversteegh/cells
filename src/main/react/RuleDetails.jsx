import React, {Component} from 'react';
import './RuleDetails.css';

import Core from "./Core"

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
        this.props.app.Rules.onChange(rules => this.setState({rules: rules}));
        this.props.app.Rules.onSelect(rule => this.loadRule(rule));
        this.props.app.Rules.onUpdate(rule => this.loadRule(rule));
    }

    loadRule(rule) {
        this.setState({
            rule: rule,
            name: rule && rule.name ? rule.name : 'Rule',
            rotatable: rule && rule instanceof Core.PatternRule && rule.rotatable
        });
    }

    onChangeName(event) {
        this.state.rule.name = event.target.value;
        this.props.app.Rules.doUpdate(this.state.rule);
    }

    onChangeRotatable(event) {
        this.state.rule.rotatable = event.target.checked;
        this.props.app.Rules.doUpdate(this.state.rule);
    }

    render() {
        let rule = this.state.rule;
        let name = this.state.name;
        let rotatable = this.state.rotatable;
        let isCustomPatternRule = (rule instanceof Core.CustomPatternRule);

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
