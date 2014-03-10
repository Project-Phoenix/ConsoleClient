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

import java.util.Map;

public class SimpleMenu {
   
    
    public SimpleMenu(String[] args, Map<String,Menu> menuList ) throws Exception {
      
        String menuType = menuType();
        
        Menu theChosen = menuList.get(menuType);
        
        if(theChosen != null)
        theChosen.execute(args);

    }
    
    public String menuType() {
        
        String menuType;
        System.out.println("Please enter what you want to do. You can either choose upload or download: ");
        menuType = Core.scanner.nextLine();

        while (!menuType.toLowerCase().equals("upload") && !menuType.toLowerCase().equals("download")) {
            System.out.println("The requested function doesn't exist. Please choose upload or download:");
            menuType = Core.scanner.nextLine();
        }

        return menuType;
    }

}
