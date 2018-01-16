package fr.sttc.server.api;

import fr.sttc.server.tournament.board.Action;
import fr.sttc.server.tournament.client.EventClient;
import fr.sttc.server.tournament.client.TournamentClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class TournamentApiClient {

    private final static Logger logger = LoggerFactory.getLogger(TournamentApiClient.class);

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(500, TimeUnit.MILLISECONDS)
            .writeTimeout(500, TimeUnit.MILLISECONDS)
            .readTimeout(500, TimeUnit.MILLISECONDS)
            .build();


    public <T extends Action> T sendEvent(EventClient event, Function<String, T> caster){

        try {
            logger.info(String.format("call server -> client: %s", event.request));
            Response response = client.newCall(getRequestFromEvent(event)).execute();
            T result = caster == null ? null : caster.apply(response.body() == null ? null : response.body().string());
            logger.info(String.format("call server -> client: response: %s", result == null || result.value() == null ? "" : result.value().toString()));
            return result;
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
