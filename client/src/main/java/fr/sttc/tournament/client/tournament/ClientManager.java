package fr.sttc.tournament.client.tournament;

import fr.sttc.tournament.client.tournament.game.Game;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class ClientManager {


    private final static Logger logger = LoggerFactory.getLogger(ClientManager.class);

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(1000, TimeUnit.MILLISECONDS)
            .writeTimeout(1000, TimeUnit.MILLISECONDS)
            .readTimeout(1000, TimeUnit.MILLISECONDS)
            .build();
    private final String clientUrl;

    public ClientManager(@Value("${client.url}") String clientUrl) {
        this.clientUrl = clientUrl;
    }

    public Boolean register(Game game, String gameId, String team, String serverUrlBase) {

        try {
            String serverUrlFull = String.format("http://%s/tournament/%s/register/%s/%s/%s",
                    serverUrlBase,
                    game.toString().toLowerCase(),
                    gameId,
                    team,
                    this.clientUrl);
            client.newCall(getRequestFromEvent(serverUrlFull)).execute();
        } catch (IOException e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    public Boolean start(Game game, String gameId, String serverUrlBase) {
        try {
            String serverUrlFull = String.format("http://%s/tournament/%s/start/%s",
                    serverUrlBase,
                    game.toString().toLowerCase(),
                    gameId);
            client.newCall(getRequestFromEvent(serverUrlFull)).execute();
        } catch (IOException e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    private Request getRequestFromEvent(String url) {

        return new Request.Builder()
                .url(url)
                .build();
    }
}
