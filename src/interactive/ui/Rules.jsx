'use strict'

class Rules extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            automaton: props.automaton,
            layer: props.automaton.getLayers()[0],
            selectedCellType: props.selectedCellType,
        }
    }

    render() {
        let rules = this.state.layer.getRules();
        let self = this;
        return (<div>{
            rules.map(function (rule, index) {
                return <Rule key={index} rule={rule} selectedCellType={self.props.selectedCellType} onClick={() => {
                    self.selectRule(rule)
                }}/>;
            })
        }
            <button onClick={() => {
                self.addRule()
            }}>➕
            </button>
            {self.state.selectedCellType.constructor.name}
        </div>)
    }

    selectRule(rule) {

    }

    addRule() {
        this.state.layer.addRule();
        this.forceUpdate();
    }
}