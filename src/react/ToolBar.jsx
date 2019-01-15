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
        };
    }

    reloadCellTypes() {
        this.setState({
            cellTypes: this.state.layer.getCellTypes(),
        });
    }

    save() {
        let layerState = this.state.mapper.mapLayer(this.state.layer);
        prompt("Copy this", JSON.stringify(layerState));
    }

    load() {
        let layerState = prompt("Paste here", "");

        if (layerState) {
            this.state.mapper.loadLayer(this.state.layer, JSON.parse(layerState));
            this.reloadCellTypes();
        }
    }

    render() {
        return (<div>
            <button onClick={() => {
                this.save()
            }}>Save
            </button>
            <button onClick={() => {
                this.load()
            }}>Load
            </button>
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
                       this.setState({
                           rules: this.state.layer.getRules()
                       });
                   }}
            />
        </div>)
    }
}

export default ToolBar