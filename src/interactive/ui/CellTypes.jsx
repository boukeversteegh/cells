'use strict';

class CellTypes extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedCellType: props.selectedCellType,
            cellTypes: props.cellTypes,
        }
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