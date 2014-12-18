package com.quantumfin.daori.rxjavatester;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.events.OnTextChangeEvent;
import rx.android.observables.ViewObservable;
import rx.functions.Func2;


public class MainActivity extends ActionBarActivity {

    private TextView helloWorld;
    private Button loginButton;
    private TextView username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sample 1
        helloWorld = (TextView) findViewById(R.id.sample1);
        Observable<String> observer = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello Master!!!");
                subscriber.onCompleted();
            }
        });
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                helloWorld.setText(s);
            }
        };
        observer.subscribe(subscriber);
        //-----

        // Sample Login with validation using rxJava
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setEnabled(false);

        Observable<OnTextChangeEvent> usernameChangeEvent = ViewObservable.text(username);
        Observable<OnTextChangeEvent> passwordChangeEvent = ViewObservable.text(password);

        Observable<Boolean> observableResult = Observable.combineLatest(usernameChangeEvent, passwordChangeEvent,
                new Func2<OnTextChangeEvent, OnTextChangeEvent, Boolean>() {
                    @Override
                    public Boolean call(OnTextChangeEvent usernameObservable, OnTextChangeEvent passwordObservable) {
                        if(!usernameObservable.text.toString().equals("") &&
                                !passwordObservable.text.toString().equals("")){
                            return true;
                        }
                        return false;
                    }
                });

        Subscriber<Boolean> buttonSubscriber = new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean state) {
                loginButton.setEnabled(state);
            }
        };
        observableResult.subscribe(buttonSubscriber);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
