import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

public class PowerHungry {
    public static String solution1(int [] xs) {

        ArrayList<Integer> negValues = new ArrayList<>();
        ArrayList<Integer> posValues = new ArrayList<>();
        ArrayList<Integer> zeroValues = new ArrayList<>();

        if(xs.length == 1 ) return String.valueOf(xs[0]);

        for (int i : xs) {
            if (i == 0) zeroValues.add(i);

            if (i < 0) negValues.add(i);

            if (i > 0) posValues.add(i);
        }

        Collections.sort(negValues);
        Collections.sort(posValues);

        //if size of negative values is odd, we will always get negative--must remove smallest negative entry.
        if(negValues.size() % 2 == 1  && posValues.size() == 0 && zeroValues.size() > 0)
            return "0";
        if(negValues.size() % 2 == 1  && (negValues.size() != posValues.size()) )
            negValues.remove(negValues.size()-1);



        if(negValues.size() == 1 && negValues.size() == posValues.size()) return posValues.get(0).toString();


        /*if we haven't modified total entries from input array, then we have to choose to remove smallest multiplier in one of the lists to remove.
        In this case, if negValues[k-2] * negValues [k-1] > posValues[0] then we can remove posValues[0] to get a valid subset.
        INVERSE: if negValues[k-2] * negValues [k-1] < posValues[0] then we can remove negValues[k-2] * negValues [k-1] to get a valid subset.
         */
        if( (negValues.size() + posValues.size() == xs.length) && negValues.size() > 1 && posValues.size() != 0  )
            if ((negValues.get(negValues.size() - 2) * negValues.get(negValues.size() - 1) >= posValues.get(0))) {
                posValues.remove(0);
            } else {
                negValues.remove(negValues.size() - 2);
                negValues.remove(negValues.size() - 1);
            }

        return maxProductBigInt(negValues, posValues).toString();
    }

    public static BigInteger maxProductBigInt(ArrayList<Integer> negValues, ArrayList<Integer> posValues){

        BigInteger maxProduct = new BigInteger("1");

        for (int i = 0; i < posValues.size(); i++) {
            maxProduct = maxProduct.multiply(BigInteger.valueOf(posValues.get(i)));
        }

        for (int i = 0; i < negValues.size(); i++) {
            maxProduct = maxProduct.multiply(BigInteger.valueOf(negValues.get(i)));
        }

        return maxProduct;
    }
}
