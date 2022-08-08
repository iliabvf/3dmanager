package com.example.application.views.models;

import com.example.application.DataBasesController;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dnd.DropEvent;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.awt.*;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PageTitle("Models")
@Route(value = "models", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class ModelsView extends Main implements HasComponents, HasStyle {

    private OrderedList imageContainer;

    static private ModelsView modelsView;
    H2 header;

    TreeGrid<Projects> treeGrid;

    public H2 getHeader() {
        return header;
    }

    static public ModelsView getModelsView() {
        return modelsView;
    }

    public ModelsView() {
        constructUI();

//        imageContainer.add(new ModelsViewCard("Snow mountains under stars",
//                "https://images.unsplash.com/photo-1519681393784-d120267933ba?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=750&q=80"));
//        imageContainer.add(new ModelsViewCard("Snow covered mountain",
//                "https://images.unsplash.com/photo-1512273222628-4daea6e55abb?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=750&q=80"));
//        imageContainer.add(new ModelsViewCard("River between mountains",
//                "https://images.unsplash.com/photo-1536048810607-3dc7f86981cb?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=375&q=80"));
//        imageContainer.add(new ModelsViewCard("Milky way on mountains",
//                "https://images.unsplash.com/photo-1515705576963-95cad62945b6?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&auto=format&fit=crop&w=750&q=80"));
//        imageContainer.add(new ModelsViewCard("Mountain with fog",
//                "https://images.unsplash.com/photo-1513147122760-ad1d5bf68cdb?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80"));
//        imageContainer.add(new ModelsViewCard("Mountain at night",
//                "https://images.unsplash.com/photo-1562832135-14a35d25edef?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=815&q=80"));

        modelsView = this;
    }

    public void refreshImages(String parentFullPath){
        imageContainer.removeAll();

        String absoluteImagesPath = new File("img").getAbsolutePath();

        for (Map.Entry<MainLayout.PicFolder,String> entry : MainLayout.getMainLayout().getFilesMap().entrySet() ) {
            if(!entry.getValue().contains(parentFullPath)){
                continue;
            }

                File file = new File(entry.getKey().getFullPath());
//        File[] files = new File(parentFullPath).listFiles();
//        for (File file : files) {
//            if (!file.isDirectory()) {
                imageContainer.add(new ModelsViewCard(file.getName().replace(".jpg", "").replace(".png", "").replace(".jpeg", "")
                        //"https://images.unsplash.com/photo-1519681393784-d120267933ba?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=750&q=80")
                        ,file.getAbsolutePath().replace(absoluteImagesPath, "img")
//                        "frontend/img/Bathroom/Bathtub/Antonio Lupi SUITE.jpeg"
                        ,entry.getKey().getColor()
                ));
//                System.out.println("File: " + file.getAbsolutePath());
//                filesMap.put(file.getAbsolutePath(), parent.getAbsolutePath());

//            }
        }

    }

    private void constructUI() {
        addClassNames("models-view", "max-w-screen-lg", "mx-auto", "pb-l", "px-l");

        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames("items-center", "justify-between");

        VerticalLayout headerContainer = new VerticalLayout();

//        TreeGrid treeGrid = MainLayout.getMainLayout().getChildren().filter(component -> component instanceof TreeGrid).findFirst();

        header = new H2("Gallery");
        header.setId("header");
        header.addClassNames("mb-0", "mt-xl", "text-3xl");

        Paragraph description = new Paragraph("Royalty free photos and pictures, courtesy of Unsplash");
        description.addClassNames("mb-xl", "mt-0", "text-secondary");
        headerContainer.add(header, description);

//        Select<String> sortBy = new Select<>();
//        sortBy.setLabel("Sort by");
//        sortBy.setItems("Popularity", "Newest first", "Oldest first");
//        sortBy.setValue("Popularity");

        imageContainer = new OrderedList();
        imageContainer.addClassNames("gap-m", "grid", "list-none", "m-0", "p-0");

        container.add(header); //, sortBy);

        VerticalLayout leftContainer = new VerticalLayout();
//        leftContainer.addClassNames("flex", "flex-col", "flex-1", "overflow-y-auto");
        leftContainer.add(container,imageContainer);

        // Add right side container with projects
        VerticalLayout rightContainer = new VerticalLayout();
//        rightContainer.addClassNames("flex", "flex-col", "flex-1", "overflow-y-auto");
        rightContainer.setSizeFull();
        rightContainer.add(createTree());
        rightContainer.setAlignItems(HorizontalLayout.Alignment.END);
        rightContainer.setWidth("auto");

        rightContainer.setHeight(leftContainer.getHeight());

        DropTarget.create(rightContainer).addDropListener(new ComponentEventListener<DropEvent<VerticalLayout>>() {
            @Override
            public void onComponentEvent(DropEvent<VerticalLayout> verticalLayoutDropEvent) {
                if (verticalLayoutDropEvent.getDragSourceComponent().get() == null
                        || !(verticalLayoutDropEvent.getDragSourceComponent().get() instanceof ModelsViewCard)) {
                    return;
                }

                Dialog dialog = new Dialog();
                dialog.setCloseOnEsc(true);
                dialog.setCloseOnOutsideClick(true);
                dialog.setWidth("30em");
                dialog.setHeight("20em");
                dialog.setHeaderTitle("Please select a project");

                ComboBox<Projects> comboBox = new ComboBox<>();
                List<Projects> projects = new ArrayList<>();
                projects.add(new Projects(1,"Project 1"));
                projects.add(new Projects(2,"Project 2"));
                projects.add(new Projects(3,"Project 3"));
                comboBox.setItems(projects);

                Button button = new Button("Add");
                button.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                    @Override
                    public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                        if (comboBox.getValue() == null){
                            return;
                        }

                        ModelsViewCard card = (ModelsViewCard) verticalLayoutDropEvent.getDragSourceComponent().get();


                        try {
                            PreparedStatement stmt;
                            ResultSet rs = null;
                            String sql = "INSERT INTO projectFiles (name,project) VALUES (?,?)";
                            stmt = MainLayout.getMainLayout().getDataBasesController().getHsqlConnection().prepareStatement(sql);
                            stmt.setString(1, card.getText());
                            stmt.setInt(2, comboBox.getValue().getId());
                            stmt.execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        refreshProjectTree();

                        dialog.close();
                    }
                });

                dialog.add(comboBox,button);

                dialog.add();
                dialog.open();

            }
        });

        HorizontalLayout mainContainer = new HorizontalLayout();
//        mainContainer.addClassNames("flex", "flex-col", "flex-1", "overflow-y-auto");
        mainContainer.setWidth("100%");
        mainContainer.add(leftContainer, rightContainer);

        add(mainContainer);


    }

    void refreshProjectTree(){
        treeGrid.setItems(getStaff(null), this::getStaff);
    }

    public List<Projects> getStaff(Projects parent) {

        if (parent == null) {
//            for (Map.Entry<String,String> dir : dirsMap.entrySet()) {
//                if (dir.getValue() == null) {
//                    File file = new File(dir.getKey());
////                    folders.add(new MainLayout.Projects(file.getName(), dir.getKey()));
//                    folders.add(new Projects(file.getName(), dir.getKey()));
//                }
//            }
            List<Projects> folders = new ArrayList<>();
            folders.add(new Projects(1,"Project 1"));
            folders.add(new Projects(2,"Project 2"));
            folders.add(new Projects(3,"Project 3"));
            return folders;
        } else {
            List<Projects> folders = new ArrayList<>();
            if (!(parent instanceof ProjectFiles)){
                try {
                    PreparedStatement stmt;
                    ResultSet rs = null;
                    String sql = "SELECT * FROM projectFiles WHERE project = ?";
                    stmt = MainLayout.getMainLayout().getDataBasesController().getHsqlConnection().prepareStatement(sql);
                    stmt.setInt(1, parent.getId());
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        folders.add(new ProjectFiles(rs.getInt("id"),rs.getString("name")));
//                    picFolders.put(rs.getString("filePath"), new MainLayout.PicFolder(rs.getString("fileName"), rs.getString("filePath"), new Color(rs.getInt("red"), rs.getInt("green"), rs.getInt("blue"))));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return folders;


//            for (Map.Entry<String,String> dir : dirsMap.entrySet()) {
//                if (dir.getValue() != null && dir.getValue().equals(parent.getFullPath())) {
//                    File file = new File(dir.getKey());
//                    folders.add(new PicFolder(file.getName(), dir.getKey()));
//                }
//            }
        }
    }

    HashMap<String,String> dirsMap = null;
    HashMap<String, String> filesMap = new HashMap<>();
    private Component createTree(){
        treeGrid = new TreeGrid<>();
        treeGrid.setId("projectsTree");
//        treeGrid.getStyle().set("position", "fixed");
//        treeGrid.getStyle().set("display", "content");
        treeGrid.setItems(getStaff(null), this::getStaff);
        treeGrid.addHierarchyColumn(Projects::getName).setHeader("Projects");
        treeGrid.setSelectionMode(TreeGrid.SelectionMode.SINGLE);
        treeGrid.setWidth("10em");

//        treeGrid.addItemClickListener(new ComponentEventListener<ItemClickEvent<PicFolder>>() {
//            @Override
//            public void onComponentEvent(ItemClickEvent<PicFolder> PicFolderItemClickEvent) {
//                ModelsView.getModelsView().getHeader().setText(PicFolderItemClickEvent.getItem().getName());
//                ModelsView.getModelsView().refreshImages(PicFolderItemClickEvent.getItem().getFullPath());
//            }
//        });

//        treeGrid.addColumn(PicFolder::getfullPath).setHeader("Last name");
//        treeGrid.addColumn(PicFolder::getEmail).setHeader("Email");

        return treeGrid;
    }


}