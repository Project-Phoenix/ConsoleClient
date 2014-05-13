/*
 * Copyright (C) 2013 Project-Phoenix
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

public class MenuHandler extends Menu {

    // Map to save all existent menus
    private HashMap<String, Menu> menuList = new HashMap<String, Menu>();
    private DownloadHandler downloadHandler;

    public MenuHandler(String[] args) {
        downloadHandler = new DownloadHandler(args);
    }

    // add menu
    public void register(String key, Menu value) {
        menuList.put(key, value);
    }

    /*user input about what to do {upload, download}*/
    public String menuType() {

        String menuType;
        System.out.println("Please enter what you want to do. You can either choose upload or download: ");
        menuType = Core.scanner.nextLine().toLowerCase();

        while (!menuType.toLowerCase().equals("upload") && !menuType.toLowerCase().equals("download")) {
            System.out.println("The requested function doesn't exist. Please choose upload or download:");
            menuType = Core.scanner.nextLine().toLowerCase();
        }

        return menuType;
    }


    public void execute(String[] args) throws Exception {

        String menuType = menuType();

        if (menuType.equals("upload")) {
//            uploadHandler.execute(args);
        } else if (menuType.equals("download")){
//            String[] arguments = getMissingArguments(args);
            downloadHandler.execute(args);
        }


    }

}