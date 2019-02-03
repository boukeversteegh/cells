import React, {Component} from 'react';
import './RuleDetails.css';
import Events from "./Events";

import Core from "./Core"

class RuleDetails extends Component {
    constructor(props) {
        super(props);

        this.state = {
            rule: props.rule,
            name: null,
        };

        props.events.on(Events.RULE_SELECTED, rule => {
            this.loadRule(rule);
        });

        props.events.on(Events.RULE_UPDATED, rule => {
            if (rule === this.state.rule) {
                this.loadRule(rule);
            }
        });

        this.onChangeName = this.onChangeName.bind(this);
        this.onChangeRotatable = this.onChangeRotatable.bind(this);
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
        this.props.events.trigger(Events.RULE_UPDATED, this.state.rule);
    }

    onChangeRotatable(event) {
        console.log(event.target.checked);
        this.state.rule.rotatable = event.target.checked;
        this.props.events.trigger(Events.RULE_UPDATED, this.state.rule);
    }

    render() {
        let rule = this.state.rule;
        let name = this.state.name;
        let rotatable = this.state.rotatable;
        let isCustomPatternRule = (rule instanceof Core.CustomPatternRule);

        if (rule) {
            console.log(rule);
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
                <button onClick={() => {
                    this.props.events.trigger(Events.RULE_DELETED, rule)
                }}>Delete Rule
                </button>
            </div>
        } else {
            return <div id="rule-details"></div>
        }
    }
}

export default RuleDetails;
