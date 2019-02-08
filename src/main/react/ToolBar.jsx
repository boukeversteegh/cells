import React, {Component} from 'react';
import CellTypes from "./CellTypes";
import Rules from "./Rules";
import Events from "./Events";

import Core from "./Core"

class ToolBar extends Component {
    constructor(props) {
        super(props);

        this.state = {
            layer: props.layer,
            cellTypes: props.cellTypes,
            mapper: new Core.JsonMapper(),
            layerState: '',
        };
    }

    componentDidMount() {
    }

    componentDidUpdate(prevProps, prevState) {
        if (prevState.layerState != this.state.layerState && this.state.layerState !== "") {
            this.layerStateInput.select();
            document.execCommand('copy');
            this.setState({layerState: ""})
        }
    }

    save() {
        let layerState = this.state.mapper.mapLayer(this.state.layer);
        this.setState({
            layerState: JSON.stringify(layerState)
        });
    }

    load() {
        let layerState = prompt("Paste here", "");

        if (layerState) {
            this.state.mapper.loadLayer(this.state.layer, JSON.parse(layerState));
            this.props.events.trigger(Events.LAYER_CHANGED, this.state.layer);
        }
    }

    clearRules() {
        this.props.app.Rules.clear();
    }

    clearScreen() {
        this.state.layer.clear();
        this.props.events.trigger(Events.LAYER_CHANGED, this.state.layer)
    }

    render() {
        return (<div id="toolbar">
            <div id="buttons">
                <button onClick={() => {
                    this.save()
                }}>Save
                </button>
                <button onClick={() => {
                    this.load()
                }}>Load
                </button>
                <button onClick={() => {
                    this.clearRules()
                }}>Clear Rules
                </button>
                <button onClick={() => {
                    this.clearScreen()
                }}>Clear Screen
                </button>
            </div>
            <input
                hidden={this.state.layerState === ""}
                value={this.state.layerState}
                readOnly
                ref={(layerStateInput) => this.layerStateInput = layerStateInput}
            />
            <CellTypes
                events={this.props.events}
                app={this.props.app}
            />
            <Rules
                layer={this.state.layer}
                events={this.props.events}
                app={this.props.app}
            />
        </div>)
    }
}

export default ToolBar