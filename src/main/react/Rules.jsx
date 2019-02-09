import React, {Component} from 'react';
import Rule from "./Rule";
import './Rules.css';

class Rules extends Component {
    constructor(props) {
        console.log("Construct Rules", props);
        super(props);
        this.state = {
            selectedCellType: null,
            rules: [],
        };
    }

    componentDidMount() {
        this.props.app.Rules.changes.observe(rules => this.setState({
            rules: rules
        }));

        this.props.app.CellTypes.selected.observe(cellType => this.setState({
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

        return <div id="rules">
            {
            rules.map(function (rule) {
                return <Rule
                    key={rule.id} // react key
                    app={app}
                    rule={rule}
                    selectedCellType={self.state.selectedCellType}
                />;
            })
        }

        </div>
    }
}

export default Rules;