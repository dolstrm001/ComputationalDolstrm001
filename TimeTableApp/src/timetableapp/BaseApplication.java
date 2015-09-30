package timetableapp;

import timetableapp.Gui.GuiHelper;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import processing.core.PApplet;
import timetableapp.util.AppState;

public class BaseApplication extends PApplet {

    GuiHelper guiHelper;

    @Override
    public void settings() {
        height = displayHeight - 200;
        width = displayWidth - 500;
        size(width, height);
    }

    @Override
    public void setup() {
        AppState.getInstance().setApp(this);
        AppState.getInstance().setCp5(new ControlP5(this));
        AppState.getInstance().setFont();
        frameRate(60);
        guiHelper = new GuiHelper();
        background(AppState.getInstance().getBackgroundcolor());
    }

    @Override
    public void draw() {
        guiHelper.draw();
        //text("some nice OpenSans text", 150, 150);
    }

    public void controlEvent(ControlEvent evt) {
        guiHelper.controlEvent(evt);
    }
}
