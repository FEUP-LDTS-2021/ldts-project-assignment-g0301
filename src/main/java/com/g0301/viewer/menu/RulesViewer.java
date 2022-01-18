package com.g0301.viewer.menu;

import com.g0301.gui.Gui;
import com.g0301.model.Button;
import com.g0301.viewer.ButtonViewer;

import java.io.IOException;
import java.util.List;

public class RulesViewer extends StateViewer {

    public RulesViewer(Gui gui, List<Button> buttons) {
        super(gui, buttons);
    }

    @Override
    public void draw() throws IOException {
        gui.clear();
        gui.drawInstructions();
        drawButtons(buttons, new ButtonViewer());
        gui.refresh();
    }
}
