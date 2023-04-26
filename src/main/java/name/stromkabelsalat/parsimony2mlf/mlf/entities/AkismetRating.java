package name.stromkabelsalat.parsimony2mlf.mlf.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "mlf2_akismet_rating")
@Data
public class AkismetRating implements Comparable<AkismetRating> {
	@Id
	private Integer eid;

	@Override
	public int compareTo(final AkismetRating other) {
		return this.eid.compareTo(other.eid);
	}
}
