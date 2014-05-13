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
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.phoenix.rs.EntityUtil;
import de.phoenix.rs.entity.PhoenixSubmission;
import de.phoenix.rs.entity.PhoenixSubmissionResult;
import de.phoenix.rs.entity.PhoenixTask;
import de.phoenix.rs.key.KeyReader;
import de.phoenix.rs.key.SelectEntity;

public class Upload {

    private WebResource wrTask;
    private WebResource wrSubmit;

    public Upload() {
        wrTask = PhoenixTask.getResource(Core.client, Core.BASE_URL);
        wrSubmit = PhoenixTask.submitResource(Core.client, Core.BASE_URL);
    }

    public void execute(PhoenixTask task, List<String> path) throws Exception {
        
        List<File> file = new ArrayList<File>();
        for(int i = 0; i < path.size(); i++) {
            File pathfile = new File(path.get(i));
            file.add(i, pathfile);
        }

        SelectEntity<PhoenixTask> selectByTitle = new SelectEntity<PhoenixTask>().addKey("title", task.getTitle());

        ClientResponse post = wrTask.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, selectByTitle);
        System.out.println("Title is " + task.getTitle());
        List<PhoenixTask> list = EntityUtil.extractEntityList(post);
        PhoenixTask reqTask = list.get(0);

        PhoenixSubmission sub = new PhoenixSubmission(file, file);
        // connects a solution to a task
        post = wrSubmit.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, KeyReader.createAddTo(reqTask, Arrays.asList(sub)));
        System.out.println(post);
        System.out.println(post.getStatus());

        PhoenixSubmissionResult result = post.getEntity(PhoenixSubmissionResult.class);
        System.out.println(result.getStatusText());
    }
    

}
