package com.vaadin.graph;

import java.util.HashSet;
import java.util.Set;

import com.vaadin.graph.GraphLoader.NodeSelector;
import com.vaadin.graph.client.ClientVertex;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

final class ShowHandler implements ClickListener {
    private static final long serialVersionUID = 1L;

    private final Window dialog;
    private final String groupId;
    private final NodeSelector selector;
    private final GraphExplorer parent;

    ShowHandler(GraphExplorer parent, NodeSelector selector, String groupId,
            Window dialog) {
        this.parent = parent;
        this.selector = selector;
        this.groupId = groupId;
        this.dialog = dialog;
    }

    public void buttonClick(ClickEvent event) {
        dialog.getParent().removeWindow(dialog);
        parent.graphLoader.loadMembers(parent.graph, groupId,
                selector.getSelectedNodeIds());
        Set<ClientVertex> lockedVertices = new HashSet<ClientVertex>();
        if (!parent.graph.containsVertex(groupId)) {
            parent.removedId = groupId;
        } else {
            lockedVertices.add(parent.graph.getVertex(groupId));
        }
        for (ClientVertex v : parent.graph.getVertices()) {
            if (ClientVertex.EXPANDED.equals(v.getState())) {
                lockedVertices.add(v);
            }
        }
        parent.graph.layout(parent.clientWidth, parent.clientHeight,
                lockedVertices);
        parent.requestRepaint();
    }
}