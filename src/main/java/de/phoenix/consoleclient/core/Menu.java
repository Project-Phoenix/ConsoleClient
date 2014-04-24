/*
 * Copyright (C) 2013 Project-Phoenix
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

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.rs.EntityUtil;
import de.phoenix.rs.entity.PhoenixLecture;
import de.phoenix.rs.entity.PhoenixLectureGroup;
import de.phoenix.rs.entity.PhoenixLectureGroupTaskSheet;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixTaskSheet;
import de.phoenix.rs.entity.PhoenixLectureGroupTaskSheet.PhoenixDatedTask;
import de.phoenix.rs.key.SelectAllEntity;
import de.phoenix.rs.key.SelectEntity;

public abstract class Menu {

    private WebResource wrSheet;

    public Menu() {
        wrSheet = PhoenixTaskSheet.getResource(Core.client, Core.BASE_URL);
    }

    public abstract void execute(String[] args) throws Exception;

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
        if(file.isDirectory()) {
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
    
    
    /* shows all task of a tasksheet */
    public List<String> showTasks(PhoenixTaskSheet taskSheet) {

        List<String> titles = new ArrayList<String>();
        List<PhoenixTask> taskTitles = taskSheet.getTasks();
        for (int i = 0; i < taskTitles.size(); i++) {
            System.out.println("(" + (i + 1) + ") " + taskTitles.get(i).getTitle());
            titles.add(i, taskTitles.get(i).getTitle());
        }
        return titles;
    }

    /* converts a title to the assigned tasksheet */
    public PhoenixTaskSheet titleToTaskSheet(String title) {

        SelectEntity<PhoenixTaskSheet> selectByTitle = new SelectEntity<PhoenixTaskSheet>().addKey("title", title);
        ClientResponse post = wrSheet.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selectByTitle);
        PhoenixTaskSheet taskByTitle = EntityUtil.extractEntity(post);

        return taskByTitle;

    }

    /* returns a list with the names of all tasksheets */
    public List<String> showAllTaskSheets() {

        WebResource getTaskSheetResource = PhoenixTaskSheet.getResource(Core.client, Core.BASE_URL);
        ClientResponse response = getTaskSheetResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new SelectAllEntity<PhoenixTask>());
        if (response.getStatus() == 404) {
            System.out.println("Sorry, there are no Tasks available");
            return null;
        }

        System.out.println("Status ist: " + response.getStatus());

        List<PhoenixTaskSheet> sheets = EntityUtil.extractEntityList(response);
        List<String> sheetTitles = new ArrayList<String>();

        if (sheets.isEmpty()) {
            System.out.println("Sorry, there are no tasksheets available");
            return null;
        } else {
            for (int i = 0; i < sheets.size(); i++) {
                System.out.println("(" + (i + 1) + ") " + sheets.get(i).getTitle());
                sheetTitles.add(i, sheets.get(i).getTitle());
            }
            return sheetTitles;
        }

    }

    /* returns the title the user chose */
    public String userChoice(List<String> listedTitles) {

        String title;

        // user enters name or number he wants to download
        String input = Core.scanner.nextLine();

        // String consists only of a number
        if (input.matches("[0-9]+")) {
            int inputInt = Integer.parseInt(input);

            if (inputInt > listedTitles.size()) {
                System.out.println("invalid input");
                return null;
            }

            title = listedTitles.get(Integer.parseInt(input) - 1);

            // User entered the title
        } else {
            title = input;
            if (!listedTitles.contains(title)) {
                System.out.println("Title doesn't exist.");
                return null;
            }
        }
        return title;
    }

    public PhoenixTaskSheet getCurrentTaskSheet() {

        WebResource getCurrentTaskSheet = PhoenixLectureGroupTaskSheet.currentTaskSheet(Core.client, Core.BASE_URL);

        SelectEntity<PhoenixLectureGroup> groupSelector = new SelectEntity<PhoenixLectureGroup>().addKey("name", "Gruppe 1");
        SelectEntity<PhoenixLecture> lectureSelector = new SelectEntity<PhoenixLecture>().addKey("title", "Einführung in die Informatik");
        groupSelector.addKey("lecture", lectureSelector);

        ClientResponse response = getCurrentTaskSheet.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, groupSelector);
        System.out.println(response.getStatus());
        
        //PhoenixLectureGroupTaskSheet to TaskSheet
        PhoenixLectureGroupTaskSheet wantedSheet = EntityUtil.extractEntity(response);
        List<PhoenixTask>tmp = new ArrayList<PhoenixTask>();
        for (PhoenixDatedTask datedTask : wantedSheet.getTasks()) {
            tmp.add(datedTask.getTask());
        }
        PhoenixTaskSheet t = new PhoenixTaskSheet(wantedSheet.getTaskSheetTitle(), tmp, null);
        
        return t;
    }
   

}
