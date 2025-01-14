package timetableapp.util;

import timetableapp.util.state.AppState;
import java.io.File;
import java.util.concurrent.Callable;
import processing.core.PApplet;
import processing.data.Table;
import timetableapp.gui.Dialog;

public class Parser implements Callable<Table> {

    private PApplet app = AppState.getInstance().getApp();
    private File file;
    private String extension;
    private Table table;

    public Parser(File file) {
        this.file = file;
        extension = getExtension(file);
    }

    public Table parse() {
        table = null;
        if ("txt".equals(extension)) {
            extension = new Dialog().optionDialog(new String[]{"csv", "tsv"}, "is the data cvs or tsv?");
        }

        switch (extension) {
            case ("tsv"):
            case ("tab"):
                table = app.loadTable(file.getAbsolutePath(), "header, tsv");
                break;
            case ("csv"):
            case ("ics"):
                handleUnimplementedExtension(extension);
        }
        return table;
    }

    private String getExtension(File file) {
        int i = file.getAbsolutePath().lastIndexOf('.');
        if (i > 0) {
            return file.getAbsolutePath().substring(i + 1);
        }
        return null;
    }

    public void handleUnimplementedExtension(String extension) {
        new Dialog("not implemented yet for " + extension, Dialog.WARNING_MESSAGE);
        AppState.getInstance().getFileLoadedStateObserver().resetValue();
    }

    @Override
    public Table call() throws Exception {
        return parse();
    }
}
