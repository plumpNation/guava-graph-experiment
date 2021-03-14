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
        MutableNetwork<Integer, MyEdge> network = NetworkBuilder
                .undirected()
                .allowsParallelEdges(true)
                .build();

        final MyEdge terryEdge = new MyEdge("terry");
        final MyEdge jeffEdge = new MyEdge("jeff");
        final MyEdge fredEdge = new MyEdge("fred");
        final MyEdge boomEdge = new MyEdge("boom");
        final MyEdge foobarEdge = new MyEdge("foobar");


        network.addEdge(1, 2, jeffEdge);
        network.addEdge(1, 2, terryEdge);
        network.addEdge(2, 3, fredEdge);
        network.addEdge(3, 4, boomEdge);
        network.addEdge(4, 5, foobarEdge);

        assertEquals(Set.of(2), network.adjacentNodes(1));

        // All edges attached to 1 and 2
        assertEquals(Set.of(jeffEdge, fredEdge), network.adjacentEdges(terryEdge));

        // All edges attached to 2 and 3
        assertEquals(Set.of(jeffEdge, terryEdge, boomEdge), network.adjacentEdges(fredEdge));

        // All edges attached to 4 and 5
        assertEquals(Set.of(boomEdge), network.adjacentEdges(foobarEdge));

        assertTrue(network.nodes().contains(1));
        assertTrue(network.nodes().contains(2));
        assertTrue(network.nodes().contains(3));
        assertTrue(network.nodes().contains(4));
        assertTrue(network.nodes().contains(5));
        assertFalse(network.nodes().contains(100));
    }
}