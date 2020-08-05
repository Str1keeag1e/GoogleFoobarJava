import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MinionLaborShifts {
    public static int [] solution(int[] data, int n){
        if(data.length >= 100) throw new IllegalArgumentException();

        LinkedHashMap<Integer,Integer> listOfTasks = new LinkedHashMap<>();

        for(int key : data){
            if(listOfTasks.containsKey(key)){
                int count = listOfTasks.get(key);
                count++;
                listOfTasks.put(key, count);
            }else{
                listOfTasks.put(key,1);
            }
        }

        listOfTasks.entrySet().removeIf(entry -> entry.getValue() <= n);

        ArrayList<Integer> listOfData = new ArrayList<>();

        for(int i : data){
            listOfData.add(i);
        }

        listOfData.removeAll(listOfTasks.keySet());

        return listOfData.stream().mapToInt(Integer::intValue).toArray();

    }
}