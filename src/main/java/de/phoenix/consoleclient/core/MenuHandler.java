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

public class MenuHandler {

    // Map to save all existent menus
    private HashMap<String, Menu> menuList = new HashMap<String, Menu>();

    // add menu
    public void register(String key, Menu value) {
        menuList.put(key, value);
    }

    public void execute(String[] args) throws Exception {

        if(args.length == 0) new SimpleMenu(args, menuList);
        
        
//        Menu theChosen;
//        String wantedMenu = null;
//
//        if (args.length == 0 || !args[0].toLowerCase().contains("download") || !args[0].toLowerCase().contains("upload")) {
//            System.out.println(args.length);
//            System.out.println("Please enter what you want to do. You can either choose 'upload' or 'download':");
//        } else {
//            wantedMenu = args[0].toLowerCase();
//        }

//        // choose menu by parameters
//        theChosen = menuList.get(wantedMenu);
//        if (theChosen != null) {
//            theChosen.execute(args);
//        } else {
//            System.out.println("The requested function doesn't exist.");
//            return;
//        }
//
//        do {
//            if(wantedMenu == null) wantedMenu = Core.scanner.nextLine();
//            theChosen = menuList.get(wantedMenu);
//            if (theChosen == null) {
//                System.out.println("The requested function doesn't exist. Please choose upload or download:");
//            }
//
//        } while (theChosen == null);
//        
//        theChosen.execute(args);

    }

}