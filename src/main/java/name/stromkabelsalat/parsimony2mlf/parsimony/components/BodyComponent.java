package name.stromkabelsalat.parsimony2mlf.parsimony.components;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import name.stromkabelsalat.parsimony2mlf.components.UniqueStringComponent;

@Component
public class BodyComponent {
	private final JsoupComponent jsoupComponent;
	private final String elementInPreMarker;

	public BodyComponent(final JsoupComponent jsoupComponent, final UniqueStringComponent uniqueStringComponent) {
		this.jsoupComponent = jsoupComponent;
		this.elementInPreMarker = uniqueStringComponent.uniqueString();
	}

	public String body(final String rawBody) {
		final Document document = Jsoup.parse(rawBody);

		this.jsoupComponent.removeComments(document);

		Element elementToBeRemoved;
		while ((elementToBeRemoved = this.jsoupComponent.findAnyElementToBeRemoved(document)) != null) {
			elementToBeRemoved.remove();
		}

		final Element firstChild = document
				.body()
				.children()
				.first();

		if (firstChild != null && "blockquote".equals(firstChild.tagName())
				&& firstChild.nextElementSibling() == null) {
			document
					.body()
					.empty()
					.appendChildren(firstChild.childNodes());
		}

		document.getElementsByTag("pre").forEach(pre -> pre.getAllElements().forEach(element -> {
			element.before(this.elementInPreMarker);
			element.after(this.elementInPreMarker);

			if (element.childNodeSize() > 0) {
				element.prepend(this.elementInPreMarker);
				element.append(this.elementInPreMarker);
			}
		}));

		final String resultString = Jsoup
				.parse(document.html())
				.body()
				.html()
				.replace("<p> ", "<p>")
				.replace(" </p>", "</p>")
				.replace("<br> ", "<br>")
				.replace(" <br>", "<br>")
				.replace(this.elementInPreMarker + "<br>" + this.elementInPreMarker + System.lineSeparator(),
						System.lineSeparator())
				.replace(this.elementInPreMarker, "")
				.replace("<p>&gt;", "<p>&gt; ")
				.replace("<p>&gt;  ", "<p>&gt; ")
				.replace("<br>&gt;", "<br>&gt; ")
				.replace("<br>&gt;  ", "<br>&gt; ");

		return Jsoup.parse(resultString).body().html();
	}
}
