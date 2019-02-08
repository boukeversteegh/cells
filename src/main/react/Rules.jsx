import React, {Component} from 'react';
import Events from "./Events";
import Rule from "./Rule";
import './Rules.css';

class Rules extends Component {
    constructor(props) {
        console.log("Construct Rules");
        super(props);
        this.state = {
            selectedCellType: null,
            rules: [],
        };
    }

    componentDidMount() {
        this.props.app.Rules.onChange(rules => this.setState({
            rules: rules
        }));

        this.props.app.CellTypes.onSelect(cellType => this.setState({
            selectedCellType: cellType
        }));
    }

    render() {
        let rules = this.state.rules;
        let self = this;
        let app = this.props.app;
        let Rules = app.Rules;

        return (<div id="rules">{
            rules.map(function (rule) {
                return <Rule app={app} key={rule.id} rule={rule} events={self.props.events} selectedCellType={self.state.selectedCellType}/>;
            })
        }
            {
                Rules.getTypes().map(ruleType => {
                    return <button key={ruleType.key} onClick={() => {
                        app.Rules.addRule(ruleType.new());
                        this.props.events.trigger(Events.RULES_CHANGED, this.props.layer.getRules())
                    }}>Add {ruleType.key}</button>
                })
            }

            <button onClick={() => {
                this.props.layer.addRule();
                this.props.events.trigger(Events.RULES_CHANGED, this.props.layer.getRules())
            }}>➕</button>
        </div>)
    }
}

export default Rules;