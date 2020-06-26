package campcompanion.model;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "appuser")
public class User {

	// ATTRIBUTES
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
		
	@Column(name = "username", length = 30, nullable = false)
	private String username;
	
	@Column(name = "password", length = 255, nullable = false)
	private String password;
	
	//@OneToMany(fetch = FetchType.LAZY, mappedBy = "spot")
	//private List<Spot> favoritesSpot;
	
	// CONSTRUCTOR
	public User() {
	}

	// GETTERS AND SETTERS

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

//	public List<Spot> getFavoritesSpot() {
//		return favoritesSpot;
//	}

//	public void setFavoritesSpot(List<Spot> favoritesSpot) {
//		this.favoritesSpot = favoritesSpot;
//	}

	// METHODS

}
