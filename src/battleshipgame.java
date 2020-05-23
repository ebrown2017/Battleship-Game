import java.util.Scanner;
import java.util.Random;

public class battleshipgame {
    static char[][] grid = new char[10][10];
    static char[][] activeBoard = new char[10][10];
    static char response = 'y';
    static int numShips = 5;
    static int userShipsAlive = numShips;
    static int enemyShipsAlive = numShips;

    public static void main(String args[]) {

        System.out.println("***** Welcome to the Battle Ships game *****");
        System.out.println("Right now, the sea is empty");

        while (response == 'y'){
            clearGrid(grid);
            clearGrid(activeBoard);
            printGrid(grid);
            getUserShips(grid, numShips);
            printGrid(grid);
            addEnemyShips(activeBoard, numShips);
            activeGame(activeBoard, grid);
        }
    }


    private static void printGrid(char[][] gameGrid) {
        System.out.print("     ");
        for (int i = 0; i < 10; i++) {
            if (i == 9) {
                System.out.println(i);
            } else {
                System.out.print(i + ", ");
            }
        }

        System.out.print("    ");
        for (int j = 0; j < 10; j++) {
            if (j == 9) {
                System.out.println("___");
            } else {
                System.out.print("___");
            }
        }

        for (int row = 0; row < gameGrid.length; row++) {
            System.out.print(row + "  | ");
            for (int col = 0; col < gameGrid[row].length; col++) {
                if (col == gameGrid[row].length - 1){
                    System.out.println(gameGrid[row][col] + " |");
                    break;
                } else {
                    System.out.print(gameGrid[row][col] + ", ");
                }
            }
        }

        System.out.print("    ");
        for (int j = 0; j < 10; j++) {
            if (j == 9) {
                System.out.println("___");
            } else {
                System.out.print("___");
            }
        }

        System.out.println("");
    }

    private static char[][] clearGrid(char[][] gameGrid) {
        for (int row = 0; row < gameGrid.length; row++){
            for (int col = 0; col < gameGrid[row].length; col++){
                gameGrid[row][col] = ' ';
            }
        }
        return gameGrid;
    }

    private static void getUserShips(char[][] gameGrid, int shipCount) {
        Scanner scnr = new Scanner(System.in);
        String input;
        for (int i = 0; i < shipCount; i++) {
            if(shipCount == 1) {
                System.out.print("Please Enter x,y coordinates for ship: ");
            } else {
                System.out.print("Please Enter x,y coordinates for ship " + (i + 1) + ": ");
            }
            input = scnr.next();
            int xCord = Character.getNumericValue(input.charAt(0));
            int yCord = Character.getNumericValue(input.charAt(2));
            try {
                addUserShips(gameGrid, xCord, yCord);
            } catch(IndexOutOfBoundsException e) {
                System.out.println("Error: Ship coordinates larger than grid, pick another position");
                getUserShips(gameGrid, 1);
            } catch(Exception e) {
                System.out.println("Error: Something went wrong, pick another position");
                getUserShips(gameGrid, 1);
            }
        }
    }

    private static void addUserShips(char[][] gameGrid, int xCord, int yCord) {
        if (gameGrid[yCord][xCord] == '@') {
            System.out.println("Error: Ship already at coordinates, pick another position");
            getUserShips(gameGrid, 1);
        } else {
            gameGrid[yCord][xCord] = '@';
            activeBoard[yCord][xCord] = '@';
        }
    }

    private static void addEnemyShips(char[][] gameGrid, int shipCount) {
        Random random = new Random();
        int randXCord;
        int randYCord;

        System.out.println("Computer is deploying ships:");

        for (int i = 0; i < shipCount; i++) {
            randXCord = random.nextInt((9 - 0) + 1);
            randYCord = random.nextInt((9 - 0) + 1);

            if(activeBoard[randYCord][randXCord] == '@' || activeBoard[randYCord][randXCord] == '?'){
                i--;
            } else {
                activeBoard[randYCord][randXCord] = '?';
                System.out.println("Enemy Ship " + (i + 1) + " deployed");
            }
        }
    }

    public static void activeGame(char[][] activeBoard, char[][] grid) {
        while (userShipsAlive != 0 && enemyShipsAlive != 0) {
            getValidUserGuess(activeBoard);
            printGrid(grid);
            getValidComputerGuess(activeBoard);
        }
    }

    private static void getValidUserGuess(char[][] activeBoard) {
        Scanner scnr = new Scanner(System.in);

        System.out.print("Please enter an target coordinate x,y: ");

        String input = scnr.next();
        int xCord = Character.getNumericValue(input.charAt(0));
        int yCord = Character.getNumericValue(input.charAt(2));

        try {
            checkEnemyHit(activeBoard, grid, xCord, yCord);
        } catch(IndexOutOfBoundsException e) {
            System.out.println("Error: Ship coordinates larger than grid, pick another position");
            getValidUserGuess(activeBoard);
        } catch(Exception e) {
            System.out.println("Error: Something went wrong, pick another position");
            getValidUserGuess(activeBoard);
        }
    }

    private static void checkEnemyHit(char[][] activeBoard, char[][] grid, int xCord, int yCord) {
        System.out.print("Coordiante " + xCord + "," + yCord + ": ");
        if (activeBoard[yCord][xCord] == '?') {
            enemyShipsAlive--;
            activeBoard[yCord][xCord] = 'X';
            grid[yCord][xCord] = 'X';
            System.out.println("Hit!");
        } else if (activeBoard[yCord][xCord] == '@' || activeBoard[yCord][xCord] == 'O' || activeBoard[yCord][xCord] == 'X') {
            System.out.println("Invalid Coordinate");
            getValidUserGuess(activeBoard);
        } else {
                activeBoard[yCord][xCord] = 'O';
                grid[yCord][xCord] = 'O';
                System.out.println("Miss!");
            }
    }

    private static void getValidComputerGuess(char[][] activeBoard) {
        Random random = new Random();
        int randXCord = random.nextInt((9 - 0) + 1);
        int randYCord = random.nextInt((9 - 0) + 1);

        checkUserHit(activeBoard, randXCord, randYCord);
    }

    private static void checkUserHit(char[][] activeBoard, int xCord, int yCord) {
        if (activeBoard[yCord][xCord] == '@') {
            userShipsAlive--;
            activeBoard[yCord][xCord] = 'x';
            grid[yCord][xCord] = '*';
            System.out.print("Computer coordinate guess " + xCord + "," + yCord  + ": ");
            System.out.println("Hit!");
        } else if (activeBoard[yCord][xCord] == '?' || activeBoard[yCord][xCord] == 'x' || activeBoard[yCord][xCord] == 'o') {
            getValidUserGuess(activeBoard);
        } else {
            activeBoard[yCord][xCord] = 'o';
            System.out.print("Computer coordinate guess " + xCord + "," + yCord + ": ");
            System.out.println("Miss!");
        }
    }

    private static void checkWinnerAndRematch() {
        Scanner scnr = new Scanner(System.in);

        if (userShipsAlive == 0){
            System.out.println("Computer Wins!");
        }
        if (enemyShipsAlive == 0) {
            System.out.println("User Wins!");
        }

        System.out.println("Play Again? (y/n): ");
        response = scnr.nextLine().charAt(0);
    }
}
