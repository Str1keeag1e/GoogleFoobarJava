import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

public class ExpandingNebula  {
    public static int solution(boolean [][] g) {
        //we want to work with the shortest side. So if initially, the row is longer than column

        int numRows = g.length;
        int numCols = g[0].length;

        if(g.length < g[0].length){
            boolean [][] newMatrix = transposeMatrix(g);
            numRows = newMatrix.length;
            numCols = newMatrix[0].length;

            g = newMatrix;
        }

        //We can turn the nebula into bit strings
        //First I will sum up the bit string amount (true = 1, false = 0) and stick it into an ArrayList.
        // ex:[true,true,true,true,true] represents [2^4, 2^3, 2^2, 2^1, 2^0] = 31 when summed over the array.

        ArrayList<Integer> numsInNebula = new ArrayList<>();

        for(int i = 0; i < numRows; i++){
            int bitSum = 0;
            for(int j = 0; j < numCols ; j++){
                if(g[i][j]){
                    bitSum += (1 << j);
                }
            }
            numsInNebula.add(bitSum);
        }


        HashMap<Integer,Integer> testMap = new HashMap<>();

        // We know we can expand any row[0][n] into a pre-image of size [1][n+1];
        // Build a HashMap<IntegerA,HashMap<IntegerB,IntegerC>>
        // 1.) stores the values of the individual row bit string as IntegerA. row[0]
        // 2.) store what a possible expanded row[0] value would be in Integer B
        // 3.) store what possible row[1] value into IntegerC (This map must use Integer B as the row above value).

        HashMap<Integer,HashMap<Integer,ArrayList<Integer>>> numberMap = generateMap(numCols, numsInNebula);

        // now we need to setup an actual pre-image map that will store
        // the possible pre-images for the next row.
        // once this is created--we will always use the possible pre-images
        // to process the next row. This will continually filter down the amount of pre-images
        // we will count the total amount of pre-images stored when we reach the last row.

        HashMap<Integer,Integer> preImageMap = new HashMap<>();
        for(int i = 0; i < (1 << (numCols + 1)); i++){
            preImageMap.put(i, 1);
        }


        //now the actual processing until last row.

        //for each number of the original nebula
        for(Integer i : numsInNebula){

            Hashtable<Integer,Integer> nextRow = new Hashtable<>();

            //for each number in the original nebula, and within the possible pre-image set.



            for(int j : preImageMap.keySet()){

                //going through the list stored in numberMap(rowValue,j) -

                if(numberMap.get(i).get(j) == null) continue;

                for(int k : numberMap.get(i).get(j)){




                    if(!nextRow.containsKey(k)) {
                        nextRow.put(k, preImageMap.get(j));
                        testMap.put(k, preImageMap.get(j));
                    }else{
                        nextRow.put(k, nextRow.get(k) + preImageMap.get(j));
                        testMap.put(k, testMap.get(k) + preImageMap.get(j));
                    }
                }
            }
            preImageMap.clear();
            preImageMap.putAll(nextRow);



        }
        int result = preImageMap.values().stream().mapToInt(Integer::intValue).sum();


        return result;
    }


    public static HashMap<Integer,HashMap<Integer,ArrayList<Integer>>> generateMap (int n, ArrayList<Integer> nums){

        //[key, key, list of values]
        //[original value, top row expanded, list of possible bottom rows expanded]
        HashMap<Integer,HashMap<Integer,ArrayList<Integer>>> numberMap = new HashMap<>();

        //Need to put into set so we don't hit the same number again. (The pre-images are deterministic based on current row and next row.)
        HashSet<Integer> numsSet = new HashSet<>(nums);

        //looping through all possible values of expanded row. so 2^n+1 - 1.
        for(int i = 0; i < (1 << n + 1); i++){
            for(int j = 0; j < (1 << n + 1); j++){

                int originalValue = generate(i, j, n);

                if(numsSet.contains(originalValue)){
                    if(numberMap.get(originalValue) == null){
                        HashMap<Integer,ArrayList<Integer>> tempMap = new HashMap<>();

                        numberMap.put(originalValue, tempMap);
                    }

                    numberMap.get(originalValue).computeIfAbsent(i, k -> new ArrayList<>()).add(j);
                }

            }

        }

        return numberMap;
    }

    public static boolean[][] transposeMatrix(boolean [][] b){
        int m = b.length;
        int n = b[0].length;

        boolean [][] newMatrix = new boolean[n][m];

        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                newMatrix[i][j] = b[j][i];
            }
        }
        return newMatrix;
    }


    //generates if arg1, arg2 are valid bit string rows for the original value.
    public static int generate(int currentRowValue, int nextRowValue, int bitStringLength){

        int a = currentRowValue & ~(1 << bitStringLength);
        int b = nextRowValue & ~(1 << bitStringLength);
        int c = currentRowValue >> 1;
        int d = nextRowValue >> 1;
        return (a & ~b & ~c & ~d) | (~a & b & ~c & ~d) | (~a & ~b & c & ~d) | (~a & ~b & ~c & d);

    }

}
