import React, {Component} from 'react';
import CellType from "./CellType";
import './CellTypes.css';
import Events from "./Events";

import Core from "./Core"

import {SketchPicker} from 'react-color';
import reactCSS from 'reactcss'

class CellTypes extends Component {
    constructor(props) {
        super(props);
        this.state = {
            layer: null,
            cellTypes: [],
            selectedCellType: null,
            showColorPicker: false,
        };

        // props.events.on(Events.CELL_TYPES_CHANGED, cellTypes => {
        //     this.setState({
        //         cellTypes: cellTypes,
        //         showColorPicker: false,
        //     })
        // });

        props.events.on(Events.LAYER_CHANGED, layer => {
            this.setState({layer: layer});
        });
    }

    componentDidMount() {
        this.props.app.CellTypes.selected.observe(cellType => this.setState({
            selectedCellType: cellType
        }));

        this.props.app.CellTypes.changes.observe(cellTypes => this.setState({
            cellTypes: cellTypes,
            showColorPicker: false,
        }))
    }

    onClick(cellType) {
        this.setState({
            showColorPicker:
                this.state.selectedCellType instanceof Core.CustomCellType
                && (this.state.selectedCellType === cellType)
                && (!this.state.showColorPicker)
        });
        this.props.app.CellTypes.select(cellType);
    }


    handleClose = () => {
        this.setState({showColorPicker: false})
    };


    handleColorChange(color) {
        this.state.selectedCellType.color = color.hex;
        this.forceUpdate();
    }

    render() {
        let self = this;

        const styles = reactCSS({
            'default': {
                color: {
                    width: '36px',
                    height: '14px',
                    borderRadius: '2px',
                    background: `${this.state.selectedCellType ? this.state.selectedCellType.color : '#FFFF00'}`,
                },
                swatch: {
                    padding: '5px',
                    background: '#fff',
                    borderRadius: '1px',
                    boxShadow: '0 0 0 1px rgba(0,0,0,.1)',
                    display: 'inline-block',
                    cursor: 'pointer',
                },
                popover: {
                    position: 'absolute',
                    zIndex: '2',
                    top: '32px',
                },
                cover: {
                    position: 'fixed',
                    top: '0px',
                    right: '0px',
                    bottom: '0px',
                    left: '0px',
                },
            },
        });

        return (<div id="cell-types">
            {this.state.showColorPicker && <div style={styles.cover} onClick={() => {
                self.handleClose()
            }}/>}
            {
                this.state.cellTypes
                    .map(function (cellType, index) {
                        return <div key={index} className="cell-container">
                            <CellType

                                cellType={cellType}
                                onClick={() => self.onClick(cellType)}
                                selected={self.state.selectedCellType === cellType}
                            />
                            {self.state.showColorPicker && self.state.selectedCellType === cellType ?
                                <div style={styles.popover}>
                                    <SketchPicker color={self.state.selectedCellType.color} onChange={(color) => {
                                        self.handleColorChange(color)
                                    }}/>
                                </div> : null}
                        </div>
                    })
            }
            <div
                className="cell"
                id="cell-add-button"
                onClick={() => self.props.app.CellTypes.add()}
            >
                +
            </div>
            {/*{isCustom && this.state.showColorPicker && <span style={{position: "absolute", zIndex: 2}}>*/}
            {/*<SketchPicker color={self.state.selectedCellType.color} onChange={(color) => {*/}
            {/*self.state.selectedCellType.color = color.hex;*/}
            {/*this.forceUpdate();*/}
            {/*}}/>*/}
            {/*</span>}*/}




        </div>)
    }
}

export default CellTypes