package com.plumpnation.graphexperiment;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GraphExperimentTest {

    @Test
    @SuppressWarnings("UnstableApiUsage") // Graph is in Beta
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

        assertEquals(Collections.emptySet(), graph.adjacentNodes(100));
    }
}