import React, {Component} from 'react';
import './Rule.css';

import Core from "./Core"
import PatternRule from "./PatternRule";
import RandomWalkRule from "./RandomWalkRule";

class Rule extends Component {
    constructor(props) {
        super(props);

        this.state = {selected: false};
    }

    componentDidMount() {
        this.props.app.Rules.selected.observe(rule => this.setState({
            selected: (rule === this.props.rule)
        }));

        this.props.app.Rules.updates.observe(rule => {
            if (rule === this.props.rule) {
                this.forceUpdate();
            }
        })
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
            ruleBody = <PatternRule rule={rule} app={this.props.app}/>;
        }
        if (rule instanceof Core.RandomWalkRule) {
            ruleBody = <RandomWalkRule rule={rule} app={this.props.app}/>;
        }
        return <div className="rule" data-editable={editable | 0} data-selected={selected | 0}>
            <div className="header" onClick={() => this.props.app.Rules.select(rule)}>{name}</div>
            {ruleBody}
        </div>;
    }
}

export default Rule;