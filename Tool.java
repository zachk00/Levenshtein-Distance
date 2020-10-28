


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

    public LinkedList<String> getOrderOfOperations(){ // the order of operations must be fliped, as it is initially found backwards
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
        
        orderOfOperations(matrix, from, to); // use completed matrix to find order of operations
        this.result = matrix[to.length()][from.length()];
    }

    private static int[][] initMatrix(String from, String to) {//initializes 2d array based on the lengths of the input strings + 1, which accounts for the empty string

        int[][] initial = new int[to.length() + 1][from.length() + 1];

        for (int i = 0; i < from.length() + 1; i++) {//row
            initial[0][i] = i;
        }

        for (int j = 0; j < to.length() + 1; j++) {//col

            initial[j][0] = j;
        }

        return initial;
    }

    private static void debug(int[][] matrix, String from, String to) {//method to print out values stored in 2d array for debugging
        for (int i = 0; i < from.length() + 1; i++) {
            for (int j = 0; j < to.length() + 1; j++) {
                System.out.print(matrix[j][i] + " ");
            }
            System.out.println();
        }
    }

    private static Map<Character, Integer> minRepeated(String from, String to) { 
        
        /*  
            In the algorithm to find the levenshtein edit distance, characters which are equal must be taken into account
            this is because letters that the same require no action, meaning no edit is needed
            this method finds how many times each character in the original string appears in the final string
            Ex. Seen -> Teen
            The letter 'e' appears twice in both teen and seen, and therefore no action should be taken when comparing those two characters
            ( <s, 0 >, <e, 2> , <n, 1>) would be the resulting map produced
        */
        
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

        Stack<String> operations = new Stack<String>(); // stores order of operations in reverse, will utilizes last in, first out principle to get correct order

        int curr = matrix[matrix.length - 1][matrix[0].length - 1]; //value in the current square
        int col = matrix.length - 1; // current column
        int row = matrix[0].length - 1; // current row
        int indexRow, indexCol; // location at which operation occurrs in original string



        while (col > 0 || row > 0) {//loop until adjacent to the top left square
            //ensure no out of bounds via checking to see if next index is greater than zero

            // if diagonal square is valid, and is greater than or equal to the other squares, take that square
            if (row != 0 && col != 0 && matrix[col - 1][row] >= matrix[col - 1][row - 1] && matrix[col][row - 1] >= matrix[col - 1][row - 1]) {
                row--;
                col--;
                //if the characters in that row/col are equal, then no operation is necessary
                if (from.charAt(row) == to.charAt(col)) {

                    operations.push("Nothing");
                } else {
                    //other wise, we must replace the letter in the row with the letter in the collumn
                    indexCol = col;
                    indexRow = row;
                    operations.push("Replace "+ from.charAt(row) + " at index " + indexRow +  " with " + to.charAt(col) + "." );
                }
            } else if (row == 0 || col != 0 && matrix[col][row - 1] >= matrix[col - 1][row]) { // otherwise, if the top square is greater or equal to the left square
                if(curr - 1 == matrix[col - 1][row] || curr == matrix[col - 1][row]){// and the current values is one greater or equal to the value of top left square
                    //select this square and perform an insertion of the column's character
                    indexCol =  - 1;
                    operations.push("Insert " + to.charAt(col - 1) + " at index " + indexCol +  ".");
                    col--;

                }

            } else{ //otherwise perform a deletion of the character from the next highest row

                indexRow = row - 1;
                operations.push("Delete " + from.charAt(indexRow) + " at index " + indexRow + ".");//
                row--;

            }

            curr = matrix[col][row]; //update the value of the square to the current square

        }
        //solve final step
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
