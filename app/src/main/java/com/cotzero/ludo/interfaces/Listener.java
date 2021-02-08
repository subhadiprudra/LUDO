package com.cotzero.ludo.interfaces;

public interface Listener {
    void onPositionChange(int a);
    void onCompleteGo(int color, int xPosition,int yPosition, int index, int cursorIndex,Boolean isGoingHome);
    void onBeforeGo(int color, int xPosition,int yPosition, int index, int cursorIndex,Boolean isGoingHome);

}
