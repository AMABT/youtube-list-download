package com.example.malecu.youtubelistdownload.Net;

import java.util.concurrent.Callable;

public interface CancellableCallable<T> extends Callable<T>, Cancellable {
}
