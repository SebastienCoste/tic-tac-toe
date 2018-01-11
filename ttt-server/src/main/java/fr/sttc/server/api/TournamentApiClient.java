package fr.sttc.server.api;

import fr.sttc.server.tournament.board.Action;
import fr.sttc.server.tournament.client.EventClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class TournamentApiClient {

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(500, TimeUnit.MILLISECONDS)
            .writeTimeout(500, TimeUnit.MILLISECONDS)
            .readTimeout(500, TimeUnit.MILLISECONDS)
            .build();


    public <T extends Action> T sendEvent(EventClient event, Function<String, T> caster){

        try {
            Response response = client.newCall(getRequestFromEvent(event)).execute();
            return caster == null ? null : caster.apply(response.body() == null ? null : response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    private Request getRequestFromEvent(EventClient event){

        return  new Request.Builder()
                .url(event.request)
                .build();
    }
}
