'use strict';

const e = React.createElement;

class Layers extends React.Component {
    constructor(props) {
        super(props);
        this.layers = props.layers
    }

    render() {
        return e(
            'div', {}, this.layers.map(function (layer) {
                return e('li', {key: layer}, layer);
            })
        )
    }

    load(layers) {
        this.layers = layers;
    }
}