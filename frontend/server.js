const express = require('express');
const http = require('http');
const WebSocket = require('ws');
const path = require('path');
const ejs = require('ejs');

const app = express();
const server = http.createServer(app);
const wss = new WebSocket.Server({ server });

app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, 'views', 'ejs'));

app.use(express.static(path.join(__dirname, 'public')));

let players = [];
let currentPlayer = 'X';
let board = [
    ['', '', ''],
    ['', '', ''],
    ['', '', '']
];
let isGameActive = true;
let xWins = 0;
let oWins = 0;
let ties = 0;

wss.on('connection', function connection(ws) {
    players.push(ws);

    if (players.length === 2) {
        players.forEach(player => player.send(JSON.stringify({ type: 'start_game' })));
    }

    ws.on('message', function incoming(message) {
        const data = JSON.parse(message);
        if (data.type === 'move' && isGameActive && data.player === currentPlayer) {
            const { row, col } = data.payload;
            if (isValidAction(row, col)) {
                board[row][col] = currentPlayer;
                currentPlayer = currentPlayer === 'X' ? 'O' : 'X'; // Switch turns
                handleGameResult();
                broadcastGameState();
            }
        } else if (data.type === 'start_new_game') {
            startNewGame(ws);
        }
    });

    ws.on('close', function close() {
        players = players.filter(player => player !== ws);
    });
});

function isValidAction(row, col){
    return board[row][col] === '';
}

function broadcastGameState() {
    const gameState = {
        type: 'game_state',
        payload: {
            board,
            currentPlayer,
            isGameActive
        }
    };
    players.forEach(player => player.send(JSON.stringify(gameState)));
}
function checkWinner() {
    // Check rows
    for (let i = 0; i < 3; i++) {
        if (board[i][0] !== '' && board[i][0] === board[i][1] && board[i][1] === board[i][2]) {
            return board[i][0];
        }
    }

    // Check columns
    for (let j = 0; j < 3; j++) {
        if (board[0][j] !== '' && board[0][j] === board[1][j] && board[1][j] === board[2][j]) {
            return board[0][j];
        }
    }

    // Check diagonals
    if (board[0][0] !== '' && board[0][0] === board[1][1] && board[1][1] === board[2][2]) {
        return board[0][0];
    }
    if (board[0][2] !== '' && board[0][2] === board[1][1] && board[1][1] === board[2][0]) {
        return board[0][2];
    }

    return null; // No winner
}

function checkDraw() {
    for (let row of board) {
        if (row.includes('')) {
            return false; // Empty cell found, game not a draw
        }
    }
    return true; // All cells filled, game is a draw
}

function handleGameResult() {
    const winner = checkWinner();
    let gameResultMessage;

    if (winner) {
        isGameActive = false;
        if (winner === 'X') {
            xWins++;
        } else {
            oWins++;
        }
        gameResultMessage = `Player ${winner} wins!`;
    } else if (checkDraw()) {
        isGameActive = false;
        ties++;
        gameResultMessage = "It's a tie!";
    } else{
        gameResultMessage = '';
    }

    broadcastGameResult(gameResultMessage);
}

function broadcastGameResult(message) {
    const gameResultMessage = {
        type: 'game_result',
        payload: {
            message: message,
            xWins: xWins,
            oWins: oWins,
            ties: ties
        }
    };

    wss.clients.forEach(client => {
        if (client.readyState === WebSocket.OPEN) {
            client.send(JSON.stringify(gameResultMessage));
        }
    });
}

function startNewGame(wsInitiating) {
    // Reset the board and game state
    board = [
        ['', '', ''],
        ['', '', ''],
        ['', '', '']
    ];
    currentPlayer = 'X';
    isGameActive = true;

    // Broadcast message to all clients except the one who initiated the new game
    wss.clients.forEach(function each(client) {
        if (client !== wsInitiating && client.readyState === WebSocket.OPEN) {
            client.send(JSON.stringify({ type: 'start_game' }));
        }
    });
}


app.get('/', function(req, res) {
    res.render('index');
});
server.listen(3000, function listening() {
    console.log('WebSocket server is running on port 3000');
});


