class Events {
    static RULES_CHANGED = 0;
    static RULE_SELECTED = 1;
    static CELL_TYPE_SELECTED = 4;
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