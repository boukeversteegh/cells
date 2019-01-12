'use strict';

const e = React.createElement;

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
        return (<div id="cell-types">
            {
                this.state.cellTypes
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
        </div>)
    }
}