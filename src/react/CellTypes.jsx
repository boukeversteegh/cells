import React, {Component} from 'react';
import CellType from "./CellType";
import './CellTypes.css';

const cells = window.cells;

class CellTypes extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedCellType: props.selectedCellType,
        }
    }

    static getDerivedStateFromProps(props, state) {
        if (props.cellTypes.indexOf(state.selectedCellType) === -1) {
            return {
                selectedCellType: props.cellTypes[0]
            }
        }
        return null;
    }

    selectCellType(cellType) {
        this.setState({selectedCellType: cellType});
        this.props.onSelect(cellType);
    }

    render() {
        let self = this;
        let isCustom = (this.props.selectedCellType instanceof cells.interactive.CustomCellType);
        return (<div id="cell-types">
            {
                this.props.cellTypes
                    .map(function (cellType, index) {
                        return <CellType
                            key={index}
                            cellType={cellType}
                            onClick={() => {
                                self.selectCellType(cellType)
                            }}

                            selected={self.state.selectedCellType === cellType}
                        />;
                    })
            }
            <div
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
            >🎨</div>
            <div
                className="cell"
                onClick={() => {
                    this.props.onAdd();
                    this.forceUpdate();
                }}
            >
                +
            </div>
        </div>)
    }
}

export default CellTypes