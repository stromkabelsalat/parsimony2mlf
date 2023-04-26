package name.stromkabelsalat.parsimony2mlf.parsimony.components;

import java.time.ZonedDateTime;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.stromkabelsalat.parsimony2mlf.data.entities.MessageDataRecord;
import name.stromkabelsalat.parsimony2mlf.data.repositories.MessageDataRecordRepository;
import name.stromkabelsalat.parsimony2mlf.parsimony.entities.ParsimonyMessage;
import name.stromkabelsalat.parsimony2mlf.parsimony.entities.ParsimonyNode;
import name.stromkabelsalat.parsimony2mlf.parsimony.model.Geschrieben;
import name.stromkabelsalat.parsimony2mlf.parsimony.model.InnerOuter;
import name.stromkabelsalat.parsimony2mlf.parsimony.repositories.ParsimonyNodeRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class ParsimonyMessageComponent {
	private final BodyComponent bodyComponent;
	private final GeschriebenComponent geschriebenComponent;
	private final InnerOuterComponent innerOuterComponent;
	private final MessageDataRecordRepository messageDataRecordRepository;
	private final ParsimonyNodeRepository parsimonyNodeRepository;
	private final SeparatorComponent separatorComponent;
	private final QuirksReplacerComponent quirksReplacerComponent;

	public ParsimonyMessage parsimonyMessage(final MessageDataRecord messageDataRecord) {
		final ParsimonyMessage parsimonyMessage = new ParsimonyMessage();

		final Short epochNum = messageDataRecord.epochNum();
		parsimonyMessage.setEpochNum(epochNum);

		final Long boardNum = messageDataRecord.boardNum();
		parsimonyMessage.setBoardNum(boardNum);

		final Integer num = messageDataRecord.num();
		parsimonyMessage.setNum(num);

		log.info("Creating ParsimonyMessage {} {} {}", epochNum, boardNum, num);

		final String contentWithQuirks = messageDataRecord.content();
		final String contentWithoutQuirks = this.quirksReplacerComponent.replaceQuirks(contentWithQuirks);
		final String[] sections = contentWithoutQuirks.split(this.separatorComponent.getSeparator());

		Optional.of(sections[0]).ifPresent(section -> {
			final String subject = Jsoup.parse(section).getElementsByTag("h1").first().text();
			parsimonyMessage.setSubject(subject);
		});

		Optional.of(sections[2]).ifPresent(section -> {
			String rawBody = section;

			final InnerOuter antwortInnerOuter = this.innerOuterComponent.innerOuter(rawBody,
					InnerOuterComponent.ANTWORT_REGEX);

			final Integer parentNum = Optional
					.ofNullable(antwortInnerOuter.inner())
					.map(antwortInner -> Jsoup.parse(antwortInner).getElementsByTag("a").first())
					.map(aElement -> aElement.attr("href").replaceFirst(".htm", ""))
					.map(Integer::valueOf)
					.filter(parentNumCandidate -> this.messageDataRecordRepository
							.existsByEpochNumAndBoardNumAndNum(epochNum, boardNum, parentNumCandidate))
					.orElse(this.parsimonyNodeRepository
							.findByEpochNumAndBoardNumAndNum(epochNum, boardNum, num)
							.map(ParsimonyNode::parentNum)
							.orElse(null));

			parsimonyMessage.setParentNum(parentNum);

			rawBody = antwortInnerOuter.outer();

			final InnerOuter geschriebenInnerOuter = this.innerOuterComponent.innerOuter(rawBody,
					InnerOuterComponent.GESCHRIEBEN_REGEX);

			Optional.of(geschriebenInnerOuter.inner()).ifPresent(geschriebenInner -> {
				final Geschrieben geschrieben = this.geschriebenComponent.geschrieben(geschriebenInner);

				final String name = geschrieben.getName();
				final String nameColor = geschrieben.getNameColor();
				final String email = geschrieben.getEmail();
				final ZonedDateTime date = geschrieben.getDate();

				parsimonyMessage.setName(name);
				parsimonyMessage.setNameColor(nameColor);
				parsimonyMessage.setEmail(email);
				parsimonyMessage.setDate(date);
			});

			rawBody = geschriebenInnerOuter.outer();

			final InnerOuter imgInnerOuter = this.innerOuterComponent.innerOuter(rawBody,
					InnerOuterComponent.IMG_REGEX);

			Optional.ofNullable(imgInnerOuter.inner()).ifPresent(imgInner -> {
				final String img = Jsoup.parse(imgInner).getElementsByTag("img").first().attr("src");
				parsimonyMessage.setImg(img);
			});

			rawBody = imgInnerOuter.outer();

			final InnerOuter urlInnerOuter = this.innerOuterComponent.innerOuter(rawBody,
					InnerOuterComponent.URL_REGEX);

			Optional.ofNullable(urlInnerOuter.inner()).ifPresent(urlInner -> {
				final Element aElement = Jsoup.parse(urlInner).getElementsByTag("a").first();

				final String url = aElement.attr("href");
				parsimonyMessage.setUrl(url);

				final String urlTitle = aElement.text();
				parsimonyMessage.setUrlTitle(urlTitle);
			});

			rawBody = urlInnerOuter.outer();

			final String body = this.bodyComponent.body(rawBody);
			parsimonyMessage.setBody(body);
		});

		return parsimonyMessage;
	}
}
