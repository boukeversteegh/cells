import React, {Component} from 'react';
import './AddRule.css';

class AddRule extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedType: props.app.Rules.getTypes()[0].key,
        };
    }

    componentDidMount() {
    }

    render() {
        let app = this.props.app;
        let Rules = app.Rules;

        return <div id="add-rule">
            <select size={Rules.getTypes().length}
                    onChange={(event) => this.setState({selectedType: event.target.value})}>{
                Rules.getTypes().map(ruleType => {
                    return <option key={ruleType.key} value={ruleType.key} onClick={() => {
                    }}>{ruleType.key}</option>
                })
            }
            </select>
            <button onClick={() => {
                return app.Rules.addByKey(this.state.selectedType)
            }}>➕
            </button>
        </div>
    }
}

export default AddRule;