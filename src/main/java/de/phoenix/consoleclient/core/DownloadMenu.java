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

import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixTaskSheet;

public class DownloadMenu extends Menu2 {

    public DownloadMenu(String[] args) {
        System.out.println("DownloadMenu");
    }

    public List<String> checkOnTask(String userInput) {

        List<String> downloadList = new ArrayList<String>();
        PhoenixTaskSheet taskSheet = null;
        PhoenixTask task = null;

        String[] inputSplitted = userInput.split(" ");
        String[] inputSplittedForSheet = userInput.split("task");

        System.out.println(inputSplittedForSheet[0]);
        taskSheet = userChosenSheet(inputSplittedForSheet[0], getAllTaskSheets());
        downloadList.add(taskSheet.getTitle());

        if (inputSplitted[inputSplitted.length - 1].equals("task")) {
            System.out.println("Please enter which tasksheet you want to download:");
            showTasks(taskSheet);
            task = userChosenTask(taskSheet);
            downloadList.add(task.getTitle());
        } else {
            downloadList.add("notask");
        }

        return downloadList;
    }

    public List<String> getTaskSheet(String[] args) {
        String taskSheetTitle = "";
        PhoenixTaskSheet taskSheet = null;
        List<PhoenixTaskSheet> taskSheetList = getAllTaskSheets();
        List<String> taskSheetAndTask = new ArrayList<String>();
        String input;

        if (args.length < 2) {
        
            System.out.println("Please choose which tasksheet you want to download. If you want to download single tasks just write 'task' after the sheet:");
            showAllTaskSheets(getAllTaskSheets());
            input = Core.scanner.nextLine();
            // taskSheet and task at the first two places of the list
            taskSheetAndTask = checkOnTask(input);
        
        } else if (args.length == 2) {
            
            for (int i = 0; i < taskSheetList.size(); i++) {
                if (taskSheetList.get(i).getTitle().toLowerCase().equals(args[1].toLowerCase())) {
                    taskSheet = taskSheetList.get(i);
                    taskSheetTitle = args[1];
                    // taskSheet at first place of the list
                    taskSheetAndTask.add(taskSheetTitle);
                } else {
                    System.out.println("Sorry wrong title of the taskSheet.");
                    return null;
                }
            }
            
            System.out.println("If you want to download single tasks please enter 'tasks':");
            input = Core.scanner.nextLine();
            if (input.equals("tasks")) {
                showTasks(taskSheet);
                PhoenixTask task = userChosenTask(taskSheet);
                taskSheetAndTask.add(task.getTitle());            
            } else {
                taskSheetAndTask.add("notask");
            }
            
        } else {
            
            for (int i = 0; i < taskSheetList.size(); i++) {
                if (taskSheetList.get(i).getTitle().toLowerCase().equals(args[1].toLowerCase())) {
                    taskSheet = taskSheetList.get(i);
                    // taskSheet at first place of the list
                    taskSheetAndTask.add(taskSheetTitle);
                } else {
                    System.out.println("Sorry wrong title of the taskSheet.");
                    return null;
                }
            }
            
            for (int i = 0; i < taskSheet.getTasks().size(); i++) {
                if(taskSheet.getTasks().get(i).getTitle().toLowerCase().equals(args[2].toLowerCase())) {
                    //task at second place of the list
                    taskSheetAndTask.add(args[2]);
                } else {
                    System.out.println("Sorry wrong task title.");
                    return null;
                }
            }
            
        }

        System.out.println(taskSheetAndTask.toString());
        return taskSheetAndTask;

    }

}
