package com.giaquino.todo.common.app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import java.util.ArrayList;
import java.util.List;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * @author Gian Darren Azriel Aquino.
 */
public abstract class BaseFragment extends Fragment {

    private final static Handler MAIN_THREAD_HANDLER = new Handler(Looper.getMainLooper());

    private volatile boolean isRunning;

    private Unbinder unbinder;
    private List<Runnable> runnables = new ArrayList<>();
    private CompositeSubscription subscriptions = new CompositeSubscription();

    protected abstract void initialize();

    protected void runOnUIThreadIfAlive(@NonNull final Runnable runnable) {
        if (!isAlive()) {
            Timber.d("Fragment not alive, defer running runnable.");
            return;
        }
        if (!isRunning) {
            runnables.add(runnable);
            Timber.d("Fragment is not running, caching runnable. cache count %d", runnables.size());
            return;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
            return;
        }
        MAIN_THREAD_HANDLER.post(runnable::run);
    }

    public void addSubscriptionToUnsubscribe(@NonNull Subscription subscription) {
        subscriptions.add(subscription);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initialize();
    }

    @Override public void onDetach() {
        super.onDetach();
        subscriptions.clear();
        unbinder.unbind();
    }

    @Override public void onResume() {
        super.onResume();
        isRunning = true;
        for (Runnable runnable : runnables) {
            runnable.run();
        }
        runnables.clear();
    }

    @Override public void onPause() {
        super.onPause();
        isRunning = false;
    }

    protected boolean isAlive() {
        return isAdded() && !isDetached() && !isRemoving();
    }
}
