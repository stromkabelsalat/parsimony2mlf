package name.stromkabelsalat.parsimony2mlf.parsimony.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.springframework.stereotype.Component;

@Component
public class JsoupComponent {
	public Element findAnyElementToBeRemoved(final Document document) {
		return Stream
				.of(this.findAnyOutsiderElement(document), this.findAnyEmptyElement(document))
				.filter(Objects::nonNull)
				.findAny()
				.orElse(null);
	}

	public Element findAnyEmptyElement(final Document document) {
		return document
				.getAllElements()
				.stream()
				.filter(element -> !List.of(
						"body",
						"area",
						"base",
						"br",
						"col",
						"embed",
						"hr",
						"img",
						"input",
						"link",
						"meta",
						"param",
						"source",
						"track",
						"wbr")
						.contains(element.tagName()))
				.filter(element -> !element.hasText())
				.filter(element -> element.children().isEmpty())
				.findAny()
				.orElse(null);
	}

	public Element findAnyOutsiderElement(final Document document) {
		return document
				.getAllElements()
				.stream()
				.filter(element -> List.of("br", "hr").contains(element.tagName()))
				.filter(element -> {
					final List<Node> nonBlankSiblings = element
							.parent()
							.childNodes()
							.stream()
							.filter(node -> !node.toString().isBlank())
							.toList();

					final int nonBlankSiblingsSize = nonBlankSiblings.size();

					final List<Node> nonBlankOutsiderSiblings = new ArrayList<>();

					if (nonBlankSiblingsSize > 0) {
						final Node firstNonBlankSibling = nonBlankSiblings.get(0);
						nonBlankOutsiderSiblings.add(firstNonBlankSibling);
					}

					if (nonBlankSiblingsSize > 1) {
						final Node lastNonBlankSibling = nonBlankSiblings.get(nonBlankSiblingsSize - 1);
						nonBlankOutsiderSiblings.add(lastNonBlankSibling);
					}

					return nonBlankOutsiderSiblings.contains(element);
				})
				.findAny()
				.orElse(null);
	}

	public void removeComments(final Node node) {
		node
				.childNodes()
				.stream()
				.filter(childNode -> "#comment".equals(childNode.nodeName()))
				.forEach(Node::remove);

		node
				.childNodes()
				.forEach(this::removeComments);
	}
}
