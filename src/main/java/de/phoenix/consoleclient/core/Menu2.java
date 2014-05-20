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

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.rs.EntityUtil;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixTaskSheet;
import de.phoenix.rs.key.SelectAllEntity;

public class Menu2 {

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
    public PhoenixTaskSheet userChosenSheet(String input, List<PhoenixTaskSheet> listedTitles) {

        PhoenixTaskSheet sheet = null;
        String title;

        // String consists only of a number
        if (input.matches("[0-9]+")) {
            int inputInt = Integer.parseInt(input);

            while (inputInt > listedTitles.size()) {
                System.out.println("invalid input, try again: ");
                input = Core.scanner.nextLine();
                inputInt = Integer.parseInt(input);
            }
            sheet = listedTitles.get(Integer.parseInt(input) - 1);

            // User entered the title
        } else {
            title = input;

            while (sheet == null) {
                for (int j = 0; j < listedTitles.size(); j++) {
                    if (title.toLowerCase().equals(listedTitles.get(j).getTitle().toLowerCase())) {
                        sheet = listedTitles.get(j);
                    }
                }

                if (sheet == null) {
                    System.out.println("Sorry, wrong title. Please try again: ");
                    title = Core.scanner.nextLine();
                }
            }
        }
        return sheet;
    }

    public PhoenixTask userChosenTask(PhoenixTaskSheet taskSheet) {
        PhoenixTask task = null;
        String title;
        List<PhoenixTask> listedTasks = taskSheet.getTasks();

        // user enters name or number he wants to download
        String input = Core.scanner.nextLine();

        // String consists only of a number
        if (input.matches("[0-9]+")) {
            int inputInt = Integer.parseInt(input);

            while (inputInt > listedTasks.size()) {
                System.out.println("invalid input, try again: ");
                input = Core.scanner.nextLine();
                inputInt = Integer.parseInt(input);
            }
            task = listedTasks.get(Integer.parseInt(input) - 1);

            // User entered the title
        } else {
            title = input;
            while (task == null) {
                for (int i = 0; i < listedTasks.size(); i++) {
                    if (title.equals(listedTasks.get(i).getTitle())) {
                        task = listedTasks.get(i);
                    }
                }
                
                if(task == null) {
                    System.out.println("Sorry, wrong title. Please try again:");
                    title = Core.scanner.nextLine();
                }
            }
        }
        return task;
    }
    

}
