import React, {Component} from 'react';
import Events from "./Events";
import Rule from "./Rule";
import './Rules.css';

class Rules extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedCellType: null,
        };
        props.events.on(Events.CELL_TYPE_SELECTED, cellType => {
            this.setState({selectedCellType: cellType});
        })
    }

    render() {
        let rules = this.props.rules;
        let self = this;
        return (<div id="rules">{
            rules.map(function (rule, index) {
                return <Rule key={index} rule={rule} events={self.props.events} selectedCellType={self.state.selectedCellType}/>;
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