package io.github.orlisan.caputmundi;

public record DignitasRomana(int virtus, int fides, int pietas) {
    int media() {
        return (virtus + fides + pietas) / 4;
    }
}
