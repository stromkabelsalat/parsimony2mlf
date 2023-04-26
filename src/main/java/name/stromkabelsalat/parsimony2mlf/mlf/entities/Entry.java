package name.stromkabelsalat.parsimony2mlf.mlf.entities;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "mlf2_entries")
@Data
public class Entry implements Comparable<Entry> {
	@Id
	private Integer id;

	@Column(nullable = false)
	private Integer pid;

	@Column(nullable = false)
	private Integer tid;

	@Column(nullable = false)
	private String uniqid;

	@Column(nullable = false)
	private ZonedDateTime time;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String subject;

	@Column(nullable = false)
	private Integer category;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String ip;

	@Column(nullable = false)
	private String text;

	@Column(nullable = false)
	private String editKey;

	@Column(nullable = false)
	private String hp;

	@Column(nullable = false)
	private String location;

	@Override
	public int compareTo(final Entry other) {
		return this.id.compareTo(other.id);
	}
}
