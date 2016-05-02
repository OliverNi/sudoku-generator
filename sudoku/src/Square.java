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
