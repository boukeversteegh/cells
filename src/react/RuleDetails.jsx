import React, {Component} from 'react';
import './RuleDetails.css';
import Events from "./Events";

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
    }

    loadRule(rule) {
        this.setState({
            rule: rule,
            name: rule && rule.name ? rule.name : 'Rule'
        });
    }

    onChangeName(event) {
        this.state.rule.name = event.target.value;
        this.props.events.trigger(Events.RULE_UPDATED, this.state.rule);
    }

    render() {
        let rule = this.state.rule;
        let name = this.state.name;

        if (rule) {
            console.log(rule);
            return <div id="rule-details">
                <input value={name} onChange={this.onChangeName}
                       readOnly={!(rule instanceof window.cells.interactive.CustomPatternRule)}/>
                <button onClick={() => {
                    this.props.events.trigger(Events.RULE_DELETED, rule)
                }}>Delete Rule
                </button>
            </div>
        } else {
            return <div id="rule-details">Select a rule</div>
        }
    }
}

export default RuleDetails;
