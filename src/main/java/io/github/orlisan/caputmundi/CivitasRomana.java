package io.github.orlisan.caputmundi;

public record CivitasRomana(int virtus, int fides, int pietas, int dignitas) {
    int media() {
        return (virtus + fides + pietas + dignitas) / 4;
    }
}
