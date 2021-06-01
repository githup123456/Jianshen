package com.shyj.jianshen.eventbus;

public class ActionPlayEvent{

    private boolean isQuick;

    public ActionPlayEvent(boolean isQuick){
        this.isQuick = isQuick;
    }

    public boolean isQuick() {
        return isQuick;
    }
}
