'use strict';

class ToolBar extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            layer: props.layer,
            rules: props.rules,
            cellTypes: props.cellTypes,
            selectedCellType: props.cellTypes[0],
        }
    }

    render() {
        return (<div>
            <CellTypes
                cellTypes={this.state.cellTypes}
                selectedCellType={this.state.selectedCellType}
                onSelect={(cellType) => {
                    this.setState({selectedCellType: cellType});
                    this.props.onSelectCellType(cellType);
                }}
                onAdd={() => {
                    this.props.onAddCellType();
                    this.setState({
                        cellTypes: this.state.layer.getCellTypes(),
                    });

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