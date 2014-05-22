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

public class DownloadMenu extends Menu2 {

    private Download download;

    public DownloadMenu(String[] args) {
        download = new Download();
        System.out.println("DownloadMenu");
    }

//    public List<String> checkOnTask(String userInput) {
//
//        List<String> downloadList = new ArrayList<String>();
//        PhoenixTaskSheet taskSheet = null;
//        List<PhoenixTask> tasks = new ArrayList<PhoenixTask>();
//
//        String[] inputSplitted = userInput.split(" ");
//        String[] inputSplittedForSheet = userInput.split("task");
//
//        System.out.println(inputSplittedForSheet[0]);
//        taskSheet = userChosenSheet(inputSplittedForSheet[0], getAllTaskSheets());
//        downloadList.add(taskSheet.getTitle());
//
//        if (inputSplitted[inputSplitted.length - 1].equals("task")) {
//            System.out.println("Please enter which tasksheet you want to download:");
//            showTasks(taskSheet);
//            tasks = userChosenTask(taskSheet);
//            downloadList.add(tasks.getTitle());
//        } else {
//            downloadList.add("notask");
//        }
//
//        return downloadList;
//    }

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

    public List<PhoenixTask> getTask(PhoenixTaskSheet taskSheet, String[] args) {
        List<PhoenixTask> tasks = new ArrayList<PhoenixTask>();
        String input;
            
        if (args.length < 3) {
            System.out.println("Please enter which task you want to download:");
            showTasks(taskSheet);
            input = Core.scanner.nextLine();
            tasks = userChosenTask(input, taskSheet);
        } else {
            tasks = userChosenTask(args[2], taskSheet);
        }

        return tasks;
    }

    public String getPath(String[] args) {
        String path;

        if (args.length < 4) {
            System.out.println("Please enter a path where your files should be saved:");
            path = Core.scanner.nextLine();

            File file = new File(path);
            while (!file.exists()) {
                System.out.println("Sorry, the given path does not exist. Please try again:");
                path = Core.scanner.nextLine();
                file = new File(path);
            }
        } else {
            path = args[3];

            File file = new File(path);
            while (!file.exists()) {
                System.out.println("Sorry, the given path does not exist. Please try again:");
                path = Core.scanner.nextLine();
                file = new File(path);
            }
        }
        System.out.println("path is " + path);
        return path;

    }

    public void execute(String[] args) {
        PhoenixTaskSheet taskSheet = getTaskSheet(args);
        List<PhoenixTask> tasks = getTask(taskSheet, args);
        String path = getPath(args);
        System.out.println(taskSheet.toString());
        System.out.println(tasks.toString());
        System.out.println(path);
        download.execute(taskSheet, tasks, path);
    }

}
