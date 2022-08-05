package com.example.application.views;


import com.example.application.ColorThief;
import com.example.application.MMCQ;
import com.example.application.components.appnav.AppNav;
import com.example.application.components.appnav.AppNavItem;
import com.example.application.views.models.ModelsView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.PageTitle;
import org.apache.xmlbeans.impl.xb.xsdschema.WhiteSpaceDocument;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H1 viewTitle;
    static private MainLayout mainLayout;

    HashMap<String,String> dirsMap = null;
    HashMap<PicFolder, String> filesMap = new HashMap<>();

    public HashMap<PicFolder, String> getFilesMap() {
        return filesMap;
    }

    static public MainLayout getMainLayout() {
        return mainLayout;
    }

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        addToDrawer(createDrawerContent());
        mainLayout = this;
    }

    private Component createHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addClassNames("view-toggle");
        toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames("view-title");

        Select<String> sortBy = new Select<>();
//        sortBy.setLabel("Sort by");
        sortBy.setItems("Popularity", "Newest first", "Oldest first");
        sortBy.setValue("Popularity");

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(new Label("Sort by:"),sortBy);
        horizontalLayout.setPadding(true);
        horizontalLayout.setMargin(false);
        horizontalLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        Header header = new Header(toggle, viewTitle, horizontalLayout);
        header.addClassNames("view-header");
        return header;
    }

    private Component createDrawerContent() {
        H2 appName = new H2("3D Manager");
        appName.addClassNames("app-name");

        com.vaadin.flow.component.html.Section section = new com.vaadin.flow.component.html.Section(appName
//                , createNavigation()
                , createTree()
                , createFooter());
        section.addClassNames("drawer-section");
        return section;
    }

    private AppNav createNavigation() {
        AppNav nav = new AppNav();
        nav.addClassNames("app-nav");

        nav.addItem(new AppNavItem("Models", ModelsView.class, "la la-th-list"));

        return nav;
    }

    public class PicFolder{
        String name;
        String fullPath;
        String color;

        public PicFolder(String name, String fullPath, String color) {
            this.name = name;
            this.fullPath = fullPath;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public String getFullPath() {
            return fullPath;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

    public List<PicFolder> getStaff(PicFolder parent) {
        List<PicFolder> folders = new ArrayList<>();

        if (parent == null) {
            for (Map.Entry<String,String> dir : dirsMap.entrySet()) {
                if (dir.getValue() == null) {
                    File file = new File(dir.getKey());
                    folders.add(new PicFolder(file.getName(),dir.getKey(),null));
                }
            }
        } else {
            for (Map.Entry<String,String> dir : dirsMap.entrySet()) {
                if (dir.getValue() != null && dir.getValue().equals(parent.getFullPath())) {
                    File file = new File(dir.getKey());
                    folders.add(new PicFolder(file.getName(),dir.getKey(),null));
                }
            }
        }
        return folders;
    }

    public void findFiles(File curFile, File parent) {
        File[] files = curFile.listFiles();

        if (dirsMap == null) {
            dirsMap = new HashMap<>();
        }

        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println("Directory: " + file.getAbsolutePath());
                findFiles(file,file); // Calls same method again.
                dirsMap.put(file.getAbsolutePath(), parent == null ? null : parent.getAbsolutePath());
            } else {
                System.out.println("File: " + file.getAbsolutePath());
                filesMap.put(new PicFolder(file.getName(),file.getAbsolutePath(),getImageColor(file.getPath())), parent.getAbsolutePath());
            }
        }
    }

    private Component createTree(){

        File dir = new File("img");
        try{
            filesMap = new HashMap<>();
            dirsMap = null;
            findFiles(dir,null);
        }catch(Exception e){
            e.printStackTrace();
        }

        TreeGrid<PicFolder> treeGrid = new TreeGrid<>();
        treeGrid.setItems(getStaff(null), this::getStaff);
        treeGrid.addHierarchyColumn(PicFolder::getName).setHeader("Folders");
        treeGrid.setSelectionMode(TreeGrid.SelectionMode.SINGLE);

        treeGrid.addItemClickListener(new ComponentEventListener<ItemClickEvent<PicFolder>>() {
            @Override
            public void onComponentEvent(ItemClickEvent<PicFolder> PicFolderItemClickEvent) {
                ModelsView.getModelsView().getHeader().setText(PicFolderItemClickEvent.getItem().getName());
                ModelsView.getModelsView().refreshImages(PicFolderItemClickEvent.getItem().getFullPath());
            }
        });

//        treeGrid.addColumn(PicFolder::getfullPath).setHeader("Last name");
//        treeGrid.addColumn(PicFolder::getEmail).setHeader("Email");

        return treeGrid;
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        layout.addClassNames("app-nav-footer");

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

    private String getImageColor(String imagePath){
        BufferedImage img1 = null;
        try {
            img1 = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (img1 == null) {
            return "";
        }
//        MMCQ.CMap result = ColorThief.getColorMap(img1, 10);
        int[] result = ColorThief.getColor(img1);
        return result[0] + "," + result[1] + "," + result[2];
    }

}
