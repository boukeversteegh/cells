import React, {Component} from 'react';
import './App.css';
import ToolBar from "./ToolBar";
import Screen from "./Screen";

class App extends Component {
    constructor(props) {
        super(props);

        let automaton = window.cells.interactive.init();
        let layer = automaton.getLayers()[0];

        this.state = {
            layer: layer,
            automaton: automaton
        }
    }

    componentDidMount() {
    }

    render() {
        return (
            <div className="App">
                {<ToolBar
                    layer={this.state.layer}
                    rules={this.state.layer.getRules()}
                    cellTypes={this.state.layer.getCellTypes()}
                    onSelectCellType={(cellType) => {
                        this.setState({selectedCellType: cellType});
                    }}
                    onAddCellType={() => {
                        this.state.layer.addCellType();
                    }}
                />}
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
        );
    }
}

export default App;
