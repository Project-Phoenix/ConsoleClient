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

import java.util.ArrayList;
import java.util.List;

import de.phoenix.util.Configuration;
import de.phoenix.util.JSONConfiguration;

public class DownloadHandler extends Menu {

    private Download download;

    public DownloadHandler(String[] args) {
        this.download = new Download(args);
    }

    /* returns a downloadpath, at first start a config file is created */
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
        String path;
        String sheetTitle;

        if (args[1].isEmpty()) {
            path = firstStart();
        } else {
            path = args[1];
        }

        if (args[2].isEmpty()) {
            System.out.println("Please enter which tasksheet you want to download: ");
            List<String> sheets = showAllTaskSheets();
            if (sheets == null) {
                System.out.println("Fehler bei getMissingArguments: sheets");
                return null;
            }
            sheetTitle = userChosenTitle(sheets);
            if (sheetTitle == null) {
                System.out.println("Fehler bei getMissingArguments: sheetTitle");
                return null;
            }
        } else {
            sheetTitle = args[1];
        }

        if (args[3].isEmpty()) {
            System.out.println("Want to download the whole sheet (S) or a specific task (T) ?");
            String what = Core.scanner.nextLine().toLowerCase();
            while (!what.equals("s") && !what.equals("t")) {
                System.out.println(what);
                System.out.println("Please enter (S) or (T)");
                what = Core.scanner.nextLine().toLowerCase();
            }

            String taskTitle = "";
            if (what.equals("t")) {
                List<String> tasks = showTasks(titleToTaskSheet(sheetTitle));
                if (tasks == null) {
                    System.out.println("Fehler bei tasks in getMissingArguments");
                    return null;
                }
                taskTitle = userChosenTitle(tasks);
                if (taskTitle == null) {
                    System.out.println("Fehler bei getMissingArguments: taskTitle");
                    return null;
                }
            }
        }

        // args should be like
        // "[download] [path where to save the files] [tasksheetTitle] [download task or taskheet {task, all}]"
        arguments.add(0, "download");
        arguments.add(1, path);
        arguments.add(2, args[2]);
        arguments.add(3, args[3]);
        arguments.add(4, null);

        // converts arraylist to string[]
        String[] argumentString = new String[arguments.size()];
        for (String element : arguments) {
            argumentString[arguments.indexOf(element)] = arguments.get(arguments.indexOf(element));
        }

        return argumentString;

    }

    public void execute(String[] args) throws Exception {
        String[] arguments = getMissingArguments(args);
        download.execute(arguments);
    }

}
