package com.example.application.views.models;

import com.example.application.CardComponent;
import com.example.application.ColorUtils;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dnd.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ModelsViewCard extends ListItem {

    public ModelsViewCard(String text, String url, java.awt.Color colorTag) {
        addClassNames("bg-contrast-5", "flex", "flex-col", "items-start", "p-m", "rounded-l");

        Div div = new Div();
        div.addClassNames("bg-contrast", "flex items-center", "justify-center", "mb-m", "overflow-hidden",
                "rounded-m w-full");
//        div.setHeight("160px");


        File file = new File(url);
//        System.out.println("Expecting to find file from " + file.getAbsolutePath());
        StreamResource streamResource = new StreamResource(file.getName(), () -> {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                // file not found
                e.printStackTrace();
            }
            return null;
        });

        Image image = new Image(streamResource, "alt text");
        image.setWidth("100%");

        //
//        Image image = new Image();
//        image.setWidth("100%");
//        image.setSrc(url);
//        image.setAlt(text);

        image.addClickListener(new ComponentEventListener<ClickEvent<Image>>() {
            @Override
            public void onComponentEvent(ClickEvent<Image> imageClickEvent) {
                Dialog dialog = new Dialog();
//                dialog.setWidth("100%");
                dialog.setHeight("100%");
                dialog.setCloseOnOutsideClick(true);
                dialog.setCloseOnEsc(true);
                dialog.setModal(true);

                Image image1 = new Image(streamResource, "alt text");
//                image1.setWidth("100%");
//                image1.setHeight("100%");
                image1.getStyle().set("max-width", "100%");
                image1.getStyle().set("height", "auto");

                Button button = new Button("Close");
                button.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                    @Override
                    public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                        dialog.close();
                    }
                });

                HorizontalLayout horizontalLayout = new HorizontalLayout();
                horizontalLayout.add(image1, button);
                horizontalLayout.setWidth("100%");
//                horizontalLayout.setHeight("-1");
                horizontalLayout.setAlignItems(HorizontalLayout.Alignment.CENTER);
                horizontalLayout.setAlignItems(VerticalLayout.Alignment.START);

                dialog.add(horizontalLayout);
                dialog.open();
            }
        });

        div.add(image);

        Span header = new Span();
        header.addClassNames("text-xl", "font-semibold");
        header.setText(text);

//        Span subtitle = new Span();
//        subtitle.addClassNames("text-s", "text-secondary");
//        subtitle.setText("Card subtitle");

//        Paragraph description = new Paragraph(
//                "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut.");
//        description.addClassName("my-m");

        Span badge = new Span();
        badge.getElement().setAttribute("theme", "badge");
        badge.setText(text);

        Span colorBadge = new Span();
        colorBadge.getElement().setAttribute("theme", "badge");

        String resultStr = new ColorUtils().getColorNameFromRgb(colorTag.getRed(), colorTag.getGreen(), colorTag.getBlue());

        colorBadge.getStyle().set("background-color", "rgb(" + colorTag.getRed() + "," + colorTag.getGreen() + "," + colorTag.getBlue() + ")");
        colorBadge.setText(resultStr);

        add(div, header
                //, subtitle, description
                , badge, colorBadge);

        // Drag
        DragSource<ListItem> dragSource = DragSource.create(this);

        // TEST
//        VerticalLayout columnAWrapper = new VerticalLayout();
//        columnAWrapper.addClassName("wrapper");
//        VerticalLayout columnA = new VerticalLayout();
//        columnA.addClassNames("column", "column-a");
//        columnAWrapper.add(new Label("Column A"), columnA);
//
//        VerticalLayout columnBWrapper = new VerticalLayout();
//        columnBWrapper.addClassName("wrapper");
//        VerticalLayout columnB = new VerticalLayout();
//        columnB.addClassNames("column", "column-b");
//        columnBWrapper.add((Component) new Label("Column B"), columnB);
//
//        Button button = new Button("Would you drag me to column B");
//        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//        button.setClassName("my-button");
//
//        DragSource<Button> dragSource = DragSource.create(button);
//        dragSource.addDragStartListener(event -> {
//            if (button.getParent().get() == columnA) {
//                columnB.setClassName("drop-here", true);
//                event.setDragData("From A");
//            } else {
//                columnA.setClassName("drop-here", true);
//                event.setDragData("From B");
//            }
//        });
//
//        dragSource.addDragEndListener(event -> {
//            columnA.setClassName("drop-here", false);
//            columnB.setClassName("drop-here", false);
//        });
//
//        DropTarget.create(columnA).addDropListener(this::onDrop);
//        DropTarget.create(columnB).addDropListener(this::onDrop);
//
//        columnA.add(button);
//        add(columnAWrapper, columnBWrapper);

//        // test
//        CardComponent box1 = new CardComponent();
//        box1.setHeight("4em");
//        box1.setWidth("4em");
//        CardComponent box2 = new CardComponent();
//        box2.setHeight("4em");
//        box2.setWidth("4em");
//
//        box1.setDragData("Queen of Hearts");
//        box2.setDragData(11);
//
//// Make box 1 draggable and store reference to the configuration object
//        DragSource<CardComponent> box1DragSource = DragSource.create(box1);
//        box1.getStyle().set("background-color", "red");
//
//// Access box 2 drag related configuration object without making it draggable
//        DragSource<CardComponent> box2DragSource = DragSource.configure(box2);
//        box2.getStyle().set("background-color", "green");
//// Make box 2 draggable
//        box2DragSource.setDraggable(true);
//
//        add(box1, box2);

    }

    private void onDrop(DropEvent<VerticalLayout> event) {
        if (event.getDragSourceComponent().isPresent()
//                && event.getDragSourceComponent().get() == button
        ) {
            String dragData = (String) event.getDragData().orElse("");
            Notification.show("You dropped " + dragData);
//            if ("From A".equals(dragData)
////                    && event.getComponent() == columnB
//            ) {
//                columnA.remove(button);
//                columnB.add(button);
//                button.setText("Good! I'd like to go back to column A");
//            } else if ("From B".equals(dragData)
////                    && event.getComponent() == columnA
//            ) {
//                columnB.remove(button);
//                columnA.add(button);
//                button.setText("Nice! I'd like to go back to column B");
//            }
        }
    }
}
