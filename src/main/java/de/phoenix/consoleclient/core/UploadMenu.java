package de.phoenix.consoleclient.core;

import java.io.File;
import java.util.List;

import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixTaskSheet;
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

public class UploadMenu extends Menu2 {
 
    public UploadMenu(String[] args) {
        System.out.println("Constructor constructed.");
    }
    
    public PhoenixTaskSheet getTaskSheet(String[] args) {
        PhoenixTaskSheet taskSheet = null;
        String taskSheetTitle = "";
        
        List<PhoenixTaskSheet> taskSheetList = getAllTaskSheets();
       
        if(args.length < 2) {
            System.out.println("Please choose a tasksheet to upload to:");
            showAllTaskSheets(taskSheetList);
            taskSheet = userChosenSheet(taskSheetList);
        }
        else {
            System.out.println("BIN DRIN");
            taskSheetTitle = args[1];
            for (int i = 0; i < taskSheetList.size(); i++) {
                if (taskSheetTitle.toLowerCase().equals(taskSheetList.get(i).getTitle().toLowerCase())) {
                    taskSheet = taskSheetList.get(i);
                }
            }
            if (taskSheet == null) {
                System.out.println("Sorry, wrong title.");
                return null;
            }
        }
        
        return taskSheet;     
    }
    
    public PhoenixTask getTask(String[] args) {
        PhoenixTask task = null;
        PhoenixTaskSheet taskSheet = getTaskSheet(args);
        
        if (taskSheet == null) {
            return null;
        }
        
        if(args.length < 3) {
            System.out.println("Please choose a task to upload to:");
            showTasks(taskSheet);
            task = userChosenTask(taskSheet);
        } else {
            for (int i = 0; i < taskSheet.getTasks().size(); i++) {
                if (args[2].equals(taskSheet.getTasks().get(i).getTitle())) {
                    task = taskSheet.getTasks().get(i);
                    System.out.println("Success with " + task.getTitle());
                }
            }
            if (task == null) {
                System.out.println("Sorry, wrong title.");
                return null;
            }
        }
        return task;
    }
    
    public String getUploadFilePath(String[] args) {
        String path;
        File file;
        
        if (args.length < 4) {
            System.out.println("Please enter, where your file is saved:");
            path = Core.scanner.nextLine();
        } else {
            path = args[3];
        }
        
        file = new File(path);
        while(!file.exists()) {
            System.out.println("Sorry your path doesn't exist. Try again:");
            path = Core.scanner.nextLine();
            file = new File(path);
        }
        //TODO: MIST getUploadFilePath noch irgendwo aufrufen...
        
        return path;
    }
}
