package io.github.orlisan.caputmundi.client.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

public class LituusEditBox extends EditBox {
    private final String regex;


    public LituusEditBox(Font font, Component narration, String regex) {
        super(font, narration);
        this.regex = regex;
    }

    public LituusEditBox(Font font, int x, int y, int width, int height, @Nullable EditBox oldBox, Component narration, String regex) {
        super(font, x, y, width, height, oldBox, narration);
        this.regex = regex;
    }

    public LituusEditBox(Font font, int x, int y, int width, int height, Component narration, String regex) {
        super(font, x, y, width, height, narration);
        this.regex = regex;
    }

    public LituusEditBox(Font font, int width, int height, Component narration, String regex) {
        super(font, width, height, narration);
        this.regex = regex;
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        char c = event.codepointAsString().charAt(0);
        if (!String.valueOf(c).matches(regex)) return false;
        return super.charTyped(event);
    }
}
