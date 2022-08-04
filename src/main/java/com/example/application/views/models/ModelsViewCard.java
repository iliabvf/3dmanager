package com.example.application.views.models;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.server.StreamResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ModelsViewCard extends ListItem {

    public ModelsViewCard(String text, String url) {
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
                dialog.setWidth("60em");
                dialog.setHeight("50em");
                dialog.setCloseOnOutsideClick(true);
                dialog.setCloseOnEsc(true);
                dialog.setModal(true);

                Image image1 = new Image(streamResource, "alt text");
                image1.setWidth("100%");
                image1.setHeight("100%");

                dialog.add(image1);
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

        add(div, header
                //, subtitle, description
                , badge);

    }
}
