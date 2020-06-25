package campcompanion.model;

import java.util.List;

public class User {

	// ATTRIBUTES
	private String username;
	private String password;
	private List<Spot> favoritesSpot;

	// GETTERS AND SETTERS
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

	public List<Spot> getFavoritesSpot() {
		return favoritesSpot;
	}

	public void setFavoritesSpot(List<Spot> favoritesSpot) {
		this.favoritesSpot = favoritesSpot;
	}

	// METHODS

}
