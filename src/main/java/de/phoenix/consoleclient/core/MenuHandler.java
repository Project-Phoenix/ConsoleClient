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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.phoenix.util.Configuration;
import de.phoenix.util.JSONConfiguration;

public class MenuHandler extends Menu {

    // Map to save all existent menus
    private HashMap<String, Menu> menuList = new HashMap<String, Menu>();
    private Download download;

    public MenuHandler(String[] args) {
        download = new Download(args);
    }

    // add menu
    public void register(String key, Menu value) {
        menuList.put(key, value);
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

    public String firstStart() throws Exception {

        String path;
        Configuration config = new JSONConfiguration("config.json");
        if (!config.exists("downloadPath")) {
            System.out.println("It seems to be your first start. Please enter where you wanna save your files:");
            path = Core.scanner.nextLine();
            config.setString("downloadPath", path);
        } else {
            path = config.getString("downloadPath");
        }
        return path;
    }

    public String[] getMissingArguments(String[] args) throws Exception {
        List<String> arguments = new ArrayList<String>();

        String path = firstStart();

        System.out.println("Please enter which tasksheet you want to download: ");
        List<String> sheets = showAllTaskSheets();
        if (sheets == null) {
            System.out.println("Fehler bei getMissingArguments: sheets");
            return null;
        }
        String sheetTitle = userChoice(sheets);
        if (sheetTitle == null) {
            System.out.println("Fehler bei getMissingArguments: sheetTitle");
            return null;
        }
        
        System.out.println("Want to download the whole sheet (all) or a specific task (task) ?");
        String what = Core.scanner.nextLine();
        while(!what.equals("all") && !what.equals("task")) {
            System.out.println(what);
            System.out.println("Please enter (all) or (task)");
            what = Core.scanner.nextLine();
        }        

        String taskTitle = "";
        if(what.equals("task")){
          List<String> tasks = showTasks(titleToTaskSheet(sheetTitle));
          if(tasks == null) {
              System.out.println("Fehler bei tasks in getMissingArguments");
              return null;
          }
          taskTitle = userChoice(tasks);
          if(taskTitle == null) {
              System.out.println("Fehler bei getMissingArguments: taskTitle");
              return null;
          }
        }
        
        // args should be like
        // "[download] [path where to save the files] [tasksheetTitle] [download task or taskheet {task, all}]"
        arguments.add(0, "download");
        arguments.add(1, path);
        arguments.add(2, sheetTitle);
        arguments.add(3, what);
        arguments.add(4, taskTitle);

        String[] argumentString = new String[arguments.size()];
        for (String element : arguments) {
            argumentString[arguments.indexOf(element)] = arguments.get(arguments.indexOf(element));
        }
        
        return argumentString;

    }

    public void execute(String[] args) throws Exception {

        String menuType = menuType();

        if (menuType.equals("upload")) {
            System.out.println("Lalala upload");
            return;
        } else {
            String[] arguments = getMissingArguments(args);
            download.execute(arguments);
        }

//        if(args.length == 0) new SimpleMenu(args, menuList);

//        Menu theChosen;
//        String wantedMenu = null;
//
//        if (args.length == 0 || !args[0].toLowerCase().contains("download") || !args[0].toLowerCase().contains("upload")) {
//            System.out.println(args.length);
//            System.out.println("Please enter what you want to do. You can either choose 'upload' or 'download':");
//            wantedMenu = Core.scanner.nextLine();
//        } else {
//            wantedMenu = args[0].toLowerCase();
//        }
//
//        // choose menu by parameters
//        theChosen = menuList.get(wantedMenu);
//        if (theChosen == null) {
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

//        CommandLineParser parser = new DefaultParser();
//
//        try {
//            CommandLine line = parser.parse(this.option, args);
//
//            if (line.hasOption("download") && line.hasOption("current")) {
//                try {
//                    downloadCurrent.execute(args);
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

//
    }

}