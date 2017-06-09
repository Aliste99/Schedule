package com.example.thirtyseven.myschedule;

/**
 * Created by ThirtySeven on 07.06.2017.
 */

class DayHelper {

    public int dayToInt(String s) {
        int result = 0;
        switch (s){
            case "monday":
            case "понедельник":
            case "Понедельник":
            case "пн":
            case "Пн":
            case "mon":
                result = 0;
                break;

            case "tuesday":
            case "вторник":
            case "Вторник":
            case "Вт":
            case "вт":
            case "tues":
                result = 1;
                break;


        }
        return result;
    }
}
