import React, {Component} from 'react';
import './App.css';
import ToolBar from "./ToolBar";
import Screen from "./Screen";
import RuleDetails from "./RuleDetails";
import Events from "./Events";

import Core from "./Core"

class App extends Component {
    constructor(props) {
        super(props);

        let app = Core.init();
        let automaton = app.automaton;
        let layer = automaton.getLayers()[0];

        let events = new Events();
        this.state = {
            layer: layer,
            automaton: automaton,
            app: app,
            events: events,
            selectedRule: null,
            rulePaint: false,
        };

        // Reducers
        events.on(Events.RULE_SELECTED, (rule) => {
            console.log("RuleDetails", "Some rule was selected", rule);
            this.setState({selectedRule: rule});
        });

        events.on(Events.CELL_TYPES_CHANGED, cellTypes => {
            this.state.app.CellTypes.select(cellTypes[0]);
        });

        events.on(Events.LAYER_CHANGED, layer => {
            this.state.events.trigger(Events.CELL_TYPES_CHANGED, layer.getCellTypes());
            this.state.events.trigger(Events.RULES_CHANGED, layer.getRules());
        });
    }

    componentDidMount() {
        this.state.events.trigger(Events.LAYER_CHANGED, this.state.layer);

        this.state.app.CellTypes.selected.observe(cellType => this.setState({
            selectedCellType: cellType
        }))
    }

    render() {
        return (
            <div className="App">
                <label>Magic rule paint <input type="checkbox" onChange={() => {
                    this.setState({rulePaint: !this.state.rulePaint})
                }} value={this.state.rulePaint}/></label>
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
                    // ruleTypes={this.state.app.getRuleTypes()}
                    app={this.state.app}

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
                        app={this.state.app}
                    />
                    <Screen
                        width={this.state.automaton.w}
                        height={this.state.automaton.h}
                        layer={this.state.layer}
                        automaton={this.state.automaton}
                        onPaint={(x, y) => {
                            if (!this.state.rulePaint) {
                                this.state.layer.set(x, y, this.state.selectedCellType);
                            } else {
                                let rule = this.state.app.Rules.newPatternRule();

                                rule.setInput(-1, -1, this.state.layer.get(x - 1, y - 1));
                                rule.setInput(0, -1, this.state.layer.get(x, y - 1));
                                rule.setInput(1, -1, this.state.layer.get(x + 1, y - 1));
                                rule.setInput(-1, 0, this.state.layer.get(x - 1, y));
                                rule.setInput(0, 0, this.state.layer.get(x, y));
                                rule.setInput(1, 0, this.state.layer.get(x + 1, y));
                                rule.setInput(-1, 1, this.state.layer.get(x - 1, y + 1));
                                rule.setInput(0, 1, this.state.layer.get(x, y + 1));
                                rule.setInput(1, 1, this.state.layer.get(x + 1, y + 1));

                                rule.setOutput(0, 0, this.state.selectedCellType);

                                this.state.app.Rules.addRule(rule);
                            }
                        }}
                    />
                </div>
            </div>
        );
    }
}

export default App;
