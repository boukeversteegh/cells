const canvas = document.getElementById('display');
canvas.width = cells.w;
canvas.height = cells.h;
const ctx = canvas.getContext('2d');

var t = 0;
document.getElementById('reset').onclick = cells.reset
document.getElementById('iterate').onclick = function () {
    iterate(t++);
    render();
};
document.getElementById('stop').onclick = stop;
document.getElementById('start').onclick = start;
document.getElementById('random').onclick = generateRandomWorld;

const world = cells.world;

const w = cells.w;
const h = cells.h;


function generateRandomWorld() {
    cells.clearWorld();
    speckleWorld();
    // bucketWorld()
}

function speckleWorld() {
    for (let y = 2; y < h; y++) {
        for (let x = 0; x < w; x++) {
            if (Math.random() < 0.1) {
                set(x-1, y, cells.Cell.DIRT);
                set(x, y, cells.Cell.DIRT);
                set(x+1, y, cells.Cell.DIRT);
                set(x, y-1, cells.Cell.NONE);
            }
            if ((Math.random()) < Math.pow(y / h, 2)) {
                set(x + 1, y, cells.Cell.DIRT);
            }
        }
    }
    world[0][w / 2] = cells.Cell.WATER_SOURCE;
}

function bucketWorld() {
    // add random dirt
    for (let i = 0; i < (w * h) / (15 * 10); i++) {
        const y = 10 + Math.trunc(Math.random() * h);
        const x = Math.trunc(Math.random() * w);
        const l = Math.trunc(3 + Math.random() * (w / 20));
        for (let dx = x; dx <= x + l; dx++) {
            set(dx, y, cells.Cell.DIRT);
        }

        // walls
        set(x, y - 1, cells.Cell.DIRT);
        if (Math.random() > 0.5)
            set(x, y - 2, cells.Cell.DIRT);
        set(x + l, y - 1, cells.Cell.DIRT);
        if (Math.random() > 0.5)
            set(x + l, y - 2, cells.Cell.DIRT);
    }
    world[0][w / 2] = cells.Cell.WATER_SOURCE;
}

function set(x, y, c) {
    cells.set(x, y, c)
}

var changes = [];

function render() {
    if (changes.length) {
        for (let i = 0; i < changes.length; i++) {
            [x, y] = changes[i];
            renderCell(x, y);
        }
    } else {
        for (let y = 0; y < h; y++) {
            for (let x = 0; x < w; x++) {
                renderCell(x, y);
            }
        }
    }
}

function renderCell(x, y) {
    const c = world[y][x];
    ctx.fillStyle = cells.getColor(c);
    ctx.fillRect(x, y, 1, 1);
}

function iterate(t) {
    changes = []

    let iteration_changes = cells.iterate(world, w, h);

    if (!iteration_changes.length) {
        stop()
    }

    for (let change of iteration_changes) {
        changes.push([change.first, change.second]);
    }
}

let world1 = [
    "                  +              ",
    "                                 ",
    "                                 ",
    "                                 ",
    "               ###########       ",
    "                  #              ",
    "            #######                ",
    "   #                 #    #####    ",
    "   #                 #               ",
    "   ###########       #  # #          ",
    "   ###########       #  # #          ",
    "   ###################  ###          ",
    "                                     ",
    "                                     ",
    "                     #        #      ",
    "                     ##########      ",
    "                                     ",
    "                                     ",
    "                             #####    ",
    "                                     ",
    "                                               # ",
    "                          #        #           # ",
    "                          ##########           #",
    "                 ######            #           #",
    "                      ######       #########   #",
    "                                #              #",
    "                                ################",
];

// generateRandomWorld();
cells.day17.loadWorld(world1);
render();
start();

function start() {
    if (!window.running) {
        window.running = setInterval(function () {
            iterate(t++);
            render();
        }, 10);
    }
}

function stop() {
    window.clearInterval(window.running);
    window.running = false;
}
