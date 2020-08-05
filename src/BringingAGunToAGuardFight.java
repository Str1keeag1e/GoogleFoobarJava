import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class BringingAGunToAGuardFight {

    public static final int[] UNIT_VECTOR_X = {1, 0};


    public static int solution(int[] dimensions, int[] your_position, int[] guard_position, int distance) {
        ArrayList<int[]> virtualYourPositions;
        ArrayList<int[]> virtualGuardPositions;
        HashMap<Double, int[]> anglePositions = new HashMap<>();


        int max_dimensionX = your_position[0] + distance;
        int max_dimensionY = your_position[1] + distance;

        double numOfReflectionsYAxis = Math.ceil((double) max_dimensionX / dimensions[0]);
        double numOfReflectionsXAxis = Math.ceil((double) max_dimensionY / dimensions[1]);

        virtualGuardPositions = virtualPositions(your_position, guard_position, dimensions, numOfReflectionsYAxis, numOfReflectionsXAxis, distance).get(0);
        virtualYourPositions = virtualPositions(your_position, guard_position, dimensions, numOfReflectionsYAxis, numOfReflectionsXAxis, distance).get(1);
/*
        System.out.println("------MAIN SOLUTION TESTING");
        for(int i [] : virtualGuardPositions){
            System.out.println(Arrays.toString(i));
        }
*/
        //Go through virtual guard position list which is sorted by distance from your_position
        //If hashmap anglePositions do not contain the angle AND does not hit a corner then add to list.
        for (int[] virtualGuardPosition : virtualGuardPositions) {

            //double angleKey = angleBetween(UNIT_VECTOR_X, subtractArrays(virtualGuardPosition, your_position));
            double angleKey = angleBetween(virtualGuardPosition, your_position);

            if (!anglePositions.containsKey(angleKey) && !doesHitCorner(your_position, virtualGuardPosition, dimensions)) {
               /* System.out.println("Angle is : " + Math.toDegrees(angleKey));
                System.out.println("Virtual Guard Position is + " + Arrays.toString(virtualGuardPosition));*/
                anglePositions.put(angleKey, virtualGuardPosition);
            }/*else {
                System.out.println("----------NOT ADDED---------");
                System.out.println("Angle is : " + Math.toDegrees(angleKey));
                System.out.println("Virtual Guard Position is + " + Arrays.toString(virtualGuardPosition));
                System.out.println(doesHitCorner(your_position,virtualGuardPosition,dimensions));
                System.out.println("----------------------------");

            }*/
        }

        //really shit code right here--This happened because I was being dumb when creating reflections--index 0 contains your_position which isn't virtual so I shouldn't have to worry about colliding into it.
        //atan2(0,0) produces 0 so it messes up calculations with other virtual Your_positions on the axis.
        virtualYourPositions.remove(0);


        for (int[] virtualYourPosition : virtualYourPositions) {
            //double angleKey = angleBetween(UNIT_VECTOR_X, subtractArrays(virtualYourPosition, your_position));
            double angleKey = angleBetween(virtualYourPosition, your_position);
            if (anglePositions.containsKey(angleKey) && (calculateDistance(anglePositions.get(angleKey), your_position) > calculateDistance(virtualYourPosition, your_position))) {
                Arrays.toString(anglePositions.remove(angleKey));
            }
        }

        return anglePositions.size();
    }

    /***
     //need to return array of virtual images created by plane mirrors,
     // as long as the distance between your_position and virtualGuardPositions is less than distance, we can hit guard.
     // only concern after that is--if we bounce into corner or hit ourselves before guard.
     NOTE: These positions inputted will be centered from (0,0) these will not be vector representations from your_position. we will handle that later.
     ***/


    public static ArrayList<ArrayList<int[]>> virtualPositions(int[] your_position, int[] guard_position, int[] dimensions, double numOfReflectionsYAxis, double numOfReflectionsXAxis, int distance) {
        ArrayList<ArrayList<int[]>> virtualPositions = new ArrayList<>();
        ArrayList<int[]> virtualGuardPositions = new ArrayList<>();
        ArrayList<int[]> virtualYourPositions = new ArrayList<>();
        ArrayList<int[]> mirrorPlanesYAxis = new ArrayList<>();
        ArrayList<int[]> mirrorPlanesXAxis = new ArrayList<>();


       /* //Printingout num reflects//DEBUG//DEBUG//DEBUG//DEBUG//DEBUG//DEBUG//DEBUG//DEBUG//DEBUG//DEBUG//DEBUG//DEBUG
        System.out.println("Number of reflections Y Axis " + numOfReflectionsYAxis);

        System.out.println("Number of reflections X Axis " + numOfReflectionsXAxis);
        //DEBUG//DEBUG//DEBUG//DEBUG//DEBUG//DEBUG//DEBUG//DEBUG//DEBUG//DEBUG//DEBUG//DEBUG//DEBUG//DEBUG//DEBUG//DEBUG
*/
        //add original coordinates to virtualGuardPos and virtualYourPos for easier use down the road

        virtualYourPositions.add(your_position);
        virtualGuardPositions.add(guard_position);


        //first need to get positions in the first quadrant
        //distance between mirror and object will define reflections. distance from mirror minus x = x2
        //we will multiply each one of those by the respective numOfReflectionsXAxis and numOfReflectionsYaxis


        //creating extra mirrors to reflect left to right
        for (int i = 1; i <= numOfReflectionsYAxis; i++) {
            int[] mirroredPlane = new int[2];
            mirroredPlane[0] = i * dimensions[0];
            mirroredPlane[1] = dimensions[1];
            mirrorPlanesYAxis.add(mirroredPlane);
        }
        //creating extra mirrors to reflect up and down
        for (int i = 1; i <= numOfReflectionsXAxis; i++) {
            int[] mirroredPlane = new int[2];
            mirroredPlane[0] = dimensions[0];
            mirroredPlane[1] = i * dimensions[1];
            mirrorPlanesXAxis.add(mirroredPlane);
        }

        //creating new coordinates for each one; depending on how many times reflected may have two different distances.

        //new positions with respect to X coordinates (reflect across y axis)
        for (int i = 0; i < numOfReflectionsYAxis; i++) {
            int[] mirroredYourPosition = new int[2];
            int[] mirroredGuardPosition = new int[2];


            if (i % 2 == 0) {
                mirroredYourPosition[0] = mirrorPlanesYAxis.get(i)[0] + (mirrorPlanesYAxis.get(0)[0] - your_position[0]);
                mirroredGuardPosition[0] = mirrorPlanesYAxis.get(i)[0] + (mirrorPlanesYAxis.get(0)[0] - guard_position[0]);

            } else {
                mirroredGuardPosition[0] = mirrorPlanesYAxis.get(i)[0] + guard_position[0];
                mirroredYourPosition[0] = mirrorPlanesYAxis.get(i)[0] + your_position[0];
            }
            //y position will stay same regardless--no need to put them in conditionals.
            mirroredGuardPosition[1] = guard_position[1];
            mirroredYourPosition[1] = your_position[1];


            virtualYourPositions.add(mirroredYourPosition);
            virtualGuardPositions.add(mirroredGuardPosition);

        }
        //now we just take the reflections we just did and do it for Y coordinates (reflect across x axis)
        for (int i = 0; i < numOfReflectionsXAxis; i++) {
            for (int j = 0; j < numOfReflectionsYAxis; j++) {
                int[] mirroredYourPosition = new int[2];
                int[] mirroredGuardPosition = new int[2];

                if (i % 2 == 0) {
                    mirroredYourPosition[1] = mirrorPlanesXAxis.get(i)[1] + mirrorPlanesXAxis.get(0)[1] - virtualYourPositions.get(0)[1];
                    mirroredGuardPosition[1] = mirrorPlanesXAxis.get(i)[1] + mirrorPlanesXAxis.get(0)[1] - virtualGuardPositions.get(0)[1];
                } else {
                    mirroredYourPosition[1] = mirrorPlanesXAxis.get(i)[1] + virtualYourPositions.get(0)[1];
                    mirroredGuardPosition[1] = mirrorPlanesXAxis.get(i)[1] + virtualGuardPositions.get(0)[1];
                }

                mirroredYourPosition[0] = virtualYourPositions.get(j)[0];
                mirroredGuardPosition[0] = virtualGuardPositions.get(j)[0];

                virtualYourPositions.add(mirroredYourPosition);
                virtualGuardPositions.add(mirroredGuardPosition);
            }
        }


        int n = (int) numOfReflectionsXAxis * (int) numOfReflectionsYAxis; //total number of reflections + 1 (in first quadrant);
        //Okay so we have done the first quadrant of reflections, now we need to reflect across quadrant II, III, and IV;
        //respectively, that is (-x,y),(-x,-y) and (x,-y)

        //doing Guard Positions
        for (int i = 0; i < n; i++) {
            int[] reflectionII = new int[2];
            int[] reflectionIII = new int[2];
            int[] reflectionIV = new int[2];

            reflectionII[0] = -1 * virtualGuardPositions.get(i)[0];
            reflectionII[1] = virtualGuardPositions.get(i)[1];


            reflectionIII[0] = -1 * virtualGuardPositions.get(i)[0];
            reflectionIII[1] = -1 * virtualGuardPositions.get(i)[1];


            reflectionIV[0] = virtualGuardPositions.get(i)[0];
            reflectionIV[1] = -1 * virtualGuardPositions.get(i)[1];


            virtualGuardPositions.add(reflectionII);
            virtualGuardPositions.add(reflectionIII);
            virtualGuardPositions.add(reflectionIV);
        }

        //now do Your Positions (could've done this in one loop, but easier to understand)
        for (int i = 0; i < n; i++) {
            int[] reflectionII = new int[2];
            int[] reflectionIII = new int[2];
            int[] reflectionIV = new int[2];

            reflectionII[0] = -1 * virtualYourPositions.get(i)[0];
            reflectionII[1] = virtualYourPositions.get(i)[1];


            reflectionIII[0] = -1 * virtualYourPositions.get(i)[0];
            reflectionIII[1] = -1 * virtualYourPositions.get(i)[1];


            reflectionIV[0] = virtualYourPositions.get(i)[0];
            reflectionIV[1] = -1 * virtualYourPositions.get(i)[1];


            virtualYourPositions.add(reflectionII);
            virtualYourPositions.add(reflectionIII);
            virtualYourPositions.add(reflectionIV);
        }

        //now filter out both lists if distance from i to your_position > distance
        //could've done this on input but easier to follow.. definitely slower though
        //We will also need to filter by angle--go to angleFilter function to see more.

        virtualGuardPositions.removeIf(i -> calculateDistance(i, your_position) > distance);
        virtualYourPositions.removeIf(i -> calculateDistance(i, your_position) > distance);

        virtualGuardPositions.sort(Comparator.comparingDouble(a -> calculateDistance(a, your_position)));
        virtualYourPositions.sort(Comparator.comparingDouble(a -> calculateDistance(a, your_position)));


        virtualPositions.add(virtualGuardPositions);
        virtualPositions.add(virtualYourPositions); //no longer need virtual mirror positions, can calculate distance bounced just using virtual positions + your_position. (assuming we did reflection and mirroring correctly).


        return virtualPositions;
    }

    public static boolean doesHitCorner(int[] your_position, int[] v, int[] dimensions) {

        int[] topLeftCorner = new int[]{0, dimensions[1]};
        int[] topRightCorner = new int[]{dimensions[0], dimensions[1]};
        int[] bottomRightCorner = new int[]{dimensions[0], 0};
        int[] bottomLeftCorner = new int[]{0, 0};

        //angle between vector v - your_position and topLeftCorner - your_position;
        return (angleBetween(v, your_position) == angleBetween(topLeftCorner, your_position) ||
                angleBetween(v, your_position) == angleBetween(topRightCorner, your_position) ||
                angleBetween(v, your_position) == angleBetween(bottomRightCorner, your_position) ||
                angleBetween(v, your_position) == angleBetween(bottomLeftCorner, your_position));
    }


    public static double angleBetween(int[] u, int[] v) {
        //return Math.atan2(twoArgCrossProduct(u, v), innerProduct(u,v));

        return Math.atan2(u[1] - v[1], u[0] - v[0]);
    }

    public static int[] subtractArrays(int[] a, int[] b) {
        if (a.length != b.length) throw new IllegalArgumentException("must be same length");

        for (int i = 0; i < a.length; i++) {
            a[i] -= b[i];
        }
        return a;
    }

    public static double twoArgCrossProduct(int[] u, int[] v) {
        if (u.length != v.length || u.length != 2)
            throw new IllegalArgumentException("Dimensions of vector must be same and must be size 2 for this fast crossProduct to work");
        return ((u[0] * v[1]) - (u[1] * v[0]));
    }


    public static double innerProduct(int[] u, int[] v) {
        if (u.length != v.length) throw new IllegalArgumentException("Dimensions of vector must be same");

        return u[0] * v[0] + u[1] * v[1];

    }

    public static double calculateDistance(int[] var1, int[] var2) {
        return Math.sqrt(Math.pow(var2[0] - var1[0], 2) + Math.pow(var2[1] - var1[1], 2));
    }


    public static double magnitude(int[] v) { //this is distance, make sure to subtract from total distance every bounce. probably won't need this.
        return Math.sqrt(Math.pow(v[0], 2) + Math.pow(v[1], 2));
    }


}