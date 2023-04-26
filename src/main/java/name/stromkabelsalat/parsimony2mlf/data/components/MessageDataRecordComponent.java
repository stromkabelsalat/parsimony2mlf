package name.stromkabelsalat.parsimony2mlf.data.components;

import java.util.List;

import org.springframework.stereotype.Component;

import name.stromkabelsalat.parsimony2mlf.data.entities.DataRecord;
import name.stromkabelsalat.parsimony2mlf.data.entities.MessageDataRecord;

@Component
public class MessageDataRecordComponent {
	public MessageDataRecord messageDataRecord(final DataRecord dataRecord) {
		final List<String> nameComponents = dataRecord.nameComponents();

		final String epochNumString = nameComponents.get(2).replace("parsimony", "");
		final Short epochNum = Short.valueOf(epochNumString);

		final String boardNumString = nameComponents.get(4).replace("forum", "");
		final Long boardNum = Long.valueOf(boardNumString);

		final String numString = nameComponents.get(6).replace(".htm", "");
		final Integer num = Integer.valueOf(numString);

		final String content = dataRecord.content();

		return new MessageDataRecord(epochNum, boardNum, num, content);
	}
}
