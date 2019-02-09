class Events {

    static CELL_TYPES_CHANGED = 6;
    static LAYER_CHANGED = 5;

    constructor() {
        this.listeners = {};
    }

    on(event, listener) {
        if (!this.listeners[event]) {
            this.listeners[event] = [];
        }
        this.listeners[event].push(listener);
    }

    trigger(event, ...args) {
        if (!this.listeners[event]) {
            return;
        }
        for (let listener of this.listeners[event]) {
            listener(...args)
        }
    }
}


export default Events