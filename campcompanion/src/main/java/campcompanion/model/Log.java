package campcompanion.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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

	// GETTERS AND SETTERS
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
