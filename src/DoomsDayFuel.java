import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DoomsDayFuel {

    public static int[] doomsdayFuel(int[][] m) {
        HashMap<Integer,Integer> list = new HashMap<>();

        for(int i = 0; i < m.length; i++){
            int total = 0;
            for(int j = 0; j < m[i].length; j++){
                total += m[i][j];
            }
            list.put(i,total);
        }

        int zeroRows = 0;

        for(int i : list.values()){
            if(i == 0)
                zeroRows++;
        }

        int nonZeroRows = m.length - zeroRows;

        float [][] identityMatrix = identityMatrixFloat(nonZeroRows);
        float [][] qMatrix = new float[nonZeroRows][nonZeroRows];
        float [][] rMatrix = new float[nonZeroRows][zeroRows];

        //populating qMatrix, god forsaken code but im brain dead rn and it could be improved alot probably.
        ArrayList<Integer> qList = new ArrayList<>();
        ArrayList<Integer> qListValues = new ArrayList<>();

        for(Map.Entry<Integer,Integer> i : list.entrySet()){
            if(i.getValue() != 0){
                qList.add(i.getKey());
            }
        }

        for(int i : qList){
            for(int j : qList){
                qListValues.add(m[i][j]);
            }
        }

        for(int i = 0; i < qMatrix.length; i++){
            for(int j = 0; j < qMatrix.length; j++){
                qMatrix[i][j] = qListValues.remove(0);
            }
        }
        for(int i = 0; i < qMatrix.length; i++){
            for(int j = 0; j < qMatrix.length; j++){
                qMatrix[i][j] = qMatrix[i][j] / list.get(qList.get(i));
            }
        }

        //populating rMatrix

        ArrayList<Integer> rList = new ArrayList<>();
        ArrayList<Integer> rListValues = new ArrayList<>();

        for(Map.Entry<Integer,Integer> i : list.entrySet()){
            if(i.getValue() == 0){
                rList.add(i.getKey());
            }
        }


        for(int i : qList){
            for(int j : rList){
                rListValues.add(m[i][j]);
            }
        }



        for(int i = 0; i < rMatrix.length; i++){
            for(int j = 0; j < rMatrix[i].length; j++){
                rMatrix[i][j] = rListValues.remove(0);
            }
        }
        for(int i = 0; i < rMatrix.length; i++){
            for(int j = 0; j < rMatrix[i].length; j++){
                rMatrix[i][j] = rMatrix[i][j] / list.get(qList.get(i));
            }
        }
        //debug



        //find F = (I - Q)^-1

        subtractMatrix(identityMatrix,qMatrix);


        float [][] fMatrix = bareissAlgorithm(identityMatrix);



        rMatrix = multiplyMatrix(fMatrix,rMatrix);




        ArrayList<Double> numerators = new ArrayList<>();
        ArrayList<Double> denominators = new ArrayList<>();
        for(float i : rMatrix[0]){
            convertDecimalToFraction(i,numerators,denominators);
        }

        int [] n1 = numerators.stream().mapToInt(Double::intValue).toArray();
        int [] d1 = denominators.stream().mapToInt(Double::intValue).toArray();



        return biggestNumerator(n1,d1);
    }


    public static float [][] subtractMatrix(float [][] m1, float[][] m2){
        for(int i = 0; i < m1.length; i++){
            for(int j = 0; j < m1[i].length; j++){
                m1[i][j] = m1[i][j] - m2[i][j];
            }
        }
        return m1;
    }

    public static int [] biggestNumerator(int [] n1, int [] d1){
        if(n1.length != d1.length) throw new IllegalArgumentException("Lengths must be same! ");
        int biggest = 1;
        int n = n1.length;

        for(int i = 0; i < n1.length; i++){
            if(d1[i] > biggest)
                biggest = d1[i];
        }

        for(int i = 0; i < n1.length; i++){
            if(d1[i] < biggest)
                n1[i] *=  (biggest /d1[i]);
        }
        n1 = Arrays.copyOf(n1,n + 1);
        n1[n] = biggest;


        return n1;
    }

    public static double findBiggest(ArrayList<Double> x){
        double biggest = 0;

        for(int i = 0; i < x.size()-1;i++){
            if(x.get(i) > biggest)
                biggest = x.get(i);
        }
        return biggest;
    }

    /***
     public static int [][] identityMatrix(int n){
     int [][] identityMatrix = new int [n][n];
     for(int i = 0; i < n; i++){
     identityMatrix[i][i] = 1;
     }
     return identityMatrix;
     }
     ***/
    public static float [][] identityMatrixFloat(int n){
        float [][] identityMatrix = new float [n][n];
        for(int i = 0; i < n; i++){
            identityMatrix[i][i] = 1;
        }
        return identityMatrix;
    }

    /***
     public int [][] swapRows(int [][] m, int row1, int row2){

     int [] temp = m[row1];
     m[row1] = m[row2];
     m[row2] = temp;

     return m;

     }

     public void upperTriangle(float[][] m) {

     float f1 = 0;
     float temp = 0;
     float temp1 = 0;
     int tms = m.length; // get This Matrix Size (could be smaller than
     // global)
     int switches = 1;

     int iDF = 1;

     for (int col = 0; col < tms - 1; col++) {
     for (int row = col + 1; row < tms; row++) {
     switches = 1;

     while (m[col][col] == 0) // check if 0 in diagonal
     { // if so switch until not
     if (col + switches >= tms) // check if switched all rows
     {
     iDF = 0;
     break;
     } else {
     for (int c = 0; c < tms; c++) {
     temp = m[col][c];
     m[col][c] = m[col + switches][c]; // switch rows
     m[col + switches][c] = temp;


     }
     switches++; // count row switches
     iDF = iDF * -1; // each switch changes determinant
     // factor
     }
     }

     if (m[col][col] != 0) {

     try {
     f1 = (-1) * m[row][col] / m[col][col];
     for (int i = col; i < tms; i++) {
     m[row][i] = f1 * m[col][i] + m[row][i];
     }
     } catch (Exception e) {
     System.out.println("Still Here!!!");
     }

     }

     }
     }
     }

     ***/
    public static float [][] bareissHelper(float [][] m){
        int n = m.length;
        float [][] combinedMatrix = new float [n][n*2];
        //copying over array to combinedMatrix

        for(int i = 0, k = n ; i < n; i++, k++){
            for(int j = 0 ; j < n  ; j++){
                combinedMatrix[i][j] = m[i][j];
            }
            combinedMatrix[i][k] = 1;
        }
        return combinedMatrix;
    }

    public static float [][] bareissAlgorithm(float [][] m){
        int n = m.length;

        float [][] combinedMatrix = bareissHelper(m);
        int rows = combinedMatrix.length;
        int cols = combinedMatrix[0].length;

        float [][] tempMatrix = new float [rows][cols];


        for(int i = 0; i < rows; i++){
            float pivotDivisor = 1;
            arrayCopy(combinedMatrix, tempMatrix);

            for(int j = 0 ; j < rows; j++) {
                if (j == i) continue;
                for (int k = 0; k < cols; k++) {
                    float a1 = tempMatrix[j][i] * tempMatrix[i][k];
                    float a2 = tempMatrix[i][i] * tempMatrix[j][k];

                    combinedMatrix[j][k] = (a2 - a1) / pivotDivisor;
                }
            }

            pivotDivisor = combinedMatrix[i][i];
        }

        bareissHelper2(combinedMatrix);

        for(int i = 0; i < n; i++){
            for(int j = 0, k = n; j < n ; j++, k++){
                m[i][j] = combinedMatrix[i][k];
            }
        }
        return m;
    }

    public static float [][] bareissHelper2(float [][] m){


        int n = m.length;
        float pivotElement =  m[n-1][n-1];
        for(int i = 0;  i < n - 1; i ++){
            float divisor = (m[i][i] / pivotElement);
            for(int j = 0; j < m[i].length; j++){
                m[i][j] /= divisor;
            }
        }

        for(int i = 0; i < n ; i++){
            for(int j = n; j < m[i].length; j++){
                m[i][j] /= pivotElement;
            }
        }

        return m;
    }

    public static void arrayCopy(float[][] aSource, float[][] aDestination) {
        for (int i = 0; i < aSource.length; i++) {
            System.arraycopy(aSource[i], 0, aDestination[i], 0, aSource[i].length);
        }
    }

    public int [][] transposeMatrix(int [][] m){
        for(int i = 0; i < m.length; i++){
            for(int j = i; j < m[i].length; j++){
                int temp = m[i][j];
                m[i][j] = m[j][i];
                m[j][i] = temp;
            }
        }

        return m;
    }
    /***
     public float [][] transposeMatrix(float [][] m){
     for(int i = 0; i < m.length; i++){
     for(int j = i; j < m[i].length; j++){
     float temp = m[i][j];
     m[i][j] = m[j][i];
     m[j][i] = temp;
     }
     }

     return m;
     }

     public int [][] multiplyMatrix(int [][] m1, int [][] m2){
     if(m1[0].length != m2.length) throw new IllegalArgumentException("Must be matrix of sizes AxB times BxZ to produce AxZ matrix");

     int [][] newMatrix = new int[m1.length][m2[0].length];

     for(int i = 0; i < m1.length; i ++){
     for(int j = 0; j < m2[0].length; j ++){
     for(int k = 0; k < m2.length; k++){
     newMatrix[i][j] = m1[i][k] * m2[k][j];
     }
     }
     }
     return newMatrix;
     }
     ***/
    public static float [][] multiplyMatrix(float [][] m1, float [][] m2){
        if(m1[0].length != m2.length) throw new IllegalArgumentException("Must be matrix of sizes AxB times BxZ to produce AxZ matrix");

        float [][] newMatrix = new float[m1.length][m2[0].length];

        for(int i = 0; i < m1.length; i ++){
            for(int j = 0; j < m2[0].length; j ++){
                for(int k = 0; k < m1.length; k++){
                    newMatrix[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
        return newMatrix;
    }


    /***
     public float determinant(float [][] m){
     if(m.length == 1) return (int) m[0][0];
     //assuming m is already square
     if(m.length == 2) return (int) ((m[0][0] * m[1][1]) - (m[0][1]* m[1][0]));

     int result = 1;
     // upperTriangle(m);

     for(int i = 0; i < m.length; i++){
     result *= m[i][i];
     }

     if(result == 0) throw new IllegalArgumentException("Matrix determinant is 0, matrix is singular.");

     return result;
     }

     ***/
    static private void convertDecimalToFraction(double x, ArrayList<Double> numerators, ArrayList<Double> denominators){


        double tolerance = 1.0E-6;
        double h1=1; double h2=0;
        double k1=0; double k2=1;
        double b = x;
        do {
            double a = Math.floor(b);
            double aux = h1; h1 = a*h1+h2; h2 = aux;
            aux = k1; k1 = a*k1+k2; k2 = aux;
            b = 1/(b-a);
        } while (Math.abs(x-h1/k1) > x*tolerance);
        numerators.add(h1);
        denominators.add(k1);
    }

}
