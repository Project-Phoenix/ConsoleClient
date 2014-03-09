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

        if (args.length != 1) {
            System.out.println("Please enter what you want to do.\n" + "[USAGE:] java -jar ... thingToDo");
            return;
        }
        // choose menu by parameters
        Menu theChosen = menuList.get(args[0].toLowerCase());
        if (theChosen != null) {
            theChosen.execute(args);
        } else {
            System.out.println("The requested function doesn't exist.");
            return;
        }

    }

}