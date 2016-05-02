import helpers.Pair;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ubuntu on 2016-05-02.
 */
public class Table {
    public static final int TABLE_SIZE = 4;
    private Random rand;
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
        rand = new Random();
        pool = new ArrayList<>();
        quarantine = new ArrayList<>();
        squares = new Square[TABLE_SIZE][TABLE_SIZE];
        for (int i = 1; i <= TABLE_SIZE; i++){
            pool.add(i);
        }

        double sqrt = Math.sqrt(TABLE_SIZE);
        for (int y = 0; y < sqrt; y++){
            for (int x = 0; x < sqrt; x++){
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
                if (checkConflict(x, y, pool.get(nextVal))){
                    //conflict detected
                    quarantine.add(pool.get(nextVal));
                    pool.remove(nextVal);
                    x--;
                    //Check if this was the last option - if so -> backtrack
                    if (pool.size() == 0){
                        backtrack();
                    }
                }
                else{
                    //Valid number
                    placeNumberInCell(x, y, pool.get(nextVal));
                    pool.remove(nextVal);
                    moveQuarantinesToPool();
                }
            }
        }
    }

    /**
     * Returns a String output of the table
     * @return the table in String format
     */
    public String output(){
        StringBuilder builder = new StringBuilder();
        builder.append("|");
        for (int i = 0; i < TABLE_SIZE; i++){
            builder.append("-");
        }

        builder.append("-|");
        builder.append("\n");

        double sqrt = Math.sqrt(TABLE_SIZE);
        for (int i = 0; i < sqrt; i++){
            for (int j = 0; j < sqrt; j++){
                builder.append("|");
                for (int k = 0; k < sqrt; k++){
                    for (int l = 0; l < sqrt; l++){
                        builder.append(squares[i][j].getNumbers()[k][l]);
                    }
                    builder.append("|");
                }
                builder.append("\n");
            }
        }
        builder.append("|");
        for (int i = 0; i < TABLE_SIZE; i++){
            builder.append("-");
        }
        builder.append("-|");

        return builder.toString();
    }

    /**
     * Picks a random number from the pool
     * @return the index of the number in the pool
     */
    private int nextRandomFromPool(){
        return rand.nextInt(pool.size());
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
        else if (checkConflictColumn(x)) {
            //Conflict detected - number already exists in column (x-axis)
            return true;
        }
        else if (checkConflictRow(y)){
            //Conflict detected - number already exists in row (y-axis)
            return true;
        }

        return false;
    }

    private boolean checkConflictColumn(int col){
        return false;
    }

    private boolean checkConflictRow(int row){
        return false;
    }

    /**
     * Place a number in a cell
     * @param x x-coordinate in the table of the cell to be checked
     * @param y y-coordinate in the table of the cell to be checked
     */
    private void placeNumberInCell(int x, int y, int number){

    }

    /**
     * Moves numbers in quarantine to the pool
     * @return the number of numbers moved to the pool
     */
    private int moveQuarantinesToPool(){
        return 0;
    }

    /**
     *
     */
    private void backtrack(){
        //@TODO remove previous cell
        //@TODO place previous value in quarantine
    }
}
