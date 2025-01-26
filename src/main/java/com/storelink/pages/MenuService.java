package com.storelink.pages;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class MenuService {

    public List<Menu> getMenus() {
        
        List<Menu> menus = new ArrayList<>();

        // Example menu: Dashboard
        Menu dashboards = new Menu();
        dashboards.setId(1);
        dashboards.setName("Dashboard");
        dashboards.setUrl("#");

        Menu dashboard1 = new Menu();
        dashboard1.setId(2);
        dashboard1.setName("Dashboard1");
        dashboard1.setUrl("/cms/dashboard");

        dashboards.setSubMenus(List.of(dashboard1));


        //PERMISSIONS
        Menu permissions = new Menu();
        permissions.setId(3);
        permissions.setName("Permission");
        permissions.setUrl("#");
        
        Menu createPermission = new Menu();
        createPermission.setId(4);
        createPermission.setName("Create Permission");
        createPermission.setUrl("/cms/permission/create");
        
        Menu permissionList = new Menu();
        permissionList.setId(5);
        permissionList.setName("Permission List");
        permissionList.setUrl("/cms/permission/permissions");
        
        permissions.setSubMenus(List.of(createPermission,permissionList));
        
        
        Menu users = new Menu();
        users.setId(6);
        users.setName("User Management");
        users.setUrl("#");
        
        Menu createUser = new Menu();
        createUser.setId(7);
        createUser.setName("Create user");
        createUser.setUrl("/cms/user/create");
        
        Menu usersList = new Menu();
        usersList.setId(8);
        usersList.setName("Users");
        usersList.setUrl("/cms/user/users");

        users.setSubMenus(List.of(createUser,usersList));

        // Add menus to the list
        menus.add(dashboards);
        menus.add(permissions);
        menus.add(users);
        
        return menus;
    }
}