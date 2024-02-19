let currentPlayer = 'X';
const board = [
    ['', '', ''],
    ['', '', ''],
    ['', '', '']
];

function handleClick(row, col) {
    if (board[row][col] === '') {
        board[row][col] = currentPlayer;
        document.getElementById('board').children[row * 3 + col].innerText = currentPlayer;
        currentPlayer = currentPlayer === 'X' ? 'O' : 'X';
    }
}
