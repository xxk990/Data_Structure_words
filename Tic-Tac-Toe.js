var readline = require('readline');
var fs = require('fs');

gameBoard = {}

SYMBOL = ['X', 'O', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 
           'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'Y', 'Z'];
	
var rl = readline.createInterface({
 	input: process.stdin,
 	output: process.stdout
});

// Ask if the player would like to resume a saved game.
//  If yes, load game.
//  if no, creat a new game with a new save file.
function askSaveGame () {
	rl.question('Would you like to resume a saved game? (Y/N) Press Y to resume a saved game; Press N to start a new game.', function(answer) {
		switch(answer.trim()) {
			case 'Y':
				resumeSavedGame();
				break;
			case 'N':
				NewGame();
				break;	
			case 'y':
				resumeSavedGame();
				break;
			case 'n':
				NewGame();
				break;	

			default:
				askSaveGame();
		}	
	});
}
askSaveGame();

// When user wants to resume a saved game
function resumeSavedGame() {
	rl.question('\nPlease enter the filename you saved the game before: ', function(filename) {
		try{
  			fs.readFileSync(filename, 'utf8');
  		}
		catch(err){
			console.error('\nError: ENOENT: no such file, open\'' + filename + '\'!! Please try again!!\n');
			process.exit(0);
 		}
		var obj = JSON.parse(fs.readFileSync(filename, 'utf8'));
		for (var property in obj) {
			gameBoard[property] = obj[property];
		}
		gameBoard.currentPlayer = (obj.currentPlayer - 1) % obj.numOfPlayers;
		if (gameBoard.currentPlayer <= 0) gameBoard.currentPlayer = gameBoard.numOfPlayers - 1;
		console.log('The board is as following now:');
		gameBoard.printBoard();
		console.log('Player ' + gameBoard.currentPlayer + ' turn');
		play();
	})
}

//When the new game begins
//firstly, the player need to enter the number of player in this game, maximum is 26.
//secondly, the player need to enter the size of the board., maximun is 999.
//thirdly, the player neeed to enter win sequence count.
function NewGame() {
	var numOfPlayers = -1;
	var boardSize = -1;
	var winSequen = -1;
	rl.question('\n How many players are playing in this game?(maximun: 26) ', function(numOfPlayers) {
		numOfPlayers = parseInt(numOfPlayers);
		if ( numOfPlayers <= 0 || numOfPlayers > 26 || typeof(numOfPlayers) != typeof(1) || !numOfPlayers) {
			console.error("\nError: The maximum number of players is 26, and there should be at least 1 player.");
			NewGame();
		}
		rl.question('How large the board should be?(maximun: 999) ', function(boardSize) {
			boardSize = parseInt(boardSize);
			if (!boardSize || boardSize <= 0 || boardSize > 999 || typeof(boardSize) != typeof(1)) {
				console.error("\nError: The maximum size of board is 999, and there should be at least 1X1 board");
				NewGame();
			}
			rl.question('Please enter the win sequence count.(Win sequence should less then the size of the board) ', function(winSequen) {
				winSequen = parseInt(winSequen);
				if (!winSequen || winSequen <= 0 || typeof(winSequen) != typeof(1)){
					console.error("\nError:The win sequence should be at least 1");
					NewGame();
				}
				

				if (boardSize < winSequen) {
					console.error("\nError:The win sequence should be at least 1 and less than the size of the board.");
					NewGame();
				}
				
				gameBoard.numOfPlayers = numOfPlayers;
				gameBoard.boardSize = boardSize;
				gameBoard.winSequen = winSequen;
				gameBoard.currentPlayer = 0;
				gameBoard.marks = [];
				
				// Player 0 begin to play
				play();
			})
		})
	})
}

// To check the player's position
// If the position is marked, return the player id of the mark.
// If the position is not marked, return -1.
gameBoard.getPlayerMark = function getPlayerMark(row, col){
	for (var i = 0; i < this.marks.length; i++) {
		mark = this.marks[i];
		if (mark.row === row && mark.collumn === col) {
			return mark.player;
		}
	};	
	return -1;
}

// Print the board
gameBoard.printBoard = function getPlayerMark() {
	
	for (var i = 0; i < this.boardSize; i++) {

		if (i === 0) {
			//print collumns
			for (var j = 0; j < this.boardSize; j++) {
				if (j === 0) {
					process.stdout.write('   ');
				} else{
					process.stdout.write(' ');
				};
				// Print the number of collumn
				col = j + 1;
				process.stdout.write(col + '');
				if (col < 10) { 
					process.stdout.write('  '); 
				} else if (col < 100){
					process.stdout.write(' '); 
				};
			};
			process.stdout.write('\n');

		} else{ 
			for (var j = 0; j < this.boardSize; j++) {
				if (j === 0) {
					process.stdout.write('   ');
				} else {
					process.stdout.write('+');
				}
				process.stdout.write('---');
			};			
			process.stdout.write('\n');
		};


		// the rest of the lines
		for (var j = 0; j < this.boardSize; j++) {
			row = i + 1;
			col = j + 1;
			if (j === 0) {
				process.stdout.write(row + '');
				if (row < 10) { 
					process.stdout.write('  '); 
				} else if (row < 100){
					process.stdout.write(' '); 
				};
			} else{
				process.stdout.write('|');
			};
			var player = this.getPlayerMark(row, col);
			if (player === -1) {
				process.stdout.write('   ');
			} else{
				process.stdout.write(' ' + SYMBOL[player] + ' ');
			};
		};
		process.stdout.write('\n');
	};
}
//check the player's position is not out of board
function chekSize (row, col) {
	if (row > gameBoard.boardSize || col > gameBoard.boardSize)
		return true;
return false;
}

//check the position is already exist a mark.
function chekExist (row, col) {
	for (var k_mark = 0; k_mark <= (gameBoard.marks.length-1); k_mark++){
		if (row === gameBoard.marks[k_mark].row && col === gameBoard.marks[k_mark].collumn)
			return true;
	}
	return false;
}

//to check the player is win or not
function winCheck (player) {

	for (var i = 0; i < gameBoard.boardSize; i++) {
		for (var j = 0; j < gameBoard.boardSize; j++) {
			row = i + 1;
			col = j + 1;
			if (gameBoard.getPlayerMark(row, col) === player) {
				// winning check in horizontal
				for (var t = 1; t < gameBoard.winSequen; t++) {
					if (gameBoard.getPlayerMark(row, col + t) != player) {
						break;
					};
					if (t === gameBoard.winSequen - 1)
						return true;
				};
				// winning check in vertical
				for (var t = 1; t < gameBoard.winSequen; t++) {
					if (gameBoard.getPlayerMark(row + t, col) != player) {
						break;
					};
					if (t === gameBoard.winSequen - 1)
						return true;
				};
				// winning in diagonal
				for (var t = 1; t < gameBoard.winSequen; t++) {
					if (gameBoard.getPlayerMark(row + t, col + t) != player) {
						break;
					};
					if (t === gameBoard.winSequen - 1)
						return true;
				};
				return false;
			};	
		};
	};
}

//to check the game is tie or not
function tieCheck () {
	if (gameBoard.marks.length === gameBoard.boardSize * gameBoard.boardSize)
		return true;
	else 
		return false;
}



function play () {
	var player = gameBoard.currentPlayer;
	rl.question('\n\n Enter row and collumn number or press Q to quit: ', function(inputT) {
		
		if ((inputT.split(' ')[0] == 'Q') || (inputT.split(' ')[0] == 'q')) {
			rl.question('\n Enter the name of the game you want to save: ', function(filename) {
				gameBoard.marks.pop();
				gameBoard.currentPlayer = player + 1;
				filenameS = filename + '.sav';
				var str = JSON.stringify(gameBoard);
				
				try{
					fs.readFileSync(filenameS, 'utf8')
				}
  				catch(e){
  						fs.writeFileSync(filenameS, str, 'utf8');
						rl.close();
						console.log('\nThe game has been saved into file \'' + filenameS +'\'!\n');
						process.exit(0);
  				}
				console.error('\nError: file\'' + filename + '\'is already there!');
  				
  				filenameS = filename;
  				filenameS += '-1.sav';
				fs.writeFileSync(filenameS, str, 'utf8');
				rl.close();
				console.log('\nThe game has been saved in \'' + filenameS +'\'!\n');
				process.exit(0);
			})
		}
	
		// Otherwise, process the row and collumn number
		var row = parseInt(inputT.split(' ')[0]);
		var col = parseInt(inputT.split(' ')[1]);
		mark = {
			'player': player, 'row': row, 'collumn': col
		};
	
		if (!isNaN(row) && isNaN(col)){
			console.error('\n Error: the number of row and collum should separet by sapce like: \'5 7\' ');
			process.exit(0);
		}
	
		
		

		if (chekSize(row, col)){
			console.error('\nError: You can not put there, it is out of space. \n\n');
			
			process.exit(0);
		};

		if (chekExist(row, col) === true){
			console.error('\nError: You can not put there, there is alreay a mark!  \n\n');
			
			process.exit(0);
		};

		gameBoard.marks.push(mark);

		if (winCheck(player) === true) {
			console.log('\nCongratuations! player ' + player + 'win the game!!!\n')
			process.exit(0);
		};
		
		if (tieCheck() === true) {
			console.log('\nAll the spots are marked. Tie\n')
			process.exit(0);
		};

		// turn to next player
		if (player === gameBoard.numOfPlayers - 1) {
			player = -1;
		};

		console.log('\nThe board is :');
		gameBoard.printBoard();
		console.log('it\'s turn for player ' + (player+1));
		gameBoard.currentPlayer = player + 1;
		play();
	})
}
