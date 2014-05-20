package de.phoenix.consoleclient.core;

import java.io.File;
import java.util.ArrayList;
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

    private Upload upload;

    public UploadMenu(String[] args) {
        upload = new Upload();
        System.out.println("Constructor constructed.");
    }

    public PhoenixTaskSheet getTaskSheet(String[] args) {
        PhoenixTaskSheet taskSheet = null;
        String taskSheetTitle = "";
        String input;

        List<PhoenixTaskSheet> taskSheetList = getAllTaskSheets();

        if (args.length < 2) {
            System.out.println("Please choose a tasksheet to upload to:");
            showAllTaskSheets(taskSheetList);
            input = Core.scanner.nextLine();
            taskSheet = userChosenSheet(input, taskSheetList);
        } else {
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

        if (args.length < 3) {
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

    public List<List<String>> getUploadFilePath(String[] args) {
        List<String> path = new ArrayList<String>();
        List<String> attachment = new ArrayList<String>();
        List<List<String>> uploads = new ArrayList<List<String>>();
        File file;

        // TODO: mehrmals nachfragen? sonst evtl attach vor nen Pfad schreiben
        // no file declared, splits into textbased files and attachments
        if (args.length < 4) {
            System.out.println("Please enter, where your file is saved:");

            String input = Core.scanner.nextLine();
            String[] splittedInput = input.split(" ");
            for (int i = 0; i < splittedInput.length; i++) {
                if (splittedInput[i].equals("attach")) {
                    if (i == splittedInput.length - 1) {
                        System.out.println("Sorry, 'attach' needs a filepath following.");
                        return null;
                    }
                    attachment.add(splittedInput[i + 1]);
                    i++;
                } else {
                    path.add(splittedInput[i]);
                }
            }
        } else {

            // length of the argsString concerning file paths
            int i = args.length - 3;
            int j = 0;
            // adds all further files to filelist
            while (i > 0) {
                if (args[j + 3].equals("attach")) {
                    if ((j + 3) == (args.length - 1)) {
                        System.out.println("Sorry, 'attach' needs a filepath following.");
                        return null;
                    }
                    attachment.add(args[j + 4]);
                    j++;
                    i--;
                } else {
                    path.add(args[j + 3]);
                }
                j++;
                i--;
            }
        }

        System.out.println(path.toString());
        System.out.println(attachment.toString());

        // tests if entered pathes exist, if not, asks for the right ones
        for (int i = 0; i < path.size(); i++) {
            file = new File(path.get(i));
            System.out.println(path.toString());
            while (!file.exists()) {
                System.out.println("Sorry [" + path.get(i) + "] doesn't exist. Try again:");
                path.set(i, Core.scanner.nextLine());
                file = new File(path.get(i));
            }
        }

        uploads.add(0, attachment);
        uploads.add(1, path);
        
        return uploads;
    }

    public void execute(String[] args) throws Exception {
        PhoenixTask task = getTask(args);
        List<List<String>> path = getUploadFilePath(args);
        upload.execute(task, path);
    }
}
