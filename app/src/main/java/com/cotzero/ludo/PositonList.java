package com.cotzero.ludo;

import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

public class PositonList {

    int color;
    ArrayList<PositionModel> list;

    public PositonList(int color) {
        this.color = color;
        list= new ArrayList<>();
        list.add(new PositionModel(7,2));
        list.add(new PositionModel(7,3));
        list.add(new PositionModel(7,4));
        list.add(new PositionModel(7,5));
        list.add(new PositionModel(7,6));
        list.add(new PositionModel(6,7));
        list.add(new PositionModel(5,7));
        list.add(new PositionModel(4,7));
        list.add(new PositionModel(3,7));
        list.add(new PositionModel(2,7));
        list.add(new PositionModel(1,7));
        list.add(new PositionModel(1,8));
        list.add(new PositionModel(1,9));
        list.add(new PositionModel(2,9));
        list.add(new PositionModel(3,9));
        list.add(new PositionModel(4,9));
        list.add(new PositionModel(5,9));
        list.add(new PositionModel(6,9));
        list.add(new PositionModel(7,10));
        list.add(new PositionModel(7,11));
        list.add(new PositionModel(7,12));
        list.add(new PositionModel(7,13));
        list.add(new PositionModel(7,14));
        list.add(new PositionModel(7,15));
        list.add(new PositionModel(8,15));
        list.add(new PositionModel(9,15));
        list.add(new PositionModel(9,14));
        list.add(new PositionModel(9,13));
        list.add(new PositionModel(9,12));
        list.add(new PositionModel(9,11));
        list.add(new PositionModel(9,10));
        list.add(new PositionModel(10,9));
        list.add(new PositionModel(11,9));
        list.add(new PositionModel(12,9));
        list.add(new PositionModel(13,9));
        list.add(new PositionModel(14,9));
        list.add(new PositionModel(15,9));
        list.add(new PositionModel(15,8));
        list.add(new PositionModel(15,7));
        list.add(new PositionModel(14,7));
        list.add(new PositionModel(13,7));
        list.add(new PositionModel(12,7));
        list.add(new PositionModel(11,7));
        list.add(new PositionModel(10,7));
        list.add(new PositionModel(9,6));
        list.add(new PositionModel(9,5));
        list.add(new PositionModel(9,4));
        list.add(new PositionModel(9,3));
        list.add(new PositionModel(9,2));
        list.add(new PositionModel(9,1));
        list.add(new PositionModel(8,1));
        list.add(new PositionModel(7,1));
    }

    public List<PositionModel> getList() {

        List<PositionModel> path = new ArrayList<>();

        switch(color){

            case 1 :
                for(int i = 0;i<51;i++){
                    path.add(list.get(i));
                }
                path.add(new PositionModel(8,2));
                path.add(new PositionModel(8,3));
                path.add(new PositionModel(8,4));
                path.add(new PositionModel(8,5));
                path.add(new PositionModel(8,6));
                path.add(new PositionModel(8,7));
                break;


            case 2:

                for(int i = 13;i<52;i++){
                    path.add(list.get(i));
                }

                for(int i = 0;i<12;i++){
                    path.add(list.get(i));
                }

                path.add(new PositionModel(2,8));
                path.add(new PositionModel(3,8));
                path.add(new PositionModel(4,8));
                path.add(new PositionModel(5,8));
                path.add(new PositionModel(6,8));
                path.add(new PositionModel(7,8));

                break;

            case 3:

                for(int i = 26;i<52;i++){
                    path.add(list.get(i));
                }

                for(int i = 0;i<25;i++){
                    path.add(list.get(i));
                }

                path.add(new PositionModel(8,14));
                path.add(new PositionModel(8,13));
                path.add(new PositionModel(8,12));
                path.add(new PositionModel(8,11));
                path.add(new PositionModel(8,10));
                path.add(new PositionModel(8,9));

                break;

            case 4:

                for(int i = 39;i<52;i++){
                    path.add(list.get(i));
                }

                for(int i = 0;i<38;i++){
                    path.add(list.get(i));
                }
                path.add(new PositionModel(14,8));
                path.add(new PositionModel(13,8));
                path.add(new PositionModel(12,8));
                path.add(new PositionModel(11,8));
                path.add(new PositionModel(10,8));
                path.add(new PositionModel(9,8));


                break;

            case 5:
                path.add(new PositionModel(7,2));
                path.add(new PositionModel(3,7));
                path.add(new PositionModel(2,9));
                path.add(new PositionModel(7,13));
                path.add(new PositionModel(9,14));
                path.add(new PositionModel(13,9));
                path.add(new PositionModel(14,7));
                path.add(new PositionModel(9,3));

        }


        return path;
    }
}
