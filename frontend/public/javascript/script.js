let isGameActive = true;
let currentPlayer = 'X';
let ws;
let isMyTurn = true;
let board = [
    ['', '', ''],
    ['', '', ''],
    ['', '', '']
];

function initWebSocket() {
    ws = new WebSocket('ws://localhost:3000');

    ws.onopen = function () {
        console.log('WebSocket connection established');
    };

    ws.onmessage = function (event) {
        const data = JSON.parse(event.data);
        if (data.type === 'game_state') {
            updateGameState(data.payload);
            isMyTurn = data.payload.currentPlayer === currentPlayer;
        } else if (data.type === 'game_result') {
            handleGameResult(data.payload);
        }
    };

    ws.onerror = function (error) {
        console.error('WebSocket error:', error);
    };

    ws.onclose = function () {
        console.log('WebSocket connection closed');
    };
}

function handleClick(row, col) {
    if (isGameActive && isMyTurn) {
        const move = { type: 'move', payload: { row, col }, player: currentPlayer };
        ws.send(JSON.stringify(move));
    } else {
        console.log("It's not your turn!");
    }
}

function updateGameState(gameState) {
    currentPlayer = gameState.currentPlayer;
    isGameActive = gameState.isGameActive;
    isMyTurn = currentPlayer === 'X';

    for (let i = 0; i < 3; i++) {
        for (let j = 0; j < 3; j++) {
            document.getElementById(`cell-${i}-${j}`).innerText = gameState.board[i][j];
        }
    }

    document.getElementById('display-player').innerText = `Player ${currentPlayer}'s turn`;
}

function handleGameResult(result) {
    document.getElementById('game-result').innerText = result.message;

    document.getElementById('winsX').innerText = result.xWins;
    document.getElementById('winsO').innerText = result.oWins;
    document.getElementById('ties').innerText = result.ties;

    document.getElementById('new-game-button').style.display = 'block';
}

function startNewGame() {
    // Clear the board (reset all cells to empty)
    for (let i = 0; i < 3; i++) {
        for (let j = 0; j < 3; j++) {
            board[i][j] = '';
            document.getElementById(`cell-${i}-${j}`).innerText = '';
        }
    }

    // Reset game state
    currentPlayer = 'X';
    document.getElementById('display-player').innerText = `Player ${currentPlayer}'s turn`;
    document.getElementById('game-result').innerText = ''; // Clear result
    document.getElementById('new-game-button').style.display = 'none';
    isGameActive = true;

    // Inform the server that a new game has started
    const message = { type: 'start_new_game' };
    ws.send(JSON.stringify(message));
}


initWebSocket();
