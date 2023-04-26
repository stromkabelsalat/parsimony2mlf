package name.stromkabelsalat.parsimony2mlf.mlf.components;

import org.springframework.stereotype.Component;

@Component
public class MlfValuesComponent {
	public Integer category(final Short epochNum, final Long boardNum) {
		return boardNum.intValue() * 10 + epochNum;
	}

	public Integer id(final Short epochNum, final Long boardNum, final Integer num) {
		if (num == null) {
			return 0;
		}

		return this.category(epochNum, boardNum) * 100_000 + num;
	}
}
