package name.stromkabelsalat.parsimony2mlf.mlf.colors.model;

public record Color(int r, int g, int b) {
	public double distanceTo(final Color other) {
		final int rDistance = this.r - other.r;
		final int gDistance = this.g - other.g;
		final int bDistance = this.b - other.b;

		return Math.sqrt((double) rDistance * rDistance + gDistance * gDistance + bDistance * bDistance);
	}
}
