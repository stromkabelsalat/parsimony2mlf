package name.stromkabelsalat.parsimony2mlf.data.entities;

public record MessageDataRecord(Short epochNum, Long boardNum, Integer num, String content)
		implements Comparable<MessageDataRecord> {
	@Override
	public int compareTo(final MessageDataRecord other) {
		if (!this.epochNum.equals(other.epochNum)) {
			return this.epochNum.compareTo(other.epochNum);
		}

		if (!this.boardNum.equals(other.boardNum)) {
			return this.boardNum.compareTo(other.boardNum);
		}

		return this.num.compareTo(other.num);
	}
}
