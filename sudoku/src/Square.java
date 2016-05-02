import helpers.Pair;

import java.util.ArrayList;

/**
 * Created by ubuntu on 2016-05-02.
 */
public class Square {
    private int[][] numbers;

    /**
     * Constructs a sudoku cell(a square in the table)
     */
    public Square(){
        numbers = new int[Table.TABLE_SIZE][Table.TABLE_SIZE];
    }

    /**
     * Calculates square-number based on table coordinates
     * @param x x table-coordinate
     * @param y y table-coordinate
     * @return square-number Pair
     */
    public static Pair<Integer, Integer> calcSquareNumber(int x, int y){
        int squareX = (1 + (x-1)) / Table.SQUARE_SIZE;
        int squareY = (1 + (y-1)) / Table.SQUARE_SIZE;
        return new Pair<>(squareX, squareY);
    }

    /**
     * Checks if the cell contains a number
     * @param number the number to be checked
     * @return TRUE: the cell contains the number; FALSE: The cell does NOT contain the number
     */
    public boolean contains(int number){
        for (int y = 0; y < Table.SQUARE_SIZE; y++){
            for (int x = 0; x < Table.SQUARE_SIZE; x++){
                if (numbers[x][y] == number){
                    return true;
                }
            }
        }
        return false;
    }

    public int[][] getNumbers() {
        return numbers;
    }

    public void setNumbers(int[][] numbers) {
        this.numbers = numbers;
    }

    /**
     * Returns a row in the square as a string
     * @param row index of row
     * @return the row as a string
     */
    public String rowOutput(int row){
        StringBuilder builder = new StringBuilder();
        builder.append(numbers[0][row]);
        builder.append(" ");
        builder.append(numbers[1][row]);
        return builder.toString();
    }
}
