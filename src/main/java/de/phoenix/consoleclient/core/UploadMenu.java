package de.phoenix.consoleclient.core;
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
    
    public String getTaskSheet(String[] args) {
        String taskSheetTitle;
        
        if(args.length < 2) {
            System.out.println("Please choose a tasksheet to upload to:");
            taskSheetTitle = userChosenTitle(showAllTaskSheets());
        }
        else {
            taskSheetTitle = args[1];
        }
        return taskSheetTitle;     
    }
}
