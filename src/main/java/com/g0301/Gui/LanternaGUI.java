package com.g0301.Gui;

import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.AWTTerminalFrame;
import com.g0301.model.Position;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public class LanternaGUI implements Gui {
    private final TerminalScreen screen;
    private final int width;
    private final int height;

    public LanternaGUI(int width, int height) throws IOException, FontFormatException {
        AWTTerminalFontConfiguration fontConfiguration = loadTronFont();
        Terminal terminal = createTerminal(width, height, fontConfiguration);
        this.screen = createScreen(terminal);
        this.height = height;
        this.width = width;
        addCloseScreenListener();
    }

    /**
     * @brief Creates the screen
     * @param terminal Terminal that's the base for the screen
     * @return The created screen
     */
    public TerminalScreen createScreen(Terminal terminal) throws IOException {
        final TerminalScreen terminalScreen = new TerminalScreen(terminal);

        terminalScreen.setCursorPosition(null);
        terminalScreen.startScreen();
        terminalScreen.doResizeIfNecessary();

        return terminalScreen;
    }

    /**
     * @brief Creates the terminal
     * @param width Terminal's width
     * @param height Terminal's height
     * @param fontConfiguration Font to be used in the terminal
     * @return The created terminal
     */
    public Terminal createTerminal(int width, int height, AWTTerminalFontConfiguration fontConfiguration) throws IOException {
        TerminalSize terminalSize = new TerminalSize(width, height);
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory()
                .setInitialTerminalSize(terminalSize);
        terminalFactory.setForceAWTOverSwing(true);
        terminalFactory.setTerminalEmulatorFontConfiguration(fontConfiguration);
        Terminal terminal = terminalFactory.createTerminal();
        return terminal;
    }

    /**
     * @brief Loads a custom font to the terminal
     * @return The applied font configuration
     */
    public AWTTerminalFontConfiguration loadTronFont() throws IOException, FontFormatException {
        File tronFontFile = new File("src/main/resources/fonts/square.ttf");
        Font font = Font.createFont(Font.TRUETYPE_FONT, tronFontFile);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(font);

        Font loadedFont = font.deriveFont(Font.PLAIN, 10);
        return AWTTerminalFontConfiguration.newInstance(loadedFont);
    }

    /**
     * @brief Creates the text graphics
     * @return A screen with the text graphics created
     */
    @Override
    public TextGraphics createTextGraphics() {
        return screen.newTextGraphics();
    }

    /**
     * @return True if the terminal is displayable, false otherwise
     */
    @Override
    public boolean isActive() {
        return ((AWTTerminalFrame) screen.getTerminal()).isDisplayable();
    }

    /**
     * @return The GUI's height
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * @return The GUI's width
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * @breif Clears the screen
     */
    @Override
    public void clear() {
        screen.clear();
    }

    /**
     * @brief Closes the screen
     */
    @Override
    public void close() throws IOException {
        screen.close();
    }

    /**
     * @breif Refreshes the screen
     */
    @Override
    public void refresh() throws IOException {
        screen.refresh();
    }

    /**
     * @brief Draws the screen's background
     * @param textGraphics
     * @param color
     */
    @Override
    public void drawBackground(TextGraphics textGraphics, String color) {
        textGraphics.setBackgroundColor(TextColor.Factory.fromString(color));
        textGraphics.fillRectangle(new TerminalPosition(0, 0), new TerminalSize(width, height), ' ');
    }

    /**
     * @brief Draws a car in the screen
     * @param position Position in which to draw the car
     * @param color The car's color
     */
    @Override
    public void drawCar(Position position, String color) {
        TextGraphics graphics = screen.newTextGraphics();
        graphics.setBackgroundColor(TextColor.Factory.fromString(color));
        drawElement(graphics, position, color, "C");
    }

    /**
     * @brief Draws an element in the screen
     * @param graphics Graphics that permit the drawing
     * @param position The position in which to draw the element
     * @param color The element's color
     * @param text The element's representation
     */
    @Override
    public void drawElement(TextGraphics graphics, Position position, String color, String text) {
        graphics.setForegroundColor(TextColor.Factory.fromString(color));
        graphics.enableModifiers(SGR.BOLD);
        graphics.putString(position.getX(), position.getY(), text);
    }

    /**
     * @breif Draws a wall
     * @param position Position in which to draw the wall
     * @param color The wall's color
     */
    @Override
    public void drawWall(Position position, String color) {
        TextGraphics graphics = screen.newTextGraphics();
        graphics.setBackgroundColor(TextColor.Factory.fromString(color));
        drawElement(graphics, position, color, "*");
    }

    /**
     * @breif Draws a car's tail
     * @param position Position in which to draw the tail
     * @param color The tail's color
     */
    @Override
    public void drawTrail(Position position, String color) {
        TextGraphics graphics = screen.newTextGraphics();
        graphics.setBackgroundColor(TextColor.Factory.fromString(color));
        drawElement(graphics, position, color, "-");
    }

    /**
     * @return The screen
     */
    public TerminalScreen getScreen() {
        return screen;
    }

    /**
     * @brief Adds a close screen listener, so when the close button of the window is pressed, the window is closed
     */
    private void addCloseScreenListener() {
        ((AWTTerminalFrame) screen.getTerminal()).addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ((AWTTerminalFrame) screen.getTerminal()).dispose();
                System.exit(0);
            }
        });
    }

    /**
     * @brief Adds a keyboard listener to handle user input
     * @param observer The keyboard listener
     */
    public void addKeyboardListener(KeyboardObserver observer) {
        ((AWTTerminalFrame) screen.getTerminal()).getComponent(0).addKeyListener(observer);
    }
}
