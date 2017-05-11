package com.example.rmontoya.busdata.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.rmontoya.busdata.R;
import com.example.rmontoya.busdata.bus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.BusViewHolder> {

    public ImageAdapter() {
    }

    @Override
    public BusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.bus_row, parent, false);

        return new BusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BusViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }


    class BusViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.row_image)
        ImageView imageRow;
        private Subscription imageRowSubscription;

        BusViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            setupObserver();
        }

        private void setupObserver() {
//            Observer<Object> rowObserver = new Observer<Object>() {
//                @Override
//                public void onCompleted() {
//
//                }
//
//                @Override
//                public void onError(Throwable e) {
//
//                }
//
//                @Override
//                public void onNext(Object busObject) {
//                    imageRowSubscription = Observable.just(busObject)
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .filter(o -> o instanceof Bitmap)
//                            .subscribe(o -> {
//                                imageRow.setImageBitmap((Bitmap) o);
//                                unsubscribeRowObserver();
//                            });
//                }
//            };
            EventBus.getInstance().subscribe(busObject -> imageRowSubscription = Observable.just(busObject)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter(o -> o instanceof Bitmap)
                    .subscribe(o -> {
                        imageRow.setImageBitmap((Bitmap) o);
                        unsubscribeRowObserver();
                    }));
        }

        private void unsubscribeRowObserver() {
            imageRowSubscription.unsubscribe();
        }

    }

}
