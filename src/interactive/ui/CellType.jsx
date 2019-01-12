'use strict'

class CellType extends React.Component {
    constructor(props) {
        super(props);
        this.state = {cellType: props.cellType, onClick: props.onClick}
    }

    render() {
        let self = this;
        let cellBackground = this.props.cellType.getColor(0, 0);
        let cellStyle = {};
        let isAny = this.props.cellType === cells.interactive.Any;
        if (!isAny) {
            cellStyle['background'] = cellBackground
        }
        return <div className="cell"
                    data-any={isAny | 0}
                    style={cellStyle}
                    onClick={self.props.onClick}
        >{this.state.version}
        </div>
    }
}