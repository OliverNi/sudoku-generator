import helpers.Pair;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ubuntu on 2016-05-02.
 */
public class Table {
    public static final int TABLE_SIZE = 4;
    public static final int SQUARE_SIZE = (int)Math.sqrt(TABLE_SIZE);
    private Random rand;

    //Keeps track of backtracking history to make sure that
    // a number is not tried several times in one cell
    ArrayList<Integer>[][] backtrackHist;
    //Pool which stores available numbers
    private ArrayList<Integer> pool;
    /* Quarantine for numbers where they are placed temporarily
       if they cannot be us for the next action but may be used in the future.
     */
    private ArrayList<Integer> quarantine;

    //The squares in the table
    private Square[][] squares;

    /**
     * Constructs an empty sudoku table
     */
    public Table(){
        backtrackHist= new ArrayList[TABLE_SIZE][TABLE_SIZE];
        for(int i=0;i<backtrackHist.length;i++){
            for(int j=0;j<backtrackHist[i].length;j++){
                backtrackHist[i][j]=new ArrayList<Integer>();
            }
        }
        rand = new Random();
        pool = new ArrayList<>();
        quarantine = new ArrayList<>();
        squares = new Square[SQUARE_SIZE][SQUARE_SIZE];
        for (int i = 1; i <= TABLE_SIZE; i++){
            pool.add(i);
        }

        for (int y = 0; y < SQUARE_SIZE; y++){
            for (int x = 0; x < SQUARE_SIZE; x++){
                squares[x][y] = new Square();
            }
        }
    }

    /**
     * Generates numbers for the sudoku table
     * @param nrOfClues the number of initial clues in the table
     */
    public void generate(int nrOfClues){
        int nextVal;
        for (int y = 0; y < TABLE_SIZE; y++){
            for (int x = 0; x < TABLE_SIZE; x++){
                nextVal = nextRandomFromPool();
                if (pool.size() == 0){
                    int temp = x;
                    x--;
                    moveQuarantinesToPool();
                    int val = backtrack(x, y);
                    if (backtrackHist[temp][y].contains(val)){
                        //The previous backtrack-number has already been tried -> go back one more step
                        x--;
                        backtrack(x, y);
                        x--;
                    }
                    else {
                        //The backtrack was successful -> refill the pool
                        x--;
                        moveQuarantinesToPool();
                    }
                }
                else if (checkConflict(x, y, pool.get(nextVal))){
                    //conflict detected
                    backtrackHist[x][y].add(pool.get(nextVal));
                    x--;
                    quarantine.add(pool.remove(nextVal));
                }
                else{
                    //Valid number
                    placeNumberInSquare(x, y, pool.get(nextVal));
                    pool.remove(nextVal);
                    moveQuarantinesToPool();
                }
                System.out.println(output());
            }
            restorePool();
        }
    }

    /**
     * Returns a String output of the table
     * @return the table in String format
     */
    public String output(){
        StringBuilder builder = new StringBuilder();
        builder.append("|--");
        for (int i = 0; i < TABLE_SIZE; i++){
            builder.append("-");
        }
        builder.append("-|");
        builder.append("\n");

        int nrOfSquares = TABLE_SIZE / SQUARE_SIZE;
        int row = 0;
        for (int y = 0; y < SQUARE_SIZE; y++) {
            //Print one Square-row
            builder.append("|");
            for (int x = 0; x < nrOfSquares; x++) {
                builder.append(squares[x][y].rowOutput(row));
                builder.append("|");
            }
            builder.append("\n");
            builder.append("|");
            row = (row + 1) % SQUARE_SIZE;
            for (int x = 0; x < nrOfSquares; x++) {
                builder.append(squares[x][y].rowOutput(row));
                builder.append("|");
            }
            builder.append("\n");
            row = (row + 1) % SQUARE_SIZE;
        }
        builder.append("|--");
        for (int i = 0; i < TABLE_SIZE; i++){
            builder.append("-");
        }
        builder.append("-|");
        builder.append("\n");

        return builder.toString();
    }

