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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.filter.EduFilter;
import de.phoenix.filter.TextFilter;
import de.phoenix.rs.EntityUtil;
import de.phoenix.rs.entity.PhoenixAttachment;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.entity.PhoenixTaskSheet;
import de.phoenix.rs.entity.PhoenixText;
import de.phoenix.rs.key.SelectEntity;
import de.phoenix.util.Configuration;
import de.phoenix.util.JSONConfiguration;

public class Download extends Menu {

    private WebResource wrTask;

    public Download(String[] args) {
        wrTask = PhoenixTask.getResource(Core.client, Core.BASE_URL);
    }

    public String firstStart() throws Exception {

        String path;
        Configuration config = new JSONConfiguration("config.json");
        if (!config.exists("downloadPath")) {
            System.out.println("It seems to be your first start. Please enter where you wanna save your files:");
            path = Core.scanner.nextLine();
            config.setString("downloadPath", path);
        } else {
            path = config.getString("downloadPath");
        }
        return path;
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

    public void createTaskOnComputer(File file, PhoenixTaskSheet taskSheet, String path, String taskTitle) throws IOException {

        SelectEntity<PhoenixTask> selectByTitle = new SelectEntity<PhoenixTask>().addKey("title", taskTitle);
        ClientResponse post = wrTask.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selectByTitle);
        PhoenixTask reqTitle = EntityUtil.extractEntity(post);

        List<PhoenixText> pattern = reqTitle.getPattern();
        List<PhoenixAttachment> attachment = reqTitle.getAttachments();
        String description = reqTitle.getDescription();

        TextFilter t = EduFilter.INSTANCE;
        String descrFiltered = t.filter(description);

        File directory = new File(path + "/" + taskSheet.getTitle());
        directory.mkdir();

        // task is without any given code or attachment, just text
        if (pattern.isEmpty() && attachment.isEmpty()) {

            File taskFile = new File(directory, taskTitle + ".java");

            boolean override = fileOverride(taskFile);
            if(override == false) {
                return;
            }

            // if file doesn't exist, create this file and write the
            // description as a comment in it
            writeInFile(taskFile, "/*" + descrFiltered + "*/");

            // task has one pattern hence one class to submit, no
            // attachments
        } else if (pattern.size() == 1 && attachment.isEmpty()) {

            File taskFile = new File(directory, pattern.get(0).getFullname());

            boolean override = fileOverride(taskFile);
            if(override == false) {
                return;
            }
            
            writeInFile(taskFile, "/*" + descrFiltered + "*/\n" + pattern.get(0).getText());

            // task has at least two patterns or some attachments
        } else {

            File dir = new File(directory, taskTitle);
            boolean override = fileOverride(dir);
            if(override == false) {
                return;
            }
            dir.mkdir();

            if (!pattern.isEmpty()) {
                // writes each class in a file in the directory
                for (PhoenixText clazz : pattern) {

                    File taskFile = new File(directory + "/" + taskTitle, clazz.getFullname());
                    writeInFile(taskFile, "/*" + description + "*/\n" + clazz.getText());

                }
            }

            if (!attachment.isEmpty()) {

                if (pattern.isEmpty()) {
                    File taskFile = new File(directory + "/" + taskTitle, taskTitle + ".java");
                    writeInFile(taskFile, descrFiltered);
                }

                for (PhoenixAttachment picture : attachment) {

                    File taskFile = new File(directory + "/" + taskTitle, picture.getFullname());
                    byte[] content = picture.getContent();
                    FileOutputStream fos;

                    fos = new FileOutputStream(taskFile);
                    fos.write(content);
                    fos.close();

                }
            }
        }

    }

    public void downloadChosenTaskSheet(String path, String taskSheetTitle) throws IOException {

        System.out.println(path + "  " + taskSheetTitle);
        PhoenixTaskSheet wantedTaskSheet = titleToTaskSheet(taskSheetTitle);

        // all tasks from selected PhoenixTaskSheet
        List<PhoenixTask> tasks = wantedTaskSheet.getTasks();

        // Überordner
        File file = new File(path, taskSheetTitle);
        if(fileOverride(file) == false) return;
        
        file.mkdir();
        
        for (int i = 0; i < tasks.size(); i++) {

            String title = tasks.get(i).getTitle();
            createTaskOnComputer(file, wantedTaskSheet, path, title);
        }

    }
    
    public void downloadChosenTask(String path, PhoenixTaskSheet taskSheet, String taskTitle) throws IOException {
        
  
        File file = new File(path, taskTitle);
        createTaskOnComputer(file, taskSheet, path, taskTitle);
        
    }

    @Override
    public void execute(String[] args) throws Exception {
        System.out.println("There is something to download!!!");
        String path = args[1];
        String taskSheetTitle = args[2];
        String taskTitle = args[4];
        
        if(args[3].equals("all")) {
            downloadChosenTaskSheet(path, taskSheetTitle);
        } else if (args[3].equals("task")) {
            downloadChosenTask(path, titleToTaskSheet(taskSheetTitle), taskTitle);
        }

    }

}
