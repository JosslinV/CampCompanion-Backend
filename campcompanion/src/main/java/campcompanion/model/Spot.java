package campcompanion.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "spot")
public class Spot {
	
	//ATTRIBUTES
	@Id
	@Column(name = "spot_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name", length = 50, nullable = false)
	private String name;
	
	@Column(name = "latitude", nullable = false)
	private double latitude;
	
	@Column(name = "longitude", nullable = false)
	private double longitude;
	
	@Column(name = "img_path")
	private String imgPath;
	
	@Column(name = "accessibility_note", nullable = false)
	private int accessibilityNote;
	
	@Column(name = "location_note", nullable = false)
	private int locationNote;
	
	@Column(name = "utilities_note", nullable = false)
	private int utilitiesNote;
	
	@Column(name = "privacy_note", nullable = false)
	private int privacyNote;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "relatedSpot")
	private Set<Log> logs;
	
	@ManyToMany(mappedBy = "favoriteSpots")
	private Set<User> users;
	
	//CONSTRUCTOR
	public Spot() {}
	
	//GETTERS AND SETTERS
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public int getAccessibilityNote() {
		return accessibilityNote;
	}

	public void setAccessibilityNote(int accessibilityNote) {
		this.accessibilityNote = accessibilityNote;
	}

	public int getLocationNote() {
		return locationNote;
	}

	public void setLocationNote(int locationNote) {
		this.locationNote = locationNote;
	}

	public int getUtilitiesNote() {
		return utilitiesNote;
	}

	public void setUtilitiesNote(int utilitiesNote) {
		this.utilitiesNote = utilitiesNote;
	}

	public int getPrivacyNote() {
		return privacyNote;
	}

	public void setPrivacyNote(int privacyNote) {
		this.privacyNote = privacyNote;
	}

	public Set<Log> getLogs() {
		return logs;
	}

	public void setLogs(Set<Log> logs) {
		this.logs = logs;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	//METHODS

}
