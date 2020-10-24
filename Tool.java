
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.*;

public class Tool {
    private int result;
    private Stack<String> orderOfOperations;

    public Tool() {
        result = 0;
    }

    public int getResult() {
        return this.result;
    }

    public LinkedList<String> getOrderOfOperations(){
        LinkedList<String> operations = new LinkedList<String>();
        while(!this.orderOfOperations.isEmpty()){
            operations.add(orderOfOperations.pop());
        }
        return operations;
    }

    public void editDistance(String from, String to) {
        //col, row
        //edit distance is 0 if strings are equal
        if (from.equalsIgnoreCase(to)) {
            this.result = 0;
        }
        from = from.toLowerCase();
        to = to.toLowerCase();

        int[][] matrix = initMatrix(from, to); //initial 2d array based on lengths of input strings


        //add a space to the front of the input strings to account for the empty strings
        String spaceFrom = " ";
        spaceFrom = spaceFrom.concat(from);
        String toFrom = " ";
        toFrom = toFrom.concat(to);
        Map<Character, Integer> repeatedCharacters = minRepeated(from, to);

        for (int i = 1; i < from.length() + 1; i++) {
            for (int j = 1; j < to.length() + 1; j++) {
                //retrieve the minimum edit distance from the previous substrings
                //if the character is the same, we need not perform an operation unless we have a differing number of the same letter
                // ex. seen -> bet,  we can leave one of the e's alone since we will need it in the final string, but the second e must be deleted/replaced
                int min = Math.min(matrix[j - 1][i - 1], Math.min(matrix[j - 1][i], matrix[j][i - 1]));
                if (spaceFrom.charAt(i) != toFrom.charAt(j) || repeatedCharacters.get(spaceFrom.charAt(i)) == 0) {
                    min++;
                }
                if (spaceFrom.charAt(i) == toFrom.charAt(j)) {
                    repeatedCharacters.replace(spaceFrom.charAt(i), repeatedCharacters.get(spaceFrom.charAt(i)) - 1);
                }

                matrix[j][i] = min;

            }

        }
        //debug(matrix, from, to);
        orderOfOperations(matrix, from, to);
        this.result = matrix[to.length()][from.length()];
    }

    private static int[][] initMatrix(String from, String to) {

        int[][] initial = new int[to.length() + 1][from.length() + 1];

        for (int i = 0; i < from.length() + 1; i++) {
            initial[0][i] = i;
        }

        for (int j = 0; j < to.length() + 1; j++) {//col

            initial[j][0] = j;
        }

        return initial;
    }

    private static void debug(int[][] matrix, String from, String to) {
        for (int i = 0; i < from.length() + 1; i++) {
            for (int j = 0; j < to.length() + 1; j++) {
                System.out.print(matrix[j][i] + " ");
            }
            System.out.println();
        }
    }

    private static Map<Character, Integer> minRepeated(String from, String to) {
        Map<Character, Integer> repeated = new HashMap<Character, Integer>();

        for (int i = 0; i < from.length(); i++) {
            repeated.putIfAbsent(from.charAt(i), 0);
        }

        for (int i = 0; i < to.length(); i++) {
            for (Character val : repeated.keySet()) {
                if (val == to.charAt(i)) {
                    repeated.replace(to.charAt(i), repeated.get(to.charAt(i)) + 1);
                }
            }
        }

        return repeated;
    }

    private void orderOfOperations(int[][] matrix, String from, String to) {

        Stack<String> operations = new Stack<String>();

        int curr = matrix[matrix.length - 1][matrix[0].length - 1];
        int col = matrix.length - 1;
        int row = matrix[0].length - 1;
        boolean nextCol;
        boolean nextRow;
        int indexRow, indexCol;
        //System.out.println(matrix[0].length); // rows
        //System.out.println(matrix.length); //cols


        while (col > 0 && row > 0) {
            nextCol = col - 1 >= 0;
            nextRow = row - 1 >= 0;
            if (nextCol && nextRow && matrix[col - 1][row] >= matrix[col - 1][row - 1] && matrix[col][row - 1] >= matrix[col - 1][row - 1]) {
                row--;
                col--;
                if (from.charAt(row) == to.charAt(col)) {

                    operations.push("Nothing");
                } else {
                   indexCol = col;
                   indexRow = row;
                   operations.push("Replace "+ from.charAt(row) + " at index " + indexRow +  " with " + to.charAt(col) + "." );
                }
            } else if (nextRow && nextCol && matrix[col][row - 1] >= matrix[col - 1][row]) {
                if(curr - 1 == matrix[col - 1][row] || curr == matrix[col - 1][row]){
                    indexCol = col;
                    operations.push("Insert " + to.charAt(col) + " at index " + indexCol +  ".");
                    col--;

                }

            } else{
                indexRow = row - 1;
                operations.push("Delete " + from.charAt(row - 1) + " at index " + indexRow + ".");//
                row--;

            }

            curr = matrix[col][row];

        }

        if(col == 1 && row == 0){
            indexCol = col - 1;
            operations.push("Insert " + to.charAt(col - 1) +  " at index " + indexCol + ".");

        }else if(row == 1 && col == 0){
            indexRow = row - 1;
            operations.push("Delete " + from.charAt(row - 1) + " at index " + indexRow + "." );

        }else if(row == 1 && col == 1){
            operations.push("Replace " + from.charAt(row) + " with " + to.charAt(col - 1));

        }


        this.orderOfOperations  = operations;

    }

}
