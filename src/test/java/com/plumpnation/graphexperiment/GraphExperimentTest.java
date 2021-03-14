package com.plumpnation.graphexperiment;

import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("UnstableApiUsage") // Graph is in Beta
class GraphExperimentTest {

    @Test
    public void testImmutableGraph() {
        ImmutableGraph<Integer> graph =
                GraphBuilder.directed()
                        .<Integer>immutable()
                        .addNode(1)
                        .putEdge(1, 2)
                        .putEdge(2, 3) // also adds nodes 2 and 3 if not already present
                        .putEdge(2, 3) // no effect; Graph does not support parallel edges
                        .addNode(100)
                        .build();

        // nodes are returned as type Set<Integer>
        assertEquals(Set.of(2), graph.adjacentNodes(1));
        assertEquals(Set.of(1, 3), graph.adjacentNodes(2));
        assertEquals(Set.of(2), graph.adjacentNodes(3));

        assertTrue(graph.hasEdgeConnecting(1, 2));
        assertTrue(graph.hasEdgeConnecting(2, 3));

        assertFalse(graph.hasEdgeConnecting(1, 100));
        assertEquals(Collections.emptySet(), graph.adjacentNodes(100));

        assertTrue(graph.nodes().contains(1));
        assertTrue(graph.nodes().contains(2));
        assertTrue(graph.nodes().contains(3));
        assertTrue(graph.nodes().contains(100));
    }

    @Test
    public void testMutableGraph() {
        // Unlike the immutable graph, you build first...
        MutableGraph<Integer> graph = GraphBuilder.undirected().build();

        // ...then update the graph as you like.
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);
        graph.addNode(100);

        graph.putEdge(1, 2);
        graph.putEdge(1, 3);

        assertFalse(graph.hasEdgeConnecting(2, 3));

        assertTrue(graph.hasEdgeConnecting(1, 3));
        assertTrue(graph.hasEdgeConnecting(1, 2));

        assertFalse(graph.hasEdgeConnecting(1, 100));
        assertEquals(Collections.emptySet(), graph.adjacentNodes(100));

        assertTrue(graph.nodes().contains(1));
        assertTrue(graph.nodes().contains(2));
        assertTrue(graph.nodes().contains(3));
        assertTrue(graph.nodes().contains(100));
    }

    @Test
    public void testMutableNetwork() {
        MutableNetwork<MyNode, MyEdge> network = NetworkBuilder
                .undirected()
                .allowsParallelEdges(true)
                .build();

        final MyNode node1 = new MyNode(1, "d108fhsifshf");
        final MyNode node2 = new MyNode(2, "1doeufh10fsfq");
        final MyNode node3 = new MyNode(3, "8sfhpifgf17s");
        final MyNode node4 = new MyNode(4, "01ndsifs7wbj");
        final MyNode node5 = new MyNode(5, "bsyfg61vosjhb");
        final MyNode node100 = new MyNode(100, "2busdg710ubfu");

        final MyEdge edge1 = new MyEdge("terry");
        final MyEdge edge2 = new MyEdge("jeff");
        final MyEdge edge3 = new MyEdge("fred");
        final MyEdge edge4 = new MyEdge("boom");
        final MyEdge edge5 = new MyEdge("foobar");

        network.addEdge(node1, node2, edge2);
        network.addEdge(node1, node2, edge1);
        network.addEdge(node2, node3, edge3);
        network.addEdge(node3, node4, edge4);
        network.addEdge(node4, node5, edge5);

        assertEquals(Set.of(node2), network.adjacentNodes(node1));

        // Parallel edges connect these nodes, so we get 2 back
        assertEquals(Set.of(edge2, edge1), network.edgesConnecting(node1, node2));

        // All edges attached to 1 and 2
        assertEquals(Set.of(edge2, edge3), network.adjacentEdges(edge1));

        // All edges attached to 2 and 3
        assertEquals(Set.of(edge2, edge1, edge4), network.adjacentEdges(edge3));

        // All edges attached to 4 and 5
        assertEquals(Set.of(edge4), network.adjacentEdges(edge5));

        assertTrue(network.nodes().containsAll(Set.of(node1, node2, node3, node4, node5)));

        assertFalse(network.nodes().contains(node100));
    }
}