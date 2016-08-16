package server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import entities.Task;
import hibernate.HibernateUtil;

@RestController
public class Controller {
	
	private String imgDir = "C:/workfolder/strider/e_taskmanager/site/images/";
	
	private String getTasksJson(Session session) {
		List<Task> list = session.createCriteria(Task.class).list();
		Gson gson = new Gson();
	    return gson.toJson(list);
	}

	@CrossOrigin(origins = "http://localhost:8888")
	@RequestMapping("/createtask")
    public String newTask(@RequestParam(value="title") String title) {
    	Task task = new Task(title);
    	Session session = HibernateUtil.getSessionFactory().openSession();
	    session.beginTransaction();
	    session.save(task);
	    session.getTransaction().commit();
	    String jsonTasks = getTasksJson(session);
	    session.close();
    	return jsonTasks;
    }
    
    @CrossOrigin(origins = "http://localhost:8888")
    @RequestMapping("/gettasks")
    public String getTasks() {
    	Session session = HibernateUtil.getSessionFactory().openSession();
    	String jsonTasks = getTasksJson(session);
	    session.close();
    	return jsonTasks;
    }
    
    @CrossOrigin(origins = "http://localhost:8888")
    @RequestMapping("/deletetask")
    public String deleteTask(@RequestParam(value="idtask") int idTask) {
    	Session session = HibernateUtil.getSessionFactory().openSession();
	    session.beginTransaction();
	    Task task = (Task) session.get(Task.class, idTask);
	    session.delete(task);
	    session.getTransaction().commit();
	    String jsonTasks = getTasksJson(session);
	    session.close();
    	return jsonTasks;
    }
    
    @CrossOrigin(origins = "http://localhost:8888")
    @RequestMapping("/concludetask")
    public String concludeTask(@RequestParam(value="idtask") int idTask, @RequestParam(value="imgpath") String imgPath, @RequestParam(value="latitude") Double latitude, @RequestParam(value="longitude") Double longitude) {
    	Session session = HibernateUtil.getSessionFactory().openSession();
	    session.beginTransaction();
	    Task task = (Task) session.get(Task.class, idTask);
	    task.setStatus('D');
	    task.setImgPath(imgPath);
	    task.setLatitude(latitude);
	    task.setLongitude(longitude);
	    session.save(task);
	    session.getTransaction().commit();
	    String jsonTasks = getTasksJson(session);
	    session.close();
    	return jsonTasks;
    }
    
    @RequestMapping(value = "/uploadimage", method = RequestMethod.POST)
	public String uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("idtask") int idTask) {
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				File serverFile = new File(imgDir + Integer.toString(idTask) + ".jpg");
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
				return "{'result':'ok'}";
			} catch (Exception e) {
				return "{'result':'not ok'}";
			}
		} else {
			return "{'result':'empty file'}";
		}
	}
    
}