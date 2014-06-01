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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixTaskSheet;

public class DownloadMenu extends Menu {

    private Download download;

    public DownloadMenu(String[] args) {
        download = new Download(args);
    }

    /*
     * Asks the user which taskSheet he wants to download or takes the string,
     * which was entered at the second place as title of the wanted taskSheet.
     */
    public PhoenixTaskSheet getTaskSheet(String[] args) {

        String input;

        PhoenixTaskSheet taskSheet = null;

        List<PhoenixTaskSheet> taskSheetList = getAllTaskSheets();

        if (args.length < 2) {

            System.out.println("Please choose which tasksheet you want to download:");
            showAllTaskSheets(taskSheetList);
            input = Core.scanner.nextLine();
            taskSheet = userChosenSheet(input, taskSheetList);

        } else {

            taskSheet = userChosenSheet(args[1], taskSheetList);

        }

        return taskSheet;
    }

    /*
     * Asks the user which tasks he wants to download or takes the string, which
     * was entered at the third place as title of the task.
     */
    public List<PhoenixTask> getTask(PhoenixTaskSheet taskSheet, String[] args) {
        List<PhoenixTask> tasks = new ArrayList<PhoenixTask>();
        String input;

        if (args.length < 3) {
            System.out.println("Please enter which task you want to download. If you want to download the entire tasksheet, press enter.");
            showTasks(taskSheet);
            input = Core.scanner.nextLine();
            tasks = userChosenTask(input, taskSheet);
        } else {
            File file = new File(args[2]);
            if (file.exists()) {
                System.out.println("File on third place!");
                tasks = taskSheet.getTasks();
            } else {
                tasks = userChosenTask(args[2], taskSheet);
            }
        }

        return tasks;
    }

    /*
     * Asks the user where he wants his files to be saved or takes the string,
     * which was entered at the forth place as path.
     */
    public String getPath(String[] args) {
        String path = "";

        if (args.length < 3) {
            System.out.println("Please enter a path where your files should be saved:");
            path = Core.scanner.nextLine();

            File file = new File(path);
            while (!file.exists()) {
                System.out.println("Sorry, [" + path +"] does not exist. Please try again:");
                path = Core.scanner.nextLine();
                file = new File(path);
            }
        } else if (args.length == 4) {
            path = args[3];

            File file = new File(path);
            while (!file.exists()) {
                System.out.println("Sorry, [" + path +"] does not exist. Please try again:");
                path = Core.scanner.nextLine();
                file = new File(path);
            }
        } else {
            File file = new File(args[2]);
            if (file.exists()) {
                path = args[2];
            }
        }

        return path;

    }

    public void execute(String[] args) {

        PhoenixTaskSheet taskSheet = getTaskSheet(args);
        List<PhoenixTask> tasks = getTask(taskSheet, args);
        String path = getPath(args);
        download.execute(taskSheet, tasks, path);
    }

}
