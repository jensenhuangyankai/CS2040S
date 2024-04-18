import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class TSPGraph implements IApproximateTSP {

    @Override
    public void MST(TSPMap map) {
        HashSet<Integer> visited = new HashSet<>();
        HashMap<Integer, Number[]> distances = new HashMap<>(); //node id, [parent_node_id, distance]

        TreeMapPriorityQueue<Double, Integer> pq = new TreeMapPriorityQueue<>();
        for (int i = 0; i < map.getCount() ; i++) {
            distances.put(i, new Number[]{0, Double.POSITIVE_INFINITY});
            pq.add(i, Double.POSITIVE_INFINITY);
        }

        pq.decreasePriority(0, 0.0);

        while (!pq.isEmpty()) {
            Integer smallest = pq.extractMin();
            TSPMap.Point smallestp = map.getPoint(smallest);
            visited.add(smallest);

            for (int i = 0; i < map.getCount() ; i++) {
                if (visited.contains(i)) continue;

                TSPMap.Point p = map.getPoint(i);
                double newDist = smallestp.distance(p);

                if (newDist < (Double) distances.get(i)[1] ) {
                    distances.put(i, new Number[]{smallest, newDist});
                }
                pq.decreasePriority(i, newDist);
            }


        }


        for (int i = 0; i < distances.size() ;i++) {
            map.setLink(i, (int) distances.get(i)[0]);
        }


    }



    @Override
    public void TSP(TSPMap map) {
        MST(map);

        HashSet<Integer> visited = new HashSet<>();
        HashMap<Integer, HashSet<Integer>> nodes = new HashMap<>(); //child_node_id : [connected nodes] for undirected graph.
        Stack<Integer> stack = new Stack<>();

        for (int i =0; i < map.getCount(); i++) { //undirected graph
            HashSet<Integer> newArr = nodes.getOrDefault(i, new HashSet<>());
            newArr.add(map.getLink(i));
            nodes.put(i, newArr);

            newArr = nodes.getOrDefault(map.getLink(i), new HashSet<>());
            newArr.add(i);
            nodes.put(map.getLink(i), newArr);
        }
        //System.out.println(nodes);
        for (int i = 0; i < map.getCount(); i++) map.eraseLink(i);

        HashMap<Integer, Integer> path = new HashMap<>(); //old : new node
        stack.add(0);
        while (!stack.isEmpty()){
            Integer num = stack.pop();
            for (Integer i: nodes.get(num)) {
                if (!visited.contains(i)) {
                    stack.add(i);
                    //System.out.println("num:" + num + "child:" + i);
                    //if (!path.containsKey(num)) path.put(num, i);
                    //else path.put(i, num);
                    if (map.getLink(i) == -1) map.setLink(i, num);
                    else map.setLink(num, i);

                }
            }
            visited.add(num);

        }



        //System.out.println(path);
        //Integer last = 0;
        //for (Integer key: path.keySet()) {

        //    map.setLink(key, path.get(key));
            //last = path.get(key);
        //}
        map.setLink( 0,4);

        for (int i = 0; i < map.getCount(); i++) System.out.println("i:" + i + " child: " + map.getLink(i));

        //System.out.println("done");
    }

    @Override
    public boolean isValidTour(TSPMap map) {
        // Note: this function should with with *any* map, and not just results from TSP().
        // TODO: implement this method
        Integer counter = 1;
        Integer nextPoint = map.getLink(1);
        while (nextPoint != 0) {
            nextPoint = map.getLink(nextPoint);
            counter++;
        }
        counter++;
        System.out.println(counter);
        if (counter != map.getCount() -1) {
            return false;
        }
        else return true;
    }

    @Override
    public double tourDistance(TSPMap map) {
        // Note: this function should with with *any* map, and not just results from TSP().
        // TODO: implement this method
        return 0;
    }

    public static void main(String[] args) {
        TSPMap map = new TSPMap(args.length > 0 ? args[0] : "tenpoints.txt");
        TSPGraph graph = new TSPGraph();

        //graph.MST(map);
        graph.TSP(map);

        //  map.redraw();
        System.out.println(graph.isValidTour(map));
        // System.out.println(graph.tourDistance(map));
    }
}
