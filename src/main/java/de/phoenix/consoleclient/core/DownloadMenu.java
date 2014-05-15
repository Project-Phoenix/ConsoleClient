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

import java.util.List;

import de.phoenix.rs.entity.PhoenixTaskSheet;

public class DownloadMenu extends Menu2 {

    public DownloadMenu(String[] args) {
        System.out.println("DownloadMenu");
    }

    public PhoenixTaskSheet getTaskSheet(String[] args) {
        PhoenixTaskSheet taskSheet = null;
        List<PhoenixTaskSheet> taskSheetList = getAllTaskSheets();

        if (args.length < 2) {
            System.out.println("Please choose which tasksheet you want to download. If you want to download single tasks just write 'task' after the sheet:");
            showAllTaskSheets(taskSheetList);
            String input = Core.scanner.nextLine();
            String[] inputSplitted = input.split("task");
            System.out.println(inputSplitted[0]);

            for (int i = 0; i < taskSheetList.size(); i++) {
                if (taskSheetList.get(i).getTitle().equals(inputSplitted[0])) {
                    taskSheet = taskSheetList.get(i);
                    System.out.println("tasksheet found");
                }
            }

            if (taskSheet == null) {
                System.out.println("tasksheet is null");
                return null;
            }

            for (int i = 0; i < inputSplitted.length; i++) {
                if (inputSplitted[i].equals("task")) {
                    showTasks(taskSheet);
                }
            }

        }

        return taskSheet;

    }

}
