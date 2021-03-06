package com.vaadin.graph;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.*;

import org.apache.commons.collections15.Transformer;

import com.vaadin.graph.client.*;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.graph.Graph;

class LayoutEngine {
    static void layout(Graph<NodeProxy, ArcProxy> graph, final int width,
                       final int height, Collection<NodeProxy> lockedNodes) {
        Dimension size = new Dimension(width, height);
        FRLayout<NodeProxy, ArcProxy> layout = new FRLayout<NodeProxy, ArcProxy>(graph,
                                                                                 size);
        layout.lock(false);
        for (NodeProxy v : lockedNodes) {
            layout.lock(v, true);
        }

        layout.setInitializer(new Transformer<NodeProxy, Point2D>() {
            public Point2D transform(NodeProxy input) {
                int x = input.getX();
                int y = input.getY();
                return new Point2D.Double(x == -1 ? new Random().nextInt(width) : x,
                                          y == -1 ? new Random().nextInt(height) : y);
            }
        });

        layout.initialize();
        while (!layout.done()) {
            layout.step();
        }
        for (NodeProxy v : graph.getVertices()) {
            Point2D location = layout.transform(v);
            v.setX((int) location.getX());
            v.setY((int) location.getY());
        }
    }
}
