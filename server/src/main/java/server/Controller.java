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
    public String deleteTask(@RequestParam(value="idtask") int idTask, @RequestParam(value="imgpath") String imgPath) {
    	Session session = HibernateUtil.getSessionFactory().openSession();
	    session.beginTransaction();
	    Task task = (Task) session.get(Task.class, idTask);
	    task.setStatus('D');
	    task.setImgPath(imgPath);
	    session.save(task);
	    session.getTransaction().commit();
	    String jsonTasks = getTasksJson(session);
	    session.close();
    	return jsonTasks;
    }
    
    /*
    @CrossOrigin(origins = "http://localhost:8888")
    @RequestMapping(value = { "/uploadimage" }, method = RequestMethod.POST, produces = "application/json")
	public String uploadImage(final HttpServletRequest request) throws IOException {
		InputStream is = null;
		is = request.getInputStream();
		byte[] bytes = IOUtils.toByteArray(is);
		System.out.println("read " + bytes.length + " bytes.");
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		String jsonTasks = getTasksJson(session);
	    session.close();
    	return jsonTasks;
	}
    
    @RequestMapping(value = "/uploadimage", method = RequestMethod.POST)
	public @ResponseBody
	String uploadImage(@RequestParam("name") String name, @RequestParam("file") MultipartFile file) {
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();
				String rootPath = Controller.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
				File dir = new File(rootPath + File.separator + "tmpFiles");
				if (!dir.exists()){
					dir.mkdirs();
				}
				File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
				return "{'result':'1'}";
			} catch (Exception e) {
				return "{'result':'0'}";
			}
		} else {
			return "{'result':'-1'}";
		}
	}    
    */
    
}