/**
 * Created by ubuntu on 2016-05-02.
 */
public class Main {
    public static void main(String args[]){
        Table table = new Table();
        table.generate(6);
        System.out.println(table.output());
    }
}
