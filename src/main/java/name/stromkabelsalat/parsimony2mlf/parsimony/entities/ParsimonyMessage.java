package name.stromkabelsalat.parsimony2mlf.parsimony.entities;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ParsimonyMessage implements Comparable<ParsimonyMessage> {
	private Short epochNum;
	private Long boardNum;
	private Integer num;
	private Integer parentNum;
	private String name;
	private String nameColor;
	private String email;
	private String subject;
	private LocalDateTime date;
	private String img;
	private String url;
	private String urlTitle;
	private String body;

	@Override
	public int compareTo(final ParsimonyMessage other) {
		if (!this.epochNum.equals(other.epochNum)) {
			return this.epochNum.compareTo(other.epochNum);
		}

		if (!this.boardNum.equals(other.boardNum)) {
			return this.boardNum.compareTo(other.boardNum);
		}

		return this.num.compareTo(other.num);
	}
}
