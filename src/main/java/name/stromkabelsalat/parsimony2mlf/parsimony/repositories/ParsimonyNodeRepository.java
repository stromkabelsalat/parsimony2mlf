package name.stromkabelsalat.parsimony2mlf.parsimony.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;

import name.stromkabelsalat.parsimony2mlf.data.repositories.ArchivDataRecordRepository;
import name.stromkabelsalat.parsimony2mlf.parsimony.components.NumComponent;
import name.stromkabelsalat.parsimony2mlf.parsimony.entities.ParsimonyNode;

@Repository
public class ParsimonyNodeRepository {
	private final List<ParsimonyNode> parsimonyNodes = new ArrayList<>();

	public ParsimonyNodeRepository(final ArchivDataRecordRepository archivDataRecordRepository,
			final NumComponent numComponent) {
		archivDataRecordRepository.findAll().forEach(archivDataRecord -> {
			final Short epochNum = archivDataRecord.epochNum();
			final Long boardNum = archivDataRecord.boardNum();

			final String content = archivDataRecord.content();
			final Document document = Jsoup.parse(content);

			final Elements liElements = document.getElementsByTag("li");

			for (final Element liElement : liElements) {
				final Integer num = numComponent.num(liElement);

				final Element parentLiElement = liElement
						.parent()
						.parent();

				final Integer parentNum = Optional
						.ofNullable(parentLiElement)
						.filter(parentLiElementCandidate -> "li".equals(parentLiElementCandidate.tagName()))
						.map(numComponent::num)
						.orElse(null);

				final ParsimonyNode parsimonyNode = new ParsimonyNode(epochNum, boardNum, num, parentNum);

				this.parsimonyNodes.add(parsimonyNode);
			}
		});
	}

	public Optional<ParsimonyNode> findByEpochNumAndBoardNumAndNum(final Short epochNum, final Long boardNum,
			final Integer num) {
		return this.parsimonyNodes
				.stream()
				.filter(parsimonyNode -> parsimonyNode.epochNum().equals(epochNum))
				.filter(parsimonyNode -> parsimonyNode.boardNum().equals(boardNum))
				.filter(parsimonyNode -> parsimonyNode.num().equals(num))
				.findAny();
	}

}
