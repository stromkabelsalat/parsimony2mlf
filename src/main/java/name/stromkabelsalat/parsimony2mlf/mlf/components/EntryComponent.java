package name.stromkabelsalat.parsimony2mlf.mlf.components;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import name.stromkabelsalat.parsimony2mlf.components.UniqueStringComponent;
import name.stromkabelsalat.parsimony2mlf.mlf.entities.Entry;
import name.stromkabelsalat.parsimony2mlf.mlf.model.IdTid;
import name.stromkabelsalat.parsimony2mlf.parsimony.entities.ParsimonyMessage;

@Component
@RequiredArgsConstructor
@Slf4j
public class EntryComponent {
	private final MlfValuesComponent mlfValuesComponent;
	private final MainTextComponent mainTextComponent;
	private final UniqueStringComponent uniqueStringComponent;
	private final LinkReplacerComponent linkReplacerComponent;

	private final List<IdTid> idTids = new ArrayList<>();

	public Entry entry(final ParsimonyMessage parsimonyMessage) {
		final Integer id = this.mlfValuesComponent.id(parsimonyMessage.getEpochNum(), parsimonyMessage.getBoardNum(),
				parsimonyMessage.getNum());

		log.info("Creating Entry {}", id);

		final Integer pid = Optional
				.ofNullable(parsimonyMessage.getParentNum())
				.map(parentNum -> this.mlfValuesComponent.id(parsimonyMessage.getEpochNum(),
						parsimonyMessage.getBoardNum(), parentNum))
				.orElse(0);

		final Integer tid = Optional
				.of(pid)
				.filter(pidCandidate -> pidCandidate != 0)
				.map(pidCandidate -> this.idTids
						.stream()
						.filter(idTid -> idTid.id().equals(pidCandidate))
						.findAny()
						.map(IdTid::tid)
						.orElse(null))
				.orElse(id);

		final IdTid idTid = new IdTid(id, tid);
		this.idTids.add(idTid);

		final String uniqid = this.uniqueStringComponent.uniqueString(13);
		final ZonedDateTime time = parsimonyMessage.getDate();
		final String name = Optional.ofNullable(parsimonyMessage.getName()).orElse("");

		final String subject = Optional
				.of(parsimonyMessage.getSubject())
				.map(subjectCandidate -> {
					if (subjectCandidate.startsWith("Re: ")) {
						return subjectCandidate.replaceFirst("Re: ", "").trim();
					} else {
						return subjectCandidate;
					}
				})
				.orElse("");

		final Integer category = this.mlfValuesComponent.category(parsimonyMessage.getEpochNum(),
				parsimonyMessage.getBoardNum());
		final String email = Optional.ofNullable(parsimonyMessage.getEmail()).orElse("");
		final String ip = "127.0.0.1";

		final StringBuilder textBuilder = new StringBuilder();

		Optional
				.ofNullable(parsimonyMessage.getImg())
				.ifPresent(img -> textBuilder
						.append("🖼️ [img]" + img + "[/img]")
						.append(System.lineSeparator()).append(System.lineSeparator()));

		Optional
				.of(this.mainTextComponent.mainText(parsimonyMessage.getBody()))
				.filter(mainText -> !mainText.isBlank())
				.ifPresent(mainText -> textBuilder
						.append(mainText)
						.append(System.lineSeparator())
						.append(System.lineSeparator()));

		Optional
				.ofNullable(parsimonyMessage.getUrl())
				.ifPresent(url -> textBuilder
						.append("🏠 [b][link=" + url + "]" + parsimonyMessage.getUrlTitle() + "[/link][/b]"));

		final String textWithOriginalLinks = textBuilder.toString().strip();
		final String text = this.linkReplacerComponent.replaceLinks(textWithOriginalLinks,
				parsimonyMessage.getEpochNum(), parsimonyMessage.getBoardNum());

		final String editKey = "";
		final String hpWithOriginalLink = Optional.ofNullable(parsimonyMessage.getUrl()).orElse("");
		final String hp = this.linkReplacerComponent.replaceLinks(hpWithOriginalLink, parsimonyMessage.getEpochNum(),
				parsimonyMessage.getBoardNum());

		final String location = Optional
				.ofNullable(parsimonyMessage.getNameColor())
				.map(_ -> "Stammposter")
				.orElse("");

		final Entry entry = new Entry();
		entry.setId(id);
		entry.setPid(pid);
		entry.setTid(tid);
		entry.setUniqid(uniqid);
		entry.setTime(time);
		entry.setName(name);
		entry.setSubject(subject);
		entry.setCategory(category);
		entry.setEmail(email);
		entry.setIp(ip);
		entry.setText(text);
		entry.setEditKey(editKey);
		entry.setHp(hp);
		entry.setLocation(location);

		return entry;
	}
}
