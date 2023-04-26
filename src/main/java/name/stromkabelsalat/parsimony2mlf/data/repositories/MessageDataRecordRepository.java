package name.stromkabelsalat.parsimony2mlf.data.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import name.stromkabelsalat.parsimony2mlf.data.components.MessageDataRecordComponent;
import name.stromkabelsalat.parsimony2mlf.data.entities.MessageDataRecord;

@Repository
public class MessageDataRecordRepository {
	// [1-9]\d{0,5}\.htm
	public static final String NUM_HTM_REGEX = "[1-9]\\d{0,5}\\.htm";

	private final List<MessageDataRecord> messageDataRecords;

	public MessageDataRecordRepository(final MessageDataRecordComponent messageDataRecordComponent,
			final DataRecordRepository dataRecordRepository) {

		this.messageDataRecords = dataRecordRepository
				.findAll()
				.stream()
				.filter(dataRecord -> dataRecord.nameComponents().size() == 7)
				.filter(dataRecord -> "parsimony".equals(dataRecord.nameComponents().get(1)))
				.filter(dataRecord -> dataRecord.nameComponents().get(2).startsWith("parsimony"))
				.filter(dataRecord -> "forum".equals(dataRecord.nameComponents().get(3)))
				.filter(dataRecord -> dataRecord.nameComponents().get(4).startsWith("forum"))
				.filter(dataRecord -> "messages".equals(dataRecord.nameComponents().get(5)))
				.filter(dataRecord -> dataRecord.nameComponents().get(6).matches(NUM_HTM_REGEX))
				.map(messageDataRecordComponent::messageDataRecord)
				.sorted()
				.toList();
	}

	public List<MessageDataRecord> findAll() {
		return this.messageDataRecords;
	}

	public Boolean existsByEpochNumAndBoardNumAndNum(final Short epochNum, final Long boardNum, final Integer num) {
		return this.messageDataRecords
				.stream()
				.filter(messageDataRecord -> messageDataRecord.epochNum().equals(epochNum))
				.filter(messageDataRecord -> messageDataRecord.boardNum().equals(boardNum))
				.anyMatch(messageDataRecord -> messageDataRecord.num().equals(num));
	}
}
