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
import java.io.FileOutputStream;
import java.io.IOException;
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

public class Download extends Menu {
    
    private WebResource wrTask;

    public Download(String[] args) {
        wrTask = PhoenixTask.getResource(Core.client, Core.BASE_URL);
    }
    
    public void createTaskOnComputer(PhoenixTaskSheet taskSheet, PhoenixTask task, String path) {

        String taskTitle = task.getTitle();
        
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

                    try {
                        fos = new FileOutputStream(taskFile); 
                        fos.write(content);
                        fos.close();   
                    } catch (IOException e) {
                        System.err.println("While attaching files, an error has occured.");
                        e.printStackTrace();
                        return;
                    }
                    
                }
            }
        }

    }

    
    public void downloadChosenTaskSheet(PhoenixTaskSheet taskSheet, List<PhoenixTask> tasks, String path) {

       // Überordner
        File file = new File(path, taskSheet.getTitle());
        
        file.mkdir();
        
        for (int i = 0; i < tasks.size(); i++) {
            PhoenixTask task = tasks.get(i);
            createTaskOnComputer(taskSheet, task, path);
        }
        
        System.out.println("");
    }
    

    public void execute(PhoenixTaskSheet taskSheet, List<PhoenixTask> tasks, String path) {
        
        downloadChosenTaskSheet(taskSheet, tasks, path);
    }

}
