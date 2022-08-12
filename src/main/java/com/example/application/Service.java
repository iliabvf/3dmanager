package com.example.application;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;

public class Service {
    public static Component findComponentWithId(Component root, String id) {
        for (Component child : root.getChildren().toList()) {
            if(id.equals(child.getId().orElse("null"))) {
                // found it!
                return child;
            } else if(child instanceof HasComponents) {
                // recursively go through all children that themselves have children
                Component ret= findComponentWithId(child, id);
                if(ret!=null)return ret;
            }
        }
        // none was found
        return null;
    }

}
