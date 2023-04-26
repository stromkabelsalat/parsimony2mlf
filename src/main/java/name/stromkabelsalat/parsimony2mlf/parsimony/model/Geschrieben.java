package name.stromkabelsalat.parsimony2mlf.parsimony.model;

import java.time.ZonedDateTime;

import lombok.Data;

@Data
public final class Geschrieben {
	private String name;
	private String nameColor;
	private String email;
	private ZonedDateTime date;
}
