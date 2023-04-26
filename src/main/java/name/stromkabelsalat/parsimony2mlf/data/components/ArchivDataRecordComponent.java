package name.stromkabelsalat.parsimony2mlf.data.components;

import java.util.List;

import org.springframework.stereotype.Component;

import name.stromkabelsalat.parsimony2mlf.data.entities.ArchivDataRecord;
import name.stromkabelsalat.parsimony2mlf.data.entities.DataRecord;

@Component
public class ArchivDataRecordComponent {
	public ArchivDataRecord archivDataRecord(final DataRecord dataRecord) {
		final List<String> nameComponents = dataRecord.nameComponents();

		final String epochNumString = nameComponents.get(2).replace("parsimony", "");
		final Short epochNum = Short.valueOf(epochNumString);

		final String boardNumString = nameComponents.get(4).replace("forum", "");
		final Long boardNum = Long.valueOf(boardNumString);

		final String content = dataRecord.content();

		return new ArchivDataRecord(epochNum, boardNum, content);
	}
}
