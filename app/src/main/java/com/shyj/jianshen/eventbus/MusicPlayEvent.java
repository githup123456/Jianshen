package com.shyj.jianshen.eventbus;

public class MusicPlayEvent {
    boolean isPlay;
    public MusicPlayEvent(boolean isPlay){
        this.isPlay = isPlay;
    }

    public boolean isPlay() {
        return isPlay;
    }
}
