import java.math.BigInteger;
import java.util.HashMap;

public class FuelInjectionPerfection {
    private static HashMap<BigInteger,Integer> memoMap = new HashMap<>();
    private static final BigInteger ONE = new BigInteger("1");

    public static int injection(String x){

        BigInteger input = new BigInteger(x);
        memoMap.put(new BigInteger("1"),0);
        memoMap.put(new BigInteger("2"), 1);

        return minimumOperations(input);
    }


    public static int minimumOperations(BigInteger input ) {

        Integer memoResult = memoMap.get(input);

        if(memoResult != null)
            return memoResult;

        int result;

        if(isEven(input)) {
            result = 1 + minimumOperations(input.shiftRight(1));
        }
        else {
            result =  1 + Math.min(minimumOperations(input.subtract(ONE)), minimumOperations(input.add(ONE)));
        }

        memoMap.put(input, result);

        return result;

    }

    public static boolean isEven(BigInteger x){
        //X XOR 1 == X + 1
        return (x.xor(new BigInteger("1")).equals( x.add(new BigInteger("1"))));
    }

    public static boolean isPowerOfTwo(BigInteger x){
        // x > 0 && (x AND x - 1) == 0
        BigInteger zero = new BigInteger("0");
        BigInteger one = new BigInteger("1");
        return (x.compareTo(zero) > 0 && (( x.and(x.subtract(one)).equals(zero))));
    }
}
