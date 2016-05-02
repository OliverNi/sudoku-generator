import helpers.Pair;

/**
 * Created by ubuntu on 2016-05-02.
 */
public class Square {
    int[][] numbers;
    /**
     * Constructs a sudoku cell(a square in the table)
     */
    public Square(){
        numbers = new int[Table.TABLE_SIZE][Table.TABLE_SIZE];
    }

    public static Pair<Integer, Integer> calcCellNumber(int x, int y){
        double sqrt = Math.sqrt(Table.TABLE_SIZE);
        int squareX = (x-1) / (int) sqrt;
        int squareY = (y-1) / (int) sqrt;
        return new Pair<>(squareX, squareY);
    }

    /**
     * Checks if the cell contains a number
     * @param number the number to be checked
     * @return TRUE: the cell contains the number; FALSE: The cell does NOT contain the number
     */
    public boolean contains(int number){
        return false;
    }

    public int[][] getNumbers() {
        return numbers;
    }

    public void setNumbers(int[][] numbers) {
        this.numbers = numbers;
    }
}
