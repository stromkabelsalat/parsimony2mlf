package name.stromkabelsalat.parsimony2mlf.mlf.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "mlf2_b8_rating")
@Data
public class B8Rating implements Comparable<B8Rating> {
	@Id
	private Integer eid;

	@Override
	public int compareTo(final B8Rating other) {
		return this.eid.compareTo(other.eid);
	}
}
