import React, {Component} from 'react';
import './CellType.css';

const cells = window.cells;

class CellType extends Component {
    constructor(props) {
        super(props);
        this.state = {
            cellType: props.cellType,
            onClick: props.onClick,
            selected: props.selected,
        }
    }

    render() {
        if (!this.props.cellType) {
            return '?'
        }
        let self = this;
        let cellBackground = this.props.cellType.getColor(0, 0);
        let cellStyle = {};
        let isAny = this.props.cellType === cells.interactive.Any;
        if (!isAny) {
            cellStyle['background'] = cellBackground
        }
        return <div className="cell"
                    data-any={isAny | 0}
                    data-dim={self.props.dim | 0}
                    style={cellStyle}
                    onClick={self.props.onClick}
                    data-selected={self.props.selected | 0}
        >{this.state.version}
        </div>
    }
}

export default CellType;