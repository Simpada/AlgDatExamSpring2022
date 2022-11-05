package org.pg4200.exam2022;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class Ex02Test {

    private Ex02<String> createNetwork() {

        Ex02<String> graph = new Ex02<>();

        graph.addEdge("Node 1", "Node 2", graph.metro);
        graph.addEdge("Node 2", "Node 3", graph.metro);
        graph.addEdge("Node 3", "Node 7", graph.metro);
        graph.addEdge("Node 7", "Node 9", graph.metro);
        graph.addEdge("Node 9", "Node 10", graph.metro);
        graph.addEdge("Node 10", "Node 8", graph.metro);
        graph.addEdge("Node 8", "Node 4", graph.metro);
        graph.addEdge("Node 4", "Node 1", graph.metro);
        //graph.addEdge("Node 4", "Node 11", graph.metro);

        graph.addEdge("Node 5", "Node 1", graph.tram);
        graph.addEdge("Node 5", "Node 2", graph.tram);
        graph.addEdge("Node 5", "Node 4", graph.tram);
        graph.addEdge("Node 5", "Node 6", graph.tram);
        graph.addEdge("Node 5", "Node 8", graph.tram);
        graph.addEdge("Node 5", "Node 10", graph.tram);
        graph.addEdge("Node 6", "Node 2", graph.tram);
        graph.addEdge("Node 6", "Node 3", graph.tram);
        graph.addEdge("Node 6", "Node 7", graph.tram);
        graph.addEdge("Node 6", "Node 10", graph.tram);

        assertEquals(8, graph.getNumberOfVertices(graph.metro));
        assertEquals(9, graph.getNumberOfVertices(graph.tram));

        assertEquals(8, graph.getNumberOfEdges(graph.metro));
        assertEquals(10, graph.getNumberOfEdges(graph.tram));

        return graph;
    }

    @Test
    public void findPaths() {
        Ex02<String> graph = createNetwork();

        for (int i = 0; i < 1; i++) {

            String node1 = graph.selectNodeRandomly();
            String node2 = graph.selectNodeRandomly();

            List<String> path = graph.findPath(node1,node2);

            //System.out.println(path);

            assertTrue(path.size() >= 1);
            //assertTrue(path.size() <= 6);

        }
    }
}