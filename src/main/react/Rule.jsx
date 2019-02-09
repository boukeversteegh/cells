import React, {Component} from 'react';
import './Rule.css';
import Events from "./Events";

import Core from "./Core"
import PatternRule from "./PatternRule";

class Rule extends Component {
    constructor(props) {
        super(props);

        this.state = {selected: false};

        if (props.events) {
            props.events.on(Events.RULE_UPDATED, (rule) => {
                if (rule === props.rule) {
                    this.forceUpdate();
                }
            });
        }
    }

    componentDidMount() {
        this.props.app.Rules.onSelect(rule => this.setState({
            selected: (rule === this.props.rule)
        }));
    }


    render() {
        const rule = this.props.rule;

        if (!rule) {
            return <div className={"rule"}>???</div>
        }

        const editable = rule.isEditable();
        const selected = this.state.selected;
        const name = rule.name ? rule.name : "Rule";

        let ruleBody = '';
        if (rule instanceof Core.PatternRule) {
            ruleBody = <PatternRule
                rule={rule}
                app={this.props.app}
                selected={selected}
            />;
        }
        return <div className="rule" data-editable={editable | 0} data-selected={selected | 0}>
            <div className="header" onClick={() => this.props.app.Rules.select(rule)}>{name}</div>
            {ruleBody}
        </div>;
    }
}

export default Rule;