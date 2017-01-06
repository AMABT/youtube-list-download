package com.example.malecu.youtubelistdownload;

import android.content.Context;
import android.provider.MediaStore;
import android.text.format.Time;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static okhttp3.internal.Util.UTF_8;

/**
 * Created by malecu on 05/01/17.
 */

public class YtRestClient {

    protected String TAG = "YtRestClient";
    protected OkHttpClient mOkHttpClient;
    protected String mApiUrl;

    public YtRestClient(Context context) {
        mOkHttpClient = new OkHttpClient().newBuilder().readTimeout(60, TimeUnit.SECONDS).build();
        mApiUrl = context.getString(R.string.api_url);
    }

    /**
     * Make Http call to API
     *
     * @param url               YoutubeUrl to get info for
     * @param onSuccessListener Object that implements method to call when the result is ready
     * @param onErrorListener   Object that implements method to call when there is an error
     * @return an object that can be canceled
     */
    public Cancellable getInfoAsync(String url, final OnSuccessListener<List<Video>> onSuccessListener, final OnErrorListener onErrorListener) {

        JSONObject json = new JSONObject();
        try {
            json.put("action", "getInfo");
            json.put("url", url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request.Builder builder = new Request.Builder();
        builder.url(mApiUrl);
        builder.post(RequestBody.create(MediaType.parse("application/json"), json.toString()));

        return new CancellableOkHttpAsync<>(
                builder.build(),
                new ResponseReader<List<Video>>() {
                    @Override
                    public List<Video> read(Response response) throws Exception {
                        if (response.code() == 200) {
                            InputStreamReader inputStreamReader = new InputStreamReader(response.body().byteStream(), UTF_8);

                            //Scanner s = new java.util.Scanner(inputStreamReader).useDelimiter("\\A");
                            //String result = s.hasNext() ? s.next() : "";

                            //Log.i(TAG, result);
                            //JsonReader reader = new JsonReader(new StringReader(result));
                            JsonReader reader = new JsonReader(inputStreamReader);

                            return new VideoReader().read(reader);
                        }
                        return null;
                    }
                },
                onSuccessListener,
                onErrorListener
        );
    }

    /**
     * Make Http call to API
     *
     * @param url               YoutubeUrl to get info for
     * @param onSuccessListener Object that implements method to call when the result is ready
     * @param onErrorListener   Object that implements method to call when there is an error
     * @return an object that can be canceled
     */
    public Cancellable downloadVideoAudio(String url, final OnSuccessListener<InputStream> onSuccessListener, final OnErrorListener onErrorListener) {

        JSONObject json = new JSONObject();
        try {
            json.put("action", "download");
            json.put("url", url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request.Builder builder = new Request.Builder();
        builder.url(mApiUrl);
        builder.post(RequestBody.create(MediaType.parse("application/json"), json.toString()));

        return new CancellableOkHttpAsync<>(
                builder.build(),
                new ResponseReader<InputStream>() {
                    @Override
                    public InputStream read(Response response) throws Exception {
                        if (response.code() == 200) {

                            return response.body().byteStream();
                        }
                        return null;
                    }
                },
                onSuccessListener,
                onErrorListener
        );
    }

    private static interface ResponseReader<E> {
        E read(Response response) throws Exception;
    }

    /**
     * Implement Cancelable for current protocol
     * @param <T>
     */
    private class CancellableOkHttpAsync<T> implements Cancellable {
        private Call mCall;

        public CancellableOkHttpAsync(final Request request,
                                      final ResponseReader<T> responseReader,
                                      final OnSuccessListener<T> successListener,
                                      final OnErrorListener errorListener) {
            try {
                mCall = mOkHttpClient.newCall(request);

                Log.d(TAG, String.format("started %s %s", request.method(), request.url()));

                // Attach methods to call the callbacks provided by action/fragment
                mCall.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        notifyFailure(e, request, errorListener);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            notifySuccess(response, request, successListener, responseReader);
                        } catch (Exception e) {
                            notifyFailure(e, request, errorListener);
                        }
                    }
                });
            } catch (Exception e) {
                notifyFailure(e, request, errorListener);
            }
        }


        @Override
        public void cancel() {
            if (mCall != null) {
                mCall.cancel();
            }
        }

        private void notifySuccess(Response response, Request request,
                                   OnSuccessListener<T> successListener, ResponseReader<T> responseReader) throws Exception {
            if (mCall.isCanceled()) {
                Log.d(TAG, String.format("completed, but cancelled %s %s", request.method(), request.url()));
            } else {
                Log.d(TAG, String.format("completed %s %s", request.method(), request.url()));
                successListener.onSuccess(responseReader.read(response));
            }
        }

        /**
         * Be careful because no callback is called in case of cancel, any action must be implemented after cancel()
         * @param e
         * @param request
         * @param errorListener
         */
        private void notifyFailure(Exception e, Request request, OnErrorListener errorListener) {
            if (mCall.isCanceled()) {
                Log.d(TAG, String.format("failed, but cancelled %s %s", request.method(), request.url()));
            } else {
                Log.e(TAG, String.format("failed %s %s", request.method(), request.url()), e);
                errorListener.onError(e instanceof ResourceException ? e : new ResourceException(e));
            }
        }
    }
}
