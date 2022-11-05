package org.pg4200.exam2022;


import org.pg4200.ex09Mine.UndirectedGraph;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Ex02<V> extends UndirectedGraph<V> {

    protected Map<V, Set<V>> metro = new HashMap<>();
    protected Map<V, Set<V>> tram = new HashMap<>();

    protected Map<V, Set<V>> secondGraph = new HashMap<>();

    /** Assumptions
     * - That you can only ever switch transport type once
     * - That the fastest path to the target is what is desired and that only one path is desired
     * - That the layout of the networks may vary, so the graphs are not given values
     * - That the metro and tram maps will contain the actual data of the vertices
     *
     * Explanation
     * I create two graphs, metro and tram, they are used to hold the network/graph with all connections
     * Then I check of either of the maps has the keys, making sure both a start and an end is found
     * Then I create the path, which will contain the route, later to be returned
     * Then I check if I'm already at my destination, if so, I just return the path with the one destination
     * Then I check where to start, I start default at metro, but if I can't find start there, I start with tram
     * I create a stack, to hold my path elements to check through the graph
     * Then I create an array to hold all the paths I find before starting the search, using the main graph as input
     * Then I start moving through the graph, mapping all possible routes to the end, and if a node in first graph
     * is shared by second graph, I jump into the second and look there as well. This is a one way jump, and it
     * cannot jump back to the first graph.
     * After I have gotten a list of all possible routes from start to end, I iterate through them, only keeping
     * the shortest route, before returning the value.
     */

    public V selectNodeRandomly() {
        List<V> keys;
        Random rand = new Random();

        // Added this to combine nodes from both graphs so the randomizer could give any point from any graph
        Map<V, Set<V>> combined = Stream.of(metro, tram)
                .flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (value1, value2) -> value1
                ));

        keys = List.copyOf(combined.keySet()); // convert the key set into a list

        int sel = rand.nextInt(keys.size()); // select a random number between 0 and the max size of the list
        return keys.get(sel); // return the entry for the randomly selected number
    }

    public List<V> findPath(V start, V end) {

        if ( (!metro.containsKey(start) && !tram.containsKey(start)) || (!metro.containsKey(end) && !tram.containsKey(end)) ) {
            return Collections.emptyList();
        }

        List<V> path = new ArrayList<>();

        if (start.equals(end)) {
            path.add(start);
            return path;
        }

        Map<V, Set<V>> firstGraph = metro;
        secondGraph = tram;

        if (!metro.containsKey(start)) {
            firstGraph = tram;
            secondGraph = metro;
        }

        /*
         Step 1: Start at Metro, search metro for start, if it can't find start vertex, start at tram
         Step 2: Start finding path to end, and at all shared vertex, jump to other graph, BUT ONLY ONCE, NO JUMP BACK
         Step 3: Isolate fastest found route
         */

        Deque<V> stack = new ArrayDeque<>();

        List<List<V>> allPaths = new ArrayList<>();
        findAllPaths(allPaths, stack, start, end, firstGraph, true);
        path = allPaths.get(0);

        System.out.println(allPaths);

        for (List<V> p : allPaths) {
            if (p.size() < path.size()) {
                path = p;
            }
        }

        return path;
    }

    private void findAllPaths(List<List<V>> paths, Deque<V> stack, V current, V end, Map<V, Set<V>> graph, Boolean onSecond) {

        stack.push(current);

        if (isPathTo(stack, end)) {
            List<V> path = new ArrayList<>(stack);
            Collections.reverse(path);
            paths.add(path);
            return;
        }

        for (V connected : getAdjacents(current, graph)) {

            if (stack.contains(connected)) {
                continue;
            }

            if (secondGraph.containsKey(current) && secondGraph.containsKey(end) && onSecond) {
                stack.pop();
                findAllPaths(paths, stack, current, end, secondGraph, false);
            }

            findAllPaths(paths, stack, connected, end, graph, onSecond);
            stack.pop();
        }
    }

    public Collection<V> getAdjacents(V vertex, Map<V, Set<V>> graph) {

        Objects.requireNonNull(vertex);

        return graph.get(vertex);
    }


    /**
     * Everything below here is customized code from <UndirectedGraph.java> to allow me to specify which of the two graphs to affect
     * When making test, use the following to add Edges and Vertices to the tram and metro graphs, using them as arguments
     */

    public void addVertex(V vertex, Map<V, Set<V>> graph) {
        Objects.requireNonNull(vertex);

        graph.putIfAbsent(vertex, new HashSet<>());
    }

    public void addEdge(V from, V to, Map<V, Set<V>> graph) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);

        addVertex(from, graph);
        addVertex(to, graph);

        graph.get(from).add(to);

        if(! from.equals(to)) {
            //ie, if not a self-loop
            graph.get(to).add(from);
        }
    }

    public int getNumberOfVertices(Map<V, Set<V>> graph) {
        return graph.size();
    }

    public int getNumberOfEdges(Map<V, Set<V>> graph) {
        long edges =  graph.values().stream()
                .mapToInt(s -> s.size())
                .sum();

        /*
            As each edge is represented by 2 connections,
            we need to divide by 2.
            Ie, if edge X-Y, we have 1 connection from
            X to Y, and 1 from Y to X.

            However, we also have to consider self-loops X-X
         */

        edges += graph.entrySet().stream()
                .filter(e -> e.getValue().contains(e.getKey()))
                .count();

        return (int) edges / 2;
    }

    public void removeEdge(V from, V to, Map<V, Set<V>> graph) {

        Objects.requireNonNull(from);
        Objects.requireNonNull(to);

        Set<V> connectedFrom = graph.get(from);
        Set<V> connectedTo = graph.get(to);

        if(connectedFrom != null){
            connectedFrom.remove(to);
        }
        if(connectedTo != null){
            connectedTo.remove(from);
        }
    }

    public void removeVertex(V vertex, Map<V, Set<V>> graph) {

        Objects.requireNonNull(vertex);

        if(! graph.containsKey(vertex)){
            //nothing to do
            return;
        }

        /*
            Before we can remove the vertex, we have to
            remove all other connections to such vertex in the
            other vertices.
            Note: we can call "forEach" directly on a collection
            without opening a stream() first.
         */
        graph.get(vertex).forEach(v -> graph.get(v).remove(vertex));

        graph.remove(vertex);
    }


}
