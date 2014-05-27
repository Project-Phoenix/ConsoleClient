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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.rs.EntityUtil;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixTaskSheet;
import de.phoenix.rs.key.SelectAllEntity;

public class Menu {

    /* prints and returns a list with the names of all tasksheets */
    public List<PhoenixTaskSheet> getAllTaskSheets() {

        WebResource getTaskSheetResource = PhoenixTaskSheet.getResource(Core.client, Core.BASE_URL);
        ClientResponse response = getTaskSheetResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new SelectAllEntity<PhoenixTask>());
        if (response.getStatus() == 404) {
            System.out.println("Sorry, there are no Tasks available");
            return null;
        }

        List<PhoenixTaskSheet> sheets = EntityUtil.extractEntityList(response);
        return sheets;
    }

    public void showAllTaskSheets(List<PhoenixTaskSheet> taskSheets) {

        if (taskSheets.isEmpty()) {
            System.out.println("Sorry, there are no tasksheets available");
        } else {
            for (int i = 0; i < taskSheets.size(); i++) {
                System.out.println("(" + (i + 1) + ") " + taskSheets.get(i).getTitle());
            }
        }
    }

    /* shows all tasks of a tasksheet */
    public void showTasks(PhoenixTaskSheet taskSheet) {

        List<PhoenixTask> taskTitles = taskSheet.getTasks();
        for (int i = 0; i < taskTitles.size(); i++) {
            System.out.println("(" + (i + 1) + ") " + taskTitles.get(i).getTitle());
        }
    }

    /* returns the title the user chose */
    public PhoenixTaskSheet userChosenSheet(String input, List<PhoenixTaskSheet> listedSheets) {

        PhoenixTaskSheet sheet = null;
        String title;

        while (sheet == null) {
            // String consists only of a number
            if (input.matches("[0-9]+")) {
                int inputInt = Integer.parseInt(input);

                while (inputInt > listedSheets.size()) {
                    System.out.println("invalid input, try again: ");
                    input = Core.scanner.nextLine();
                    inputInt = Integer.parseInt(input);
                }
                sheet = listedSheets.get(Integer.parseInt(input) - 1);

                // User entered the title
            } else {
                title = input;

                for (int j = 0; j < listedSheets.size(); j++) {
                    if (title.toLowerCase().equals(listedSheets.get(j).getTitle().toLowerCase())) {
                        sheet = listedSheets.get(j);
                    }
                }
                
                if (sheet == null) {
                    System.out.println("Sorry, wrong title for tasksheet. Please try again: ");
                    showAllTaskSheets(getAllTaskSheets());
                    input = Core.scanner.nextLine();
                }
            }
        }
        return sheet;
    }

    public List<PhoenixTask> userChosenTask(String input, PhoenixTaskSheet taskSheet) {
        List<PhoenixTask> tasks = new ArrayList<PhoenixTask>();
        String title;
        List<PhoenixTask> listedTasks = taskSheet.getTasks();

        
        if(input.equals("")) {
            tasks = taskSheet.getTasks();
        }

        while (tasks.isEmpty()) {
        // String consists only of a number
        if (input.matches("[0-9]+")) {
            int inputInt = Integer.parseInt(input);

            while (inputInt > listedTasks.size()) {
                System.out.println("invalid input, try again: ");
                input = Core.scanner.nextLine();
                inputInt = Integer.parseInt(input);
            }
            tasks.add(listedTasks.get(Integer.parseInt(input) - 1));

            // User entered the title
        } else {
            title = input;
            
                for (int i = 0; i < listedTasks.size(); i++) {
                    if (title.equals(listedTasks.get(i).getTitle())) {
                        tasks.add(listedTasks.get(i));
                    }
                }

                if (tasks.isEmpty()) {
                    System.out.println("Sorry, wrong title for task. Please try again:");
                    showTasks(taskSheet);
                    input = Core.scanner.nextLine();
                }
            }
        }
        return tasks;
    }
    
    public void deleteDir(File dir) {
        File[] listFiles = dir.listFiles();
        for (int i = 0; i < listFiles.length; ++i) {
            File file = listFiles[i];
            if (file.isDirectory())
                deleteDir(file);
            else
                file.delete();
        }
        dir.delete();
    }

    public void deleteFile(File file) {
        if (file.isDirectory()) {
            deleteDir(file);
            System.out.println("Directory deleted.");
        } else {
            file.delete();
            System.out.println("File deleted.");
        }
    }

    /*
     * returns false if file already exists and the user don't want to override
     * it. Otherwise deletes files and returns true
     */
    public boolean fileOverride(File file) {

        if (file.exists()) {
            System.out.println(file.getName() + " already exists. Do you want to override it? Enter (Y) or (N)");
            String answer = Core.scanner.nextLine();

            if (answer.equals("N"))
                return false;
            else
                deleteFile(file);
        }

        return true;
    }
    
    public void writeInFile(File file, String text) {
        Writer fw;
        Writer bw;
        try {
            fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write(text);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
