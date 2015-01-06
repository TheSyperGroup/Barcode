package com.jikabao.android.common.util;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;


/**
 * A Logback appender that will display a message on the screen. Used to show up noteworthy problems
 * during development and testing, that would otherwise not be explicitly visible to the end-user.
 * Should be invoked only for infrequent ERROR or WARN level events. See logback.xml for the configuration.
 */
public class LoggerNotificationAppender extends AppenderBase<ILoggingEvent> {

    private static final String TAG = LoggerNotificationAppender.class.getName();
    private static final String FORMATTED_MESSAGE_KEY = "formatted_message";

    private static Context context;
    private static Handler handler;

    /**
     * Sets the Application object that will be used to display messages.
     * This must be called before the first message logged using this appender.
     */
    public static void init(Application app) {
        context = app.getApplicationContext();

        // Create a handler that will display Toast messages on the main UI thread.
        handler = new Handler(app.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                Toast.makeText(context, msg.getData().getString(FORMATTED_MESSAGE_KEY), Toast.LENGTH_LONG).show();
            }
        };

    }

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        // Echo to logcat so we can see there that the logging event was received.
        Log.println(Log.INFO, TAG, String.format("%s - %s:\n%s",
                iLoggingEvent.getLoggerName(), iLoggingEvent.getLevel(), iLoggingEvent.getFormattedMessage()));

        // Send a message containing the log message to the handler.
        Message message = handler.obtainMessage();
        message.getData().putString(FORMATTED_MESSAGE_KEY, iLoggingEvent.getFormattedMessage());
        message.sendToTarget();
    }

}
