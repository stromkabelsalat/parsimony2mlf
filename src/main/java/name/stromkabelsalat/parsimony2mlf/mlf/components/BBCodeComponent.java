package name.stromkabelsalat.parsimony2mlf.mlf.components;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import name.stromkabelsalat.parsimony2mlf.components.UniqueStringComponent;
import name.stromkabelsalat.parsimony2mlf.mlf.colors.repositories.ValidColorStringRepository;

@Component
public class BBCodeComponent {
	private final ValidColorStringRepository validColorStringRepository;

	private final String rightBreakMarker;
	private final String leftSpaceMarker;
	private final String rightSpaceMarker;
	private final String lineSeparatorMarker;

	public BBCodeComponent(final ValidColorStringRepository validColorStringRepository,
			final UniqueStringComponent uniqueStringComponent) {
		this.validColorStringRepository = validColorStringRepository;

		this.rightBreakMarker = uniqueStringComponent.uniqueString();
		this.leftSpaceMarker = uniqueStringComponent.uniqueString();
		this.rightSpaceMarker = uniqueStringComponent.uniqueString();
		this.lineSeparatorMarker = uniqueStringComponent.uniqueString();
	}

	public String bbCode(final String body) {
		final Document document = Jsoup.parse(body);

		document
				.getAllElements()
				.stream()
				.filter(element -> List.of("p", "center", "td").contains(element.tagName()))
				.forEach(element -> element
						.before(this.leftSpaceMarker)
						.before(this.lineSeparatorMarker)
						.before(this.rightSpaceMarker)
						.after(this.lineSeparatorMarker)
						.after(this.leftSpaceMarker));

		document
				.getAllElements()
				.stream()
				.filter(element -> List.of("ol", "ul", "dl").contains(element.tagName()))
				.forEach(element -> element
						.before(this.leftSpaceMarker)
						.before(this.lineSeparatorMarker)
						.before("[list]")
						.before(this.lineSeparatorMarker)
						.before(this.rightSpaceMarker)
						.after(this.lineSeparatorMarker)
						.after("[/list]")
						.after(this.leftSpaceMarker));

		document
				.getAllElements()
				.stream()
				.filter(element -> List.of("li", "dt").contains(element.tagName()))
				.filter(element -> List.of("ol", "ul", "dl").contains(element.parent().tagName()))
				.forEach(element -> element
						.before("[*]")
						.before(this.rightSpaceMarker)
						.after(this.rightSpaceMarker)
						.after(this.lineSeparatorMarker)
						.after(this.leftSpaceMarker));

		document
				.getAllElements()
				.stream()
				.filter(element -> List.of("h1", "h2", "h3", "h4", "h5", "h6").contains(element.tagName()))
				.forEach(element -> element
						.before(this.leftSpaceMarker)
						.before("[size=large][b]")
						.before(this.rightSpaceMarker)
						.after("[/b][/size]")
						.after(this.leftSpaceMarker));

		document
				.getElementsByTag("hr")
				.forEach(element -> element
						.before(this.leftSpaceMarker)
						.before(this.lineSeparatorMarker)
						.before("--")
						.before(this.rightSpaceMarker)
						.after(this.lineSeparatorMarker));

		document
				.getElementsByTag("blockquote")
				.forEach(element -> element
						.before(this.leftSpaceMarker)
						.before("[quote]")
						.before(this.rightBreakMarker)
						.after("[/quote]")
						.after(this.leftSpaceMarker));

		document
				.getElementsByTag("pre")
				.forEach(element -> element
						.before(this.leftSpaceMarker)
						.before(this.lineSeparatorMarker)
						.before("[code]")
						.before(this.rightSpaceMarker)
						.after("[/code]")
						.after(this.leftSpaceMarker));

		document
				.getAllElements()
				.stream()
				.filter(element -> element.hasAttr("color"))
				.forEach(element -> element
						.before("[color=" + this.validColorStringRepository.findMostSimilarTo(element.attr("color"))
								+ "]")
						.after("[/color]"));

		document
				.getElementsByTag("br")
				.forEach(element -> element
						.before(this.lineSeparatorMarker)
						.before(this.rightSpaceMarker));

		document
				.getElementsByTag("a")
				.stream()
				.filter(element -> element.hasAttr("href"))
				.forEach(element -> element
						.before("[link=" + element.attr("href") + "]")
						.after("[/link]"));

		document
				.getElementsByTag("img")
				.stream()
				.filter(element -> element.hasAttr("src"))
				.forEach(element -> element.before("[img]" + element.attr("src") + "[/img]"));

		document
				.getAllElements()
				.stream()
				.filter(element -> List.of("b", "i", "u", "quote").contains(element.tagName()))
				.forEach(element -> element
						.before("[" + element.tagName() + "]")
						.after("[/" + element.tagName() + "]"));

		document
				.getElementsByTag("strong")
				.forEach(element -> element
						.before("[b]")
						.after("[/b]"));

		document
				.getAllElements()
				.stream()
				.filter(element -> List.of("blink", "marquee").contains(element.tagName()))
				.forEach(element -> element
						.before(this.lineSeparatorMarker)
						.before("[b]")
						.before(this.rightSpaceMarker)
						.after(this.lineSeparatorMarker)
						.after("[/b]")
						.after(this.leftSpaceMarker));

		document
				.getElementsByTag("em")
				.forEach(element -> element
						.before("[i]")
						.after("[/i]"));

		document
				.getElementsByTag("big")
				.forEach(element -> element
						.before("[size=large]")
						.after("[/size]"));

		document
				.getElementsByTag("small")
				.forEach(element -> element
						.before("[size=small]")
						.after("[/size]"));

		document
				.getElementsByTag("code")
				.forEach(element -> element
						.before("[inlinecode]")
						.after("[/inlinecode]"));

		document
				.getElementsByTag("legend")
				.forEach(element -> element
						.after(this.lineSeparatorMarker));

		return document
				.text()
				.replace(this.lineSeparatorMarker, System.lineSeparator())
				.replace(" " + this.leftSpaceMarker, "")
				.replace(this.leftSpaceMarker, "")
				.replace(this.rightSpaceMarker + " ", "")
				.replace(this.rightSpaceMarker, "")
				.replace(this.rightBreakMarker + System.lineSeparator(), "")
				.replace(this.rightBreakMarker, "")
				.strip();
	}
}
