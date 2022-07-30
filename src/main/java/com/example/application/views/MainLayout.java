package com.example.application.views;


import com.example.application.components.appnav.AppNav;
import com.example.application.components.appnav.AppNavItem;
import com.example.application.views.models.ModelsView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.PageTitle;

import java.util.ArrayList;
import java.util.List;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H1 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        addToDrawer(createDrawerContent());
    }

    private Component createHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addClassNames("view-toggle");
        toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames("view-title");

        Header header = new Header(toggle, viewTitle);
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
        treeGrid.addHierarchyColumn(Person::getFirstName).setHeader("First name");
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
