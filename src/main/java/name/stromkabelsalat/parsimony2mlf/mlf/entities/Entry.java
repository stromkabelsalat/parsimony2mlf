package name.stromkabelsalat.parsimony2mlf.mlf.entities;

import java.time.LocalDateTime;

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

	private Integer pid;

	private Integer tid;

	private String uniqid;

	private LocalDateTime time;

	private String name;

	private String subject;

	private Integer category;

	private String email;

	private String ip;

	private String text;

	private String editKey;

	private String hp;

	private String location;

	@Override
	public int compareTo(final Entry other) {
		return this.id.compareTo(other.id);
	}
}
