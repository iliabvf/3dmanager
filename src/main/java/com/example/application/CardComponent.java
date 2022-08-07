package com.example.application;

import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.html.Div;

public class CardComponent extends Div implements DragSource<CardComponent>, HasStyle {
    public CardComponent() {
        // all cards will be draggable by default
        setDraggable(true);
    }
    // all DragSource methods have default implementations

}
