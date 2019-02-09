import React, {Component} from 'react';
import Rule from "./Rule";
import './Rules.css';

class Rules extends Component {
    constructor(props) {
        console.log("Construct Rules", props);
        super(props);
        this.state = {
            selectedCellType: null,
            selectedType: props.app.Rules.getTypes()[0].key,
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

    componentWillUnmount() {
        // @todo remove event listeners
    }

    render() {
        let rules = this.state.rules;
        let self = this;
        let app = this.props.app;
        let Rules = app.Rules;

        return <div id="rules">
            {
            rules.map(function (rule) {
                return <Rule app={app} key={rule.id} rule={rule} events={self.props.events}
                             selectedCellType={self.state.selectedCellType}/>;
            })
        }

        </div>
    }
}

export default Rules;