import React, {Component} from 'react';
import './App.css';
import ToolBar from "./ToolBar";
import Screen from "./Screen";
import RuleDetails from "./RuleDetails";
import Events from "./Events";

class App extends Component {
    constructor(props) {
        super(props);

        let automaton = window.cells.interactive.init();
        let layer = automaton.getLayers()[0];

        let events = new Events();
        this.state = {
            layer: layer,
            automaton: automaton,
            events: events,
            selectedRule: null,
        };

        // Reducers
        events.on(Events.RULE_SELECTED, (rule) => {
            console.log("RuleDetails", "Some rule was selected", rule);
            this.setState({selectedRule: rule});
        });
        events.on(Events.RULE_DELETED, (rule) => {
            layer.deleteRule(rule);
            events.trigger(Events.RULES_CHANGED, layer.getRules());
            if (this.state.selectedRule === rule) {
                events.trigger(Events.RULE_SELECTED, null);
            }
        });
        events.on(Events.CELL_TYPE_SELECTED, cellType => {
            this.setState({selectedCellType: cellType});
        });

        events.on(Events.CELL_TYPES_CHANGED, cellTypes => {
            this.state.events.trigger(Events.CELL_TYPE_SELECTED, cellTypes[0]);
        })

        events.on(Events.LAYER_CHANGED, layer => {
            this.state.events.trigger(Events.CELL_TYPES_CHANGED, layer.getCellTypes());
            this.state.events.trigger(Events.RULES_CHANGED, layer.getRules());
        });
    }

    componentDidMount() {
        this.state.events.trigger(Events.LAYER_CHANGED, this.state.layer);
    }

    render() {
        return (
            <div className="App">
                <ToolBar
                    events={this.state.events}
                    layer={this.state.layer}
                    cellTypes={this.state.layer.getCellTypes()}
                    selectedCellType={this.state.selectedCellType}
                    onAddCellType={() => {
                        this.state.layer.addCellType();
                    }}
                    onSelectRule={(rule) => {
                        this.setState({selectedRule: rule});
                    }}
                />
                <div style={{
                    flexDirection: "row",
                    display: "flex",
                    clear: "both",
                    minHeight: "1000px",
                    alignItems: "flex-start"
                }}>
                    <RuleDetails
                        events={this.state.events}
                    />
                    <Screen
                        width={this.state.automaton.w}
                        height={this.state.automaton.h}
                        layer={this.state.layer}
                        automaton={this.state.automaton}
                        onPaint={(x, y) => {
                            this.state.layer.set(x, y, this.state.selectedCellType)
                        }}
                    />
                </div>
            </div>
        );
    }
}

export default App;
