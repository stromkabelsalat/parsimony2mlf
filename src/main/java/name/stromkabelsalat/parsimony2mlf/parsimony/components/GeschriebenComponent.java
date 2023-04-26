package name.stromkabelsalat.parsimony2mlf.parsimony.components;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import name.stromkabelsalat.parsimony2mlf.components.LeftRightComponent;
import name.stromkabelsalat.parsimony2mlf.model.LeftRight;
import name.stromkabelsalat.parsimony2mlf.parsimony.model.Geschrieben;

@Component
@RequiredArgsConstructor
public class GeschriebenComponent {
	private final LeftRightComponent leftRightComponent;

	public Geschrieben geschrieben(final String geschriebenInner) {
		final Geschrieben geschrieben = new Geschrieben();

		final LeftRight geschriebenLeftRight = this.leftRightComponent.leftRight(geschriebenInner, " am ");

		Optional.of(geschriebenLeftRight.left()).ifPresent(rawGeschriebenVonAutor -> {
			final String rawAutor = rawGeschriebenVonAutor.replaceFirst("Geschrieben von ", "");
			final Document autorDocument = Jsoup.parse(rawAutor);

			final String nameColor = Optional
					.of(autorDocument.getElementsByTag("font").attr("color"))
					.filter(string -> !string.isEmpty())
					.orElse(null);
			geschrieben.setNameColor(nameColor);

			final String name = Optional
					.of(autorDocument.text())
					.filter(nameXName -> !nameXName.isBlank())
					.map(nameXName -> {
						if (nameColor == null) {
							return nameXName;
						}

						final int length = nameXName.length();

						if (length % 2 == 0) {
							return nameXName;
						}

						// 01234

						final int index = (length - 1) / 2;

						if (nameXName.charAt(index) != ' ') {
							return nameXName;
						}

						final String left = nameXName.substring(0, index);
						final String right = nameXName.substring(index + 1);

						if (!left.equals(right)) {
							return nameXName;
						}

						return left;
					})
					.orElse(null);

			geschrieben.setName(name);

			final Element aElement = autorDocument.getElementsByTag("a").first();
			final String email = Optional
					.ofNullable(aElement)
					.map(_ -> aElement.attr("href").replaceFirst("mailto:", ""))
					.orElse(null);
			geschrieben.setEmail(email);
		});

		Optional.of(geschriebenLeftRight.right()).ifPresent(rawDateAtTimeColon -> {
			final String dateAtTimeColon = Jsoup.parse(rawDateAtTimeColon).text();
			final String dateTimeColon = dateAtTimeColon.replaceFirst(" at ", " ").replaceFirst(" um ", " ");
			final String dateTime = dateTimeColon.substring(0, dateTimeColon.length() - 1);

			final LeftRight dateTimeLeftRight = this.leftRightComponent.leftRight(dateTime, " ");

			final String date = dateTimeLeftRight
					.left()
					.replace("Maerz", "März")
					.replace("2099", "1999")
					.replace("19100", "2000");
			final DateTimeFormatter dateTimeformatter = DateTimeFormatter.ofPattern("dd. MMMM yyyy", Locale.GERMANY);
			final LocalDate localDate = LocalDate.parse(date, dateTimeformatter);

			final String rawTime = dateTimeLeftRight.right();
			final String time = Optional
					.of(rawTime)
					.filter(timeCandidate -> timeCandidate.length() < 2 + 1 + 2 + 1 + 2)
					.map(timeCandidate -> timeCandidate + ":00")
					.orElse(rawTime);
			final LocalTime localTime = LocalTime.parse(time);

			final LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
			final ZoneId zoneId = ZoneId.of("Europe/Berlin");

			final ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, zoneId);

			geschrieben.setDate(zonedDateTime);
		});

		return geschrieben;
	}
}
