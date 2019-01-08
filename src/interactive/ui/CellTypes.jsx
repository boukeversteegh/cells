'use strict';

const e = React.createElement;

class CellTypes extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            ui: props.ui,
            automaton: props.automaton,
            selectedCellType: props.automaton.getLayers()[0].getCellTypes()[0],
        }
    }

    selectCellType(cellType) {
        this.setState({selectedCellType: cellType});
        this.state.ui.selectedCellType = cellType;
    }

    render() {
        let self = this;
        return (<div>
            {
                this.state.automaton.getLayers()[0].getCellTypes()
                    .map(function (cellType, index) {
                        return <span
                            key={index}
                            className="cell-type"
                            data-selected={(self.state.selectedCellType == cellType) ? '1' : '0'}
                            style={{backgroundColor: cellType.getColor(0, 0)}}
                            onClick={() => {
                                self.selectCellType(cellType)
                            }}
                        />
                    })
            }
        </div>)
    }
}