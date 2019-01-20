import React, {Component} from 'react';
import CellTypes from "./CellTypes";
import Rules from "./Rules";

class ToolBar extends Component {
    constructor(props) {
        super(props);
        this.state = {
            layer: props.layer,
            rules: props.rules,
            cellTypes: props.cellTypes,
            selectedCellType: props.cellTypes[0],
            mapper: new window.cells.interactive.JsonMapper(),
            layerState: '',
        };
    }

    reloadCellTypes() {
        this.setState({
            cellTypes: this.state.layer.getCellTypes(),
        });
    }

    reloadRules() {
        this.setState({
            rules: this.state.layer.getRules()
        });
    }

    save() {
        let layerState = this.state.mapper.mapLayer(this.state.layer);
        this.setState({
            layerState: JSON.stringify(layerState)
        });
    }

    componentDidUpdate(prevProps, prevState) {
        if (prevState.layerState != this.state.layerState) {
            this.layerStateInput.select();
            document.execCommand('copy');
        }
    }

    load() {
        let layerState = prompt("Paste here", "");

        if (layerState) {
            this.state.mapper.loadLayer(this.state.layer, JSON.parse(layerState));
            this.reloadCellTypes();
            this.reloadRules();
            this.forceUpdate();
        }
    }

    clear() {
        this.state.layer.clear();
        this.state.layer.rules.clear();
        this.reloadRules()
    }

    render() {
        return (<div id="toolbar">
            <button onClick={() => {
                this.save()
            }}>Save
            </button>
            <button onClick={() => {
                this.load()
            }}>Load
            </button>
            <button onClick={() => {
                this.clear()
            }}>Clear
            </button>
            <input value={this.state.layerState} readOnly
                   ref={(layerStateInput) => this.layerStateInput = layerStateInput}/>
            <CellTypes
                cellTypes={this.state.cellTypes}
                selectedCellType={this.state.selectedCellType}
                onSelect={(cellType) => {
                    this.setState({selectedCellType: cellType});
                    this.props.onSelectCellType(cellType);
                }}
                onAdd={() => {
                    this.props.onAddCellType();
                    this.reloadCellTypes();

                }}
            />
            <Rules rules={this.state.rules}
                   selectedCellType={this.state.selectedCellType}
                   onAddRule={() => {
                       this.state.layer.addRule();
                       this.reloadRules();
                   }}
            />
        </div>)
    }
}

export default ToolBar