import java.util.Scanner;

public class BattleshipFinal {

    // battleship constants
    static final int BOARD_SIZE = 5;
    static final int NUM_SHIPS = 5;
    static final char EMPTY = '-';
    static final char HIT = 'X';
    static final char MISS = 'O';
    static final char SHIP = '@';

    // main method to run the program
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // print welcome message
        System.out.println("Welcome to Battleship");
        System.out.println();

        // creater players boards
        char[][] playerOneBoard = createEmptyBoard();
        char[][] playerTwoBoard = createEmptyBoard();

        // get player 1 ship locations
        System.out.println("Player 1, enter your ships' coordinates");
        placeShips(input, playerOneBoard, 1);
        printBattleShip(playerOneBoard);
        clearScreen();
        
        // get player 2 ship loctions
        System.out.println("Player 2, enter you ships' coordinates");
        placeShips(input, playerTwoBoard, 2);
        printBattleShip(playerTwoBoard);
        clearScreen();

        // create target history boards
        char[][] playerOneTargetHistory = createEmptyBoard();
        char[][] playerTwoTargetHistory = createEmptyBoard();

        // main game loop
        int turn = 1;
        while (!gameOver(playerOneTargetHistory, playerTwoTargetHistory)) {
            if (turn == 1) {
                playerTurn(input, playerOneTargetHistory, playerTwoBoard, 1, 2);
                turn = 2;
            } else {
                playerTurn(input, playerTwoTargetHistory, playerOneBoard, 2, 1);
                turn = 1;
            }
        }

        // find winner and show final boards
        if (winner(playerOneTargetHistory)) {
            System.out.println("Player 1 wins! You sunk all of your opponent's ships.");
        } else if (winner(playerTwoTargetHistory)) {
            System.out.println("Player 2 wins! You sunk all of you opponent's ships.");
        }
        System.out.println();

        System.out.println("Final boards:");
        System.out.println();
        printBattleShip(playerOneTargetHistory);
        printBattleShip(playerTwoTargetHistory);

    }

    // create an empty board
    public static char[][] createEmptyBoard() {
        char[][] board = new char[BOARD_SIZE][BOARD_SIZE];
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                board[row][col] = EMPTY;
            }
        }
        return board;
    }

    // handle ship placement 
    public static void placeShips(Scanner input, char[][] board, int playerNum) {
        int ship = 1;
        while (ship <= NUM_SHIPS) {
            System.out.println("Enter ship " + ship + " lodation:");
            if (input.hasNextInt()) {
                int x = input.nextInt();
                if (validInt(x)) {
                    if (input.hasNextInt()) {
                        int y = input.nextInt();
                        if (validInt(y)) {
                            if (spaceAvailable(x, y, board)) {
                                board[x][y] = SHIP;
                                ship++;
                                input.nextLine();
                            } else {
                                System.out.println("You already have a ship there. Choose different coordinates.");
                                input.nextLine();
                            }
                        } else {
                            System.out.println("Invalid coordinates. Choose different coordinates.");   
                            input.nextLine();     
                        }
                    } else {
                        System.out.println("Invalid coordinates. Choose different coordinates.");   
                        input.nextLine();
                    }
                } else {
                    System.out.println("Invalid coordinates. Choose different coordinates.");   
                    input.nextLine();
                }
            } else {
                System.out.println("Invalid coordinates. Choose different coordinates.");   
                input.nextLine();
            }
        }
    }

    // handle player's turn
    public static void playerTurn(Scanner input, char[][] targetHistory, char[][] opponentBoard, int currentPlayer, int opponent) {
        boolean validShot = false;
        while (!validShot) {
            System.out.println("Payer " + currentPlayer + " enter shot row/col:");
            if (input.hasNextInt()) {
                int x = input.nextInt();
                if (validInt(x)) {
                    if (input.hasNextInt()) {
                        int y = input.nextInt();
                        if (validInt(y)) {
                            if (spotNotShot(x, y, targetHistory)) {
                                if (isHit(x, y, opponentBoard)) {
                                    System.out.println("Player " + currentPlayer + " hit Player " + opponent + "'s ship!");
                                    targetHistory[x][y] = HIT;
                                } else {
                                    System.out.println("Player " + currentPlayer + " has missed.");
                                    targetHistory[x][y] = MISS;
                                }
                                printBattleShip(targetHistory);
                                System.out.println();
                                validShot = true;
                                input.nextLine();
                            } else {
                                System.out.println("You already fired on this spot. Choose different coordinates.");
                                input.nextLine();
                            }
                        } else {
                            System.out.println("Invalid coordinates. Choose different coordinates.");
                            input.nextLine();
                        }
                    } else {
                        System.out.println("Invalid coordinates. Choose different coordinates.");
                        input.nextLine();
                    }
                } else {
                    System.out.println("Invalid coordinates. Choose different coordinates.");
                    input.nextLine();
                }
            } else {
                System.out.println("Invalid coordinates. Choose different coordinates.");
                input.nextLine();
            }
        }   
    }

    // check if game is over 
    public static boolean gameOver(char[][] playerOneTargetHistory, char[][] playerTwoTargetHistory) {
        return winner(playerOneTargetHistory) || winner(playerTwoTargetHistory);
    }

    // clear the screen
    public static void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    // check if coordinates are in bounds
    public static boolean validInt(int location) {
        return location >= 0 && location < BOARD_SIZE;
    }

    // check if spot is available 
    public static boolean spaceAvailable(int x, int y, char[][] board) {
        return board[x][y] != SHIP;
    }

    // check if spot has been shot as
    public static boolean spotNotShot(int x, int y, char[][] board) {
        return board[x][y] != HIT && board[x][y] != MISS;
    }
        
    // check if shot hit
    public static boolean isHit(int x, int y, char[][] opponentBoard){
        return opponentBoard[x][y] == SHIP;
    }

    // check if a player has won
    public static boolean winner(char[][] board) {
        int hitCount = 0;
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] == HIT) {
                    hitCount++;
                }
            }
        }
        return hitCount == NUM_SHIPS;
    }
    // Print game board to console
    public static void printBattleShip(char[][] player) {
        System.out.print("  ");
        for (int row = -1; row < BOARD_SIZE; row++) {
            if (row > -1) {
                System.out.print(row + " ");
            }
            for (int column = 0; column < BOARD_SIZE; column++) {
                if (row == -1) {
                    System.out.print(column + " ");
                } else {
                    System.out.print(player[row][column] + " ");
                }
            }
        System.out.println("");
        }
    }
}