package com.example.application.views;


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
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.PageTitle;
import org.apache.xmlbeans.impl.xb.xsdschema.WhiteSpaceDocument;

import java.util.ArrayList;
import java.util.List;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H1 viewTitle;
    static private MainLayout mainLayout;
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

        Header header = new Header(toggle, viewTitle, new Span(), new Label(" Sort by"),sortBy);
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

    public class Person{
        String firstName;
        String lastName;
        String email;

        public Person(String firstName, String lastName, String email) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getEmail() {
            return email;
        }
    }

    public List<Person> getStaff(Person manager) {
        List<Person> managers = new ArrayList<>();
        if (manager == null) {
            managers.add(new Person("Interior", "", ""));
            managers.add(new Person("Exterior", "", ""));
        } else if (manager.getFirstName().equals("Interior")) {
            managers.add(new Person("Tables", "", ""));
            managers.add(new Person("Stairs", "", ""));
        } else if (manager.getFirstName().equals("Exterior")) {
            managers.add(new Person("Houses", "", ""));
            managers.add(new Person("Trees", "", ""));
        }
        return managers;
    }
    private Component createTree(){

        TreeGrid<Person> treeGrid = new TreeGrid<>();
        treeGrid.setItems(getStaff(null), this::getStaff);
        treeGrid.addHierarchyColumn(Person::getFirstName).setHeader("Folders");
        treeGrid.setSelectionMode(TreeGrid.SelectionMode.SINGLE);

        treeGrid.addItemClickListener(new ComponentEventListener<ItemClickEvent<Person>>() {
            @Override
            public void onComponentEvent(ItemClickEvent<Person> personItemClickEvent) {
                ModelsView.getModelsView().getHeader().setText(personItemClickEvent.getItem().getFirstName());
            }
        });

//        treeGrid.addColumn(Person::getLastName).setHeader("Last name");
//        treeGrid.addColumn(Person::getEmail).setHeader("Email");

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
}