    /**
     * Picks a random number from the pool
     * @return the index of the number in the pool
     */
    private int nextRandomFromPool(){
        int size;
        if ((size = pool.size()) > 0) {
            return rand.nextInt(size);
        }
        return 0;
    }

    /**
     * Checks if there are any conflicts caused by a cell's number
     * @param x x-coordinate in the table of the cell to be checked
     * @param y y-coordinate in the table of the cell to be checked
     * @param number the number to be checked
     * @return TRUE: conflict detected; FALSE: no conflict
     */
    private boolean checkConflict(int x, int y, int number){
        Pair<Integer, Integer> pair = Square.calcSquareNumber(x, y);
        if (squares[pair.getX()][pair.getY()].contains(number)){
            //conflict detected - number already exists in square
            return true;
        }
        else if (checkConflictColumn(x, y, number)) {
            //Conflict detected - number already exists in column (x-axis)
            return true;
        }

        return false;
    }

    /**
     * Checks for conflicts in the x-axis
     * @param x x table-coordinate
     * @param y y table-coordinate
     * @param number number to be checked
     * @return TRUE: conflict found; FALSE: Conflict not found
     */
    private boolean checkConflictColumn(int x, int y, int number){
        while (y > 0){
            y--;
            Pair<Integer, Integer> sCoord = Square.calcSquareNumber(x, y);
            int sX = x % SQUARE_SIZE;
            int sY = y % SQUARE_SIZE;
            if (squares[sCoord.getX()][sCoord.getY()].getNumbers()[sX][sY] == number){
                return true;
            }
        }
        return false;
    }

    /**
     * Place a number in a square
     * @param x x-coordinate in the table of the cell to be checked
     * @param y y-coordinate in the table of the cell to be checked
     */
    private void placeNumberInSquare(int x, int y, int number){
        Pair<Integer, Integer> sCoord = Square.calcSquareNumber(x, y);
        int sX = x % SQUARE_SIZE;
        int sY = y % SQUARE_SIZE;
        squares[sCoord.getX()][sCoord.getY()].getNumbers()[sX][sY] = number;
    }

    /**
     * Moves numbers in quarantine to the pool
     * @return the number of numbers moved to the pool
     */
    private int moveQuarantinesToPool(){
        int count = 0;
        for (Integer i:
                quarantine) {
            if (!pool.contains(i)) {
                pool.add(i);
                count++;
            }
        }
        quarantine.clear();
        return count;
    }

    /**
     * Moves back one step in the table-filling process
     * @param x x table-coordinate
     * @param y y table-coordinate
     * @return The value which was quarantined
     */
    private int backtrack(int x, int y){
        Pair<Integer, Integer> squareCoord = Square.calcSquareNumber(x, y);
        int sX = x % SQUARE_SIZE;
        int sY = y % SQUARE_SIZE;
        int val = squares[squareCoord.getX()][squareCoord.getY()]
                .getNumbers()[sX][sY];
        squares[squareCoord.getX()][squareCoord.getY()]
                .getNumbers()[sX][sY] = 0;
        quarantine.add(val);
        return val;
    }

    /**
     * Restores the pool and the quarantine to its initial state
     */
    private void restorePool(){
        pool = new ArrayList<>();
        quarantine = new ArrayList<>();
        for (int i = 1; i <= TABLE_SIZE; i++){
            pool.add(i);
        }
    }

    /**
     * Retrieves the number at specified table-coordinates
     * @param x x table-coordinate
     * @param y y table-coordinate
     * @return the number stored at the specified table-coordinates
     */
    private int getNumber(int x, int y){
        Pair<Integer, Integer> squareCoord = Square.calcSquareNumber(x, y);
        int sX = x % SQUARE_SIZE;
        int sY = y % SQUARE_SIZE;
        return squares[squareCoord.getX()][squareCoord.getY()]
                .getNumbers()[sX][sY];
    }
}
