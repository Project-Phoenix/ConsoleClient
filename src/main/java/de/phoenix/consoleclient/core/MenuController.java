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


/* User enters what to do. He can choose {upload} or {download}. Depending on his answer the proper Menu is chosen*/
public class MenuController {

    private DownloadMenu downloadMenu;
    private UploadMenu uploadMenu;

    public MenuController(String[] args) {
        uploadMenu = new UploadMenu(args);
        downloadMenu = new DownloadMenu(args);
    }

    // Possibility to choose action. Asks for an action until the user enters an available action.
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

    public void execute(String[] args) {

        String menuType = menuType(args);
        if (menuType.equals("download")) {
            downloadMenu.execute(args);
        } else if (menuType.equals("upload")) {
            uploadMenu.execute(args);
        }
    }
}
