public class DistractTheGuards {

    public static int solution (int [] banana_list ) {
        int n = banana_list.length;

        boolean [][] bpGraph = new boolean[n][n];



        //creating adjacency matrix, 0 for non-looping, 1 for looping
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(i == j) continue;
                if(doesLoop(banana_list[i],banana_list[j])){
                    bpGraph[i][j] = true;
                }
            }
        }

        int result = maxBPM(bpGraph);

        if(n % 2 == 1)
            return n - result + 1;

        return n - result;
    }


    static boolean bpm(boolean[][] bpGraph, int u, boolean[] visited, int[] matchJobs) {

        int N = bpGraph.length;

        for (int v = 0; v < N; v++)
        {

            if (bpGraph[u][v] && !visited[v])
            {


                visited[v] = true;


                if (matchJobs[v] < 0 || bpm(bpGraph, matchJobs[v],
                        visited, matchJobs))
                {
                    matchJobs[v] = u;
                    return true;
                }
            }
        }
        return false;
    }


    static int maxBPM(boolean[][] bpGraph)
    {
        int N = bpGraph.length;


        int[] matchJobs = new int[N];

        for(int i = 0; i < N; ++i)
            matchJobs[i] = -1;


        int result = 0;
        for (int u = 0; u < N; u++)
        {

            boolean[] visited = new boolean[N];
            for(int i = 0; i < N; ++i)
                visited[i] = false;

            if (bpm(bpGraph, u, visited, matchJobs))
                result++;
        }
        return result;
    }



    public static boolean doesLoop(int a, int b){
        boolean result = false;

        int totalBananas = a + b;
        int temp = totalBananas;
        while(temp % 2 == 0){
            temp /= 2;
        }
        if(a % temp != 0) result = true;

        return result;
    }

}
