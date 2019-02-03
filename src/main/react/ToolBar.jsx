import React, {Component} from 'react';
import CellTypes from "./CellTypes";
import Rules from "./Rules";
import Events from "./Events";

import Core from "./Core"

class ToolBar extends Component {
    constructor(props) {
        super(props);

        let self = this;

        props.events.on(Events.RULES_CHANGED, (rules) => {
            self.setState({rules: rules})
        });

        this.state = {
            layer: props.layer,
            rules: [],
            cellTypes: props.cellTypes,
            mapper: new Core.JsonMapper(),
            layerState: '',
        };
    }

    reloadCellTypes() {
        this.setState({
            cellTypes: this.state.layer.getCellTypes(),
        });
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
        this.state.layer.rules.clear();
        this.props.events.trigger(Events.LAYER_CHANGED, this.state.layer)
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
            />
            <Rules rules={this.state.rules}
                   layer={this.state.layer}
                   events={this.props.events}
            />
        </div>)
    }
}

export default ToolBar