import React, {Component} from 'react';
import CellType from "./CellType";
import './CellTypes.css';
import Events from "./Events";

import Core from "./Core"

class CellTypes extends Component {
    constructor(props) {
        super(props);
        this.state = {
            layer: null,
            cellTypes: [],
            selectedCellType: null,
        };

        props.events.on(Events.CELL_TYPE_SELECTED, cellType => {
            this.setState({selectedCellType: cellType})
        });
        props.events.on(Events.CELL_TYPES_CHANGED, cellTypes => {
            this.setState({cellTypes: cellTypes})
        });
        props.events.on(Events.LAYER_CHANGED, layer => {
            this.setState({layer: layer});
        });
    }

    render() {
        let self = this;
        let isCustom = (this.state.selectedCellType instanceof Core.CustomCellType);
        return (<div id="cell-types">
            {
                this.state.cellTypes
                    .map(function (cellType, index) {
                        return <CellType
                            key={index}
                            cellType={cellType}
                            onClick={() => {
                                self.props.events.trigger(Events.CELL_TYPE_SELECTED, cellType)
                            }}
                            selected={self.state.selectedCellType === cellType}
                        />;
                    })
            }
            <div
                className="cell"
                onClick={() => {
                    if (self.state.layer) {
                        self.state.layer.addCellType();
                        self.props.events.trigger(Events.CELL_TYPES_CHANGED, self.state.layer.getCellTypes());
                    }
                }}
            >
                +
            </div>
            {isCustom && <div
                className="cell"
                onClick={(event) => {
                    if (isCustom) {
                        let newColor = prompt("New color", self.state.selectedCellType.color);
                        if (newColor) {
                            self.state.selectedCellType.color = newColor;
                            this.forceUpdate();
                        }
                    }
                }}
            ><span role="img" aria-label="change color">🎨</span></div>}
        </div>)
    }
}

export default CellTypes