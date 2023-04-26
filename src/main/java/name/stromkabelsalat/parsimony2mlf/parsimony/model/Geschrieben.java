package name.stromkabelsalat.parsimony2mlf.parsimony.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Geschrieben {
	private String name;
	private String nameColor;
	private String email;
	private LocalDateTime date;
}
