package campcompanion.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Entity
@Table(name = "log")
public class Log {

	// ATTRIBUTES
	@Id
	@Column(name = "log_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "comment")
	private String comment;
	
	@Column(name = "note")
	private int note;
	
	@ManyToOne()
	@JoinColumn(name="spot_id", nullable=false)
	private Spot relatedSpot;
	
	//CONSTRUCTOR
	public Log() {}

	// GETTERS AND SETTERS
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getNote() {
		return note;
	}

	public void setNote(int note) {
		this.note = note;
	}

	public Spot getRelatedSpot() {
		return relatedSpot;
	}

	public void setRelatedSpot(Spot relatedSpot) {
		this.relatedSpot = relatedSpot;
	}

	// METHODS

}
