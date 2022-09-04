package com.example.application.views;


import com.example.application.ColorThief;
import com.example.application.ColorUtils;
import com.example.application.DataBasesController;
import com.example.application.components.appnav.AppNav;
import com.example.application.components.appnav.AppNavItem;
import com.example.application.models.ModelsView;
import com.example.application.models.SearchItem;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.GeneratedVaadinComboBox;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.PageTitle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.List;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H1 viewTitle;
    static private MainLayout mainLayout;

    HashMap<String,String> dirsMap = null;
    HashMap<PicFolder, String> filesMap = new HashMap<>();

    static private DataBasesController dataBasesController;

    private HashMap<String,PicFolder> picFolders;

    public DataBasesController getDataBasesController() {
        return dataBasesController;
    }

    public HashMap<PicFolder, String> getFilesMap() {
        return filesMap;
    }

    static public MainLayout getMainLayout() {
        return mainLayout;
    }

    public MainLayout() {
        mainLayout = this;
        dataBasesController = new DataBasesController();

        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        addToDrawer(createDrawerContent());
    }

    private Component createHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addClassNames("view-toggle");
        toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        ComboBox<SearchItem> searchComboBox = new ComboBox<>();
        searchComboBox.setPlaceholder("Search here tags, models, colors, etc...");
        searchComboBox.setAllowCustomValue(true);

        List<SearchItem> searchList = new ArrayList<>();
        try {
            PreparedStatement stmt;
            ResultSet rs = null;
            String sql = "SELECT name,id FROM pictags WHERE name IS NOT NULL GROUP BY name ";
            stmt = MainLayout.getMainLayout().getDataBasesController().getHsqlConnection().prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                searchList.add(new SearchItem(rs.getString("name"),rs.getInt("id"),"pictags"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            PreparedStatement stmt;
            ResultSet rs = null;
            String sql = "SELECT fileName,id FROM picfolder WHERE fileName IS NOT NULL GROUP BY fileName ";
            stmt = MainLayout.getMainLayout().getDataBasesController().getHsqlConnection().prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                searchList.add(new SearchItem(rs.getString("fileName"),rs.getInt("id"),"picfolder"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            PreparedStatement stmt;
            ResultSet rs = null;
            String sql = "SELECT color,id FROM picfolder WHERE color IS NOT NULL GROUP BY color ";
            stmt = MainLayout.getMainLayout().getDataBasesController().getHsqlConnection().prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                searchList.add(new SearchItem(rs.getString("color"),rs.getInt("id"),"picfolder"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        searchComboBox.setItems(searchList);

        searchComboBox.addCustomValueSetListener(new ComponentEventListener<GeneratedVaadinComboBox.CustomValueSetEvent<ComboBox<SearchItem>>>() {
            @Override
            public void onComponentEvent(GeneratedVaadinComboBox.CustomValueSetEvent<ComboBox<SearchItem>> comboBoxCustomValueSetEvent) {
                ModelsView.getModelsView().refreshImagesFromSearch(comboBoxCustomValueSetEvent.getDetail());
            }
        });
        searchComboBox.addValueChangeListener(new HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<SearchItem>, SearchItem>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<SearchItem>, SearchItem> comboBoxStringComponentValueChangeEvent) {
                ModelsView.getModelsView().refreshImagesFromSearch(comboBoxStringComponentValueChangeEvent.getValue() == null ? "" : comboBoxStringComponentValueChangeEvent.getValue());
            }
        });

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

        Header header = new Header(toggle, searchComboBox, horizontalLayout);
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
        Color color;
        int id;

        public PicFolder(String name, String fullPath, Color color, int id) {
            this.name = name;
            this.fullPath = fullPath;
            this.color = color;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public String getFullPath() {
            return fullPath;
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public int getId() {
            return id;
        }
    }

    public List<PicFolder> getStaff(PicFolder parent) {
        List<PicFolder> folders = new ArrayList<>();

        if (parent == null) {
            for (Map.Entry<String,String> dir : dirsMap.entrySet()) {
                if (dir.getValue() == null) {
                    File file = new File(dir.getKey());
                    folders.add(new PicFolder(file.getName(),dir.getKey(),null,0));
                }
            }
        } else {
            for (Map.Entry<String,String> dir : dirsMap.entrySet()) {
                if (dir.getValue() != null && dir.getValue().equals(parent.getFullPath())) {
                    File file = new File(dir.getKey());
                    folders.add(new PicFolder(file.getName(),dir.getKey(),null,0));
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
                Color foundColor = null;

                PicFolder foundPicFolder = picFolders.get(file.getAbsolutePath());
                int foundPicFolderId = 0;
                if (foundPicFolder != null) {
                    foundColor = foundPicFolder.getColor();
                    foundPicFolderId = foundPicFolder.getId();
                }

                if (foundColor == null) {
                    foundColor = getImageColor(file.getPath());
                    if (foundColor != null) {
                        picFolders.put(file.getAbsolutePath(), new PicFolder(file.getName(),file.getAbsolutePath(),foundColor,foundPicFolderId));
                        try {
                            PreparedStatement stmt;
                            ResultSet rs = null;
                            String sql = "INSERT INTO picFolder (fileName,filePath,red,green,blue,color) VALUES (?,?,?,?,?,?)";
                            stmt = dataBasesController.getHsqlConnection().prepareStatement(sql);
                            stmt.setString(1, file.getName());
                            stmt.setString(2, file.getAbsolutePath());
                            stmt.setInt(3, foundColor.getRed());
                            stmt.setInt(4, foundColor.getGreen());
                            stmt.setInt(5, foundColor.getBlue());
                            stmt.setString(6, new ColorUtils().getColorNameFromRgb(foundColor.getRed(), foundColor.getGreen(), foundColor.getBlue()));
                            stmt.execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                filesMap.put(new PicFolder(file.getName(),file.getAbsolutePath(),foundColor,foundPicFolderId), parent.getAbsolutePath());

            }
        }
    }

    private Component createTree(){
        picFolders = new HashMap<>();

        try {
            PreparedStatement stmt;
            ResultSet rs = null;
            String sql = "SELECT * FROM picFolder";
            stmt = dataBasesController.getHsqlConnection().prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                picFolders.put(rs.getString("filePath"), new PicFolder(rs.getString("fileName"), rs.getString("filePath"), new Color(rs.getInt("red"), rs.getInt("green"), rs.getInt("blue")), rs.getInt("id")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    private Color getImageColor(String imagePath){
        BufferedImage img1 = null;
        try {
            img1 = ImageIO.read(new File(imagePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (img1 == null) {
            return null;
        }
//        MMCQ.CMap result = ColorThief.getColorMap(img1, 10);
        int[] result = ColorThief.getColor(img1);

        return new Color(result[0],result[1],result[2]);
    }

}
