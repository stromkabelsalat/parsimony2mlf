package name.stromkabelsalat.parsimony2mlf.parsimony.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import name.stromkabelsalat.parsimony2mlf.data.repositories.MessageDataRecordRepository;
import name.stromkabelsalat.parsimony2mlf.parsimony.components.ParsimonyMessageComponent;
import name.stromkabelsalat.parsimony2mlf.parsimony.entities.ParsimonyMessage;

@Repository
public class ParsimonyMessageRepository {
	private final List<ParsimonyMessage> parsimonyMessages;

	public ParsimonyMessageRepository(final MessageDataRecordRepository messageDataRecordRepository,
			final ParsimonyMessageComponent parsimonyMessageComponent) {
		this.parsimonyMessages = messageDataRecordRepository
				.findAll()
				.stream()
				.map(parsimonyMessageComponent::parsimonyMessage)
				.sorted()
				.toList();
	}

	public List<ParsimonyMessage> findAll() {
		return this.parsimonyMessages;
	}

	public boolean existsByEpochNumAndBoardNum(final Short epochNum, final Long boardNum) {
		return this.parsimonyMessages
				.stream()
				.filter(parsimonyMessage -> parsimonyMessage.getEpochNum().equals(epochNum))
				.anyMatch(parsimonyMessage -> parsimonyMessage.getBoardNum().equals(boardNum));
	}
}
