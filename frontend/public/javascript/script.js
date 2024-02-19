let currentPlayer = 'X';
let board = [
    ['', '', ''],
    ['', '', ''],
    ['', '', '']
];
let isGameActive = true;

function isValidAction(row, col){
    return !(board[row][col].innerText === 'X' || board[row][col].innerText === 'O');
}

function handleClick(row, col) {
    if (isValidAction(row, col) && isGameActive && board[row][col] === '') {
        board[row][col] = currentPlayer;
        document.getElementById('board').children[row * 3 + col].innerText = currentPlayer;
        currentPlayer = currentPlayer === 'X' ? 'O' : 'X';
        document.getElementById('display-player').innerText = `Player ${currentPlayer}'s turn`;
        handleGameResult();
    }
}


// Assuming you have a function to check for a winner
function checkWinner() {
    // Check rows
    for (let i = 0; i < 3; i++) {
        if (board[i][0] !== '' && board[i][0] === board[i][1] && board[i][1] === board[i][2]) {
            return board[i][0]; // Winner found
        }
    }

    // Check columns
    for (let j = 0; j < 3; j++) {
        if (board[0][j] !== '' && board[0][j] === board[1][j] && board[1][j] === board[2][j]) {
            return board[0][j]; // Winner found
        }
    }

    // Check diagonals
    if (board[0][0] !== '' && board[0][0] === board[1][1] && board[1][1] === board[2][2]) {
        return board[0][0]; // Winner found
    }
    if (board[0][2] !== '' && board[0][2] === board[1][1] && board[1][1] === board[2][0]) {
        return board[0][2]; // Winner found
    }

    return null; // No winner
}

// Assuming you have a function to check for a draw
function checkDraw() {
    for (let row of board) {
        if (row.includes('')) {
            return false; // Empty cell found, game not a draw
        }
    }
    return true; // All cells filled, game is a draw
}

// Example usage (triggered on user input):
function handleGameResult() {
    const winner = checkWinner();
    if (winner) {
        console.log(`Player ${winner} wins!`);
        isGameActive = false;
    } else if (checkDraw()) {
        console.log("It's a draw!");
        isGameActive = false;
    } else {
        console.log("Game still ongoing.");
    }
}