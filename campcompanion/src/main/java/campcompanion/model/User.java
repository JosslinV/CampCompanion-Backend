package campcompanion.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "appuser")
public class User {

	// ATTRIBUTES
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
		
	@Column(name = "username", length = 30, nullable = false)
	private String username;
	
	@Column(name = "password", length = 255, nullable = false)
	private String password;
	
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "favorite_spot", 
        joinColumns = { @JoinColumn(name = "spot_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
	private Set<Spot> favoriteSpots;
	
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

	public Set<Spot> getFavoriteSpots() {
		return favoriteSpots;
	}

	public void setFavoriteSpots(Set<Spot> favoriteSpots) {
		this.favoriteSpots = favoriteSpots;
	}

	// METHODS

}
