'use strict'

class Rules extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            rules: props.rules,
            selectedCellType: props.selectedCellType,
        }
    }

    render() {
        let rules = this.props.rules;
        let self = this;
        return (<div id="rules">{
            rules.map(function (rule, index) {
                return <Rule key={index} rule={rule} selectedCellType={self.props.selectedCellType} onClick={() => {
                    self.selectRule(rule)
                }}/>;
            })
        }
            <button onClick={this.props.onAddRule}>➕</button>
        </div>)
    }

    selectRule(rule) {

    }

    // addRule() {
    // this.state.layer.addRule();
    // this.forceUpdate();
    // }
}