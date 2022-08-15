package com.example.application.models;

import com.example.application.CardComponent;
import com.example.application.ColorUtils;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.GeneratedVaadinComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dnd.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.server.StreamResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ModelsViewCard extends ListItem {

    private String text;
    private String fileName;
    private int foundPicFolderId;
    private java.awt.Color colorTag;

    private Div divTags;

    public List<String> tagsList = new ArrayList<>();

    public String getText() {
        return text;
    }
    public String getFileName() {
        return fileName;
    }

    void fillDivTags(){
        divTags.removeAll();

        Span badge = new Span();
        badge.getElement().setAttribute("theme", "badge");
        badge.setText(text);

        Span colorBadge = new Span();
        colorBadge.getElement().setAttribute("theme", "badge");

        String resultStr = "";
        if (colorTag != null){
            resultStr = new ColorUtils().getColorNameFromRgb(colorTag.getRed(), colorTag.getGreen(), colorTag.getBlue());
            colorBadge.getStyle().set("background-color", "rgb(" + colorTag.getRed() + "," + colorTag.getGreen() + "," + colorTag.getBlue() + ")");
            colorBadge.setText(resultStr);

        }

        // Add tag button
        Button buttonAddTag = new Button();
        buttonAddTag.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonAddTag.setText("+Add");
        buttonAddTag.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                Dialog dialog = new Dialog();
                dialog.setHeaderTitle("Add tag");
                dialog.setCloseOnOutsideClick(true);
                dialog.setCloseOnEsc(true);
                dialog.setModal(true);
                dialog.setWidth("20em");
                dialog.setHeight("10em");

                HorizontalLayout horizontalLayout = new HorizontalLayout();

                ComboBox<String> newTagComboBox = new ComboBox<>();
                newTagComboBox.setPlaceholder("Please type tag name");

                tagsList.clear();
                try {
                    PreparedStatement stmt;
                    ResultSet rs = null;
                    String sql = "SELECT * FROM pictags WHERE name IS NOT NULL GROUP BY name ";
                    stmt = MainLayout.getMainLayout().getDataBasesController().getHsqlConnection().prepareStatement(sql);
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        tagsList.add(rs.getString("name"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (tagsList.size() == 0){
                    tagsList.add("Empty");
                }

                newTagComboBox.setItems(tagsList);
                newTagComboBox.setAllowCustomValue(true);
//                newTagComboBox.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<String>, String>>() {
//                    @Override
//                    public void valueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<String>, String> comboBoxStringComponentValueChangeEvent) {
////                        comboBoxStringComponentValueChangeEvent.
//                        int a = 0;
//                    }
//                });
                newTagComboBox.addCustomValueSetListener(new ComponentEventListener<GeneratedVaadinComboBox.CustomValueSetEvent<ComboBox<String>>>() {
                    @Override
                    public void onComponentEvent(GeneratedVaadinComboBox.CustomValueSetEvent<ComboBox<String>> comboBoxCustomValueSetEvent) {
                        tagsList.add(comboBoxCustomValueSetEvent.getDetail());
                        newTagComboBox.setItems(tagsList);
                        newTagComboBox.setValue(comboBoxCustomValueSetEvent.getDetail());
                    }
                });

                Button button = new Button();
                button.setText("Add");
                button.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                    @Override
                    public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                        if (newTagComboBox.getValue() == null || newTagComboBox.getValue().isEmpty()){
                            Notification.show("Please select tag");
                            return;
                        }
                        try {
                            PreparedStatement stmt;
                            ResultSet rs = null;
                            String sql = "INSERT INTO pictags (name,picFolder) VALUES (?,?)";
                            stmt = MainLayout.getMainLayout().getDataBasesController().getHsqlConnection().prepareStatement(sql);
                            stmt.setString(1, newTagComboBox.getValue());
                            stmt.setInt(2, foundPicFolderId);
                            stmt.execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        fillDivTags();

                        dialog.close();
                    }
                });
                horizontalLayout.add(newTagComboBox, button);
                dialog.add(horizontalLayout);
                dialog.open();
            }
        });

        divTags.add(badge, colorBadge);

        // Add custom tags
        try {
            PreparedStatement stmt;
            ResultSet rs = null;
            String sql = "SELECT * FROM pictags WHERE picFolder = ?";
            stmt = MainLayout.getMainLayout().getDataBasesController().getHsqlConnection().prepareStatement(sql);
            stmt.setInt(1, foundPicFolderId);
            rs = stmt.executeQuery();
            while (rs.next()) {
//                tagsList.add(rs.getString("name"));
                Span tagBadge = new Span();
                tagBadge.getElement().setAttribute("theme", "badge");
                tagBadge.setText(rs.getString("name"));
                divTags.add(tagBadge);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        divTags.add(buttonAddTag);


    }

    public ModelsViewCard(String fileName, String url, java.awt.Color colorTag, int foundPicFolderId) {
        this.fileName = fileName;
        this.text = fileName.replace(".jpg", "").replace(".png", "").replace(".jpeg", "").replace(".gif", "");
        this.foundPicFolderId = foundPicFolderId;
        this.colorTag = colorTag;

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

        divTags = new Div();
        divTags.addClassNames("flex", "flex-row", "flex-wrap", "justify-between", "items-center", "mb-m");
        fillDivTags();


        add(div, header
                //, subtitle, description
                ,divTags);

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
