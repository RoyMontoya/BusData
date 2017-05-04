package com.example.rmontoya.busdata.bus;

import rx.subjects.PublishSubject;

public class EventBus {

    private static PublishSubject<Object> eventObservable = PublishSubject.create();

    public static PublishSubject<Object> getInstance() {
        return eventObservable;
    }

//    public static void startObservable(Bitmap bitmap) {
//        eventObservable.just(bitmap);
//    }

}
