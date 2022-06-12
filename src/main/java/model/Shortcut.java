package model;

import java.io.Serializable;

public class Shortcut implements Serializable {

    public String name;
    public ShortcutType type;
    public String command;

    public Shortcut() {
        this.name = "";
        this.type = ShortcutType.SQL;
        this.command = "";
    }


}

