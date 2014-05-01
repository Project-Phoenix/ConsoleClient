/*
 * Copyright (C) 2014 Project-Phoenix
 * 
 * This file is part of ConsoleClient.
 * 
 * ConsoleClient is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * ConsoleClient is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ConsoleClient.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.phoenix.consoleclient.core;

import java.util.HashMap;

public class MenuController {

    // Map to save all existent menus
    private HashMap<String, Menu> menuList = new HashMap<String, Menu>();
    private DownloadHandler downloadHandler;;
    private UploadMenu uploadMenu;

    public MenuController(String[] args) {
        downloadHandler = new DownloadHandler(args);
        uploadMenu = new UploadMenu(args);
    }

    // add menu
    public void register(String key, Menu value) {
        menuList.put(key, value);
    }

    /* first selection which menu to choose either upload or download */
    public String menuType(String[] args) {

        String menuType;

        if (!args[0].equals("download") && !args[0].equals("upload")) {
            System.out.println("Please enter what you want to do. You can either choose upload or download: ");
            menuType = Core.scanner.nextLine().toLowerCase();

            while (!menuType.toLowerCase().equals("upload") && !menuType.toLowerCase().equals("download")) {
                System.out.println("The requested function doesn't exist. Please choose upload or download:");
                menuType = Core.scanner.nextLine().toLowerCase();
            }
        } else {
            menuType = args[0];
        }
        return menuType;
    }

    public void execute(String[] args) throws Exception {

        String menuType = menuType(args);
        if(menuType.equals("download")) {
            downloadHandler.execute(args);
        } else if (menuType.equals("upload")) {
            uploadMenu.getTaskSheet(args);
        }
        System.out.println(menuType);

    }

}
