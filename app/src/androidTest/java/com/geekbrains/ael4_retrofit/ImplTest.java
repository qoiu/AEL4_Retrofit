package com.geekbrains.ael4_retrofit;



import androidx.test.runner.AndroidJUnit4;

import com.geekbrains.ael4_retrofit.dagger.DaggerNetModules;
import com.geekbrains.ael4_retrofit.model.RepoUsers;
import com.google.gson.stream.MalformedJsonException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import dagger.Component;
import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.subscribers.TestSubscriber;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.HttpException;

@Component(modules = {DaggerNetModules.class})
interface TestNetworkComponent {
    void inject(ImplTest test);
}

class TestDaggerNetModule extends DaggerNetModules{
    MockWebServer server;
    TestDaggerNetModule(MockWebServer server){
        this.server = server;
    }
    @Override
    public String provideEndpoint() {
        System.out.println(server.url("/").toString());
        return server.url("/").toString();
    }
}

@RunWith(AndroidJUnit4.class)
public class ImplTest {
    private static MockWebServer mockWebServer;

    private static String user="qoiu";
    private static String imgUrl="https://img.url.jpg";

    @Inject
    Single<List<RepoUsers>> request;
    @Before
    public void setupServer() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        DaggerTestNetworkComponent.builder()
                .daggerNetModules(new TestDaggerNetModule(mockWebServer))
                .build()
                .inject(this);
    }


    private MockResponse errorResponse(int code, String message){
        return new MockResponse().setResponseCode(code).setBody(message);
    }

    @Test
    public void errorPageNotFound(){
        mockWebServer.enqueue(errorResponse(404,pageNotFoundMsg()));
        TestSubscriber<Integer> subscriber = getBooleanTestSubscriber();
        subscriber.awaitTerminalEvent();
        subscriber.assertError(HttpException.class);
        subscriber.dispose();
    }

    @Test
    public void errorWrongBody(){
        mockWebServer.enqueue(createIncorrectMockResponse());
        TestSubscriber<Integer> subscriber = getBooleanTestSubscriber();
        subscriber.awaitTerminalEvent();
        subscriber.assertError(MalformedJsonException.class);
        subscriber.dispose();
    }

    @Test
    public void correctResponse(){
        mockWebServer.enqueue(createCorrectMockResponse());
        TestSubscriber<Integer> subscriber = getBooleanTestSubscriber();
        subscriber.awaitTerminalEvent();
        subscriber.assertComplete();
        subscriber.dispose();
    }


    private Subscription newSubscription(){
        return new Subscription() {
            @Override
            public void request(long n) {

            }

            @Override
            public void cancel() {

            }
        };
    }

    private TestSubscriber<Integer> getBooleanTestSubscriber() {
        TestSubscriber<Integer> subscriber=TestSubscriber.create();
        subscriber.onSubscribe(newSubscription());
        request.subscribeWith(new DisposableSingleObserver<List<RepoUsers>>() {
            @Override
            public void onSuccess(List<RepoUsers> retrofitModels) {
                if(retrofitModels.size() == 0){
                    subscriber.onNext(200);
                    subscriber.onComplete();
                    return;
                }
                RepoUsers model = retrofitModels.get(0);
                boolean equal = model.getUser().equals(user) &&
                        model.getImg_url().equals(imgUrl);
                subscriber.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                subscriber.onError(e);
            }
        });
        return subscriber;
    }

    private String pageNotFoundMsg(){
        return "{\n\"message\": \"Not found\",\n\"documentation_url\": \"http://github\"\n}";
    }

    private MockResponse createIncorrectMockResponse(){
        return new MockResponse().setBody("[{\n" +
                "\"login\": \"" + user + "\",\n"+
                "}]");
    }
    private MockResponse createCorrectMockResponse(){
        return new MockResponse().setBody("[{\n" +
                "\"login\": \"" + user + "\",\n" +
                "\"avatar_url\": \"" + imgUrl + "\"\n" +
                "}]");
    }

    @After
    public void shutdownServer() throws IOException {
        mockWebServer.shutdown();
    }
}
