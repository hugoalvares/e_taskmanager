package entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "task")
public class Task implements Serializable {
	 
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "idtask")
	private int idtask;
	@Column(name = "title")
	private String title;
	@Column(name = "status")	
	private char status;
	@Column(name = "imgpath")	
	private String imgPath;
	@Column(name = "latitude")	
	private Double latitude;
	@Column(name = "longitude")	
	private Double longitude;
	
	public Task () {
		
	}
	
	public Task (String title) {
		this.title = title;
		this.status = 'P';
	}
	
	public int getIdTask() {
		return idtask;
	}
	
	public void setIdTask(int idtask) {
		this.idtask = idtask;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public char getStatus() {
		return status;
	}
	
	public void setStatus(char status) {
		this.status = status;
	}
	
	public String getImgPath() {
		return imgPath;
	}
	
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public Double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	public Double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}	

}
